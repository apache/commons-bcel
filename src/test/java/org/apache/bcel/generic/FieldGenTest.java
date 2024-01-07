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

package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FieldGen}.
 */
public class FieldGenTest {

    @Test
    @Disabled
    public void testBCELComparator() throws Exception {
        final ConstantLong[] constantPool = { new ConstantLong(0), new ConstantLong(0) };
        final FieldGen obj = new FieldGen(new Field(0, 0, 0, null, new ConstantPool(constantPool)), new ConstantPoolGen(constantPool));
        assertTrue(FieldGen.getComparator().equals(null, null));
        assertTrue(FieldGen.getComparator().equals(obj, obj));
        assertFalse(FieldGen.getComparator().equals(obj, null));
        assertFalse(FieldGen.getComparator().equals(null, obj));
    }
}
