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
 * 
 */

package org.apache.bcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.TestCase;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

public final class PerformanceTest extends TestCase {

    private static byte[] read(final InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("Class not found");
        }
        byte[] b = new byte[is.available()];
        int len = 0;
        while (true) {
            int n = is.read(b, len, b.length - len);
            if (n == -1) {
                if (len < b.length) {
                    byte[] c = new byte[len];
                    System.arraycopy(b, 0, c, 0, len);
                    b = c;
                }
                return b;
            }
            len += n;
            if (len == b.length) {
                byte[] c = new byte[b.length + 1000];
                System.arraycopy(b, 0, c, 0, len);
                b = c;
            }
        }
    }

    private static void test(int fraction) throws IOException {
        NanoTimer total = new NanoTimer();
        NanoTimer parseTime = new NanoTimer();
        NanoTimer cgenTime = new NanoTimer();
        NanoTimer mgenTime = new NanoTimer();
        NanoTimer mserTime = new NanoTimer();
        NanoTimer serTime = new NanoTimer();

        total.start();

        String javaHome = System.getProperty("java.home");

        JarFile jar = new JarFile(javaHome + "/lib/rt.jar");
        Enumeration<?> en = jar.entries();
        int i = 0;

        while (en.hasMoreElements()) {
            JarEntry e = (JarEntry) en.nextElement();
            if (e.getName().endsWith(".class") && i++ % fraction == 0) {
                InputStream in = jar.getInputStream(e);
                byte[] bytes = read(in);

                parseTime.start();
                JavaClass clazz = new ClassParser(new ByteArrayInputStream(bytes), e.getName())
                        .parse();
                parseTime.stop();

                cgenTime.start();
                ClassGen cg = new ClassGen(clazz);
                cgenTime.stop();

                Method[] methods = cg.getMethods();
                for (Method m : methods) {
                    mgenTime.start();
                    MethodGen mg = new MethodGen(m, cg.getClassName(), cg.getConstantPool());
                    InstructionList il = mg.getInstructionList();
                    mgenTime.stop();

                    mserTime.start();
                    if (il != null) {
                        mg.getInstructionList().setPositions();
                        mg.setMaxLocals();
                        mg.setMaxStack();
                    }
                    cg.replaceMethod(m, mg.getMethod());
                    mserTime.stop();
                }

                serTime.start();
                cg.getJavaClass().getBytes();
                serTime.stop();
            }
        }

        jar.close();
        total.stop();
        System.out.println("ClassParser.parse: " + parseTime);
        System.out.println("ClassGen.init: " + cgenTime);
        System.out.println("MethodGen.init: " + mgenTime);
        System.out.println("MethodGen.getMethod: " + mserTime);
        System.out.println("ClassGen.getJavaClass.getBytes: " + serTime);
        System.out.println("Total: " + total);
        System.out.println();
    }

    public void testPerformance() throws IOException {
        test(1);
        test(1);
        test(1);        
    }

}
