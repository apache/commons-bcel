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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.bcel.Repository;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link JavaClass}.
 * <p>
 * Tests for cyclic hierarchy vulnerabilities in {@link JavaClass}. These tests demonstrate CWE-674 (Uncontrolled Recursion) vulnerabilities:
 * <ul>
 * <li>getAllInterfaces(): infinite queue growth with cyclic interfaces</li>
 * <li>getSuperClasses(): infinite loop with cyclic superclasses Without the fix, these tests will fail. With the fix, they pass.</li>
 * </ul>
 */
class JavaClassCyclicTest {

    private JavaClass cyclicClassA;

    private JavaClass cyclicClassB;

    private JavaClass cyclicInterfaceA;

    private JavaClass cyclicInterfaceB;

    private JavaClass cyclicTestClass;

    private SyntheticRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        repo = SyntheticRepository.getInstance();
        Repository.setRepository(repo);
        // Create cyclic interfaces: A extends B, B extends A
        final byte[] interfaceABytes = JavaClassTest.createInterface("CyclicInterfaceA", "CyclicInterfaceB");
        final byte[] interfaceBBytes = JavaClassTest.createInterface("CyclicInterfaceB", "CyclicInterfaceA");
        final byte[] testClassBytes = JavaClassTest.createClass("CyclicTestClass", "java.lang.Object", "CyclicInterfaceA");
        cyclicInterfaceA = new ClassParser(new ByteArrayInputStream(interfaceABytes), "CyclicInterfaceA.class").parse();
        cyclicInterfaceB = new ClassParser(new ByteArrayInputStream(interfaceBBytes), "CyclicInterfaceB.class").parse();
        cyclicTestClass = new ClassParser(new ByteArrayInputStream(testClassBytes), "CyclicTestClass.class").parse();
        repo.storeClass(cyclicInterfaceA);
        repo.storeClass(cyclicInterfaceB);
        repo.storeClass(cyclicTestClass);
        // Create cyclic classes: A extends B, B extends A
        final byte[] classABytes = JavaClassTest.createClass("CyclicClassA", "CyclicClassB");
        final byte[] classBBytes = JavaClassTest.createClass("CyclicClassB", "CyclicClassA");
        cyclicClassA = new ClassParser(new ByteArrayInputStream(classABytes), "CyclicClassA.class").parse();
        cyclicClassB = new ClassParser(new ByteArrayInputStream(classBBytes), "CyclicClassB.class").parse();
        repo.storeClass(cyclicClassA);
        repo.storeClass(cyclicClassB);
    }

    @AfterEach
    void tearDown() {
        if (cyclicInterfaceA != null) {
            repo.removeClass(cyclicInterfaceA);
        }
        if (cyclicInterfaceB != null) {
            repo.removeClass(cyclicInterfaceB);
        }
        if (cyclicTestClass != null) {
            repo.removeClass(cyclicTestClass);
        }
        if (cyclicClassA != null) {
            repo.removeClass(cyclicClassA);
        }
        if (cyclicClassB != null) {
            repo.removeClass(cyclicClassB);
        }
    }

    /**
     * Tests that getAllInterfaces() handles cyclic interface hierarchies gracefully. BUG: Without fix, getAllInterfaces() has no visited-node check before
     * enqueueing, causing infinite queue growth and eventual heap exhaustion (OutOfMemoryError). FIXED: With the fix, already-visited nodes are skipped,
     * preventing infinite growth.
     */
    @Test
    void testGetAllInterfacesCyclic() throws Exception {
        // TODO Use the test method once ClassCircularityError is implemented for this case
        // test(cyclicTestClass::getAllInterfaces);
        // TOOO Remove once the above is used
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<JavaClass[]> future = executor.submit(() -> cyclicTestClass.getAllInterfaces());
            // Without fix: will timeout (infinite queue growth) or throw OOM
            // With fix: completes quickly and returns the interfaces
            final JavaClass[] interfaces = future.get(3, TimeUnit.SECONDS);
            assertNotNull(interfaces, "getAllInterfaces() should return non-null array");
            assertTrue(interfaces.length >= 2, "Should find at least CyclicInterfaceA and CyclicInterfaceB");
        } catch (final TimeoutException e) {
            fail("getAllInterfaces() timed out - infinite queue growth vulnerability detected");
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof OutOfMemoryError) {
                fail("getAllInterfaces() caused OutOfMemoryError - infinite queue growth vulnerability detected");
            }
            throw e;
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Tests that getSuperClasses() detects cyclic superclass hierarchies. BUG: Without fix, getSuperClasses() has no cycle detection, causing an infinite loop
     * when traversing cyclic superclass chains. FIXED: With the fix, ClassCircularityError is thrown when a cycle is detected.
     */
    @Test
    void testGetSuperClassesCyclic() throws Exception {
        test(cyclicClassA::getSuperClasses);
    }

    void test(final Callable<JavaClass[]> callable) throws Exception {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<JavaClass[]> future = executor.submit(callable);
            // Without fix: will timeout (infinite loop)
            // With fix: throws ClassCircularityError immediately
            future.get(3, TimeUnit.SECONDS);
            fail("Should have thrown ClassCircularityError for cyclic hierarchy");
        } catch (final TimeoutException e) {
            fail("Timeout: infinite loop vulnerability detected");
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof ClassFormatException) {
                // Expected with fix - test passes
                return;
            }
            throw e;
        } finally {
            executor.shutdownNow();
        }
    }
}
