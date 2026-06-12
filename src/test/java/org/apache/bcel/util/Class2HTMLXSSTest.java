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
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

class Class2HTMLXSSTest {

    /**
     * A field name in the constant pool is attacker controlled and may contain HTML metacharacters; the generated
     * documentation must escape it in text context rather than emit it raw.
     */
    @Test
    void testFieldNameIsEscaped() throws Exception {
        final ClassGen cg = new ClassGen("Evil", "java.lang.Object", "Evil.java", Const.ACC_PUBLIC, null);
        cg.addField(new FieldGen(Const.ACC_PUBLIC, Type.INT, "x<script>alert(1)</script>", cg.getConstantPool()).getField());
        final JavaClass jc = cg.getJavaClass();

        final File outputDir = new File("target/test-output/html-xss");
        if (!outputDir.mkdirs()) {
            assertTrue(outputDir.isDirectory());
        }
        new Class2HTML(jc, outputDir.getAbsolutePath() + File.separator);

        final String methods = new String(Files.readAllBytes(new File(outputDir, "Evil_methods.html").toPath()), StandardCharsets.UTF_8);
        // The field name rendered as link text must be escaped, not emitted as a live tag.
        assertFalse(methods.contains("\">x<script>"), "field name was emitted unescaped in text context");
        assertTrue(methods.contains("&lt;script&gt;"), "expected the field name to be HTML-escaped");
    }
}
