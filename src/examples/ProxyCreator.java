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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * Dynamically creates and uses a proxy for {@code java.awt.event.ActionListener}
 * via the classloader mechanism if called with
 * <pre>java org.apache.bcel.util.JavaWrapper ProxyCreator</pre>
 *
 * The trick is to encode the byte code we need into the class name
 * using the Utility.encode() method. This will result however in big
 * ugly class name, so for many cases it will be more sufficient to
 * put some clever creation code into the class loader.<br> This is
 * comparable to the mechanism provided via
 * {@code java.lang.reflect.Proxy}, but much more flexible.
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 * @see org.apache.bcel.util.JavaWrapper
 * @see org.apache.bcel.util.ClassLoader
 * @see Utility
 */
public class ProxyCreator {

    /**
     * Load class and create instance
     */
    public static Object createProxy(String pack, String class_name) {
        try {
            Class<?> cl = Class.forName(pack + "$$BCEL$$" + class_name);
            return cl.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create JavaClass object for a simple proxy for an java.awt.event.ActionListener
     * that just prints the passed arguments, load and use it via the class loader
     * mechanism.
     */
    public static void main(String[] argv) throws Exception {
        ClassLoader loader = ProxyCreator.class.getClassLoader();

        // instanceof won't work here ...
        if (loader.getClass().toString().equals("class org.apache.bcel.util.ClassLoader")) {
            // Real class name will be set by the class loader
            ClassGen cg = new ClassGen("foo", "java.lang.Object", "", Constants.ACC_PUBLIC,
                    new String[]{"java.awt.event.ActionListener"});

            // That's important, otherwise newInstance() won't work
            cg.addEmptyConstructor(Constants.ACC_PUBLIC);

            InstructionList il = new InstructionList();
            ConstantPoolGen cp = cg.getConstantPool();
            InstructionFactory factory = new InstructionFactory(cg);

            int out = cp.addFieldref("java.lang.System", "out",
                    "Ljava/io/PrintStream;");
            int println = cp.addMethodref("java.io.PrintStream", "println",
                    "(Ljava/lang/Object;)V");
            MethodGen mg = new MethodGen(Constants.ACC_PUBLIC, Type.VOID,
                    new Type[]{
                            new ObjectType("java.awt.event.ActionEvent")
                    }, null, "actionPerformed", "foo", il, cp);

            // System.out.println("actionPerformed:" + event);
            il.append(new GETSTATIC(out));
            il.append(factory.createNew("java.lang.StringBuffer"));
            il.append(InstructionConstants.DUP);
            il.append(new PUSH(cp, "actionPerformed:"));
            il.append(factory.createInvoke("java.lang.StringBuffer", "<init>", Type.VOID,
                    new Type[]{Type.STRING}, Constants.INVOKESPECIAL));

            il.append(new ALOAD(1));
            il.append(factory.createAppend(Type.OBJECT));
            il.append(new INVOKEVIRTUAL(println));
            il.append(InstructionConstants.RETURN);

            mg.stripAttributes(true);
            mg.setMaxStack();
            mg.setMaxLocals();
            cg.addMethod(mg.getMethod());

            byte[] bytes = cg.getJavaClass().getBytes();

            System.out.println("Uncompressed class: " + bytes.length);

            String s = Utility.encode(bytes, true);
            System.out.println("Encoded class: " + s.length());

            System.out.print("Creating proxy ... ");
            ActionListener a = (ActionListener) createProxy("foo.bar.", s);
            System.out.println("Done. Now calling actionPerformed()");

            a.actionPerformed(new ActionEvent(a, ActionEvent.ACTION_PERFORMED, "hello"));
        } else {
            System.err.println("Call me with java org.apache.bcel.util.JavaWrapper ProxyCreator");
        }
    }

}
