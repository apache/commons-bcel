package org.apache.bcel.generic;

import org.junit.Assert;
import org.junit.Test;

public class BranchHandleTestCase {

    // Test that setInstruction only allows BranchInstructions
    @Test(expected=ClassGenException.class)
    public void testsetInstructionNull() {
        BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        Assert.assertNotNull(bh);
        bh.setInstruction(null);
        Assert.assertNotNull(bh);
    }

    @Test
    public void testsetInstructionBI() {
        BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        Assert.assertNotNull(bh);
        bh.setInstruction(new GOTO(null));        
        Assert.assertNotNull(bh);
    }

    @Test(expected=ClassGenException.class)
    public void testsetInstructionnotBI() {
        BranchHandle bh = BranchHandle.getBranchHandle(new GOTO(null));// have to start with a valid BI
        Assert.assertNotNull(bh);
        bh.setInstruction(new NOP());        
        Assert.assertNotNull(bh);
    }

    @Test(expected=ClassGenException.class)
    public void testGetBHnull() {
        BranchHandle.getBranchHandle(null); 
    }
}
