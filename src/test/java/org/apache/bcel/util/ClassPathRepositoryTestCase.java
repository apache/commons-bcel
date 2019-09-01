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
 *
 */
package org.apache.bcel.util;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link ClassPathRepository}, {@link MemorySensitiveClassPathRepository}, and {@link
 * LruCacheClassPathRepository} for their common attributes of caching.
 *
 * <p>Without memory scarcity, these classes behave in the same manner.
 */
public class ClassPathRepositoryTestCase {

    private void verifyCaching(final AbstractClassPathRepository repository) throws ClassNotFoundException {
        // Tests loadClass()
        final JavaClass class1 = repository.loadClass("java.lang.String");
        Assert.assertNotNull(class1);
        final JavaClass class2 = repository.loadClass("java/lang/Long"); // Slashes should work
        Assert.assertNotNull(class2);

        // Tests findClass()
        Assert.assertEquals(class1, repository.findClass("java.lang.String"));
        Assert.assertEquals(class2, repository.findClass("java.lang.Long"));

        // Tests removeClass()
        repository.removeClass(class1);
        Assert.assertNull(repository.findClass("java.lang.String"));

        // Tests clear()
        repository.clear();
        Assert.assertNull(repository.findClass("java.lang.Long"));
    }

    @Test
    public void testClassPathRepository() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            verifyCaching(new ClassPathRepository(classPath));
        }
    }

    @Test
    public void testMemorySensitiveClassPathRepository() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            verifyCaching(new MemorySensitiveClassPathRepository(classPath));
        }
    }

    @Test
    public void testLruCacheClassPathRepository() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            verifyCaching(new LruCacheClassPathRepository(classPath, 10));
        }
    }

    @Test
    public void testClassPath() throws IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            Assert.assertEquals(classPath, repository.getClassPath());
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testNoClassNotFound() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            repository.loadClass("no.such.Class");
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassWithoutPackage() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            repository.loadClass("ClassXYZ");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            repository.loadClass("");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final ClassPathRepository repository = new ClassPathRepository(classPath);
            repository.loadClass((String) null);
        }
    }
}
