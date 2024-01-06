/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.bcel.verifier.structurals;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.LDC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class InstConstraintVisitorTestCase {
    private ConstantPoolGen cp;

    @ParameterizedTest
    @MethodSource("org.apache.bcel.verifier.statics.Pass3aVerifierTestCase#constantsNotSupportedByLdc")
    public void rejectLdcConstantModule(final Constant constant) {
        final InstConstraintVisitor visitor = new InstConstraintVisitor();

        cp = mock(ConstantPoolGen.class);
        when(cp.getConstant(0)).thenReturn(constant);

        visitor.setConstantPoolGen(cp);

        final LDC ldc = new LDC(0);

        assertThatCode(() -> visitor.visitLDC(ldc)).hasMessageStartingWith("Instruction LDC constraint violated: Referenced constant should be a");
    }

    @BeforeEach
    public void setup() {
        cp = mock(ConstantPoolGen.class);
    }
}
