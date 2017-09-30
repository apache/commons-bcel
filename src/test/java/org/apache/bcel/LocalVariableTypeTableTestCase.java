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
 *
 */
package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class LocalVariableTypeTableTestCase extends AbstractTestCase {
    public class TestClassLoader extends ClassLoader {
        public TestClassLoader(final ClassLoader parent) {
            super(parent);
        }

        public Class<?> findClass(final String name, final byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    @Test
    public void testWithGenericArguement() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        final String targetClass = PACKAGE_BASE_NAME + ".data.SimpleClassHasMethodIncludeGenericArgument";
        final TestClassLoader loader = new TestClassLoader(getClass().getClassLoader());
        final Class cls = loader.findClass(targetClass, getBytesFromClass(targetClass));

        java.lang.reflect.Method method = cls.getDeclaredMethod("a", String.class, List.class);
        method.invoke(null, "a1", new LinkedList<String>());
        method = cls.getDeclaredMethod("b", String.class, List.class);
        method.invoke(null, "b1", new LinkedList<String>());
        method = cls.getDeclaredMethod("c", String.class, String.class);
        method.invoke(null, "c1", "c2");
        method = cls.getDeclaredMethod("d", List.class, String.class);
        method.invoke(null, new LinkedList<String>(), "d2");
    }

    private byte[] getBytesFromClass(final String className) throws ClassNotFoundException, IOException {
        final JavaClass clazz = getTestClass(className);
        final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        final Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if (!method.isNative() && !method.isAbstract()) {
                methods[i] = injection(clazz, method, cp, findFirstStringLocalVariableOffset(method));
            }
        }

        clazz.setConstantPool(cp.getFinalConstantPool());

        return clazz.getBytes();
    }

    public Method injection(final JavaClass clazz, Method method, final ConstantPoolGen cp, final int firstStringOffset) {
        final MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);

        final InstructionList instructionList = methodGen.getInstructionList();
        instructionList.insert(instructionList.getStart(), makeWillBeAddedInstructionList(methodGen, firstStringOffset));

        methodGen.setMaxStack();
        methodGen.setMaxLocals();

        method = methodGen.getMethod();
        instructionList.dispose();

        return method;
    }

    public InstructionList makeWillBeAddedInstructionList(final MethodGen methodGen, final int firstStringOffset) {
        if (firstStringOffset == -1) {
            return new InstructionList();
        }

        final LocalVariableGen localVariableGen = methodGen.getLocalVariables()[firstStringOffset];
        Instruction instruction;

        if (localVariableGen != null) {
            instruction = new ALOAD(localVariableGen.getIndex());
        } else {
            instruction = new ACONST_NULL();
        }

        return createPrintln(methodGen.getConstantPool(), instruction);
    }

    public InstructionList createPrintln(final ConstantPoolGen cp, final Instruction instruction) {
        final InstructionList il = new InstructionList();

        final int out = cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
        final int println = cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
        il.append(new GETSTATIC(out));
        il.append(instruction);
        il.append(new INVOKEVIRTUAL(println));

        return il;
    }

    public int findFirstStringLocalVariableOffset(final Method method) {
        final Type[] argumentTypes = method.getArgumentTypes();
        int offset = -1;

        for (int i = 0, count = argumentTypes.length; i < count; i++) {
            if (Type.STRING.getSignature().equals(argumentTypes[i].getSignature())) {
                if (method.isStatic()) {
                    offset = i;
                } else {
                    offset = i + 1;
                }

                break;
            }
        }

        return offset;
    }
}
