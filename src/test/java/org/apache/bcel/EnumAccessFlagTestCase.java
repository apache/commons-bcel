package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;

public class EnumAccessFlagTestCase extends AbstractTestCase
{
	/**
	 * An enumerated type, once compiled, should result in a class file that is
	 * marked such that we can determine from the access flags (through BCEL)
	 * that it was originally an enum type declaration.
	 */
	public void testEnumClassSaysItIs() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.SimpleEnum");
		assertTrue(
				"Expected SimpleEnum class to say it was an enum - but it didn't !",
				clazz.isEnum());
		clazz = getTestClass("org.apache.bcel.data.SimpleClass");
		assertTrue(
				"Expected SimpleClass class to say it was not an enum - but it didn't !",
				!clazz.isEnum());
	}
}