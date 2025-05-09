/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.generic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.util.ByteSequence;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.stream.Streams;

/**
 * This class is a container for a list of <a href="Instruction.html">Instruction</a> objects. Instructions can be appended, inserted, moved, deleted, and so
 * on. Instructions are being wrapped into <a href="InstructionHandle.html">InstructionHandles</a> objects that are returned upon append/insert operations. They
 * give the user (read only) access to the list structure, such that it can be traversed and manipulated in a controlled way.
 * <p>
 * A list is finally dumped to a byte code array with <a href="#getByteCode()">getByteCode</a>.
 * </p>
 *
 * @see Instruction
 * @see InstructionHandle
 * @see BranchHandle
 */
public class InstructionList implements Iterable<InstructionHandle> {

    /**
     * Find the target instruction (handle) that corresponds to the given target position (byte code offset).
     *
     * @param ihs array of instruction handles, i.e. il.getInstructionHandles()
     * @param pos array of positions corresponding to ihs, i.e. il.getInstructionPositions()
     * @param count length of arrays
     * @param target target position to search for
     * @return target position's instruction handle if available
     */
    public static InstructionHandle findHandle(final InstructionHandle[] ihs, final int[] pos, final int count, final int target) {
        if (ihs != null && pos != null) {
            int l = 0;
            int r = count - 1;
            /*
             * Do a binary search since the pos array is orderd.
             */
            do {
                final int i = l + r >>> 1;
                final int j = pos[i];
                if (j == target) {
                    return ihs[i];
                }
                if (target < j) {
                    r = i - 1;
                } else {
                    l = i + 1;
                }
            } while (l <= r);
        }
        return null;
    }

    private InstructionHandle start;
    private InstructionHandle end;
    private int length; // number of elements in list

    private int[] bytePositions; // byte code offsets corresponding to instructions

    private List<InstructionListObserver> observers;

    /**
     * Create (empty) instruction list.
     */
    public InstructionList() {
    }

    /**
     * Create instruction list containing one instruction.
     *
     * @param i initial instruction
     */
    public InstructionList(final BranchInstruction i) {
        append(i);
    }

    /**
     * Initialize instruction list from byte array.
     *
     * @param code byte array containing the instructions
     */
    public InstructionList(final byte[] code) {
        int count = 0; // Contains actual length
        final int[] pos;
        final InstructionHandle[] ihs;
        try (ByteSequence bytes = new ByteSequence(code)) {
            ihs = new InstructionHandle[code.length];
            pos = new int[code.length]; // Can't be more than that
            /*
             * Pass 1: Create an object for each byte code and append them to the list.
             */
            while (bytes.available() > 0) {
                // Remember byte offset and associate it with the instruction
                final int off = bytes.getIndex();
                pos[count] = off;
                /*
                 * Reads one instruction from the byte stream, the byte position is set accordingly.
                 */
                final Instruction i = Instruction.readInstruction(bytes);
                final InstructionHandle ih;
                if (i instanceof BranchInstruction) {
                    ih = append((BranchInstruction) i);
                } else {
                    ih = append(i);
                }
                ih.setPosition(off);
                ihs[count] = ih;
                count++;
            }
        } catch (final IOException e) {
            throw new ClassGenException(e.toString(), e);
        }
        bytePositions = Arrays.copyOf(pos, count); // Trim to proper size
        /*
         * Pass 2: Look for BranchInstruction and update their targets, i.e., convert offsets to instruction handles.
         */
        for (int i = 0; i < count; i++) {
            if (ihs[i] instanceof BranchHandle) {
                final BranchInstruction bi = (BranchInstruction) ihs[i].getInstruction();
                int target = bi.getPosition() + bi.getIndex(); /*
                                                                * Byte code position: relative -> absolute.
                                                                */
                // Search for target position
                InstructionHandle ih = findHandle(ihs, pos, count, target);
                if (ih == null) {
                    throw new ClassGenException("Couldn't find target for branch: " + bi);
                }
                bi.setTarget(ih); // Update target
                // If it is a Select instruction, update all branch targets
                if (bi instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    final Select s = (Select) bi;
                    final int[] indices = s.getIndices();
                    for (int j = 0; j < indices.length; j++) {
                        target = bi.getPosition() + indices[j];
                        ih = findHandle(ihs, pos, count, target);
                        if (ih == null) {
                            throw new ClassGenException("Couldn't find target for switch: " + bi);
                        }
                        s.setTarget(j, ih); // Update target
                    }
                }
            }
        }
    }

