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

package org.apache.bcel.classfile;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.bcel.Const;
import org.apache.bcel.util.Args;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class is derived from <em>Attribute</em> and records the classes and interfaces that are authorized to claim
 * membership in the nest hosted by the current class or interface. There may be at most one Record attribute in a
 * ClassFile structure.
 *
 * @see Attribute
 * @since 6.9.0
 */
public final class Record extends Attribute {

    private static final RecordComponentInfo[] EMPTY_RCI_ARRAY = new RecordComponentInfo[] {};
    
    private RecordComponentInfo[] components;

    /**
     * Constructs object from input stream.
     *
     * @param nameIndex Index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constantPool Array of constants
     * @throws IOException if an I/O error occurs.
     */
    Record(final int nameIndex, final int length, final DataInput input, final ConstantPool constantPool) throws IOException {
        this(nameIndex, length, (RecordComponentInfo[]) null, constantPool);
        final int classCount = input.readUnsignedShort();
        components = new RecordComponentInfo[classCount];
        for (int i = 0; i < classCount; i++) {
            final int index = input.readUnsignedShort();
            final int descriptorIndex =input.readUnsignedShort();
            final int attributesCount = input.readUnsignedShort();
            final Attribute[] attributes = new Attribute[attributesCount];
            for (int j = 0 ; j< attributesCount; j++)
                attributes[j] = Attribute.readAttribute(input, constantPool);
            components[i] = new RecordComponentInfo(
                    index,
                    descriptorIndex,
                    attributes,input,constantPool);
        }
    }

    /**
     * @param nameIndex Index in constant pool
     * @param length Content length in bytes
     * @param classes Table of Record component info 
     * @param constantPool Array of constants
     */
    public Record(final int nameIndex, final int length, final RecordComponentInfo[] classes, final ConstantPool constantPool) {
        super(Const.ATTR_RECORD, nameIndex, length, constantPool);
        this.components = classes != null ? classes : EMPTY_RCI_ARRAY;
        Args.requireU2(this.components.length, "attributes.length");
    }

    /**
     * Initialize from another object. Note that both objects use the same references (shallow copy). Use copy() for a
     * physical copy.
     *
     * @param c Source to copy.
     */
    public Record(final Record c) {
        this(c.getNameIndex(), c.getLength(), c.getComponents(), c.getConstantPool());
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitly defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitRecord(this);
    }

    /**
     * @return deep copy of this attribute
     */
    @Override
    public Attribute copy(final ConstantPool constantPool) {
        final Record c = (Record) clone();
        if (components.length > 0) {
            c.components = components.clone();
        }
        c.setConstantPool(constantPool);
        return c;
    }

    /**
     * Dump RecordComponentInfo attribute to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void dump(final DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(components.length);
        for (final RecordComponentInfo component : components) {
            component.write(file);
        }
    }

    /**
     * @return array of Record Component Info elements.
     */
    public RecordComponentInfo[] getComponents() {
        return components;
    }
 
    /**
     * @return String representation, i.e., a list of classes.
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("Record(");
        buf.append(components.length);
        buf.append("):\n");
        for (final RecordComponentInfo component : components) {
            buf.append("  ").append(component.toString()).append("\n");
        }
        return buf.substring(0, buf.length() - 1); // remove the last newline
    }
}
