/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;

public class CounterVisitorTestCase extends AbstractCounterVisitorTestCase
{
	@Override
    protected JavaClass getTestClass() throws ClassNotFoundException
	{
		return getTestClass("org.apache.bcel.data.MarkedType");
	}

	public void testAnnotationsCount()
	{
		// System.out
		// .println("AnnotationsCount = " + getVisitor().annotationCount);
		assertTrue(getVisitor().annotationCount == 2);
	}

	public void testAnnotationDefaultCount()
	{
		// System.out.println("AnnotationDefaultCount = "
		// + getVisitor().annotationDefaultCount);
		assertTrue(getVisitor().annotationDefaultCount == 0);
	}

	public void testAnnotationEntryCount()
	{
		// System.out.println("AnnotationEntryCount = "
		// + getVisitor().annotationEntryCount);
		assertTrue(getVisitor().annotationEntryCount == 0);
	}

	public void testCodeCount()
	{
		// System.out.println("CodeCount = " + getVisitor().codeCount);
		assertTrue(getVisitor().codeCount == 1);
	}

	public void testCodeExceptionCount()
	{
		// System.out.println("CodeExceptionCount = "
		// + getVisitor().codeExceptionCount);
		assertTrue(getVisitor().codeExceptionCount == 0);
	}

	public void testConstantClassCount()
	{
		// System.out.println("ConstantClassCount = "
		// + getVisitor().constantClassCount);
		assertTrue(getVisitor().constantClassCount == 2);
	}

	public void testConstantDoubleCount()
	{
		// System.out.println("ConstantDoubleCount = "
		// + getVisitor().constantDoubleCount);
		assertTrue(getVisitor().constantDoubleCount == 0);
	}

	public void testConstantFieldrefCount()
	{
		// System.out.println("ConstantFieldrefCount = "
		// + getVisitor().constantFieldrefCount);
		assertTrue(getVisitor().constantFieldrefCount == 0);
	}

	public void testConstantFloatCount()
	{
		// System.out.println("ConstantFloatCount = "
		// + getVisitor().constantFloatCount);
		assertTrue(getVisitor().constantFloatCount == 0);
	}

	public void testConstantIntegerCount()
	{
		// System.out.println("ConstantIntegerCount = "
		// + getVisitor().constantIntegerCount);
		assertTrue(getVisitor().constantIntegerCount == 0);
	}

	public void testConstantInterfaceMethodrefCount()
	{
		// System.out.println("ConstantInterfaceMethodrefCount = "
		// + getVisitor().constantInterfaceMethodrefCount);
		assertTrue(getVisitor().constantInterfaceMethodrefCount == 0);
	}

	public void testConstantLongCount()
	{
		// System.out.println("ConstantLongCount = "
		// + getVisitor().constantLongCount);
		assertTrue(getVisitor().constantLongCount == 0);
	}

	public void testConstantMethodrefCount()
	{
		// System.out.println("ConstantMethodrefCount = "
		// + getVisitor().constantMethodrefCount);
		assertTrue(getVisitor().constantMethodrefCount == 1);
	}

	public void testConstantNameAndTypeCount()
	{
		// System.out.println("ConstantNameAndTypeCount = "
		// + getVisitor().constantNameAndTypeCount);
		assertTrue(getVisitor().constantNameAndTypeCount == 1);
	}

	public void testConstantPoolCount()
	{
		// System.out.println("ConstantPoolCount = "
		// + getVisitor().constantPoolCount);
		assertTrue(getVisitor().constantPoolCount == 1);
	}

	public void testConstantStringCount()
	{
		// System.out.println("ConstantStringCount = "
		// + getVisitor().constantStringCount);
		assertTrue(getVisitor().constantStringCount == 0);
	}

	public void testConstantValueCount()
	{
		// System.out.println("ConstantValueCount = "
		// + getVisitor().constantValueCount);
		assertTrue(getVisitor().constantValueCount == 0);
	}

