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

import java.util.StringTokenizer;

import org.apache.commons.bcel6.Constants;
import org.apache.commons.bcel6.classfile.Constant;
import org.apache.commons.bcel6.classfile.ConstantCP;
import org.apache.commons.bcel6.classfile.ConstantPool;

/**
 * Super class for the INVOKExxx family of instructions.
 *
 * @version $Id$
 */
public abstract class InvokeInstruction extends FieldOrMethod implements ExceptionThrower,
        StackConsumer, StackProducer {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    InvokeInstruction() {
    }


    /**
     * @param index to constant pool
     */
    protected InvokeInstruction(short opcode, int index) {
        super(opcode, index);
    }


    /**
     * @return mnemonic for instruction with symbolic references resolved
     */
    @Override
    public String toString( ConstantPool cp ) {
        Constant c = cp.getConstant(super.getIndex());
        StringTokenizer tok = new StringTokenizer(cp.constantToString(c));
        return Constants.getOpcodeName(super.getOpcode()) + " " + tok.nextToken().replace('.', '/')
                + tok.nextToken();
    }


    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     * @return Number of words consumed from stack by this instruction
     */
    @Override
    public int consumeStack( ConstantPoolGen cpg ) {
        int sum;
        if ((super.getOpcode() == Constants.INVOKESTATIC) || (super.getOpcode() == Constants.INVOKEDYNAMIC)) {
            sum = 0;
        } else {
            sum = 1; // this reference
        }

        String signature = getSignature(cpg);
        sum += Type.getArgumentTypesSize(signature);
        return sum;
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
     * This overrides the deprecated version as we know here that the referenced class
     * cannot be an array unless something has gone badly wrong.
     * @return name of the referenced class/interface
     * @throws IllegalArgumentException if the referenced class is an array (this should not happen)
     */
    @Override
    public String getClassName( ConstantPoolGen cpg ) {
        ConstantPool cp = cpg.getConstantPool();
        ConstantCP cmr = (ConstantCP) cp.getConstant(super.getIndex());
        String className = cp.getConstantString(cmr.getClassIndex(), Constants.CONSTANT_Class);
        if (className.startsWith("[")) {
            throw new IllegalArgumentException("Cannot be used on an array type");
        }
        return className.replace('/', '.');
    }

    
}
