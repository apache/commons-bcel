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

import org.junit.jupiter.api.Test;

public class VerifierInvokeTestCase extends AbstractVerifierTestCase {

    @Test
    public void testLegalInvokeInterface() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeInterface01", "Verification of invokeinterface on method defined in superinterface must pass.");
    }

    @Test
    public void testLegalInvokeSpecial() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeSpecial01", "Verification of invokespecial on method defined in superclass must pass.");
        assertVerifyOK("TestLegalInvokeSpecial02", "Verification of invokespecial on method defined in superclass must pass.");
    }

    @Test
    public void testLegalInvokeStatic() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeStatic01", "Verification of invokestatic on method defined in superclass must pass.");
    }

    @Test
    public void testLegalInvokeVirtual() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeVirtual01", "Verification of invokevirtual on method defined in superclass must pass.");
        assertVerifyOK("TestLegalInvokeVirtual02", "Verification of invokevirtual on method defined in superinterface must pass.");
    }
}
