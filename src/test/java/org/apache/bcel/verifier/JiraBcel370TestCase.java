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

package org.apache.bcel.verifier;

import java.io.File;
import java.io.FileInputStream;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.MethodGen;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests BCEL-370.
 */
public class JiraBcel370TestCase extends AbstractTestCase {
    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "target/test-classes/com/puppycrawl/tools/checkstyle/grammar/java/JavaLanguageParser$ClassBlockContext.class",
        "target/test-classes/com/foo/Foo.class"
    })
    // @formatter:on
    public void testLdcGetType(final String classFileName) throws Exception {
        try (FileInputStream file = new FileInputStream(classFileName)) {
            final ClassParser parser = new ClassParser(file, new File(classFileName).getName());
            final JavaClass clazz = parser.parse();

            final Method[] methods = clazz.getMethods();

            final ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());
            final MethodGen methodGen = new MethodGen(methods[0], classFileName, cp);

            // The first instruction is an LDC CONSTANT_Dynamic added by Jacoco
            final Instruction instruction = methodGen.getInstructionList().getInstructions()[0];

            instruction.accept(new EmptyVisitor() {
                @Override
                public void visitLDC(final LDC ldc) {
                    // Without the change to LDC.getType() this fails because the tag is CONSTANT_Dynamic
                    ldc.getType(cp);
                }
            });
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "com.foo.Foo"
    })
    // @formatter:on
    public void testVerify(final String className) throws ClassNotFoundException {
        // Without the changes to the verifier this fails because it doesn't allow LDC CONSTANT_Dynamic
        Verifier.verifyType(className);
    }
}
