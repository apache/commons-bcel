/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.bcel.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.junit.jupiter.api.Test;

/**
 * Output file paths are built as "dir + className + suffix" from the attacker-controlled this_class constant.
 * Names carrying Windows separators, drive designators or ".." must be rejected, or a crafted class file writes
 * its HTML outside the target directory.
 */
class Class2HTMLTraversalTest {

    private static File outputDir() {
        final File outputDir = new File("target/test-output/html-traversal");
        if (!outputDir.mkdirs()) {
            assertTrue(outputDir.isDirectory());
        }
        return outputDir;
    }

    @Test
    void testRejectsClassNameWithDriveDesignator() {
        final JavaClass jc = new ClassGen("C:pwn", "java.lang.Object", "pwn.java", Const.ACC_PUBLIC, null).getJavaClass();
        assertThrows(IOException.class, () -> new Class2HTML(jc, outputDir().getAbsolutePath() + File.separator));
    }

    @Test
    void testRejectsClassNameWithWindowsTraversal() {
        final JavaClass jc = new ClassGen("..\\..\\pwn", "java.lang.Object", "pwn.java", Const.ACC_PUBLIC, null).getJavaClass();
        assertThrows(IOException.class, () -> new Class2HTML(jc, outputDir().getAbsolutePath() + File.separator));
    }
}
