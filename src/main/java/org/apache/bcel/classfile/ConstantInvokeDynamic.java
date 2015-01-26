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
 * and represents a reference to a invoke dynamic.
 * 
 * @see     Constant
 * @since 6.0
 */
public final class ConstantInvokeDynamic extends Constant {

    private static final long serialVersionUID = 4310367359017396174L;
    private int bootstrap_method_attr_index;
    private int name_and_type_index;


    /**
     * Initialize from another object.
     */
    public ConstantInvokeDynamic(ConstantInvokeDynamic c) {
        this(c.getBootstrapMethodAttrIndex(), c.getNameAndTypeIndex());
    }


    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantInvokeDynamic(DataInput file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }


    public ConstantInvokeDynamic(int bootstrap_method_attr_index,
            int name_and_type_index) {
        super(Constants.CONSTANT_InvokeDynamic);
        this.bootstrap_method_attr_index = bootstrap_method_attr_index;
        this.name_and_type_index = name_and_type_index;
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
        // TODO Add .visitMethodType to Visitor interface
    }


    /**
     * Dump name and signature index to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    @Override
    public final void dump( DataOutputStream file ) throws IOException {
        file.writeByte(tag);
        file.writeShort(bootstrap_method_attr_index);
        file.writeShort(name_and_type_index);
    }


    public int getBootstrapMethodAttrIndex() {
        return bootstrap_method_attr_index;
    }


    public void setBootstrapMethodAttrIndex(int bootstrap_method_attr_index) {
        this.bootstrap_method_attr_index = bootstrap_method_attr_index;
    }


    public int getNameAndTypeIndex() {
        return name_and_type_index;
    }


    public void setNameAndTypeIndex(int name_and_type_index) {
        this.name_and_type_index = name_and_type_index;
    }


    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        return super.toString() + "(bootstrap_method_attr_index = " +
                bootstrap_method_attr_index + ", name_and_type_index = " +
                name_and_type_index + ")";
    }
}
