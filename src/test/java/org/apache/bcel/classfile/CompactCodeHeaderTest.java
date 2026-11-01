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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.bcel.Const;
import org.apache.bcel.Java1OakExampleCreator;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests compact four-byte and standard eight-byte Code headers and their preservation during bytecode manipulation.
 * The sizes cover max_stack, max_locals, and code_length only; the rest of the Code attribute layout is unchanged.
 */
class CompactCodeHeaderTest {

    private static final class TestLoader extends ClassLoader {
        Class<?> define(final byte[] bytes) {
            return defineClass(null, bytes, 0, bytes.length);
        }
    }

    private static JavaClass compactHeaderClass() throws IOException {
        return parse(fixture(), true);
    }

    private static byte[] dump(final Code code) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bytes)) {
            code.dump(out);
        }
        assertEquals(code.getLength() + 6, bytes.size());
        return bytes.toByteArray();
    }

    private static byte[] fixture() throws IOException {
        return Files.readAllBytes(Paths.get("src/test/resources/Java1OakExample.class"));
    }

    private static Code mainCode(final JavaClass clazz) {
        return Arrays.stream(clazz.getMethods()).filter(method -> "main".equals(method.getName())).findFirst().get().getCode();
    }

    private static JavaClass parse(final byte[] bytes, final boolean compactCodeHeader) throws IOException {
        final ClassParser parser = new ClassParser(new ByteArrayInputStream(bytes), "Java1OakExample.class");
        assertFalse(parser.isCompactCodeHeaderEnabled());
        parser.setCompactCodeHeaderEnabled(compactCodeHeader);
        assertEquals(compactCodeHeader, parser.isCompactCodeHeaderEnabled());
        return parser.parse();
    }

    private static Code readCode(final byte[] bytes, final ConstantPool cp, final boolean compactCodeHeader) throws IOException {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final Code code = (Code) Attribute.readAttribute(in, cp, compactCodeHeader);
            assertEquals(0, in.available());
            return code;
        }
    }

    private static JavaClass rebuild(final JavaClass clazz) {
        final ClassGen cg = new ClassGen(clazz);
        for (final Method method : cg.getMethods()) {
            final MethodGen mg = new MethodGen(method, cg.getClassName(), cg.getConstantPool());
            assertTrue(mg.isCompactCodeHeader());
            cg.replaceMethod(method, mg.getMethod());
        }
        return cg.getJavaClass();
    }

    @Test
    void testAddedMethodUsesEnclosingCompactCodeHeader() throws IOException {
        final ClassGen cg = new ClassGen(compactHeaderClass());
        final Method main = Arrays.stream(cg.getMethods()).filter(method -> "main".equals(method.getName())).findFirst().get();
        final MethodGen mg = new MethodGen(main, cg.getClassName(), cg.getConstantPool());
        mg.setName("anotherMethod");
        mg.setCompactCodeHeader(false);
        final Method added = mg.getMethod();
        cg.addMethod(added);
        final JavaClass parsed = parse(cg.getJavaClass().getBytes(), true);
        assertEquals(3, parsed.getMethods().length);
        for (final Method method : parsed.getMethods()) {
            assertTrue(method.getCode().isCompactCodeHeader());
        }
        assertFalse(added.getCode().isCompactCodeHeader(), "Dump must not modify shared method attributes");
        added.getCode().setMaxLocals(256);
        assertThrows(ClassFormatException.class, () -> cg.getJavaClass().getBytes());
    }

    @Test
    void testAttributeLayoutSelection() throws IOException {
        final Code code = mainCode(compactHeaderClass());
        assertArrayEquals(dump(code), dump(readCode(dump(code), code.getConstantPool(), true)));
        code.setCompactCodeHeader(false);
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(dump(code)))) {
            final Code parsed = (Code) Attribute.readAttribute(in, code.getConstantPool());
            assertFalse(parsed.isCompactCodeHeader());
            assertEquals(0, in.available());
            assertArrayEquals(code.getCode(), parsed.getCode());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 65535})
    void testCodeLengthLimits(final int length) throws IOException {
        final Code code = mainCode(compactHeaderClass());
        code.setCode(new byte[length]);
        assertEquals(length, readCode(dump(code), code.getConstantPool(), true).getCode().length);
        code.setCode(new byte[65536]);
        assertThrows(ClassFormatException.class, () -> dump(code));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 256})
    void testCompactCodeHeaderBounds(final int invalid) throws IOException {
        final Code code = mainCode(compactHeaderClass());
        code.setMaxStack(255);
        code.setMaxLocals(255);
        final Code parsed = readCode(dump(code), code.getConstantPool(), true);
        assertEquals(255, parsed.getMaxStack());
        assertEquals(255, parsed.getMaxLocals());
        code.setMaxStack(invalid);
        assertThrows(ClassFormatException.class, () -> dump(code));
        code.setMaxStack(255);
        code.setMaxLocals(invalid);
        assertThrows(ClassFormatException.class, () -> dump(code));
    }

    @Test
    void testCompactCodeHeaderValidatesBeforeChangingMode() throws IOException {
        final Code code = mainCode(compactHeaderClass());
        code.setCompactCodeHeader(false);
        code.setMaxStack(256);
        code.setMaxLocals(256);
        assertEquals(256, readCode(dump(code), code.getConstantPool(), false).getMaxStack());
        final int length = code.getLength();
        assertThrows(ClassFormatException.class, () -> code.setCompactCodeHeader(true));
        assertFalse(code.isCompactCodeHeader());
        assertEquals(length, code.getLength());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testCompactCodeHeaderVersionsRoundTrip(final int minor) throws IOException {
        final byte[] bytes = fixture();
        bytes[5] = (byte) minor;
        final JavaClass clazz = parse(bytes, true);
        assertEquals(minor, clazz.getMinor());
        assertArrayEquals(bytes, clazz.getBytes());
        assertTrue(mainCode(clazz).isCompactCodeHeader());
    }

    @Test
    void testCopiesPreserveLayout() throws IOException {
        final JavaClass original = compactHeaderClass();
        assertArrayEquals(fixture(), original.copy().getBytes());
        final Code code = mainCode(original);
        final Code shallow = new Code(code);
        final Code deep = (Code) code.copy(code.getConstantPool());
        assertTrue(shallow.isCompactCodeHeader());
        assertTrue(deep.isCompactCodeHeader());
        assertArrayEquals(dump(code), dump(shallow));
        assertArrayEquals(dump(code), dump(deep));
        shallow.setCompactCodeHeader(false);
        assertTrue(code.isCompactCodeHeader());
    }

    @Test
    void testFixtureValuesAndReproducibility() throws IOException {
        assertArrayEquals(Java1OakExampleCreator.create().getBytes(), fixture());
        final JavaClass clazz = compactHeaderClass();
        assertEquals("Java1OakExample", clazz.getClassName());
        assertEquals(45, clazz.getMajor());
        assertEquals(2, clazz.getMinor());
        assertEquals(2, clazz.getMethods().length);
        final Code main = mainCode(clazz);
        assertEquals(2, main.getMaxStack());
        assertEquals(1, main.getMaxLocals());
        assertEquals(9, main.getCode().length);
        assertNotNull(main.getLocalVariableTable());
        assertEquals(0, main.getExceptionTable().length);
    }

    @Test
    void testLengthRecalculation() throws IOException {
        final Code code = mainCode(compactHeaderClass());
        code.setAttributes(Attribute.EMPTY_ARRAY);
        code.setExceptionTable(new CodeException[] {new CodeException(0, 1, 0, 0)});
        code.setCode(new byte[] {(byte) Const.RETURN});
        assertEquals(17, code.getLength());
        final Code parsed = readCode(dump(code), code.getConstantPool(), true);
        assertEquals(1, parsed.getExceptionTable().length);
        assertEquals(1, parsed.getCode().length);
        code.setCompactCodeHeader(false);
        assertEquals(21, code.getLength());
        assertEquals(1, readCode(dump(code), code.getConstantPool(), false).getExceptionTable().length);
        code.setCompactCodeHeader(true);
        assertEquals(17, code.getLength());
    }

    @Test
    void testLocalVariableGrowthFailureDoesNotLeaveAttributes() throws IOException {
        final JavaClass clazz = compactHeaderClass();
        final Method main = Arrays.stream(clazz.getMethods()).filter(method -> "main".equals(method.getName())).findFirst().get();
        final MethodGen mg = new MethodGen(main, clazz.getClassName(), new ConstantPoolGen(clazz.getConstantPool()));
        mg.addLocalVariable("extra", Type.INT, 255, mg.getInstructionList().getStart(), mg.getInstructionList().getEnd());
        // Rebuilding the local variable table restores the required count of 256 during getMethod().
        mg.setMaxLocals(1);
        assertThrows(ClassFormatException.class, mg::getMethod);
        assertEquals(0, mg.getCodeAttributes().length, "Failed generation must not leave temporary attributes");
        mg.setCompactCodeHeader(false);
        final Code code = mg.getMethod().getCode();
        assertEquals(256, code.getMaxLocals());
        assertEquals(1, Arrays.stream(code.getAttributes()).filter(LocalVariableTable.class::isInstance).count());
    }

    @Test
    void testMethodGenPreservesLayout() throws IOException {
        final JavaClass clazz = compactHeaderClass();
        final JavaClass rebuilt = parse(rebuild(clazz).getBytes(), true);
        assertTrue(mainCode(rebuilt).isCompactCodeHeader());
        assertArrayEquals(mainCode(clazz).getCode(), mainCode(rebuilt).getCode());
        final MethodGen mg = new MethodGen(clazz.getMethods()[0], clazz.getClassName(), new ConstantPoolGen(clazz.getConstantPool()));
        assertTrue(mg.copy(clazz.getClassName(), new ConstantPoolGen(clazz.getConstantPool())).getMethod().getCode().isCompactCodeHeader());
        mg.setMaxStack(256);
        assertThrows(ClassFormatException.class, mg::getMethod);
        mg.setCompactCodeHeader(false);
        assertFalse(mg.getMethod().getCode().isCompactCodeHeader());
    }

    @Test
    void testNonCodeAttributesWithCompactCodeHeaders() throws IOException {
        final ClassGen cg = new ClassGen(compactHeaderClass());
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.INT, "VALUE", cg.getConstantPool());
        fg.setInitValue(42);
        cg.addField(fg.getField());
        final JavaClass parsed = parse(cg.getJavaClass().getBytes(), true);
        assertEquals("42", parsed.getFields()[0].getConstantValue().toString());
        assertEquals("Java1OakExample.java", parsed.getSourceFileName());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testStandardCodeHeaderRemainsDefault(final int minor) throws IOException {
        final JavaClass clazz = compactHeaderClass();
        for (final Method method : clazz.getMethods()) {
            method.getCode().setCompactCodeHeader(false);
        }
        clazz.setMinor(minor);
        final byte[] bytes = clazz.getBytes();
        final JavaClass parsed = parse(bytes, false);
        assertFalse(mainCode(parsed).isCompactCodeHeader());
        assertEquals(1, mainCode(parsed).getMaxLocals());
        assertArrayEquals(bytes, parsed.getBytes());
    }

    @Test
    void testTruncatedCompactCodeAttribute() throws IOException {
        final Code code = mainCode(compactHeaderClass());
        final byte[] bytes = dump(code);
        assertThrows(EOFException.class, () -> readCode(Arrays.copyOf(bytes, bytes.length - 1), code.getConstantPool(), true));
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testValidCompactCodeHeaderFixtureAndRegeneratedMethodsLoadInJava8() throws Exception {
        assertNotNull(new TestLoader().define(fixture()).getDeclaredMethod("main", String[].class));
        assertNotNull(new TestLoader().define(rebuild(compactHeaderClass()).getBytes()).getDeclaredMethod("main", String[].class));
    }

    @ParameterizedTest
    @CsvSource({"45, 3", "46, 0"})
    void testVersionChangesSelectStandardCodeHeaders(final int major, final int minor) throws Exception {
        final JavaClass clazz = compactHeaderClass();
        clazz.setMajor(major);
        clazz.setMinor(minor);
        final byte[] bytes = clazz.getBytes();
        assertFalse(mainCode(parse(bytes, false)).isCompactCodeHeader());
        assertFalse(mainCode(parse(bytes, true)).isCompactCodeHeader(), "Compact Code header parsing must only apply to versions 45.0 through 45.2");
        assertTrue(mainCode(clazz).isCompactCodeHeader(), "Dump must not mutate the original attributes");
        assertNotNull(new TestLoader().define(bytes).getDeclaredMethod("main", String[].class));
        final ClassGen cg = new ClassGen(compactHeaderClass());
        cg.setMajor(major);
        cg.setMinor(minor);
        assertFalse(mainCode(parse(cg.getJavaClass().getBytes(), false)).isCompactCodeHeader());
    }
}
