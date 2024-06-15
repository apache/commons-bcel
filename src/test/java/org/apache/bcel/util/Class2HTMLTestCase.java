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

package org.apache.bcel.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class Class2HTMLTestCase {

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "target/test-classes/Java8Example.class",
        "target/test-classes/Java4Example.class"})
    // @formatter:on
    public void testConvertJavaUtil(final String classFileName) throws Exception {
        final File outputDir = new File("target/test-output/html");
        if (!outputDir.mkdirs()) { // either was not created or already existed
            assertTrue(outputDir.isDirectory()); // fail if missing
        }

        try (FileInputStream file = new FileInputStream(classFileName)) {

            final ClassParser parser = new ClassParser(file, new File(classFileName).getName());

            new Class2HTML(parser.parse(), outputDir.getAbsolutePath() + "/");
            // TODO assertions on generated HTML code
        }
    }

    /**
     * Tests that we do not break binary compatibility with BCEL-330.
     */
    @Test
    public void testReferenceToConstant() {
        @SuppressWarnings("unused")
        final short referenceToConstant = Constants.AALOAD;
    }

}
