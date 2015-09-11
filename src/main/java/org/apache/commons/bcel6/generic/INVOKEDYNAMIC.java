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
package org.apache.commons.bcel6.generic;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.bcel6.Const;
import org.apache.commons.bcel6.ExceptionConst;
import org.apache.commons.bcel6.classfile.ConstantPool;
import org.apache.commons.bcel6.util.ByteSequence;

/**
 * Class for INVOKEDYNAMIC. Not an instance of InvokeInstruction, since that class
 * expects to be able to get the class of the method. Ignores the bootstrap
 * mechanism entirely.
 *
 * @version $Id: InvokeInstruction.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @see
 * <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokedynamic">
 * The invokedynamic instruction in The Java Virtual Machine Specification</a>
 * @since 6.0
 */
public class INVOKEDYNAMIC extends InvokeInstruction {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    INVOKEDYNAMIC() {
    }


    public INVOKEDYNAMIC(int index) {
        super(Const.INVOKEDYNAMIC, index);
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        out.writeByte(super.getOpcode());
        out.writeShort(super.getIndex());
        out.writeByte(0);
        out.writeByte(0);
       }


    /**
     * Read needed data (i.e., index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        super.initFromFile(bytes, wide);
        super.setLength(5);
        bytes.readByte(); // Skip 0 byte
        bytes.readByte(); // Skip 0 byte
    }


    /**
     * @return mnemonic for instruction with symbolic references resolved
     */
    @Override
    public String toString( ConstantPool cp ) {
        return super.toString(cp);
    }


    @Override
    public Class<?>[] getExceptions() {
        return ExceptionConst.createExceptions(ExceptionConst.EXCS.EXCS_INTERFACE_METHOD_RESOLUTION,
            ExceptionConst.UNSATISFIED_LINK_ERROR,
            ExceptionConst.ABSTRACT_METHOD_ERROR,
            ExceptionConst.ILLEGAL_ACCESS_ERROR,
            ExceptionConst.INCOMPATIBLE_CLASS_CHANGE_ERROR);
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
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitStackProducer(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        v.visitFieldOrMethod(this);
        v.visitInvokeInstruction(this);
        v.visitINVOKEDYNAMIC(this);
    }
}
