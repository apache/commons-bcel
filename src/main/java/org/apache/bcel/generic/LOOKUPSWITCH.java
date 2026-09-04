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

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.util.ByteSequence;

/**
 * LOOKUPSWITCH - Switch with unordered set of values
 *
 * @see SWITCH
 */
public class LOOKUPSWITCH extends Select {

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    LOOKUPSWITCH() {
    }

    /**
     * Constructs a LOOKUPSWITCH instruction.
     *
     * @param match array of match values.
     * @param targets array of branch targets.
     * @param defaultTarget default branch target.
     */
    public LOOKUPSWITCH(final int[] match, final InstructionHandle[] targets, final InstructionHandle defaultTarget) {
        super(org.apache.bcel.Const.LOOKUPSWITCH, match, targets, defaultTarget);
        /* alignment remainder assumed 0 here, until dump time. */
        final short length = (short) (9 + getMatchLength() * 8);
        super.setLength(length);
        setFixedLength(length);
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, that is, the most specific visitXXX() call comes last.
     *
     * @param v Visitor object.
     */
    @Override
    public void accept(final Visitor v) {
        v.visitVariableLengthInstruction(this);
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitSelect(this);
        v.visitLOOKUPSWITCH(this);
    }

    /**
     * Dumps instruction as byte code to stream out.
     *
     * @param out Output stream.
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        super.dump(out);
        final int matchLength = getMatchLength();
        out.writeInt(matchLength); // npairs
        for (int i = 0; i < matchLength; i++) {
            out.writeInt(super.getMatch(i)); // match-offset pairs
            out.writeInt(setIndices(i, getTargetOffset(super.getTarget(i))));
        }
    }

    /**
     * Reads needed data (for example index) from file.
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        super.initFromFile(bytes, wide); // reads padding
        final int matchLength = bytes.readInt();
        // Require the match table to actually fit into the remaining code bytes (8 bytes per match-offset pair). The npairs field is attacker-controlled in
        // a malicious class file and could otherwise request a multi-gigabyte allocation, or a negative array size, before a single pair is read.
        if (matchLength < 0 || matchLength > bytes.available() / 8) {
            throw new ClassFormatException("Invalid lookupswitch: npairs=" + matchLength + ", but only " + bytes.available() + " bytes of code remain.");
        }
        setMatchLength(matchLength);
        final short fixedLength = (short) (9 + matchLength * 8);
        setFixedLength(fixedLength);
        super.setLength((short) (fixedLength + super.getPadding()));
        super.setMatches(new int[matchLength]);
        super.setIndices(new int[matchLength]);
        super.setTargets(new InstructionHandle[matchLength]);
        for (int i = 0; i < matchLength; i++) {
            super.setMatch(i, bytes.readInt());
            super.setIndices(i, bytes.readInt());
        }
    }
}
