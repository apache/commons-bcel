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

import java.io.IOException;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * Create HelloWorld class:
 *
 * <PRE>
 * import java.io.*;
 *
 * public class HelloWorld {
 *     public static void main(String[] argv) {
 *         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 *         String name = null;
 *
 *         try {
 *             System.out.print("Please enter your name> ");
 *             name = in.readLine();
 *         } catch (IOException e) {
 *             System.out.println(e);
 *             return;
 *         }
 *
 *         System.out.println("Hello, " + name);
 *     }
 * }
 * </PRE>
 */
public class HelloWorldBuilder {
    public static void main(final String[] argv) {
        final ClassGen cg = new ClassGen("HelloWorld", "java.lang.Object", "<generated>", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        final ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
        final InstructionList il = new InstructionList();
        final MethodGen mg = new MethodGen(Const.ACC_STATIC | Const.ACC_PUBLIC, // access flags
            Type.VOID, // return type
            new Type[] { // argument types
                new ArrayType(Type.STRING, 1)},
            new String[] {"argv"}, // arg names
            "main", "HelloWorld", // method, class
            il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);

        final ObjectType iStream = new ObjectType("java.io.InputStream");
        final ObjectType pStream = new ObjectType("java.io.PrintStream");

        // Create BufferedReader object and store it in local variable 'in'.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant, i.e. flyweight
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", iStream, Const.GETSTATIC));

        // Call constructors, i.e. BufferedReader(InputStreamReader())
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>", Type.VOID, new Type[] {iStream}, Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID, new Type[] {new ObjectType("java.io.Reader")}, Const.INVOKESPECIAL));

        // Create local variable 'in'
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType("java.io.BufferedReader"), null, null);
        final int in = lg.getIndex();
        lg.setStart(il.append(new ASTORE(in))); // 'i' valid from here

        // Create local variable 'name'
        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        final int name = lg.getIndex();
        il.append(InstructionConst.ACONST_NULL);
        lg.setStart(il.append(new ASTORE(name))); // 'name' valid from here

        // try { ...
        final InstructionHandle tryStart = il.append(factory.createFieldAccess("java.lang.System", "out", pStream, Const.GETSTATIC));

        il.append(new PUSH(cp, "Please enter your name> "));
        il.append(factory.createInvoke("java.io.PrintStream", "print", Type.VOID, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(new ALOAD(in));
        il.append(factory.createInvoke("java.io.BufferedReader", "readLine", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(new ASTORE(name));

        // Upon normal execution we jump behind exception handler, the target address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle tryEnd = il.append(g);

        /*
         * } catch() { ... } Add exception handler: print exception and return from method
         */
        final InstructionHandle handler = il.append(factory.createFieldAccess("java.lang.System", "out", pStream, Const.GETSTATIC));
        // Little trick in order not to save exception object temporarily
        il.append(InstructionConst.SWAP);

        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] {Type.OBJECT}, Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        mg.addExceptionHandler(tryStart, tryEnd, handler, new ObjectType("java.io.IOException"));

        // Normal code continues, now we can set the branch target of the GOTO that jumps over the handler code.
        final InstructionHandle ih = il.append(factory.createFieldAccess("java.lang.System", "out", pStream, Const.GETSTATIC));
        g.setTarget(ih);

        // String concatenation compiles to StringBuffer operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il.append(factory.createInvoke("java.lang.StringBuffer", "<init>", Type.VOID, new Type[] {Type.STRING}, Const.INVOKESPECIAL));
        il.append(new ALOAD(name));

        // Concatenate strings using a StringBuffer and print them.
        il.append(factory.createInvoke("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));

        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));

        il.append(InstructionConst.RETURN);

        mg.setMaxStack(5); // Needed stack size
        cg.addMethod(mg.getMethod());

        il.dispose(); // Reuse instruction handles

        // Add public <init> method, i.e. empty constructor
        cg.addEmptyConstructor(Const.ACC_PUBLIC);

        // Get JavaClass object and dump it to file.
        try {
            cg.getJavaClass().dump("HelloWorld.class");
        } catch (final IOException e) {
            System.err.println(e);
        }
    }
}
