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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link PMGClass}.
 */
class PMGClassTest {

    private ConstantPool newConstantPool() {
        return new ConstantPool(new ConstantUtf8("PMG"), new ConstantUtf8("pmg-value"), new ConstantUtf8("pmg.Class"));
    }

    /**
     * A PMG attribute body is exactly two u2 values; a mismatched declared length would let the remainder of the
     * attribute be reparsed as subsequent class file structures, diverging from a JVM that skips the attribute by its
     * declared length.
     */
    @Test
    void testDeclaredLengthMustMatchContent() {
        assertThrowsExactly(ClassFormatException.class, () -> new PMGClass(1, 8, 2, 3, newConstantPool()));
        assertThrowsExactly(ClassFormatException.class, () -> new PMGClass(1, 0, 2, 3, newConstantPool()));
    }

    @Test
    void testValidLength() {
        assertEquals(4, new PMGClass(1, 4, 2, 3, newConstantPool()).getLength());
    }
}
