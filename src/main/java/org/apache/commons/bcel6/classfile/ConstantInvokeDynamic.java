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
package org.apache.commons.bcel6.classfile;

import java.io.DataInput;
import java.io.IOException;

import org.apache.commons.bcel6.Constants;

/** 
 * This class is derived from the abstract 
 * <A HREF="org.apache.commons.bcel6.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a invoke dynamic.
 * 
 * @see     Constant
 * @since 6.0
 */
public final class ConstantInvokeDynamic extends ConstantCP {

    private static final long serialVersionUID = 4310367359017396174L;

    /**
     * Initialize from another object.
     */
    public ConstantInvokeDynamic(ConstantInvokeDynamic c) {
        super(Constants.CONSTANT_InvokeDynamic, c.getBootstrapMethodAttrIndex(), c.getNameAndTypeIndex());
    }


    /**
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantInvokeDynamic(DataInput file) throws IOException {
        super(Constants.CONSTANT_InvokeDynamic, file);
    }


    public ConstantInvokeDynamic(int bootstrap_method_attr_index, int name_and_type_index) {
        super(Constants.CONSTANT_InvokeDynamic, bootstrap_method_attr_index, name_and_type_index);
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
        v.visitConstantInvokeDynamic(this);
    }


    /**
     * @return String representation
     */
    @Override
    public final String toString() {
        // UNDONE: need to string replace "class_index" with "bootstrap_method_attr_index"
        return super.toString();
    }
}
