package org.apache.bcel;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.SyntheticRepository;

public class AbstractTestCase extends TestCase
{
	private boolean verbose = false;

	protected JavaClass getTestClass(String name) throws ClassNotFoundException
	{
		return SyntheticRepository.getInstance().loadClass(name);
	}

	protected Method getMethod(JavaClass cl, String methodname)
	{
		Method[] methods = cl.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method m = methods[i];
			if (m.getName().equals(methodname))
			{
				return m;
			}
		}
		return null;
	}

	protected Attribute findAttribute(String name, Attribute[] all)
	{
		List chosenAttrsList = new ArrayList();
		for (int i = 0; i < all.length; i++)
		{
			if (verbose)
				System.err.println("Attribute: " + all[i].getName());
			if (all[i].getName().equals(name))
				chosenAttrsList.add(all[i]);
		}
		assertTrue("Should be one match: " + chosenAttrsList.size(),
				chosenAttrsList.size() == 1);
		return (Attribute) chosenAttrsList.get(0);
	}
}
