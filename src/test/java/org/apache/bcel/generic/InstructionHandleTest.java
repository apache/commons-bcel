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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class InstructionHandleTest {

    // Test that setInstruction only allows Instructions that are not BranchInstructions

    @Test
    void testBCEL195() {
        final InstructionList il = new InstructionList();
        final InstructionHandle ih = il.append(InstructionConst.NOP);
        new TABLESWITCH(new int[0], InstructionHandle.EMPTY_ARRAY, ih);
        new TABLESWITCH(new int[0], InstructionHandle.EMPTY_ARRAY, ih);
    }

    @Test
    void testGetIHnull() {
        assertThrows(ClassGenException.class, () -> InstructionHandle.getInstructionHandle(null));
    }

    @Test
    void testsetInstructionI() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP()); // have to start with a valid non BI
        assertNotNull(ih);
        ih.setInstruction(new NOP());
        assertNotNull(ih);
    }

    @Test
    void testsetInstructionnotI() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP()); // have to start with a valid non BI
        assertNotNull(ih);
        assertThrows(ClassGenException.class, () -> ih.setInstruction(new GOTO(null)));
    }

    @Test
    void testsetInstructionNull() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP()); // have to start with a valid non BI
        assertNotNull(ih);
        assertThrows(ClassGenException.class, () -> ih.setInstruction(null));
    }
}
