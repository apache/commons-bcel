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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class SWITCHTest {

    /**
     * {@link SWITCH} documents that the key array is sorted internally while the caller's arrays are left unaltered,
     * so an unsorted match array whose sorted form is gap-free must produce a TABLESWITCH whose case values are
     * ascending and paired with their targets. Filling the table from the unsorted originals instead yields
     * out-of-order match values padded with the default target.
     */
    @Test
    void testUnsortedMatchBuildsSortedTableSwitch() {
        final InstructionList il = new InstructionList();
        final InstructionHandle defaultTarget = il.append(InstructionConst.NOP);
        final InstructionHandle target1 = il.append(InstructionConst.NOP);
        final InstructionHandle target2 = il.append(InstructionConst.NOP);
        final InstructionHandle target3 = il.append(InstructionConst.NOP);
        // Sorted form {1, 2, 3} is gap-free, so a TABLESWITCH is generated.
        final int[] match = {2, 1, 3};
        final InstructionHandle[] targets = {target2, target1, target3};
        final Select select = (Select) new SWITCH(match, targets, defaultTarget).getInstruction();
        assertInstanceOf(TABLESWITCH.class, select);
        assertArrayEquals(new int[] {1, 2, 3}, select.getMatchs());
        assertArrayEquals(new InstructionHandle[] {target1, target2, target3}, select.getTargets());
    }
}
