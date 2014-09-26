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
 * RET - Return from subroutine
 *
 * <PRE>Stack: ... -&gt; ...</PRE>
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class RET extends Instruction implements IndexedInstruction, TypedInstruction {

    private static final long serialVersionUID = -3751746568458761719L;
    private boolean wide;
    private int index; // index to local variable containg the return address


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    RET() {
    }


    public RET(int index) {
        super(org.apache.bcel.Constants.RET, (short) 2);
        setIndex(index); // May set wide as side effect
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        if (wide) {
            out.writeByte(org.apache.bcel.Constants.WIDE);
        }
        out.writeByte(opcode);
        if (wide) {
            out.writeShort(index);
        } else {
            out.writeByte(index);
        }
    }


    private void setWide() {
        wide = index > org.apache.bcel.Constants.MAX_BYTE;
        if (wide) {
            length = 4; // Including the wide byte  
        } else {
            length = 2;
        }
    }


    /**
     * Read needed data (e.g. index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        this.wide = wide;
        if (wide) {
            index = bytes.readUnsignedShort();
            length = 4;
        } else {
            index = bytes.readUnsignedByte();
            length = 2;
        }
    }


    /**
     * @return index of local variable containg the return address
     */
    public final int getIndex() {
        return index;
    }


    /**
     * Set index of local variable containg the return address
     */
    public final void setIndex( int n ) {
        if (n < 0) {
            throw new ClassGenException("Negative index value: " + n);
        }
        index = n;
        setWide();
    }


    /**
     * @return mnemonic for instruction
     */
    @Override
    public String toString( boolean verbose ) {
        return super.toString(verbose) + " " + index;
    }


    /** @return return address type
     */
    public Type getType( ConstantPoolGen cp ) {
        return ReturnaddressType.NO_TARGET;
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
        v.visitRET(this);
    }
}
