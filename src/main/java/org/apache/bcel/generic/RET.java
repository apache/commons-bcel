/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.util.ByteSequence;

/**
 * RET - Return from subroutine
 *
 * <PRE>
 * Stack: ... -&gt; ...
 * </PRE>
 */
public class RET extends Instruction implements IndexedInstruction, TypedInstruction {

    private boolean wide;
    private int index; // index to local variable containg the return address

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    RET() {
    }

    public RET(final int index) {
        super(org.apache.bcel.Const.RET, (short) 2);
        setIndex(index); // May set wide as side effect
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitRET(this);
    }

    /**
     * Dump instruction as byte code to stream out.
     *
     * @param out Output stream
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        if (wide) {
            out.writeByte(org.apache.bcel.Const.WIDE);
        }
        out.writeByte(super.getOpcode());
        if (wide) {
            out.writeShort(index);
        } else {
            out.writeByte(index);
        }
    }

    /**
     * @return index of local variable containg the return address
     */
    @Override
    public final int getIndex() {
        return index;
    }

    /**
     * @return return address type
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return ReturnaddressType.NO_TARGET;
    }

    /**
     * Reads needed data (for example index) from file.
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        this.wide = wide;
        if (wide) {
            index = bytes.readUnsignedShort();
            super.setLength(4);
        } else {
            index = bytes.readUnsignedByte();
            super.setLength(2);
        }
    }

    /**
     * Sets index of local variable containg the return address
     */
    @Override
    public final void setIndex(final int n) {
        if (n < 0) {
            throw new ClassGenException("Negative index value: " + n);
        }
        index = n;
        setWide();
    }

    private void setWide() {
        wide = index > org.apache.bcel.Const.MAX_BYTE;
        if (wide) {
            super.setLength(4); // Including the wide byte
        } else {
            super.setLength(2);
        }
    }

    /**
     * @return mnemonic for instruction
     */
    @Override
    public String toString(final boolean verbose) {
        return super.toString(verbose) + " " + index;
    }
}
