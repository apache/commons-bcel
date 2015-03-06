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
import org.apache.bcel.Constants;

/** 
 * This class represents the table of exceptions that are thrown by a
 * method. This attribute may be used once per method.  The name of
 * this class is <em>ExceptionTable</em> for historical reasons; The
 * Java Virtual Machine Specification, Second Edition defines this
 * attribute using the name <em>Exceptions</em> (which is inconsistent
 * with the other classes).
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @see     Code
 */
public final class ExceptionTable extends Attribute {

    private static final long serialVersionUID = 2045358830660883220L;

    private int[] exception_index_table; // constant pool


    /**
     * Initialize from another object. Note that both objects use the same
     * references (shallow copy). Use copy() for a physical copy.
     */
    public ExceptionTable(ExceptionTable c) {
        this(c.getNameIndex(), c.getLength(), c.getExceptionIndexTable(), c.getConstantPool());
    }


    /**
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param exception_index_table Table of indices in constant pool
     * @param constant_pool Array of constants
     */
    public ExceptionTable(int name_index, int length, int[] exception_index_table,
            ConstantPool constant_pool) {
        super(Constants.ATTR_EXCEPTIONS, name_index, length, constant_pool);
        setExceptionIndexTable(exception_index_table);
    }


    /**
     * Construct object from input stream.
     * @param name_index Index in constant pool
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    ExceptionTable(int name_index, int length, DataInput input, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (int[]) null, constant_pool);
        int number_of_exceptions = input.readUnsignedShort();
        exception_index_table = new int[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++) {
            exception_index_table[i] = input.readUnsignedShort();
        }
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
        v.visitExceptionTable(this);
    }


    /**
     * Dump exceptions attribute to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    @Override
    public final void dump( DataOutputStream file ) throws IOException {
        super.dump(file);
        file.writeShort(exception_index_table.length);
        for (int index : exception_index_table) {
            file.writeShort(index);
        }
    }


    /**
     * @return Array of indices into constant pool of thrown exceptions.
     */
    public final int[] getExceptionIndexTable() {
        return exception_index_table;
    }


    /**
     * @return Length of exception table.
     */
    public final int getNumberOfExceptions() {
        return exception_index_table == null ? 0 : exception_index_table.length;
    }


    /**
     * @return class names of thrown exceptions
     */
    public final String[] getExceptionNames() {
        String[] names = new String[exception_index_table.length];
        for (int i = 0; i < exception_index_table.length; i++) {
            names[i] = constant_pool.getConstantString(exception_index_table[i], 
                    Constants.CONSTANT_Class).replace('/', '.');
        }
        return names;
    }


    /**
     * @param exception_index_table the list of exception indexes
     * Also redefines number_of_exceptions according to table length.
     */
    public final void setExceptionIndexTable( int[] exception_index_table ) {
        this.exception_index_table = exception_index_table != null ? exception_index_table : new int[0];
    }


    /**
     * @return String representation, i.e., a list of thrown exceptions.
     */
    @Override
    public final String toString() {
        StringBuilder buf = new StringBuilder();
        String str;
        for (int i = 0; i < exception_index_table.length; i++) {
            str = constant_pool.getConstantString(exception_index_table[i], Constants.CONSTANT_Class);
            buf.append(Utility.compactClassName(str, false));
            if (i < exception_index_table.length - 1) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }


    /**
     * @return deep copy of this attribute
     */
    @Override
    public Attribute copy( ConstantPool _constant_pool ) {
        ExceptionTable c = (ExceptionTable) clone();
        if (exception_index_table != null) {
            c.exception_index_table = new int[exception_index_table.length];
            System.arraycopy(exception_index_table, 0, c.exception_index_table, 0,
                    exception_index_table.length);
        }
        c.constant_pool = _constant_pool;
        return c;
    }
}
