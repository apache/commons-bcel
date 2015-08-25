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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.bcel6.classfile.Utility;

/**
 * Instances of this class give users a handle to the instructions contained in
 * an InstructionList. Instruction objects may be used more than once within a
 * list, this is useful because it saves memory and may be much faster.
 *
 * Within an InstructionList an InstructionHandle object is wrapped
 * around all instructions, i.e., it implements a cell in a
 * doubly-linked list. From the outside only the next and the
 * previous instruction (handle) are accessible. One
 * can traverse the list via an Enumeration returned by
 * InstructionList.elements().
 *
 * @version $Id: InstructionHandle.java 1696863 2015-08-20 21:47:14Z sebb $
 * @see Instruction
 * @see BranchHandle
 * @see InstructionList 
 */
public class InstructionHandle implements InstructionTargeter {

    InstructionList list; // this InstructionList this handle is part of.
    InstructionHandle next;
    InstructionHandle prev; // Will be set from the outside
    Instruction instruction;    // the associated instruction
    protected int i_position = -1; // byte code offset of instruction
    private Set<InstructionTargeter> targeters;   // The set of items that target the associated instruction.

    private Map<Object, Object> attributes;


    /** Factory method.
     */
    InstructionHandle(InstructionList list, Instruction instruction  ) {
        if(instruction==null) {
            throw new NullPointerException("instruction may not be null");
        }
        this.list = list;
        this.instruction = instruction;
    }


    public final InstructionHandle getNext() {
        return next;
    }


    public final InstructionHandle getPrev() {
        return prev;
    }


    public final Instruction getInstruction() {
        return instruction;
    }


    /**
     * Replace current instruction contained in this handle.
     */
    public final void setInstruction( Instruction i ) {
        if (i == null) {
            throw new NullPointerException("May not assign null to handle");
        }
        releaseTargets();
        instruction = i;
    }


    /**
     * Remove this InstructionHandle from the targeter set of the index instruction's target.
     */
    void releaseTargets() {
        if (instruction instanceof BranchInstruction) {
            ((BranchInstruction)instruction).releaseTargets(this);
        }
    }


    /**
     * Temporarily swap the current instruction, without disturbing
     * anything. Meant to be used by a debugger, implementing
     * breakpoints. Current instruction is returned.
     */
    public Instruction swapInstruction( Instruction i ) {
        Instruction oldInstruction = instruction;
        instruction = i;
        return oldInstruction;
    }



    /**
     * Called by InstructionList.setPositions when setting the position for every
     * instruction. In the presence of variable length instructions `setPositions()'
     * performs multiple passes over the instruction list to calculate the
     * correct (byte) positions and offsets by calling this function.
     *
     * @param offset additional offset caused by preceding (variable length) instructions
     * @param max_offset the maximum offset that may be caused by these instructions
     * @return additional offset caused by possible change of this instruction's length
     */
    protected int updatePosition( int offset, int max_offset ) {
        if(instruction instanceof BranchInstruction) {
            return ((BranchInstruction)instruction).updatePosition(offset, max_offset);
        }
        i_position += offset;
        return 0;
    }


    /** @return the position, i.e., the byte code offset of the contained
     * instruction. This is accurate only after
     * InstructionList.setPositions() has been called.
     */
    public int getPosition() {
        return i_position;
    }


    /** Set the position, i.e., the byte code offset of the contained
     * instruction.
     */
    void setPosition( int pos ) {
        i_position = pos;
        if(instruction instanceof BranchInstruction) {
            ((BranchInstruction)instruction).setPosition(pos);
        }
    }


    /**
     * Denote this handle isn't referenced anymore by t.
     */
    public void removeTargeter( InstructionTargeter t ) {
        if (targeters != null) {
            targeters.remove(t);
        }
    }


    /**
     * Denote this handle is being referenced by t.
     */
    public void addTargeter( InstructionTargeter t ) {
        if (targeters == null) {
            targeters = new HashSet<>();
        }
        targeters.add(t);
    }


    public boolean hasTargeters() {
        return (targeters != null) && (targeters.size() > 0);
    }


