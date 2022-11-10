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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link ModularRuntimeImage}.
 */
public class ModularRuntimeImageTestCase {

    @BeforeAll
    public static void before() {
        assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9));
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamModularRuntimeImage")
    public void testListJreModule(final ModularRuntimeImage modularRuntimeImage) throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.MODULES_PATH + "/java.base");
        assertFalse(listEntries.isEmpty());
        assertTrue(listEntries.toString().indexOf("/java.base") > -1);
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamModularRuntimeImage")
    public void testListJreModulePackageDir(final ModularRuntimeImage modularRuntimeImage) throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.MODULES_PATH + "/java.base/java/lang");
        assertFalse(listEntries.isEmpty());
        assertTrue(listEntries.toString().indexOf("/java.base/java/lang/String.class") > -1);
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamModularRuntimeImage")
    public void testListJreModules(final ModularRuntimeImage modularRuntimeImage) throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.MODULES_PATH);
        assertFalse(listEntries.isEmpty());
        assertTrue(listEntries.toString().indexOf("/java.base") > -1);
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamModularRuntimeImage")
    public void testListJrePackages(final ModularRuntimeImage modularRuntimeImage) throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.PACKAGES_PATH);
        assertFalse(listEntries.isEmpty());
        assertTrue(listEntries.toString().indexOf("java.lang") > -1);
    }
}
