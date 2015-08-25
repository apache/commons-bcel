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

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.bcel6.util.ByteSequence;

/** 
 * Read a list of instructions from byte code
 */
public class InstructionListParser {


    final private InstructionList instructionList; // The list being appended to.
    final private InstructionHandle[] instructionHandles; // The handles for each read instruction
    final private int[] positions; // The positions of each instruction
    final private int count; // The number of instructions read


    /**
     * Initialize instruction list from byte array.
     *
     * @param code byte array containing the instructions
     */
    InstructionListParser(InstructionList instructionList, byte[] code) {
        this.instructionList = instructionList;
        instructionHandles = new InstructionHandle[code.length];
        positions = new int[code.length]; // Can't be more than that
        count = createHandles(code);
        setTargets();
    }


    /**
     * Find the target instruction (handle) that corresponds to the given target
     * position (byte code offset).
     *
     * @param target target position to search for
     * @return target position's instruction handle if available
     */
    InstructionHandle findHandle(int target) {
        int idx = Arrays.binarySearch(positions, 0, count, target);
        if (idx<0) {
            return null;
        }
        return instructionHandles[idx];
    }


    /**
     * Create an object for each byte code and append them
     * to the list.
     * @param code The byte code.
     * @return The count of instructions in the byte code.
     */
    private int createHandles(byte[] code) {
        ByteSequence bytes = new ByteSequence(code);
        int count = 0; // track the actual length
        try {
            while (bytes.available() > 0) {
                // Remember byte offset and associate it with the instruction
                int off = bytes.getIndex();
                positions[count] = off;
                /* Read one instruction from the byte stream, the byte position is set
                 * accordingly.
                 */
                Instruction instruction = Instruction.readInstruction(bytes);
                InstructionHandle handle = new InstructionHandle(instructionList, instruction);
                handle.setPosition(off);
                instructionList.append(handle);
                instructionHandles[count++] = handle;
            }
        } catch (IOException e) {
            throw new ClassGenException(e.toString(), e);
        }
        return count;
    }

    /**
     * Look for BranchInstruction and update their targets; 
     * Convert offsets to instruction handles.
     */
    private void setTargets() {
        for(int i= 0; i<count; ++i) {
            Instruction instruction = instructionHandles[i].instruction;
            if (instruction instanceof BranchInstruction) {
                ((BranchInstruction)instruction).convertOffsetToInstructionHandle(this);
            }
        }
    }


    /**
     * Get the positions of the Instructions in the list.
     * @return The trimmed array of positions.
     */
    int[] getTrimmedPositions() {
        int[] rc = new int[count];
        System.arraycopy(positions, 0, rc, 0, count);
        return rc;
    }
}