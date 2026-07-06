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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link LocalVariableTable#getLocalVariable(int, int)}.
 */
class LocalVariableTableTest {

    /**
     * The live range of a local variable is the half-open interval {@code [start_pc, start_pc + length)} per JVMS 4.7.13, so a
     * pc equal to {@code start_pc + length} is out of scope and must not match.
     */
    @Test
    void testGetLocalVariableLiveRangeEndIsExclusive() {
        final ConstantPool constantPool = new ConstantPool(new Constant[1]);
        // Variable in slot 1, live at pcs 2, 3, 4 (start 2, length 3).
        final LocalVariable variable = new LocalVariable(2, 3, 0, 0, 1, constantPool);
        final LocalVariableTable table = new LocalVariableTable(0, 0, new LocalVariable[] {variable}, constantPool);
        assertNull(table.getLocalVariable(1, 1), "pc before start");
        assertSame(variable, table.getLocalVariable(1, 2), "pc at start");
        assertSame(variable, table.getLocalVariable(1, 4), "pc at last valid position");
        assertNull(table.getLocalVariable(1, 5), "pc at start + length is out of scope");
    }
}
