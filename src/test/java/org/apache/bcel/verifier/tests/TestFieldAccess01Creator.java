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
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 * Emits a class with an instance field {@code int x} and a static method {@code test} that applies {@code getfield} or
 * {@code putfield} for that field to its single parameter. When the parameter type is not the class declaring the field,
 * for example {@code java.lang.String}, the access is illegal and structural verification must reject it.
 */
public class TestFieldAccess01Creator extends TestCreator {
    private final InstructionFactory factory;
    private final ConstantPoolGen cp;
    private final ClassGen cg;
    private final short opcode;
    private final ObjectType objectrefType;
    private final String className;
    private final String simpleClassName;

    public TestFieldAccess01Creator(final short opcode, final boolean validObjectref) {
        this.opcode = opcode;
        simpleClassName = "TestFieldAccess01" + (opcode == Const.GETFIELD ? "Get" : "Put") + (validObjectref ? "Valid" : "Invalid");
        className = TEST_PACKAGE + "." + simpleClassName;
        objectrefType = validObjectref ? ObjectType.getInstance(className) : Type.STRING;
        cg = new ClassGen(className, "java.lang.Object", simpleClassName + ".java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        cp = cg.getConstantPool();
        factory = new InstructionFactory(cg, cp);
    }

    @Override
    public void create(final OutputStream out) throws IOException {
        cg.addField(new FieldGen(Const.ACC_PUBLIC, Type.INT, "x", cp).getField());
        createMethod_0();
        createMethod_1();
        cg.getJavaClass().dump(out);
    }

    private void createMethod_0() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", className, il, cp);
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
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, new Type[] { objectrefType }, new String[] { "o" }, "test",
            className, il, cp);
        il.append(InstructionFactory.createLoad(objectrefType, 0));
        if (opcode == Const.GETFIELD) {
            il.append(factory.createFieldAccess(className, "x", Type.INT, Const.GETFIELD));
            il.append(InstructionConst.POP);
        } else {
            il.append(InstructionConst.ICONST_1);
            il.append(factory.createFieldAccess(className, "x", Type.INT, Const.PUTFIELD));
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
}
