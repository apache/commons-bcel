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

import java.io.IOException;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link INVOKEINTERFACE}.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-6.html#jvms-6.5.invokeinterface">JVM INVOKEINTERFACE specification</a>
 */
class INVOKEINTERFACETest {

    private static int dumpedCountByte(final INVOKEINTERFACE instruction) throws IOException {
        // opcode, u2 index, u1 count, u1 zero
        return instruction.dumpToByteArray()[3] & 0xFF;
    }

    @Test
    void testCountRoundTrips() throws IOException {
        assertEquals(Const.MAX_BYTE, dumpedCountByte(new INVOKEINTERFACE(1, Const.MAX_BYTE)));
    }

    /**
     * The {@code count} operand of {@code invokeinterface} is an unsigned byte (1-255) and {@code dump} writes it with {@code writeByte}, so the constructor
     * must reject values above {@link Const#MAX_BYTE} instead of truncating them.
     */
    @Test
    void testRejectsCountAboveU1() {
        assertEquals(Const.MAX_BYTE, new INVOKEINTERFACE(1, Const.MAX_BYTE).getCount());
        assertThrows(ClassGenException.class, () -> new INVOKEINTERFACE(1, Const.MAX_BYTE + 1));
    }
}
