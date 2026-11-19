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

package org.apache.bcel.verifier;

import java.io.IOException;

import org.apache.bcel.Const;
import org.apache.bcel.verifier.tests.TestFieldAccess01Creator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VerifierFieldAccessTest extends AbstractVerifierTest {

    @ParameterizedTest
    @ValueSource(shorts = { Const.GETFIELD, Const.PUTFIELD })
    void testFieldAccessOnUnrelatedObjectref(final short opcode) throws IOException, ClassNotFoundException {
        final TestFieldAccess01Creator creator = new TestFieldAccess01Creator(opcode, false);
        creator.create();
        assertVerifyRejected(creator.getSimpleClassName(), "Verification of " + Const.getOpcodeName(opcode) + " on a java.lang.String objectref must fail.");
    }

    @ParameterizedTest
    @ValueSource(shorts = { Const.GETFIELD, Const.PUTFIELD })
    void testFieldAccessOnDeclaringClassObjectref(final short opcode) throws IOException, ClassNotFoundException {
        final TestFieldAccess01Creator creator = new TestFieldAccess01Creator(opcode, true);
        creator.create();
        assertVerifyOK(creator.getSimpleClassName(), "Verification of " + Const.getOpcodeName(opcode) + " on an objectref of the declaring class must pass.");
    }
}
