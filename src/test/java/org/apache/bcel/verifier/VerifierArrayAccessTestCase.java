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

import org.apache.bcel.verifier.tests.TestArrayAccess02Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess03Creator;
import org.apache.bcel.verifier.tests.TestArrayAccess04DoubleCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04FloatCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04IntCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04LongCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04ShortCreator;
import org.apache.bcel.verifier.tests.TestArrayAccess04UnknownCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VerifierArrayAccessTestCase extends AbstractVerifierTestCase {

    @Test
    public void testInvalidArrayAccess() throws IOException, ClassNotFoundException {
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
        Assertions.assertThrowsExactly(IllegalArgumentException.class, testArrayAccess04UnknownCreator::create, "Invalid type <unknown object>");
    }

    @Test
    public void testValidArrayAccess() throws IOException, ClassNotFoundException {
        assertVerifyOK("TestArrayAccess01", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
        new TestArrayAccess02Creator().create();
        assertVerifyOK("TestArrayAccess02", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
    }

}
