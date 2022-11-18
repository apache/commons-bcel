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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

public class BasicTypeTest {

    @Test
    public void testClassName() {
        assertEquals(boolean.class.getName(), BasicType.getType(Const.T_BOOLEAN).getClassName());
        assertEquals(byte.class.getName(), BasicType.getType(Const.T_BYTE).getClassName());
        assertEquals(char.class.getName(), BasicType.getType(Const.T_CHAR).getClassName());
        assertEquals(double.class.getName(), BasicType.getType(Const.T_DOUBLE).getClassName());
        assertEquals(float.class.getName(), BasicType.getType(Const.T_FLOAT).getClassName());
        assertEquals(int.class.getName(), BasicType.getType(Const.T_INT).getClassName());
        assertEquals(long.class.getName(), BasicType.getType(Const.T_LONG).getClassName());
        assertEquals(short.class.getName(), BasicType.getType(Const.T_SHORT).getClassName());
        assertEquals(void.class.getName(), BasicType.getType(Const.T_VOID).getClassName());
    }

    @Test
    public void testGetType() {
        assertEquals(Type.BYTE, BasicType.getType(Const.T_BYTE));
    }

    @Test
    public void testSignature() {
        assertEquals("Z", BasicType.getType(Const.T_BOOLEAN).getSignature());
        assertEquals("B", BasicType.getType(Const.T_BYTE).getSignature());
        assertEquals("C", BasicType.getType(Const.T_CHAR).getSignature());
        assertEquals("D", BasicType.getType(Const.T_DOUBLE).getSignature());
        assertEquals("F", BasicType.getType(Const.T_FLOAT).getSignature());
        assertEquals("I", BasicType.getType(Const.T_INT).getSignature());
        assertEquals("J", BasicType.getType(Const.T_LONG).getSignature());
        assertEquals("S", BasicType.getType(Const.T_SHORT).getSignature());
        assertEquals("V", BasicType.getType(Const.T_VOID).getSignature());
    }
}

