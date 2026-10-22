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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.verifier.statics.StringRepresentation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests attribute dispatch, copying, and nesting without requiring a newer source language level. */
class TypeAnnotationAttributesTest {

    private static final class Fixture {
        private final ConstantPool pool;
        private final int nameIndex;
        private final int typeIndex;
        private final int codeIndex;
        private final int recordIndex;
        private final int componentNameIndex;
        private final int descriptorIndex;

        Fixture(final boolean visible) {
            final ConstantPoolGen gen = new ConstantPoolGen();
            nameIndex = gen.addUtf8(visible ? "RuntimeVisibleTypeAnnotations" : "RuntimeInvisibleTypeAnnotations");
            typeIndex = gen.addUtf8("Lexample/Annotation;");
            codeIndex = gen.addUtf8("Code");
            recordIndex = gen.addUtf8("Record");
            componentNameIndex = gen.addUtf8("value");
            descriptorIndex = gen.addUtf8("Ljava/lang/String;");
            pool = gen.getFinalConstantPool();
        }

        byte[] annotationAttribute(final int target, final byte[] info, final int count) throws IOException {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(bytes)) {
                out.writeShort(count);
                for (int i = 0; i < count; i++) {
                    out.writeByte(target);
                    out.write(info);
                    out.writeByte(0); // empty type path
                    out.writeShort(typeIndex);
                    out.writeShort(0); // no element-value pairs
                }
            }
            return attribute(nameIndex, bytes.toByteArray());
        }

