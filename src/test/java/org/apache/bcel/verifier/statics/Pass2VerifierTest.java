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

package org.apache.bcel.verifier.statics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;

import org.apache.bcel.Const;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class Pass2VerifierTest {

    @AfterEach
    void afterEach() {
        VerifierFactory.clear();
        Repository.clearCache();
    }

    /**
     * Tests that a referenced exception class with a circular superclass hierarchy is rejected instead of looping forever.
     */
    @Test
    void testCyclicExceptionSuperclassChainRejected() {
        final ClassGen cgA = new ClassGen("Pass2CyclicA", "Pass2CyclicB", "Pass2CyclicA.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[0]);
        final ClassGen cgB = new ClassGen("Pass2CyclicB", "Pass2CyclicA", "Pass2CyclicB.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[0]);
        Repository.addClass(cgA.getJavaClass());
        Repository.addClass(cgB.getJavaClass());
        final String className = "Pass2CyclicX";
        final ClassGen cg = new ClassGen(className, "java.lang.Object", "Pass2CyclicX.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[0]);
        final InstructionList il = new InstructionList();
        il.append(InstructionFactory.createReturn(Type.VOID));
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[0], "test", className, il,
                cg.getConstantPool());
        mg.addException("Pass2CyclicA");
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());
        Repository.addClass(cg.getJavaClass());
        final Verifier verifier = VerifierFactory.getVerifier(className);
        assertEquals(VerificationResult.VR_OK, verifier.doPass1());
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> assertEquals(VerificationResult.VERIFIED_REJECTED, verifier.doPass2().getStatus()));
    }

    /**
     * Tests that we do not break binary compatibility with BCEL-330.
     */
    @Test
    void testReferenceToConstant() {
        @SuppressWarnings("unused")
        final short referenceToConstant = Constants.AALOAD;
    }
}
