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

package org.apache.bcel.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.AbstractTest;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.SWAP;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class VerifierMainTest extends AbstractTest {

    @TempDir
    Path tempDir;

    @Test
    void testSWAP() throws Exception {
        final String className = "org.apache.bcel.data.SWAP";
        final Path classFile = tempDir.resolve("org/apache/bcel/data/SWAP.class");
        final ClassGen classGen = new ClassGen(className, "java.lang.Object", "SWAP.java", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        classGen.setMajor(Const.MAJOR_1_4);
        classGen.setMinor(Const.MINOR_1_4);
        classGen.addEmptyConstructor(Const.ACC_PUBLIC);
        final InstructionList instructions = new InstructionList();
        try {
            instructions.append(new ILOAD(0));
            instructions.append(new ILOAD(1));
            instructions.append(InstructionConst.SWAP);
            instructions.append(InstructionConst.ISUB);
            instructions.append(InstructionConst.IRETURN);
            final MethodGen methodGen = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, new Type[] { Type.INT, Type.INT },
                new String[] { "first", "second" }, "swap", className, instructions, classGen.getConstantPool());
            methodGen.setMaxStack();
            methodGen.setMaxLocals();
            classGen.addMethod(methodGen.getMethod());
        } finally {
            instructions.dispose();
        }
        classGen.getJavaClass().dump(classFile.toFile());
        final String javaAgent = getJavaAgent();
        final List<String> args = new ArrayList<>();
        args.add(SystemProperties.getJavaHome() + File.separator + "bin" + File.separator + "java");
        if (javaAgent != null) {
            args.add(javaAgent.replace("jacoco.exec", "jacoco_org.apache.bcel.data.SWAP.exec"));
        }
        args.add("-cp");
        args.add(tempDir + File.pathSeparator + SystemProperties.getJavaClassPath());
        args.add("org.apache.bcel.verifier.Verifier");
        args.add("org/apache/bcel/data/SWAP.class");
        final ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        final Process p = pb.start();
        try (BufferedInputStream is = new BufferedInputStream(p.getInputStream())) {
            final byte[] buff = new byte[2048];
            final StringBuilder sb = new StringBuilder();
            for (int len; (len = is.read(buff)) != -1;) {
                sb.append(new String(buff, 0, len));
            }
            final String output = sb.toString();
            assertEquals(0, p.waitFor(), output);
            assertEquals(0, StringUtils.countMatches(output, "VERIFIED_REJECTED"), output);
            assertEquals(6, StringUtils.countMatches(output, "VERIFIED_OK"), output);
        }
        // Class has passed the JustIce verifier, but now we need to ensure that the SWAP instruction is in the generated class.
        final List<SWAP> swapInstructionsList = new ArrayList<>();
        final EmptyVisitor swapCollector = new EmptyVisitor() {
            @Override
            public void visitSWAP(final SWAP obj) {
                swapInstructionsList.add(obj);
                super.visitSWAP(obj);
            }
        };
        try (InputStream in = Files.newInputStream(classFile)) {
            final ClassParser classParser = new ClassParser(in, "SWAP.class");
            final JavaClass javaClass = classParser.parse();
            for (final Method method : javaClass.getMethods()) {
                final InstructionList instructionList = new InstructionList(method.getCode().getCode());
                try {
                    for (final InstructionHandle instructionHandle : instructionList) {
                        instructionHandle.accept(swapCollector);
                    }
                } finally {
                    instructionList.dispose();
                }
            }
        }
        assertEquals(1, swapInstructionsList.size());
    }
}
