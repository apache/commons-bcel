package org.apache.commons.bcel6.generic;

import org.junit.Assert;
import org.junit.Test;

public class InstructionHandleTestCase {

    @Test(expected=NullPointerException.class)
    public void testsetInstructionNull() {
        InstructionList il = new InstructionList();
        InstructionHandle ih = il.append(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(null);
    }

    @Test
    public void testsetInstructionI() {
        InstructionList il = new InstructionList();
        InstructionHandle ih = il.append(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new NOP());        
        Assert.assertNotNull(ih.getInstruction());
    }

    @Test
    public void testsetInstructionnotI() {
        InstructionList il = new InstructionList();
        InstructionHandle ih = il.append(new NOP());// have to start with a valid non BI
        Assert.assertNotNull(ih);
        ih.setInstruction(new GOTO(null));        
        Assert.assertNotNull(ih.getInstruction());
    }

    @Test(expected=NullPointerException.class)
    public void testGetIHnull() {
        InstructionList il = new InstructionList();
        il.append((Instruction)null); 
    }
}
