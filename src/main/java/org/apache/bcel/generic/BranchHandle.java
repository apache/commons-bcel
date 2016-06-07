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
package org.apache.bcel.generic;

/**
 * BranchHandle is returned by specialized InstructionList.append() whenever a
 * BranchInstruction is appended. This is useful when the target of this
 * instruction is not known at time of creation and must be set later
 * via setTarget().
 *
 * @see InstructionHandle
 * @see Instruction
 * @see InstructionList
 * @version $Id$
 */
public final class BranchHandle extends InstructionHandle {

    private BranchHandle(final BranchInstruction i) {
        super(i);
    }

    /** Factory methods.
     */
    private static BranchHandle bh_list = null; // List of reusable handles


    static BranchHandle getBranchHandle( final BranchInstruction i ) {
        if (bh_list == null) {
            return new BranchHandle(i);
        }
        BranchHandle bh = bh_list;
        bh_list = (BranchHandle) bh.getNext();
        bh.setInstruction(i);
        return bh;
    }


    /** Handle adds itself to the list of resuable handles.
     */
    @Override
    protected void addHandle() {
        super.setNext(bh_list);
        bh_list = this;
    }

    // get the instruction as a BranchInstruction
    // (do the cast once)
    private BranchInstruction getBI() {
        return (BranchInstruction) super.getInstruction();
    }

    /* Override InstructionHandle methods: delegate to branch instruction.
     * Through this overriding all access to the private i_position field should
     * be prevented.
     */
    @Override
    public int getPosition() {
        return getBI().getPosition();
    }


    @Override
    void setPosition( final int pos ) {
        // Original code: i_position = bi.position = pos;
        getBI().setPosition(pos);
        super.setPosition(pos);
    }


    @Override
    protected int updatePosition( final int offset, final int max_offset ) {
        int x = getBI().updatePosition(offset, max_offset);
        super.setPosition(getBI().getPosition());
        return x;
    }


    /**
     * Pass new target to instruction.
     */
    public void setTarget( final InstructionHandle ih ) {
        getBI().setTarget(ih);
    }


    /**
     * Update target of instruction.
     */
    public void updateTarget( final InstructionHandle old_ih, final InstructionHandle new_ih ) {
        getBI().updateTarget(old_ih, new_ih);
    }


    /**
     * @return target of instruction.
     */
    public InstructionHandle getTarget() {
        return getBI().getTarget();
    }


    /** 
     * Set new contents. Old instruction is disposed and may not be used anymore.
     */
    @Override // This is only done in order to apply the additional type check; could be merged with super impl.
    public void setInstruction( final Instruction i ) { // TODO could be package-protected?
        super.setInstruction(i);
        if (!(i instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning " + i
                    + " to branch handle which is not a branch instruction");
        }
    }
}
