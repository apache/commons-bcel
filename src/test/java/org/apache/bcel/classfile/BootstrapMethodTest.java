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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class BootstrapMethodTest {

    private static byte[] dump(final BootstrapMethod method) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream(); DataOutputStream output = new DataOutputStream(bytes)) {
            method.dump(output);
            return bytes.toByteArray();
        }
    }

    @Test
    void testConstructorCopiesArguments() {
        final int[] arguments = {10, 20, 30};
        final BootstrapMethod method = new BootstrapMethod(1, arguments);
        arguments[0] = 999;
        assertArrayEquals(new int[] {10, 20, 30}, method.getBootstrapArguments());
        assertEquals(3, method.getNumBootstrapArguments());
    }

    @Test
    void testConstructorNormalizesNull() throws IOException {
        final BootstrapMethod method = new BootstrapMethod(1, (int[]) null);
        assertArrayEquals(new int[0], method.getBootstrapArguments());
        assertEquals(0, method.getNumBootstrapArguments());
        assertArrayEquals(new int[0], new BootstrapMethod(method).getBootstrapArguments());
        assertArrayEquals(new int[0], method.copy().getBootstrapArguments());
        assertArrayEquals(new byte[] {0, 1, 0, 0}, dump(method));
    }

    @Test
    void testDumpChangesOnlyAfterExplicitUpdate() throws IOException {
        final int[] arguments = {10, 20};
        final BootstrapMethod method = new BootstrapMethod(1, arguments);
        arguments[0] = 99;
        method.getBootstrapArguments()[1] = 88;
        assertArrayEquals(new byte[] {0, 1, 0, 2, 0, 10, 0, 20}, dump(method));

        final int[] replacement = {30};
        method.setBootstrapArguments(replacement);
        replacement[0] = 77;
        assertArrayEquals(new byte[] {0, 1, 0, 1, 0, 30}, dump(method));
    }

    @Test
    void testGetterReturnsSnapshot() {
        final BootstrapMethod method = new BootstrapMethod(1, new int[] {10, 20, 30});
        final int[] arguments = method.getBootstrapArguments();
        arguments[0] = 999;
        assertArrayEquals(new int[] {10, 20, 30}, method.getBootstrapArguments());
    }

    @Test
    void testReadAndDumpArguments() throws IOException {
        final byte[] expected = {0, 1, 0, 3, 0, 10, 0, 20, 0, 30};
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(expected))) {
            final BootstrapMethod method = new BootstrapMethod(input);
            assertEquals(1, method.getBootstrapMethodRef());
            assertEquals(3, method.getNumBootstrapArguments());
            assertArrayEquals(new int[] {10, 20, 30}, method.getBootstrapArguments());
            assertArrayEquals(expected, dump(method));
        }
    }

    @Test
    void testSetterCopiesArguments() {
        final BootstrapMethod method = new BootstrapMethod(1, new int[] {10});
        final int[] arguments = {20, 30};
        method.setBootstrapArguments(arguments);
        arguments[0] = 999;
        assertArrayEquals(new int[] {20, 30}, method.getBootstrapArguments());
        assertEquals(2, method.getNumBootstrapArguments());
    }

    @Test
    void testSetterNormalizesNull() throws IOException {
        final BootstrapMethod method = new BootstrapMethod(1, new int[] {10, 20, 30});
        method.setBootstrapArguments(null);
        assertArrayEquals(new int[0], method.getBootstrapArguments());
        assertEquals(0, method.getNumBootstrapArguments());
        assertArrayEquals(new int[0], method.copy().getBootstrapArguments());
        assertArrayEquals(new byte[] {0, 1, 0, 0}, dump(method));
    }
}
