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

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.bcel6.util.ByteSequence;

/** 
 * Select - Abstract super class for LOOKUPSWITCH and TABLESWITCH instructions.
 * 
 * <p>We use our super's <code>target</code> property as the default target.
 *
 * @version $Id: Select.java 1697260 2015-08-23 21:10:54Z sebb $
 * @see LOOKUPSWITCH
 * @see TABLESWITCH
 * @see InstructionList
 */
public abstract class Select extends BranchInstruction implements VariableLengthInstruction,
        StackConsumer /* @since 6.0 */, StackProducer {

    protected int[] match; // matches, i.e., case 1: ... TODO could be package-protected?
    protected int[] indices; // target offsets TODO could be package-protected?
    // TODO: make an inner class that holds match, index, target
    protected InstructionHandle[] targets; // target objects in instruction list TODO could be package-protected?
    protected int fixed_length; // fixed length defined by subclasses TODO could be package-protected?
    protected int match_length; // number of cases TODO could be package-protected?
    protected int padding = 0; // number of pad bytes for alignment TODO could be package-protected?


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    Select() {
    }


    /**
     * (Match, target) pairs for switch.
     * `Match' and `targets' must have the same length of course.
     *
     * @param match array of matching values
     * @param targets instruction targets
     * @param defaultTarget default instruction target
     */
    // TODO: change to opcode, Case...
    Select(short opcode, int[] match, InstructionHandle[] targets, InstructionHandle defaultTarget) {
        super(opcode, defaultTarget);
        this.targets = targets;
        this.match = match;
        if ((match_length = match.length) != targets.length) {
            throw new ClassGenException("Match and target array have not the same length: Match length: " +
                match.length + " Target length: " + targets.length);
        }
        indices = new int[match_length];
    }


    /**
     * Since this is a variable length instruction, it may shift the following
     * instructions which then need to update their position.
     *
     * Called by InstructionList.setPositions when setting the position for every
     * instruction. In the presence of variable length instructions `setPositions'
     * performs multiple passes over the instruction list to calculate the
     * correct (byte) positions and offsets by calling this function.
     *
     * @param offset additional offset caused by preceding (variable length) instructions
     * @param max_offset the maximum offset that may be caused by these instructions
     * @return additional offset caused by possible change of this instruction's length
     */
    @Override
    protected int updatePosition( int offset, int max_offset ) {
        setPosition(getPosition() + offset); // Additional offset caused by preceding SWITCHs, GOTOs, etc.
        short old_length = length;
        /* Alignment on 4-byte-boundary, + 1, because of tag byte.
         */
        padding = (4 - ((getPosition() + 1) % 4)) % 4;
        length = (short) (fixed_length + padding); // Update length
        return length - old_length;
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        out.writeByte(opcode);
        for (int i = 0; i < padding; i++) {
            out.writeByte(0);
        }
        super.setIndex(getTargetOffset()); // Write default target offset
        out.writeInt(super.getIndex());
    }


    /**
     * Read needed data (e.g. index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        padding = (4 - (bytes.getIndex() % 4)) % 4; // Compute number of pad bytes
        for (int i = 0; i < padding; i++) {
            bytes.readByte();
        }
        // Default branch target common for both cases (TABLESWITCH, LOOKUPSWITCH)
        super.setIndex(bytes.readInt());
    }


    /**
     * @return mnemonic for instruction
     */
    @Override
    public String toString( boolean verbose ) {
        StringBuilder buf = new StringBuilder(super.toString(verbose));
        if (verbose) {
            for (int i = 0; i < match_length; i++) {
                String s = "null";
                if (targets[i] != null) {
                    s = targets[i].getInstruction().toString();
                }
                buf.append("(").append(match[i]).append(", ").append(s).append(" = {").append(
                        indices[i]).append("})");
            }
        } else {
            buf.append(" ...");
        }
        return buf.toString();
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Select copy = (Select) super.clone();
        copy.match = match.clone();
        copy.indices = indices.clone();
        copy.targets = targets.clone();
        return copy;
    }


    /**
     * @return array of match indices
     */
    public int[] getMatchs() {
        return match;
    }


    /**
     * @return array of match target offsets
     */
    public int[] getIndices() {
        return indices;
    }


    /**
     * Get the target for a case.
     * @param matchIdx The case index.
     * @return The InstructionHandle.
     */
    public InstructionHandle getMatchTarget(int matchIdx) {
        return targets[matchIdx];
    }


    /**
     * Release the associated targets
     * @param instructionHandle The InstructionHandle which currently holds this Instruction
     */
    void releaseTargets(InstructionHandle instructionHandle) {
        super.releaseTargets(instructionHandle);
        for(InstructionHandle target : targets) {
            target.removeTargeter(instructionHandle);
        }
    }


    /**
     * Convert absolute index offset into InstructionHandles.
     */
    void convertOffsetToInstructionHandle(InstructionListParser ilp) {
        super.convertOffsetToInstructionHandle(ilp);
        // Search for target position and set target
        for (int i = 0; i < targets.length; ++i) {
            InstructionHandle target = ilp.findHandle(position + indices[i]);
            if (target == null) {
                throw new ClassGenException("Couldn't find target for " + this + " index: " + i);
            }
            targets[i] = target;
        }
    }


    /**
     * Set the target for this instruction.  Should only be used by InstructionHandle.
     * @param matchIdx The case index.
     * @param target The target for the case.
     */
    final void setMatchTarget(int matchIdx, InstructionHandle target) {
        targets[matchIdx] = target;
    }


    /**
     * Get the number of MatchOffset pairs.
     * @return The number of MatchOffset pairs.
     */
    public int getMatchCount() {
        return match_length;
    }
}
