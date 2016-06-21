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

import java.io.File;
import java.io.FileInputStream;

import org.apache.bcel.classfile.ClassParser;
import org.junit.Assert;

import junit.framework.TestCase;

public class Class2HTMLTestCase extends TestCase {

    public void testConvertJavaUtil() throws Exception {
        final File outputDir = new File("target/test-output/html");
        if (!outputDir.mkdirs()) { // either was not created or already existed
            Assert.assertTrue(outputDir.isDirectory()); // fail if missing
        }

        try (FileInputStream file = new FileInputStream("target/test-classes/Java8Example.class")) {

            final ClassParser parser = new ClassParser(file, "Java8Example.class");

            new Class2HTML(parser.parse(), outputDir.getAbsolutePath() + "/");
        }
    }
}
