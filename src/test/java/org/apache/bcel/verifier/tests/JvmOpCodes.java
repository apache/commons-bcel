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

/*
 * A selection of JVM opcodes, each one in a minimal method.
 */
public class JvmOpCodes {

    long l1, l2;

    void dup2x1(String[] s) {
        s[0] += "s"; // Form 1
        l2 = l1 = 1; // Form 2
    }

    long dup2(long a) {
        return a = a + 1;
    }

    long dup2x2(long[] array, int i, long l) {
        return array[i] = l;
    }

    int dupx2(int[] array, int i, int l) {
        return array[i] = l;
    }

    long lneg(long a) {
        return -a;
    }

    long lor(long a, long b) {
        return a | b;
    }

    long land(long a, long b) {
        return a & b;
    }

    long lushr(long a, long b) {
        return a >>> b;
    }

    int iushr(int a, int b) {
        return a >>> b;
    }

    long lshl(long a, long b) {
        return a << b;
    }

    float fsub(float a, float b) {
        return a - b;
    }

    float fadd(float a, float b) {
        return a + b;
    }

    float frem(float a, float b) {
        return a % b;
    }

    float fneg(float a) {
        return -a;
    }

    double drem(double a, double b) {
        return a % b;
    }

    double dneg(double a) {
        return -a;
    }

    void pop() {
        Math.round(0.5f);
    }

    void pop2() {
        Math.round(0.5d);
    }
}
