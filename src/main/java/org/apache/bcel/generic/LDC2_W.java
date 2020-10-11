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

/**
 * LDC2_W - Push long or double from constant pool
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 */
public class LDC2_W extends CPInstruction implements PushInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    LDC2_W() {
    }


    public LDC2_W(final int index) {
        super(org.apache.bcel.Const.LDC2_W, index);
    }


    @Override
    public Type getType( final ConstantPoolGen cpg ) {
        switch (cpg.getConstantPool().getConstant(super.getIndex()).getTag()) {
            case org.apache.bcel.Const.CONSTANT_Long:
                return Type.LONG;
            case org.apache.bcel.Const.CONSTANT_Double:
                return Type.DOUBLE;
            default: // Never reached
                throw new IllegalArgumentException("Unknown constant type " + super.getOpcode());
        }
    }


    public Number getValue( final ConstantPoolGen cpg ) {
        final org.apache.bcel.classfile.Constant c = cpg.getConstantPool().getConstant(super.getIndex());
        switch (c.getTag()) {
            case org.apache.bcel.Const.CONSTANT_Long:
                return Long.valueOf(((org.apache.bcel.classfile.ConstantLong) c).getBytes());
            case org.apache.bcel.Const.CONSTANT_Double:
                return new Double(((org.apache.bcel.classfile.ConstantDouble) c).getBytes());
            default: // Never reached
                throw new IllegalArgumentException("Unknown or invalid constant type at " + super.getIndex());
        }
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
    public void accept( final Visitor v ) {
        v.visitStackProducer(this);
        v.visitPushInstruction(this);
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitLDC2_W(this);
    }
}
