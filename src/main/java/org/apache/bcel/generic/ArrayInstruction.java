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

import org.apache.bcel.ExceptionConst;

/**
 * Super class for instructions dealing with array access such as IALOAD.
 *
 */
public abstract class ArrayInstruction extends Instruction implements ExceptionThrower,
        TypedInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    ArrayInstruction() {
    }


    /**
     * @param opcode of instruction
     */
    protected ArrayInstruction(final short opcode) {
        super(opcode, (short) 1);
    }


    @Override
    public Class<?>[] getExceptions() {
        return ExceptionConst.createExceptions(ExceptionConst.EXCS.EXCS_ARRAY_EXCEPTION);
    }


    /** @return type associated with the instruction
     */
    @Override
    public Type getType( final ConstantPoolGen cp ) {
        final short _opcode = super.getOpcode();
        switch (_opcode) {
            case org.apache.bcel.Const.IALOAD:
            case org.apache.bcel.Const.IASTORE:
                return Type.INT;
            case org.apache.bcel.Const.CALOAD:
            case org.apache.bcel.Const.CASTORE:
                return Type.CHAR;
            case org.apache.bcel.Const.BALOAD:
            case org.apache.bcel.Const.BASTORE:
                return Type.BYTE;
            case org.apache.bcel.Const.SALOAD:
            case org.apache.bcel.Const.SASTORE:
                return Type.SHORT;
            case org.apache.bcel.Const.LALOAD:
            case org.apache.bcel.Const.LASTORE:
                return Type.LONG;
            case org.apache.bcel.Const.DALOAD:
            case org.apache.bcel.Const.DASTORE:
                return Type.DOUBLE;
            case org.apache.bcel.Const.FALOAD:
            case org.apache.bcel.Const.FASTORE:
                return Type.FLOAT;
            case org.apache.bcel.Const.AALOAD:
            case org.apache.bcel.Const.AASTORE:
                return Type.OBJECT;
            default:
                throw new ClassGenException("Unknown case in switch" + _opcode);
        }
    }
}
