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
package org.apache.bcel;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;

/**
 * The repository maintains informations about class interdependencies, for example, whether a class is a sub-class of another.
 * Delegates actual class loading to SyntheticRepository with current class path by default.
 *
 * @see org.apache.bcel.util.Repository
 * @see SyntheticRepository
 */
public abstract class Repository {

    private static org.apache.bcel.util.Repository repository = SyntheticRepository.getInstance();

    /**
     * Constructs a new Repository.
     */
    public Repository() {
    }

    /**
     * Adds clazz to repository if there isn't an equally named class already in there.
     *
     * @param clazz the class to add.
     * @return old entry in repository.
     */
    public static JavaClass addClass(final JavaClass clazz) {
        final JavaClass old = repository.findClass(clazz.getClassName());
        repository.storeClass(clazz);
        return old;
    }

    /**
     * Clears the repository.
     */
    public static void clearCache() {
        repository.clear();
    }

    /**
     * Gets all interfaces implemented by the class.
     *
     * @param clazz the class.
     * @return all interfaces implemented by class and its super classes and the interfaces that those interfaces extend,
     *         and so on. (Some people call this a transitive hull).
     * @throws ClassNotFoundException if any of the class's superclasses or superinterfaces can't be found.
     */
    public static JavaClass[] getInterfaces(final JavaClass clazz) throws ClassNotFoundException {
        return clazz.getAllInterfaces();
    }

    /**
     * Gets all interfaces implemented by the class.
     *
     * @param className the class name.
     * @return all interfaces implemented by class and its super classes and the interfaces that extend those interfaces,
     *         and so on.
     * @throws ClassNotFoundException if the named class can't be found, or if any of its superclasses or superinterfaces
     *         can't be found.
     */
    public static JavaClass[] getInterfaces(final String className) throws ClassNotFoundException {
        return getInterfaces(lookupClass(className));
    }

    /**
     * Gets the currently used repository instance.
     *
     * @return currently used repository instance.
     */
    public static org.apache.bcel.util.Repository getRepository() {
        return repository;
    }

    /**
     * Gets the list of super classes.
     *
     * @param clazz the class.
     * @return list of super classes of clazz in ascending order, that is, Object is always the last element.
     * @throws ClassNotFoundException if any of the superclasses can't be found.
     */
    public static JavaClass[] getSuperClasses(final JavaClass clazz) throws ClassNotFoundException {
        return clazz.getSuperClasses();
    }

    /**
     * Gets the list of super classes.
     *
     * @param className the class name.
     * @return list of super classes of clazz in ascending order, that is, Object is always the last element.
     * @throws ClassNotFoundException if the named class or any of its superclasses can't be found.
     */
    public static JavaClass[] getSuperClasses(final String className) throws ClassNotFoundException {
        return getSuperClasses(lookupClass(className));
    }

    /**
     * Tests if clazz is an implementation of interface inter.
     *
     * @param clazz the class to test.
     * @param inter the interface.
     * @return true, if clazz is an implementation of interface inter.
     * @throws ClassNotFoundException if any superclasses or superinterfaces of clazz can't be found.
     */
    public static boolean implementationOf(final JavaClass clazz, final JavaClass inter) throws ClassNotFoundException {
        return clazz.implementationOf(inter);
    }

    /**
     * Tests if clazz is an implementation of interface inter.
     *
     * @param clazz the class to test.
     * @param inter the interface name.
     * @return true, if clazz is an implementation of interface inter.
     * @throws ClassNotFoundException if inter or any superclasses or superinterfaces of clazz can't be found.
     */
    public static boolean implementationOf(final JavaClass clazz, final String inter) throws ClassNotFoundException {
        return implementationOf(clazz, lookupClass(inter));
    }

    /**
     * Tests if clazz is an implementation of interface inter.
     *
     * @param clazz the class name to test.
     * @param inter the interface.
     * @return true, if clazz is an implementation of interface inter.
     * @throws ClassNotFoundException if clazz or any superclasses or superinterfaces of clazz can't be found.
     */
    public static boolean implementationOf(final String clazz, final JavaClass inter) throws ClassNotFoundException {
        return implementationOf(lookupClass(clazz), inter);
    }

