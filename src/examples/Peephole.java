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

import org.apache.commons.bcel6.Repository;
import org.apache.commons.bcel6.classfile.JavaClass;
import org.apache.commons.bcel6.classfile.Method;
import org.apache.commons.bcel6.generic.ConstantPoolGen;
import org.apache.commons.bcel6.generic.InstructionHandle;
import org.apache.commons.bcel6.generic.InstructionList;
import org.apache.commons.bcel6.generic.InstructionTargeter;
import org.apache.commons.bcel6.generic.MethodGen;
import org.apache.commons.bcel6.generic.TargetLostException;
import org.apache.commons.bcel6.util.InstructionFinder;

/**
 * Remove NOPs from given class
 *
 * @version $Id$
 */
public class Peephole {

    public static void main(String[] argv) {
        try {
            // Load the class from CLASSPATH.
            JavaClass clazz = Repository.lookupClass(argv[0]);
            Method[] methods = clazz.getMethods();
            ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

            for (int i = 0; i < methods.length; i++) {
                if (!(methods[i].isAbstract() || methods[i].isNative())) {
                    MethodGen mg = new MethodGen(methods[i], clazz.getClassName(), cp);
                    Method stripped = removeNOPs(mg);

                    if (stripped != null) {
                        methods[i] = stripped; // Overwrite with stripped method
                    }
                }
            }

            // Dump the class to <class name>_.class
            clazz.setConstantPool(cp.getFinalConstantPool());
            clazz.dump(clazz.getClassName() + "_.class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Method removeNOPs(MethodGen mg) {
        InstructionList il = mg.getInstructionList();
        InstructionFinder f = new InstructionFinder(il);
        String pat = "NOP+"; // Find at least one NOP
        InstructionHandle next = null;
        int count = 0;

        for (Iterator<InstructionHandle[]> e = f.search(pat); e.hasNext(); ) {
            InstructionHandle[] match = e.next();
            InstructionHandle first = match[0];
            InstructionHandle last = match[match.length - 1];

            // Some nasty Java compilers may add NOP at end of method.
            if ((next = last.getNext()) == null) {
                break;
            }

            count += match.length;

            // Delete NOPs and redirect any references to them to the following (non-nop) instruction.
            try {
                il.delete(first, last);
            } catch (TargetLostException e2) {
                for (InstructionHandle target : e2.getTargets()) {
                    for (InstructionTargeter targeter : target.getTargeters()) {
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
