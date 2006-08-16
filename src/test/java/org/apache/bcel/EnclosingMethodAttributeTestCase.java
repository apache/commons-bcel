package org.apache.bcel;

import java.io.File;
import java.io.IOException;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EnclosingMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.SyntheticRepository;

public class EnclosingMethodAttributeTestCase extends AbstractTestCase
{
	/**
	 * Verify for an inner class declared inside the 'main' method that the
	 * enclosing method attribute is set correctly.
	 */
	public void testCheckMethodLevelNamedInnerClass()
			throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AttributeTestClassEM01$1S");
		ConstantPool pool = clazz.getConstantPool();
		Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
		assertTrue("Expected 1 EnclosingMethod attribute but found "
				+ encMethodAttrs.length, encMethodAttrs.length == 1);
		EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
		String enclosingClassName = em.getEnclosingClass().getBytes(pool);
		String enclosingMethodName = em.getEnclosingMethod().getName(pool);
		assertTrue(
				"Expected class name to be 'org/apache/bcel/data/AttributeTestClassEM01' but was "
						+ enclosingClassName, enclosingClassName
						.equals("org/apache/bcel/data/AttributeTestClassEM01"));
		assertTrue("Expected method name to be 'main' but was "
				+ enclosingMethodName, enclosingMethodName.equals("main"));
	}

	/**
	 * Verify for an inner class declared at the type level that the
	 * EnclosingMethod attribute is set correctly (i.e. to a null value)
	 */
	public void testCheckClassLevelNamedInnerClass()
			throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AttributeTestClassEM02$1");
		ConstantPool pool = clazz.getConstantPool();
		Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
		assertTrue("Expected 1 EnclosingMethod attribute but found "
				+ encMethodAttrs.length, encMethodAttrs.length == 1);
		EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
		String enclosingClassName = em.getEnclosingClass().getBytes(pool);
		assertTrue(
				"The class is not within a method, so method_index should be null, but it is "
						+ em.getEnclosingMethodIndex(), em
						.getEnclosingMethodIndex() == 0);
		assertTrue(
				"Expected class name to be 'org/apache/bcel/data/AttributeTestClassEM02' but was "
						+ enclosingClassName, enclosingClassName
						.equals("org/apache/bcel/data/AttributeTestClassEM02"));
	}

	/**
	 * Check that we can save and load the attribute correctly.
	 */
	public void testAttributeSerializtion() throws ClassNotFoundException,
			IOException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.AttributeTestClassEM02$1");
		ConstantPool pool = clazz.getConstantPool();
		Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
		assertTrue("Expected 1 EnclosingMethod attribute but found "
				+ encMethodAttrs.length, encMethodAttrs.length == 1);
		// Write it out
		File tfile = createTestdataFile("AttributeTestClassEM02$1.class");
		clazz.dump(tfile);
		// Read in the new version and check it is OK
		SyntheticRepository repos2 = createRepos(".");
		JavaClass clazz2 = repos2.loadClass("AttributeTestClassEM02$1");
		EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
		String enclosingClassName = em.getEnclosingClass().getBytes(pool);
		assertTrue(
				"The class is not within a method, so method_index should be null, but it is "
						+ em.getEnclosingMethodIndex(), em
						.getEnclosingMethodIndex() == 0);
		assertTrue(
				"Expected class name to be 'org/apache/bcel/data/AttributeTestClassEM02' but was "
						+ enclosingClassName, enclosingClassName
						.equals("org/apache/bcel/data/AttributeTestClassEM02"));
		tfile.deleteOnExit();
	}
}