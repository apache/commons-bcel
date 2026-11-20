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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    /**
     * Quotes in attacker-controlled names must be escaped in attribute context (HREF/NAME values), or the name
     * breaks out of the attribute and injects live event handlers.
     */
    @Test
    void testFieldNameQuotesAreEscapedInAttributeContext() throws Exception {
        final ClassGen cg = new ClassGen("Evil2", "java.lang.Object", "Evil2.java", Const.ACC_PUBLIC, null);
        cg.addField(new FieldGen(Const.ACC_PUBLIC, Type.INT, "x\" onmouseover=\"alert(1)", cg.getConstantPool()).getField());
        final JavaClass jc = cg.getJavaClass();

        final File outputDir = new File("target/test-output/html-xss");
        if (!outputDir.mkdirs()) {
            assertTrue(outputDir.isDirectory());
        }
        new Class2HTML(jc, outputDir.getAbsolutePath() + File.separator);

        final String methods = new String(Files.readAllBytes(new File(outputDir, "Evil2_methods.html").toPath()), StandardCharsets.UTF_8);
        assertFalse(methods.contains("x\" onmouseover"), "field name broke out of the attribute context");
        assertTrue(methods.contains("x&quot; onmouseover"), "expected quotes in the field name to be escaped");
    }

    /**
     * Tests that an unknown code-level attribute description is shown with its actual (escaped) name.
     *
     * <p>
     * Prior to the fix, the attribute tag was passed to {@code Const.getAttributeName()} unconditionally, which threw
     * {@code ArrayIndexOutOfBoundsException} for {@code Const.ATTR_UNKNOWN} (-1), so Class2HTML crashed on any method whose Code attribute
     * has an unknown sub-attribute.
     * </p>
     */
    @Test
    void testUnknownCodeAttributeNameIsShown(@TempDir final Path outputDir) throws Exception {
        final ClassGen cg = new ClassGen("EvilUnknownCodeAttr", "java.lang.Object", "EvilUnknownCodeAttr.java", Const.ACC_PUBLIC, null);
        final ConstantPoolGen cpg = cg.getConstantPool();
        final String marker = "x<script>alert(1)</script>";
        final int nameIndex = cpg.addUtf8(marker);
        final InstructionList instructions = new InstructionList();
        instructions.append(InstructionConst.ICONST_0);
        instructions.append(InstructionConst.IRETURN);
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, Type.NO_ARGS, null, "value", "EvilUnknownCodeAttr", instructions, cpg);
        mg.setMaxStack();
        mg.setMaxLocals();
        final Method method = mg.getMethod();
        for (final Attribute attribute : method.getAttributes()) {
            if (attribute instanceof Code) {
                final Code code = (Code) attribute;
                final Attribute[] subAttributes = code.getAttributes();
                final Attribute[] extended = Arrays.copyOf(subAttributes, subAttributes.length + 1);
                extended[subAttributes.length] = new Unknown(nameIndex, 0, new byte[0], cpg.getConstantPool());
                code.setAttributes(extended);
            }
        }
        cg.addMethod(method);
        instructions.dispose();

        final JavaClass jc = new ClassParser(new ByteArrayInputStream(cg.getJavaClass().getBytes()), "EvilUnknownCodeAttr.class").parse();
        assertNotNull(jc.getMethods()[0].getCode(), "expected the parsed method to retain its Code attribute");
        new Class2HTML(jc, outputDir.toAbsolutePath() + File.separator);

        final String codeHtml = new String(Files.readAllBytes(outputDir.resolve("EvilUnknownCodeAttr_code.html")), StandardCharsets.UTF_8);
        assertFalse(codeHtml.contains(">SourceFile</A>"), "unknown code attribute was mislabeled as SourceFile");
        assertTrue(codeHtml.contains("<LI>(Unknown attribute x&lt;script&gt;alert(1)&lt;/script&gt;)</LI>"),
            "expected the unknown code attribute description to be HTML-escaped");
    }

    /**
     * Tests that unknown method attribute descriptions are HTML-escaped.
     *
     * <p>
     * Attribute names come from attacker-controlled {@code CONSTANT_Utf8} entries and may contain HTML metacharacters. This is a regression for an
     * escaping omission in the earlier fix for CVE-2026-105111.
     * </p>
     */
    @ParameterizedTest
    @CsvSource({
        "0, ''",
        "1, ': 00'",
        "10, ': 00 00 00 00 00 00 00 00 00 00'",
        "11, ': 00 00 00 00 00 00 00 00 00 00... (truncated)'"
    })
    void testUnknownMethodAttributeNameIsEscaped(final int length, final String expectedPayload, @TempDir final Path outputDir) throws Exception {
        final ClassGen cg = new ClassGen("EvilUnknownAttr", "java.lang.Object", "EvilUnknownAttr.java", Const.ACC_PUBLIC, null);
        final ConstantPoolGen cpg = cg.getConstantPool();
        final String marker = "x<svg onload=\"alert(1)\">&'\"y";
        final int nameIndex = cpg.addUtf8(marker);
        final InstructionList instructions = new InstructionList();
        instructions.append(InstructionConst.ICONST_0);
        instructions.append(InstructionConst.IRETURN);
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, Type.NO_ARGS, null, "value", "EvilUnknownAttr", instructions, cpg);
        mg.addAttribute(new Unknown(nameIndex, length, new byte[length], cpg.getConstantPool()));
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());
        instructions.dispose();

        final JavaClass jc = new ClassParser(new ByteArrayInputStream(cg.getJavaClass().getBytes()), "EvilUnknownAttr.class").parse();
        assertNotNull(jc.getMethods()[0].getCode(), "expected the parsed method to retain its Code attribute");
        new Class2HTML(jc, outputDir.toAbsolutePath() + File.separator);

        final String code = new String(Files.readAllBytes(outputDir.resolve("EvilUnknownAttr_code.html")), StandardCharsets.UTF_8);
        assertFalse(code.contains("<svg"), "unknown attribute name was emitted as a live HTML element");
        assertTrue(code.contains("<LI>(Unknown attribute x&lt;svg onload=&quot;alert(1)&quot;&gt;&amp;&#39;&quot;y" + expectedPayload + ")</LI>"),
            "expected the complete unknown attribute description to be HTML-escaped");
    }
}
