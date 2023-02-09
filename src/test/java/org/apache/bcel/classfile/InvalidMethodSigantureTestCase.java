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

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InvalidMethodSigantureTestCase {

    class TestVisitor extends org.apache.bcel.classfile.EmptyVisitor {
        @Override
        public void visitField(final Field field) {
            field.getType();
        }
    }

    @Test
    public void testMethodWithParens() throws Exception {
        try (final InputStream inputStream = Files.newInputStream(Paths.get("src/test/resources/invalidmethodsig/test$method name with () in it$1.class"))) {
            final ClassParser classParser = new ClassParser(inputStream, "test.class");
            final JavaClass javaClass = classParser.parse();
            final TestVisitor visitor = new TestVisitor();
            new org.apache.bcel.classfile.DescendingVisitor(javaClass, visitor).visit();
        }
    }
}
