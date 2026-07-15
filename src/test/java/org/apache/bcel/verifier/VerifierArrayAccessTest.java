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

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.tests.TestArrayAccess02Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess03Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess04DoubleCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04FloatCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04IntCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04LongCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04ShortCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04UnknownCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess05Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess06Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess07Creator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VerifierArrayAccessTest extends AbstractVerifierTest {

    /**
     * Array load and store instructions applied to an array whose component type does not match the instruction.
     * {@code caload} and {@code iaload} have their own tests, {@link org.apache.bcel.verifier.tests.TestArrayAccess06Creator} and
     * {@link org.apache.bcel.verifier.tests.TestArrayAccess05Creator}.
     */
    static Stream<Arguments> invalidComponentTypeArrayAccess() {
        return Stream.of(
        // @formatter:off
            Arguments.of(InstructionConst.AALOAD, Type.INT),
            Arguments.of(InstructionConst.AASTORE, Type.INT),
            Arguments.of(InstructionConst.BALOAD, Type.INT),
            Arguments.of(InstructionConst.BASTORE, Type.INT),
            Arguments.of(InstructionConst.CASTORE, Type.INT),
            Arguments.of(InstructionConst.DALOAD, Type.INT),
            Arguments.of(InstructionConst.DASTORE, Type.INT),
            Arguments.of(InstructionConst.FALOAD, Type.INT),
            Arguments.of(InstructionConst.FASTORE, Type.INT),
            Arguments.of(InstructionConst.IASTORE, Type.CHAR),
            Arguments.of(InstructionConst.LALOAD, Type.INT),
            Arguments.of(InstructionConst.LASTORE, Type.INT),
            Arguments.of(InstructionConst.SALOAD, Type.INT),
            Arguments.of(InstructionConst.SASTORE, Type.INT));
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("invalidComponentTypeArrayAccess")
    void testInvalidComponentTypeArrayAccess(final ArrayInstruction arrayInstruction, final Type arrayElementType)
        throws IOException, ClassNotFoundException {
        final TestArrayAccess07Creator creator = new TestArrayAccess07Creator(arrayInstruction, arrayElementType);
        creator.create();
        assertVerifyRejected(creator.getSimpleClassName(),
            "Verification of " + arrayInstruction.getName() + " applied to a " + arrayElementType + "[] must fail.");
    }

    @Test
    void testInvalidArrayAccess() throws IOException, ClassNotFoundException {
        new TestArrayAccess03Creator().create();
        assertVerifyRejected("TestArrayAccess03", "Verification of an arraystore instruction on an object must fail.");
        new TestArrayAccess04IntCreator().create();
        assertVerifyRejected("TestArrayAccess04Int", "Verification of an arraystore instruction of an int on an array of references must fail.");
        new TestArrayAccess04FloatCreator().create();
        assertVerifyRejected("TestArrayAccess04Float", "Verification of an arraystore instruction of a float on an array of references must fail.");
        new TestArrayAccess04DoubleCreator().create();
        assertVerifyRejected("TestArrayAccess04Double", "Verification of an arraystore instruction of a double on an array of references must fail.");
        new TestArrayAccess04LongCreator().create();
        assertVerifyRejected("TestArrayAccess04Long", "Verification of an arraystore instruction of a long on an array of references must fail.");
        new TestArrayAccess04ShortCreator().create();
        assertVerifyRejected("TestArrayAccess04Short", "Verification of an arraystore instruction of a short on an array of references must fail.");
        final TestArrayAccess04UnknownCreator testArrayAccess04UnknownCreator = new TestArrayAccess04UnknownCreator();
        assertThrowsExactly(IllegalArgumentException.class, testArrayAccess04UnknownCreator::create, "Invalid type <unknown object>");
        new TestArrayAccess05Creator().create();
        assertVerifyRejected("TestArrayAccess05", "Verification of iaload applied to a multidimensional int[][] must fail.");
        new TestArrayAccess06Creator().create();
        assertVerifyRejected("TestArrayAccess06", "Verification of caload applied to an int[] must fail.");
    }

    @Test
    void testValidArrayAccess() throws IOException, ClassNotFoundException {
        assertVerifyOK("TestArrayAccess01", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
        new TestArrayAccess02Creator().create();
        assertVerifyOK("TestArrayAccess02", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
    }

}
