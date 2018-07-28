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

package org.apache.bcel.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.bcel.generic.JdkGenericDumpTestCase;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests {@link ModularRuntimeImage}.
 */
@RunWith(Parameterized.class)
public class ModularRuntimeImageTestCase {

    @Parameters(name = "{0}")
    public static Collection<String> data() {
        return JdkGenericDumpTestCase.data();
    }

    private final String javaHome;
    private final ModularRuntimeImage modularRuntimeImage;

    public ModularRuntimeImageTestCase(final String javaHome) throws IOException {
        this.javaHome = javaHome;
        Assume.assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9));
        this.modularRuntimeImage = new ModularRuntimeImage(javaHome);
    }

    @Test
    public void testListJreModules() throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.MODULES_PATH);
        Assert.assertFalse(listEntries.isEmpty());
        Assert.assertTrue(listEntries.toString().indexOf("/java.base") > -1);
    }

    @Test
    public void testListJreModule() throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.MODULES_PATH + "/java.base");
        Assert.assertFalse(listEntries.isEmpty());
        Assert.assertTrue(listEntries.toString().indexOf("/java.base") > -1);
    }

    @Test
    public void testListJreModulePackageDir() throws IOException {
        final List<Path> listEntries = modularRuntimeImage
                .list(ModularRuntimeImage.MODULES_PATH + "/java.base/java/lang");
        Assert.assertFalse(listEntries.isEmpty());
        Assert.assertTrue(listEntries.toString().indexOf("/java.base/java/lang/String.class") > -1);
    }

    @Test
    public void testListJrePackages() throws IOException {
        final List<Path> listEntries = modularRuntimeImage.list(ModularRuntimeImage.PACKAGES_PATH);
        Assert.assertFalse(listEntries.isEmpty());
        Assert.assertTrue(listEntries.toString().indexOf("java.lang") > -1);
    }
}
