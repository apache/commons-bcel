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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.bcel.classfile.JavaClass;

/**
 * Maintains a least-recently-used (LRU) cache of {@link JavaClass} with maximum size {@code cacheSize}.
 *
 * <p>
 * This repository supports a class path consisting of too many JAR files to handle in {@link ClassPathRepository} or
 * {@link MemorySensitiveClassPathRepository} without causing {@code OutOfMemoryError}.
 * </p>
 *
 * @since 6.4.0
 */
public class LruCacheClassPathRepository extends AbstractClassPathRepository {

    private final LinkedHashMap<String, JavaClass> loadedClasses;

    public LruCacheClassPathRepository(final ClassPath path, final int cacheSize) {
        super(path);

        if (cacheSize < 1) {
            throw new IllegalArgumentException("cacheSize must be a positive number.");
        }
        final int initialCapacity = (int) (0.75 * cacheSize);
        final boolean accessOrder = true; // Evicts least-recently-accessed
        loadedClasses = new LinkedHashMap<String, JavaClass>(initialCapacity, cacheSize, accessOrder) {

            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, JavaClass> eldest) {
                return size() > cacheSize;
            }
        };
    }

    @Override
    public JavaClass findClass(final String className) {
        return loadedClasses.get(className);
    }

    @Override
    public void storeClass(final JavaClass javaClass) {
        // Not storing parent's _loadedClass
        loadedClasses.put(javaClass.getClassName(), javaClass);
        javaClass.setRepository(this);
    }

    @Override
    public void removeClass(final JavaClass javaClass) {
        loadedClasses.remove(javaClass.getClassName());
    }

    @Override
    public void clear() {
        loadedClasses.clear();
    }
}
