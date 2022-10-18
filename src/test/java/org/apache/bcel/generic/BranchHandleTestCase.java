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
 */
package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BranchHandleTestCase {

    @Test
    public void testGetBHnull() {
        assertThrows(ClassGenException.class, () -> BranchHandle.getBranchHandle(null));
    }

    @Test
    public void testsetInstructionBI() {
        final BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        assertNotNull(bh);
        bh.setInstruction(new GOTO(null));
        assertNotNull(bh);
    }

    @Test
    public void testsetInstructionnotBI() {
        final BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        assertNotNull(bh);
        assertThrows(ClassGenException.class, () -> bh.setInstruction(new NOP()));
    }

    // Test that setInstruction only allows BranchInstructions
    @Test
    public void testsetInstructionNull() {
        final BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        assertNotNull(bh);
        assertThrows(ClassGenException.class, () -> bh.setInstruction(null));
    }
}
