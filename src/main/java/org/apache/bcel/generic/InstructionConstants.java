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
 */
package org.apache.bcel.generic;

/**
 * Contains shareable instruction objects.
 * <p>
 * In order to save memory you can use some instructions multiply, since they have an immutable state and are directly
 * derived from Instruction. I.e. they have no instance fields that could be changed. Since some of these instructions
 * like ICONST_0 occur very frequently this can save a lot of time and space. This feature is an adaptation of the
 * FlyWeight design pattern, we just use an array instead of a factory.
 * </p>
 * <p>
 * The Instructions can also accessed directly under their names, so it's possible to write
 * il.append(Instruction.ICONST_0);
 * </p>
 *
 * @deprecated (since 6.0) Do not use. Use {@link InstructionConst} instead.
 */
@Deprecated
public interface InstructionConstants {

    /**
     * Deprecated, consider private and ignore.
     */
    class Clinit {
        // empty
    }

    /*
     * NOTE these are not currently immutable, because Instruction has mutable protected fields opcode and length.
     */
    Instruction NOP = InstructionConst.NOP;
    Instruction ACONST_NULL = InstructionConst.ACONST_NULL;
    Instruction ICONST_M1 = InstructionConst.ICONST_M1;
    Instruction ICONST_0 = InstructionConst.ICONST_0;
    Instruction ICONST_1 = InstructionConst.ICONST_1;
    Instruction ICONST_2 = InstructionConst.ICONST_2;
    Instruction ICONST_3 = InstructionConst.ICONST_3;
    Instruction ICONST_4 = InstructionConst.ICONST_4;
    Instruction ICONST_5 = InstructionConst.ICONST_5;
    Instruction LCONST_0 = InstructionConst.LCONST_0;
    Instruction LCONST_1 = InstructionConst.LCONST_1;
    Instruction FCONST_0 = InstructionConst.FCONST_0;
    Instruction FCONST_1 = InstructionConst.FCONST_1;
    Instruction FCONST_2 = InstructionConst.FCONST_2;
    Instruction DCONST_0 = InstructionConst.DCONST_0;
    Instruction DCONST_1 = InstructionConst.DCONST_1;
    ArrayInstruction IALOAD = InstructionConst.IALOAD;
    ArrayInstruction LALOAD = InstructionConst.LALOAD;
    ArrayInstruction FALOAD = InstructionConst.FALOAD;
    ArrayInstruction DALOAD = InstructionConst.DALOAD;
    ArrayInstruction AALOAD = InstructionConst.AALOAD;
    ArrayInstruction BALOAD = InstructionConst.BALOAD;
    ArrayInstruction CALOAD = InstructionConst.CALOAD;
    ArrayInstruction SALOAD = InstructionConst.SALOAD;
    ArrayInstruction IASTORE = InstructionConst.IASTORE;
    ArrayInstruction LASTORE = InstructionConst.LASTORE;
    ArrayInstruction FASTORE = InstructionConst.FASTORE;
    ArrayInstruction DASTORE = InstructionConst.DASTORE;
    ArrayInstruction AASTORE = InstructionConst.AASTORE;
    ArrayInstruction BASTORE = InstructionConst.BASTORE;
    ArrayInstruction CASTORE = InstructionConst.CASTORE;
    ArrayInstruction SASTORE = InstructionConst.SASTORE;
    StackInstruction POP = InstructionConst.POP;
    StackInstruction POP2 = InstructionConst.POP2;
    StackInstruction DUP = InstructionConst.DUP;
    StackInstruction DUP_X1 = InstructionConst.DUP_X1;
    StackInstruction DUP_X2 = InstructionConst.DUP_X2;
    StackInstruction DUP2 = InstructionConst.DUP2;
    StackInstruction DUP2_X1 = InstructionConst.DUP2_X1;
    StackInstruction DUP2_X2 = InstructionConst.DUP2_X2;
    StackInstruction SWAP = InstructionConst.SWAP;
    ArithmeticInstruction IADD = InstructionConst.IADD;
    ArithmeticInstruction LADD = InstructionConst.LADD;
    ArithmeticInstruction FADD = InstructionConst.FADD;
    ArithmeticInstruction DADD = InstructionConst.DADD;
    ArithmeticInstruction ISUB = InstructionConst.ISUB;
    ArithmeticInstruction LSUB = InstructionConst.LSUB;
    ArithmeticInstruction FSUB = InstructionConst.FSUB;
    ArithmeticInstruction DSUB = InstructionConst.DSUB;
    ArithmeticInstruction IMUL = InstructionConst.IMUL;
    ArithmeticInstruction LMUL = InstructionConst.LMUL;
    ArithmeticInstruction FMUL = InstructionConst.FMUL;
    ArithmeticInstruction DMUL = InstructionConst.DMUL;
    ArithmeticInstruction IDIV = InstructionConst.IDIV;
    ArithmeticInstruction LDIV = InstructionConst.LDIV;
    ArithmeticInstruction FDIV = InstructionConst.FDIV;
    ArithmeticInstruction DDIV = InstructionConst.DDIV;
    ArithmeticInstruction IREM = InstructionConst.IREM;
    ArithmeticInstruction LREM = InstructionConst.LREM;
    ArithmeticInstruction FREM = InstructionConst.FREM;
    ArithmeticInstruction DREM = InstructionConst.DREM;
    ArithmeticInstruction INEG = InstructionConst.INEG;
    ArithmeticInstruction LNEG = InstructionConst.LNEG;
    ArithmeticInstruction FNEG = InstructionConst.FNEG;
    ArithmeticInstruction DNEG = InstructionConst.DNEG;
    ArithmeticInstruction ISHL = InstructionConst.ISHL;
    ArithmeticInstruction LSHL = InstructionConst.LSHL;
    ArithmeticInstruction ISHR = InstructionConst.ISHR;
    ArithmeticInstruction LSHR = InstructionConst.LSHR;
    ArithmeticInstruction IUSHR = InstructionConst.IUSHR;
    ArithmeticInstruction LUSHR = InstructionConst.LUSHR;
    ArithmeticInstruction IAND = InstructionConst.IAND;
    ArithmeticInstruction LAND = InstructionConst.LAND;
    ArithmeticInstruction IOR = InstructionConst.IOR;
    ArithmeticInstruction LOR = InstructionConst.LOR;
    ArithmeticInstruction IXOR = InstructionConst.IXOR;
    ArithmeticInstruction LXOR = InstructionConst.LXOR;
    ConversionInstruction I2L = InstructionConst.I2L;
    ConversionInstruction I2F = InstructionConst.I2F;
    ConversionInstruction I2D = InstructionConst.I2D;
    ConversionInstruction L2I = InstructionConst.L2I;
    ConversionInstruction L2F = InstructionConst.L2F;
    ConversionInstruction L2D = InstructionConst.L2D;
    ConversionInstruction F2I = InstructionConst.F2I;
    ConversionInstruction F2L = InstructionConst.F2L;
    ConversionInstruction F2D = InstructionConst.F2D;
    ConversionInstruction D2I = InstructionConst.D2I;
    ConversionInstruction D2L = InstructionConst.D2L;
    ConversionInstruction D2F = InstructionConst.D2F;
    ConversionInstruction I2B = InstructionConst.I2B;
    ConversionInstruction I2C = InstructionConst.I2C;
    ConversionInstruction I2S = InstructionConst.I2S;
    Instruction LCMP = InstructionConst.LCMP;
    Instruction FCMPL = InstructionConst.FCMPL;
    Instruction FCMPG = InstructionConst.FCMPG;
    Instruction DCMPL = InstructionConst.DCMPL;
    Instruction DCMPG = InstructionConst.DCMPG;
    ReturnInstruction IRETURN = InstructionConst.IRETURN;
    ReturnInstruction LRETURN = InstructionConst.LRETURN;
    ReturnInstruction FRETURN = InstructionConst.FRETURN;
    ReturnInstruction DRETURN = InstructionConst.DRETURN;
    ReturnInstruction ARETURN = InstructionConst.ARETURN;
    ReturnInstruction RETURN = InstructionConst.RETURN;
    Instruction ARRAYLENGTH = InstructionConst.ARRAYLENGTH;
    Instruction ATHROW = InstructionConst.ATHROW;
    Instruction MONITORENTER = InstructionConst.MONITORENTER;
    Instruction MONITOREXIT = InstructionConst.MONITOREXIT;

