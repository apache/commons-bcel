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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests BCEL-368.
 */
public class TestJira368 {

    private JavaClass parseJavaClass() throws IOException {
        return new ClassParser("src/test/resources/jira368/Test.class").parse();
    }

    @Test
    public void testMethodCodeStringBrief() throws Exception {
        for (Method method : parseJavaClass().getMethods()) {
            final String string = method.getCode().toString(false);
            // System.out.println(string);
            assertNotNull(string);
        }
    }

    @Test
    public void testMethodCodeStringVerbose() throws Exception {
        for (Method method : parseJavaClass().getMethods()) {
            final String string = method.getCode().toString(true);
            // System.out.println(string);
            assertNotNull(string);
        }
    }

    @Test
    public void testMethodSignature() throws Exception {
        final String string = parseJavaClass().toString();
        // System.out.println(string);
        assertNotNull(string);
    }

}
