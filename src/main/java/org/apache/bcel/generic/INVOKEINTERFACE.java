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

import org.apache.bcel.Const;
import org.apache.bcel.ExceptionConst;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.util.ByteSequence;

/**
 * INVOKEINTERFACE - Invoke interface method
 *
 * <PRE>
 * Stack: ..., objectref, [arg1, [arg2 ...]] -&gt; ...
 * </PRE>
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokeinterface"> The
 *      invokeinterface instruction in The Java Virtual Machine Specification</a>
 */
public final class INVOKEINTERFACE extends InvokeInstruction {

    private int nargs; // Number of arguments on stack (number of stack slots), called "count" in vmspec2

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    INVOKEINTERFACE() {
    }

    public INVOKEINTERFACE(final int index, final int nargs) {
        super(Const.INVOKEINTERFACE, index);
        super.setLength(5);
        if (nargs < 1) {
            throw new ClassGenException("Number of arguments must be > 0 " + nargs);
        }
        this.nargs = nargs;
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitStackProducer(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        v.visitFieldOrMethod(this);
        v.visitInvokeInstruction(this);
        v.visitINVOKEINTERFACE(this);
    }

    @Override
    public int consumeStack(final ConstantPoolGen cpg) { // nargs is given in byte-code
        return nargs; // nargs includes this reference
    }

    /**
     * Dump instruction as byte code to stream out.
     *
     * @param out Output stream
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        out.writeByte(super.getOpcode());
        out.writeShort(super.getIndex());
        out.writeByte(nargs);
        out.writeByte(0);
    }

    /**
     * The <B>count</B> argument according to the Java Language Specification, Second Edition.
     */
    public int getCount() {
        return nargs;
    }

    @Override
    public Class<?>[] getExceptions() {
        return ExceptionConst.createExceptions(ExceptionConst.EXCS.EXCS_INTERFACE_METHOD_RESOLUTION, ExceptionConst.UNSATISFIED_LINK_ERROR,
            ExceptionConst.ABSTRACT_METHOD_ERROR, ExceptionConst.ILLEGAL_ACCESS_ERROR, ExceptionConst.INCOMPATIBLE_CLASS_CHANGE_ERROR);
    }

    /**
     * Reads needed data (i.e., index) from file.
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        super.initFromFile(bytes, wide);
        super.setLength(5);
        nargs = bytes.readUnsignedByte();
        bytes.readByte(); // Skip 0 byte
    }

    /**
     * @return mnemonic for instruction with symbolic references resolved
     */
    @Override
    public String toString(final ConstantPool cp) {
        return super.toString(cp) + " " + nargs;
    }
}
