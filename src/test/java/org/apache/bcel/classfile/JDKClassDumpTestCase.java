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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test that dump() methods work on the JDK classes
 */
public class JDKClassDumpTestCase {

    private void compare(final JavaClass jc, final InputStream inputStream, final String name) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            jc.dump(dos);
        }
        try (DataInputStream src = new DataInputStream(inputStream)) {
            int i = 0;
            for (final int out : baos.toByteArray()) {
                final int in = src.read();
                final int j = i;
                assertEquals(in, out & 0xFF, () -> name + ": Mismatch at " + j);
                i++;
            }
        }
    }

    private void testJar(final JarFile jar) throws Exception {
        System.out.println("parsing " + jar.getName());
        final Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            final JarEntry e = en.nextElement();
            final String name = e.getName();
            if (name.endsWith(".class")) {
                // System.out.println("parsing " + name);
                try (InputStream inputStream1 = jar.getInputStream(e); InputStream inputStream2 = jar.getInputStream(e);) {
                    compare(new ClassParser(inputStream1, name).parse(), inputStream2, name);
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamJarFiles")
    public void testPerformance(final JarFile path) throws Exception {
        assertDoesNotThrow(() -> testJar(path));
    }

}
