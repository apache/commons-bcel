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

import java.util.HashMap;
import java.util.Map;

/**
 * This repository is used in situations where a Class is created outside the realm of a ClassLoader. Classes are loaded from the file systems using the paths
 * specified in the given class path. By default, this is the value returned by ClassPath.getClassPath().
 * <p>
 * This repository uses a factory design, allowing it to maintain a collection of different classpaths, and as such It is designed to be used as a singleton per
 * classpath.
 * </p>
 *
 * @see org.apache.bcel.Repository
 *
 */
public class SyntheticRepository extends MemorySensitiveClassPathRepository {

    // private static final String DEFAULT_PATH = ClassPath.getClassPath();
    private static final Map<ClassPath, SyntheticRepository> instances = new HashMap<>(); // CLASSPATH X REPOSITORY

    private SyntheticRepository(final ClassPath path) {
        super(path);
    }

    public static SyntheticRepository getInstance() {
        return getInstance(ClassPath.SYSTEM_CLASS_PATH);
    }

    public static SyntheticRepository getInstance(final ClassPath classPath) {
        SyntheticRepository rep = instances.get(classPath);
        if (rep == null) {
            rep = new SyntheticRepository(classPath);
            instances.put(classPath, rep);
        }
        return rep;
    }
}
