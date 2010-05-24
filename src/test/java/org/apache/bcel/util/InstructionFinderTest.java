package org.apache.bcel.util;

import java.util.Iterator;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.generic.IADD;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

public class InstructionFinderTest extends AbstractTestCase {

	public void testSearch() {
		InstructionList il = new InstructionList();
		il.append(new ILOAD(1));
		il.append(new ILOAD(2));
		il.append(new IADD());
		il.append(new ISTORE(3));
		InstructionFinder finder = new InstructionFinder(il);
		
		Iterator it = finder.search("ILOAD IADD", il.getInstructionHandles()[0], null );
		InstructionHandle[] ihs = (InstructionHandle[])it.next();
		assertEquals(2, ihs.length);
		assertEquals(ihs[0].getInstruction(), new ILOAD(2));
		assertEquals(ihs[1].getInstruction(), new IADD());
	}
}
