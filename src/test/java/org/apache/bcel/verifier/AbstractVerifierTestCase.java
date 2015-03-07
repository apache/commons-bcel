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

import junit.framework.TestCase;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public abstract class AbstractVerifierTestCase extends TestCase {

    public static final String TEST_PACKAGE = "org.apache.bcel.verifier.tests.";

    /**
     * Asserts that the verification of the given class is OK. If it isn't it throws an AssertionFailedError with the given message.
     *
     * @param classname simple classname of the class to verify
     * @param message   message displayed if assertion fails
     */
    public void assertVerifyOK(String classname, String message) {
        final String testClassname = TEST_PACKAGE + classname;
        assertTrue(message, doAllPasses(testClassname));
    }

    /**
     * Asserts that the verification of the given class is rejected. If it isn't it throws an AssertionFailedError with the given message.
     *
     * @param classname simple classname of the class to verify
     * @param message   message displayed if assertion fails
     */
    public void assertVerifyRejected(String classname, String message) {
        final String testClassname = TEST_PACKAGE + classname;
        assertFalse(message, doAllPasses(testClassname));
    }

    /**
     * Executes all the verification on the given class.
     *
     * @param classname name of the class to verify
     * @return false if the verification fails, true otherwise
     */
    public boolean doAllPasses(String classname) {
        int nbMethods = 0;

        try {
            JavaClass jc = Repository.lookupClass(classname);
            nbMethods = jc.getMethods().length;
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
            return false;
        }

        Verifier verifier = VerifierFactory.getVerifier(classname);
        VerificationResult result = verifier.doPass1();
        if (result.getStatus() != VerificationResult.VERIFIED_OK) {
            return false;
        }

        result = verifier.doPass2();
        if (result.getStatus() != VerificationResult.VERIFIED_OK) {
            return false;
        }

        for (int i = nbMethods; --i >= 0; ) {
            result = verifier.doPass3a(i);
            if (result.getStatus() != VerificationResult.VERIFIED_OK) {
                return false;
            }
            result = verifier.doPass3b(i);
            if (result.getStatus() != VerificationResult.VERIFIED_OK) {
                return false;
            }
        }

        return true;
    }

}
