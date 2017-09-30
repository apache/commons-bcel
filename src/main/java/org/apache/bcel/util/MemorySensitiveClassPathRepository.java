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
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * This repository is used in situations where a Class is created outside the realm of a ClassLoader. Classes are loaded from the file systems using the paths
 * specified in the given class path. By default, this is the value returned by ClassPath.getClassPath(). This repository holds onto classes with
 * SoftReferences, and will reload as needed, in cases where memory sizes are important.<br>
 *
 * @see org.apache.bcel.Repository
 */
public class MemorySensitiveClassPathRepository implements Repository {

    private ClassPath _path = null;
    private final Map<String, SoftReference<JavaClass>> _loadedClasses = new HashMap<>(); // CLASSNAME X JAVACLASS

    public MemorySensitiveClassPathRepository(final ClassPath path) {
        this._path = path;
    }

    /**
     * Store a new JavaClass instance into this Repository.
     */
    @Override
    public void storeClass(final JavaClass clazz) {
        _loadedClasses.put(clazz.getClassName(), new SoftReference<>(clazz));
        clazz.setRepository(this);
    }

    /**
     * Remove class from repository
     */
    @Override
    public void removeClass(final JavaClass clazz) {
        _loadedClasses.remove(clazz.getClassName());
    }

    /**
     * Find an already defined (cached) JavaClass object by name.
     */
    @Override
    public JavaClass findClass(final String className) {
        final SoftReference<JavaClass> ref = _loadedClasses.get(className);
        if (ref == null) {
            return null;
        }
        return ref.get();
    }

    /**
     * Find a JavaClass object by name. If it is already in this Repository, the Repository version is returned. Otherwise, the Repository's classpath is
     * searched for the class (and it is added to the Repository if found).
     *
     * @param className
     *            the name of the class
     * @return the JavaClass object
     * @throws ClassNotFoundException
     *             if the class is not in the Repository, and could not be found on the classpath
     */
    @Override
    public JavaClass loadClass(String className) throws ClassNotFoundException {
        if ((className == null) || className.isEmpty()) {
            throw new IllegalArgumentException("Invalid class name " + className);
        }
        className = className.replace('/', '.'); // Just in case, canonical form
        final JavaClass clazz = findClass(className);
        if (clazz != null) {
            return clazz;
        }
        try {
            return loadClass(_path.getInputStream(className), className);
        } catch (final IOException e) {
            throw new ClassNotFoundException("Exception while looking for class " + className + ": " + e, e);
        }
    }

    /**
     * Find the JavaClass object for a runtime Class object. If a class with the same name is already in this Repository, the Repository version is returned.
     * Otherwise, getResourceAsStream() is called on the Class object to find the class's representation. If the representation is found, it is added to the
     * Repository.
     *
     * @see Class
     * @param clazz
     *            the runtime Class object
     * @return JavaClass object for given runtime class
     * @throws ClassNotFoundException
     *             if the class is not in the Repository, and its representation could not be found
     */
    @Override
    public JavaClass loadClass(final Class<?> clazz) throws ClassNotFoundException {
        final String className = clazz.getName();
        final JavaClass repositoryClass = findClass(className);
        if (repositoryClass != null) {
            return repositoryClass;
        }
        String name = className;
        final int i = name.lastIndexOf('.');
        if (i > 0) {
            name = name.substring(i + 1);
        }
        JavaClass cls = null;
        try (InputStream clsStream = clazz.getResourceAsStream(name + ".class")) {
            return cls = loadClass(clsStream, className);
        } catch (final IOException e) {
            return cls;
        }

    }

    private JavaClass loadClass(final InputStream is, final String className) throws ClassNotFoundException {
        try {
            if (is != null) {
                final ClassParser parser = new ClassParser(is, className);
                final JavaClass clazz = parser.parse();
                storeClass(clazz);
                return clazz;
            }
        } catch (final IOException e) {
            throw new ClassNotFoundException("Exception while looking for class " + className + ": " + e, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    // ignored
                }
            }
        }
        throw new ClassNotFoundException("SyntheticRepository could not load " + className);
    }

    /**
     * ClassPath associated with the Repository.
     */
    @Override
    public ClassPath getClassPath() {
        return _path;
    }

    /**
     * Clear all entries from cache.
     */
    @Override
    public void clear() {
        _loadedClasses.clear();
    }
}
