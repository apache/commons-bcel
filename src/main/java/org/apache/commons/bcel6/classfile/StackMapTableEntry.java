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

import org.apache.commons.bcel6.Constants;

/**
 * This class represents a stack map entry recording the types of
 * local variables and the the of stack items at a given byte code offset.
 * See CLDC specification ��?5.3.1.2
 *
 * @version $Id$
 * @see     StackMap
 * @see     StackMapType
 * @since 6.0
 */
public final class StackMapTableEntry implements Cloneable {

    private final int frame_type;
    private int byte_code_offset;
    private StackMapType[] types_of_locals;
    private StackMapType[] types_of_stack_items;
    private ConstantPool constant_pool;


    /**
     * Construct object from input stream.
     * 
     * @param input Input stream
     * @throws IOException
     */
    StackMapTableEntry(DataInput input, ConstantPool constant_pool) throws IOException {
        this(input.readByte() & 0xFF, -1, null, null, constant_pool);

        if (frame_type >= Constants.SAME_FRAME && frame_type <= Constants.SAME_FRAME_MAX) {
            byte_code_offset = frame_type - Constants.SAME_FRAME;
        } else if (frame_type >= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME && frame_type <= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            byte_code_offset = frame_type - Constants.SAME_LOCALS_1_STACK_ITEM_FRAME;
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(input, constant_pool);
        } else if (frame_type == Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            byte_code_offset = input.readShort();
            types_of_stack_items = new StackMapType[1];
            types_of_stack_items[0] = new StackMapType(input, constant_pool);
        } else if (frame_type >= Constants.CHOP_FRAME && frame_type <= Constants.CHOP_FRAME_MAX) {
            byte_code_offset = input.readShort();
        } else if (frame_type == Constants.SAME_FRAME_EXTENDED) {
            byte_code_offset = input.readShort();
        } else if (frame_type >= Constants.APPEND_FRAME && frame_type <= Constants.APPEND_FRAME_MAX) {
            byte_code_offset = input.readShort();
            int number_of_locals = frame_type - 251;
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(input, constant_pool);
            }            
        } else if (frame_type == Constants.FULL_FRAME) {        
            byte_code_offset = input.readShort();
            int number_of_locals = input.readShort();
            types_of_locals = new StackMapType[number_of_locals];
            for (int i = 0; i < number_of_locals; i++) {
                types_of_locals[i] = new StackMapType(input, constant_pool);
            }
            int number_of_stack_items = input.readShort();
            types_of_stack_items = new StackMapType[number_of_stack_items];
            for (int i = 0; i < number_of_stack_items; i++) {
                types_of_stack_items[i] = new StackMapType(input, constant_pool);
            }
        } else {
            /* Can't happen */
            throw new ClassFormatException ("Invalid frame type found while parsing stack map table: " + frame_type);
        }
    }


    public StackMapTableEntry(int tag, int byte_code_offset,
            StackMapType[] types_of_locals,
            StackMapType[] types_of_stack_items, ConstantPool constant_pool) {
        this.frame_type = tag;
        this.byte_code_offset = byte_code_offset;
        this.types_of_locals = types_of_locals != null ? types_of_locals : new StackMapType[0];
        this.types_of_stack_items = types_of_stack_items != null ? types_of_stack_items : new StackMapType[0];
        this.constant_pool = constant_pool;
    }


    /**
     * Dump stack map entry
     *
     * @param file Output file stream
     * @throws IOException
     */
    public final void dump( DataOutputStream file ) throws IOException {
        file.write(frame_type);
        if (frame_type >= Constants.SAME_FRAME && frame_type <= Constants.SAME_FRAME_MAX) {
            // nothing to be done
        } else if (frame_type >= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME && frame_type <= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            types_of_stack_items[0].dump(file);
        } else if (frame_type == Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            file.writeShort(byte_code_offset);
            types_of_stack_items[0].dump(file);
        } else if (frame_type >= Constants.CHOP_FRAME && frame_type <= Constants.CHOP_FRAME_MAX) {
            file.writeShort(byte_code_offset);
        } else if (frame_type == Constants.SAME_FRAME_EXTENDED) {
            file.writeShort(byte_code_offset);
        } else if (frame_type >= Constants.APPEND_FRAME && frame_type <= Constants.APPEND_FRAME_MAX) {
            file.writeShort(byte_code_offset);
            for (StackMapType type : types_of_locals) {
                type.dump(file);
            }            
        } else if (frame_type == Constants.FULL_FRAME) {        
            file.writeShort(byte_code_offset);
            file.writeShort(types_of_locals.length);
            for (StackMapType type : types_of_locals) {
                type.dump(file);
            }
            file.writeShort(types_of_stack_items.length);
            for (StackMapType type : types_of_stack_items) {
                type.dump(file);
            }
        } else {
            /* Can't happen */
            throw new ClassFormatException ("Invalid Stack map table tag: " + frame_type);
        }
    }


    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        StringBuilder buf = new StringBuilder(64);
        buf.append("(");
        if (frame_type >= Constants.SAME_FRAME && frame_type <= Constants.SAME_FRAME_MAX) {
            buf.append("SAME");
        } else if (frame_type >= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME && frame_type <= Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_MAX) {
            buf.append("SAME_LOCALS_1_STACK");
        } else if (frame_type == Constants.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED) {
            buf.append("SAME_LOCALS_1_STACK_EXTENDED");
        } else if (frame_type >= Constants.CHOP_FRAME && frame_type <= Constants.CHOP_FRAME_MAX) {
            buf.append("CHOP ").append(String.valueOf(251-frame_type));
        } else if (frame_type == Constants.SAME_FRAME_EXTENDED) {
            buf.append("SAME_EXTENDED");
        } else if (frame_type >= Constants.APPEND_FRAME && frame_type <= Constants.APPEND_FRAME_MAX) {
            buf.append("APPEND ").append(String.valueOf(frame_type-251));
        } else if (frame_type == Constants.FULL_FRAME) {        
            buf.append("FULL");
        } else {
            buf.append("UNKNOWN");
        }
        buf.append(", offset delta=").append(byte_code_offset);
        if (types_of_locals.length > 0) {
            buf.append(", locals={");
            for (int i = 0; i < types_of_locals.length; i++) {
                buf.append(types_of_locals[i]);
                if (i < types_of_locals.length - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        if (types_of_stack_items.length > 0) {
            buf.append(", stack items={");
            for (int i = 0; i < types_of_stack_items.length; i++) {
                buf.append(types_of_stack_items[i]);
                if (i < types_of_stack_items.length - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        buf.append(")");
        return buf.toString();
    }


    public void setByteCodeOffset( int b ) {
        byte_code_offset = b;
    }


    public int getByteCodeOffset() {
        return byte_code_offset;
    }


    public int getNumberOfLocals() {
        return types_of_locals.length;
    }


    public void setTypesOfLocals( StackMapType[] types ) {
        types_of_locals = types != null ? types : new StackMapType[0];
    }


    public StackMapType[] getTypesOfLocals() {
        return types_of_locals;
    }


    public int getNumberOfStackItems() {
        return types_of_stack_items.length;
    }


    public void setTypesOfStackItems( StackMapType[] types ) {
        types_of_stack_items = types != null ? types : new StackMapType[0];
    }


    public StackMapType[] getTypesOfStackItems() {
        return types_of_stack_items;
    }


    /**
     * @return deep copy of this object
     */
    public StackMapTableEntry copy() {
        try {
            return (StackMapTableEntry) clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        v.visitStackMapTableEntry(this);
    }


    /**
     * @return Constant pool used by this object.
     */
    public final ConstantPool getConstantPool() {
        return constant_pool;
    }


    /**
     * @param constant_pool Constant pool to be used for this object.
     */
    public final void setConstantPool( ConstantPool constant_pool ) {
        this.constant_pool = constant_pool;
    }
}
