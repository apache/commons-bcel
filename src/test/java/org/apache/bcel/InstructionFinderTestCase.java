package org.apache.bcel;

import java.util.Iterator;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.util.InstructionFinder;
import org.apache.bcel.util.InstructionFinder.CodeConstraint;

public class InstructionFinderTestCase extends AbstractTestCase
{
	public void testSearchAll() throws Exception
	{
		JavaClass clazz = getTestClass("org.apache.bcel.util.InstructionFinder");
		Method[] methods = clazz.getMethods();
		Method searchM = null;
		for (Method m : methods)
		{
			if (m.getName().equals("search") && (m.getArgumentTypes().length == 3))
			{
				searchM = m;
				break;
			}
		}
		
		if (searchM == null)
			throw new Exception("search method not found");
		
		byte[] bytes = searchM.getCode().getCode();
		InstructionList il = new InstructionList(bytes);
		InstructionFinder finder = new InstructionFinder(il);
		Iterator it = finder.search(".*", il.getStart(), null);
		
		InstructionHandle[] ihs = (InstructionHandle[])it.next();
		int size = 0;
		for (InstructionHandle ih : ihs)
		{
			size += ih.getInstruction().getLength();
		}
		assertEquals(bytes.length, size);
		
	}
}
