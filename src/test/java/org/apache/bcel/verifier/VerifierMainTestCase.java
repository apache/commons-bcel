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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.SWAP;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.jupiter.api.Test;

public class VerifierMainTestCase extends AbstractTestCase {

    @Test
    public void testSWAP() throws Exception {
        final String[] argv = { "src/test/java/org/apache/bcel/data/SWAP.java", "-g", "-source", "1.4", "-target", "1.4", "-d", "target/test-classes" };
        new Main(new PrintWriter(System.out), new PrintWriter(System.err), false/*systemExit*/, null/*options*/, null/*progress*/).compile(argv);
        final String javaAgent = getJavaAgent();
        final List<String> args = new ArrayList<>();
        args.add("java");
        if (javaAgent != null) {
            args.add(javaAgent.replace("jacoco.exec", "jacoco_org.apache.bcel.data.SWAP.exec"));
        }
        args.add("-cp");
        args.add(System.getProperty("java.class.path"));
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
        // Class has passed the JustIce verifier, but now we need to ensure that the SWAP instruction is in the compiled class.
        final List<SWAP> swapInstructionsList = new ArrayList<>();
        final EmptyVisitor swapCollector = new EmptyVisitor() {
            @Override
            public void visitSWAP(final SWAP obj) {
                swapInstructionsList.add(obj);
                super.visitSWAP(obj);
            }
        };
        try (InputStream in = Files.newInputStream(Paths.get("target/test-classes/org/apache/bcel/data/SWAP.class"))) {
            final ClassParser classParser = new ClassParser(in, "SWAP.class");
            final JavaClass javaClass = classParser.parse();
            final Method method = javaClass.getMethod(org.apache.bcel.data.SWAP.class.getMethod("getTestConstructor", Class.class));
            final byte[] code = method.getCode().getCode();
            final InstructionList instructionList = new InstructionList(code);
            for (final InstructionHandle instructionHandle : instructionList) {
                instructionHandle.accept(swapCollector);
            }
        }
        assertEquals(1, swapInstructionsList.size());
    }
}
