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
import java.io.Serializable;

/**
 * Entry of the bootstrap_methods table.
 * 
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">The class File Format : The BootstrapMethods Attribute</a>
 * @since 6.0
 */
public class BootstrapMethod implements Serializable, Cloneable {

    private static final long serialVersionUID = -4517534834047695344L;

    /** Index of the CONSTANT_MethodHandle_info structure in the constant_pool table */
    private int bootstrap_method_ref;

    /** Array of references to the constant_pool table */
    private int[] bootstrap_arguments;

    public BootstrapMethod() {
    }

    /**
     * Construct object from input stream.
     * 
     * @param input Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    BootstrapMethod(DataInput input) throws IOException, ClassFormatException {
        bootstrap_method_ref = input.readUnsignedShort();

        int num_bootstrap_methods = input.readUnsignedShort();

        bootstrap_arguments = new int[num_bootstrap_methods];
        for (int i = 0; i < num_bootstrap_methods; i++) {
            bootstrap_arguments[i] = input.readUnsignedShort();
        }
    }

    public int getBootstrapMethodRef() {
        return bootstrap_method_ref;
    }

    public void setBootstrapMethodRef(int bootstrap_method_ref) {
        this.bootstrap_method_ref = bootstrap_method_ref;
    }

    public int[] getBootstrapArguments() {
        return bootstrap_arguments;
    }

    public void setBootstrapArguments(int[] bootstrap_arguments) {
        this.bootstrap_arguments = bootstrap_arguments;
    }

    /**
     * Dump object to file stream on binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    public final void dump(DataOutputStream file) throws IOException {
        file.writeShort(bootstrap_method_ref);
        file.writeShort(bootstrap_arguments.length);
        for (int bootstrap_argument : bootstrap_arguments) {
            file.writeShort(bootstrap_argument);
        }
    }

    /**
     * @return deep copy of this object
     */
    public BootstrapMethod copy() {
        try {
            return (BootstrapMethod) clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }
}