    /**
     * Tests if clazz is an implementation of interface inter.
     *
     * @param clazz the class name to test.
     * @param inter the interface name.
     * @return true, if clazz is an implementation of interface inter.
     * @throws ClassNotFoundException if clazz, inter, or any superclasses or superinterfaces of clazz can't be found.
     */
    public static boolean implementationOf(final String clazz, final String inter) throws ClassNotFoundException {
        return implementationOf(lookupClass(clazz), lookupClass(inter));
    }

    /**
     * Equivalent to runtime "instanceof" operator.
     *
     * @param clazz the class to test.
     * @param superclass the superclass.
     * @return true, if clazz is an instance of superclass.
     * @throws ClassNotFoundException if any superclasses or superinterfaces of clazz can't be found.
     */
    public static boolean instanceOf(final JavaClass clazz, final JavaClass superclass) throws ClassNotFoundException {
        return clazz.instanceOf(superclass);
    }

    /**
     * Tests if clazz is an instance of superclass.
     *
     * @param clazz the class to test.
     * @param superclass the superclass name.
     * @return true, if clazz is an instance of superclass.
     * @throws ClassNotFoundException if superclass can't be found.
     */
    public static boolean instanceOf(final JavaClass clazz, final String superclass) throws ClassNotFoundException {
        return instanceOf(clazz, lookupClass(superclass));
    }

    /**
     * Tests if clazz is an instance of superclass.
     *
     * @param clazz the class name to test.
     * @param superclass the superclass.
     * @return true, if clazz is an instance of superclass.
     * @throws ClassNotFoundException if clazz can't be found.
     */
    public static boolean instanceOf(final String clazz, final JavaClass superclass) throws ClassNotFoundException {
        return instanceOf(lookupClass(clazz), superclass);
    }

    /**
     * Tests if clazz is an instance of superclass.
     *
     * @param clazz the class name to test.
     * @param superclass the superclass name.
     * @return true, if clazz is an instance of superclass.
     * @throws ClassNotFoundException if either clazz or superclass can't be found.
     */
    public static boolean instanceOf(final String clazz, final String superclass) throws ClassNotFoundException {
        return instanceOf(lookupClass(clazz), lookupClass(superclass));
    }

    /**
     * Tries to find class source using the internal repository instance.
     *
     * @param clazz the class.
     * @see Class
     * @return JavaClass object for given runtime class.
     * @throws ClassNotFoundException if the class could not be found or parsed correctly.
     */
    public static JavaClass lookupClass(final Class<?> clazz) throws ClassNotFoundException {
        return repository.loadClass(clazz);
    }

    /**
     * Lookups class somewhere found on your CLASSPATH, or wherever the repository instance looks for it.
     *
     * @param className the class name.
     * @return class object for given fully qualified class name.
     * @throws ClassNotFoundException if the class could not be found or parsed correctly.
     */
    public static JavaClass lookupClass(final String className) throws ClassNotFoundException {
        return repository.loadClass(className);
    }

    /**
     * Looks up the class file.
     *
     * @param className the class name.
     * @return class file object for given Java class by looking on the system class path; returns null if the class file
     *         can't be found.
     */
    public static ClassPath.ClassFile lookupClassFile(final String className) {
        try (ClassPath path = repository.getClassPath()) {
            return path == null ? null : path.getClassFile(className);
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * Removes given class from repository.
     *
     * @param clazz the class to remove.
     */
    public static void removeClass(final JavaClass clazz) {
        repository.removeClass(clazz);
    }

    /**
     * Removes class with given (fully qualified) name from repository.
     *
     * @param clazz the class name to remove.
     */
    public static void removeClass(final String clazz) {
        repository.removeClass(repository.findClass(clazz));
    }

    /**
     * Sets repository instance to be used for class loading.
     *
     * @param rep the repository instance.
     */
    public static void setRepository(final org.apache.bcel.util.Repository rep) {
        repository = rep;
    }
}
