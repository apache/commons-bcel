package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;

public class AnnotationAccessFlagTestCase extends AbstractTestCase
{
	/**
	 * If you write an annotation and compile it, the class file generated
	 * should be marked as an annotation type - which is detectable through
	 * BCEL.
	 */
	public void testAnnotationClassSaysItIs() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.SimpleAnnotation");
		assertTrue(
				"Expected SimpleAnnotation class to say it was an annotation - but it didn't !",
				clazz.isAnnotation());
		clazz = getTestClass("org.apache.bcel.data.SimpleClass");
		assertTrue(
				"Expected SimpleClass class to say it was not an annotation - but it didn't !",
				!clazz.isAnnotation());
	}
}