    /**
     * Get the set of InstructionHandle that have an associated instruction 
     * targeting the instruction associated with this InstructionHandle.
     * 
     * @return An unmodifiable set of InstructionHandle.
     */
    public Set<InstructionTargeter> getTargeters() {
        if (!hasTargeters()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(targeters);
    }


    /** @return a (verbose) string representation of the contained instruction. 
     */
    public String toString( boolean verbose ) {
        return Utility.format(i_position, 4, false, ' ') + ": " + instruction.toString(verbose);
    }


    /** @return a string representation of the contained instruction. 
     */
    @Override
    public String toString() {
        return toString(true);
    }


    /** Add an attribute to an instruction handle.
     *
     * @param key the key object to store/retrieve the attribute
     * @param attr the attribute to associate with this handle
     */
    public void addAttribute( Object key, Object attr ) {
        if (attributes == null) {
            attributes = new HashMap<>(3);
        }
        attributes.put(key, attr);
    }


    /** Delete an attribute of an instruction handle.
     *
     * @param key the key object to retrieve the attribute
     */
    public void removeAttribute( Object key ) {
        if (attributes != null) {
            attributes.remove(key);
        }
    }


    /** Get attribute of an instruction handle.
     *
     * @param key the key object to store/retrieve the attribute
     */
    public Object getAttribute( Object key ) {
        if (attributes != null) {
            return attributes.get(key);
        }
        return null;
    }


    /** @return all attributes associated with this handle
     */
    public Collection<Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>(3);
        }
        return attributes.values();
    }


    /** Convenience method, simply calls accept() on the contained instruction.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        instruction.accept(v);
    }


    /**
     * Set target of branch instruction associated with this handle.
     *  
     * @param target The target for the instruction
     */
    void setBranchTarget( InstructionHandle target ) {
        if(target==null) {
            throw new NullPointerException("target should not be null");
        }

        if(instruction instanceof BranchInstruction) {
            BranchInstruction bi = (BranchInstruction)instruction;
            InstructionHandle oldTarget = bi.getTarget();
            if(oldTarget!=null) {
                oldTarget.removeTargeter(this);
            }

            bi.setTarget(target);
            target.addTargeter(this);
        }
        else {
            throw new IllegalStateException(instruction + " is not a branch instruction");
        }
    }


    /**
     * Set target of select instruction associated with this handle.
     * @param matchIdx The case index.
     * @param target The target for the instruction
     */
    void setSelectTarget(int matchIdx, InstructionHandle target ) {
        if(target==null) {
            throw new NullPointerException("target should not be null");
        }

        if(instruction instanceof Select) {
            Select s = (Select)instruction;
            InstructionHandle oldTarget = s.getMatchTarget(matchIdx);
            if(oldTarget!=null) {
                oldTarget.removeTargeter(this);
            }
            s.setMatchTarget(matchIdx, target);
            target.addTargeter(this);
        }
        else {
            throw new IllegalStateException(instruction + " is not a select instruction");
        }
    }



    @Override
    public boolean containsTarget(InstructionHandle ih) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) throws ClassGenException {
        // TODO Auto-generated method stub
        
    }


    /**
     * Update targeter collections in old and new handle.
     * 
     * @param oldHandle The InstructionHandle which is no longer the target.
     * @param newHandle The InstructionHandle which is now the target.
     * @param targeter The targeter which is changing.
     */
    static void notifyTarget(InstructionHandle oldHandle, InstructionHandle newHandle, InstructionTargeter targeter) {
        if(oldHandle!=null) {
            oldHandle.removeTargeter(targeter);
        }
        if(newHandle!=null) {
            newHandle.addTargeter(targeter);
        }
    }


    /**
     * Invoked by BranchInstructions to remove associated InstructionHandle as targeter for this handle
     * @param branchInstruction
     */
    void removeTargeter(BranchInstruction branchInstruction) {
        InstructionHandle ih = findAssociatedInstructionHandle(branchInstruction);
        if(ih!=null) {
            removeTargeter(ih);
        }
    }


    /**
     * Invoked by BranchInstructions to add associated InstructionHandle as targeter for this handle
     * @param branchInstruction
     */
    void addTargeter(BranchInstruction branchInstruction) {
        InstructionHandle ih = findAssociatedInstructionHandle(branchInstruction);
        if(ih!=null) {
            addTargeter(ih);
        }
    }


    /**
     * Find the first InstructionHandle that holds a specific instruction instance.
     * Singleton instances may occur multiple times in instruction list.  This is ok, since the index 
     * instruction which are adding/removing targeters should not be added to instruction list twice.
     * 
     * @param instruction The instance to find.
     * @return The first instruction handle, or null if not found
     */
    private InstructionHandle findAssociatedInstructionHandle(Instruction instruction) {
        return list.findAssociatedInstructionHandle(instruction);
    }

}
