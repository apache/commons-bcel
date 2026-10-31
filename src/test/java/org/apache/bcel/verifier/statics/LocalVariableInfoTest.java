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

package org.apache.bcel.verifier.statics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;

import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link LocalVariableInfo}.
 */
class LocalVariableInfoTest {

    @Test
    void testConflictingNameRejected() {
        final LocalVariableInfo info = new LocalVariableInfo();
        info.add("a", 0, 10, Type.INT);
        assertThrows(LocalVariableInfoInconsistentException.class, () -> info.add("b", 5, 10, Type.INT));
    }

    @Test
    void testConflictingTypeRejected() {
        final LocalVariableInfo info = new LocalVariableInfo();
        info.add("a", 0, 10, Type.INT);
        assertThrows(LocalVariableInfoInconsistentException.class, () -> info.add("a", 10, 5, Type.FLOAT));
    }

    @Test
    void testConsistentOverlapAccepted() {
        final LocalVariableInfo info = new LocalVariableInfo();
        info.add("a", 0, 10, Type.INT);
        info.add("a", 5, 10, Type.INT);
        assertEquals("a", info.getName(15));
        assertNull(info.getName(16));
    }

    @Test
    void testLookups() {
        final LocalVariableInfo info = new LocalVariableInfo();
        info.add("a", 0, 10, Type.INT);
        info.add("b", 20, 5, Type.FLOAT);
        assertEquals("a", info.getName(0));
        assertEquals("a", info.getName(10));
        assertEquals(Type.INT, info.getType(5));
        assertNull(info.getName(11));
        assertNull(info.getType(19));
        assertEquals("b", info.getName(20));
        assertEquals(Type.FLOAT, info.getType(25));
        assertNull(info.getName(26));
    }

    /**
     * A malicious class file can declare 65,535 maximum-length LocalVariableTable entries for the same slot; processing them must stay proportional to the
     * number of entries, not to entries times offsets.
     */
    @Test
    void testManyMaximumLengthEntriesFinishQuickly() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            final LocalVariableInfo info = new LocalVariableInfo();
            for (int i = 0; i < 65_535; i++) {
                info.add("dup", 0, 65_535, Type.INT);
            }
            assertEquals("dup", info.getName(65_535));
            assertNull(info.getName(65_536));
        });
    }
}
