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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class TestReturn01Creator extends TestCreator {
    private final InstructionFactory factory;
    private final ConstantPoolGen cp;
    private final ClassGen cg;

    public TestReturn01Creator() {
        cg = new ClassGen(TEST_PACKAGE + ".TestReturn01", "java.lang.Object", "TestReturn01.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});

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
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", TEST_PACKAGE + ".TestReturn01", il, cp);

        final InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        assertNotNull(ih_0); // TODO why is this not used
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        final InstructionHandle ih_4 = il.append(InstructionFactory.createReturn(Type.VOID));
        assertNotNull(ih_4); // TODO why is this not used
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethod_1() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[] {}, "foo",
            TEST_PACKAGE + ".TestReturn01", il, cp);

        final InstructionHandle ih_0 = il.append(factory.createNew("java.lang.Object"));
        assertNotNull(ih_0); // TODO why is this not used
        il.append(InstructionConst.DUP);
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionConst.NOP);
        final InstructionHandle ih_8 = il.append(InstructionFactory.createReturn(Type.OBJECT));
        assertNotNull(ih_8); // TODO why is this not used
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
}
