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

/**
 * LCONST - Push 0 or 1, other values cause an exception
 *
 * <PRE>
 * Stack: ... -&gt; ...,
 * </PRE>
 */
public class LCONST extends Instruction implements ConstantPushInstruction {

    private final long value;

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    LCONST() {
        this(0);
    }

    public LCONST(final long l) {
        super(org.apache.bcel.Const.LCONST_0, (short) 1);
        if (l == 0) {
            super.setOpcode(org.apache.bcel.Const.LCONST_0);
        } else if (l == 1) {
            super.setOpcode(org.apache.bcel.Const.LCONST_1);
        } else {
            throw new ClassGenException("LCONST can be used only for 0 and 1: " + l);
        }
        value = l;
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
        v.visitLCONST(this);
    }

    /**
     * @return Type.LONG
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.LONG;
    }

    @Override
    public Number getValue() {
        return Long.valueOf(value);
    }
}
