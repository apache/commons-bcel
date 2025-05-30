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
 * SIPUSH - Push short
 *
 * <PRE>
 * Stack: ... -&gt; ..., value
 * </PRE>
 */
public class SIPUSH extends Instruction implements ConstantPushInstruction {

    private short b;

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    SIPUSH() {
    }

    public SIPUSH(final short b) {
        super(org.apache.bcel.Const.SIPUSH, (short) 3);
        this.b = b;
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitPushInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitConstantPushInstruction(this);
        v.visitSIPUSH(this);
    }

    /**
     * Dump instruction as short code to stream out.
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        super.dump(out);
        out.writeShort(b);
    }

    /**
     * @return Type.SHORT
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.SHORT;
    }

    @Override
    public Number getValue() {
        return Integer.valueOf(b);
    }

    /**
     * Reads needed data (for example index) from file.
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        super.setLength(3);
        b = bytes.readShort();
    }

    /**
     * @return mnemonic for instruction
     */
    @Override
    public String toString(final boolean verbose) {
        return super.toString(verbose) + " " + b;
    }
}
