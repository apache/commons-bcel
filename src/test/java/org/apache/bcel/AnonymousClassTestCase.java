package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;

public class AnonymousClassTestCase extends AbstractTestCase
{
	public void testRegularClassIsNotAnonymous() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AnonymousClassTest");
		assertFalse("regular outer classes are not anonymous", clazz
				.isAnonymous());
		assertFalse("regular outer classes are not nested", clazz.isNested());
	}

	public void testNamedInnerClassIsNotAnonymous()
			throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AnonymousClassTest$X");
		assertFalse("regular inner classes are not anonymous", clazz
				.isAnonymous());
		assertTrue("regular inner classes are nested", clazz.isNested());
	}

	public void testStaticInnerClassIsNotAnonymous()
			throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AnonymousClassTest$Y");
		assertFalse("regular static inner classes are not anonymous", clazz
				.isAnonymous());
		assertTrue("regular static inner classes are nested", clazz.isNested());
	}

	public void testAnonymousInnerClassIsAnonymous()
			throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AnonymousClassTest$1");
		assertTrue("anonymous inner classes are anonymous", clazz.isAnonymous());
		assertTrue("anonymous inner classes are anonymous", clazz.isNested());
	}
}