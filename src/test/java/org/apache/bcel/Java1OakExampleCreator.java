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

package org.apache.bcel;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

/**
 * Creates the version 45.2 fixture corresponding to {@code src/test/resources/Java1OakExample.java}, using compact Code headers.
 * Each header stores {@code max_stack}, {@code max_locals}, and {@code code_length} in one, one, and two bytes, respectively.
 *
 * <p>
 * To regenerate the fixture from the project directory, run:
 * </p>
 * <pre>
 * mvn test-compile exec:java -Dexec.classpathScope=test -Dexec.mainClass=org.apache.bcel.Java1OakExampleCreator
 * </pre>
 */
public final class Java1OakExampleCreator {

    /**
     * Creates the example class with compact Code headers.
     *
     * @return The example class.
     */
    public static JavaClass create() {
        final ClassGen cg = new ClassGen("Java1OakExample", "java.lang.Object", "Java1OakExample.java",
            Const.ACC_PUBLIC | Const.ACC_SUPER, new String[0]);
        cg.setMinor(2);
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
        final InstructionList il = new InstructionFactory(cg).createPrintln("Hello Oak!");
        il.append(InstructionConst.RETURN);
        final MethodGen main = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, new Type[] {new ArrayType(Type.STRING, 1)},
            new String[] {"args"}, "main", cg.getClassName(), il, cg.getConstantPool());
        main.setMaxStack();
        main.setMaxLocals();
        cg.addMethod(main.getMethod());
        il.dispose();
        final JavaClass result = cg.getJavaClass();
        // Select the historical four-byte Code header explicitly; version 45.2 can also use the standard eight-byte layout.
        for (final Method method : result.getMethods()) {
            method.getCode().setCompactCodeHeader(true);
        }
        return result;
    }

    /**
     * Writes the fixture to its resource path or the given file.
     *
     * @param args Optional output file name.
     * @throws IOException Thrown if the output cannot be written.
     */
    public static void main(final String[] args) throws IOException {
        create().dump(args.length == 0 ? "src/test/resources/Java1OakExample.class" : args[0]);
    }

    private Java1OakExampleCreator() {
    }
}
