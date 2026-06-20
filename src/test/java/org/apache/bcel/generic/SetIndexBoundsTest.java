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

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

/**
 * A constant pool or local variable index is encoded as a u2, so {@code setIndex} must reject a value above
 * {@link Const#MAX_SHORT}; otherwise {@code dump} truncates it with {@code writeShort} and the emitted instruction
 * references a different slot than requested. {@code LocalVariableInstruction.setIndex} already enforces this; these
 * cover the siblings that did not.
 */
class SetIndexBoundsTest {

    @Test
    void testCpInstructionRejectsIndexAboveU2() {
        assertEquals(Const.MAX_SHORT, new LDC_W(Const.MAX_SHORT).getIndex());
        assertThrows(ClassGenException.class, () -> new LDC_W(Const.MAX_SHORT + 1));
    }

    @Test
    void testIincRejectsIndexAboveU2() {
        assertEquals(Const.MAX_SHORT, new IINC(Const.MAX_SHORT, 1).getIndex());
        assertThrows(ClassGenException.class, () -> new IINC(Const.MAX_SHORT + 1, 1));
    }

    @Test
    void testRetRejectsIndexAboveU2() {
        assertEquals(Const.MAX_SHORT, new RET(Const.MAX_SHORT).getIndex());
        assertThrows(ClassGenException.class, () -> new RET(Const.MAX_SHORT + 1));
    }
}