        TypeAnnotations annotations(final int count) throws IOException {
            return (TypeAnnotations) parse(annotationAttribute(0x13, new byte[0], count), pool);
        }
    }

    private static byte[] attribute(final int nameIndex, final byte[] contents) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeShort(nameIndex);
            out.writeInt(contents.length);
            out.write(contents);
        }
        return bytes.toByteArray();
    }

    private static byte[] dump(final Attribute attribute) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            attribute.dump(out);
        }
        return bytes.toByteArray();
    }

    private static Attribute parse(final byte[] bytes, final ConstantPool pool) throws IOException {
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final Attribute attribute = Attribute.readAttribute(input, pool);
            assertEquals(0, input.available());
            return attribute;
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void dispatchesAndCopiesBothAttributes(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final byte[] encoded = fixture.annotationAttribute(0x13, new byte[0], 2);
        final TypeAnnotations original = (TypeAnnotations) parse(encoded, fixture.pool);
        final String name = visible ? "RuntimeVisibleTypeAnnotations" : "RuntimeInvisibleTypeAnnotations";
        assertEquals(visible ? Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS : Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS, original.getTag());
        assertEquals(name, Const.getAttributeName(original.getTag()));
        assertEquals(visible ? RuntimeVisibleTypeAnnotations.class : RuntimeInvisibleTypeAnnotations.class, original.getClass());
        assertEquals(visible, original.isRuntimeVisible());
        assertEquals(2, original.getNumTypeAnnotations());
        assertEquals(name + ":\n  " + original.getTypeAnnotationEntries()[0] + "\n  " + original.getTypeAnnotationEntries()[1], original.toString());
        assertEquals(original.toString(), new StringRepresentation(original).toString());
        final ConstantPool otherPool = fixture.pool.copy();
        final TypeAnnotations copy = (TypeAnnotations) original.copy(otherPool);
        assertSame(otherPool, copy.getConstantPool());
        assertNotSame(original.getTypeAnnotationEntries(), copy.getTypeAnnotationEntries());
        int index = 0;
        for (final TypeAnnotationEntry entry : copy) {
            assertSame(entry, copy.getTypeAnnotationEntries()[index]);
            assertNotSame(original.getTypeAnnotationEntries()[index], entry);
            assertNotSame(original.getTypeAnnotationEntries()[index].getAnnotationEntry(), entry.getAnnotationEntry());
            assertSame(otherPool, entry.getAnnotationEntry().getConstantPool());
            assertEquals(visible, entry.getAnnotationEntry().isRuntimeVisible());
            index++;
        }
        assertEquals(2, index);
        assertArrayEquals(encoded, dump(original));
        assertArrayEquals(encoded, dump(copy));
        copy.getTypeAnnotationEntries()[0] = copy.getTypeAnnotationEntries()[1];
        assertNotSame(original.getTypeAnnotationEntries()[0], original.getTypeAnnotationEntries()[1]);
        copy.setTypeAnnotationTable(null);
        copy.setLength(2);
        assertEquals(0, copy.getNumTypeAnnotations());
        assertFalse(copy.iterator().hasNext());
        assertEquals(name + ":\n", copy.toString());
        assertArrayEquals(fixture.annotationAttribute(0x13, new byte[0], 0), dump(copy));
        assertEquals(2, original.getNumTypeAnnotations());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void readsEmptyAttributes(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final byte[] encoded = fixture.annotationAttribute(0x13, new byte[0], 0);
        final TypeAnnotations annotations = (TypeAnnotations) parse(encoded, fixture.pool);
        assertEquals(0, annotations.getNumTypeAnnotations());
        assertFalse(annotations.iterator().hasNext());
        assertArrayEquals(encoded, dump(annotations));
        assertArrayEquals(encoded, dump(annotations.copy(fixture.pool.copy())));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void rejectsTruncatedAttributes(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final byte[] encoded = fixture.annotationAttribute(0x13, new byte[0], 2);
        for (int length = 0; length < encoded.length; length++) {
            final byte[] truncated = Arrays.copyOf(encoded, length);
            assertThrows(EOFException.class, () -> parse(truncated, fixture.pool), "length " + length);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void enforcesTheUnsignedAnnotationCount(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final TypeAnnotations annotations = fixture.annotations(0);
        final TypeAnnotationEntry entry = new TypeAnnotationEntry(0x13, new byte[0], new byte[0],
                new AnnotationEntry(fixture.typeIndex, fixture.pool, visible));
        final TypeAnnotationEntry[] entries = new TypeAnnotationEntry[65535];
        Arrays.fill(entries, entry);
        annotations.setTypeAnnotationTable(entries);
        annotations.setLength(2 + entries.length * 6);
        final byte[] encoded = dump(annotations);
        assertEquals(65535, ((TypeAnnotations) parse(encoded, fixture.pool)).getNumTypeAnnotations());
        annotations.setTypeAnnotationTable(new TypeAnnotationEntry[65536]);
        assertThrows(ClassFormatException.class, () -> dump(annotations));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void readsTypeAnnotationsInsideCode(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final byte[] nested = fixture.annotationAttribute(0x40, new byte[] {0, 1, 0, 0, 0, 1, 0, 0}, 1);
        final ByteArrayOutputStream body = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(body)) {
            out.writeShort(0); // max_stack
            out.writeShort(1); // max_locals
            out.writeInt(1); // code_length
            out.writeByte(Const.RETURN);
            out.writeShort(0); // exception_table_length
            out.writeShort(1); // attributes_count
            out.write(nested);
        }
        final byte[] encoded = attribute(fixture.codeIndex, body.toByteArray());
        final Code code = (Code) parse(encoded, fixture.pool);
        assertEquals(1, code.getAttributes().length);
        final TypeAnnotations annotations = (TypeAnnotations) code.getAttributes()[0];
        assertEquals(visible, annotations.isRuntimeVisible());
        assertEquals(0x40, annotations.getTypeAnnotationEntries()[0].getTargetType());
        assertArrayEquals(new byte[] {0, 1, 0, 0, 0, 1, 0, 0}, annotations.getTypeAnnotationEntries()[0].getTargetInfo());
        assertArrayEquals(encoded, dump(code));
        assertArrayEquals(encoded, dump(code.copy(fixture.pool.copy())));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void readsTypeAnnotationsInsideRecordComponents(final boolean visible) throws IOException {
        final Fixture fixture = new Fixture(visible);
        final byte[] nested = fixture.annotationAttribute(0x13, new byte[0], 1);
        final ByteArrayOutputStream body = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(body)) {
            out.writeShort(1); // components_count
            out.writeShort(fixture.componentNameIndex);
            out.writeShort(fixture.descriptorIndex);
            out.writeShort(1); // attributes_count
            out.write(nested);
        }
        final byte[] encoded = attribute(fixture.recordIndex, body.toByteArray());
        final Record record = (Record) parse(encoded, fixture.pool);
        assertEquals(1, record.getComponents().length);
        final RecordComponentInfo component = record.getComponents()[0];
        assertEquals(1, component.getAttributes().length);
        final TypeAnnotations annotations = (TypeAnnotations) component.getAttributes()[0];
        assertSame(annotations, component.getAttribute(visible ? Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS : Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS));
        assertEquals(visible, annotations.isRuntimeVisible());
        assertTrue(annotations.iterator().hasNext());
        assertEquals(0x13, annotations.getTypeAnnotationEntries()[0].getTargetType());
        assertArrayEquals(encoded, dump(record));
    }
}
