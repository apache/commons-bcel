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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.JavaClass;

/**
 * This repository is used in situations where a Class is created outside the realm of a ClassLoader. Classes are loaded from the file systems using the paths
 * specified in the given class path. By default, this is the value returned by ClassPath.getClassPath(). This repository holds onto classes with
 * SoftReferences, and will reload as needed, in cases where memory sizes are important.
 *
 * @see org.apache.bcel.Repository
 */
public class MemorySensitiveClassPathRepository extends AbstractClassPathRepository {

    private final Map<String, SoftReference<JavaClass>> loadedClasses = new HashMap<>(); // CLASSNAME X JAVACLASS

    public MemorySensitiveClassPathRepository(final ClassPath path) {
        super(path);
    }

    /**
     * Store a new JavaClass instance into this Repository.
     */
    @Override
    public void storeClass(final JavaClass clazz) {
        // Not calling super.storeClass because this subclass maintains the mapping.
        loadedClasses.put(clazz.getClassName(), new SoftReference<>(clazz));
        clazz.setRepository(this);
    }

    /**
     * Remove class from repository
     */
    @Override
    public void removeClass(final JavaClass clazz) {
        loadedClasses.remove(clazz.getClassName());
    }

    /**
     * Find an already defined (cached) JavaClass object by name.
     */
    @Override
    public JavaClass findClass(final String className) {
        final SoftReference<JavaClass> ref = loadedClasses.get(className);
        if (ref == null) {
            return null;
        }
        return ref.get();
    }

    /**
     * Clear all entries from cache.
     */
    @Override
    public void clear() {
        loadedClasses.clear();
    }
}
