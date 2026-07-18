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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Const;
import org.apache.bcel.util.ByteSequence;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link LocalVariableInstruction} parsing of the {@code wide} prefix.
 */
class LocalVariableInstructionTest {

    private static int dumpedLength(final Instruction instruction) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            instruction.dump(dos);
        }
        return bos.toByteArray().length;
    }

    private static Instruction readInstruction(final byte[] code) throws IOException {
        try (ByteSequence bytes = new ByteSequence(code)) {
            return Instruction.readInstruction(bytes);
        }
    }

    /**
     * A {@code wide} prefix whose index does not fit in a byte must keep the four-byte encoding.
     */
    @Test
    void testWideLoadWithLargeIndexStaysWide() throws IOException {
        // wide, iload, index 0x012C (300)
        final Instruction instruction = readInstruction(new byte[] {(byte) Const.WIDE, (byte) Const.ILOAD, 0x01, 0x2C});
        assertEquals(300, ((ILOAD) instruction).getIndex());
        assertEquals(4, instruction.getLength());
        assertEquals(instruction.getLength(), dumpedLength(instruction));
    }

    /**
     * {@code dump} emits the {@code wide} prefix only when the index needs it, so a {@code wide} load whose index fits in a byte must report the two-byte
     * length it actually writes rather than the four bytes it was read from.
     */
    @Test
    void testWideLoadWithSmallIndexLengthMatchesDump() throws IOException {
        // wide, iload, index 0x0005
        final Instruction instruction = readInstruction(new byte[] {(byte) Const.WIDE, (byte) Const.ILOAD, 0x00, 0x05});
        assertEquals(5, ((ILOAD) instruction).getIndex());
        assertEquals(dumpedLength(instruction), instruction.getLength());
    }

    /**
     * The store family shares the same base-class parsing, so it must round-trip its length too.
     */
    @Test
    void testWideStoreWithSmallIndexLengthMatchesDump() throws IOException {
        // wide, istore, index 0x0007
        final Instruction instruction = readInstruction(new byte[] {(byte) Const.WIDE, (byte) Const.ISTORE, 0x00, 0x07});
        assertEquals(7, ((ISTORE) instruction).getIndex());
        assertEquals(dumpedLength(instruction), instruction.getLength());
    }
}
