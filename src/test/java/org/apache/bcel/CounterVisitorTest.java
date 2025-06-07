/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

public class CounterVisitorTest extends AbstractCounterVisitorTest {
    @Override
    protected JavaClass getTestClass() throws ClassNotFoundException {
        return getTestJavaClass(PACKAGE_BASE_NAME + ".data.MarkedType");
    }

    @Test
    void testAnnotationDefaultCount() {
        assertEquals(0, getVisitor().annotationDefaultCount, "annotationDefaultCount");
    }

    @Test
    void testAnnotationEntryCount() {
        assertEquals(2, getVisitor().annotationEntryCount, "annotationEntryCount");
    }

    @Test
    void testAnnotationsCount() {
        assertEquals(2, getVisitor().annotationCount, "annotationCount");
    }

    @Test
    void testCodeCount() {
        assertEquals(1, getVisitor().codeCount, "codeCount");
    }

    @Test
    void testCodeExceptionCount() {
        assertEquals(0, getVisitor().codeExceptionCount, "codeExceptionCount");
    }

    @Test
    void testConstantClassCount() {
        assertEquals(2, getVisitor().constantClassCount, "constantClassCount");
    }

    @Test
    void testConstantDoubleCount() {
        assertEquals(0, getVisitor().constantDoubleCount, "constantDoubleCount");
    }

    @Test
    void testConstantFieldrefCount() {
        assertEquals(0, getVisitor().constantFieldrefCount, "constantFieldrefCount");
    }

    @Test
    void testConstantFloatCount() {
        assertEquals(0, getVisitor().constantFloatCount, "constantFloatCount");
    }

    @Test
    void testConstantIntegerCount() {
        assertEquals(0, getVisitor().constantIntegerCount, "constantIntegerCount");
    }

    @Test
    void testConstantInterfaceMethodrefCount() {
        assertEquals(0, getVisitor().constantInterfaceMethodrefCount, "constantInterfaceMethodrefCount");
    }

    @Test
    void testConstantLongCount() {
        assertEquals(0, getVisitor().constantLongCount, "constantLongCount");
    }

    @Test
    void testConstantMethodrefCount() {
        assertEquals(1, getVisitor().constantMethodrefCount, "constantMethodrefCount");
    }

    @Test
    void testConstantNameAndTypeCount() {
        assertEquals(1, getVisitor().constantNameAndTypeCount, "constantNameAndTypeCount");
    }

    @Test
    void testConstantPoolCount() {
        assertEquals(1, getVisitor().constantPoolCount, "constantPoolCount");
    }

    @Test
    void testConstantStringCount() {
        assertEquals(0, getVisitor().constantStringCount, "constantStringCount");
    }

    @Test
    void testConstantValueCount() {
        assertEquals(0, getVisitor().constantValueCount, "constantValueCount");
    }

    @Test
    void testDeprecatedCount() {
        assertEquals(0, getVisitor().deprecatedCount, "deprecatedCount");
    }

    @Test
    void testEnclosingMethodCount() {
        assertEquals(0, getVisitor().enclosingMethodCount, "enclosingMethodCount");
    }

    @Test
    void testExceptionTableCount() {
        assertEquals(0, getVisitor().exceptionTableCount, "exceptionTableCount");
    }

    @Test
    void testFieldCount() {
        assertEquals(0, getVisitor().fieldCount, "fieldCount");
    }

    @Test
    void testInnerClassCount() {
        assertEquals(0, getVisitor().innerClassCount, "innerClassCount");
    }

    @Test
    void testInnerClassesCount() {
        assertEquals(0, getVisitor().innerClassesCount, "innerClassesCount");
    }

    @Test
    void testJavaClassCount() {
        assertEquals(1, getVisitor().javaClassCount, "javaClassCount");
    }

    @Test
    void testLineNumberCount() {
        assertEquals(1, getVisitor().lineNumberCount, "lineNumberCount");
    }

    @Test
    void testLineNumberTableCount() {
        assertEquals(1, getVisitor().lineNumberTableCount, "lineNumberTableCount");
    }

    @Test
    void testLocalVariableCount() {
        assertEquals(1, getVisitor().localVariableCount, "localVariableCount");
    }

    @Test
    void testLocalVariableTableCount() {
        assertEquals(1, getVisitor().localVariableTableCount, "localVariableTableCount");
    }

    @Test
    void testLocalVariableTypeTableCount() {
        assertEquals(0, getVisitor().localVariableTypeTableCount, "localVariableTypeTableCount");
    }

    @Test
    void testMethodCount() {
        assertEquals(1, getVisitor().methodCount, "methodCount");
    }

    @Test
    void testParameterAnnotationCount() {
        assertEquals(0, getVisitor().parameterAnnotationCount, "parameterAnnotationCount");
    }

    @Test
    void testRecordCount() {
        assertEquals(0, getVisitor().recordCount, "recordCount");
    }

    @Test
    void testSignatureCount() {
        assertEquals(0, getVisitor().signatureAnnotationCount, "signatureAnnotationCount");
    }

    @Test
    void testSourceFileCount() {
        assertEquals(1, getVisitor().sourceFileCount, "sourceFileCount");
    }

    @Test
    void testStackMapCount() {
        assertEquals(0, getVisitor().stackMapCount, "stackMapCount");
    }

    @Test
    void testStackMapEntryCount() {
        assertEquals(0, getVisitor().stackMapEntryCount, "stackMapEntryCount");
    }

    @Test
    void testStackMapTypeCount() {
        assertEquals(0, getVisitor().stackMapTypeCount, "stackMapTypeCount");
    }

    @Test
    void testSyntheticCount() {
        assertEquals(0, getVisitor().syntheticCount, "syntheticCount");
    }

    @Test
    void testUnknownCount() {
        assertEquals(0, getVisitor().unknownCount, "unknownCount");
    }
}
