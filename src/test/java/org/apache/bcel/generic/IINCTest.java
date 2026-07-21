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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.bcel.util.ByteSequence;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link IINC}.
 */
class IINCTest {

    /**
     * The increment of a wide {@code iinc} is a signed short, so a value inside that range must round-trip through
     * {@code dump}.
     */
    @Test
    void testWideIncrementRoundTrips() throws Exception {
        final IINC iinc = new IINC(0, 30000);
        try (ByteSequence bytes = new ByteSequence(iinc.dumpToByteArray())) {
            assertEquals(30000, ((IINC) Instruction.readInstruction(bytes)).getIncrement());
        }
    }

    /**
     * {@code dump} emits the wide-form increment with {@code writeShort}, so the constructor and {@link IINC#setIncrement}
     * must reject an increment outside the signed-short range instead of truncating it to a different value.
     */
    @Test
    void testRejectsIncrementAboveShort() {
        assertEquals(Short.MAX_VALUE, new IINC(0, Short.MAX_VALUE).getIncrement());
        assertThrows(ClassGenException.class, () -> new IINC(0, Short.MAX_VALUE + 1));
    }

    @Test
    void testRejectsIncrementBelowShort() {
        assertEquals(Short.MIN_VALUE, new IINC(0, Short.MIN_VALUE).getIncrement());
        assertThrows(ClassGenException.class, () -> new IINC(0, Short.MIN_VALUE - 1));
    }

    @Test
    void testSetIncrementRejectsOutOfRange() {
        final IINC iinc = new IINC(0, 1);
        assertThrows(ClassGenException.class, () -> iinc.setIncrement(40000));
    }
}
