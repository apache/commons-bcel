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

import org.apache.commons.bcel6.Constants;
import org.apache.commons.bcel6.classfile.ConstantPool;
import org.apache.commons.bcel6.util.ByteSequence;

/** 
 * Abstract super class for all Java byte codes.
 *
 * @version $Id: Instruction.java 1696777 2015-08-20 12:19:42Z sebb $
 */
public abstract class Instruction implements Cloneable {

    protected short length = 1; // Length of instruction in bytes 
    protected short opcode = -1; // Opcode number


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    Instruction() {
    }


    public Instruction(short opcode, short length) {
        this.length = length;
        this.opcode = opcode;
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    public void dump( DataOutputStream out ) throws IOException {
        out.writeByte(opcode); // Common for all instructions
    }


    /** @return name of instruction, i.e., opcode name
     */
    public String getName() {
        return Constants.OPCODE_NAMES[opcode];
    }


    /**
     * Long output format:
     *
     * &lt;name of opcode&gt; "["&lt;opcode number&gt;"]" 
     * "("&lt;length of instruction&gt;")"
     *
     * @param verbose long/short format switch
     * @return mnemonic for instruction
     */
    public String toString( boolean verbose ) {
        if (verbose) {
            return getName() + "[" + opcode + "](" + length + ")";
        }
        return getName();
    }


    /**
     * @return mnemonic for instruction in verbose format
     */
    @Override
    public String toString() {
        return toString(true);
    }


    /**
     * @return mnemonic for instruction with sumbolic references resolved
     */
    public String toString( ConstantPool cp ) {
        return toString(false);
    }


    /**
     * Use with caution, since `BranchInstruction's have a `target' reference which
     * is not copied correctly (only basic types are). This also applies for 
     * `Select' instructions with their multiple branch targets.
     *
     * @see BranchInstruction
     * @return (shallow) copy of an instruction
     */
    public Instruction copy() {
        Instruction i = null;
        // "Constant" instruction, no need to duplicate
        if (InstructionConstants.getInstruction(this.getOpcode()) != null) {
            i = this;
        } else {
            try {
                i = (Instruction) clone();
            } catch (CloneNotSupportedException e) {
                System.err.println(e);
            }
        }
        return i;
    }


    /**
     * Read needed data (e.g. index) from file.
     *
     * @param bytes byte sequence to read from
     * @param wide "wide" instruction flag
     * @throws IOException may be thrown if the implementation needs to read data from the file
     */
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
    }


