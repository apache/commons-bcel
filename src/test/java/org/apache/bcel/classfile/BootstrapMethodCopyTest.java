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

import org.junit.jupiter.api.Test;

/**
 * Tests that {@link BootstrapMethod} copies preserve their source's values and can be updated independently.
 */
class BootstrapMethodCopyTest {

    private static void assertIndependentUpdates(final BootstrapMethod original, final BootstrapMethod copy) {
        assertEquals(original.getBootstrapMethodRef(), copy.getBootstrapMethodRef());
        assertArrayEquals(original.getBootstrapArguments(), copy.getBootstrapArguments());

        final int[] changedArguments = copy.getBootstrapArguments();
        changedArguments[0] = 999;
        copy.setBootstrapArguments(changedArguments);
        assertArrayEquals(new int[] {999, 20, 30}, copy.getBootstrapArguments());
        assertArrayEquals(new int[] {10, 20, 30}, original.getBootstrapArguments());

        original.setBootstrapArguments(new int[] {40, 50});
        original.setBootstrapMethodRef(2);
        assertArrayEquals(new int[] {40, 50}, original.getBootstrapArguments());
        assertArrayEquals(new int[] {999, 20, 30}, copy.getBootstrapArguments());
        assertEquals(2, original.getBootstrapMethodRef());
        assertEquals(1, copy.getBootstrapMethodRef());
    }

    @Test
    void testCopyConstructorCanBeUpdatedIndependently() {
        final BootstrapMethod original = new BootstrapMethod(1, new int[] {10, 20, 30});
        assertIndependentUpdates(original, new BootstrapMethod(original));
    }

    @Test
    void testCopyCanBeUpdatedIndependently() {
        final BootstrapMethod original = new BootstrapMethod(1, new int[] {10, 20, 30});
        assertIndependentUpdates(original, original.copy());
    }
}
