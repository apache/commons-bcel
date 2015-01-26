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

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.bcel.Constants;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantInvokeDynamic;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.util.ByteSequence;

/**
 * Class for INVOKEDYNAMIC. Not an instance of InvokeInstruction, since that class
 * expects to be able to get the class of the method. Ignores the bootstrap
 * mechanism entirely.
 *
 * @version $Id: InvokeInstruction.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @author  Bill Pugh
 * @since 6.0
 */
public class INVOKEDYNAMIC extends NameSignatureInstruction implements ExceptionThrower,
        StackConsumer, StackProducer {

    private static final long serialVersionUID = 1L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    INVOKEDYNAMIC() {
    }


    /**
     * @param index to constant pool
     */
    public INVOKEDYNAMIC(short opcode, int index) {
        super(opcode, index);
    }


    /**
     * @return mnemonic for instruction with symbolic references resolved
     */
    @Override
    public String toString( ConstantPool cp ) {
        Constant c = cp.getConstant(index);
        StringTokenizer tok = new StringTokenizer(cp.constantToString(c));
        return Constants.OPCODE_NAMES[opcode] + " " + tok.nextToken().replace('.', '/')
                + tok.nextToken();
    }

    /** 
     * Get the ConstantInvokeDynamic associated with this instruction
     */

      public ConstantInvokeDynamic getInvokeDynamic( ConstantPoolGen cpg ) {
          ConstantPool cp = cpg.getConstantPool();
          return (ConstantInvokeDynamic) cp.getConstant(index);
       }

    @Override
    public ConstantNameAndType getNameAndType( ConstantPoolGen cpg ) {
        ConstantPool cp = cpg.getConstantPool();
        ConstantInvokeDynamic id = getInvokeDynamic(cpg);
        return (ConstantNameAndType) cp.getConstant(id.getNameAndTypeIndex());
     }

    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     * @return Number of words consumed from stack by this instruction
     */
    @Override
    public int consumeStack( ConstantPoolGen cpg ) {

        String signature = getSignature(cpg);
        return  Type.getArgumentTypesSize(signature);
    }

    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     * @return Number of words produced onto stack by this instruction
     */
    @Override
    public int produceStack( ConstantPoolGen cpg ) {
        String signature = getSignature(cpg);
        return Type.getReturnTypeSize(signature);
    }


    /** @return return type of referenced method.
     */
    @Override
    public Type getType( ConstantPoolGen cpg ) {
        return getReturnType(cpg);
    }


    /** @return name of referenced method.
     */
    public String getMethodName( ConstantPoolGen cpg ) {
        return getName(cpg);
    }


    /** @return return type of referenced method.
     */
    public Type getReturnType( ConstantPoolGen cpg ) {
        return Type.getReturnType(getSignature(cpg));
    }


    /** @return argument types of referenced method.
     */
    public Type[] getArgumentTypes( ConstantPoolGen cpg ) {
        return Type.getArgumentTypes(getSignature(cpg));
    }

    /**
     * Read needed data (i.e., index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        super.initFromFile(bytes, wide);
        length = 5;
        bytes.readUnsignedShort();
    }

    public Class<?>[] getExceptions() {
        Class<?>[] cs = new Class[4 + ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION, 0, cs, 0,
                ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 3] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 2] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 1] = ExceptionConstants.ABSTRACT_METHOD_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
        return cs;
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
        v.visitCPInstruction(this);
        v.visitNameSignatureInstruction(this);
        v.visitINVOKEDYNAMIC(this);
    }
}
