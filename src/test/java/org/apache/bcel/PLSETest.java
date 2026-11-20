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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PLSETest extends AbstractTest {

    /**
     * Checks BCEL-208 by requesting a local-variable table after removing a method's instruction list.
     * <p>
     * Selects {@code PLSETestClass.meth1(int)} by name, clears its instructions, and adds a local variable with null instruction handles.
     * Building the local-variable table must still succeed. Selecting the method by name preserves this case when compilers order methods differently.
     * </p>
     *
     * @throws ClassNotFoundException if the source fixture cannot be loaded.
     */
    @Test
    void testBCEL208() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = getMethod(clazz, "meth1");
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        mg.setInstructionList(null);
        mg.addLocalVariable("local2", Type.INT, null, null);
        // Before BCEL-208, this threw a NullPointerException.
        mg.getLocalVariableTable(pool);
    }

    /**
     * Checks BCEL-262 by parsing the array-clone invocation in a generated enum {@code values()} method.
     * <p>
     * The removed {@code PLSETestEnum.java} fixture contained:
     * </p>
     * <pre>{@code
     * enum PLSETestEnum {
     *     TEST_VALUE
     * }
     * }</pre>
     * <p>
     * javac implements the implicit {@code values()} method by cloning a backing array, with code equivalent to:
     * </p>
     * <pre>{@code
     * public static PLSETestEnum[] values() {
     *     return $VALUES.clone();
     * }
     * }</pre>
     * <p>
     * This test constructs that backing field and method in an enum class-file fixture, serializes the class, reads it with {@link ClassParser}, and
     * decodes the parsed method with {@link MethodGen}. It finds the invocation by instruction type and checks that its owner is the enum-array
     * descriptor. This preserves the original fixture's constant-pool, instruction-decoding, and array-owner regression coverage without depending
     * on a compiler's enum implementation, method order, or instruction offsets. Before BCEL-262, the array owner caused an IllegalArgumentException.
     * </p>
     *
     * @throws IOException if the generated class cannot be parsed.
     */
    @Test
    void testBCEL262() throws IOException {
        final String className = PACKAGE_BASE_NAME + ".data.PLSETestEnum";
        final ClassGen gen = new ClassGen(className, "java.lang.Enum", "PLSETestEnum.java",
            Const.ACC_PUBLIC | Const.ACC_FINAL | Const.ACC_SUPER | Const.ACC_ENUM, new String[0]);
        gen.setMajor(Const.MAJOR_1_8);
        gen.setMinor(Const.MINOR_1_8);
        final ConstantPoolGen pool = gen.getConstantPool();
        final ArrayType arrayType = new ArrayType(new ObjectType(className), 1);
        gen.addField(new FieldGen(Const.ACC_PRIVATE | Const.ACC_STATIC | Const.ACC_FINAL | Const.ACC_SYNTHETIC, arrayType, "$VALUES", pool).getField());
        final InstructionFactory factory = new InstructionFactory(gen);
        final InstructionList instructions = new InstructionList();
        instructions.append(factory.createGetStatic(className, "$VALUES", arrayType));
        instructions.append(new INVOKEVIRTUAL(pool.addMethodref(arrayType.getSignature(), "clone", "()Ljava/lang/Object;")));
        instructions.append(factory.createCheckCast(arrayType));
        instructions.append(InstructionConst.ARETURN);
        final MethodGen values = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, arrayType, Type.NO_ARGS, new String[0], "values", className,
            instructions, pool);
        values.setMaxStack();
        values.setMaxLocals();
        gen.addMethod(values.getMethod());

        final JavaClass parsed = new ClassParser(new ByteArrayInputStream(gen.getJavaClass().getBytes()), className).parse();
        final ConstantPoolGen parsedPool = new ConstantPoolGen(parsed.getConstantPool());
        final MethodGen parsedValues = new MethodGen(getMethod(parsed, "values"), parsed.getClassName(), parsedPool);
        final InvokeInstruction invocation = Stream.of(parsedValues.getInstructionList().getInstructions()).filter(InvokeInstruction.class::isInstance)
            .map(InvokeInstruction.class::cast).findFirst().orElseThrow(AssertionError::new);
        assertEquals(Const.INVOKEVIRTUAL, invocation.getOpcode());
        assertEquals("clone", invocation.getMethodName(parsedPool));
        assertEquals("()Ljava/lang/Object;", invocation.getSignature(parsedPool));
        assertEquals("[Lorg.apache.bcel.data.PLSETestEnum;", invocation.getClassName(parsedPool));
    }

    /**
     * Checks BCEL-295 by preserving a loop variable's live range through a {@link MethodGen} round trip.
     * <p>
     * {@code PLSETestClass2.main(String[])} contains:
     * </p>
     * <pre>{@code
     * int t = 0;
     * for (int i = 0; i < 100; i++) {
     *     t += i;
     * }
     * }</pre>
     * <p>
     * Finds {@code main} and {@code i} by name, then compares the original local-variable scope length with the reconstructed table. The compiler
     * may omit {@code t} or assign a different slot or bytecode offset to {@code i}; the live range must still be preserved. Before BCEL-295, the
     * reconstructed range could incorrectly include the instruction immediately after the variable's scope.
     * </p>
     *
     * @throws Exception if the source fixture cannot be loaded or converted.
     */
    @Test
    void testBCEL295() throws Exception {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass2");
        final ClassGen cg = new ClassGen(clazz);
        final ConstantPoolGen pool = cg.getConstantPool();
        final Method m = getMethod(clazz, "main");
        final LocalVariableTable lvt = m.getLocalVariableTable();
        final LocalVariable lv = Stream.of(lvt.getLocalVariableTable()).filter(variable -> "i".equals(variable.getName())).findFirst()
            .orElseThrow(AssertionError::new);
        // System.out.println(lv);
        final MethodGen mg = new MethodGen(m, cg.getClassName(), pool);
        final LocalVariableTable newLvt = mg.getLocalVariableTable(mg.getConstantPool());
        final LocalVariable newLv = newLvt.getLocalVariable(lv.getIndex(), lv.getStartPC());
        assertNotNull(newLv, "The loop variable must survive the MethodGen round trip");
        // System.out.println(new_lv);
        assertEquals(lv.getLength(), newLv.getLength(), "live range length");
    }

    /**
     * Checks BCEL-361 at the signed-short boundary and the maximum unsigned-short value.
     * <p>
     * Serializes and parses a {@link LineNumberTable} with explicitly chosen bytecode offsets and source line numbers, checks the unsigned getters,
     * and verifies formatting after using the setters as well. Values 32767, 32768, and 65535 exercise both sides of the sign boundary and the upper
     * limit. The table is tested independently of a method, so the maximum unsigned-short value is included even though it cannot index valid code.
     * </p>
     * <p>
     * The deeply nested try/finally fixture {@code LargeMethod.java} is preserved as a compiled resource and covered by
     * {@link #testBCEL361LargeMethod()}.
     * </p>
     *
     * @param value the bytecode offset and source line number to round-trip.
     * @throws IOException if the line-number attribute cannot be serialized or parsed.
     */
    @ParameterizedTest
    @ValueSource(ints = { 32767, 32768, 65535 })
    void testBCEL361(final int value) throws IOException {
        final ConstantPoolGen pool = new ConstantPoolGen();
        final int nameIndex = pool.addUtf8("LineNumberTable");
        // Explicit values exercise unsigned offsets even when a compiler optimizes away a large source fixture.
        final LineNumberTable table = new LineNumberTable(nameIndex, 6, new LineNumber[] { new LineNumber(value, value) }, pool.getConstantPool());
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (DataOutputStream data = new DataOutputStream(output)) {
            table.dump(data);
        }
        final LineNumberTable parsed;
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(output.toByteArray()))) {
            parsed = (LineNumberTable) Attribute.readAttribute((DataInput) input, pool.getConstantPool());
        }
        final LineNumber line = parsed.getLineNumberTable()[0];
        assertEquals(value, line.getStartPC());
        assertEquals(value, line.getLineNumber());
        line.setStartPC(value);
        line.setLineNumber(value);
        assertEquals("LineNumber(" + value + ", " + value + ")", line.toString());
    }

    /**
     * Checks BCEL-361 using the original twelve-level nested try/finally fixture and its javac-generated bytecode.
     * <p>
     * The unchanged source is retained in {@code src/test/resources/issue361/LargeMethod.java}. It repeatedly nests this pattern in an instance
     * initializer, to a depth of twelve try/finally blocks:
     * </p>
     * <pre>{@code
     * int a;
     * try {
     *     a = 0;
     * } finally {
     *     try {
     *         a = 0;
     *     } finally {
     *         a = 0;
     *     }
     * }
     * }</pre>
     * <p>
     * javac expands the full source into a 64507-byte constructor with 8188 exception handlers. The checked-in {@code LargeMethod.jar} contains
     * that complete class, including its line-number, local-variable, and stack-map tables. Reading this fixed fixture retains the original nesting
     * and compiler-generated control flow even when the test suite is compiled by ECJ. The compiler and reproduction commands are documented in
     * {@code src/test/README.md}.
     * </p>
     * <p>
     * The test parses the entire class and its Code attributes, asserts the constructor size and handler count, decodes its branch instructions,
     * and checks line-number offsets and exception-handler positions beyond 32767. It checks every line-number entry's unsigned getters and text.
     * The fixture is parsed rather than executed, as in the original regression test. The independent boundary cases in {@link #testBCEL361(int)}
     * additionally exercise setters and the maximum unsigned-short value.
     * </p>
     *
     * @throws IOException if the compiled fixture cannot be parsed.
     */
    @Test
    void testBCEL361LargeMethod() throws IOException {
        final JavaClass parsed = new ClassParser("src/test/resources/issue361/LargeMethod.jar", "org/apache/bcel/data/LargeMethod.class").parse();
        final Code code = getMethod(parsed, Const.CONSTRUCTOR_NAME).getCode();
        assertEquals(64507, code.getCode().length, "Preserve the original javac-generated constructor");
        assertEquals(8188, code.getExceptionTable().length, "Preserve the deeply nested finally handlers");
        assertTrue(Stream.of(code.getExceptionTable()).anyMatch(handler -> handler.getHandlerPC() > Short.MAX_VALUE));
        final InstructionList instructions = new InstructionList(code.getCode());
        assertTrue(Stream.of(instructions.getInstructions()).anyMatch(BranchInstruction.class::isInstance));
        final LineNumber[] lines = code.getLineNumberTable().getLineNumberTable();
        assertTrue(Stream.of(lines).anyMatch(line -> line.getStartPC() > Short.MAX_VALUE));
        for (final LineNumber line : lines) {
            assertTrue(line.getStartPC() >= 0 && line.getStartPC() < code.getCode().length, "Line-number offsets must index the code");
            assertTrue(line.getLineNumber() >= 0, "Source line numbers must be unsigned");
            assertEquals("LineNumber(" + line.getStartPC() + ", " + line.getLineNumber() + ")", line.toString());
        }
    }

    /**
     * Checks BCEL-79 by preserving local-variable table entries through a {@link MethodGen} round trip.
     * <p>
     * Selects {@code PLSETestClass.meth2(int, ArrayList, int)} by name and compares the number of local variables before and after reconstruction.
     * The fixture retains its parameters and local variable; the lookup is independent of the compiler's method ordering.
     * </p>
     *
     * @throws ClassNotFoundException if the source fixture cannot be loaded.
     */
    @Test
    void testBCEL79() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = getMethod(clazz, "meth2");
        final LocalVariableTable lvt = m.getLocalVariableTable();
        // System.out.println(lvt);
        // System.out.println(lvt.getTableLength());
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        final LocalVariableTable newLvt = mg.getLocalVariableTable(mg.getConstantPool());
        // System.out.println(new_lvt);
        assertEquals(lvt.getTableLength(), newLvt.getTableLength(), "number of locals");
    }

    /**
     * Test to improve BCEL tests code coverage for classfile/Utility.java.
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testCoverage(final boolean compress) throws ClassNotFoundException, java.io.IOException {
        // load a class with a wide variety of byte codes - including tableswitch and lookupswitch
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.ConstantPoolX");
        for (final Method m : clazz.getMethods()) {
            final String signature = m.getSignature();
            Utility.methodTypeToSignature(Utility.methodSignatureReturnType(signature), Utility.methodSignatureArgumentTypes(signature)); // discard result
            final Code code = m.getCode();
            if (code != null) {
                // TODO: need for real assertions here
                final String encoded = Utility.encode(code.getCode(), compress);
                assertNotNull(encoded);
                // following statement will throw exception without classfile/Utility.encode fix
                assertNotNull(Utility.decode(encoded, compress));
                assertNotNull(code.toString());
            }
        }
    }
}
