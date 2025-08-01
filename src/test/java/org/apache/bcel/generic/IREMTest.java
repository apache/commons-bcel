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

import org.junit.jupiter.api.Test;

class IREMTest {
    @Test
    void testAccept() {
        final CountingVisitor countVisitor = new CountingVisitor();
        final IREM irem = new IREM();
        irem.accept(countVisitor);
        final CountingVisitor expected = new CountingVisitor();
        expected.setExceptionThrower(1);
        expected.setTypedInstruction(1);
        expected.setStackProducer(1);
        expected.setStackConsumer(1);
        expected.setArithmeticInstruction(1);
        expected.setIrem(1);
        assertEquals(expected, countVisitor);
    }

}
