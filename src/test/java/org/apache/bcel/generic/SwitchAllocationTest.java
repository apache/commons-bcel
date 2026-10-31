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
import org.apache.bcel.classfile.ClassFormatException;
import org.junit.jupiter.api.Test;

/**
 * Tests that {@link TABLESWITCH} and {@link LOOKUPSWITCH} validate their match table counts against the remaining code bytes instead of allocating whatever
 * amount of memory the raw count fields request.
 */
class SwitchAllocationTest {

    private static byte[] switchBytes(final short opcode, final int... operands) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeByte(opcode);
            for (int i = 0; i < 3; i++) {
                dos.writeByte(0); // padding up to the next 4-byte boundary
            }
            for (final int operand : operands) {
                dos.writeInt(operand);
            }
        }
        return baos.toByteArray();
    }

    @Test
    void testLookupswitchHugeNpairsRejected() throws IOException {
        // default = 0, npairs = Integer.MAX_VALUE, no pairs present.
        final byte[] code = switchBytes(Const.LOOKUPSWITCH, 0, Integer.MAX_VALUE);
        assertThrows(ClassFormatException.class, () -> new InstructionList(code));
    }

    @Test
    void testLookupswitchNegativeNpairsRejected() throws IOException {
        final byte[] code = switchBytes(Const.LOOKUPSWITCH, 0, -1);
        assertThrows(ClassFormatException.class, () -> new InstructionList(code));
    }

    @Test
    void testLookupswitchValidAccepted() throws IOException {
        // default = 0, npairs = 1, one (match, offset) pair branching back to the instruction itself.
        final byte[] code = switchBytes(Const.LOOKUPSWITCH, 0, 1, 5, 0);
        assertEquals(1, new InstructionList(code).getLength());
    }

    @Test
    void testTableswitchHugeRangeRejected() throws IOException {
        // default = 0, low = 0, high = Integer.MAX_VALUE - 1: requests roughly 8 GB across the three tables.
        final byte[] code = switchBytes(Const.TABLESWITCH, 0, 0, Integer.MAX_VALUE - 1);
        assertThrows(ClassFormatException.class, () -> new InstructionList(code));
    }

    @Test
    void testTableswitchOverflowingRangeRejected() throws IOException {
        // high - low + 1 overflows int arithmetic to a negative value.
        final byte[] code = switchBytes(Const.TABLESWITCH, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThrows(ClassFormatException.class, () -> new InstructionList(code));
    }

    @Test
    void testTableswitchValidAccepted() throws IOException {
        // default = 0, low = 0, high = 1, two jump offsets branching back to the instruction itself.
        final byte[] code = switchBytes(Const.TABLESWITCH, 0, 0, 1, 0, 0);
        assertEquals(1, new InstructionList(code).getLength());
    }
}
