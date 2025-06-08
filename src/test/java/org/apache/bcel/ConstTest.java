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

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Const}.
 */
class ConstTest {

    @Test
    void testJava22() throws Exception {
        assertEquals(66, Const.MAJOR_22);
        assertEquals(0, Const.MINOR_22);
    }

    @Test
    void testJava23() throws Exception {
        assertEquals(67, Const.MAJOR_23);
        assertEquals(0, Const.MINOR_23);
    }

    @Test
    void testJava24() throws Exception {
        assertEquals(68, Const.MAJOR_24);
        assertEquals(0, Const.MINOR_24);
    }

    @Test
    void testJava25() throws Exception {
        assertEquals(69, Const.MAJOR_25);
        assertEquals(0, Const.MINOR_25);
    }
}
