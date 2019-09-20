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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ModularRuntimeImage;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sun.jna.platform.win32.Advapi32Util;

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
@RunWith(Parameterized.class)
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
                    final ClassParser parser = new ClassParser(inputStream, name.toAbsolutePath().toString());
                    final JavaClass jc = parser.parse();
                    Assert.assertNotNull(jc);
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

    private static void addAllJavaHomesOnWindows(final String keyJre, final Set<String> javaHomes) {
        if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJre)) {
            javaHomes.addAll(findJavaHomesOnWindows(keyJre, Advapi32Util.registryGetKeys(HKEY_LOCAL_MACHINE, keyJre)));
        }
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

    @Parameters(name = "{0}")
    public static Collection<String> data() {
        return findJavaHomes();
    }

    private static Set<String> findJavaHomes() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return findJavaHomesOnWindows();
        }
        final Set<String> javaHomes = new HashSet<>(1);
        javaHomes.add(SystemUtils.JAVA_HOME);
        return javaHomes;
    }

    private static Set<String> findJavaHomesOnWindows() {
        final Set<String> javaHomes = new HashSet<>();
        addAllJavaHomesOnWindows(KEY_JRE, javaHomes);
        addAllJavaHomesOnWindows(KEY_JRE_9, javaHomes);
        addAllJavaHomesOnWindows(KEY_JDK, javaHomes);
        addAllJavaHomesOnWindows(KEY_JDK_9, javaHomes);
        addAllJavaHomes(EXTRA_JAVA_HOMES, javaHomes);
        return javaHomes;
    }

    private static void addAllJavaHomes(final String extraJavaHomesProp, final Set<String> javaHomes) {
        final String path = System.getProperty(extraJavaHomesProp);
        if (StringUtils.isEmpty(path)) {
            return;
        }
        final String[] paths = path.split(File.pathSeparator);
        javaHomes.addAll(Arrays.asList(paths));

    }

    private static Set<String> findJavaHomesOnWindows(final String keyJavaHome, final String[] keys) {
        final Set<String> javaHomes = new HashSet<>(keys.length);
        for (final String key : keys) {
            if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJavaHome + "\\" + key)) {
                final String javaHome = Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE,
                    keyJavaHome + "\\" + key, "JavaHome");
                if (StringUtils.isNoneBlank(javaHome)) {
                    if (new File(javaHome).exists()) {
                        javaHomes.add(javaHome);
                    }
                }
            }
        }
        return javaHomes;
    }

    private final String javaHome;

    public JdkGenericDumpTestCase(final String javaHome) {
        this.javaHome = javaHome;
    }

    private void compare(final String name, final Method m) {
        // System.out.println("Method: " + m);
        final Code c = m.getCode();
        if (c == null) {
            return; // e.g. abstract method
        }
        final byte[] src = c.getCode();
        final InstructionList il = new InstructionList(src);
        final byte[] out = il.getByteCode();
        if (src.length == out.length) {
            assertArrayEquals(name + ": " + m.toString(), src, out);
        } else {
            System.out.println(name + ": " + m.toString() + " " + src.length + " " + out.length);
            System.out.println(bytesToHex(src));
            System.out.println(bytesToHex(out));
            for (final InstructionHandle ih : il) {
                System.out.println(ih.toString(false));
            }
            fail("Array comparison failure");
        }
    }

    private File[] listJdkJars() throws Exception {
        final File javaLib = new File(javaHome, "lib");
        return javaLib.listFiles((FileFilter) file -> file.getName().endsWith(".jar"));
    }

    private File[] listJdkModules() throws Exception {
        final File javaLib = new File(javaHome, "jmods");
        return javaLib.listFiles((FileFilter) file -> file.getName().endsWith(".jmod"));
    }

    private void testJar(final File file) throws Exception {
        System.out.println(file);
        try (JarFile jar = new JarFile(file)) {
            final Enumeration<JarEntry> en = jar.entries();
            while (en.hasMoreElements()) {
                final JarEntry e = en.nextElement();
                final String name = e.getName();
                if (name.endsWith(".class")) {
                    // System.out.println("- " + name);
                    try (InputStream in = jar.getInputStream(e)) {
                        final ClassParser parser = new ClassParser(in, name);
                        final JavaClass jc = parser.parse();
                        for (final Method m : jc.getMethods()) {
                            compare(name, m);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testJdkJars() throws Exception {
        final File[] jars = listJdkJars();
        if (jars != null) {
            for (final File file : jars) {
                testJar(file);
            }
        }
    }

    @Test
    public void testJdkModules() throws Exception {
        final File[] jmods = listJdkModules();
        if (jmods != null) {
            for (final File file : jmods) {
                testJar(file);
            }
        }
    }

    @Test
    public void testJreModules() throws Exception {
        Assume.assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9));
        try (final ModularRuntimeImage mri = new ModularRuntimeImage(javaHome)) {
            final List<Path> modules = mri.modules();
            Assert.assertFalse(modules.isEmpty());
            for (final Path path : modules) {
                Files.walkFileTree(path, new ClassParserFilesVisitor("*.class"));
            }
        }
    }

}
