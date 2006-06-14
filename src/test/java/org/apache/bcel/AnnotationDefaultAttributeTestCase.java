package org.apache.bcel;

import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SimpleElementValue;

public class AnnotationDefaultAttributeTestCase extends AbstractTestCase
{
	/**
	 * For values in an annotation that have default values, we should be able
	 * to query the AnnotationDefault attribute against the method to discover
	 * the default value that was originally declared.
	 */
	public void testMethodAnnotations() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.SimpleAnnotation");
		Method m = getMethod(clazz, "fruit");
		AnnotationDefault a = (AnnotationDefault) findAttribute(
				"AnnotationDefault", m.getAttributes());
		SimpleElementValue val = (SimpleElementValue) a.getDefaultValue();
		assertTrue("Should be STRING but is " + val.getElementValueType(), val
				.getElementValueType() == ElementValue.STRING);
		assertTrue("Should have default of bananas but default is "
				+ val.getValueString(), val.getValueString().equals("bananas"));
	}
}
