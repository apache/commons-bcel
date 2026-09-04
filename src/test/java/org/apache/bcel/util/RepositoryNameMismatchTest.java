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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.junit.jupiter.api.Test;

/**
 * A class file served under one name whose this_class declares another name must not be returned or cached:
 * repositories key their caches on the parsed name, so accepting the mismatch lets attacker bytes squat on
 * an arbitrary class name JVM-wide (including the static org.apache.bcel.Repository facade and the
 * VerifierFactory verdict cache).
 */
class RepositoryNameMismatchTest {

    private static byte[] classBytes(final String className) {
        return new ClassGen(className, "java.lang.Object", className + ".java", Const.ACC_PUBLIC, null).getJavaClass().getBytes();
    }

    @Test
    void testClassLoaderRepositoryRejectsMismatchedName() {
        final byte[] bytes = classBytes("evil.Squatter");
        final ClassLoader loader = new ClassLoader(getClass().getClassLoader()) {
            @Override
            public InputStream getResourceAsStream(final String name) {
                return "victim/Innocent.class".equals(name) ? new ByteArrayInputStream(bytes) : super.getResourceAsStream(name);
            }
        };
        final ClassLoaderRepository repository = new ClassLoaderRepository(loader);
        assertThrows(ClassNotFoundException.class, () -> repository.loadClass("victim.Innocent"));
        assertNull(repository.findClass("evil.Squatter"), "mismatched class must not be cached under its declared name");
        assertNull(repository.findClass("victim.Innocent"), "mismatched class must not be cached under the requested name");
    }
}
