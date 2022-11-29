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
 */

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

public class CounterVisitorTestCase extends AbstractCounterVisitorTestCase {
    @Override
    protected JavaClass getTestClass() throws ClassNotFoundException {
        return getTestJavaClass(PACKAGE_BASE_NAME + ".data.MarkedType");
    }

    @Test
    public void testAnnotationDefaultCount() {
        assertEquals(0, getVisitor().annotationDefaultCount, "annotationDefaultCount");
    }

    @Test
    public void testAnnotationEntryCount() {
        assertEquals(2, getVisitor().annotationEntryCount, "annotationEntryCount");
    }

    @Test
    public void testAnnotationsCount() {
        assertEquals(2, getVisitor().annotationCount, "annotationCount");
    }

    @Test
    public void testCodeCount() {
        assertEquals(1, getVisitor().codeCount, "codeCount");
    }

    @Test
    public void testCodeExceptionCount() {
        assertEquals(0, getVisitor().codeExceptionCount, "codeExceptionCount");
    }

    @Test
    public void testConstantClassCount() {
        assertEquals(2, getVisitor().constantClassCount, "constantClassCount");
    }

    @Test
    public void testConstantDoubleCount() {
        assertEquals(0, getVisitor().constantDoubleCount, "constantDoubleCount");
    }

    @Test
    public void testConstantFieldrefCount() {
        assertEquals(0, getVisitor().constantFieldrefCount, "constantFieldrefCount");
    }

    @Test
    public void testConstantFloatCount() {
        assertEquals(0, getVisitor().constantFloatCount, "constantFloatCount");
    }

    @Test
    public void testConstantIntegerCount() {
        assertEquals(0, getVisitor().constantIntegerCount, "constantIntegerCount");
    }

    @Test
    public void testConstantInterfaceMethodrefCount() {
        assertEquals(0, getVisitor().constantInterfaceMethodrefCount, "constantInterfaceMethodrefCount");
    }

    @Test
    public void testConstantLongCount() {
        assertEquals(0, getVisitor().constantLongCount, "constantLongCount");
    }

    @Test
    public void testConstantMethodrefCount() {
        assertEquals(1, getVisitor().constantMethodrefCount, "constantMethodrefCount");
    }

    @Test
    public void testConstantNameAndTypeCount() {
        assertEquals(1, getVisitor().constantNameAndTypeCount, "constantNameAndTypeCount");
    }

    @Test
    public void testConstantPoolCount() {
        assertEquals(1, getVisitor().constantPoolCount, "constantPoolCount");
    }

    @Test
    public void testConstantStringCount() {
        assertEquals(0, getVisitor().constantStringCount, "constantStringCount");
    }

    @Test
    public void testConstantValueCount() {
        assertEquals(0, getVisitor().constantValueCount, "constantValueCount");
    }

    @Test
    public void testDeprecatedCount() {
        assertEquals(0, getVisitor().deprecatedCount, "deprecatedCount");
    }

    @Test
    public void testEnclosingMethodCount() {
        assertEquals(0, getVisitor().enclosingMethodCount, "enclosingMethodCount");
    }

    @Test
    public void testExceptionTableCount() {
        assertEquals(0, getVisitor().exceptionTableCount, "exceptionTableCount");
    }

    @Test
    public void testFieldCount() {
        assertEquals(0, getVisitor().fieldCount, "fieldCount");
    }

    @Test
    public void testInnerClassCount() {
        assertEquals(0, getVisitor().innerClassCount, "innerClassCount");
    }

    @Test
    public void testInnerClassesCount() {
        assertEquals(0, getVisitor().innerClassesCount, "innerClassesCount");
    }

    @Test
    public void testJavaClassCount() {
        assertEquals(1, getVisitor().javaClassCount, "javaClassCount");
    }

    @Test
    public void testLineNumberCount() {
        assertEquals(1, getVisitor().lineNumberCount, "lineNumberCount");
    }

    @Test
    public void testLineNumberTableCount() {
        assertEquals(1, getVisitor().lineNumberTableCount, "lineNumberTableCount");
    }

    @Test
    public void testLocalVariableCount() {
        assertEquals(1, getVisitor().localVariableCount, "localVariableCount");
    }

    @Test
    public void testLocalVariableTableCount() {
        assertEquals(1, getVisitor().localVariableTableCount, "localVariableTableCount");
    }

    @Test
    public void testLocalVariableTypeTableCount() {
        assertEquals(0, getVisitor().localVariableTypeTableCount, "localVariableTypeTableCount");
    }

    @Test
    public void testMethodCount() {
        assertEquals(1, getVisitor().methodCount, "methodCount");
    }

    @Test
    public void testParameterAnnotationCount() {
        assertEquals(0, getVisitor().parameterAnnotationCount, "parameterAnnotationCount");
    }

    @Test
    public void testSignatureCount() {
        assertEquals(0, getVisitor().signatureAnnotationCount, "signatureAnnotationCount");
    }

    @Test
    public void testSourceFileCount() {
        assertEquals(1, getVisitor().sourceFileCount, "sourceFileCount");
    }

    @Test
    public void testStackMapCount() {
        assertEquals(0, getVisitor().stackMapCount, "stackMapCount");
    }

    @Test
    public void testStackMapEntryCount() {
        assertEquals(0, getVisitor().stackMapEntryCount, "stackMapEntryCount");
    }

    @Test
    public void testStackMapTypeCount() {
        assertEquals(0, getVisitor().stackMapTypeCount, "stackMapTypeCount");
    }

    @Test
    public void testSyntheticCount() {
        assertEquals(0, getVisitor().syntheticCount, "syntheticCount");
    }

    @Test
    public void testUnknownCount() {
        assertEquals(0, getVisitor().unknownCount, "unknownCount");
    }
}
