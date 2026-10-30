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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TransitiveHull}.
 *
 * @see <a href="https://github.com/apache/commons-bcel/pull/476">PR #476</a>
 */
class TransitiveHullTest {

    @Test
    @Disabled
    void testA() {
        TransitiveHull.main(new String[] { A.class.getName() });
    }

    @Test
    @Disabled
    void testB() {
        TransitiveHull.main(new String[] { B.class.getName() });
    }

    @Test
    @Disabled
    void testC() {
        TransitiveHull.main(new String[] { C.class.getName() });
    }

    @Test
    @Disabled
    void testClassDoesNotExist() {
        TransitiveHull.main(new String[] { "ClassDoesNotExist" });
    }

    @Test
    @Disabled
    void testSelf() {
        TransitiveHull.main(new String[] { TransitiveHull.class.getName() });
    }
}
