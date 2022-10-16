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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.ClassPathRepository;
import org.junit.jupiter.api.Test;

public class ConstantPoolTestCase extends AbstractTestCase {

    private InstructionHandle[] getInstructionHandles(final JavaClass clazz, final ConstantPoolGen cp, final Method method) {
        final MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);
        final InstructionList instructionList = methodGen.getInstructionList();
        return instructionList.getInstructionHandles();
    }

    @Test
    public void testConstantToString() throws ClassNotFoundException {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            if (method.getName().equals("<init>")) {
                for (final InstructionHandle instructionHandle : getInstructionHandles(clazz, cp, method)) {
                    final String string = instructionHandle.getInstruction().toString(cp.getConstantPool());
                    assertNotNull(string);
                    // TODO Need real assertions.
                    // System.out.println(string);
                }
            }
        }
    }

    @Test
    public void testTooManyConstants() throws ClassNotFoundException {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        int i = cp.getSize();
        while (i < Const.MAX_CP_ENTRIES - 1) {
            cp.addLong(i);
            i = cp.getSize(); // i += 2
        }
        assertThrows(IllegalStateException.class, () -> cp.addLong(0));
    }

    @Test
    public void doubleConstantWontThrowClassFormatException() throws ClassNotFoundException {
        ClassPath cp = new ClassPath("target/test-classes/org/apache/bcel/classfile");
        JavaClass c = new ClassPathRepository(cp).loadClass("ClassWithDoubleConstantPoolItem");

        ConstantPool pool = c.getConstantPool();
        for (int i = 1; i < pool.getLength(); ++i) {
            pool.getConstant(i);
        }
    }

    @Test
    public void longConstantWontThrowClassFormatException() throws ClassNotFoundException {
        ClassPath cp = new ClassPath("target/test-classes/org/apache/bcel/classfile");
        JavaClass c = new ClassPathRepository(cp).loadClass("ClassWithLongConstantPoolItem");

        ConstantPool pool = c.getConstantPool();
        for (int i = 1; i < pool.getLength(); ++i) {
            pool.getConstant(i);
        }
    }
}

class ClassWithDoubleConstantPoolItem {
    double d = 42; // here is the key; we need a double constant value
}

class ClassWithLongConstantPoolItem {
    long l = 42; // here is the key; we need a double constant value
}
