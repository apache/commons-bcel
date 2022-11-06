/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.generic.ClassElementValueGen;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ElementValueGen;
import org.apache.bcel.generic.EnumElementValueGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.SimpleElementValueGen;
import org.junit.jupiter.api.Test;

public class ElementValueGenTestCase extends AbstractTestCase {
    private void checkSerialize(final ElementValueGen evgBefore, final ConstantPoolGen cpg) throws IOException {
        final String beforeValue = evgBefore.stringifyValue();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            evgBefore.dump(dos);
            dos.flush();
        }
        ElementValueGen evgAfter;
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            evgAfter = ElementValueGen.readElementValue(dis, cpg);
        }
        final String afterValue = evgAfter.stringifyValue();
        assertEquals(beforeValue, afterValue, "Deserialization failed");
    }

    private ClassGen createClassGen(final String className) {
        return new ClassGen(className, "java.lang.Object", "<generated>", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
    }

    @Test
    public void testCreateBooleanElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_BOOLEAN, cp, true);
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupInteger(1); // 1 == true
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateByteElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_BYTE, cp, (byte) 'z');
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupInteger((byte) 'z');
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateCharElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_CHAR, cp, 't');
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupInteger('t');
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    // //
    // Create class element value
    @Test
    public void testCreateClassElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final ObjectType classType = new ObjectType("java.lang.Integer");
        final ClassElementValueGen evg = new ClassElementValueGen(classType, cp);
        assertTrue(evg.getClassString().contains("Integer"), "Unexpected value for contained class: '" + evg.getClassString() + "'");
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateDoubleElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_DOUBLE, cp, 333.44);
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupDouble(333.44);
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    // //
    // Create enum element value
    @Test
    public void testCreateEnumElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final ObjectType enumType = new ObjectType("SimpleEnum"); // Supports rainbow
        // :)
        final EnumElementValueGen evg = new EnumElementValueGen(enumType, "Red", cp);
        // Creation of an element like that should leave a new entry in the
        // cpool
        assertEquals(cp.lookupUtf8("Red"), evg.getValueIndex(), "The new ElementValue value index should match the contents of the constantpool");
        // BCELBUG: Should the class signature or class name be in the constant
        // pool? (see note in ConstantPool)
        // assertTrue("The new ElementValue type index should match the contents
        // of the constantpool but "+
        // evg.getTypeIndex()+"!="+cp.lookupClass(enumType.getSignature()),
        // evg.getTypeIndex()==cp.lookupClass(enumType.getSignature()));
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateFloatElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_FLOAT, cp, 111.222f);
        // Creation of an element like that should leave a new entry in the
        // cpool
        assertEquals(cp.lookupFloat(111.222f), evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    /**
     * Create primitive element values
     */
    @Test
    public void testCreateIntegerElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_INT, cp, 555);
        // Creation of an element like that should leave a new entry in the
        // cpool
        assertEquals(cp.lookupInteger(555), evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateLongElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_LONG, cp, 3334455L);
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupLong(3334455L);
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    @Test
    public void testCreateShortElementValue() throws Exception {
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_SHORT, cp, (short) 42);
        // Creation of an element like that should leave a new entry in the
        // cpool
        final int idx = cp.lookupInteger(42);
        assertEquals(idx, evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }

    // //
    // Create string element values
    @Test
    public void testCreateStringElementValue() throws Exception {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.STRING, cp, "hello");
        // Creation of an element like that should leave a new entry in the
        // cpool
        assertEquals(cp.lookupUtf8("hello"), evg.getIndex(), "Should have the same index in the constantpool");
        checkSerialize(evg, cp);
    }
}