	public void testDeprecatedCount()
	{
		// System.out.println("DeprecatedCount = " +
		// getVisitor().deprecatedCount);
		assertTrue(getVisitor().deprecatedCount == 0);
	}

	public void testEnclosingMethodCount()
	{
		// System.out.println("EnclosingMethodCount = "
		// + getVisitor().enclosingMethodCount);
		assertTrue(getVisitor().enclosingMethodCount == 0);
	}

	public void testExceptionTableCount()
	{
		// System.out.println("ExceptionTableCount = "
		// + getVisitor().exceptionTableCount);
		assertTrue(getVisitor().exceptionTableCount == 0);
	}

	public void testFieldCount()
	{
		// System.out.println("FieldCount = " + getVisitor().fieldCount);
		assertTrue(getVisitor().fieldCount == 0);
	}

	public void testInnerClassCount()
	{
		// System.out.println("InnerClassCount = " +
		// getVisitor().innerClassCount);
		assertTrue(getVisitor().innerClassCount == 0);
	}

	public void testInnerClassesCount()
	{
		// System.out.println("InnerClassesCount = "
		// + getVisitor().innerClassesCount);
		assertTrue(getVisitor().innerClassesCount == 0);
	}

	public void testJavaClassCount()
	{
		// System.out.println("JavaClassCount = " +
		// getVisitor().javaClassCount);
		assertTrue(getVisitor().javaClassCount == 1);
	}

	public void testLineNumberCount()
	{
		// System.out.println("LineNumberCount = " +
		// getVisitor().lineNumberCount);
		assertTrue(getVisitor().lineNumberCount == 1);
	}

	public void testLineNumberTableCount()
	{
		// System.out.println("LineNumberTableCount = "
		// + getVisitor().lineNumberTableCount);
		assertTrue(getVisitor().lineNumberTableCount == 1);
	}

	public void testLocalVariableCount()
	{
		// System.out.println("LocalVariableCount = "
		// + getVisitor().localVariableCount);
		assertTrue(getVisitor().localVariableCount == 1);
	}

	public void testLocalVariableTableCount()
	{
		// System.out.println("LocalVariableTableCount = "
		// + getVisitor().localVariableTableCount);
		assertTrue(getVisitor().localVariableTableCount == 1);
	}

	public void testLocalVariableTypeTableCount()
	{
		// System.out.println("LocalVariableTypeTableCount = "
		// + getVisitor().localVariableTypeTableCount);
		assertTrue(getVisitor().localVariableTypeTableCount == 0);
	}

	public void testMethodCount()
	{
		// System.out.println("MethodCount = " + getVisitor().methodCount);
		assertTrue(getVisitor().methodCount == 1);
	}

	public void testParameterAnnotationCount()
	{
		// System.out.println("ParameterAnnotationCount = "
		// + getVisitor().methodCount);
		assertTrue(getVisitor().methodCount == 1);
	}

	public void testSignatureCount()
	{
		// System.out.println("SignatureCount = "
		// + getVisitor().signatureAnnotationCount);
		assertTrue(getVisitor().signatureAnnotationCount == 0);
	}

	public void testSourceFileCount()
	{
		// System.out.println("SourceFileCount = " +
		// getVisitor().sourceFileCount);
		assertTrue(getVisitor().sourceFileCount == 1);
	}

	public void testStackMapCount()
	{
		// System.out.println("StackMapCount = " + getVisitor().stackMapCount);
		assertTrue(getVisitor().stackMapCount == 0);
	}

	public void testStackMapEntryCount()
	{
		// System.out.println("StackMapEntryCount = "
		// + getVisitor().stackMapEntryCount);
		assertTrue(getVisitor().stackMapEntryCount == 0);
	}

	public void testSyntheticCount()
	{
		// System.out.println("SyntheticCount = " +
		// getVisitor().syntheticCount);
		assertTrue(getVisitor().syntheticCount == 0);
	}

	public void testUnknownCount()
	{
		// System.out.println("UnknownCount = " + getVisitor().unknownCount);
		assertTrue(getVisitor().unknownCount == 0);
	}
}