    /**
     * Initialize list with (nonnull) compound instruction. Consumes argument list, i.e., it becomes empty.
     *
     * @param c compound instruction (list)
     */
    public InstructionList(final CompoundInstruction c) {
        append(c.getInstructionList());
    }

    /**
     * Create instruction list containing one instruction.
     *
     * @param i initial instruction
     */
    public InstructionList(final Instruction i) {
        append(i);
    }

    /**
     * Add observer for this object.
     */
    public void addObserver(final InstructionListObserver o) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(o);
    }

    /**
     * Append a branch instruction to the end of this list.
     *
     * @param i branch instruction to append
     * @return branch instruction handle of the appended instruction
     */
    public BranchHandle append(final BranchInstruction i) {
        final BranchHandle ih = BranchHandle.getBranchHandle(i);
        append(ih);
        return ih;
    }

    /**
     * Append a compound instruction.
     *
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append(final CompoundInstruction c) {
        return append(c.getInstructionList());
    }

    /**
     * Append an instruction to the end of this list.
     *
     * @param i instruction to append
     * @return instruction handle of the appended instruction
     */
    public InstructionHandle append(final Instruction i) {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(i);
        append(ih);
        return ih;
    }

    /**
     * Append a compound instruction, after instruction i.
     *
     * @param i Instruction in list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append(final Instruction i, final CompoundInstruction c) {
        return append(i, c.getInstructionList());
    }

    /**
     * Append a single instruction j after another instruction i, which must be in this list of course!
     *
     * @param i Instruction in list
     * @param j Instruction to append after i in list
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append(final Instruction i, final Instruction j) {
        return append(i, new InstructionList(j));
    }

    /**
     * Append another list after instruction i contained in this list. Consumes argument list, i.e., it becomes empty.
     *
     * @param i where to append the instruction list
     * @param il Instruction list to append to this one
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append(final Instruction i, final InstructionList il) {
        final InstructionHandle ih;
        if ((ih = findInstruction2(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        return append(ih, il);
    }

    /**
     * Append an instruction to the end of this list.
     *
     * @param ih instruction to append
     */
    private void append(final InstructionHandle ih) {
        if (isEmpty()) {
            start = end = ih;
            ih.setNext(ih.setPrev(null));
        } else {
            end.setNext(ih);
            ih.setPrev(end);
            ih.setNext(null);
            end = ih;
        }
        length++; // Update length
    }

    /**
     * Append an instruction after instruction (handle) ih contained in this list.
     *
     * @param ih where to append the instruction list
     * @param i Instruction to append
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public BranchHandle append(final InstructionHandle ih, final BranchInstruction i) {
        final BranchHandle bh = BranchHandle.getBranchHandle(i);
        final InstructionList il = new InstructionList();
        il.append(bh);
        append(ih, il);
        return bh;
    }

    /**
     * Append a compound instruction.
     *
     * @param ih where to append the instruction list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append(final InstructionHandle ih, final CompoundInstruction c) {
        return append(ih, c.getInstructionList());
    }

    /**
     * Append an instruction after instruction (handle) ih contained in this list.
     *
     * @param ih where to append the instruction list
     * @param i Instruction to append
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append(final InstructionHandle ih, final Instruction i) {
        return append(ih, new InstructionList(i));
    }

    /**
     * Append another list after instruction (handle) ih contained in this list. Consumes argument list, i.e., it becomes
     * empty.
     *
     * @param ih where to append the instruction list
     * @param il Instruction list to append to this one
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append(final InstructionHandle ih, final InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        final InstructionHandle next = ih.getNext();
        final InstructionHandle ret = il.start;
        ih.setNext(il.start);
        il.start.setPrev(ih);
        il.end.setNext(next);
        if (next != null) {
            next.setPrev(il.end);
        } else {
            end = il.end; // Update end ...
        }
        length += il.length; // Update length
        il.clear();
        return ret;
    }

    /**
     * Append another list to this one. Consumes argument list, i.e., it becomes empty.
     *
     * @param il list to append to end of this list
     * @return instruction handle of the <B>first</B> appended instruction
     */
    public InstructionHandle append(final InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (il.isEmpty()) {
            return null;
        }
        if (isEmpty()) {
            start = il.start;
            end = il.end;
            length = il.length;
            il.clear();
            return start;
        }
        return append(end, il); // was end.instruction
    }

    private void clear() {
        start = end = null;
        length = 0;
    }

    public boolean contains(final Instruction i) {
        return findInstruction1(i) != null;
    }

    public boolean contains(final InstructionHandle i) {
        if (i == null) {
            return false;
        }
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            if (ih == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return complete, i.e., deep copy of this list
     */
    public InstructionList copy() {
        final Map<InstructionHandle, InstructionHandle> map = new HashMap<>();
        final InstructionList il = new InstructionList();
        /*
         * Pass 1: Make copies of all instructions, append them to the new list and associate old instruction references with
         * the new ones, i.e., a 1:1 mapping.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            final Instruction i = ih.getInstruction();
            final Instruction c = i.copy(); // Use clone for shallow copy
            if (c instanceof BranchInstruction) {
                map.put(ih, il.append((BranchInstruction) c));
            } else {
                map.put(ih, il.append(c));
            }
        }
        /*
         * Pass 2: Update branch targets.
         */
        InstructionHandle ih = start;
        InstructionHandle ch = il.start;
        while (ih != null) {
            final Instruction i = ih.getInstruction();
            final Instruction c = ch.getInstruction();
            if (i instanceof BranchInstruction) {
                final BranchInstruction bi = (BranchInstruction) i;
                final BranchInstruction bc = (BranchInstruction) c;
                final InstructionHandle itarget = bi.getTarget(); // old target
                // New target is in hash map
                bc.setTarget(map.get(itarget));
                if (bi instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    final InstructionHandle[] itargets = ((Select) bi).getTargets();
                    final InstructionHandle[] ctargets = ((Select) bc).getTargets();
                    for (int j = 0; j < itargets.length; j++) { // Update all targets
                        ctargets[j] = map.get(itargets[j]);
                    }
                }
            }
            ih = ih.getNext();
            ch = ch.getNext();
        }
        return il;
    }

    /**
     * Remove instruction from this list. The corresponding Instruction handles must not be reused!
     *
     * @param i instruction to remove
     */
    public void delete(final Instruction i) throws TargetLostException {
        final InstructionHandle ih;
        if ((ih = findInstruction1(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        delete(ih);
    }

    /**
     * Remove instructions from instruction 'from' to instruction 'to' contained in this list. The user must ensure that
     * 'from' is an instruction before 'to', or risk havoc. The corresponding Instruction handles must not be reused!
     *
     * @param from where to start deleting (inclusive)
     * @param to where to end deleting (inclusive)
     */
    public void delete(final Instruction from, final Instruction to) throws TargetLostException {
        final InstructionHandle fromIh;
        final InstructionHandle toIh;
        if ((fromIh = findInstruction1(from)) == null) {
            throw new ClassGenException("Instruction " + from + " is not contained in this list.");
        }
        if ((toIh = findInstruction2(to)) == null) {
            throw new ClassGenException("Instruction " + to + " is not contained in this list.");
        }
        delete(fromIh, toIh);
    }

    /**
     * Remove instruction from this list. The corresponding Instruction handles must not be reused!
     *
     * @param ih instruction (handle) to remove
     */
    public void delete(final InstructionHandle ih) throws TargetLostException {
        remove(ih.getPrev(), ih.getNext());
    }

    /**
     * Remove instructions from instruction 'from' to instruction 'to' contained in this list. The user must ensure that
     * 'from' is an instruction before 'to', or risk havoc. The corresponding Instruction handles must not be reused!
     *
     * @param from where to start deleting (inclusive)
     * @param to where to end deleting (inclusive)
     */
    public void delete(final InstructionHandle from, final InstructionHandle to) throws TargetLostException {
        remove(from.getPrev(), to.getNext());
    }

    /**
     * Delete contents of list. Provides better memory utilization, because the system then may reuse the instruction
     * handles. This method is typically called right after {@link MethodGen#getMethod()}.
     */
    public void dispose() {
        // Traverse in reverse order, because ih.next is overwritten
        for (InstructionHandle ih = end; ih != null; ih = ih.getPrev()) {
            // Causes BranchInstructions to release target and targeters, because it calls dispose() on the contained instruction.
            ih.dispose();
        }
        clear();
    }

    /**
     * Gets instruction handle for instruction at byte code position pos. This only works properly, if the list is freshly
     * initialized from a byte array or setPositions() has been called before this method.
     *
     * @param pos byte code position to search for
     * @return target position's instruction handle if available
     */
    public InstructionHandle findHandle(final int pos) {
        final int[] positions = bytePositions;
        InstructionHandle ih = start;
        for (int i = 0; i < length; i++) {
            if (positions[i] == pos) {
                return ih;
            }
            ih = ih.getNext();
        }
        return null;
    }

    /**
     * Search for given Instruction reference, start at beginning of list.
     *
     * @param i instruction to search for
     * @return instruction found on success, null otherwise
     */
    private InstructionHandle findInstruction1(final Instruction i) {
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            if (ih.getInstruction() == i) {
                return ih;
            }
        }
        return null;
    }

    /**
     * Search for given Instruction reference, start at end of list
     *
     * @param i instruction to search for
     * @return instruction found on success, null otherwise
     */
    private InstructionHandle findInstruction2(final Instruction i) {
        for (InstructionHandle ih = end; ih != null; ih = ih.getPrev()) {
            if (ih.getInstruction() == i) {
                return ih;
            }
        }
        return null;
    }

    /**
     * When everything is finished, use this method to convert the instruction list into an array of bytes.
     *
     * @return the byte code ready to be dumped
     */
    public byte[] getByteCode() {
        // Update position indices of instructions
        setPositions();
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
                final Instruction i = ih.getInstruction();
                i.dump(out); // Traverse list
            }
            out.flush();
        } catch (final IOException e) {
            System.err.println(e);
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        return b.toByteArray();
    }

    /**
     * @return end of list
     */
    public InstructionHandle getEnd() {
        return end;
    }

    /**
     * @return array containing all instructions (handles)
     */
    public InstructionHandle[] getInstructionHandles() {
        final InstructionHandle[] ihs = new InstructionHandle[length];
        InstructionHandle ih = start;
        for (int i = 0; i < length; i++) {
            ihs[i] = ih;
            ih = ih.getNext();
        }
        return ihs;
    }

    /**
     * Gets positions (offsets) of all instructions in the list. This relies on that the list has been freshly created from
     * an byte code array, or that setPositions() has been called. Otherwise this may be inaccurate.
     *
     * @return array containing all instruction's offset in byte code
     */
    public int[] getInstructionPositions() {
        return bytePositions;
    }

    /**
     * @return an array of instructions without target information for branch instructions.
     */
    public Instruction[] getInstructions() {
        final List<Instruction> instructions = new ArrayList<>();
        try (ByteSequence bytes = new ByteSequence(getByteCode())) {
            while (bytes.available() > 0) {
                instructions.add(Instruction.readInstruction(bytes));
            }
        } catch (final IOException e) {
            throw new ClassGenException(e.toString(), e);
        }
        return instructions.toArray(Instruction.EMPTY_ARRAY);
    }

    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int getLength() {
        return length;
    }

    /**
     * @return start of list
     */
    public InstructionHandle getStart() {
        return start;
    }

    /**
     * Insert a branch instruction at start of this list.
     *
     * @param i branch instruction to insert
     * @return branch instruction handle of the appended instruction
     */
    public BranchHandle insert(final BranchInstruction i) {
        final BranchHandle ih = BranchHandle.getBranchHandle(i);
        insert(ih);
        return ih;
    }

    /**
     * Insert a compound instruction.
     *
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final CompoundInstruction c) {
        return insert(c.getInstructionList());
    }

    /**
     * Insert an instruction at start of this list.
     *
     * @param i instruction to insert
     * @return instruction handle of the inserted instruction
     */
    public InstructionHandle insert(final Instruction i) {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(i);
        insert(ih);
        return ih;
    }

    /**
     * Insert a compound instruction before instruction i.
     *
     * @param i Instruction in list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final Instruction i, final CompoundInstruction c) {
        return insert(i, c.getInstructionList());
    }

    /**
     * Insert a single instruction j before another instruction i, which must be in this list of course!
     *
     * @param i Instruction in list
     * @param j Instruction to insert before i in list
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final Instruction i, final Instruction j) {
        return insert(i, new InstructionList(j));
    }

    /**
     * Insert another list before Instruction i contained in this list. Consumes argument list, i.e., it becomes empty.
     *
     * @param i where to append the instruction list
     * @param il Instruction list to insert
     * @return instruction handle pointing to the first inserted instruction, i.e., il.getStart()
     */
    public InstructionHandle insert(final Instruction i, final InstructionList il) {
        final InstructionHandle ih;
        if ((ih = findInstruction1(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        return insert(ih, il);
    }

    /**
     * Insert an instruction at start of this list.
     *
     * @param ih instruction to insert
     */
    private void insert(final InstructionHandle ih) {
        if (isEmpty()) {
            start = end = ih;
            ih.setNext(ih.setPrev(null));
        } else {
            start.setPrev(ih);
            ih.setNext(start);
            ih.setPrev(null);
            start = ih;
        }
        length++;
    }

    /**
     * Insert an instruction before instruction (handle) ih contained in this list.
     *
     * @param ih where to insert to the instruction list
     * @param i Instruction to insert
     * @return instruction handle of the first inserted instruction
     */
    public BranchHandle insert(final InstructionHandle ih, final BranchInstruction i) {
        final BranchHandle bh = BranchHandle.getBranchHandle(i);
        final InstructionList il = new InstructionList();
        il.append(bh);
        insert(ih, il);
        return bh;
    }

    /**
     * Insert a compound instruction.
     *
     * @param ih where to insert the instruction list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final InstructionHandle ih, final CompoundInstruction c) {
        return insert(ih, c.getInstructionList());
    }

    /**
     * Insert an instruction before instruction (handle) ih contained in this list.
     *
     * @param ih where to insert to the instruction list
     * @param i Instruction to insert
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final InstructionHandle ih, final Instruction i) {
        return insert(ih, new InstructionList(i));
    }

    /**
     * Insert another list before Instruction handle ih contained in this list. Consumes argument list, i.e., it becomes
     * empty.
     *
     * @param ih where to append the instruction list
     * @param il Instruction list to insert
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final InstructionHandle ih, final InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Inserting null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        final InstructionHandle prev = ih.getPrev();
        final InstructionHandle ret = il.start;
        ih.setPrev(il.end);
        il.end.setNext(ih);
        il.start.setPrev(prev);
        if (prev != null) {
            prev.setNext(il.start);
        } else {
            start = il.start; // Update start ...
        }
        length += il.length; // Update length
        il.clear();
        return ret;
    }

    /**
     * Insert another list.
     *
     * @param il list to insert before start of this list
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert(final InstructionList il) {
        if (isEmpty()) {
            append(il); // Code is identical for this case
            return start;
        }
        return insert(start, il);
    }

    /**
     * Tests for empty list.
     */
    public boolean isEmpty() {
        return start == null;
    } // && end == null

    /**
     * @return iterator that lists all instructions (handles)
     */
    @Override
    public Iterator<InstructionHandle> iterator() {
        return new Iterator<InstructionHandle>() {

            private InstructionHandle ih = start;

            @Override
            public boolean hasNext() {
                return ih != null;
            }

            @Override
            public InstructionHandle next() throws NoSuchElementException {
                if (ih == null) {
                    throw new NoSuchElementException();
                }
                final InstructionHandle i = ih;
                ih = ih.getNext();
                return i;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Move a single instruction (handle) to a new location.
     *
     * @param ih moved instruction
     * @param target new location of moved instruction
     */
    public void move(final InstructionHandle ih, final InstructionHandle target) {
        move(ih, ih, target);
    }

    /**
     * Take all instructions (handles) from "start" to "end" and append them after the new location "target". Of course,
     * "end" must be after "start" and target must not be located withing this range. If you want to move something to the
     * start of the list use null as value for target.
     * <p>
     * Any instruction targeters pointing to handles within the block, keep their targets.
     * </p>
     *
     * @param start of moved block
     * @param end of moved block
     * @param target of moved block
     */
    public void move(final InstructionHandle start, final InstructionHandle end, final InstructionHandle target) {
        // Step 1: Check constraints
        if (start == null || end == null) {
            throw new ClassGenException("Invalid null handle: From " + start + " to " + end);
        }
        if (target == start || target == end) {
            throw new ClassGenException("Invalid range: From " + start + " to " + end + " contains target " + target);
        }
        for (InstructionHandle ih = start; ih != end.getNext(); ih = ih.getNext()) {
            if (ih == null) {
                throw new ClassGenException("Invalid range: From " + start + " to " + end);
            }
            if (ih == target) {
                throw new ClassGenException("Invalid range: From " + start + " to " + end + " contains target " + target);
            }
        }
        // Step 2: Temporarily remove the given instructions from the list
        final InstructionHandle prev = start.getPrev();
        InstructionHandle next = end.getNext();
        if (prev != null) {
            prev.setNext(next);
        } else {
            this.start = next;
        }
        if (next != null) {
            next.setPrev(prev);
        } else {
            this.end = prev;
        }
        start.setPrev(end.setNext(null));
        // Step 3: append after target
        if (target == null) { // append to start of list
            if (this.start != null) {
                this.start.setPrev(end);
            }
            end.setNext(this.start);
            this.start = start;
        } else {
            next = target.getNext();
            target.setNext(start);
            start.setPrev(target);
            end.setNext(next);
            if (next != null) {
                next.setPrev(end);
            } else {
                this.end = end;
            }
        }
    }

    /**
     * Redirect all references from oldTarget to newTarget, i.e., update targets of branch instructions.
     *
     * @param oldTarget the old target instruction handle
     * @param newTarget the new target instruction handle
     */
    public void redirectBranches(final InstructionHandle oldTarget, final InstructionHandle newTarget) {
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            final Instruction i = ih.getInstruction();
            if (i instanceof BranchInstruction) {
                final BranchInstruction b = (BranchInstruction) i;
                final InstructionHandle target = b.getTarget();
                if (target == oldTarget) {
                    b.setTarget(newTarget);
                }
                if (b instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    final InstructionHandle[] targets = ((Select) b).getTargets();
                    for (int j = 0; j < targets.length; j++) {
                        if (targets[j] == oldTarget) {
                            ((Select) b).setTarget(j, newTarget);
                        }
                    }
                }
            }
        }
    }

    /**
     * Redirect all references of exception handlers from oldTarget to newTarget.
     *
     * @param exceptions array of exception handlers
     * @param oldTarget the old target instruction handle
     * @param newTarget the new target instruction handle
     * @see MethodGen
     */
    public void redirectExceptionHandlers(final CodeExceptionGen[] exceptions, final InstructionHandle oldTarget, final InstructionHandle newTarget) {
        Streams.of(exceptions).forEach(exception -> {
            if (exception.getStartPC() == oldTarget) {
                exception.setStartPC(newTarget);
            }
            if (exception.getEndPC() == oldTarget) {
                exception.setEndPC(newTarget);
            }
            if (exception.getHandlerPC() == oldTarget) {
                exception.setHandlerPC(newTarget);
            }
        });
    }

    /**
     * Redirect all references of local variables from oldTarget to newTarget.
     *
     * @param lg array of local variables
     * @param oldTarget the old target instruction handle
     * @param newTarget the new target instruction handle
     * @see MethodGen
     */
    public void redirectLocalVariables(final LocalVariableGen[] lg, final InstructionHandle oldTarget, final InstructionHandle newTarget) {
        Streams.of(lg).forEach(element -> {
            if (element.getStart() == oldTarget) {
                element.setStart(newTarget);
            }
            if (element.getEnd() == oldTarget) {
                element.setEnd(newTarget);
            }
        });
    }

    /**
     * Remove from instruction 'prev' to instruction 'next' both contained in this list. Throws TargetLostException when one
     * of the removed instruction handles is still being targeted.
     *
     * @param prev where to start deleting (predecessor, exclusive)
     * @param next where to end deleting (successor, exclusive)
     */
    private void remove(final InstructionHandle prev, InstructionHandle next) throws TargetLostException {
        final InstructionHandle first;
        final InstructionHandle last; // First and last deleted instruction
        if (prev == null && next == null) {
            first = start;
            last = end;
            start = end = null;
        } else {
            if (prev == null) { // At start of list
                first = start;
                start = next;
            } else {
                first = prev.getNext();
                prev.setNext(next);
            }
            if (next == null) { // At end of list
                last = end;
                end = prev;
            } else {
                last = next.getPrev();
                next.setPrev(prev);
            }
        }
        first.setPrev(null); // Completely separated from rest of list
        last.setNext(null);
        final List<InstructionHandle> targetList = new ArrayList<>();
        for (InstructionHandle ih = first; ih != null; ih = ih.getNext()) {
            ih.getInstruction().dispose(); // for example BranchInstructions release their targets
        }
        final StringBuilder buf = new StringBuilder("{ ");
        for (InstructionHandle ih = first; ih != null; ih = next) {
            next = ih.getNext();
            length--;
            if (ih.hasTargeters()) { // Still got targeters?
                targetList.add(ih);
                buf.append(ih.toString(true)).append(" ");
                ih.setNext(ih.setPrev(null));
            } else {
                ih.dispose();
            }
        }
        buf.append("}");
        if (!targetList.isEmpty()) {
            throw new TargetLostException(targetList.toArray(InstructionHandle.EMPTY_ARRAY), buf.toString());
        }
    }

    /**
     * Remove observer for this object.
     */
    public void removeObserver(final InstructionListObserver o) {
        if (observers != null) {
            observers.remove(o);
        }
    }

    /**
     * Replace all references to the old constant pool with references to the new constant pool
     */
    public void replaceConstantPool(final ConstantPoolGen oldCp, final ConstantPoolGen newCp) {
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            final Instruction i = ih.getInstruction();
            if (i instanceof CPInstruction) {
                final CPInstruction ci = (CPInstruction) i;
                final Constant c = oldCp.getConstant(ci.getIndex());
                ci.setIndex(newCp.addConstant(c, oldCp));
            }
        }
    }

    public void setPositions() { // TODO could be package-protected? (some test code would need to be repackaged)
        setPositions(false);
    }

    /**
     * Give all instructions their position number (offset in byte stream), i.e., make the list ready to be dumped.
     *
     * @param check Perform sanity checks, for example if all targeted instructions really belong to this list
     */
    public void setPositions(final boolean check) { // called by code in other packages
        int maxAdditionalBytes = 0;
        int additionalBytes = 0;
        int index = 0;
        int count = 0;
        final int[] pos = new int[length];
        /*
         * Pass 0: Sanity checks
         */
        if (check) {
            for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
                final Instruction i = ih.getInstruction();
                if (i instanceof BranchInstruction) { // target instruction within list?
                    Instruction inst = ((BranchInstruction) i).getTarget().getInstruction();
                    if (!contains(inst)) {
                        throw new ClassGenException("Branch target of " + Const.getOpcodeName(i.getOpcode()) + ":" + inst + " not in instruction list");
                    }
                    if (i instanceof Select) {
                        final InstructionHandle[] targets = ((Select) i).getTargets();
                        for (final InstructionHandle target : targets) {
                            inst = target.getInstruction();
                            if (!contains(inst)) {
                                throw new ClassGenException("Branch target of " + Const.getOpcodeName(i.getOpcode()) + ":" + inst + " not in instruction list");
                            }
                        }
                    }
                    if (!(ih instanceof BranchHandle)) {
                        throw new ClassGenException(
                            "Branch instruction " + Const.getOpcodeName(i.getOpcode()) + ":" + inst + " not contained in BranchHandle.");
                    }
                }
            }
        }
        /*
         * Pass 1: Set position numbers and sum up the maximum number of bytes an instruction may be shifted.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            final Instruction i = ih.getInstruction();
            ih.setPosition(index);
            pos[count++] = index;
            /*
             * Gets an estimate about how many additional bytes may be added, because BranchInstructions may have variable length
             * depending on the target offset (short vs. int) or alignment issues (TABLESWITCH and LOOKUPSWITCH).
             */
            switch (i.getOpcode()) {
            case Const.JSR:
            case Const.GOTO:
                maxAdditionalBytes += 2;
                break;
            case Const.TABLESWITCH:
            case Const.LOOKUPSWITCH:
                maxAdditionalBytes += 3;
                break;
            default:
                // TODO should this be an error?
                break;
            }
            index += i.getLength();
        }
        /*
         * Pass 2: Expand the variable-length (Branch) Instructions depending on the target offset (short or int) and ensure that
         * branch targets are within this list.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            additionalBytes += ih.updatePosition(additionalBytes, maxAdditionalBytes);
        }
        /*
         * Pass 3: Update position numbers (which may have changed due to the preceding expansions), like pass 1.
         */
        index = count = 0;
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            final Instruction i = ih.getInstruction();
            ih.setPosition(index);
            pos[count++] = index;
            index += i.getLength();
        }
        bytePositions = Arrays.copyOfRange(pos, 0, count); // Trim to proper size
    }

    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int size() {
        return length;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * @param verbose toggle output format
     * @return String containing all instructions in this list.
     */
    public String toString(final boolean verbose) {
        final StringBuilder buf = new StringBuilder();
        for (InstructionHandle ih = start; ih != null; ih = ih.getNext()) {
            buf.append(ih.toString(verbose)).append("\n");
        }
        return buf.toString();
    }

    /**
     * Call notify() method on all observers. This method is not called automatically whenever the state has changed, but
     * has to be called by the user after he has finished editing the object.
     */
    public void update() {
        if (observers != null) {
            for (final InstructionListObserver observer : observers) {
                observer.notify(this);
            }
        }
    }
}
