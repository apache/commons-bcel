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
        assertEquals("annotationCount", 2, getVisitor().annotationCount);
    }

    public void testAnnotationDefaultCount()
    {
        assertEquals("annotationDefaultCount", 0, getVisitor().annotationDefaultCount);
    }

    public void testAnnotationEntryCount()
    {
        assertEquals("annotationEntryCount", 2, getVisitor().annotationEntryCount);
    }

    public void testCodeCount()
    {
        assertEquals("codeCount", 1, getVisitor().codeCount);
    }

    public void testCodeExceptionCount()
    {
        assertEquals("codeExceptionCount", 0, getVisitor().codeExceptionCount);
    }

    public void testConstantClassCount()
    {
        assertEquals("constantClassCount", 2, getVisitor().constantClassCount);
    }

    public void testConstantDoubleCount()
    {
        assertEquals("constantDoubleCount", 0, getVisitor().constantDoubleCount);
    }

    public void testConstantFieldrefCount()
    {
        assertEquals("constantFieldrefCount", 0, getVisitor().constantFieldrefCount);
    }

    public void testConstantFloatCount()
    {
        assertEquals("constantFloatCount", 0, getVisitor().constantFloatCount);
    }

    public void testConstantIntegerCount()
    {
        assertEquals("constantIntegerCount", 0, getVisitor().constantIntegerCount);
    }

    public void testConstantInterfaceMethodrefCount()
    {
        assertEquals("constantInterfaceMethodrefCount", 0, getVisitor().constantInterfaceMethodrefCount);
    }

    public void testConstantLongCount()
    {
        assertEquals("constantLongCount", 0, getVisitor().constantLongCount);
    }

    public void testConstantMethodrefCount()
    {
        assertEquals("constantMethodrefCount", 1, getVisitor().constantMethodrefCount);
    }

    public void testConstantNameAndTypeCount()
    {
        assertEquals("constantNameAndTypeCount", 1, getVisitor().constantNameAndTypeCount);
    }

    public void testConstantPoolCount()
    {
        assertEquals("constantPoolCount", 1, getVisitor().constantPoolCount);
    }

    public void testConstantStringCount()
    {
        assertEquals("constantStringCount", 0, getVisitor().constantStringCount);
    }

    public void testConstantValueCount()
    {
        assertEquals("constantValueCount", 0, getVisitor().constantValueCount);
    }

    public void testDeprecatedCount()
    {
        assertEquals("deprecatedCount", 0, getVisitor().deprecatedCount);
    }

    public void testEnclosingMethodCount()
    {
        assertEquals("enclosingMethodCount", 0, getVisitor().enclosingMethodCount);
    }

    public void testExceptionTableCount()
    {
        assertEquals("exceptionTableCount", 0, getVisitor().exceptionTableCount);
    }

    public void testFieldCount()
    {
        assertEquals("fieldCount", 0, getVisitor().fieldCount);
    }

    public void testInnerClassCount()
    {
        assertEquals("innerClassCount", 0, getVisitor().innerClassCount);
    }

    public void testInnerClassesCount()
    {
        assertEquals("innerClassesCount", 0, getVisitor().innerClassesCount);
    }

    public void testJavaClassCount()
    {
        assertEquals("javaClassCount", 1, getVisitor().javaClassCount);
    }

    public void testLineNumberCount()
    {
        assertEquals("lineNumberCount", 1, getVisitor().lineNumberCount);
    }

    public void testLineNumberTableCount()
    {
        assertEquals("lineNumberTableCount", 1, getVisitor().lineNumberTableCount);
    }

    public void testLocalVariableCount()
    {
        assertEquals("localVariableCount", 1, getVisitor().localVariableCount);
    }

    public void testLocalVariableTableCount()
    {
        assertEquals("localVariableTableCount", 1, getVisitor().localVariableTableCount);
    }

    public void testLocalVariableTypeTableCount()
    {
        assertEquals("localVariableTypeTableCount", 0, getVisitor().localVariableTypeTableCount);
    }

    public void testMethodCount()
    {
        assertEquals("methodCount", 1, getVisitor().methodCount);
    }

    public void testParameterAnnotationCount()
    {
        assertEquals("parameterAnnotationCount", 0, getVisitor().parameterAnnotationCount);
    }

    public void testSignatureCount()
    {
        assertEquals("signatureAnnotationCount", 0, getVisitor().signatureAnnotationCount);
    }

    public void testSourceFileCount()
    {
        assertEquals("sourceFileCount", 1, getVisitor().sourceFileCount);
    }

    public void testStackMapCount()
    {
        assertEquals("stackMapCount", 0, getVisitor().stackMapCount);
    }

    public void testStackMapEntryCount()
    {
        assertEquals("stackMapEntryCount", 0, getVisitor().stackMapEntryCount);
    }

    public void testSyntheticCount()
    {
        assertEquals("syntheticCount", 0, getVisitor().syntheticCount);
    }

    public void testUnknownCount()
    {
        assertEquals("unknownCount", 0, getVisitor().unknownCount);
    }
}
