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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.ClassPathRepository;
import org.junit.jupiter.api.Test;

final class ClassWithDoubleConstantPoolItem {
    double d = 42; // here is the key; we need a double constant value
}

final class ClassWithLongConstantPoolItem {
    long l = 42; // here is the key; we need a double constant value
}

public class ConstantPoolTestCase extends AbstractTestCase {

    private static InstructionHandle[] getInstructionHandles(final JavaClass clazz, final ConstantPoolGen cp, final Method method) {
        final MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);
        return methodGen.getInstructionList().getInstructionHandles();
    }

    @Test
    public void testClassWithDoubleConstantPoolItem() throws ClassNotFoundException, IOException {
        try (final ClassPath cp = new ClassPath("target/test-classes/org/apache/bcel/classfile")) {
            final ClassWithDoubleConstantPoolItem classWithDoubleConstantPoolItem = new ClassWithDoubleConstantPoolItem();
            final JavaClass c = new ClassPathRepository(cp).loadClass(classWithDoubleConstantPoolItem.getClass());
            final Field[] fields = c.getFields();
            assertNotNull(fields);
            assertEquals(1, fields.length);
            assertEquals(ClassWithDoubleConstantPoolItem.class.getDeclaredFields()[0].getName(), fields[0].getName());
            final ConstantPool pool = c.getConstantPool();
            for (int i = 1; i < pool.getLength(); i++) {
                try {
                    final Constant constant = pool.getConstant(i);
                    if (constant instanceof ConstantDouble) {
                        assertEquals(classWithDoubleConstantPoolItem.d, ((ConstantDouble) constant).getBytes());
                        // Next constant pool entry will be invalid so skip it
                        i++;
                    }
                } catch (final Throwable t) {
                    t.printStackTrace();
                    fail();
                }
            }
        }
    }

    @Test
    public void testClassWithLongConstantPoolItem() throws ClassNotFoundException, IOException {
        try (final ClassPath cp = new ClassPath("target/test-classes/org/apache/bcel/classfile")) {
            final ClassWithLongConstantPoolItem classWithLongConstantPoolItem = new ClassWithLongConstantPoolItem();
            final JavaClass c = new ClassPathRepository(cp).loadClass(classWithLongConstantPoolItem.getClass());
            final Field[] fields = c.getFields();
            assertNotNull(fields);
            assertEquals(1, fields.length);
            assertEquals(ClassWithLongConstantPoolItem.class.getDeclaredFields()[0].getName(), fields[0].getName());
            final ConstantPool pool = c.getConstantPool();
            for (int i = 1; i < pool.getLength(); i++) {
                try {
                    final Constant constant = pool.getConstant(i);
                    if (constant instanceof ConstantLong) {
                        assertEquals(classWithLongConstantPoolItem.l, ((ConstantLong) constant).getBytes());
                        // Next constant pool entry will be invalid so skip it
                        i++;
                    }
                } catch (final Throwable t) {
                    t.printStackTrace();
                    fail();
                }
            }
        }
    }

    @Test
    public void testConstantToString() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());
        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals("<init>")) {
                for (final InstructionHandle instructionHandle : getInstructionHandles(clazz, cp, method)) {
                    final String instruction = instructionHandle.getInstruction().toString(cp.getConstantPool());
                    assertNotNull(instruction);
                    switch (instructionHandle.getPosition()) {
                        case 0:
                            assertEquals("aload_0", instruction);
                            break;
                        case 1:
                            assertEquals("invokespecial java/lang/Object/<init>()V", instruction);
                            break;
                        case 4:
                            assertEquals("return", instruction);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Test
    public void testTooManyConstants() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());
        int i = cp.getSize();
        while (i < Const.MAX_CP_ENTRIES - 1) {
            cp.addLong(i);
            i = cp.getSize(); // i += 2
        }
        assertThrows(IllegalStateException.class, () -> cp.addLong(0));
    }
}
