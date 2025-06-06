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

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.ByteSequence;

/**
 * Abstract super class for instructions that use an index into the constant pool such as LDC, INVOKEVIRTUAL, etc.
 *
 * @see ConstantPoolGen
 * @see LDC
 * @see INVOKEVIRTUAL
 */
public abstract class CPInstruction extends Instruction implements TypedInstruction, IndexedInstruction {

    /**
     * @deprecated (since 6.0) will be made private; do not access directly, use getter/setter
     */
    @Deprecated
    protected int index; // index to constant pool

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    CPInstruction() {
    }

    /**
     * @param index to constant pool
     */
    protected CPInstruction(final short opcode, final int index) {
        super(opcode, (short) 3);
        setIndex(index);
    }

    /**
     * Dump instruction as byte code to stream out.
     *
     * @param out Output stream
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        out.writeByte(super.getOpcode());
        out.writeShort(index);
    }

    /**
     * @return index in constant pool referred by this instruction.
     */
    @Override
    public final int getIndex() {
        return index;
    }

    /**
     * @return type related with this instruction.
     */
    @Override
    public Type getType(final ConstantPoolGen cpg) {
        final ConstantPool cp = cpg.getConstantPool();
        String name = cp.getConstantString(index, org.apache.bcel.Const.CONSTANT_Class);
        if (!name.startsWith("[")) {
            name = "L" + name + ";";
        }
        return Type.getType(name);
    }

    /**
     * Reads needed data (i.e., index) from file.
     *
     * @param bytes input stream
     * @param wide wide prefix?
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        setIndex(bytes.readUnsignedShort());
        super.setLength(3);
    }

    /**
     * Sets the index to constant pool.
     *
     * @param index in constant pool.
     */
    @Override
    public void setIndex(final int index) { // TODO could be package-protected?
        if (index < 0) {
            throw new ClassGenException("Negative index value: " + index);
        }
        this.index = index;
    }

    /**
     * Long output format:
     *
     * &lt;name of opcode&gt; "["&lt;opcode number&gt;"]" "("&lt;length of instruction&gt;")" "&lt;"&lt; constant pool
     * index&gt;"&gt;"
     *
     * @param verbose long/short format switch
     * @return mnemonic for instruction
     */
    @Override
    public String toString(final boolean verbose) {
        return super.toString(verbose) + " " + index;
    }

    /**
     * @return mnemonic for instruction with symbolic references resolved
     */
    @Override
    public String toString(final ConstantPool cp) {
        final Constant c = cp.getConstant(index);
        String str = cp.constantToString(c);
        if (c instanceof ConstantClass) {
            str = Utility.packageToPath(str);
        }
        return org.apache.bcel.Const.getOpcodeName(super.getOpcode()) + " " + str;
    }
}
