/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.bcel.util;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.bcel.classfile.JavaClass;
import org.junit.Test;


public class BCELifierTestCase {

    @Test
    public void test() throws Exception {
        final OutputStream os = new ByteArrayOutputStream();
        final JavaClass java_class = BCELifier.getJavaClass("Java8Example");
        assertNotNull(java_class);
        final BCELifier bcelifier = new BCELifier(java_class, os);
        bcelifier.start();
    }

    /*
     * Dump a class using "javap" and compare with the same class recreated
     * using BCELifier, "javac", "java" and dumped with "javap"
     * TODO: detect if JDK present and skip test if not
     */
    @Test
    public void testJavapCompare() throws Exception {
        testClassOnPath("target/test-classes/Java8Example.class");
    }

    private void testClassOnPath(final String javaClass) throws Exception {
        // Get javap of the input class
        final String initial = exec(null, "javap", "-p", "-c", javaClass);

        final File workDir = new File("target");
        final File infile = new File(javaClass);
        final JavaClass java_class = BCELifier.getJavaClass(infile.getName().replace(".class", ""));
        assertNotNull(java_class);
        final File outfile = new File(workDir, infile.getName().replace(".class", "Creator.java"));
        try (FileOutputStream fos = new FileOutputStream(outfile)) {
            final BCELifier bcelifier = new BCELifier(java_class, fos);
            bcelifier.start();
        }
        exec(workDir, "javac", "-cp", "classes", outfile.getName());
        exec(workDir, "java", "-cp", "." + File.pathSeparator + "classes", outfile.getName().replace(".java", ""));
        final String output = exec(workDir, "javap", "-p", "-c", infile.getName());
        assertEquals(canonHashRef(initial), canonHashRef(output));
    }

    // Canonicalise the javap output so it compares better
    private String canonHashRef(String input) {
        input = input.replaceAll("#\\d+", "#n"); // numbers may vary in length
        input = input.replaceAll(" +", " "); // collapse spaces
        input = input.replaceAll("//.+",""); // comments may vary
        return input;
    }

    private String exec(final File workDir, final String... args) throws Exception {
        // System.err.println(java.util.Arrays.toString(args));
        final ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(workDir);
        final Process proc = pb.start();
        try (BufferedInputStream is = new BufferedInputStream(proc.getInputStream());
                InputStream es = proc.getErrorStream()) {
            proc.waitFor();
            final byte[] buff = new byte[2048];
            int len;
            while ((len = es.read(buff)) != -1) {
                System.err.print(new String(buff, 0, len));
            }

            final StringBuilder sb = new StringBuilder();
            while ((len = is.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
            return sb.toString();
        }
    }

}
