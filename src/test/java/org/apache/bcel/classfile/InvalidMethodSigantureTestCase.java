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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * javap input:
 *
 * <pre>
 * javap "src/test/resources/kotlin/test$method name with () in it$1.class"
 * </pre>
 *
 * javap output:
 *
 * <pre>
 *
Compiled from "test.kt"
final class test$method name with () in it$1 extends kotlin.jvm.internal.Lambda implements kotlin.jvm.functions.Function0<kotlin.Unit> {
  public static final test$method name with () in it$1 INSTANCE;
  test$method name with () in it$1();
  public final void invoke();
  public java.lang.Object invoke();
  static {};
 * </pre>
 */
public class InvalidMethodSigantureTestCase {

    final class TestVisitor extends org.apache.bcel.classfile.EmptyVisitor {
        @Override
        public void visitField(final Field field) {
            field.getType();
        }
    }
    private static final String CLASS_NAME = "test$method name with () in it$1";

    private static final String SRC_TEST_RESOURCES_KOTLIN = "src/test/resources/kotlin/";

    @Test
    public void testLoadClass() throws Exception {
        final Path path = Paths.get(SRC_TEST_RESOURCES_KOTLIN);
        assertTrue(Files.exists(path));
        assertTrue(Files.isDirectory(path));
        try (URLClassLoader cl = URLClassLoader.newInstance(new URL[] { path.toUri().toURL() })) {
            Class.forName(CLASS_NAME, false, cl);
        }
    }

    @Test
    //@Disabled("TODO?")
    public void testMethodWithParens() throws Exception {
        try (final InputStream inputStream = Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_KOTLIN, CLASS_NAME + ".class"))) {
            final ClassParser classParser = new ClassParser(inputStream, CLASS_NAME);
            final JavaClass javaClass = classParser.parse();
            final TestVisitor visitor = new TestVisitor();
            new DescendingVisitor(javaClass, visitor).visit();
        }
    }
}
