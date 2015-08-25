/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.bcel6.generic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.bcel6.Constants;
import org.apache.commons.bcel6.classfile.Constant;
import org.apache.commons.bcel6.util.ByteSequence;

/** 
 * This class is a container for a list of <a
 * href="Instruction.html">Instruction</a> objects. Instructions can
 * be appended, inserted, moved, deleted, etc.. Instructions are being
 * wrapped into <a
 * href="InstructionHandle.html">InstructionHandles</a> objects that
 * are returned upon append/insert operations. They give the user
 * (read only) access to the list structure, such that it can be traversed and
 * manipulated in a controlled way.
 *
 * A list is finally dumped to a byte code array with <a
 * href="#getByteCode()">getByteCode</a>.
 *
 * @version $Id: InstructionList.java 1696827 2015-08-20 17:27:07Z sebb $
 * @see     Instruction
 * @see     InstructionHandle
 * @see BranchHandle
 */
public class InstructionList {

    private InstructionHandle start = null;
    private InstructionHandle end = null;
    private int length = 0; // number of elements in list
    private int[] byte_positions; // byte code offsets corresponding to instructions


    /**
     * Create (empty) instruction list.
     */
    public InstructionList() {
    }


    /**
     * Create instruction list containing one instruction.
     * @param i initial instruction
     */
    public InstructionList(Instruction i) {
        append(i);
    }


    /**
     * Create instruction list containing one instruction.
     * @param i initial instruction
     */
    public InstructionList(BranchInstruction i) {
        append(i);
    }


    /**
     * Initialize list with (nonnull) compound instruction. Consumes argument
     * list, i.e., it becomes empty.
     *
     * @param c compound instruction (list)
     */
    public InstructionList(CompoundInstruction c) {
        append(c.getInstructionList());
    }


    /**
     * Test for empty list.
     */
    public boolean isEmpty() {
        return start == null;
    } // && end == null


    /**
     * Get instruction handle for instruction at byte code position pos.
     * This only works properly, if the list is freshly initialized from a byte array or
     * setPositions() has been called before this method.
     *
     * @param pos byte code position to search for
     * @return target position's instruction handle if available
     */
    public InstructionHandle findHandle( int pos ) {
        InstructionHandle ih = start;
        for (int i = 0; i < length; i++) { 
            if(byte_positions[i] == pos) {
                return ih;
            }
            ih = ih.next;
            // TODO: stop if position has been passed
            // TODO: put position into InstructionHandle and binary search the list
        }
        return null;
    }


    /**
     * Initialize instruction list from byte array.
     *
     * @param code byte array containing the instructions
     */
    public InstructionList(byte[] code) {
        InstructionListParser reader= new InstructionListParser(this, code);
        byte_positions = reader.getTrimmedPositions();
    }


