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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PLSETestCase extends AbstractTestCase {
    /**
     * BCEL-208: A couple of methods in MethodGen.java need to test for an empty instruction list.
     */
    @Test
    public void testBCEL208() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = gen.getMethodAt(1);
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        mg.setInstructionList(null);
        mg.addLocalVariable("local2", Type.INT, null, null);
        // currently, this will cause null pointer exception
        mg.getLocalVariableTable(pool);
    }

    /**
     * BCEL-262:
     */
    @Test
    public void testBCEL262() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestEnum");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        // get the values() method
        final Method m = gen.getMethodAt(0);
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        final InstructionList il = mg.getInstructionList();
        // get the invokevirtual instruction
        final InstructionHandle ih = il.findHandle(3);
        final InvokeInstruction ii = (InvokeInstruction) ih.getInstruction();
        // without fix, the getClassName() will throw:
        // java.lang.IllegalArgumentException: Cannot be used on an array type
        final String cn = ii.getClassName(pool);
        assertEquals("[Lorg.apache.bcel.data.PLSETestEnum;", cn);
    }

    /**
     * BCEL-295:
     */
    @Test
    public void testBCEL295() throws Exception {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass2");
        final ClassGen cg = new ClassGen(clazz);
        final ConstantPoolGen pool = cg.getConstantPool();
        final Method m = cg.getMethodAt(1); // 'main'
        final LocalVariableTable lvt = m.getLocalVariableTable();
        final LocalVariable lv = lvt.getLocalVariable(2, 4); // 'i'
        // System.out.println(lv);
        final MethodGen mg = new MethodGen(m, cg.getClassName(), pool);
        final LocalVariableTable newLvt = mg.getLocalVariableTable(mg.getConstantPool());
        final LocalVariable newLv = newLvt.getLocalVariable(2, 4); // 'i'
        // System.out.println(new_lv);
        assertEquals(lv.getLength(), newLv.getLength(), "live range length");
    }

    /**
     * BCEL-361: LineNumber.toString() treats code offset as signed
     */
    @Test
    public void testBCEL361() throws Exception {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.LargeMethod");
        final Method[] methods = clazz.getMethods();
        final Method m = methods[0];
        // System.out.println(m.getName());
        final Code code = m.getCode();
        final LineNumberTable lnt = code.getLineNumberTable();
        final LineNumber[] lineNumbers = lnt.getLineNumberTable();
        final String data = lineNumbers[lineNumbers.length - 1].toString();
        // System.out.println(data);
        // System.out.println(data.contains("-"));
        assertFalse(data.contains("-"), "code offsets must be positive");
        Stream.of(lineNumbers).forEach(ln -> assertFalse(ln.getLineNumber() < 0));
        Stream.of(lineNumbers).forEach(ln -> assertFalse(ln.getStartPC() < 0));
    }

    /**
     * BCEL-79:
     */
    @Test
    public void testBCEL79() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = gen.getMethodAt(2);
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
    public void testCoverage(final boolean compress) throws ClassNotFoundException, java.io.IOException {
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
                // following statement will throw exeception without classfile/Utility.encode fix
                assertNotNull(Utility.decode(encoded, compress));
                assertNotNull(code.toString());
            }
        }
    }
}
