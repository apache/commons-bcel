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

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;

/**
 * Read class file(s) and patch all of its methods, so that they print
 * "hello" and their name and signature before doing anything else.
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 */
public final class helloify implements Constants {

    private static String class_name;
    private static ConstantPoolGen cp;
    private static int out;     // reference to System.out
    private static int println; // reference to PrintStream.println

    public static void main(String[] argv) throws Exception {
        for (String arg : argv) {
            if (arg.endsWith(".class")) {
                JavaClass java_class = new ClassParser(arg).parse();
                ConstantPool constants = java_class.getConstantPool();
                String file_name = arg.substring(0, arg.length() - 6) + "_hello.class";
                cp = new ConstantPoolGen(constants);

                helloifyClassName(java_class);

                out = cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
                println = cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
                // Patch all methods.
                Method[] methods = java_class.getMethods();

                for (int j = 0; j < methods.length; j++) {
                    methods[j] = helloifyMethod(methods[j]);
                }

                // Finally dump it back to a file.
                java_class.setConstantPool(cp.getFinalConstantPool());
                java_class.dump(file_name);
            }
        }
    }

    /**
     * Change class name to <old_name>_hello
     */
    private static void helloifyClassName(JavaClass java_class) {
        class_name = java_class.getClassName() + "_hello";
        int index = java_class.getClassNameIndex();

        index = ((ConstantClass) cp.getConstant(index)).getNameIndex();
        cp.setConstant(index, new ConstantUtf8(class_name.replace('.', '/')));
    }

    /**
     * Patch a method.
     */
    private static Method helloifyMethod(Method m) {
        Code code = m.getCode();
        int flags = m.getAccessFlags();
        String name = m.getName();

        // Sanity check
        if (m.isNative() || m.isAbstract() || (code == null)) {
            return m;
        }
    
        // Create instruction list to be inserted at method start.
        String mesg = "Hello from " + Utility.methodSignatureToString(m.getSignature(),
                name,
                Utility.accessToString(flags));
        InstructionList patch = new InstructionList();
        patch.append(new GETSTATIC(out));
        patch.append(new PUSH(cp, mesg));
        patch.append(new INVOKEVIRTUAL(println));

        MethodGen mg = new MethodGen(m, class_name, cp);
        InstructionList il = mg.getInstructionList();
        InstructionHandle[] ihs = il.getInstructionHandles();

        if (name.equals("<init>")) { // First let the super or other constructor be called
            for (int j = 1; j < ihs.length; j++) {
                if (ihs[j].getInstruction() instanceof INVOKESPECIAL) {
                    il.append(ihs[j], patch); // Should check: method name == "<init>"
                    break;
                }
            }
        } else {
            il.insert(ihs[0], patch);
        }

        // Stack size must be at least 2, since the println method takes 2 argument.
        if (code.getMaxStack() < 2) {
            mg.setMaxStack(2);
        }

        m = mg.getMethod();

        il.dispose(); // Reuse instruction handles

        return m;
    }
}
