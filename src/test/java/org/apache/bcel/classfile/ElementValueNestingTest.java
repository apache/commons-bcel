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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests that {@link ElementValue#readElementValue(java.io.DataInput, ConstantPool)} bounds the combined annotation/array nesting depth instead of recursing
 * until a {@link StackOverflowError}.
 */
class ElementValueNestingTest {

    /**
     * Wraps the given element value bytes into an annotation element value ('@'): one annotation with a single element value pair.
     */
    private static byte[] annotationOf(final byte[] inner) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeByte(ElementValue.ANNOTATION);
            dos.writeShort(0); // type_index
            dos.writeShort(1); // num_element_value_pairs
            dos.writeShort(0); // element_name_index
            dos.write(inner);
        }
        return baos.toByteArray();
    }

    /**
     * Wraps the given element value bytes into an array element value ('[') with a single element.
     */
    private static byte[] arrayOf(final byte[] inner) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeByte(ElementValue.ARRAY);
            dos.writeShort(1); // num_values
            dos.write(inner);
        }
        return baos.toByteArray();
    }

    private static byte[] stringValue() {
        return new byte[] {ElementValue.STRING, 0, 0};
    }

    @Test
    void testAlternatingArrayAnnotationNestingRejected() throws IOException {
        byte[] bytes = stringValue();
        for (int i = 0; i < 300; i++) {
            bytes = arrayOf(annotationOf(bytes));
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertThrows(ClassFormatException.class, () -> ElementValue.readElementValue(in, new ConstantPool(new ConstantUtf8("Test"))));
        }
    }

    @Test
    void testDeeplyNestedAnnotationsRejected() throws IOException {
        byte[] bytes = stringValue();
        for (int i = 0; i < 300; i++) {
            bytes = annotationOf(bytes);
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertThrows(ClassFormatException.class, () -> ElementValue.readElementValue(in, new ConstantPool(new ConstantUtf8("Test"))));
        }
    }

    @Test
    void testModeratelyNestedAnnotationsAccepted() throws IOException {
        byte[] bytes = stringValue();
        for (int i = 0; i < 3; i++) {
            bytes = annotationOf(arrayOf(bytes));
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertTrue(ElementValue.readElementValue(in, new ConstantPool(new ConstantUtf8("Test"))) instanceof AnnotationElementValue);
        }
    }
}
