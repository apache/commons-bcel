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

package org.apache.commons.bcel6.classfile;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.bcel6.Constants;

/**
 * This class represents a bootstrap method attribute, i.e., the bootstrap
 * method ref, the number of bootstrap arguments and an array of the
 * bootstrap arguments.
 * 
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">The class File Format : The BootstrapMethods Attribute</a>
 * @since 6.0
 */
public class BootstrapMethod implements Serializable, Cloneable {

    private static final long serialVersionUID = -4517534834047695344L;

    /** Index of the CONSTANT_MethodHandle_info structure in the constant_pool table */
    private int bootstrap_method_ref;

    private int num_bootstrap_arguments;

    /** Array of references to the constant_pool table */
    private int[] bootstrap_arguments;


    /**
     * Initialize from another object.
     */
    public BootstrapMethod(BootstrapMethod c) {
        this(c.getBootstrapMethodRef(), c.getNumBootstrapArguments(), c.getBootstrapArguments());
    }

    /**
     * Construct object from input stream.
     * 
     * @param input Input stream
     * @throws IOException
     */
    BootstrapMethod(DataInput input) throws IOException {
        this(input.readUnsignedShort(), input.readUnsignedShort(), (int[]) null);

        bootstrap_arguments = new int[num_bootstrap_arguments];
        for (int i = 0; i < num_bootstrap_arguments; i++) {
            bootstrap_arguments[i] = input.readUnsignedShort();
        }
    }


    /**
     * @param bootstrap_method_ref int index into constant_pool of CONSTANT_MethodHandle
     * @param num_bootstrap_arguments int count of number of boostrap arguments
     * @param bootstrap_arguments int[] indices into constant_pool of CONSTANT_<type>_info
     */
    public BootstrapMethod(int bootstrap_method_ref, int num_bootstrap_arguments, int[] bootstrap_arguments) {
        this.bootstrap_method_ref = bootstrap_method_ref;
        this.num_bootstrap_arguments = num_bootstrap_arguments;
        this.bootstrap_arguments = bootstrap_arguments;
    }

    /**
     * @return index into constant_pool of bootstrap_method
     */
    public int getBootstrapMethodRef() {
        return bootstrap_method_ref;
    }

    /**
     * @param bootstrap_method_ref int index into constant_pool of CONSTANT_MethodHandle
     */
    public void setBootstrapMethodRef(int bootstrap_method_ref) {
        this.bootstrap_method_ref = bootstrap_method_ref;
    }

    /**
     * @return int[] of bootstrap_method indices into constant_pool of CONSTANT_<type>_info
     */
    public int[] getBootstrapArguments() {
        return bootstrap_arguments;
    }

    /**
     * @return count of number of boostrap arguments
     */
    public int getNumBootstrapArguments() {
        return num_bootstrap_arguments;
    }

    /**
     * @param bootstrap_arguments int[] indices into constant_pool of CONSTANT_<type>_info
     */
    public void setBootstrapArguments(int[] bootstrap_arguments) {
        this.bootstrap_arguments = bootstrap_arguments;
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return "BootstrapMethod(" + bootstrap_method_ref + ", " + num_bootstrap_arguments + ", "
               //UNDONE
               //+ bootstrap_arguments + ")";
               + "UNDONE)";
    }

    /**
     * @return Resolved string representation
     */
    public final String toString( ConstantPool constant_pool ) {
        StringBuilder buf = new StringBuilder();
        String bootstrap_method_name;
        bootstrap_method_name = constant_pool.constantToString(bootstrap_method_ref,
                Constants.CONSTANT_MethodHandle);
        buf.append(Utility.compactClassName(bootstrap_method_name));
        if (num_bootstrap_arguments > 0) {
            buf.append("\n     Method Arguments:");
            for (int i = 0; i < num_bootstrap_arguments; i++) {
                buf.append("\n     ").append(i).append(": ");
                buf.append(constant_pool.constantToString(constant_pool.getConstant(bootstrap_arguments[i])));
            }
        }
        return buf.toString();
    }

    /**
     * Dump object to file stream in binary format.
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
