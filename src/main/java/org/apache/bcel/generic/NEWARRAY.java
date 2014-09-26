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
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

/** 
 * NEWARRAY -  Create new array of basic type (int, short, ...)
 * <PRE>Stack: ..., count -&gt; ..., arrayref</PRE>
 * type must be one of T_INT, T_SHORT, ...
 * 
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class NEWARRAY extends Instruction implements AllocationInstruction, ExceptionThrower,
        StackProducer {

    private static final long serialVersionUID = 7048445841018649405L;
    private byte type;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    NEWARRAY() {
    }


    public NEWARRAY(byte type) {
        super(org.apache.bcel.Constants.NEWARRAY, (short) 2);
        this.type = type;
    }


    public NEWARRAY(BasicType type) {
        this(type.getType());
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        out.writeByte(opcode);
        out.writeByte(type);
    }


    /**
     * @return numeric code for basic element type
     */
    public final byte getTypecode() {
        return type;
    }


    /**
     * @return type of constructed array
     */
    public final Type getType() {
        return new ArrayType(BasicType.getType(type), 1);
    }


    /**
     * @return mnemonic for instruction
     */
    @Override
    public String toString( boolean verbose ) {
        return super.toString(verbose) + " " + org.apache.bcel.Constants.TYPE_NAMES[type];
    }


    /**
     * Read needed data (e.g. index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        type = bytes.readByte();
        length = 2;
    }


    public Class<?>[] getExceptions() {
        return new Class[] {
            org.apache.bcel.ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION
        };
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        v.visitAllocationInstruction(this);
        v.visitExceptionThrower(this);
        v.visitStackProducer(this);
        v.visitNEWARRAY(this);
    }
}
