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
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

/**
 * Emits a class with a static method {@code test} that applies {@code getfield} or {@code putfield} to an instance field named {@code x}.
 *
 * <p>
 * The receiver is either the method's single parameter or a null constant. A parameter type that is not assignment compatible with the class named by the field
 * reference must be rejected during structural verification. Subclass receivers and the null type are permitted.
 * </p>
 */
public class TestFieldAccess01Creator extends TestCreator {
    private final InstructionFactory factory;
    private final ConstantPoolGen cp;
    private final ClassGen cg;
    private final short opcode;
    private final Type objectrefType;
    private final Type fieldType;
    private final String fieldClassName;
    private final String className;
    private final String simpleClassName;

    /**
     * Constructs a field access fixture.
     *
     * @param opcode {@link Const#GETFIELD} or {@link Const#PUTFIELD}.
     * @param fieldType the type of field {@code x}.
     * @param fieldClassName the class named by the field reference, which may inherit {@code x}.
     * @param objectrefType the receiver parameter type, or {@link Type#NULL} to emit a null constant without a parameter.
     */
    public TestFieldAccess01Creator(final short opcode, final Type fieldType, final String fieldClassName, final Type objectrefType) {
        this.opcode = opcode;
        this.fieldType = fieldType;
        this.fieldClassName = fieldClassName;
        this.objectrefType = objectrefType;
        simpleClassName = "TestFieldAccess01" + (opcode == Const.GETFIELD ? "Get" : "Put") + fieldType.getSignature();
        className = TEST_PACKAGE + "." + simpleClassName;
        cg = new ClassGen(className, "java.lang.Object", simpleClassName + ".java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
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
        final boolean nullReceiver = objectrefType == Type.NULL;
        final Type[] parameterTypes = nullReceiver ? Type.NO_ARGS : new Type[] { objectrefType };
        final String[] parameterNames = nullReceiver ? new String[] {} : new String[] { "o" };
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, parameterTypes, parameterNames, "test", className, il, cp);
        il.append(nullReceiver ? InstructionConst.ACONST_NULL : InstructionFactory.createLoad(objectrefType, 0));
        if (opcode == Const.GETFIELD) {
            il.append(factory.createFieldAccess(fieldClassName, "x", fieldType, Const.GETFIELD));
            il.append(InstructionFactory.createPop(fieldType.getSize()));
        } else {
            il.append(InstructionFactory.createNull(fieldType));
            il.append(factory.createFieldAccess(fieldClassName, "x", fieldType, Const.PUTFIELD));
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

    /**
     * Gets the simple name of the generated class.
     *
     * @return the simple class name.
     */
    public String getSimpleClassName() {
        return simpleClassName;
    }
}
