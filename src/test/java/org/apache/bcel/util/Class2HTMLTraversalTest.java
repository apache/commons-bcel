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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests that unsafe class names are rejected before any report files are written and that accepted names produce working report links.
 */
class Class2HTMLTraversalTest {

    private static JavaClass createClass(final String className) throws IOException {
        final ClassGen cg = new ClassGen(className, "java.lang.Object", "Example.java", Const.ACC_PUBLIC, null);
        final ConstantPoolGen cp = cg.getConstantPool();
        final FieldGen field = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.INT, "answer", cp);
        field.setInitValue(42);
        cg.addField(field.getField());
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
        final InstructionFactory factory = new InstructionFactory(cg);
        final InstructionList instructions = new InstructionList();
        instructions.append(factory.createGetStatic(className, "answer", Type.INT));
        instructions.append(InstructionConst.IRETURN);
        final MethodGen read = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, Type.NO_ARGS, null, "read", className, instructions, cp);
        read.addLineNumber(instructions.getStart(), 1);
        read.setMaxStack();
        read.setMaxLocals();
        cg.addMethod(read.getMethod());
        instructions.dispose();
        final InstructionList callerInstructions = new InstructionList();
        callerInstructions.append(factory.createInvoke(className, "read", Type.INT, Type.NO_ARGS, Const.INVOKESTATIC));
        callerInstructions.append(InstructionConst.IRETURN);
        final MethodGen call = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.INT, Type.NO_ARGS, null, "call", className, callerInstructions, cp);
        call.setMaxStack();
        call.setMaxLocals();
        cg.addMethod(call.getMethod());
        callerInstructions.dispose();
        return new ClassParser(new ByteArrayInputStream(cg.getJavaClass().getBytes()), "Example.class").parse();
    }

    private static List<String> getLinks(final String html) throws IOException {
        final List<String> links = new ArrayList<>();
        new ParserDelegator().parse(new StringReader(html), new HTMLEditorKit.ParserCallback() {
            private void addLinks(final MutableAttributeSet attributes) {
                for (final HTML.Attribute attribute : new HTML.Attribute[] {HTML.Attribute.HREF, HTML.Attribute.SRC}) {
                    final Object value = attributes.getAttribute(attribute);
                    if (value != null) {
                        links.add(value.toString());
                    }
                }
            }

            @Override
            public void handleSimpleTag(final HTML.Tag tag, final MutableAttributeSet attributes, final int position) {
                addLinks(attributes);
            }

            @Override
            public void handleStartTag(final HTML.Tag tag, final MutableAttributeSet attributes, final int position) {
                addLinks(attributes);
            }
        }, true);
        return links;
    }

    private static Stream<Arguments> unsafeClassNames() {
        return Stream.of(
            Arguments.of("C:pwn", "3a"),
            Arguments.of("..\\..\\pwn", "5c"),
            Arguments.of("Evil#Fragment", "23"),
            Arguments.of("Evil%2FPath", "25"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Example", "A&B C", "A&copy B", " LeadingSpace", "A'B", "A+B", "Euro€Class", "pkg.Dollar$Class"})
    void testAcceptedClassNameLinks(final String className, @TempDir final Path outputDir) throws IOException {
        new Class2HTML(createClass(className), outputDir + File.separator);
        try (Stream<Path> files = Files.list(outputDir)) {
            assertEquals(5, files.count());
        }
        final Set<String> expectedFiles = new HashSet<>();
        for (final String suffix : new String[] {".html", "_attributes.html", "_code.html", "_cp.html", "_methods.html"}) {
            expectedFiles.add(className + suffix);
        }
        final Set<String> referencedFiles = new HashSet<>();
        for (final String fileName : expectedFiles) {
            final String html = new String(Files.readAllBytes(outputDir.resolve(fileName)), StandardCharsets.UTF_8);
            final List<String> links = getLinks(html);
            assertFalse(links.isEmpty(), fileName);
            for (final String link : links) {
                assertEquals(link.trim(), link, "link must not lose leading spaces during URL parsing");
                final int fragmentIndex = link.indexOf('#');
                final URI uri = URI.create(fragmentIndex < 0 ? link : link.substring(0, fragmentIndex));
                assertFalse(uri.isAbsolute(), link);
                assertNull(uri.getRawAuthority(), link);
                assertNull(uri.getRawQuery(), link);
                final String target = uri.getPath();
                if (!target.isEmpty() && !"java.lang.Object.html".equals(target)) {
                    assertTrue(expectedFiles.contains(target), link);
                    assertTrue(Files.isRegularFile(outputDir.resolve(target)), link);
                    referencedFiles.add(target);
                }
            }
        }
        assertEquals(expectedFiles, referencedFiles);
    }

    @ParameterizedTest
    @MethodSource("unsafeClassNames")
    void testRejectsUnsafeClassName(final String className, final String codePoint, @TempDir final Path outputDir) throws IOException {
        final JavaClass jc = createClass(className);
        final IOException exception = assertThrows(IOException.class, () -> new Class2HTML(jc, outputDir + File.separator));
        assertEquals("Refusing to write HTML for a class whose name contains the unsafe character (0x" + codePoint + "): " + className, exception.getMessage());
        try (Stream<Path> files = Files.list(outputDir)) {
            assertEquals(0, files.count(), "rejection must not leave partial reports");
        }
    }

    @ParameterizedTest
    @MethodSource("unsafeClassNames")
    void testRejectsUnsafeClassNameFromCli(final String className, final String codePoint, @TempDir final Path directory) throws IOException {
        final Path input = directory.resolve("input.class");
        createClass(className).dump(input.toString());
        final Path outputDir = directory.resolve("reports");
        final IOException exception = assertThrows(IOException.class, () -> Class2HTML.main(new String[] {"-d", outputDir.toString(), input.toString()}));
        assertEquals("Refusing to write HTML for a class whose name contains the unsafe character (0x" + codePoint + "): " + className, exception.getMessage());
        try (Stream<Path> files = Files.list(outputDir)) {
            assertEquals(0, files.count(), "CLI rejection must not leave partial reports");
        }
    }
}
