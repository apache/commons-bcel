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

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Const;

/**
 * Record component info from a record. Instances from this class maps 
 * every component from a given record.
 * 
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se14/preview/specs/records-jvms.html#jvms-4.7.30">The Java Virtual Machine Specification, Java SE 14 Edition, Records (preview)</a> 
 */
public class RecordComponentInfo implements Cloneable, Node {

    private final int index;
    private final int descriptorIndex;
    private final Attribute[] attributes;
    private final ConstantPool constantPool;

    /**
     * Constructs object from its components.
     *
     * @param index Index in constant pool
     * @param descriptorIndex descriptor index
     * @param attributes Array of attributes
     * @param constantPool Constant pool
     */
    public RecordComponentInfo(int index, int descriptorIndex, Attribute[] attributes, 
            ConstantPool constantPool) {
        this.index = index;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
        this.constantPool = constantPool;
    }

    public void write(DataOutputStream file) throws IOException {
        file.writeShort(index);
        file.writeShort(descriptorIndex);
        for (final Attribute attribute : attributes) {
            attribute.dump(file);
        }
    }

    @Override
    public void accept(Visitor v) {
        v.visitRecordComponent(this);
    }

    public int getIndex() {
        return index;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    /**
     * @return String representation, i.e., a list of classes.
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("RecordComponentInfo(");
        buf.append(constantPool.getConstantString(index, Const.CONSTANT_Utf8));
        buf.append(",");
        buf.append(constantPool.getConstantString(descriptorIndex, Const.CONSTANT_Utf8));
        buf.append(",");
        buf.append(attributes.length);
        buf.append("):\n");
        for (final Attribute attribute : attributes) {
            buf.append("  ").append(attribute.toString()).append("\n");
        }
        return buf.substring(0, buf.length() - 1); // remove the last newline
    }

}
