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
 *
 */

import java.io.IOException;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConstants;
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
 * <PRE>
 * import java.io.*;
 *
 * public class HelloWorld {
 *     public static void main(String[] argv) {
 *         BufferedReader in   = new BufferedReader(new InputStreamReader(System.in));
 *         String name = null;
 * 
 *         try {
 *             System.out.print("Please enter your name> ");
 *             name = in.readLine();
 *         } catch(IOException e) { 
 *             System.out.println(e);
 *             return; 
 *         }
 * 
 *         System.out.println("Hello, " + name);
 *     }
 * }
 * </PRE>
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 */
public class HelloWorldBuilder {
    public static void main(String[] argv) {
        ClassGen cg = new ClassGen("HelloWorld", "java.lang.Object",
                "<generated>", Constants.ACC_PUBLIC |
                Constants.ACC_SUPER,
                null);
        ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Constants.ACC_STATIC |
                Constants.ACC_PUBLIC,// access flags
                Type.VOID,              // return type
                new Type[]{            // argument types
                        new ArrayType(Type.STRING, 1)
                },
                new String[]{"argv"}, // arg names
                "main", "HelloWorld",    // method, class
                il, cp);
        InstructionFactory factory = new InstructionFactory(cg);

        ObjectType i_stream = new ObjectType("java.io.InputStream");
        ObjectType p_stream = new ObjectType("java.io.PrintStream");

        // Create BufferedReader object and store it in local variable `in'.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConstants.DUP); // Use predefined constant, i.e. flyweight
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConstants.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", i_stream, Constants.GETSTATIC));

        // Call constructors, i.e. BufferedReader(InputStreamReader())
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
                Type.VOID, new Type[]{i_stream},
                Constants.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID,
                new Type[]{new ObjectType("java.io.Reader")},
                Constants.INVOKESPECIAL));

        // Create local variable `in'
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType("java.io.BufferedReader"), null, null);
        int in = lg.getIndex();
        lg.setStart(il.append(new ASTORE(in))); // `i' valid from here

        // Create local variable `name'
        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        int name = lg.getIndex();
        il.append(InstructionConstants.ACONST_NULL);
        lg.setStart(il.append(new ASTORE(name))); // `name' valid from here

        // try { ...
        InstructionHandle try_start =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Constants.GETSTATIC));

        il.append(new PUSH(cp, "Please enter your name> "));
        il.append(factory.createInvoke("java.io.PrintStream", "print", Type.VOID,
                new Type[]{Type.STRING}, Constants.INVOKEVIRTUAL));
        il.append(new ALOAD(in));
        il.append(factory.createInvoke("java.io.BufferedReader", "readLine",
                Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        il.append(new ASTORE(name));

        // Upon normal execution we jump behind exception handler, the target address is not known yet.
        GOTO g = new GOTO(null);
        InstructionHandle try_end = il.append(g);

        /* } catch() { ... }
         * Add exception handler: print exception and return from method
         */
        InstructionHandle handler =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Constants.GETSTATIC));
        // Little trick in order not to save exception object temporarily
        il.append(InstructionConstants.SWAP);

        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[]{Type.OBJECT}, Constants.INVOKEVIRTUAL));
        il.append(InstructionConstants.RETURN);
        mg.addExceptionHandler(try_start, try_end, handler, new ObjectType("java.io.IOException"));

        // Normal code continues, now we can set the branch target of the GOTO that jumps over the handler code.
        InstructionHandle ih =
                il.append(factory.createFieldAccess("java.lang.System", "out", p_stream, Constants.GETSTATIC));
        g.setTarget(ih);

        // String concatenation compiles to StringBuffer operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConstants.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il.append(factory.createInvoke("java.lang.StringBuffer", "<init>",
                Type.VOID, new Type[]{Type.STRING},
                Constants.INVOKESPECIAL));
        il.append(new ALOAD(name));
    
        // Concatenate strings using a StringBuffer and print them.
        il.append(factory.createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS,
                Constants.INVOKEVIRTUAL));

        il.append(factory.createInvoke("java.io.PrintStream", "println",
                Type.VOID, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));

        il.append(InstructionConstants.RETURN);

        mg.setMaxStack(5); // Needed stack size
        cg.addMethod(mg.getMethod());

        il.dispose(); // Reuse instruction handles

        // Add public <init> method, i.e. empty constructor
        cg.addEmptyConstructor(Constants.ACC_PUBLIC);

        // Get JavaClass object and dump it to file.
        try {
            cg.getJavaClass().dump("HelloWorld.class");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
