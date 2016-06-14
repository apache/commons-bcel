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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test that dump() methods work on the JDK classes
 */
public class JDKClassDumpTestCase {

    @Test
    public void testPerformance() throws Exception {
        File javaLib = new File(System.getProperty("java.home") + "/lib");
        javaLib.listFiles(new FileFilter() {

            @Override
            public boolean accept(final File file) {
                if(file.getName().endsWith(".jar")) {
                    try {
                        testJar(file);
                    } catch (Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
                return false;
            }
        });
    }


    private void testJar(final File file) throws Exception {
        System.out.println("parsing " + file);
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> en = jar.entries();

        while (en.hasMoreElements()) {
            JarEntry e = en.nextElement();
            final String name = e.getName();
            if (name.endsWith(".class")) {
//                System.out.println("parsing " + name);
                InputStream in = jar.getInputStream(e);
                ClassParser parser = new ClassParser(in, name);
                JavaClass jc = parser.parse();
                compare(jc, jar.getInputStream(e), name);
            }
        }
        jar.close();
    }

    private void compare(final JavaClass jc, final InputStream inputStream, final String name) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            jc.dump(dos);
        }
        try (DataInputStream src = new DataInputStream(inputStream)) {
            int i = 0;
            for (int out : baos.toByteArray()) {
                int in = src.read();
                assertEquals(name + ": Mismatch at " + i, in, out & 0xFF);
                i++;
            }
        }
    }


}
