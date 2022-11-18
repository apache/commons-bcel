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

import org.junit.jupiter.api.Test;

public class ArrayTypeTest {

    @Test
    public void testGetBasicType() {
        final BasicType type = Type.BYTE;
        final ArrayType objectType = new ArrayType(type, 1);
        assertEquals(type, objectType.getBasicType());
    }

    @Test
    public void testGetClassName() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 1);
        assertEquals("[B", objectType.getClassName());
        assertEquals(byte[].class.getName(), objectType.getClassName());
    }

    @Test
    public void testGetDimensions() {
        final int dimensions = 1;
        final ArrayType objectType = new ArrayType(Type.BYTE, dimensions);
        assertEquals(dimensions, objectType.getDimensions());
    }

    @Test
    public void testGetElementType() {
        final BasicType type = Type.BYTE;
        final ArrayType objectType = new ArrayType(Type.BYTE, 1);
        assertEquals(type, objectType.getElementType());
    }

    @Test
    public void testGetSignatureDim1() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 1);
        assertEquals("[B", objectType.getSignature());
        assertEquals(byte[].class.getName(), objectType.getSignature());
    }

    @Test
    public void testGetSignatureDim2() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 2);
        assertEquals("[[B", objectType.getSignature());
        assertEquals(byte[][].class.getName(), objectType.getSignature());
    }

    @Test
    public void testGetSignatureDim4() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 4);
        assertEquals("[[[[B", objectType.getSignature());
        assertEquals(byte[][][][].class.getName(), objectType.getSignature());
    }

    @Test
    public void testGetSize() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 1);
        assertEquals(1, objectType.getSize());
    }

    @Test
    public void testGetType() {
        final ArrayType objectType = new ArrayType(Type.BYTE, 1);
        assertEquals(13, objectType.getType());
    }

}
