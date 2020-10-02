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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterVisitorTestCase extends AbstractCounterVisitorTestCase
{
    @Override
    protected JavaClass getTestClass() throws ClassNotFoundException
    {
        return getTestClass(PACKAGE_BASE_NAME+".data.MarkedType");
    }

    @Test
    public void testAnnotationsCount()
    {
        assertEquals("annotationCount", 2, getVisitor().annotationCount);
    }

    @Test
    public void testAnnotationDefaultCount()
    {
        assertEquals("annotationDefaultCount", 0, getVisitor().annotationDefaultCount);
    }

    @Test
    public void testAnnotationEntryCount()
    {
        assertEquals("annotationEntryCount", 2, getVisitor().annotationEntryCount);
    }

    @Test
    public void testCodeCount()
    {
        assertEquals("codeCount", 1, getVisitor().codeCount);
    }

    @Test
    public void testCodeExceptionCount()
    {
        assertEquals("codeExceptionCount", 0, getVisitor().codeExceptionCount);
    }

    @Test
    public void testConstantClassCount()
    {
        assertEquals("constantClassCount", 2, getVisitor().constantClassCount);
    }

    @Test
    public void testConstantDoubleCount()
    {
        assertEquals("constantDoubleCount", 0, getVisitor().constantDoubleCount);
    }

    @Test
    public void testConstantFieldrefCount()
    {
        assertEquals("constantFieldrefCount", 0, getVisitor().constantFieldrefCount);
    }

    @Test
    public void testConstantFloatCount()
    {
        assertEquals("constantFloatCount", 0, getVisitor().constantFloatCount);
    }

    @Test
    public void testConstantIntegerCount()
    {
        assertEquals("constantIntegerCount", 0, getVisitor().constantIntegerCount);
    }

    @Test
    public void testConstantInterfaceMethodrefCount()
    {
        assertEquals("constantInterfaceMethodrefCount", 0, getVisitor().constantInterfaceMethodrefCount);
    }

    @Test
    public void testConstantLongCount()
    {
        assertEquals("constantLongCount", 0, getVisitor().constantLongCount);
    }

    @Test
    public void testConstantMethodrefCount()
    {
        assertEquals("constantMethodrefCount", 1, getVisitor().constantMethodrefCount);
    }

    @Test
    public void testConstantNameAndTypeCount()
    {
        assertEquals("constantNameAndTypeCount", 1, getVisitor().constantNameAndTypeCount);
    }

    @Test
    public void testConstantPoolCount()
    {
        assertEquals("constantPoolCount", 1, getVisitor().constantPoolCount);
    }

    @Test
    public void testConstantStringCount()
    {
        assertEquals("constantStringCount", 0, getVisitor().constantStringCount);
    }

    @Test
    public void testConstantValueCount()
    {
        assertEquals("constantValueCount", 0, getVisitor().constantValueCount);
    }

    @Test
    public void testDeprecatedCount()
    {
        assertEquals("deprecatedCount", 0, getVisitor().deprecatedCount);
    }

    @Test
    public void testEnclosingMethodCount()
    {
        assertEquals("enclosingMethodCount", 0, getVisitor().enclosingMethodCount);
    }

    @Test
    public void testExceptionTableCount()
    {
        assertEquals("exceptionTableCount", 0, getVisitor().exceptionTableCount);
    }

    @Test
    public void testFieldCount()
    {
        assertEquals("fieldCount", 0, getVisitor().fieldCount);
    }

    @Test
    public void testInnerClassCount()
    {
        assertEquals("innerClassCount", 0, getVisitor().innerClassCount);
    }

    @Test
    public void testInnerClassesCount()
    {
        assertEquals("innerClassesCount", 0, getVisitor().innerClassesCount);
    }

    @Test
    public void testJavaClassCount()
    {
        assertEquals("javaClassCount", 1, getVisitor().javaClassCount);
    }

    @Test
    public void testLineNumberCount()
    {
        assertEquals("lineNumberCount", 1, getVisitor().lineNumberCount);
    }

    @Test
    public void testLineNumberTableCount()
    {
        assertEquals("lineNumberTableCount", 1, getVisitor().lineNumberTableCount);
    }

    @Test
    public void testLocalVariableCount()
    {
        assertEquals("localVariableCount", 1, getVisitor().localVariableCount);
    }

    @Test
    public void testLocalVariableTableCount()
    {
        assertEquals("localVariableTableCount", 1, getVisitor().localVariableTableCount);
    }

    @Test
    public void testLocalVariableTypeTableCount()
    {
        assertEquals("localVariableTypeTableCount", 0, getVisitor().localVariableTypeTableCount);
    }

    @Test
    public void testMethodCount()
    {
        assertEquals("methodCount", 1, getVisitor().methodCount);
    }

    @Test
    public void testParameterAnnotationCount()
    {
        assertEquals("parameterAnnotationCount", 0, getVisitor().parameterAnnotationCount);
    }

    @Test
    public void testSignatureCount()
    {
        assertEquals("signatureAnnotationCount", 0, getVisitor().signatureAnnotationCount);
    }

    @Test
    public void testSourceFileCount()
    {
        assertEquals("sourceFileCount", 1, getVisitor().sourceFileCount);
    }

    @Test
    public void testStackMapCount()
    {
        assertEquals("stackMapCount", 0, getVisitor().stackMapCount);
    }

    @Test
    public void testStackMapEntryCount()
    {
        assertEquals("stackMapEntryCount", 0, getVisitor().stackMapEntryCount);
    }

    @Test
    public void testSyntheticCount()
    {
        assertEquals("syntheticCount", 0, getVisitor().syntheticCount);
    }

    @Test
    public void testUnknownCount()
    {
        assertEquals("unknownCount", 0, getVisitor().unknownCount);
    }
}
