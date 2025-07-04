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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ClassPathRepository}, {@link MemorySensitiveClassPathRepository}, and
 * {@link LruCacheClassPathRepository} for their common attributes of caching.
 *
 * <p>
 * Without memory scarcity, these classes behave in the same manner.
 */
class ClassPathRepositoryTest {

    @Test
    void testClassPath() throws IOException {
        try (ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            try (ClassPath repoCp = repository.getClassPath()) {
                assertEquals(classPath, repoCp);
            }
        }
    }

    @Test
    void testClassPathRepository() throws ClassNotFoundException, IOException {
        try (ClassPath classPath = new ClassPath("")) {
            verifyCaching(new ClassPathRepository(classPath));
        }
    }

    @Test
    void testClassWithoutPackage() throws IOException {
        try (ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            assertThrows(ClassNotFoundException.class, () -> repository.loadClass("ClassXYZ"));
        }
    }

    @Test
    void testEmptyInput() throws IOException {
        try (ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            assertThrows(IllegalArgumentException.class, () -> repository.loadClass(""));
        }
    }

    @Test
    void testLruCacheClassPathRepository() throws ClassNotFoundException, IOException {
        try (ClassPath classPath = new ClassPath("")) {
            verifyCaching(new LruCacheClassPathRepository(classPath, 10));
        }
    }

    @Test
    void testMemorySensitiveClassPathRepository() throws ClassNotFoundException, IOException {
        try (ClassPath classPath = new ClassPath("")) {
            verifyCaching(new MemorySensitiveClassPathRepository(classPath));
        }
    }

    @Test
    void testNoClassNotFound() throws IOException {
        try (ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            assertThrows(ClassNotFoundException.class, () -> repository.loadClass("no.such.Class"));
        }
    }

    @Test
    void testNullInput() throws IOException {
        try (ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            assertThrows(IllegalArgumentException.class, () -> repository.loadClass((String) null));
        }
    }

    private void verifyCaching(final AbstractClassPathRepository repository) throws ClassNotFoundException {
        // Tests loadClass()
        final JavaClass class1 = repository.loadClass("java.lang.String");
        assertNotNull(class1);
        final JavaClass class2 = repository.loadClass("java/lang/Long"); // Slashes should work
        assertNotNull(class2);

        // Tests findClass()
        assertEquals(class1, repository.findClass("java.lang.String"));
        assertEquals(class2, repository.findClass("java.lang.Long"));

        // Tests removeClass()
        repository.removeClass(class1);
        assertNull(repository.findClass("java.lang.String"));

        // Tests clear()
        repository.clear();
        assertNull(repository.findClass("java.lang.Long"));
    }
}
