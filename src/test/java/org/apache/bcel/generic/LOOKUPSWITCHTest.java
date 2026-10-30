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

import org.apache.bcel.util.ByteSequence;
import org.junit.jupiter.api.Test;

class LOOKUPSWITCHTest {

    private static byte[] lookupSwitch(final int npairs) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(bos)) {
            out.writeByte(org.apache.bcel.Const.LOOKUPSWITCH); // opcode at offset 0
            out.writeByte(0); // 3 padding bytes to the next 4-byte boundary
            out.writeByte(0);
            out.writeByte(0);
            out.writeInt(0); // default offset
            out.writeInt(npairs);
            for (int i = 0; i < npairs; i++) {
                out.writeInt(i); // match
                out.writeInt(i + 1); // offset
            }
        }
        return bos.toByteArray();
    }

    @Test
    void testLengthMatchesEncodedSize() throws IOException {
        final byte[] bytes = lookupSwitch(2);
        try (ByteSequence sequence = new ByteSequence(bytes)) {
            final Instruction instruction = Instruction.readInstruction(sequence);
            assertEquals(bytes.length, sequence.getIndex());
            assertEquals(bytes.length, instruction.getLength());
        }
    }
}
