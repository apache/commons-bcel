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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.apache.bcel.generic.InstructionList;
import org.junit.jupiter.api.Test;

/**
 * Tests BCEL-368.
 */
class Jira368Test {

    private JavaClass parseJavaClass() throws IOException {
        return new ClassParser("src/test/resources/jira368/Jira368.class").parse();
    }

    @Test
    void testInstructionListStringBrief() throws Exception {
        for (final Method method : parseJavaClass().getMethods()) {
            if (!method.isAbstract() && !method.isNative()) {
                final InstructionList instructionList = new InstructionList(method.getCode().getCode());
                final String string = instructionList.toString(false);
                assertNotNull(string);
            }
        }
    }

    @Test
    void testInstructionListStringVerbose() throws Exception {
        for (final Method method : parseJavaClass().getMethods()) {
            if (!method.isAbstract() && !method.isNative()) {
                final InstructionList instructionList = new InstructionList(method.getCode().getCode());
                final String string = instructionList.toString(true);
                assertNotNull(string);
            }
        }
    }

    @Test
    void testMethodSignature() throws Exception {
        final String string = parseJavaClass().toString();
        // System.out.println(string);
        assertNotNull(string);
    }

}
