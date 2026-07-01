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

package org.apache.bcel.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

class CodeHTMLNarrowingTest {

    /**
     * A wide local-variable index is a u2 in the byte code; disassembling a class whose index is >= 0x8000 must render the
     * real slot number, not a sign-extended negative. The reference disassembler {@code Utility.codeToString} reads it via
     * {@code readUnsignedShort}.
     */
    @Test
    void testWideLocalVariableIndexIsUnsigned() throws Exception {
        final int index = 40000; // > 0x7FFF so a signed read wraps negative
        final ClassGen cg = new ClassGen("Wide", "java.lang.Object", "Wide.java", Const.ACC_PUBLIC, null);
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        il.append(new ILOAD(index)); // emitted as WIDE + iload + u2 index
        il.append(InstructionFactory.createReturn(Type.INT));
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, Type.NO_ARGS, null, "m", "Wide", il, cp);
        mg.setMaxStack(1);
        mg.setMaxLocals(index + 1);
        cg.addMethod(mg.getMethod());
        final JavaClass jc = cg.getJavaClass();

        final File outputDir = new File("target/test-output/html-narrowing");
        if (!outputDir.mkdirs()) {
            assertTrue(outputDir.isDirectory());
        }
        new Class2HTML(jc, outputDir.getAbsolutePath() + File.separator);

        final String code = new String(Files.readAllBytes(new File(outputDir, "Wide_code.html").toPath()), StandardCharsets.UTF_8);
        assertTrue(code.contains("%" + index), "expected the wide index rendered as " + index);
        assertFalse(code.contains("%" + (short) index), "wide index was sign-extended to " + (short) index);
    }
}
