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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.AfterEach;

public abstract class AbstractVerifierTestCase {

    public static final String TEST_PACKAGE = AbstractVerifierTestCase.class.getPackage().getName() + ".tests.";

    @AfterEach
    public void afterEach() {
        AbstractTestCase.clear();
    }

    /**
     * Asserts that the verification of the given class is OK. If it isn't it throws an AssertionFailedError with the given
     * message.
     *
     * @param className simple class name of the class to verify
     * @param message message displayed if assertion fails
     */
    public void assertVerifyOK(final String className, final String message) throws ClassNotFoundException {
        final String testClassName = TEST_PACKAGE + className;
        assertTrue(doAllPasses(testClassName), message);
    }

    /**
     * Asserts that the verification of the given class is rejected. If it isn't it throws an AssertionFailedError with the
     * given message.
     *
     * @param className simple class name of the class to verify
     * @param message message displayed if assertion fails
     */
    public void assertVerifyRejected(final String className, final String message) throws ClassNotFoundException {
        final String testClassname = TEST_PACKAGE + className;
        assertFalse(doAllPasses(testClassname), message);
    }

    /**
     * Executes all the verification on the given class.
     *
     * @param className name of the class to verify
     * @return false if the verification fails, true otherwise
     */
    public boolean doAllPasses(final String className) throws ClassNotFoundException {
        final JavaClass jc = Repository.lookupClass(className);
        final int nbMethods = jc.getMethods().length;

        final Verifier verifier = VerifierFactory.getVerifier(className);
        VerificationResult result = verifier.doPass1();
        if (result.getStatus() != VerificationResult.VERIFIED_OK) {
            return false;
        }

        result = verifier.doPass2();
        if (result.getStatus() != VerificationResult.VERIFIED_OK) {
            return false;
        }

        for (int i = nbMethods; --i >= 0;) {
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
