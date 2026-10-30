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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.bcel.generic.ConstantPoolGen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests the target layouts and type paths defined by JVMS 4.7.20, independently of BCEL's writer. */
class TypeAnnotationEntryTest {

    // Array component, inner type, type argument 255, then its wildcard bound (JVMS 4.7.20.2).
    private static final byte[] TYPE_PATH = {0, 0, 1, 0, 3, (byte) 255, 2, 0};

    private static ConstantPool constantPool() {
        return new ConstantPool(new Constant[] {null, new ConstantUtf8("Lexample/Annotation;")});
    }

    private static byte[] dump(final TypeAnnotationEntry entry) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            entry.dump(out);
        }
        return bytes.toByteArray();
    }

    private static byte[] encodedEntry(final int target, final byte[] targetInfo, final byte[] path) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            out.writeByte(target);
            out.write(targetInfo);
            out.writeByte(path.length / 2);
            out.write(path);
            out.writeShort(1); // annotation descriptor in the constant pool
            out.writeShort(0); // no element-value pairs
        }
        return bytes.toByteArray();
    }

    private static TypeAnnotationEntry read(final byte[] bytes, final boolean visible) throws IOException {
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final TypeAnnotationEntry entry = TypeAnnotationEntry.read(input, constantPool(), visible);
            assertEquals(0, input.available(), "The entry must consume exactly its encoded bytes");
            return entry;
        }
    }

    // The numeric tags and payloads are specified by JVMS tables 4.7.20-A/B and section 4.7.20.1.
    static Stream<Arguments> targetLayouts() {
        return Stream.of(
                Arguments.of(TypeAnnotationEntry.TARGET_CLASS_TYPE_PARAMETER, 0x00, new byte[] {(byte) 254}),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_TYPE_PARAMETER, 0x01, new byte[] {(byte) 253}),
                Arguments.of(TypeAnnotationEntry.TARGET_CLASS_EXTENDS, 0x10, new byte[] {(byte) 255, (byte) 255}),
                Arguments.of(TypeAnnotationEntry.TARGET_CLASS_TYPE_PARAMETER_BOUND, 0x11, new byte[] {1, 2}),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_TYPE_PARAMETER_BOUND, 0x12, new byte[] {3, 4}),
                Arguments.of(TypeAnnotationEntry.TARGET_FIELD, 0x13, new byte[0]),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_RETURN, 0x14, new byte[0]),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_RECEIVER, 0x15, new byte[0]),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_FORMAL_PARAMETER, 0x16, new byte[] {(byte) 252}),
                Arguments.of(TypeAnnotationEntry.TARGET_THROWS, 0x17, new byte[] {(byte) 128, 1}),
                Arguments.of(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE, 0x40, new byte[] {0, 1, 0, 2, 0, 3, 0, (byte) 254}),
                Arguments.of(TypeAnnotationEntry.TARGET_RESOURCE_VARIABLE, 0x41, new byte[] {0, 1, 0, 4, 0, 5, 0, (byte) 253}),
                Arguments.of(TypeAnnotationEntry.TARGET_EXCEPTION_PARAMETER, 0x42, new byte[] {1, 2}),
                Arguments.of(TypeAnnotationEntry.TARGET_INSTANCEOF, 0x43, new byte[] {3, 4}),
                Arguments.of(TypeAnnotationEntry.TARGET_NEW, 0x44, new byte[] {5, 6}),
                Arguments.of(TypeAnnotationEntry.TARGET_CONSTRUCTOR_REFERENCE, 0x45, new byte[] {7, 8}),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_REFERENCE, 0x46, new byte[] {9, 10}),
                Arguments.of(TypeAnnotationEntry.TARGET_CAST, 0x47, new byte[] {11, 12, 2}),
                Arguments.of(TypeAnnotationEntry.TARGET_CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT, 0x48, new byte[] {13, 14, 3}),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_INVOCATION_TYPE_ARGUMENT, 0x49, new byte[] {15, 16, 4}),
                Arguments.of(TypeAnnotationEntry.TARGET_CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT, 0x4A, new byte[] {17, 18, 5}),
                Arguments.of(TypeAnnotationEntry.TARGET_METHOD_REFERENCE_TYPE_ARGUMENT, 0x4B, new byte[] {19, 20, (byte) 255}));
    }

    @ParameterizedTest
    @MethodSource("targetLayouts")
    void readsAndDumpsEveryTargetLayout(final int targetConstant, final int tag, final byte[] info) throws IOException {
        assertEquals(tag, targetConstant, "The public constant must match the JVMS tag");
        final byte[] expected = encodedEntry(tag, info, new byte[0]);
        for (final boolean visible : new boolean[] {false, true}) {
            final TypeAnnotationEntry entry = read(expected, visible);
            assertEquals(tag, entry.getTargetType());
            assertArrayEquals(info, entry.getTargetInfo());
            assertEquals(0, entry.getTypePathLength());
            assertEquals("Lexample/Annotation;", entry.getAnnotationEntry().getAnnotationType());
            assertEquals(visible, entry.getAnnotationEntry().isRuntimeVisible());
            assertArrayEquals(expected, dump(entry));
            assertArrayEquals(expected, dump(new TypeAnnotationEntry(targetConstant, info, new byte[0], entry.getAnnotationEntry())));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void readsEveryPathKindAndUnsignedArgumentIndex(final boolean visible) throws IOException {
        final byte[] expected = encodedEntry(0x13, new byte[0], TYPE_PATH);
        final TypeAnnotationEntry entry = read(expected, visible);
        assertEquals(4, entry.getTypePathLength());
        assertEquals(0, entry.getTypePathKind(0));
        assertEquals(1, entry.getTypePathKind(1));
        assertEquals(3, entry.getTypePathKind(2));
        assertEquals(2, entry.getTypePathKind(3));
        assertEquals(0, entry.getTypePathArgumentIndex(0));
        assertEquals(0, entry.getTypePathArgumentIndex(1));
        assertEquals(255, entry.getTypePathArgumentIndex(2));
        assertEquals(0, entry.getTypePathArgumentIndex(3));
        assertEquals(visible, entry.getAnnotationEntry().isRuntimeVisible());
        assertArrayEquals(expected, dump(entry));
    }

    @ParameterizedTest
    @MethodSource("targetLayouts")
    void rejectsTruncatedEntries(final int targetConstant, final int tag, final byte[] info) throws IOException {
        final byte[] bytes = encodedEntry(tag, info, TYPE_PATH);
        for (int length = 0; length < bytes.length; length++) {
            final byte[] truncated = Arrays.copyOf(bytes, length);
            assertThrows(EOFException.class, () -> read(truncated, true), "target " + targetConstant + ", length " + length);
        }
    }

    @ParameterizedTest
    @MethodSource("targetLayouts")
    void rejectsIncorrectTargetInfoLengths(final int targetConstant, final int tag, final byte[] info) {
        final AnnotationEntry annotation = new AnnotationEntry(1, constantPool(), true);
        assertEquals(tag, targetConstant);
        assertThrows(IllegalArgumentException.class,
                () -> new TypeAnnotationEntry(targetConstant, Arrays.copyOf(info, info.length + 1), new byte[0], annotation));
        if (info.length > 0) {
            assertThrows(IllegalArgumentException.class,
                    () -> new TypeAnnotationEntry(targetConstant, Arrays.copyOf(info, info.length - 1), new byte[0], annotation));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 255, 256, 65535})
    void readsUnsignedLocalVariableTableLengths(final int ranges) throws IOException {
        final byte[] info = new byte[2 + ranges * 6];
        info[0] = (byte) (ranges >>> 8);
        info[1] = (byte) ranges;
        if (ranges > 0) {
            info[info.length - 1] = (byte) 255;
        }
        for (final int target : new int[] {0x40, 0x41}) {
            final byte[] bytes = encodedEntry(target, info, new byte[0]);
            final TypeAnnotationEntry entry = read(bytes, true);
            assertArrayEquals(info, entry.getTargetInfo());
            assertArrayEquals(bytes, dump(entry));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 127, 128, 255})
    void readsUnsignedPathLengths(final int steps) throws IOException {
        final byte[] bytes = encodedEntry(0x13, new byte[0], new byte[steps * 2]);
        final TypeAnnotationEntry entry = read(bytes, true);
        assertEquals(steps, entry.getTypePathLength());
        assertArrayEquals(bytes, dump(entry));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 2, 15, 24, 63, 76, 127, 255, 256})
    void rejectsUndefinedConstructorTargets(final int target) {
        assertThrows(ClassFormatException.class, () -> new TypeAnnotationEntry(target, new byte[0], new byte[0], null));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 15, 24, 63, 76, 127, 255})
    void rejectsUndefinedEncodedTargets(final int target) throws IOException {
        final byte[] bytes = encodedEntry(target, new byte[0], new byte[0]);
        assertThrows(ClassFormatException.class, () -> read(bytes, true));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void rejectsLocalVariableTablesWithoutACompleteCount(final int length) {
        for (final int target : new int[] {0x40, 0x41}) {
            assertThrows(IllegalArgumentException.class,
                    () -> new TypeAnnotationEntry(target, new byte[length], new byte[0], new AnnotationEntry(1, constantPool(), true)));
        }
    }

    @Test
    void protectsTargetInfoAndPathFromCallerMutation() throws IOException {
        final byte[] info = {0, 2};
        final byte[] path = {3, 1};
        final TypeAnnotationEntry entry = new TypeAnnotationEntry(0x10, info, path, new AnnotationEntry(1, constantPool(), true));
        info[0] = 99;
        path[0] = 99;
        path[1] = 99;
        final byte[] returnedInfo = entry.getTargetInfo();
        returnedInfo[0] = 88;
        assertArrayEquals(new byte[] {0, 2}, entry.getTargetInfo());
        assertEquals(3, entry.getTypePathKind(0));
        assertEquals(1, entry.getTypePathArgumentIndex(0));
        assertArrayEquals(encodedEntry(0x10, new byte[] {0, 2}, new byte[] {3, 1}), dump(entry));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void deeplyCopiesNestedAnnotationValues(final boolean visible) throws IOException {
        final ConstantPoolGen gen = new ConstantPoolGen();
        final int type = gen.addUtf8("Lexample/Annotation;");
        final int nestedType = gen.addUtf8("Lexample/Nested;");
        final int arrayName = gen.addUtf8("items");
        final int nestedName = gen.addUtf8("nested");
        final int valueName = gen.addUtf8("value");
        final int first = gen.addUtf8("first");
        final int second = gen.addUtf8("second");
        final ConstantPool pool = gen.getFinalConstantPool();
        final AnnotationEntry nested = new AnnotationEntry(nestedType, pool, visible);
        nested.addElementNameValuePair(new ElementValuePair(valueName, new SimpleElementValue(ElementValue.STRING, first, pool), pool));
        final AnnotationEntry annotation = new AnnotationEntry(type, pool, visible);
        annotation.addElementNameValuePair(new ElementValuePair(arrayName, new ArrayElementValue(ElementValue.ARRAY,
                new ElementValue[] {new SimpleElementValue(ElementValue.STRING, first, pool)}, pool), pool));
        annotation.addElementNameValuePair(new ElementValuePair(nestedName, new AnnotationElementValue(ElementValue.ANNOTATION, nested, pool), pool));
        final TypeAnnotationEntry original = new TypeAnnotationEntry(0x13, new byte[0], TYPE_PATH, annotation);
        final byte[] expected = dump(original);
        final ConstantPool otherPool = pool.copy();
        final TypeAnnotationEntry copy = original.copy(otherPool);
        assertArrayEquals(expected, dump(copy));
        assertNotSame(original, copy);
        assertNotSame(annotation, copy.getAnnotationEntry());
        assertSame(otherPool, copy.getAnnotationEntry().getConstantPool());
        assertEquals(visible, copy.getAnnotationEntry().isRuntimeVisible());
        final ElementValuePair[] copiedPairs = copy.getAnnotationEntry().getElementValuePairs();
        final ArrayElementValue array = (ArrayElementValue) copiedPairs[0].getValue();
        final AnnotationEntry copiedNested = ((AnnotationElementValue) copiedPairs[1].getValue()).getAnnotationEntry();
        assertNotSame(annotation.getElementValuePairs()[0], copiedPairs[0]);
        assertNotSame(((ArrayElementValue) annotation.getElementValuePairs()[0].getValue()).getElementValuesArray(), array.getElementValuesArray());
        assertNotSame(nested, copiedNested);
        assertSame(otherPool, array.getConstantPool());
        assertSame(otherPool, array.getElementValuesArray()[0].getConstantPool());
        assertSame(otherPool, copiedNested.getConstantPool());
        assertSame(otherPool, copiedNested.getElementValuePairs()[0].getValue().getConstantPool());
        assertEquals(visible, copiedNested.isRuntimeVisible());
        ((SimpleElementValue) array.getElementValuesArray()[0]).setIndex(second);
        ((SimpleElementValue) copiedNested.getElementValuePairs()[0].getValue()).setIndex(second);
        assertEquals("second", array.getElementValuesArray()[0].stringifyValue());
        assertEquals("second", copiedNested.getElementValuePairs()[0].getValue().stringifyValue());
        assertArrayEquals(expected, dump(original), "Mutating nested copied values must not change the original");
    }

    @Test
    void wrapsCopyIoFailures() {
        final IOException failure = new IOException("Cannot encode the entry");
        final TypeAnnotationEntry entry = new TypeAnnotationEntry(0x13, new byte[0], new byte[0], null) {
            /**
             * Always throws the configured {@link IOException} to simulate an encoding failure.
             *
             * @param out The unused output stream.
             * @throws IOException Thrown unconditionally to simulate an encoding failure.
             */
            @Override
            public void dump(final DataOutputStream out) throws IOException {
                throw failure;
            }
        };
        assertSame(failure, assertThrows(UncheckedIOException.class, () -> entry.copy(constantPool())).getCause());
    }
}
