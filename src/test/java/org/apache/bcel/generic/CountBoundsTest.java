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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

/**
 * The {@code count} operand of {@code invokeinterface} and the {@code dimensions} operand of {@code multianewarray} are
 * both encoded as a u1, so the constructors must reject a value above {@link Const#MAX_BYTE}; otherwise {@code dump}
 * truncates it with {@code writeByte} and the emitted instruction carries a different count than requested. The lower
 * bound was already enforced; these cover the missing upper bound.
 */
class CountBoundsTest {

    private static int dumpedByte(final Instruction i, final int offset) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            i.dump(dos);
        }
        return bos.toByteArray()[offset] & 0xFF;
    }

    @Test
    void testInvokeInterfaceCountRoundTrips() throws IOException {
        assertEquals(Const.MAX_BYTE, dumpedByte(new INVOKEINTERFACE(1, Const.MAX_BYTE), 3));
    }

    @Test
    void testInvokeInterfaceRejectsCountAboveU1() {
        assertEquals(Const.MAX_BYTE, new INVOKEINTERFACE(1, Const.MAX_BYTE).getCount());
        assertThrows(ClassGenException.class, () -> new INVOKEINTERFACE(1, Const.MAX_BYTE + 1));
    }

    @Test
    void testMultiANewArrayDimensionsRoundTrips() throws IOException {
        assertEquals(Const.MAX_BYTE, dumpedByte(new MULTIANEWARRAY(1, (short) Const.MAX_BYTE), 3));
    }

    @Test
    void testMultiANewArrayRejectsDimensionsAboveU1() {
        assertEquals(Const.MAX_BYTE, new MULTIANEWARRAY(1, (short) Const.MAX_BYTE).getDimensions());
        assertThrows(ClassGenException.class, () -> new MULTIANEWARRAY(1, (short) (Const.MAX_BYTE + 1)));
    }
}