    /**
     * You can use these constants in multiple places safely, if you can guarantee that you will never alter their internal
     * values, e.g. call setIndex().
     */
    LocalVariableInstruction THIS = InstructionConst.THIS;
    LocalVariableInstruction ALOAD_0 = InstructionConst.ALOAD_0;
    LocalVariableInstruction ALOAD_1 = InstructionConst.ALOAD_1;
    LocalVariableInstruction ALOAD_2 = InstructionConst.ALOAD_2;
    LocalVariableInstruction ILOAD_0 = InstructionConst.ILOAD_0;
    LocalVariableInstruction ILOAD_1 = InstructionConst.ILOAD_1;
    LocalVariableInstruction ILOAD_2 = InstructionConst.ILOAD_2;
    LocalVariableInstruction ASTORE_0 = InstructionConst.ASTORE_0;
    LocalVariableInstruction ASTORE_1 = InstructionConst.ASTORE_1;
    LocalVariableInstruction ASTORE_2 = InstructionConst.ASTORE_2;
    LocalVariableInstruction ISTORE_0 = InstructionConst.ISTORE_0;
    LocalVariableInstruction ISTORE_1 = InstructionConst.ISTORE_1;
    LocalVariableInstruction ISTORE_2 = InstructionConst.ISTORE_2;

    /**
     * Gets object via its opcode, for immutable instructions like branch instructions entries are set to null.
     */
    Instruction[] INSTRUCTIONS = InstructionConst.INSTRUCTIONS;

    /**
     * Interfaces may have no static initializers, so we simulate this with an inner class.
     */
    Clinit bla = new Clinit();
}
