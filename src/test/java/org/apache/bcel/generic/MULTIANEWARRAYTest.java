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

import org.apache.bcel.Const;
import org.apache.bcel.util.ByteSequence;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link MULTIANEWARRAY}.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-4.html#jvms-4.10.1.9.multianewarray">JVM MULTIANEWARRAY specification</a>
 */
class MULTIANEWARRAYTest {

    /**
     * The {@code dimensions} operand of {@code multianewarray} is an unsigned byte (1-255), so a value above 127 must not be read as a negative number.
     */
    @Test
    void testInitFromFileReadsDimensionsUnsigned() throws Exception {
        // multianewarray, index 0x0005, dimensions 0xC8 (200)
        final byte[] code = { (byte) Const.MULTIANEWARRAY, 0x00, 0x05, (byte) 0xC8 };
        try (ByteSequence bytes = new ByteSequence(code)) {
            final MULTIANEWARRAY instruction = (MULTIANEWARRAY) Instruction.readInstruction(bytes);
            assertEquals(200, instruction.getDimensions());
        }
    }
}
