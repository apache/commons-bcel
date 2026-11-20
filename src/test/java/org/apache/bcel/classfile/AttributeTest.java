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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link Attribute}.
 */
public class AttributeTest {

    /**
     * Minimal custom attribute that deliberately inherits the base {@link Attribute#toString()} implementation.
     */
    private static final class TestAttribute extends Attribute {

        private TestAttribute(final byte tag, final int nameIndex, final int length, final ConstantPool constantPool) {
            super(tag, nameIndex, length, constantPool);
        }

        @Override
        public void accept(final Visitor visitor) {
            // This custom attribute has no visitor callback or payload.
        }

        @Override
        public Attribute copy(final ConstantPool constantPool) {
            return new TestAttribute(getTag(), getNameIndex(), getLength(), constantPool);
        }
    }

    @Test
    public void testReadAttributeInvalidName() {
        final DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, 0 }));
        final Constant[] constants = new Constant[1];
        final ConstantPool constantPool = new ConstantPool(constants);
        assertThrows(ClassFormatException.class, () -> Attribute.readAttribute(dataInput, constantPool));
    }

    @Test
    public void testReadAttributeNegativeLength() {
        final DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, -1 }));
        final Constant[] constants = new Constant[1];
        final ConstantPool constantPool = new ConstantPool(constants);
        assertThrows(ClassFormatException.class, () -> Attribute.readAttribute(dataInput, constantPool));
    }

    /**
     * Preserves the standard names at both ends of the known-tag range even when the constant-pool name differs.
     */
    @Test
    public void testToStringForKnownTags() {
        final ConstantPool constantPool = new ConstantPool(new Constant[] { null, new ConstantUtf8("DifferentName") });
        assertEquals("SourceFile", new TestAttribute(Const.ATTR_SOURCE_FILE, 1, 0, constantPool).toString());
        assertEquals("RuntimeInvisibleTypeAnnotations", new TestAttribute(Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS, 1, 0, constantPool).toString());
    }

    /**
     * Formats a custom attribute parsed through a registered reader without requiring a subclass {@code toString()} override.
     * <p>
     * The serialized attribute has a custom name and an empty payload. Its reader returns an attribute with {@link Const#ATTR_UNKNOWN}, which used
     * to make the inherited method index the standard-name table at -1. The reader is removed even if an assertion fails; reparsing then checks that
     * the built-in {@link Unknown} attribute still uses its existing description.
     * </p>
     *
     * @throws IOException if the attribute cannot be parsed.
     */
    @Test
    public void testToStringForRegisteredCustomAttribute() throws IOException {
        final String name = AttributeTest.class.getName() + ".CustomAttribute";
        final ConstantPool constantPool = new ConstantPool(new Constant[] { null, new ConstantUtf8(name) });
        final byte[] bytes = { 0, 1, 0, 0, 0, 0 }; // name_index = 1, attribute_length = 0.
        Attribute.addAttributeReader(name, (UnknownAttributeReader) (nameIndex, length, input, pool) ->
            new TestAttribute(Const.ATTR_UNKNOWN, nameIndex, length, pool));
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final TestAttribute attribute = assertInstanceOf(TestAttribute.class, Attribute.readAttribute(input, constantPool));
            assertEquals(Const.ATTR_UNKNOWN, attribute.getTag());
            assertEquals(name, attribute.toString());
        } finally {
            Attribute.removeAttributeReader(name);
        }
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final Unknown attribute = assertInstanceOf(Unknown.class, Attribute.readAttribute(input, constantPool));
            assertEquals("(Unknown attribute " + name + ")", attribute.toString());
        }
    }

    /**
     * Uses the declared name for negative tags, including {@link Const#ATTR_UNKNOWN}, and tags above the standard-name table.
     *
     * @param tag an unrecognized attribute tag.
     */
    @ParameterizedTest
    @ValueSource(ints = { Byte.MIN_VALUE, Const.ATTR_UNKNOWN, Const.KNOWN_ATTRIBUTES, Byte.MAX_VALUE })
    public void testToStringForUnrecognizedTags(final int tag) {
        final String name = "CustomAttribute";
        final ConstantPool constantPool = new ConstantPool(new Constant[] { null, new ConstantUtf8(name) });
        assertEquals(name, new TestAttribute((byte) tag, 1, 0, constantPool).toString());
    }
}
