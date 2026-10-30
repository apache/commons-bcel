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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class VerifierFactoryTest {

    @Test
    void testCacheIsBounded() {
        final String previous = System.setProperty(VerifierFactory.MAX_CACHE_SIZE_PROPERTY, "3");
        VerifierFactory.clear();
        try {
            for (int i = 0; i < 10; i++) {
                VerifierFactory.getVerifier("com.example.bcel.Bogus" + i);
            }
            assertEquals(3, VerifierFactory.getVerifiers().length);
        } finally {
            if (previous != null) {
                System.setProperty(VerifierFactory.MAX_CACHE_SIZE_PROPERTY, previous);
            } else {
                System.clearProperty(VerifierFactory.MAX_CACHE_SIZE_PROPERTY);
            }
            VerifierFactory.clear();
        }
    }

    @Test
    void testSameVerifierWhileCached() {
        VerifierFactory.clear();
        try {
            assertSame(VerifierFactory.getVerifier("com.example.bcel.Same"), VerifierFactory.getVerifier("com.example.bcel.Same"));
        } finally {
            VerifierFactory.clear();
        }
    }
}