    /**
     * Append another list after instruction (handle) ih contained in this list.
     * Consumes argument list, i.e., it becomes empty.
     *
     * @param ih where to append the instruction list 
     * @param il Instruction list to append to this one
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append( InstructionHandle ih, InstructionList il ) {
        if (il == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        InstructionHandle next = ih.next;
        InstructionHandle ret = il.start;
        ih.next = il.start;
        il.start.prev = ih;
        il.end.next = next;
        if (next != null) {
            next.prev = il.end;
        } else {
            end = il.end; // Update end ...
        }
        length += il.length; // Update length
        il.clear();
        return ret;
    }


    /**
     * Append another list after instruction i contained in this list.
     * Consumes argument list, i.e., it becomes empty.
     *
     * @param i  where to append the instruction list 
     * @param il Instruction list to append to this one
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append( Instruction i, InstructionList il ) {
        InstructionHandle ih;
        if ((ih = findInstruction2(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        return append(ih, il);
    }


    /**
     * Append another list to this one.
     * Consumes argument list, i.e., it becomes empty.
     *
     * @param il list to append to end of this list
     * @return instruction handle of the <B>first</B> appended instruction
     */
    public InstructionHandle append( InstructionList il ) {
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


    /**
     * Append an instruction to the end of this list.
     *
     * @param ih instruction to append
     */
    void append( InstructionHandle ih ) {
        if (isEmpty()) {
            start = end = ih;
            ih.next = ih.prev = null;
        } else {
            end.next = ih;
            ih.prev = end;
            ih.next = null;
            end = ih;
        }
        length++; // Update length
    }


    /**
     * Append an instruction to the end of this list.
     *
     * @param i instruction to append
     * @return instruction handle of the appended instruction
     */
    public InstructionHandle append( Instruction i ) {
        InstructionHandle ih = new InstructionHandle(this, i);
        append(ih);
        return ih;
    }


    /**
     * Append a single instruction j after another instruction i, which
     * must be in this list of course!
     *
     * @param i Instruction in list
     * @param j Instruction to append after i in list
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append( Instruction i, Instruction j ) {
        return append(i, new InstructionList(j));
    }


    /**
     * Append a compound instruction, after instruction i.
     *
     * @param i Instruction in list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append( Instruction i, CompoundInstruction c ) {
        return append(i, c.getInstructionList());
    }


    /**
     * Append a compound instruction.
     *
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append( CompoundInstruction c ) {
        return append(c.getInstructionList());
    }


    /**
     * Append a compound instruction.
     *
     * @param ih where to append the instruction list 
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first appended instruction
     */
    public InstructionHandle append( InstructionHandle ih, CompoundInstruction c ) {
        return append(ih, c.getInstructionList());
    }


    /**
     * Append an instruction after instruction (handle) ih contained in this list.
     *
     * @param ih where to append the instruction list 
     * @param i Instruction to append
     * @return instruction handle pointing to the <B>first</B> appended instruction
     */
    public InstructionHandle append( InstructionHandle ih, Instruction i ) {
        return append(ih, new InstructionList(i));
    }


    /**
     * Insert another list before Instruction handle ih contained in this list.
     * Consumes argument list, i.e., it becomes empty.
     *
     * @param ih  where to append the instruction list 
     * @param il Instruction list to insert
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( InstructionHandle ih, InstructionList il ) {
        if (il == null) {
            throw new ClassGenException("Inserting null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        InstructionHandle prev = ih.prev;
        InstructionHandle ret = il.start;
        ih.prev = il.end;
        il.end.next = ih;
        il.start.prev = prev;
        if (prev != null) {
            prev.next = il.start;
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
    public InstructionHandle insert( InstructionList il ) {
        if (isEmpty()) {
            append(il); // Code is identical for this case
            return start;
        }
        return insert(start, il);
    }


    /**
     * Insert an instruction at start of this list.
     *
     * @param ih instruction to insert
     */
    private void insert( InstructionHandle ih ) {
        if (isEmpty()) {
            start = end = ih;
            ih.next = ih.prev = null;
        } else {
            start.prev = ih;
            ih.next = start;
            ih.prev = null;
            start = ih;
        }
        length++;
    }


    /**
     * Insert another list before Instruction i contained in this list.
     * Consumes argument list, i.e., it becomes empty.
     *
     * @param i  where to append the instruction list 
     * @param il Instruction list to insert
     * @return instruction handle pointing to the first inserted instruction,
     * i.e., il.getStart()
     */
    public InstructionHandle insert( Instruction i, InstructionList il ) {
        InstructionHandle ih;
        if ((ih = findInstruction1(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        return insert(ih, il);
    }


    /**
     * Insert an instruction at start of this list.
     *
     * @param i instruction to insert
     * @return instruction handle of the inserted instruction
     */
    public InstructionHandle insert( Instruction i ) {
        InstructionHandle ih = new InstructionHandle(this, i);
        insert(ih);
        return ih;
    }


    /**
     * Insert a single instruction j before another instruction i, which
     * must be in this list of course!
     *
     * @param i Instruction in list
     * @param j Instruction to insert before i in list
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( Instruction i, Instruction j ) {
        return insert(i, new InstructionList(j));
    }


    /**
     * Insert a compound instruction before instruction i.
     *
     * @param i Instruction in list
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( Instruction i, CompoundInstruction c ) {
        return insert(i, c.getInstructionList());
    }


    /**
     * Insert a compound instruction.
     *
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( CompoundInstruction c ) {
        return insert(c.getInstructionList());
    }


    /**
     * Insert an instruction before instruction (handle) ih contained in this list.
     *
     * @param ih where to insert to the instruction list 
     * @param i Instruction to insert
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( InstructionHandle ih, Instruction i ) {
        return insert(ih, new InstructionList(i));
    }


    /**
     * Insert a compound instruction.
     *
     * @param ih where to insert the instruction list 
     * @param c The composite instruction (containing an InstructionList)
     * @return instruction handle of the first inserted instruction
     */
    public InstructionHandle insert( InstructionHandle ih, CompoundInstruction c ) {
        return insert(ih, c.getInstructionList());
    }


    /**
     * Take all instructions (handles) from "start" to "end" and append them after the
     * new location "target". Of course, "end" must be after "start" and target must
     * not be located withing this range. If you want to move something to the start of
     * the list use null as value for target.<br>
     * Any instruction targeters pointing to handles within the block, keep their targets.
     *
     * @param start  of moved block
     * @param end    of moved block
     * @param target of moved block
     */
    public void move( InstructionHandle start, InstructionHandle end, InstructionHandle target ) {
        // Step 1: Check constraints
        if ((start == null) || (end == null)) {
            throw new ClassGenException("Invalid null handle: From " + start + " to " + end);
        }
        if ((target == start) || (target == end)) {
            throw new ClassGenException("Invalid range: From " + start + " to " + end
                    + " contains target " + target);
        }
        for (InstructionHandle ih = start; ih != end.next; ih = ih.next) {
            if (ih == null) {
                throw new ClassGenException("Invalid range: From " + start + " to " + end);
            } else if (ih == target) {
                throw new ClassGenException("Invalid range: From " + start + " to " + end
                        + " contains target " + target);
            }
        }
        // Step 2: Temporarily remove the given instructions from the list
        InstructionHandle prev = start.prev;
        InstructionHandle next = end.next;
        if (prev != null) {
            prev.next = next;
        } else {
            this.start = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            this.end = prev;
        }
        start.prev = end.next = null;
        // Step 3: append after target
        if (target == null) { // append to start of list
            if (this.start != null) {
                this.start.prev = end;
            }
            end.next = this.start;
            this.start = start;
        } else {
            next = target.next;
            target.next = start;
            start.prev = target;
            end.next = next;
            if (next != null) {
                next.prev = end;
            } else {
                this.end = end;
            }
        }
    }


    /**
     * Move a single instruction (handle) to a new location.
     *
     * @param ih     moved instruction
     * @param target new location of moved instruction
     */
    public void move( InstructionHandle ih, InstructionHandle target ) {
        move(ih, ih, target);
    }


    /**
     * Remove from instruction `prev' to instruction `next' both contained
     * in this list. Throws TargetLostException when one of the removed instruction handles
     * is still being targeted.
     *
     * @param prev where to start deleting (predecessor, exclusive)
     * @param next where to end deleting (successor, exclusive)
     */
    private void remove( InstructionHandle prev, InstructionHandle next )
            throws TargetLostException {
        InstructionHandle first;
        InstructionHandle last; // First and last deleted instruction
        if ((prev == null) && (next == null)) {
            first = start;
            last = end;
            start = end = null;
        } else {
            if (prev == null) { // At start of list
                first = start;
                start = next;
            } else {
                first = prev.next;
                prev.next = next;
            }
            if (next == null) { // At end of list
                last = end;
                end = prev;
            } else {
                last = next.prev;
                next.prev = prev;
            }
        }
        first.prev = null; // Completely separated from rest of list
        last.next = null;
        for (InstructionHandle ih = first; ih != null; ih = ih.next) {
            ih.releaseTargets(); // e.g. BranchInstructions release their targets
        }

        List<InstructionHandle> target_vec = new ArrayList<>();
        StringBuilder buf = new StringBuilder("{ ");
        for (InstructionHandle ih = first; ih != null; ih = next) {
            next = ih.next;
            length--;
            if (ih.hasTargeters()) { // Still got targeters?
                target_vec.add(ih);
                buf.append(ih.toString(true)).append(" ");
                ih.next = ih.prev = null;
            }
        }
        buf.append("}");
        if (!target_vec.isEmpty()) {
            InstructionHandle[] targeted = new InstructionHandle[target_vec.size()];
            target_vec.toArray(targeted);
            throw new TargetLostException(targeted, buf.toString());
        }
    }


    /**
     * Remove instruction from this list. The corresponding Instruction
     * handles must not be reused!
     *
     * @param ih instruction (handle) to remove 
     */
    public void delete( InstructionHandle ih ) throws TargetLostException {
        remove(ih.prev, ih.next);
    }


    /**
     * Remove instruction from this list. The corresponding Instruction
     * handles must not be reused!
     *
     * @param i instruction to remove
     */
    public void delete( Instruction i ) throws TargetLostException {
        InstructionHandle ih;
        if ((ih = findInstruction1(i)) == null) {
            throw new ClassGenException("Instruction " + i + " is not contained in this list.");
        }
        delete(ih);
    }


    /**
     * Remove instructions from instruction `from' to instruction `to' contained
     * in this list. The user must ensure that `from' is an instruction before
     * `to', or risk havoc. The corresponding Instruction handles must not be reused!
     *
     * @param from where to start deleting (inclusive)
     * @param to   where to end deleting (inclusive)
     */
    public void delete( InstructionHandle from, InstructionHandle to ) throws TargetLostException {
        remove(from.prev, to.next);
    }


    /**
     * Remove instructions from instruction `from' to instruction `to' contained
     * in this list. The user must ensure that `from' is an instruction before
     * `to', or risk havoc. The corresponding Instruction handles must not be reused!
     *
     * @param from where to start deleting (inclusive)
     * @param to   where to end deleting (inclusive)
     */
    public void delete( Instruction from, Instruction to ) throws TargetLostException {
        InstructionHandle from_ih;
        InstructionHandle to_ih;
        if ((from_ih = findInstruction1(from)) == null) {
            throw new ClassGenException("Instruction " + from + " is not contained in this list.");
        }
        if ((to_ih = findInstruction2(to)) == null) {
            throw new ClassGenException("Instruction " + to + " is not contained in this list.");
        }
        delete(from_ih, to_ih);
    }


    /**
     * Search for given Instruction reference, start at beginning of list.
     *
     * @param i instruction to search for
     * @return instruction found on success, null otherwise
     */
    private InstructionHandle findInstruction1( Instruction i ) {
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            if (ih.instruction == i) {
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
    private InstructionHandle findInstruction2( Instruction i ) {
        for (InstructionHandle ih = end; ih != null; ih = ih.prev) {
            if (ih.instruction == i) {
                return ih;
            }
        }
        return null;
    }


    public boolean contains( InstructionHandle i ) {
        if (i == null) {
            return false;
        }
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            if (ih == i) {
                return true;
            }
        }
        return false;
    }


    public boolean contains( Instruction i ) {
        return findInstruction1(i) != null;
    }


    public void setPositions() { // TODO could be package-protected? (some test code would need to be repackaged)
        setPositions(false);
    }


    /**
     * Give all instructions their position number (offset in byte stream), i.e.,
     * make the list ready to be dumped.
     *
     * @param check Perform sanity checks, e.g. if all targeted instructions really belong
     * to this list
     */
    public void setPositions( boolean check ) { // called by code in other packages
        int max_additional_bytes = 0;
        int additional_bytes = 0;
        int index = 0;
        int count = 0;
        int[] pos = new int[length];
        /* Pass 0: Sanity checks
         */
        if (check) {
            sanityCheckHandleTargets();
        }
        /* Pass 1: Set position numbers and sum up the maximum number of bytes an
         * instruction may be shifted.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            Instruction i = ih.instruction;
            ih.setPosition(index);
            pos[count++] = index;
            /* Get an estimate about how many additional bytes may be added, because
             * BranchInstructions may have variable length depending on the target
             * offset (short vs. int) or alignment issues (TABLESWITCH and
             * LOOKUPSWITCH).
             */
            switch (i.getOpcode()) {
                case Constants.JSR:
                case Constants.GOTO:
                    max_additional_bytes += 2;
                    break;
                case Constants.TABLESWITCH:
                case Constants.LOOKUPSWITCH:
                    max_additional_bytes += 3;
                    break;
            }
            index += i.getLength();
        }
        /* Pass 2: Expand the variable-length (Branch)Instructions depending on
         * the target offset (short or int) and ensure that branch targets are
         * within this list.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            additional_bytes += ih.updatePosition(additional_bytes, max_additional_bytes);
        }
        /* Pass 3: Update position numbers (which may have changed due to the
         * preceding expansions), like pass 1.
         */
        index = count = 0;
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            Instruction i = ih.instruction;
            ih.setPosition(index);
            pos[count++] = index;
            index += i.getLength();
        }
        byte_positions = new int[count]; // Trim to proper size
        System.arraycopy(pos, 0, byte_positions, 0, count);
    }


    private void sanityCheckHandleTargets() {
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            Instruction i = ih.instruction;
            if (i instanceof BranchInstruction) { // target instruction within list?
                InstructionHandle inst = ((BranchInstruction) i).getTarget();
                if (!contains(inst)) {
                    throw new ClassGenException("Branch target of "
                            + Constants.OPCODE_NAMES[i.opcode] + ":" + inst
                            + " not in instruction list");
                }
                if (i instanceof Select) {
                    Select s = (Select)i;
                    for (int matchCount = s.getMatchCount(), m= 0; m<matchCount; ++m) {
                        if (!contains(s.getMatchTarget(m))) {
                            throw new ClassGenException("Branch target of "
                                    + Constants.OPCODE_NAMES[i.opcode] + ":" + inst
                                    + " not in instruction list");
                        }
                    }
                }
            }
        }
    }


    /**
     * When everything is finished, use this method to convert the instruction
     * list into an array of bytes.
     *
     * @return the byte code ready to be dumped
     */
    public byte[] getByteCode() {
        // Update position indices of instructions
        setPositions();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            for (InstructionHandle ih = start; ih != null; ih = ih.next) {
                Instruction i = ih.instruction;
                i.dump(out); // Traverse list
            }
        } catch (IOException e) {
            System.err.println(e);
            return new byte[0];
        }
        return b.toByteArray();
    }


    /**
     * @return an array of instructions without target information for branch instructions.
     */
    public Instruction[] getInstructions() {
        ByteSequence bytes = new ByteSequence(getByteCode());
        List<Instruction> instructions = new ArrayList<>();
        try {
            while (bytes.available() > 0) {
                instructions.add(Instruction.readInstruction(bytes));
            }
        } catch (IOException e) {
            throw new ClassGenException(e.toString(), e);
        }
        return instructions.toArray(new Instruction[instructions.size()]);
    }


    @Override
    public String toString() {
        return toString(true);
    }


    /**
     * @param verbose toggle output format
     * @return String containing all instructions in this list.
     */
    public String toString( boolean verbose ) {
        StringBuilder buf = new StringBuilder();
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            buf.append(ih.toString(verbose)).append("\n");
        }
        return buf.toString();
    }


