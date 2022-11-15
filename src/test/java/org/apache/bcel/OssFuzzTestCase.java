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
 */
package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;

public class OssFuzzTestCase {

    @Test
    public void testIssue51980() throws Exception {
        testOssFuzzReproducer("51980");
    }

    @Test
    public void testIssue51989() throws Exception {
        testOssFuzzReproducer("51989");
    }

    @Test
    public void testIssue52168() throws Exception {
        testOssFuzzReproducer("52168");
    }

    private void testOssFuzzReproducer(final String issue) throws Exception {
        final File reproducerFile = new File("target/test-classes/ossfuzz/issue" + issue + "/Test.class");
        try (final FileInputStream reproducerInputStream = new FileInputStream(reproducerFile)) {
            final ClassParser cp = new ClassParser(reproducerInputStream, "Test");
            assertThrows(ClassFormatException.class, () -> cp.parse());
        }
    }
}
