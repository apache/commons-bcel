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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ConstantDynamic}.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-4.html#jvms-4.4.10">JVM CONSTANT_Dynamic_info specification</a>
 */
class ConstantDynamicTest {

    /**
     * Both index fields of {@code CONSTANT_Dynamic_info} are unsigned shorts (u2), so a value above 0x7FFF must not be read as a
     * negative number, matching {@link ConstantInvokeDynamic}.
     */
    @Test
    void testReadConstantReadsIndicesUnsigned() throws IOException {
        // CONSTANT_Dynamic, bootstrap_method_attr_index 0x9C40 (40000), name_and_type_index 0xABCD (43981)
        final byte[] data = {Const.CONSTANT_Dynamic, (byte) 0x9C, 0x40, (byte) 0xAB, (byte) 0xCD};
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
            final ConstantDynamic c = (ConstantDynamic) Constant.readConstant(dis);
            assertEquals(40000, c.getBootstrapMethodAttrIndex());
            assertEquals(43981, c.getNameAndTypeIndex());
        }
    }
}
