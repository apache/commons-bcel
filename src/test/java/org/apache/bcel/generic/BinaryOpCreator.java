/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.bcel.generic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.commons.lang.ArrayUtils;

public class BinaryOpCreator {

    private static final String ORG_APACHE_BCEL_GENERIC_BINARY_OP = "org.apache.bcel.generic.BinaryOp";

    public static void main(final String[] args) throws Exception {
        final BinaryOpCreator creator = new BinaryOpCreator();
        final Path path = Paths.get("target/test-classes/org/apache/bcel/generic/BinaryOp.class");
        Files.deleteIfExists(path);
        try (OutputStream out = Files.newOutputStream(path)) {
            creator.create(out);
        }
    }

    private final InstructionFactory factory;
    private final ConstantPoolGen cp;

    private final ClassGen cg;

    public BinaryOpCreator() {
        cg = new ClassGen(ORG_APACHE_BCEL_GENERIC_BINARY_OP, "java.lang.Object", "BinaryOp.java", Const.ACC_PUBLIC | Const.ACC_SUPER,
                ArrayUtils.EMPTY_STRING_ARRAY);
        cg.setMajor(Const.MAJOR_1_8);
        cg.setMinor(Const.MINOR_1_8);

        cp = cg.getConstantPool();
        factory = new InstructionFactory(cg, cp);
    }

    public void create(final OutputStream out) throws IOException {
        createConstructor();
        createMethodISUB();
        createMethodIADD();
        createMethodIREM();
        createMethodIMUL();
        createMethodIDIV();
        createMethodIAND();
        createMethodIOR();
        createMethodIXOR();
        createMethodISHL();
        createMethodISHR();
        createMethodIUSHR();
        createMethodLSUB();
        createMethodLADD();
        createMethodLREM();
        createMethodLMUL();
        createMethodLDIV();
        createMethodLAND();
        createMethodLOR();
        createMethodLXOR();
        createMethodLSHL();
        createMethodLSHR();
        createMethodLUSHR();
        createMethodFSUB();
        createMethodFADD();
        createMethodFREM();
        createMethodFMUL();
        createMethodFDIV();
        createMethodDSUB();
        createMethodDADD();
        createMethodDREM();
        createMethodDMUL();
        createMethodDDIV();
        createMethodCalculate();
        createMethodMain();
        cg.getJavaClass().dump(out);
    }

