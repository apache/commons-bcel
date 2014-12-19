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
import java.io.IOException;
import org.apache.bcel.Constants;

/** 
 * This class represents a constant pool reference to a method.
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public final class ConstantMethodref extends ConstantCP {

    private static final long serialVersionUID = -864296320352750967L;


    /**
     * Initialize from another object.
     */
    public ConstantMethodref(ConstantMethodref c) {
        super(Constants.CONSTANT_Methodref, c.getClassIndex(), c.getNameAndTypeIndex());
    }


    /**
     * Initialize instance from input data.
     *
     * @param input input stream
     * @throws IOException
     */
    ConstantMethodref(DataInput input) throws IOException {
        super(Constants.CONSTANT_Methodref, input);
    }


    /**
     * @param class_index Reference to the class containing the method
     * @param name_and_type_index and the method signature
     */
    public ConstantMethodref(int class_index, int name_and_type_index) {
        super(Constants.CONSTANT_Methodref, class_index, name_and_type_index);
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        v.visitConstantMethodref(this);
    }
}
