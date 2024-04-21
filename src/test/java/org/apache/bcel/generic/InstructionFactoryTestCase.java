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

import static org.apache.bcel.generic.InstructionFactory.createArrayLoad;
import static org.apache.bcel.generic.InstructionFactory.createArrayStore;
import static org.apache.bcel.generic.InstructionFactory.createBinaryOperation;
import static org.apache.bcel.generic.InstructionFactory.createBranchInstruction;
import static org.apache.bcel.generic.InstructionFactory.createLoad;
import static org.apache.bcel.generic.InstructionFactory.createNull;
import static org.apache.bcel.generic.InstructionFactory.createReturn;
import static org.apache.bcel.generic.InstructionFactory.createStore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.junit.jupiter.api.Test;

public class InstructionFactoryTestCase extends AbstractTestCase {

    @Test
    public void testArrayLoad() throws Exception {
        assertEquals(InstructionConst.BALOAD, createArrayLoad(Type.BOOLEAN));
        assertEquals(InstructionConst.BALOAD, createArrayLoad(Type.BYTE));
        assertEquals(InstructionConst.CALOAD, createArrayLoad(Type.CHAR));
        assertEquals(InstructionConst.SALOAD, createArrayLoad(Type.SHORT));
        assertEquals(InstructionConst.IALOAD, createArrayLoad(Type.INT));
        assertEquals(InstructionConst.FALOAD, createArrayLoad(Type.FLOAT));
        assertEquals(InstructionConst.DALOAD, createArrayLoad(Type.DOUBLE));
        assertEquals(InstructionConst.LALOAD, createArrayLoad(Type.LONG));
        assertEquals(InstructionConst.AALOAD, createArrayLoad(Type.OBJECT));
        assertEquals(InstructionConst.AALOAD, createArrayLoad(Type.getType("[I")));
    }

    @Test
    public void testArrayStore() throws Exception {
        assertEquals(InstructionConst.BASTORE, createArrayStore(Type.BOOLEAN));
        assertEquals(InstructionConst.BASTORE, createArrayStore(Type.BYTE));
        assertEquals(InstructionConst.CASTORE, createArrayStore(Type.CHAR));
        assertEquals(InstructionConst.SASTORE, createArrayStore(Type.SHORT));
        assertEquals(InstructionConst.IASTORE, createArrayStore(Type.INT));
        assertEquals(InstructionConst.FASTORE, createArrayStore(Type.FLOAT));
        assertEquals(InstructionConst.DASTORE, createArrayStore(Type.DOUBLE));
        assertEquals(InstructionConst.LASTORE, createArrayStore(Type.LONG));
        assertEquals(InstructionConst.AASTORE, createArrayStore(Type.OBJECT));
        assertEquals(InstructionConst.AASTORE, createArrayStore(Type.getType("[I")));
    }

    @Test
    public void testCreateInvokeNullArgTypes() throws Exception {
        InstructionFactory factory = new InstructionFactory(new ClassGen(Repository.lookupClass(Object.class)));
        factory.createInvoke("", "", Type.VOID, null, Const.INVOKESPECIAL, false); // Mustn't throw an NPE
    }

    @Test
    public void testExceptions() throws Exception {
        final InstructionFactory factory = new InstructionFactory(new ClassGen(Repository.lookupClass(Object.class)));
        assertThrowsExactly(IllegalArgumentException.class, () -> createArrayLoad(Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> createArrayStore(Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBinaryOperation("$", Type.DOUBLE));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBinaryOperation("$", Type.FLOAT));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBinaryOperation("$", Type.INT));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBinaryOperation("$", Type.LONG));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBinaryOperation("*", Type.OBJECT));
        assertThrowsExactly(IllegalArgumentException.class, () -> createBranchInstruction(Short.MIN_VALUE, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> createLoad(Type.UNKNOWN, 0));
        assertThrowsExactly(IllegalArgumentException.class, () -> createNull(Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> createReturn(Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> createStore(Type.UNKNOWN, 0));
        assertThrowsExactly(IllegalArgumentException.class, () -> factory.createAppend(Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> factory.createCast(Type.UNKNOWN, Type.UNKNOWN));
        assertThrowsExactly(IllegalArgumentException.class, () -> factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Const.NOP));
        assertThrowsExactly(IllegalArgumentException.class, () -> factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.STRING }, Const.NOP));
    }

    @Test
    public void testNull() throws Exception {
        assertEquals(InstructionConst.ICONST_0, createNull(Type.BOOLEAN));
        assertEquals(InstructionConst.ICONST_0, createNull(Type.BYTE));
        assertEquals(InstructionConst.ICONST_0, createNull(Type.CHAR));
        assertEquals(InstructionConst.ICONST_0, createNull(Type.SHORT));
        assertEquals(InstructionConst.ICONST_0, createNull(Type.INT));
        assertEquals(InstructionConst.FCONST_0, createNull(Type.FLOAT));
        assertEquals(InstructionConst.DCONST_0, createNull(Type.DOUBLE));
        assertEquals(InstructionConst.LCONST_0, createNull(Type.LONG));
        assertEquals(InstructionConst.NOP, createNull(Type.VOID));
        assertEquals(InstructionConst.ACONST_NULL, createNull(Type.OBJECT));
        assertEquals(InstructionConst.ACONST_NULL, createNull(Type.getType("[I")));
    }
}
