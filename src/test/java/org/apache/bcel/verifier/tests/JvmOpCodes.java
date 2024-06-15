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

package org.apache.bcel.verifier.tests;

import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.DREM;
import org.apache.bcel.generic.DUP2;
import org.apache.bcel.generic.DUP2_X1;
import org.apache.bcel.generic.DUP2_X2;
import org.apache.bcel.generic.DUP_X2;
import org.apache.bcel.generic.FADD;
import org.apache.bcel.generic.FNEG;
import org.apache.bcel.generic.FREM;
import org.apache.bcel.generic.FSUB;
import org.apache.bcel.generic.IUSHR;
import org.apache.bcel.generic.LAND;
import org.apache.bcel.generic.LNEG;
import org.apache.bcel.generic.LOR;
import org.apache.bcel.generic.LSHL;
import org.apache.bcel.generic.LUSHR;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.POP2;

/*
 * A selection of JVM opcodes, each one in a minimal method.
 */
public class JvmOpCodes {

    long l1, l2;

    /**
     * Tests {@link DNEG}.
     */
    double dneg(final double a) {
        return -a;
    }

    /**
     * Tests {@link DREM}.
     */
    double drem(final double a, final double b) {
        return a % b;
    }

    /**
     * Tests {@link DUP2}.
     */
    long dup2(long a) {
        return a = a + 1;
    }

    /**
     * Tests {@link DUP2_X1}.
     */
    void dup2x1(final String[] s) {
        s[0] += "s"; // Form 1
        l2 = l1 = 1; // Form 2
    }

    /**
     * Tests {@link DUP2_X2}.
     */
    long dup2x2(final long[] array, final int i, final long l) {
        return array[i] = l;
    }

    /**
     * Tests {@link DUP_X2}.
     */
    int dupx2(final int[] array, final int i, final int l) {
        return array[i] = l;
    }

    /**
     * Tests {@link FADD}.
     */
    float fadd(final float a, final float b) {
        return a + b;
    }

    /**
     * Tests {@link FNEG}.
     */
    float fneg(final float a) {
        return -a;
    }

    /**
     * Tests {@link FREM}.
     */
    float frem(final float a, final float b) {
        return a % b;
    }

    /**
     * Tests {@link FSUB}.
     */
    float fsub(final float a, final float b) {
        return a - b;
    }

    /**
     * Tests {@link IUSHR}.
     */
    int iushr(final int a, final int b) {
        return a >>> b;
    }

    /**
     * Tests {@link LAND}.
     */
    long land(final long a, final long b) {
        return a & b;
    }

    /**
     * Tests {@link LNEG}.
     */
    long lneg(final long a) {
        return -a;
    }

    /**
     * Tests {@link LOR}.
     */
    long lor(final long a, final long b) {
        return a | b;
    }

    /**
     * Tests {@link LSHL}.
     */
    long lshl(final long a, final long b) {
        return a << b;
    }

    /**
     * Tests {@link LUSHR}.
     */
    long lushr(final long a, final long b) {
        return a >>> b;
    }

    /**
     * Tests {@link POP}.
     */
    void pop() {
        Math.round(0.5f);
    }

    /**
     * Tests {@link POP2}.
     */
    void pop2() {
        Math.round(0.5d);
    }
}
