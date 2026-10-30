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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.junit.jupiter.api.Test;

/**
 * Writer paths must refuse to emit structure counts that overflow their u2/u1 class-file fields instead of silently truncating them
 * (DataOutputStream.writeShort keeps only the low 16 bits) while still writing every body; the CVE-2022-42920 corruption shape.
 */
class DumpU2OverflowTest {

    @Test
    void testDumpRefusesOverflowingInterfaceCount() throws Exception {
        final JavaClass javaClass = new ClassGen("A", "java.lang.Object", "A.java", Const.ACC_PUBLIC, null).getJavaClass();
        javaClass.setInterfaces(new int[Const.MAX_SHORT + 1]);
        try (DataOutputStream dos = new DataOutputStream(new ByteArrayOutputStream())) {
            assertThrows(ClassFormatException.class, () -> javaClass.dump(dos));
        }
    }

    @Test
    void testSetExceptionTableRefusesOverflowingCount() {
        final JavaClass javaClass = new ClassGen("A", "java.lang.Object", "A.java", Const.ACC_PUBLIC, null).getJavaClass();
        final Code code = new Code(0, 0, 0, 0, new byte[0], null, null, javaClass.getConstantPool());
        assertThrows(ClassFormatException.class, () -> code.setExceptionTable(new CodeException[Const.MAX_SHORT + 1]));
    }
}
