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
import org.apache.bcel.util.ByteSequence;

/**
 * Abstract super class for instructions dealing with local variables.
 */
public abstract class LocalVariableInstruction extends Instruction implements TypedInstruction, IndexedInstruction {

    /**
     * @deprecated (since 6.0) will be made private; do not access directly, use getter/setter
     */
    @Deprecated
    protected int n = -1; // index of referenced variable

    private short cTag = -1; // compact version, such as ILOAD_0
    private short canonTag = -1; // canonical tag such as ILOAD

    /**
     * Empty constructor needed for Instruction.readInstruction. Also used by IINC()!
     */
    LocalVariableInstruction() {
    }

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise. tag and length are defined in
     * readInstruction and initFromFile, respectively.
     */
    LocalVariableInstruction(final short canonTag, final short cTag) {
        this.canonTag = canonTag;
        this.cTag = cTag;
    }

    /**
     * @param opcode Instruction opcode
     * @param cTag Instruction number for compact version, ALOAD_0, for example.
     * @param n local variable index (unsigned short)
     */
    protected LocalVariableInstruction(final short opcode, final short cTag, final int n) {
        super(opcode, (short) 2);
        this.cTag = cTag;
        canonTag = opcode;
        setIndex(n);
    }

    /**
     * Dump instruction as byte code to stream out.
     *
     * @param out Output stream
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        if (wide()) {
            out.writeByte(Const.WIDE);
        }
        out.writeByte(super.getOpcode());
        if (super.getLength() > 1) { // Otherwise ILOAD_n, instruction, for example.
            if (wide()) {
                out.writeShort(n);
            } else {
                out.writeByte(n);
            }
        }
    }

    /**
     * @return canonical tag for instruction, for example, ALOAD for ALOAD_0
     */
    public short getCanonicalTag() {
        return canonTag;
    }

    /**
     * @return local variable index (n) referred by this instruction.
     */
    @Override
    public final int getIndex() {
        return n;
    }

    /**
     * Returns the type associated with the instruction - in case of ALOAD or ASTORE Type.OBJECT is returned. This is just a
     * bit incorrect, because ALOAD and ASTORE may work on every ReferenceType (including Type.NULL) and ASTORE may even
     * work on a ReturnaddressType.
     *
     * @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        switch (canonTag) {
        case Const.ILOAD:
        case Const.ISTORE:
            return Type.INT;
        case Const.LLOAD:
        case Const.LSTORE:
            return Type.LONG;
        case Const.DLOAD:
        case Const.DSTORE:
            return Type.DOUBLE;
        case Const.FLOAD:
        case Const.FSTORE:
            return Type.FLOAT;
        case Const.ALOAD:
        case Const.ASTORE:
            return Type.OBJECT;
        default:
            throw new ClassGenException("Unknown case in switch" + canonTag);
        }
    }

    /**
     * Reads needed data (for example index) from file.
     *
     * <pre>
     * (ILOAD &lt;= tag &lt;= ALOAD_3) || (ISTORE &lt;= tag &lt;= ASTORE_3)
     * </pre>
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        if (wide) {
            n = bytes.readUnsignedShort();
            super.setLength(4);
        } else {
            final short opcode = super.getOpcode();
            if (opcode >= Const.ILOAD && opcode <= Const.ALOAD || opcode >= Const.ISTORE && opcode <= Const.ASTORE) {
                n = bytes.readUnsignedByte();
                super.setLength(2);
            } else {
                if (opcode <= Const.ALOAD_3) { // compact load instruction such as ILOAD_2
                    n = (opcode - Const.ILOAD_0) % 4;
                } else { // Assert ISTORE_0 <= tag <= ASTORE_3
                    n = (opcode - Const.ISTORE_0) % 4;
                }
                super.setLength(1);
            }
        }
    }

    /**
     * Sets the local variable index. also updates opcode and length TODO Why?
     *
     * @see #setIndexOnly(int)
     */
    @Override
    public void setIndex(final int n) { // TODO could be package-protected?
        if (n < 0 || n > Const.MAX_SHORT) {
            throw new ClassGenException("Illegal value: " + n);
        }
        this.n = n;
        // Cannot be < 0 as this is checked above
        if (n <= 3) { // Use more compact instruction xLOAD_n
            super.setOpcode((short) (cTag + n));
            super.setLength(1);
        } else {
            super.setOpcode(canonTag);
            if (wide()) {
                super.setLength(4);
            } else {
                super.setLength(2);
            }
        }
    }

    /**
     * Sets the index of the referenced variable (n) only
     *
     * @since 6.0
     * @see #setIndex(int)
     */
    final void setIndexOnly(final int n) {
        this.n = n;
    }

    /**
     * Long output format:
     *
     * &lt;name of opcode&gt; "["&lt;opcode number&gt;"]" "("&lt;length of instruction&gt;")" "&lt;"&lt; local variable
     * index&gt;"&gt;"
     *
     * @param verbose long/short format switch
     * @return mnemonic for instruction
     */
    @Override
    public String toString(final boolean verbose) {
        final short opcode = super.getOpcode();
        if (opcode >= Const.ILOAD_0 && opcode <= Const.ALOAD_3 || opcode >= Const.ISTORE_0 && opcode <= Const.ASTORE_3) {
            return super.toString(verbose);
        }
        return super.toString(verbose) + " " + n;
    }

    private boolean wide() {
        return n > Const.MAX_BYTE;
    }
}
