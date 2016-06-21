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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test that the generic dump() methods work on the JDK classes
 * Reads each class into an instruction list and then dumps
 * the instructions. The output bytes should be the same as the input.
 */
@RunWith(Parameterized.class)
public class JDKGenericDumpTestCase {

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(findJavaHomes());
    }

    private static Object[][] findJavaHomes() {
        return new Object[][] { { System.getProperty("java.home") } };
    }

    public JDKGenericDumpTestCase(final String javaHome) {
        this.javaHome = javaHome;
    }

    private String javaHome;

    @Test
    public void testJDKjars() throws Exception {
        final File[] jars = listJDKjars();
        for (final File file : jars) {
            testJar(file);
        }
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

    private File[] listJDKjars() throws Exception {
        final File javaLib = new File(javaHome + "/lib");
        return javaLib.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.getName().endsWith(".jar");
            }
        });
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
}