    /**
     * Read an instruction from (byte code) input stream and return the
     * appropriate object.
     *
     * @param bytes input stream bytes
     * @return instruction object being read
     */
    // @since 6.0 no longer final
    public static Instruction readInstruction( ByteSequence bytes ) throws IOException {
        boolean wide = false;
        short opcode = (short) bytes.readUnsignedByte();
        Instruction obj = null;
        if (opcode == Constants.WIDE) { // Read next opcode after wide byte
            wide = true;
            opcode = (short) bytes.readUnsignedByte();
        }
        final Instruction instruction = InstructionConstants.getInstruction(opcode);
        if (instruction != null) {
            return instruction; // Used predefined immutable object, if available
        }

        switch (opcode) {
            case Constants.BIPUSH:
                obj = new BIPUSH();
                break;
            case Constants.SIPUSH:
                obj = new SIPUSH();
                break;
            case Constants.LDC:
                obj = new LDC();
                break;
            case Constants.LDC_W:
                obj = new LDC_W();
                break;
            case Constants.LDC2_W:
                obj = new LDC2_W();
                break;
            case Constants.ILOAD:
                obj = new ILOAD();
                break;
            case Constants.LLOAD:
                obj = new LLOAD();
                break;
            case Constants.FLOAD:
                obj = new FLOAD();
                break;
            case Constants.DLOAD:
                obj = new DLOAD();
                break;
            case Constants.ALOAD:
                obj = new ALOAD();
                break;
            case Constants.ILOAD_0:
                obj = new ILOAD(0);
                break;
            case Constants.ILOAD_1:
                obj = new ILOAD(1);
                break;
            case Constants.ILOAD_2:
                obj = new ILOAD(2);
                break;
            case Constants.ILOAD_3:
                obj = new ILOAD(3);
                break;
            case Constants.LLOAD_0:
                obj = new LLOAD(0);
                break;
            case Constants.LLOAD_1:
                obj = new LLOAD(1);
                break;
            case Constants.LLOAD_2:
                obj = new LLOAD(2);
                break;
            case Constants.LLOAD_3:
                obj = new LLOAD(3);
                break;
            case Constants.FLOAD_0:
                obj = new FLOAD(0);
                break;
            case Constants.FLOAD_1:
                obj = new FLOAD(1);
                break;
            case Constants.FLOAD_2:
                obj = new FLOAD(2);
                break;
            case Constants.FLOAD_3:
                obj = new FLOAD(3);
                break;
            case Constants.DLOAD_0:
                obj = new DLOAD(0);
                break;
            case Constants.DLOAD_1:
                obj = new DLOAD(1);
                break;
            case Constants.DLOAD_2:
                obj = new DLOAD(2);
                break;
            case Constants.DLOAD_3:
                obj = new DLOAD(3);
                break;
            case Constants.ALOAD_0:
                obj = new ALOAD(0);
                break;
            case Constants.ALOAD_1:
                obj = new ALOAD(1);
                break;
            case Constants.ALOAD_2:
                obj = new ALOAD(2);
                break;
            case Constants.ALOAD_3:
                obj = new ALOAD(3);
                break;
            case Constants.ISTORE:
                obj = new ISTORE();
                break;
            case Constants.LSTORE:
                obj = new LSTORE();
                break;
            case Constants.FSTORE:
                obj = new FSTORE();
                break;
            case Constants.DSTORE:
                obj = new DSTORE();
                break;
            case Constants.ASTORE:
                obj = new ASTORE();
                break;
            case Constants.ISTORE_0:
                obj = new ISTORE(0);
                break;
            case Constants.ISTORE_1:
                obj = new ISTORE(1);
                break;
            case Constants.ISTORE_2:
                obj = new ISTORE(2);
                break;
            case Constants.ISTORE_3:
                obj = new ISTORE(3);
                break;
            case Constants.LSTORE_0:
                obj = new LSTORE(0);
                break;
            case Constants.LSTORE_1:
                obj = new LSTORE(1);
                break;
            case Constants.LSTORE_2:
                obj = new LSTORE(2);
                break;
            case Constants.LSTORE_3:
                obj = new LSTORE(3);
                break;
            case Constants.FSTORE_0:
                obj = new FSTORE(0);
                break;
            case Constants.FSTORE_1:
                obj = new FSTORE(1);
                break;
            case Constants.FSTORE_2:
                obj = new FSTORE(2);
                break;
            case Constants.FSTORE_3:
                obj = new FSTORE(3);
                break;
            case Constants.DSTORE_0:
                obj = new DSTORE(0);
                break;
            case Constants.DSTORE_1:
                obj = new DSTORE(1);
                break;
            case Constants.DSTORE_2:
                obj = new DSTORE(2);
                break;
            case Constants.DSTORE_3:
                obj = new DSTORE(3);
                break;
            case Constants.ASTORE_0:
                obj = new ASTORE(0);
                break;
            case Constants.ASTORE_1:
                obj = new ASTORE(1);
                break;
            case Constants.ASTORE_2:
                obj = new ASTORE(2);
                break;
            case Constants.ASTORE_3:
                obj = new ASTORE(3);
                break;
            case Constants.IINC:
                obj = new IINC();
                break;
            case Constants.IFEQ:
                obj = new IFEQ();
                break;
            case Constants.IFNE:
                obj = new IFNE();
                break;
            case Constants.IFLT:
                obj = new IFLT();
                break;
            case Constants.IFGE:
                obj = new IFGE();
                break;
            case Constants.IFGT:
                obj = new IFGT();
                break;
            case Constants.IFLE:
                obj = new IFLE();
                break;
            case Constants.IF_ICMPEQ:
                obj = new IF_ICMPEQ();
                break;
            case Constants.IF_ICMPNE:
                obj = new IF_ICMPNE();
                break;
            case Constants.IF_ICMPLT:
                obj = new IF_ICMPLT();
                break;
            case Constants.IF_ICMPGE:
                obj = new IF_ICMPGE();
                break;
            case Constants.IF_ICMPGT:
                obj = new IF_ICMPGT();
                break;
            case Constants.IF_ICMPLE:
                obj = new IF_ICMPLE();
                break;
            case Constants.IF_ACMPEQ:
                obj = new IF_ACMPEQ();
                break;
            case Constants.IF_ACMPNE:
                obj = new IF_ACMPNE();
                break;
            case Constants.GOTO:
                obj = new GOTO();
                break;
            case Constants.JSR:
                obj = new JSR();
                break;
            case Constants.RET:
                obj = new RET();
                break;
            case Constants.TABLESWITCH:
                obj = new TABLESWITCH();
                break;
            case Constants.LOOKUPSWITCH:
                obj = new LOOKUPSWITCH();
                break;
            case Constants.GETSTATIC:
                obj = new GETSTATIC();
                break;
            case Constants.PUTSTATIC:
                obj = new PUTSTATIC();
                break;
            case Constants.GETFIELD:
                obj = new GETFIELD();
                break;
            case Constants.PUTFIELD:
                obj = new PUTFIELD();
                break;
            case Constants.INVOKEVIRTUAL:
                obj = new INVOKEVIRTUAL();
                break;
            case Constants.INVOKESPECIAL:
                obj = new INVOKESPECIAL();
                break;
            case Constants.INVOKESTATIC:
                obj = new INVOKESTATIC();
                break;
            case Constants.INVOKEINTERFACE:
                obj = new INVOKEINTERFACE();
                break;
            case Constants.INVOKEDYNAMIC:
                obj = new INVOKEDYNAMIC();
                break;
            case Constants.NEW:
                obj = new NEW();
                break;
            case Constants.NEWARRAY:
                obj = new NEWARRAY();
                break;
            case Constants.ANEWARRAY:
                obj = new ANEWARRAY();
                break;
            case Constants.CHECKCAST:
                obj = new CHECKCAST();
                break;
            case Constants.INSTANCEOF:
                obj = new INSTANCEOF();
                break;
            case Constants.MULTIANEWARRAY:
                obj = new MULTIANEWARRAY();
                break;
            case Constants.IFNULL:
                obj = new IFNULL();
                break;
            case Constants.IFNONNULL:
                obj = new IFNONNULL();
                break;
            case Constants.GOTO_W:
                obj = new GOTO_W();
                break;
            case Constants.JSR_W:
                obj = new JSR_W();
                break;
            case Constants.BREAKPOINT:
                obj = new BREAKPOINT();
                break;
            case Constants.IMPDEP1:
                obj = new IMPDEP1();
                break;
            case Constants.IMPDEP2:
                obj = new IMPDEP2();
                break;
            default:
                throw new ClassGenException("Illegal opcode detected: " + opcode);

        }

        if (wide
                && !((obj instanceof LocalVariableInstruction) || (obj instanceof IINC) || (obj instanceof RET))) {
            throw new ClassGenException("Illegal opcode after wide: " + opcode);
        }
        obj.setOpcode(opcode);
        obj.initFromFile(bytes, wide); // Do further initializations, if any
        return obj;
    }

