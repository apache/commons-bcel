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

package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link FieldGen}.
 */
class FieldGenTest {

    @Test
    @Disabled
    void testBCELComparator() throws Exception {
        final ConstantLong[] constantPool = { new ConstantLong(0), new ConstantLong(0) };
        final FieldGen obj = new FieldGen(new Field(0, 0, 0, null, new ConstantPool(constantPool)), new ConstantPoolGen(constantPool));
        assertTrue(FieldGen.getComparator().equals(null, null));
        assertTrue(FieldGen.getComparator().equals(obj, obj));
        assertFalse(FieldGen.getComparator().equals(obj, null));
        assertFalse(FieldGen.getComparator().equals(null, obj));
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testBooleanInitValue(final boolean value) {
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.BOOLEAN, "VALUE", new ConstantPoolGen());
        fg.setInitValue(value);
        final Field field = fg.getField();
        assertNotNull(field.getConstantValue());
        assertEquals(value ? 1 : 0, field.getConstantPool().getConstantInteger(field.getConstantValue().getConstantValueIndex()).getBytes());
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testBooleanInitValueReplacement(final boolean value) {
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.BOOLEAN, "VALUE", new ConstantPoolGen());
        fg.setInitValue(!value);
        fg.setInitValue(value);
        final Field field = fg.getField();
        assertNotNull(field.getConstantValue());
        assertEquals(value ? 1 : 0, field.getConstantPool().getConstantInteger(field.getConstantValue().getConstantValueIndex()).getBytes());
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testCancelBooleanInitValue(final boolean value) {
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.BOOLEAN, "VALUE", new ConstantPoolGen());
        fg.setInitValue(value);
        fg.cancelInitValue();
        assertNull(fg.getField().getConstantValue());
    }
}
