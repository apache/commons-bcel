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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * The repository maintains information about which classes have
 * been loaded.
 *
 * It loads its data from the ClassLoader implementation
 * passed into its constructor.
 *
 * @see org.apache.bcel.Repository
 *
 */
public class ClassLoaderRepository implements Repository {

    private final java.lang.ClassLoader loader;
    private final Map<String, JavaClass> loadedClasses = new HashMap<>(); // CLASSNAME X JAVACLASS


    public ClassLoaderRepository(final java.lang.ClassLoader loader) {
        this.loader = loader;
    }


    /**
     * Store a new JavaClass into this Repository.
     */
    @Override
    public void storeClass( final JavaClass clazz ) {
        loadedClasses.put(clazz.getClassName(), clazz);
        clazz.setRepository(this);
    }


    /**
     * Remove class from repository
     */
    @Override
    public void removeClass( final JavaClass clazz ) {
        loadedClasses.remove(clazz.getClassName());
    }


    /**
     * Find an already defined JavaClass.
     */
    @Override
    public JavaClass findClass( final String className ) {
        return loadedClasses.containsKey(className) ? loadedClasses.get(className) : null;
    }


    /**
     * Lookup a JavaClass object from the Class Name provided.
     */
    @Override
    public JavaClass loadClass(final String className) throws ClassNotFoundException {
        final String classFile = className.replace('.', '/');
        JavaClass RC = findClass(className);
        if (RC != null) {
            return RC;
        }
        try (InputStream is = loader.getResourceAsStream(classFile + ".class")) {
            if (is == null) {
                throw new ClassNotFoundException(className + " not found.");
            }
            final ClassParser parser = new ClassParser(is, className);
            RC = parser.parse();
            storeClass(RC);
            return RC;
        } catch (final IOException e) {
            throw new ClassNotFoundException(className + " not found: " + e, e);
        }
    }


    @Override
    public JavaClass loadClass( final Class<?> clazz ) throws ClassNotFoundException {
        return loadClass(clazz.getName());
    }


    /** Clear all entries from cache.
     */
    @Override
    public void clear() {
        loadedClasses.clear();
    }


    /*
     * @return null
     */
    @Override
    public ClassPath getClassPath() {
        return null;
    }
}
