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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class VerifyJavaHomesTestCase extends AbstractVerifierTestCase {

    static int count;

    boolean logStep = Boolean.getBoolean("BCEL.logStep");
    boolean logQuiet = Boolean.getBoolean("BCEL.logQuiet");

    /**
     * Eventually runs out of memory? Super now calls VerifierFactory.clear();
     *
     * @param name
     * @throws ClassNotFoundException
     */
    @Disabled("Run once in a while, it takes a very long time.")
    @ParameterizedTest
    @MethodSource("org.apache.bcel.generic.JavaHome#streamJarEntryClassName")
    public void testJarEntryClassName(final String name) throws ClassNotFoundException {
        // System.out.println(jarEntry.getName());
        // Skip $ classes for now
        count++;
        if (logStep) {
            System.out.printf("%,d %s%n", count, name);
        } else if (!logQuiet) {
            if (count % 10 == 0) {
                System.out.print('.');
            }
            if (count % 800 == 0) {
                System.out.println();
                System.out.print(count);
            }
        }
        if (!name.contains("$")) {
            assertTrue(doAllPasses(name));
        }
    }

}