    private void createConstructor() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, ArrayUtils.EMPTY_STRING_ARRAY, "<init>",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodCalculate() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.OBJECT, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "args" },
                "calculate", ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);
        method.addException("java.lang.Exception");
        final StackMapType[] typesOfLocals = { new StackMapType((byte) 7, cp.addClass("java.lang.String"), cp.getConstantPool()) };
        final StackMapEntry[] table = { new StackMapEntry(252, 70, typesOfLocals, null, cp.getConstantPool()),
                new StackMapEntry(251, 65, null, null, cp.getConstantPool()),
                new StackMapEntry(251, 65, null, null, cp.getConstantPool()),
                new StackMapEntry(251, 65, null, null, cp.getConstantPool()) };
        final StackMap stackMap = new StackMap(cp.addUtf8("StackMapTable"), 17, table, cp.getConstantPool());
        stackMap.setStackMap(table);
        method.addCodeAttribute(stackMap);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 0));
        il.append(InstructionConst.AALOAD);
        il.append(InstructionFactory.createStore(Type.OBJECT, 2));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, "i"));
        il.append(factory.createInvoke("java.lang.String", "startsWith", Type.BOOLEAN, new Type[] { Type.STRING }, Const.INVOKEVIRTUAL));
        final BranchInstruction ifeq1 = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(ifeq1);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "getClass", new ObjectType("java.lang.Class"), Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(new ObjectType("java.lang.Class"), (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(factory.createFieldAccess("java.lang.Integer", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(factory.createFieldAccess("java.lang.Integer", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.Class", "getMethod", new ObjectType("java.lang.reflect.Method"),
                new Type[] { Type.STRING, new ArrayType(new ObjectType("java.lang.Class"), 1) }, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(Type.OBJECT, (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 1));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Integer", "parseInt", Type.INT, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Integer", "valueOf", new ObjectType("java.lang.Integer"), new Type[] { Type.INT }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 2));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Integer", "parseInt", Type.INT, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Integer", "valueOf", new ObjectType("java.lang.Integer"), new Type[] { Type.INT }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.reflect.Method", "invoke", Type.OBJECT, new Type[] { Type.OBJECT, new ArrayType(Type.OBJECT, 1) },
                Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        final InstructionHandle ih1 = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, "l"));
        il.append(factory.createInvoke("java.lang.String", "startsWith", Type.BOOLEAN, new Type[] { Type.STRING }, Const.INVOKEVIRTUAL));
        final BranchInstruction ifeq2 = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(ifeq2);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "getClass", new ObjectType("java.lang.Class"), Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(new ObjectType("java.lang.Class"), (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(factory.createFieldAccess("java.lang.Long", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(factory.createFieldAccess("java.lang.Long", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.Class", "getMethod", new ObjectType("java.lang.reflect.Method"),
                new Type[] { Type.STRING, new ArrayType(new ObjectType("java.lang.Class"), 1) }, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(Type.OBJECT, (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 1));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Long", "parseLong", Type.LONG, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Long", "valueOf", new ObjectType("java.lang.Long"), new Type[] { Type.LONG }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 2));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Long", "parseLong", Type.LONG, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Long", "valueOf", new ObjectType("java.lang.Long"), new Type[] { Type.LONG }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.reflect.Method", "invoke", Type.OBJECT, new Type[] { Type.OBJECT, new ArrayType(Type.OBJECT, 1) },
                Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        final InstructionHandle ih2 = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, "f"));
        il.append(factory.createInvoke("java.lang.String", "startsWith", Type.BOOLEAN, new Type[] { Type.STRING }, Const.INVOKEVIRTUAL));
        final BranchInstruction ifeq3 = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(ifeq3);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "getClass", new ObjectType("java.lang.Class"), Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(new ObjectType("java.lang.Class"), (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(factory.createFieldAccess("java.lang.Float", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(factory.createFieldAccess("java.lang.Float", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.Class", "getMethod", new ObjectType("java.lang.reflect.Method"),
                new Type[] { Type.STRING, new ArrayType(new ObjectType("java.lang.Class"), 1) }, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(Type.OBJECT, (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 1));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Float", "parseFloat", Type.FLOAT, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Float", "valueOf", new ObjectType("java.lang.Float"), new Type[] { Type.FLOAT }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 2));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Float", "parseFloat", Type.FLOAT, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Float", "valueOf", new ObjectType("java.lang.Float"), new Type[] { Type.FLOAT }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.reflect.Method", "invoke", Type.OBJECT, new Type[] { Type.OBJECT, new ArrayType(Type.OBJECT, 1) },
                Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        final InstructionHandle ih3 = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, "d"));
        il.append(factory.createInvoke("java.lang.String", "startsWith", Type.BOOLEAN, new Type[] { Type.STRING }, Const.INVOKEVIRTUAL));
        final BranchInstruction ifeq4 = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(ifeq4);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "getClass", new ObjectType("java.lang.Class"), Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(new ObjectType("java.lang.Class"), (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(factory.createFieldAccess("java.lang.Double", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(factory.createFieldAccess("java.lang.Double", "TYPE", new ObjectType("java.lang.Class"), Const.GETSTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.Class", "getMethod", new ObjectType("java.lang.reflect.Method"),
                new Type[] { Type.STRING, new ArrayType(new ObjectType("java.lang.Class"), 1) }, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(new PUSH(cp, 2));
        il.append(factory.createNewArray(Type.OBJECT, (short) 1));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 1));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Double", "parseDouble", Type.DOUBLE, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Double", "valueOf", new ObjectType("java.lang.Double"), new Type[] { Type.DOUBLE }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(new PUSH(cp, 2));
        il.append(InstructionConst.AALOAD);
        il.append(factory.createInvoke("java.lang.Double", "parseDouble", Type.DOUBLE, new Type[] { Type.STRING }, Const.INVOKESTATIC));
        il.append(factory.createInvoke("java.lang.Double", "valueOf", new ObjectType("java.lang.Double"), new Type[] { Type.DOUBLE }, Const.INVOKESTATIC));
        il.append(InstructionConst.AASTORE);
        il.append(factory.createInvoke("java.lang.reflect.Method", "invoke", Type.OBJECT, new Type[] { Type.OBJECT, new ArrayType(Type.OBJECT, 1) },
                Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        final InstructionHandle ih4 = il.append(InstructionConst.ACONST_NULL);
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        ifeq1.setTarget(ih1);
        ifeq2.setTarget(ih2);
        ifeq3.setTarget(ih3);
        ifeq4.setTarget(ih4);
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodDADD() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.DOUBLE, new Type[] { Type.DOUBLE, Type.DOUBLE }, new String[] { "a", "b" }, "dadd",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.DOUBLE, 1));
        il.append(InstructionFactory.createLoad(Type.DOUBLE, 3));
        il.append(InstructionFactory.createBinaryOperation("+", Type.DOUBLE));
        il.append(InstructionFactory.createReturn(Type.DOUBLE));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodDDIV() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.DOUBLE, new Type[] { Type.DOUBLE, Type.DOUBLE }, new String[] { "a", "b" }, "ddiv",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.DOUBLE, 1));
        il.append(InstructionFactory.createLoad(Type.DOUBLE, 3));
        il.append(InstructionFactory.createBinaryOperation("/", Type.DOUBLE));
        il.append(InstructionFactory.createReturn(Type.DOUBLE));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodDMUL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.DOUBLE, new Type[] { Type.DOUBLE, Type.DOUBLE }, new String[] { "a", "b" }, "dmul",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.DOUBLE, 1));
        il.append(InstructionFactory.createLoad(Type.DOUBLE, 3));
        il.append(InstructionFactory.createBinaryOperation("*", Type.DOUBLE));
        il.append(InstructionFactory.createReturn(Type.DOUBLE));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodDREM() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.DOUBLE, new Type[] { Type.DOUBLE, Type.DOUBLE }, new String[] { "a", "b" }, "drem",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.DOUBLE, 1));
        il.append(InstructionFactory.createLoad(Type.DOUBLE, 3));
        il.append(InstructionFactory.createBinaryOperation("%", Type.DOUBLE));
        il.append(InstructionFactory.createReturn(Type.DOUBLE));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodDSUB() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.DOUBLE, new Type[] { Type.DOUBLE, Type.DOUBLE }, new String[] { "a", "b" }, "dsub",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.DOUBLE, 1));
        il.append(InstructionFactory.createLoad(Type.DOUBLE, 3));
        il.append(InstructionFactory.createBinaryOperation("-", Type.DOUBLE));
        il.append(InstructionFactory.createReturn(Type.DOUBLE));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodFADD() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.FLOAT, new Type[] { Type.FLOAT, Type.FLOAT }, new String[] { "a", "b" }, "fadd",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.FLOAT, 1));
        il.append(InstructionFactory.createLoad(Type.FLOAT, 2));
        il.append(InstructionFactory.createBinaryOperation("+", Type.FLOAT));
        il.append(InstructionFactory.createReturn(Type.FLOAT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodFDIV() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.FLOAT, new Type[] { Type.FLOAT, Type.FLOAT }, new String[] { "a", "b" }, "fdiv",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.FLOAT, 1));
        il.append(InstructionFactory.createLoad(Type.FLOAT, 2));
        il.append(InstructionFactory.createBinaryOperation("/", Type.FLOAT));
        il.append(InstructionFactory.createReturn(Type.FLOAT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodFMUL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.FLOAT, new Type[] { Type.FLOAT, Type.FLOAT }, new String[] { "a", "b" }, "fmul",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.FLOAT, 1));
        il.append(InstructionFactory.createLoad(Type.FLOAT, 2));
        il.append(InstructionFactory.createBinaryOperation("*", Type.FLOAT));
        il.append(InstructionFactory.createReturn(Type.FLOAT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodFREM() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.FLOAT, new Type[] { Type.FLOAT, Type.FLOAT }, new String[] { "a", "b" }, "frem",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.FLOAT, 1));
        il.append(InstructionFactory.createLoad(Type.FLOAT, 2));
        il.append(InstructionFactory.createBinaryOperation("%", Type.FLOAT));
        il.append(InstructionFactory.createReturn(Type.FLOAT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodFSUB() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.FLOAT, new Type[] { Type.FLOAT, Type.FLOAT }, new String[] { "a", "b" }, "fsub",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.FLOAT, 1));
        il.append(InstructionFactory.createLoad(Type.FLOAT, 2));
        il.append(InstructionFactory.createBinaryOperation("-", Type.FLOAT));
        il.append(InstructionFactory.createReturn(Type.FLOAT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIADD() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "iadd",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("+", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIAND() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "iand",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("&", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIDIV() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "idiv",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("/", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIMUL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "imul",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("*", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIOR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "ior",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("|", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIREM() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "irem",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("%", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodISHL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "ishl",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("<<", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodISHR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "ishr",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation(">>", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodISUB() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "isub",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("-", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIUSHR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "iushr",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation(">>>", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodIXOR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.INT, new Type[] { Type.INT, Type.INT }, new String[] { "a", "b" }, "ixor",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.INT, 1));
        il.append(InstructionFactory.createLoad(Type.INT, 2));
        il.append(InstructionFactory.createBinaryOperation("^", Type.INT));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLADD() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "ladd",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("+", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLAND() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "land",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("&", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLDIV() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "ldiv",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("/", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLMUL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lmul",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("*", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLOR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lor",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("|", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLREM() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lrem",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("%", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLSHL() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lshl",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionConst.L2I);
        il.append(InstructionFactory.createBinaryOperation("<<", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLSHR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lshr",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionConst.L2I);
        il.append(InstructionFactory.createBinaryOperation(">>", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLSUB() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lsub",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("-", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLUSHR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lushr",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionConst.L2I);
        il.append(InstructionFactory.createBinaryOperation(">>>", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodLXOR() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.LONG, new Type[] { Type.LONG, Type.LONG }, new String[] { "a", "b" }, "lxor",
                ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);

        il.append(InstructionFactory.createLoad(Type.LONG, 1));
        il.append(InstructionFactory.createLoad(Type.LONG, 3));
        il.append(InstructionFactory.createBinaryOperation("^", Type.LONG));
        il.append(InstructionFactory.createReturn(Type.LONG));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMethodMain() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) },
                new String[] { "args" }, "main", ORG_APACHE_BCEL_GENERIC_BINARY_OP, il, cp);
        method.addException("java.lang.Exception");

        il.append(factory.createNew(ORG_APACHE_BCEL_GENERIC_BINARY_OP));
        il.append(InstructionConst.DUP);
        il.append(factory.createInvoke(ORG_APACHE_BCEL_GENERIC_BINARY_OP, "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createStore(Type.OBJECT, 1));
        il.append(factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Const.GETSTATIC));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke(ORG_APACHE_BCEL_GENERIC_BINARY_OP, "calculate", Type.OBJECT, new Type[] { new ArrayType(Type.STRING, 1) },
                Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.OBJECT }, Const.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
}
