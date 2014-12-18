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

package org.apache.bcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.FilterIterator;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, jvmArgs = "-server")
@Threads(1)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BCELBenchmark {

    private JarFile getJarFile() throws IOException {
        String javaHome = System.getProperty("java.home");
        return new JarFile(javaHome + "/lib/rt.jar");
    }

    private Iterable<JarEntry> getClasses(JarFile jar) {
        return new IteratorIterable<JarEntry>(new FilterIterator<JarEntry>(new EnumerationIterator<JarEntry>(jar.entries()), new Predicate<JarEntry>() {
            public boolean evaluate(JarEntry entry) {
                return entry.getName().endsWith(".class");
            }
        }));
    }

    /**
     * Baseline benchmark. Read the classes but don't parse them.
     */
    @Benchmark
    public void baseline(Blackhole bh) throws IOException {
        JarFile jar = getJarFile();

        for (JarEntry entry : getClasses(jar)) {
            byte[] bytes = IOUtils.toByteArray(jar.getInputStream(entry));
            bh.consume(bytes);
        }

        jar.close();
    }

    @Benchmark
    public void parser(Blackhole bh) throws IOException {
        JarFile jar = getJarFile();

        for (JarEntry entry : getClasses(jar)) {
            byte[] bytes = IOUtils.toByteArray(jar.getInputStream(entry));

            JavaClass clazz = new ClassParser(new ByteArrayInputStream(bytes), entry.getName()).parse();
            bh.consume(clazz);
        }

        jar.close();
    }

    @Benchmark
    public void generator(Blackhole bh) throws IOException {
        JarFile jar = getJarFile();

        for (JarEntry entry : getClasses(jar)) {
            byte[] bytes = IOUtils.toByteArray(jar.getInputStream(entry));

            JavaClass clazz = new ClassParser(new ByteArrayInputStream(bytes), entry.getName()).parse();

            ClassGen cg = new ClassGen(clazz);

            for (Method m : cg.getMethods()) {
                MethodGen mg = new MethodGen(m, cg.getClassName(), cg.getConstantPool());
                InstructionList il = mg.getInstructionList();

                if (il != null) {
                    mg.getInstructionList().setPositions();
                    mg.setMaxLocals();
                    mg.setMaxStack();
                }
                cg.replaceMethod(m, mg.getMethod());
            }

            bh.consume(cg.getJavaClass().getBytes());
        }

        jar.close();
    }
}
