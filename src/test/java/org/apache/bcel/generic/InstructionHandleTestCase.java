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

import org.junit.Assert;
import org.junit.Test;

public class InstructionHandleTestCase {

    // Test that setInstruction only allows Instructions that are not BranchInstructions

    @Test(expected=ClassGenException.class)
    public void testsetInstructionNull() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(null);
        Assert.assertNotNull(ih);
    }

    @Test
    public void testsetInstructionI() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new NOP());
        Assert.assertNotNull(ih);
    }

    @Test(expected=ClassGenException.class)
    public void testsetInstructionnotI() {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new GOTO(null));
        Assert.assertNotNull(ih);
    }

    @Test(expected=ClassGenException.class)
    public void testGetIHnull() {
        InstructionHandle.getInstructionHandle(null);
    }

    @Test
    public void testBCEL195() {
        final InstructionList il = new InstructionList();
        final InstructionHandle ih = il.append(InstructionConst.NOP);
        new TABLESWITCH(new int[0], new InstructionHandle[0], ih);
        new TABLESWITCH(new int[0], new InstructionHandle[0], ih);
    }
}
