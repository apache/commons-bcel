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

import java.io.DataInput;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Unknown}.
 */
class UnknownTest {

    @Test
    void testAttributeTooLarge() throws IOException {
        final ConstantPool cp = new ConstantPool(new ConstantUtf8("TestAttribute0"), new ConstantUtf8("TestAttribute1"));
        assertThrows(IOException.class, () -> new Unknown(1, Unknown.MAX_LEN + 1, (DataInput) null, cp));
    }
}
