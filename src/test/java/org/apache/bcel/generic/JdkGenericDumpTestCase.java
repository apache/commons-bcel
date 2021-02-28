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

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ModularRuntimeImage;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.sun.jna.platform.win32.Advapi32Util;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test that the generic dump() methods work on the JDK classes Reads each class into an instruction list and then dumps
 * the instructions. The output bytes should be the same as the input.
 * <p>
 * Set the property {@value #EXTRA_JAVA_HOMES} to a {@link File#pathSeparator}-separated list of JRE/JDK paths for
 * additional testing.
 * </p>
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * mvn test -Dtest=JdkGenericDumpTestCase -DExtraJavaHomes="C:\Program Files\Java\openjdk\jdk-13;C:\Program Files\Java\openjdk\jdk-14"
 * </pre>
 */
public class JdkGenericDumpTestCase {

    private static final String EXTRA_JAVA_HOMES = "ExtraJavaHomes";

    private static class ClassParserFilesVisitor extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;

        ClassParserFilesVisitor(final String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        private void find(final Path path) throws IOException {
            final Path name = path.getFileName();
            if (name != null && matcher.matches(name)) {
                try (final InputStream inputStream = Files.newInputStream(path)) {
                    final ClassParser classParser = new ClassParser(inputStream, name.toAbsolutePath().toString());
                    final JavaClass javaClass = classParser.parse();
                    assertNotNull(javaClass);
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

    private static final String KEY_JDK = "SOFTWARE\\JavaSoft\\Java Development Kit";

    private static final String KEY_JDK_9 = "SOFTWARE\\JavaSoft\\JDK";

    private static final String KEY_JRE = "SOFTWARE\\JavaSoft\\Java Runtime Environment";

    private static final String KEY_JRE_9 = "SOFTWARE\\JavaSoft\\JRE";

    private static Stream<String> getAllJavaHomesOnWindows(final String keyJre) {
        if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJre)) {
            return findJavaHomesOnWindows(keyJre, Advapi32Util.registryGetKeys(HKEY_LOCAL_MACHINE, keyJre));
        }
        return Stream.empty();
    }

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

    public static Stream<String> findJavaHomes() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return findJavaHomesOnWindows();
        }
        return Stream.of(SystemUtils.JAVA_HOME);
    }

    private static Stream<String> findJavaHomesOnWindows() {
        return Stream.concat(
                Stream.of(KEY_JRE, KEY_JRE_9, KEY_JDK, KEY_JDK_9)
                        .flatMap(JdkGenericDumpTestCase::getAllJavaHomesOnWindows),
                getAllJavaHomesFromKey(EXTRA_JAVA_HOMES)
        ).distinct();
    }

    private static Stream<String> getAllJavaHomesFromKey(final String extraJavaHomesKey) {
        return Stream.concat(
                getAllJavaHomesFromPath(System.getProperty(extraJavaHomesKey)),
                getAllJavaHomesFromPath(System.getenv(extraJavaHomesKey)));
    }

    private static Stream<String> getAllJavaHomesFromPath(final String path) {
        if (StringUtils.isEmpty(path)) {
            return Stream.empty();
        }
        return Arrays.stream(path.split(File.pathSeparator));
    }

    private static Stream<String> findJavaHomesOnWindows(final String keyJavaHome, final String[] keys) {
        final Set<String> javaHomes = new HashSet<>(keys.length);
        for (final String key : keys) {
            if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJavaHome + "\\" + key)) {
                final String javaHome = Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE,
                    keyJavaHome + "\\" + key, "JavaHome");
                if (StringUtils.isNoneBlank(javaHome) && new File(javaHome).exists()) {
                    javaHomes.add(javaHome);
                }
            }
        }
        return javaHomes.stream();
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

    private File[] listJdkJars(final String javaHome) throws Exception {
        final File javaLib = new File(javaHome, "lib");
        return javaLib.listFiles(file -> file.getName().endsWith(".jar"));
    }

    private File[] listJdkModules(final String javaHome) throws Exception {
        final File javaLib = new File(javaHome, "jmods");
        return javaLib.listFiles(file -> file.getName().endsWith(".jmod"));
    }

    private void testJar(final File file) throws Exception {
        System.out.println(file);
        try (JarFile jar = new JarFile(file)) {
            final Enumeration<JarEntry> en = jar.entries();
            while (en.hasMoreElements()) {
                final JarEntry jarEntry = en.nextElement();
                final String name = jarEntry.getName();
                if (name.endsWith(".class")) {
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
    @MethodSource("findJavaHomes")
    public void testJdkJars(final String javaHome) throws Exception {
        final File[] jars = listJdkJars(javaHome);
        if (jars != null) {
            for (final File file : jars) {
                testJar(file);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("findJavaHomes")
    public void testJdkModules(final String javaHome) throws Exception {
        final File[] jmods = listJdkModules(javaHome);
        if (jmods != null) {
            for (final File file : jmods) {
                testJar(file);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("findJavaHomes")
    public void testJreModules(final String javaHome) throws Exception {
        assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9));
        try (final ModularRuntimeImage mri = new ModularRuntimeImage(javaHome)) {
            final List<Path> modules = mri.modules();
            assertFalse(modules.isEmpty());
            for (final Path path : modules) {
                Files.walkFileTree(path, new ClassParserFilesVisitor("*.class"));
            }
        }
    }

}
