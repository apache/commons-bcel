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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class HelloWorldCreator {
    private static final String ORG_APACHE_BCEL_HELLO_WORLD = "org.apache.bcel.HelloWorld";
    public static void main(final String[] args) throws Exception {
        final org.apache.bcel.HelloWorldCreator creator = new org.apache.bcel.HelloWorldCreator();
        final Path path = Paths.get("target/test-classes/org/apache/bcel/HelloWorld.class");
        Files.deleteIfExists(path);
        try (OutputStream out = Files.newOutputStream(path)) {
            creator.create(out);
        }
    }
    private final InstructionFactory factory;
    private final ConstantPoolGen cp;

    private final ClassGen cg;

    public HelloWorldCreator() {
        cg = new ClassGen(ORG_APACHE_BCEL_HELLO_WORLD, "java.lang.Object", "HelloWorld.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        cg.setMajor(Const.MAJOR_1_8);
        cg.setMinor(Const.MINOR_1_8);

        cp = cg.getConstantPool();
        factory = new InstructionFactory(cg, cp);
    }

    public void create(final OutputStream out) throws IOException {
        createConstructor();
        createMainMethod();
        cg.getJavaClass().dump(out);
    }

    private void createConstructor() {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", ORG_APACHE_BCEL_HELLO_WORLD, il, cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }

    private void createMainMethod() {
        final InstructionList il = factory.createPrintln("Hello World!");
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main",
                ORG_APACHE_BCEL_HELLO_WORLD, il, cp);

        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
}
