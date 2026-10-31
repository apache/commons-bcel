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
package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests that the constant pool dedup tables do not conflate distinct references whose names contain one of the ASCII
 * delimiter characters. The JVMS only forbids {@code . ; [ /} (and {@code < >} for members) in names, so a class or
 * member name may legally contain {@code : # &}.
 */
class ConstantPoolGenKeyCollisionTest {

    @Test
    void testFieldrefNamesContainingDelimiter() {
        final ConstantPoolGen cpg = new ConstantPoolGen();
        final int a = cpg.addFieldref("A", "b&c", "I");
        final int b = cpg.addFieldref("A&b", "c", "I");
        assertNotEquals(a, b, "distinct fieldrefs collided to constant pool index " + a);
    }

    @Test
    void testInterfaceMethodrefNamesContainingDelimiter() {
        final ConstantPoolGen cpg = new ConstantPoolGen();
        final int a = cpg.addInterfaceMethodref("I", "m#n", "()V");
        final int b = cpg.addInterfaceMethodref("I#m", "n", "()V");
        assertNotEquals(a, b, "distinct interface methodrefs collided to constant pool index " + a);
    }

    @Test
    void testMethodrefDedupStillWorks() {
        final ConstantPoolGen cpg = new ConstantPoolGen();
        final int a = cpg.addMethodref("Foo", "bar", "()V");
        final int b = cpg.addMethodref("Foo", "bar", "()V");
        assertEquals(a, b, "identical methodrefs must reuse the same constant pool entry");
    }

    @Test
    void testMethodrefNamesContainingDelimiter() {
        final ConstantPoolGen cpg = new ConstantPoolGen();
        final int a = cpg.addMethodref("Foo", "bar:baz", "()V");
        final int b = cpg.addMethodref("Foo:bar", "baz", "()V");
        assertNotEquals(a, b, "distinct methodrefs collided to constant pool index " + a);
    }
}