    /**
     * @return iterator that lists all instructions (handles)
     */
    public Iterator<InstructionHandle> iterator() {
        return new Iterator<InstructionHandle>() {

            private InstructionHandle ih = start;


            @Override
            public InstructionHandle next() throws NoSuchElementException {
                if (ih == null) {
                    throw new NoSuchElementException();
                }
                InstructionHandle i = ih;
                ih = ih.next;
                return i;
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }


            @Override
            public boolean hasNext() {
                return ih != null;
            }
        };
    }


    /**
     * @return array containing all instructions (handles)
     */
    public InstructionHandle[] getInstructionHandles() {
        InstructionHandle[] ihs = new InstructionHandle[length];
        InstructionHandle ih = start;
        for (int i = 0; i < length; i++) {
            ihs[i] = ih;
            ih = ih.next;
        }
        return ihs;
    }


    /**
     * Get positions (offsets) of all instructions in the list. This relies on that
     * the list has been freshly created from an byte code array, or that setPositions()
     * has been called. Otherwise this may be inaccurate.
     *
     * @return array containing all instruction's offset in byte code
     */
    public int[] getInstructionPositions() {
        return byte_positions;
    }


    /**
     * @return complete, i.e., deep copy of this list
     */
    public InstructionList copy() {
        Map<InstructionHandle, InstructionHandle> map = new HashMap<>();
        InstructionList il = new InstructionList();
        /* Pass 1: Make copies of all instructions, append them to the new list
         * and associate old instruction references with the new ones, i.e.,
         * a 1:1 mapping.
         */
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            Instruction i = ih.instruction;
            Instruction c = i.copy(); // Use clone for shallow copy
            if (c instanceof BranchInstruction) {
                map.put(ih, il.append((BranchInstruction) c));
            } else {
                map.put(ih, il.append(c));
            }
        }
        /* Pass 2: Update branch targets.
         */
        InstructionHandle ih = start;
        InstructionHandle ch = il.start;
        while (ih != null) {
            Instruction i = ih.instruction;
            Instruction c = ch.instruction;
            if (i instanceof BranchInstruction) {
                BranchInstruction bi = (BranchInstruction) i;
                BranchInstruction bc = (BranchInstruction) c;
                InstructionHandle itarget = bi.getTarget(); // old target
                // New target is in hash map
                bc.setTarget(map.get(itarget));
                if (bi instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    Select si = (Select)bi;
                    Select sc = (Select)bc;
                    for (int matchCount = si.getMatchCount(), m= 0; m<matchCount; ++m) { // Update all targets
                        sc.setMatchTarget(m, map.get(si.getMatchTarget(m)));
                    }
                }
            }
            ih = ih.next;
            ch = ch.next;
        }
        return il;
    }


    /** Replace all references to the old constant pool with references to the new
     *  constant pool
     */
    public void replaceConstantPool( ConstantPoolGen old_cp, ConstantPoolGen new_cp ) {
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            Instruction i = ih.instruction;
            if (i instanceof CPInstruction) {
                CPInstruction ci = (CPInstruction) i;
                Constant c = old_cp.getConstant(ci.getIndex());
                ci.setIndex(new_cp.addConstant(c, old_cp));
            }
        }
    }


    private void clear() {
        start = end = null;
        length = 0;
    }


    /**
     * @return start of list
     */
    public InstructionHandle getStart() {
        return start;
    }


    /**
     * @return end of list
     */
    public InstructionHandle getEnd() {
        return end;
    }


    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int getLength() {
        return length;
    }


    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int size() {
        return length;
    }


    /**
     * Redirect all references from old_target to new_target. 
     * <ol>
     * <li>update targets of branch instructions.</li>
     * <li>update target references of local variables.</li>
     * <li>update start and end references of CodeExceptionGen.</li>
     * </ol>
     * @param old_target the old target instruction handle
     * @param new_target the new target instruction handle
     */
    public void redirectTargeters( InstructionHandle old_target, InstructionHandle new_target ) {
        for(InstructionTargeter targeter : old_target.getTargeters()) {
            targeter.updateTarget(old_target, new_target);
        }
    }


    private List<InstructionListObserver> observers;


    /** Add observer for this object.
     */
    public void addObserver( InstructionListObserver o ) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(o);
    }


    /** Remove observer for this object.
     */
    public void removeObserver( InstructionListObserver o ) {
        if (observers != null) {
            observers.remove(o);
        }
    }


    /** Call notify() method on all observers. This method is not called
     * automatically whenever the state has changed, but has to be
     * called by the user after he has finished editing the object.
     */
    public void update() {
        if (observers != null) {
            for (InstructionListObserver observer : observers) {
                observer.notify(this);
            }
        }
    }


    /**
     * Find the first InstructionHandle that holds a specific instruction instance.
     * Singleton instances may occur multiple times in instruction list.
     * 
     * @param instruction The instance to find.
     * @return The first instruction handle, or null if not found
     */
    InstructionHandle findAssociatedInstructionHandle(Instruction instruction) {
        // TODO: should we maintain IdentityHashMap of instructions in this list?
        for (InstructionHandle ih = start; ih != null; ih = ih.next) {
            if(ih.instruction == instruction) {
                return ih;
            }
        }
        return null;
    }
}
