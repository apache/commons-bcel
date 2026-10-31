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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests that {@link Attribute#readAttribute(DataInput, ConstantPool)} bounds the attribute nesting depth instead of recursing until a
 * {@link StackOverflowError}.
 */
class AttributeNestingTest {

    /**
     * Builds a Code attribute nested {@code depth} times inside itself: each level declares one sub-attribute, which is again a Code attribute.
     */
    private static byte[] nestedCodeAttribute(final int depth) throws IOException {
        byte[] inner = {};
        int attributeCount = 0;
        for (int i = 0; i < depth; i++) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                dos.writeShort(1); // attribute_name_index -> "Code"
                dos.writeInt(12 + inner.length); // attribute_length
                dos.writeShort(0); // max_stack
                dos.writeShort(0); // max_locals
                dos.writeInt(0); // code_length
                dos.writeShort(0); // exception_table_length
                dos.writeShort(attributeCount); // attributes_count
                dos.write(inner);
            }
            inner = baos.toByteArray();
            attributeCount = 1;
        }
        return inner;
    }

    @Test
    void testDeeplyNestedCodeAttributeRejected() throws IOException {
        final ConstantPool cp = new ConstantPool(new ConstantUtf8("unused index 0"), new ConstantUtf8("Code"));
        final byte[] bytes = nestedCodeAttribute(1_000);
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertThrows(ClassFormatException.class, () -> Attribute.readAttribute((DataInput) in, cp));
        }
    }

    @Test
    void testModeratelyNestedCodeAttributeAccepted() throws IOException {
        final ConstantPool cp = new ConstantPool(new ConstantUtf8("unused index 0"), new ConstantUtf8("Code"));
        final byte[] bytes = nestedCodeAttribute(3);
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertTrue(Attribute.readAttribute((DataInput) in, cp) instanceof Code);
        }
    }
}
