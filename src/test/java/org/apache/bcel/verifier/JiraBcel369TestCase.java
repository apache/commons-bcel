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

import org.apache.bcel.AbstractTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests BCEL-369.
 */
public class JiraBcel369TestCase extends AbstractTestCase {

    @Test
    public void testCompileAndVerify() throws ClassNotFoundException {
        Verifier.verifyType(org.apache.bcel.verifier.JiraBcel369TestFixture.class.getName());
    }

    /**
     * 3.7.0 fails with
     * <pre>{@code
     * org.apache.bcel.verifier.exc.AssertionViolatedException: INTERNAL ERROR: Some RuntimeException occurred while verify()ing class 'issue369.Test', method 'public void <init>(int arg1)'. Original RuntimeException's stack trace:
---
org.apache.bcel.verifier.exc.AssertionViolatedException: INTERNAL ERROR: outFrame not set! This:
  24: return    [InstructionContext]
ExecutionChain:   24: return    [InstructionContext]
OutFrames: '{   7: jsr -> 19    [InstructionContext]=Local Variables:
0: issue369.Test
1: int
2: <unknown object>
3: <return address targeting   10: goto[167](3) -> 34>
4: <unknown object>
OperandStack:
Slots used: 0 MaxStack: 1.
,   14: jsr -> 19   [InstructionContext]=Local Variables:
0: issue369.Test
1: int
2: java.lang.Throwable
3: <return address targeting   17: aload_2[44](1)>
4: <unknown object>
OperandStack:
Slots used: 0 MaxStack: 1.
}'.
    at org.apache.bcel.verifier.structurals.ControlFlowGraph$InstructionContextImpl.getOutFrame(ControlFlowGraph.java:282)
    at org.apache.bcel.verifier.structurals.Pass3bVerifier.circulationPump(Pass3bVerifier.java:275)
    at org.apache.bcel.verifier.structurals.Pass3bVerifier.do_verify(Pass3bVerifier.java:386)
    at org.apache.bcel.verifier.PassVerifier.verify(PassVerifier.java:98)
    at org.apache.bcel.verifier.Verifier.doPass3b(Verifier.java:166)
    at org.apache.bcel.verifier.Verifier.verifyType(Verifier.java:90)
    at org.apache.bcel.verifier.JiraBcel369TestCase.testCompiledClass(JiraBcel369TestCase.java:35)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
    ...
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)
---

    at org.apache.bcel.verifier.structurals.Pass3bVerifier.do_verify(Pass3bVerifier.java:398)
    at org.apache.bcel.verifier.PassVerifier.verify(PassVerifier.java:98)
    at org.apache.bcel.verifier.Verifier.doPass3b(Verifier.java:166)
    at org.apache.bcel.verifier.Verifier.verifyType(Verifier.java:90)
    at org.apache.bcel.verifier.JiraBcel369TestCase.testCompiledClass(JiraBcel369TestCase.java:35)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
    ...
    at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)
Caused by: org.apache.bcel.verifier.exc.AssertionViolatedException: INTERNAL ERROR: outFrame not set! This:
  24: return    [InstructionContext]
ExecutionChain:   24: return    [InstructionContext]
OutFrames: '{   7: jsr -> 19    [InstructionContext]=Local Variables:
0: issue369.Test
1: int
2: <unknown object>
3: <return address targeting   10: goto[167](3) -> 34>
4: <unknown object>
OperandStack:
Slots used: 0 MaxStack: 1.
,   14: jsr -> 19   [InstructionContext]=Local Variables:
0: issue369.Test
1: int
2: java.lang.Throwable
3: <return address targeting   17: aload_2[44](1)>
4: <unknown object>
OperandStack:
Slots used: 0 MaxStack: 1.
}'.
    at org.apache.bcel.verifier.structurals.ControlFlowGraph$InstructionContextImpl.getOutFrame(ControlFlowGraph.java:282)
    at org.apache.bcel.verifier.structurals.Pass3bVerifier.circulationPump(Pass3bVerifier.java:275)
    at org.apache.bcel.verifier.structurals.Pass3bVerifier.do_verify(Pass3bVerifier.java:386)
    ... 74 more
     * }</pre>
     *
     * @throws ClassNotFoundException
     */
    @Test
    @Disabled
    public void testCompiledClass() throws ClassNotFoundException {
        Verifier.verifyType("issue369.Test");
    }

}
