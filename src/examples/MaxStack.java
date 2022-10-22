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

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;

/**
 * Read class file(s) and examine all of its methods, determining the maximum stack depth used by analyzing control
 * flow.
 */
public final class MaxStack {

    public static void main(final String[] argv) throws Exception {
        for (final String className : argv) {
            JavaClass javaClass = Repository.lookupClass(className);

            if (javaClass == null) {
                javaClass = new ClassParser(className).parse();
            }

            final ConstantPoolGen cp = new ConstantPoolGen(javaClass.getConstantPool());

            for (final Method m : javaClass.getMethods()) {
                if (!(m.isAbstract() || m.isNative())) {
                    final MethodGen mg = new MethodGen(m, className, cp);

                    final int compiledStack = mg.getMaxStack();
                    final int compiledLocals = mg.getMaxLocals();
                    mg.setMaxStack(); // Recompute value
                    mg.setMaxLocals();
                    final int computedStack = mg.getMaxStack();
                    final int computedLocals = mg.getMaxLocals();

                    mg.getInstructionList().dispose(); // Reuse instruction handles

                    System.out.println(m);

                    if (computedStack == compiledStack) {
                        System.out.println("Stack ok(" + computedStack + ")");
                    } else {
                        System.out.println("\nCompiled stack size " + compiledStack + " computed size " + computedStack);
                    }

                    if (computedLocals == compiledLocals) {
                        System.out.println("Locals ok(" + computedLocals + ")");
                    } else {
                        System.out.println("\nCompiled locals " + compiledLocals + " computed size " + computedLocals);
                    }
                }
            }
        }
    }
}
