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
 * 
 */

package org.apache.bcel.verifier;


public class VerifierArrayAccessTestCase extends AbstractVerifierTestCase {
    
    public void testInvalidArrayAccess() {
        assertVerifyRejected("TestArrayAccess03", "Verification of an arraystore instruction on an object must fail.");
        assertVerifyRejected("TestArrayAccess04", "Verification of an arraystore instruction of an int on an array of references must fail.");
    }
    
    public void testValidArrayAccess() {
        assertVerifyOK("TestArrayAccess01", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
        assertVerifyOK("TestArrayAccess02", "Verification of an arraystore instruction on an array that is not compatible with the stored element must pass.");
    }
    
}
