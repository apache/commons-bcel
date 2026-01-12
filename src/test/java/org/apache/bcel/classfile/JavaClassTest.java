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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link JavaClass}.
 */
class JavaClassTest {

    private static final String CLASS_NAME = "TargetClass";

// Doesn't compile due to cyclic inheritance
//  private interface InterfaceA extends InterfaceB {
//  }
//
//  private interface InterfaceB extends InterfaceA {
//  }

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void beforeAll() throws Exception {
        // Create InterfaceA that extends InterfaceB (will create cycle)
        writeInterfaceA();
        // Create InterfaceB that extends InterfaceA (completes the cycle)
        writeInterfaceB();
        // Create a class that implements InterfaceA
        writeTargetClass();
        // Cycle: InterfaceA -> InterfaceB -> InterfaceA -> ...
    }

    static byte[] createClass(final String name, final String extendsClass) throws IOException {
        return toByteArray(new ClassGen(name, extendsClass, name + ".java", Const.ACC_PUBLIC, new String[] {}));
    }

    static byte[] createClass(final String name, final String extendsClass, final String implementsInterface) throws IOException {
        return toByteArray(new ClassGen(name, extendsClass, name + ".java", Const.ACC_PUBLIC, new String[] { implementsInterface }));
    }

    static byte[] createInterface(final String name, final String extendsInterface) throws IOException {
        return toByteArray(new ClassGen(name, "java.lang.Object", name + ".java", Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT,
                new String[] { extendsInterface }));
    }

    static byte[] toByteArray(final ClassGen cg) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cg.getJavaClass().dump(baos);
        return baos.toByteArray();
    }

    private static void writeInterfaceA() throws Exception {
        // Create InterfaceA that extends InterfaceB
        final ClassGen cg = new ClassGen("InterfaceA", "java.lang.Object", "InterfaceA.java", Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT,
                new String[] { "InterfaceB" });
        cg.getJavaClass().dump(tempDir.resolve("InterfaceA.class").toString());
    }

    private static void writeInterfaceB() throws Exception {
        // Create InterfaceB that extends InterfaceA
        final ClassGen cg = new ClassGen("InterfaceB", "java.lang.Object", "InterfaceB.java", Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT,
                new String[] { "InterfaceA" });
        cg.getJavaClass().dump(tempDir.resolve("InterfaceB.class").toString());
    }

    private static void writeTargetClass() throws Exception {
        // Create a class that implements InterfaceA
        final ClassGen cg = new ClassGen(CLASS_NAME, "java.lang.Object", "VulnerableClass.java", Const.ACC_PUBLIC, new String[] { "InterfaceA" });
        // Add default constructor
        final InstructionList il = new InstructionList();
        final MethodGen constructor = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<init>", CLASS_NAME, il, cg.getConstantPool());
        final InstructionFactory factory = new InstructionFactory(cg);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Const.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.VOID));
        constructor.setMaxStack();
        constructor.setMaxLocals();
        cg.addMethod(constructor.getMethod());
        il.dispose();
        // Create the class file
        cg.getJavaClass().dump(tempDir.resolve(CLASS_NAME + ".class").toString());
    }

    private Field findFieldDoesNotExist(final Class<?> clazz) throws ClassNotFoundException {
        return Repository.lookupClass(clazz.getName()).findField("nonExistentField", Type.INT);
    }

    @Test
    void testFindFieldCustomClass() throws Exception {
        final byte[] classABytes = createClass("CyclicClassA", "CyclicClassB");
        final byte[] classBBytes = createInterface("CyclicClassB", "CyclicClassA");
        final byte[] testClassBytes = createClass("CyclicTestClass", "CyclicClassA");
        final JavaClass interfaceA = new ClassParser(new ByteArrayInputStream(classABytes), "CyclicClassA.class").parse();
        final JavaClass interfaceB = new ClassParser(new ByteArrayInputStream(classBBytes), "CyclicClassB.class").parse();
        final JavaClass testClass = new ClassParser(new ByteArrayInputStream(testClassBytes), "CyclicTestClass.class").parse();
        final SyntheticRepository repo = SyntheticRepository.getInstance();
        try {
            repo.storeClass(interfaceA);
            repo.storeClass(interfaceB);
            repo.storeClass(testClass);
            Repository.setRepository(repo);
            assertThrows(ClassFormatException.class, () -> testClass.findField("nonExistentField", Type.INT));
        } finally {
            repo.removeClass(interfaceA);
            repo.removeClass(interfaceB);
            repo.removeClass(testClass);
        }
    }

    @Test
    void testFindFieldCustomInterface1() throws ClassNotFoundException {
        // Set up repository to load classes from the malicious_classes directory
        final String classPath = tempDir.toString() + System.getProperty("path.separator") + System.getProperty("java.class.path");
        Repository.setRepository(SyntheticRepository.getInstance(new ClassPath(classPath)));
        assertThrows(ClassFormatException.class, () -> Repository.lookupClass(CLASS_NAME).findField("nonExistentField", Type.INT));
    }

    @Test
    void testFindFieldCustomInterface2() throws Exception {
        final byte[] interfaceABytes = createInterface("CyclicInterfaceA", "CyclicInterfaceB");
        final byte[] interfaceBBytes = createInterface("CyclicInterfaceB", "CyclicInterfaceA");
        final byte[] testClassBytes = createClass("CyclicTestClass", "java.lang.Object", "CyclicInterfaceA");
        final JavaClass interfaceA = new ClassParser(new ByteArrayInputStream(interfaceABytes), "CyclicInterfaceA.class").parse();
        final JavaClass interfaceB = new ClassParser(new ByteArrayInputStream(interfaceBBytes), "CyclicInterfaceB.class").parse();
        final JavaClass testClass = new ClassParser(new ByteArrayInputStream(testClassBytes), "CyclicTestClass.class").parse();
        final SyntheticRepository repo = SyntheticRepository.getInstance();
        try {
            repo.storeClass(interfaceA);
            repo.storeClass(interfaceB);
            repo.storeClass(testClass);
            Repository.setRepository(repo);
            assertThrows(ClassFormatException.class, () -> testClass.findField("nonExistentField", Type.INT));
        } finally {
            repo.removeClass(interfaceA);
            repo.removeClass(interfaceB);
            repo.removeClass(testClass);
        }
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.Java8PublicClasses#getAll")
    void testFindFieldJavaLang(final Class<?> clazz) throws ClassNotFoundException {
        assertNull(findFieldDoesNotExist(clazz));
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.Java8PublicClasses#getAll")
    void testGetAllInterfaces(final Class<?> clazz) throws ClassNotFoundException {
        assertNotNull(Repository.lookupClass(clazz.getName()).getAllInterfaces());
    }

    @ParameterizedTest
    @MethodSource("org.apache.bcel.Java8PublicClasses#getAll")
    void testGetSuperClassesAll(final Class<?> clazz) throws ClassNotFoundException {
        assertNotNull(Repository.lookupClass(clazz.getName()).getSuperClasses());
    }

}
