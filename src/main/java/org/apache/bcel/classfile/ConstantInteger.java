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

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Const;

/**
 * This class is derived from the abstract {@link Constant} and represents a reference to an int object.
 *
 * @see Constant
 */
public final class ConstantInteger extends Constant implements ConstantObject {

    private int bytes;

    /**
     * Initialize from another object.
     *
     * @param c Source to copy.
     */
    public ConstantInteger(final ConstantInteger c) {
        this(c.getBytes());
    }

    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException if an I/O error occurs.
     */
    ConstantInteger(final DataInput file) throws IOException {
        this(file.readInt());
    }

    /**
     * @param bytes Data
     */
    public ConstantInteger(final int bytes) {
        super(Const.CONSTANT_Integer);
        this.bytes = bytes;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitly defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitConstantInteger(this);
    }

    /**
     * Dump constant integer to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void dump(final DataOutputStream file) throws IOException {
        file.writeByte(super.getTag());
        file.writeInt(bytes);
    }

    /**
     * @return data, i.e., 4 bytes.
     */
    public int getBytes() {
        return bytes;
    }

    /**
     * @return Integer object
     */
    @Override
    public Object getConstantValue(final ConstantPool cp) {
        return Integer.valueOf(bytes);
    }

    /**
     * @param bytes the raw bytes that represent this integer
     */
    public void setBytes(final int bytes) {
        this.bytes = bytes;
    }

    /**
     * @return String representation.
     */
    @Override
    public String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }
}
