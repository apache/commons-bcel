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

package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ModularRuntimeImage;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test that the generic dump() methods work on the JDK classes Reads each class into an instruction list and then dumps the instructions. The output bytes
 * should be the same as the input.
 * <p>
 * Sets the property {@value JavaHome#EXTRA_JAVA_HOMES} to a {@link File#pathSeparator}-separated list of JRE/JDK paths for additional testing.
 * </p>
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * mvn test -Dtest=JdkGenericDumpTestCase -DExtraJavaHomes="C:\Program Files\Java\openjdk\jdk-13;C:\Program Files\Java\openjdk\jdk-14"
 * mvn test -Dtest=JdkGenericDumpTestCase -DExtraJavaRoot="C:\Program Files\Eclipse Adoptium"
 * </pre>
 * <p>
 * Where "C:\Program Files\Eclipse Adoptium" contains JDK directories, for example:
 * </p>
 * <ul>
 * <li>jdk-11.0.16.101-hotspot</li>
 * <li>jdk-17.0.4.101-hotspot</li>
 * <li>jdk-19.0.0.36-hotspot</li>
 * <li>jdk-8.0.345.1-hotspot</li>
 * </ul>
 */
public class JdkGenericDumpTestCase {

    private static class ClassParserFilesVisitor extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;

        @SuppressWarnings("resource") // FileSystems.getDefault() returns a singleton
        ClassParserFilesVisitor(final String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        private void find(final Path path) throws IOException {
            final Path name = path.getFileName();
            if (name != null && matcher.matches(name)) {
                try (final InputStream inputStream = Files.newInputStream(path)) {
                    final ClassParser classParser = new ClassParser(inputStream, name.toAbsolutePath().toString());
                    assertNotNull(classParser.parse());
                }
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            find(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            find(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path file, final IOException e) {
            System.err.println(e);
            return FileVisitResult.CONTINUE;
        }
    }

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 3];
        int i = 0;
        for (final byte b : bytes) {
            final int v = b & 0xFF;
            hexChars[i++] = hexArray[v >>> 4];
            hexChars[i++] = hexArray[v & 0x0F];
            hexChars[i++] = ' ';
        }
        return new String(hexChars);
    }

    private void compare(final String name, final Method method) {
        // System.out.println("Method: " + m);
        final Code code = method.getCode();
        if (code == null) {
            return; // e.g. abstract method
        }
        final byte[] src = code.getCode();
        final InstructionList instructionList = new InstructionList(src);
        final byte[] out = instructionList.getByteCode();
        if (src.length == out.length) {
            assertArrayEquals(src, out, () -> name + ": " + method.toString());
        } else {
            System.out.println(name + ": " + method.toString() + " " + src.length + " " + out.length);
            System.out.println(bytesToHex(src));
            System.out.println(bytesToHex(out));
            for (final InstructionHandle instructionHandle : instructionList) {
                System.out.println(instructionHandle.toString(false));
            }
            fail("Array comparison failure");
        }
    }

    private void testJar(final Path file) throws Exception {
        System.out.println(file);
        try (JarFile jar = new JarFile(file.toFile())) {
            final Enumeration<JarEntry> en = jar.entries();
            while (en.hasMoreElements()) {
                final JarEntry jarEntry = en.nextElement();
                final String name = jarEntry.getName();
                if (name.endsWith(JavaClass.EXTENSION)) {
                    // System.out.println("- " + name);
                    try (InputStream inputStream = jar.getInputStream(jarEntry)) {
                        final ClassParser classParser = new ClassParser(inputStream, name);
                        final JavaClass javaClass = classParser.parse();
                        for (final Method method : javaClass.getMethods()) {
                            compare(name, method);
                        }
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamJarPath")
    public void testJdkJars(final Path jarPath) throws Exception {
        testJar(jarPath);
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamModulePath")
    @DisabledOnJre(value = JRE.JAVA_8)
    public void testJdkModules(final Path jmodPath) throws Exception {
        testJar(jmodPath);
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamJavaHome")
    public void testJreModules(final JavaHome javaHome) throws Exception {
        assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9));
        try (final ModularRuntimeImage mri = javaHome.getModularRuntimeImage()) {
            for (final Path path : mri.modules()) {
                Files.walkFileTree(path, new ClassParserFilesVisitor("*.class"));
            }
        }
    }

}
