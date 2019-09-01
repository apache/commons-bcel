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

import java.util.Iterator;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.util.InstructionFinder;

/**
 * Remove NOPs from given class
 *
 */
public class Peephole {

    public static void main(final String[] argv) {
        try {
            // Load the class from CLASSPATH.
            final JavaClass clazz = Repository.lookupClass(argv[0]);
            final Method[] methods = clazz.getMethods();
            final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

            for (int i = 0; i < methods.length; i++) {
                if (!(methods[i].isAbstract() || methods[i].isNative())) {
                    final MethodGen mg = new MethodGen(methods[i], clazz.getClassName(), cp);
                    final Method stripped = removeNOPs(mg);

                    if (stripped != null) {
                        methods[i] = stripped; // Overwrite with stripped method
                    }
                }
            }

            // Dump the class to <class name>_.class
            clazz.setConstantPool(cp.getFinalConstantPool());
            clazz.dump(clazz.getClassName() + "_.class");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static Method removeNOPs(final MethodGen mg) {
        final InstructionList il = mg.getInstructionList();
        final InstructionFinder f = new InstructionFinder(il);
        final String pat = "NOP+"; // Find at least one NOP
        InstructionHandle next = null;
        int count = 0;

        for (final Iterator<InstructionHandle[]> e = f.search(pat); e.hasNext(); ) {
            final InstructionHandle[] match = e.next();
            final InstructionHandle first = match[0];
            final InstructionHandle last = match[match.length - 1];

            // Some nasty Java compilers may add NOP at end of method.
            if ((next = last.getNext()) == null) {
                break;
            }

            count += match.length;

            // Delete NOPs and redirect any references to them to the following (non-nop) instruction.
            try {
                il.delete(first, last);
            } catch (final TargetLostException e2) {
                for (final InstructionHandle target : e2.getTargets()) {
                    for (final InstructionTargeter targeter : target.getTargeters()) {
                        targeter.updateTarget(target, next);
                    }
                }
            }
        }

        Method m = null;

        if (count > 0) {
            System.out.println("Removed " + count + " NOP instructions from method " + mg.getName());
            m = mg.getMethod();
        }

        il.dispose(); // Reuse instruction handles
        return m;
    }
}
