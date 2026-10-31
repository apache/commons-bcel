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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.commons.lang3.SystemProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link JavaClass}.
 */
class JavaClassTest {

    private static final String INTERFACE_NAME_A = "InterfaceA";

    private static final String INTERFACE_NAME_B = "InterfaceB";

    private static final String CLASS_NAME = "TargetClass";
// Doesn't compile due to cyclic inheritance
//  private interface InterfaceA extends InterfaceB {
//  }
//
//  private interface InterfaceB extends InterfaceA {
//  }

    @TempDir
    static Path tempDir;

    static Path tempClassFile;

    static Path tempIntefaceAFile;

    static Path tempIntefaceBFile;

    @BeforeAll
    static void beforeAll() throws Exception {
        // Create InterfaceA that extends InterfaceB (will create cycle)
        tempIntefaceAFile = writeInterfaceA();
        // Create InterfaceB that extends InterfaceA (completes the cycle)
        tempIntefaceBFile = writeInterfaceB();
        // Create a class that implements InterfaceA
        tempClassFile = writeTargetClass();
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

    private static Path writeInterfaceA() throws Exception {
        // Create InterfaceA that extends InterfaceB
        final ClassGen classGen = new ClassGen(INTERFACE_NAME_A, "java.lang.Object", INTERFACE_NAME_A + ".java",
                Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT, new String[] { INTERFACE_NAME_B });
        final Path path = tempDir.resolve(INTERFACE_NAME_A + ".class");
        classGen.getJavaClass().dump(path.toString());
        return path;
    }

    private static Path writeInterfaceB() throws Exception {
        // Create InterfaceB that extends InterfaceA
        final ClassGen classGen = new ClassGen(INTERFACE_NAME_B, "java.lang.Object", INTERFACE_NAME_B + ".java",
                Const.ACC_PUBLIC | Const.ACC_INTERFACE | Const.ACC_ABSTRACT, new String[] { INTERFACE_NAME_A });
        final Path path = tempDir.resolve(INTERFACE_NAME_B + ".class");
        classGen.getJavaClass().dump(path.toString());
        return path;
    }

    private static Path writeTargetClass() throws Exception {
        // Create a class that implements InterfaceA
        final ClassGen cg = new ClassGen(CLASS_NAME, "java.lang.Object", CLASS_NAME + ".java", Const.ACC_PUBLIC, new String[] { INTERFACE_NAME_A });
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
        final Path path = tempDir.resolve(CLASS_NAME + ".class");
        cg.getJavaClass().dump(path.toString());
        return path;
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
    void testFindFieldCustomInterface1() throws IOException, ClassNotFoundException {
        // Set up repository to load classes from the malicious_classes directory
        final String classPath = tempDir.toString() + SystemProperties.getPathSeparator() + SystemProperties.getJavaClassPath();
        Repository.setRepository(SyntheticRepository.getInstance(new ClassPath(classPath)));
        assertThrows(ClassFormatException.class, () -> Repository.lookupClass(CLASS_NAME).findField("nonExistentField", Type.INT));
        // sanity check
        final Path targetDir = Paths.get("target/test-classes");
        final Path targetClassFile = targetDir.resolve(CLASS_NAME + ".class");
        final Path targetInterfaceA = targetDir.resolve(INTERFACE_NAME_A + ".class");
        final Path targetInterfaceB = targetDir.resolve(INTERFACE_NAME_B + ".class");
        try {
            Files.copy(tempClassFile, targetClassFile);
            Files.copy(tempIntefaceAFile, targetInterfaceA);
            Files.copy(tempIntefaceBFile, targetInterfaceB);
            assertThrows(ClassCircularityError.class, () -> Class.forName(CLASS_NAME));
        } finally {
            Files.delete(targetClassFile);
            Files.delete(targetInterfaceA);
            Files.delete(targetInterfaceB);
        }
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