    /**
     * This method also gives right results for instructions whose
     * effect on the stack depends on the constant pool entry they
     * reference.
     *  @return Number of words consumed from stack by this instruction,
     * or Constants.UNPREDICTABLE, if this can not be computed statically
     */
    public int consumeStack( ConstantPoolGen cpg ) {
        return Constants.CONSUME_STACK[opcode];
    }


    /**
     * This method also gives right results for instructions whose
     * effect on the stack depends on the constant pool entry they
     * reference.
     * @return Number of words produced onto stack by this instruction,
     * or Constants.UNPREDICTABLE, if this can not be computed statically
     */
    public int produceStack( ConstantPoolGen cpg ) {
        return Constants.PRODUCE_STACK[opcode];
    }


    /**
     * @return this instructions opcode
     */
    public short getOpcode() {
        return opcode;
    }


    /**
     * @return length (in bytes) of instruction
     */
    public int getLength() {
        return length;
    }


    /**
     * Needed in readInstruction and subclasses in this package
     */
    void setOpcode( short opcode ) {
        this.opcode = opcode;
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    public abstract void accept( Visitor v );


    /**
     * Check if the value can fit in a byte (signed)
     * @param value the value to check
     * @return true if the value is in range
     * @since 6.0
     */
    public static boolean isValidByte(int value) {
        return value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE;
    }

    /**
     * Check if the value can fit in a short (signed)
     * @param value the value to check
     * @return true if the value is in range
     * @since 6.0
     */
    public static boolean isValidShort(int value) {
        return value >= Short.MIN_VALUE && value <= Short.MAX_VALUE;
    }
}
