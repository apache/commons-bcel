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
 *
 */
package org.apache.bcel.classfile;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.Constants;

/** 
 * This class is derived from the abstract 
 * <A HREF="org.apache.bcel.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a method handle.
 * 
 * @see     Constant
 * @since 6.0
 */
public final class ConstantMethodHandle extends Constant {

    private static final long serialVersionUID = -7875124116920198044L;
    private int reference_kind;
    private int reference_index;


    /**
     * Initialize from another object.
     */
    public ConstantMethodHandle(ConstantMethodHandle c) {
        this(c.getReferenceKind(), c.getReferenceIndex());
    }


    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantMethodHandle(DataInput file) throws IOException {
        this(file.readUnsignedByte(), file.readUnsignedShort());
    }


    public ConstantMethodHandle(int reference_kind, int reference_index) {
        super(Constants.CONSTANT_MethodHandle);
        this.reference_kind = reference_kind;
        this.reference_index = reference_index;
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitly
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        // TODO Add .visitMethodHandle to Visitor interface
    }


    /**
     * Dump method kind and index to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    @Override
    public final void dump( DataOutputStream file ) throws IOException {
        file.writeByte(tag);
        file.writeByte(reference_kind);
        file.writeShort(reference_index);
    }


    public int getReferenceKind() {
        return reference_kind;
    }


    public void setReferenceKind(int reference_kind) {
        this.reference_kind = reference_kind;
    }


    public int getReferenceIndex() {
        return reference_index;
    }


    public void setReferenceIndex(int reference_index) {
        this.reference_index = reference_index;
    }


    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(reference_kind = " + reference_kind +
                ", reference_index = " + reference_index + ")";
    }
}
