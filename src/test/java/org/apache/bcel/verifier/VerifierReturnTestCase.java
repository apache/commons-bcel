/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.bcel.verifier;

import java.io.IOException;

import org.apache.bcel.verifier.tests.TestReturn01Creator;
import org.apache.bcel.verifier.tests.TestReturn03BooleanCreator;
import org.apache.bcel.verifier.tests.TestReturn03ByteCreator;
import org.apache.bcel.verifier.tests.TestReturn03DoubleCreator;
import org.apache.bcel.verifier.tests.TestReturn03FloatCreator;
import org.apache.bcel.verifier.tests.TestReturn03IntCreator;
import org.apache.bcel.verifier.tests.TestReturn03LongCreator;
import org.apache.bcel.verifier.tests.TestReturn03ObjectCreator;
import org.apache.bcel.verifier.tests.TestReturn03UnknownCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VerifierReturnTestCase extends AbstractVerifierTestCase {

    @Test
    public void testInvalidReturn() throws IOException, ClassNotFoundException {
        new TestReturn01Creator().create();
        assertVerifyRejected("TestReturn01", "Verification of a void method that returns an object must fail.");
        new TestReturn03IntCreator().create();
        assertVerifyRejected("TestReturn03Int", "Verification of an int method that returns null int must fail.");
        new TestReturn03FloatCreator().create();
        assertVerifyRejected("TestReturn03Float", "Verification of a int method that returns null float must fail.");
        new TestReturn03DoubleCreator().create();
        assertVerifyRejected("TestReturn03Double", "Verification of a int method that returns null double must fail.");
        new TestReturn03LongCreator().create();
        assertVerifyRejected("TestReturn03Long", "Verification of a int method that returns null long must fail.");
        new TestReturn03ByteCreator().create();
        assertVerifyRejected("TestReturn03Byte", "Verification of a int method that returns null byte must fail.");
        new TestReturn03BooleanCreator().create();
        assertVerifyRejected("TestReturn03Boolean", "Verification of a int method that returns null boolean must fail.");
        new TestReturn03ObjectCreator().create();
        assertVerifyRejected("TestReturn03Object", "Verification of a int method that returns null Object must fail.");
        final TestReturn03UnknownCreator testReturn03UnknownCreator = new TestReturn03UnknownCreator();
        Assertions.assertThrowsExactly(IllegalArgumentException.class, testReturn03UnknownCreator::create, "Invalid type <unknown object>");
    }

    @Test
    public void testValidReturn() throws ClassNotFoundException {
        assertVerifyOK("TestReturn02", "Verification of a method that returns a newly created object must pass.");
        assertVerifyOK("TestArray01", "Verification of a method that returns an array must pass.");
    }
}
