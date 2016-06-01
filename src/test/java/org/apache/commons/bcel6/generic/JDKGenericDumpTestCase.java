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

package org.apache.commons.bcel6.generic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.bcel6.classfile.ClassParser;
import org.apache.commons.bcel6.classfile.Code;
import org.apache.commons.bcel6.classfile.JavaClass;
import org.apache.commons.bcel6.classfile.Method;
import org.junit.Test;

/**
 * Test that the generic dump() methods work on the JDK classes
 * Reads each class into an instruction list and then dumps
 * the instructions. The output bytes should be the same as the input.
 */
public class JDKGenericDumpTestCase {

    @Test
    public void testJDKjars() throws Exception {
        File[] jars = listJDKjars();
        for(File file : jars) {
            testJar(file);
        }
    }

    private void testJar(final File file) throws Exception {
        System.out.println(file);
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> en = jar.entries();

        while (en.hasMoreElements()) {
            JarEntry e = en.nextElement();
            final String name = e.getName();
            if (name.endsWith(".class")) {
//                System.out.println("- " + name);
                InputStream in = jar.getInputStream(e);
                ClassParser parser = new ClassParser(in, name);
                JavaClass jc = parser.parse();
                for(Method m : jc.getMethods()) {
                    compare(name, m);
                }
            }
        }
        jar.close();
    }

    private void compare(final String name, final Method m) {
//        System.out.println("Method: " + m);
        Code c = m.getCode();
        if (c==null) {
            return; // e.g. abstract method
        }
        byte[] src = c.getCode();
        InstructionList il = new InstructionList(src);
        byte[] out = il.getByteCode();
        if (src.length == out.length) {
            assertArrayEquals(name + ": "+m.toString(), src, out);
        } else {
            System.out.println(name + ": "+m.toString() +" "+ src.length+" "+out.length);
            System.out.println(bytesToHex(src));
            System.out.println(bytesToHex(out));
            for(InstructionHandle ih : il) {
                System.out.println(ih.toString(false));
            }
            fail("Array comparison failure");
        }
    }

    private File[] listJDKjars() throws Exception {
        File javaLib = new File(System.getProperty("java.home") + "/lib");
        return javaLib.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.getName().endsWith(".jar");
            }
        });
    }

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(final byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        int i=0;
        for (byte b : bytes) {
            int v = b & 0xFF;
            hexChars[i++] = hexArray[v >>> 4];
            hexChars[i++] = hexArray[v & 0x0F];
            hexChars[i++] = ' ';
        }
        return new String(hexChars);
    }
}
