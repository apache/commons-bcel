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

package org.apache.bcel.verifier.tests;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * Emits a method that applies the given array load or store instruction to an array whose component type does not
 * match the instruction, for example {@code castore} on an {@code int[]} or {@code laload} on an {@code int[]}.
 * Structural verification must reject every such access.
 */
public class TestArrayAccess07Creator extends TestCreator {
    private final InstructionFactory factory;
    private final ConstantPoolGen cp;
    private final ClassGen cg;
    private final ArrayInstruction arrayInstruction;
    private final Type arrayElementType;
    private final String simpleClassName;

    public TestArrayAccess07Creator(final ArrayInstruction arrayInstruction, final Type arrayElementType) {
        this.arrayInstruction = arrayInstruction;
        this.arrayElementType = arrayElementType;
        final String name = arrayInstruction.getName();
        simpleClassName = "TestArrayAccess07" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        cg = new ClassGen(TEST_PACKAGE + "." + simpleClassName, "java.lang.Object", simpleClassName + ".java", Const.ACC_PUBLIC | Const.ACC_SUPER,
            new String[] {});
        cp = cg.getConstantPool();
        factory = new InstructionFactory(cg, cp);
    }

    @Override
    public void create(final OutputStream out) throws IOException {
        createMethod_0();
        createMethod_1();
        cg.getJavaClass().dump(out);
    }

    private void createMethod_0() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", TEST_PACKAGE + "." + simpleClassName, il,
            cp);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethod_1() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[] {}, "test",
            TEST_PACKAGE + "." + simpleClassName, il, cp);

        il.append(new PUSH(cp, 1));
        il.append(factory.createNewArray(arrayElementType, (short) 1));
        il.append(new PUSH(cp, 0));
        if (arrayInstruction.produceStack(cp) == 0) {
            // store: push a value of the type the instruction stores, then access the mismatched array
            il.append(storedValue());
            il.append(arrayInstruction);
        } else {
            // load: access the mismatched array, then pop the loaded value
            il.append(arrayInstruction);
            il.append(arrayInstruction.produceStack(cp) == 2 ? InstructionConst.POP2 : InstructionConst.POP);
        }
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    @Override
    protected String getClassName() {
        return simpleClassName + JavaClass.EXTENSION;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    private Instruction storedValue() {
        switch (arrayInstruction.getOpcode()) {
        case Const.AASTORE:
            return InstructionConst.ACONST_NULL;
        case Const.DASTORE:
            return InstructionConst.DCONST_1;
        case Const.FASTORE:
            return InstructionConst.FCONST_1;
        case Const.LASTORE:
            return InstructionConst.LCONST_1;
        default:
            return InstructionConst.ICONST_1;
        }
    }
}
