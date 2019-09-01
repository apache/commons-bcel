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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link LruCacheClassPathRepository}.
 */
public class LruCacheClassPathRepositoryTestCase {

    @Test
    public void testCacheEviction() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final LruCacheClassPathRepository repository = new LruCacheClassPathRepository(classPath, 2);
            final JavaClass class1 = repository.loadClass("java.lang.String");
            Assert.assertNotNull(class1);
            final JavaClass class2 = repository.loadClass("java.lang.Long");
            Assert.assertNotNull(class2);
            final JavaClass class3 = repository.loadClass("java.lang.Integer"); // Evicts class1
            Assert.assertNotNull(class3);

            assertNull(repository.findClass("java.lang.String"));
            final JavaClass cachedClass2 = repository.findClass("java.lang.Long");
            assertEquals(class2, cachedClass2);
        }
    }

    @Test
    public void testLeastRecentlyUsedEviction() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            final LruCacheClassPathRepository repository = new LruCacheClassPathRepository(classPath, 2);
            final JavaClass class1 = repository.loadClass("java.lang.String");
            Assert.assertNotNull(class1);
            final JavaClass class2 = repository.loadClass("java.lang.Long");
            Assert.assertNotNull(class2);
            repository.findClass("java.lang.String"); // Uses class1
            final JavaClass class3 = repository.loadClass("java.lang.Integer"); // Evicts class2
            Assert.assertNotNull(class3);

            assertNull(repository.findClass("java.lang.Long"));
            final JavaClass cachedClass1 = repository.findClass("java.lang.String");
            assertEquals(class1, cachedClass1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroCacheSize() throws IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            new LruCacheClassPathRepository(classPath, 0);
        }
    }
}
