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

/**
 * Contains shareable instruction objects.
 * <p>
 * In order to save memory you can use some instructions multiply, since they have an immutable state and are directly derived from Instruction. I.e. they have
 * no instance fields that could be changed. Since some of these instructions like ICONST_0 occur very frequently this can save a lot of time and space. This
 * feature is an adaptation of the FlyWeight design pattern, we just use an array instead of a factory.
 * </p>
 * <p>
 * The Instructions can also accessed directly under their names, so it's possible to write il.append(Instruction.ICONST_0);
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
    /**
     * NOP instruction.
     *
     * @deprecated Use {@link InstructionConst#NOP}.
     */
    @Deprecated
    Instruction NOP = InstructionConst.NOP;

    /**
     * ACONST_NULL instruction.
     *
     * @deprecated Use {@link InstructionConst#ACONST_NULL}.
     */
    @Deprecated
    Instruction ACONST_NULL = InstructionConst.ACONST_NULL;

    /**
     * ICONST_M1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_M1}.
     */
    @Deprecated
    Instruction ICONST_M1 = InstructionConst.ICONST_M1;

    /**
     * ICONST_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_0}.
     */
    @Deprecated
    Instruction ICONST_0 = InstructionConst.ICONST_0;

    /**
     * ICONST_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_1}.
     */
    @Deprecated
    Instruction ICONST_1 = InstructionConst.ICONST_1;

    /**
     * ICONST_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_2}.
     */
    @Deprecated
    Instruction ICONST_2 = InstructionConst.ICONST_2;

    /**
     * ICONST_3 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_3}.
     */
    @Deprecated
    Instruction ICONST_3 = InstructionConst.ICONST_3;

    /**
     * ICONST_4 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_4}.
     */
    @Deprecated
    Instruction ICONST_4 = InstructionConst.ICONST_4;

    /**
     * ICONST_5 instruction.
     *
     * @deprecated Use {@link InstructionConst#ICONST_5}.
     */
    @Deprecated
    Instruction ICONST_5 = InstructionConst.ICONST_5;

    /**
     * LCONST_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#LCONST_0}.
     */
    @Deprecated
    Instruction LCONST_0 = InstructionConst.LCONST_0;

    /**
     * LCONST_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#LCONST_1}.
     */
    @Deprecated
    Instruction LCONST_1 = InstructionConst.LCONST_1;

    /**
     * FCONST_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#FCONST_0}.
     */
    @Deprecated
    Instruction FCONST_0 = InstructionConst.FCONST_0;

    /**
     * FCONST_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#FCONST_1}.
     */
    @Deprecated
    Instruction FCONST_1 = InstructionConst.FCONST_1;

    /**
     * FCONST_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#FCONST_2}.
     */
    @Deprecated
    Instruction FCONST_2 = InstructionConst.FCONST_2;

    /**
     * DCONST_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#DCONST_0}.
     */
    @Deprecated
    Instruction DCONST_0 = InstructionConst.DCONST_0;

    /**
     * DCONST_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#DCONST_1}.
     */
    @Deprecated
    Instruction DCONST_1 = InstructionConst.DCONST_1;

    /**
     * IALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#IALOAD}.
     */
    @Deprecated
    ArrayInstruction IALOAD = InstructionConst.IALOAD;

    /**
     * LALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#LALOAD}.
     */
    @Deprecated
    ArrayInstruction LALOAD = InstructionConst.LALOAD;

    /**
     * FALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#FALOAD}.
     */
    @Deprecated
    ArrayInstruction FALOAD = InstructionConst.FALOAD;

    /**
     * DALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#DALOAD}.
     */
    @Deprecated
    ArrayInstruction DALOAD = InstructionConst.DALOAD;

    /**
     * AALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#AALOAD}.
     */
    @Deprecated
    ArrayInstruction AALOAD = InstructionConst.AALOAD;

    /**
     * BALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#BALOAD}.
     */
    @Deprecated
    ArrayInstruction BALOAD = InstructionConst.BALOAD;

    /**
     * CALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#CALOAD}.
     */
    @Deprecated
    ArrayInstruction CALOAD = InstructionConst.CALOAD;

    /**
     * SALOAD instruction.
     *
     * @deprecated Use {@link InstructionConst#SALOAD}.
     */
    @Deprecated
    ArrayInstruction SALOAD = InstructionConst.SALOAD;

    /**
     * IASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#IASTORE}.
     */
    @Deprecated
    ArrayInstruction IASTORE = InstructionConst.IASTORE;

    /**
     * LASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#LASTORE}.
     */
    @Deprecated
    ArrayInstruction LASTORE = InstructionConst.LASTORE;

    /**
     * FASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#FASTORE}.
     */
    @Deprecated
    ArrayInstruction FASTORE = InstructionConst.FASTORE;

    /**
     * DASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#DASTORE}.
     */
    @Deprecated
    ArrayInstruction DASTORE = InstructionConst.DASTORE;

    /**
     * AASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#AASTORE}.
     */
    @Deprecated
    ArrayInstruction AASTORE = InstructionConst.AASTORE;

    /**
     * BASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#BASTORE}.
     */
    @Deprecated
    ArrayInstruction BASTORE = InstructionConst.BASTORE;

    /**
     * CASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#CASTORE}.
     */
    @Deprecated
    ArrayInstruction CASTORE = InstructionConst.CASTORE;

    /**
     * SASTORE instruction.
     *
     * @deprecated Use {@link InstructionConst#SASTORE}.
     */
    @Deprecated
    ArrayInstruction SASTORE = InstructionConst.SASTORE;

    /**
     * POP instruction.
     *
     * @deprecated Use {@link InstructionConst#POP}.
     */
    @Deprecated
    StackInstruction POP = InstructionConst.POP;

    /**
     * POP2 instruction.
     *
     * @deprecated Use {@link InstructionConst#POP2}.
     */
    @Deprecated
    StackInstruction POP2 = InstructionConst.POP2;

    /**
     * DUP instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP}.
     */
    @Deprecated
    StackInstruction DUP = InstructionConst.DUP;

    /**
     * DUP_X1 instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP_X1}.
     */
    @Deprecated
    StackInstruction DUP_X1 = InstructionConst.DUP_X1;

    /**
     * DUP_X2 instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP_X2}.
     */
    @Deprecated
    StackInstruction DUP_X2 = InstructionConst.DUP_X2;

    /**
     * DUP2 instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP2}.
     */
    @Deprecated
    StackInstruction DUP2 = InstructionConst.DUP2;

    /**
     * DUP2_X1 instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP2_X1}.
     */
    @Deprecated
    StackInstruction DUP2_X1 = InstructionConst.DUP2_X1;

    /**
     * DUP2_X2 instruction.
     *
     * @deprecated Use {@link InstructionConst#DUP2_X2}.
     */
    @Deprecated
    StackInstruction DUP2_X2 = InstructionConst.DUP2_X2;

    /**
     * SWAP instruction.
     *
     * @deprecated Use {@link InstructionConst#SWAP}.
     */
    @Deprecated
    StackInstruction SWAP = InstructionConst.SWAP;

    /**
     * IADD instruction.
     *
     * @deprecated Use {@link InstructionConst#IADD}.
     */
    @Deprecated
    ArithmeticInstruction IADD = InstructionConst.IADD;

    /**
     * LADD instruction.
     *
     * @deprecated Use {@link InstructionConst#LADD}.
     */
    @Deprecated
    ArithmeticInstruction LADD = InstructionConst.LADD;

    /**
     * FADD instruction.
     *
     * @deprecated Use {@link InstructionConst#FADD}.
     */
    @Deprecated
    ArithmeticInstruction FADD = InstructionConst.FADD;

    /**
     * DADD instruction.
     *
     * @deprecated Use {@link InstructionConst#DADD}.
     */
    @Deprecated
    ArithmeticInstruction DADD = InstructionConst.DADD;

    /**
     * ISUB instruction.
     *
     * @deprecated Use {@link InstructionConst#ISUB}.
     */
    @Deprecated
    ArithmeticInstruction ISUB = InstructionConst.ISUB;

    /**
     * LSUB instruction.
     *
     * @deprecated Use {@link InstructionConst#LSUB}.
     */
    @Deprecated
    ArithmeticInstruction LSUB = InstructionConst.LSUB;

    /**
     * FSUB instruction.
     *
     * @deprecated Use {@link InstructionConst#FSUB}.
     */
    @Deprecated
    ArithmeticInstruction FSUB = InstructionConst.FSUB;

    /**
     * DSUB instruction.
     *
     * @deprecated Use {@link InstructionConst#DSUB}.
     */
    @Deprecated
    ArithmeticInstruction DSUB = InstructionConst.DSUB;

    /**
     * IMUL instruction.
     *
     * @deprecated Use {@link InstructionConst#IMUL}.
     */
    @Deprecated
    ArithmeticInstruction IMUL = InstructionConst.IMUL;

    /**
     * LMUL instruction.
     *
     * @deprecated Use {@link InstructionConst#LMUL}.
     */
    @Deprecated
    ArithmeticInstruction LMUL = InstructionConst.LMUL;

    /**
     * FMUL instruction.
     *
     * @deprecated Use {@link InstructionConst#FMUL}.
     */
    @Deprecated
    ArithmeticInstruction FMUL = InstructionConst.FMUL;

    /**
     * DMUL instruction.
     *
     * @deprecated Use {@link InstructionConst#DMUL}.
     */
    @Deprecated
    ArithmeticInstruction DMUL = InstructionConst.DMUL;

    /**
     * IDIV instruction.
     *
     * @deprecated Use {@link InstructionConst#IDIV}.
     */
    @Deprecated
    ArithmeticInstruction IDIV = InstructionConst.IDIV;

    /**
     * LDIV instruction.
     *
     * @deprecated Use {@link InstructionConst#LDIV}.
     */
    @Deprecated
    ArithmeticInstruction LDIV = InstructionConst.LDIV;

    /**
     * FDIV instruction.
     *
     * @deprecated Use {@link InstructionConst#FDIV}.
     */
    @Deprecated
    ArithmeticInstruction FDIV = InstructionConst.FDIV;

    /**
     * DDIV instruction.
     *
     * @deprecated Use {@link InstructionConst#DDIV}.
     */
    @Deprecated
    ArithmeticInstruction DDIV = InstructionConst.DDIV;

    /**
     * IREM instruction.
     *
     * @deprecated Use {@link InstructionConst#IREM}.
     */
    @Deprecated
    ArithmeticInstruction IREM = InstructionConst.IREM;

    /**
     * LREM instruction.
     *
     * @deprecated Use {@link InstructionConst#LREM}.
     */
    @Deprecated
    ArithmeticInstruction LREM = InstructionConst.LREM;

    /**
     * FREM instruction.
     *
     * @deprecated Use {@link InstructionConst#FREM}.
     */
    @Deprecated
    ArithmeticInstruction FREM = InstructionConst.FREM;

    /**
     * DREM instruction.
     *
     * @deprecated Use {@link InstructionConst#DREM}.
     */
    @Deprecated
    ArithmeticInstruction DREM = InstructionConst.DREM;

    /**
     * INEG instruction.
     *
     * @deprecated Use {@link InstructionConst#INEG}.
     */
    @Deprecated
    ArithmeticInstruction INEG = InstructionConst.INEG;

    /**
     * LNEG instruction.
     *
     * @deprecated Use {@link InstructionConst#LNEG}.
     */
    @Deprecated
    ArithmeticInstruction LNEG = InstructionConst.LNEG;

    /**
     * FNEG instruction.
     *
     * @deprecated Use {@link InstructionConst#FNEG}.
     */
    @Deprecated
    ArithmeticInstruction FNEG = InstructionConst.FNEG;

    /**
     * DNEG instruction.
     *
     * @deprecated Use {@link InstructionConst#DNEG}.
     */
    @Deprecated
    ArithmeticInstruction DNEG = InstructionConst.DNEG;

    /**
     * ISHL instruction.
     *
     * @deprecated Use {@link InstructionConst#ISHL}.
     */
    @Deprecated
    ArithmeticInstruction ISHL = InstructionConst.ISHL;

    /**
     * LSHL instruction.
     *
     * @deprecated Use {@link InstructionConst#LSHL}.
     */
    @Deprecated
    ArithmeticInstruction LSHL = InstructionConst.LSHL;

    /**
     * ISHR instruction.
     *
     * @deprecated Use {@link InstructionConst#ISHR}.
     */
    @Deprecated
    ArithmeticInstruction ISHR = InstructionConst.ISHR;

    /**
     * LSHR instruction.
     *
     * @deprecated Use {@link InstructionConst#LSHR}.
     */
    @Deprecated
    ArithmeticInstruction LSHR = InstructionConst.LSHR;

    /**
     * IUSHR instruction.
     *
     * @deprecated Use {@link InstructionConst#IUSHR}.
     */
    @Deprecated
    ArithmeticInstruction IUSHR = InstructionConst.IUSHR;

    /**
     * LUSHR instruction.
     *
     * @deprecated Use {@link InstructionConst#LUSHR}.
     */
    @Deprecated
    ArithmeticInstruction LUSHR = InstructionConst.LUSHR;

    /**
     * IAND instruction.
     *
     * @deprecated Use {@link InstructionConst#IAND}.
     */
    @Deprecated
    ArithmeticInstruction IAND = InstructionConst.IAND;

    /**
     * LAND instruction.
     *
     * @deprecated Use {@link InstructionConst#LAND}.
     */
    @Deprecated
    ArithmeticInstruction LAND = InstructionConst.LAND;

    /**
     * IOR instruction.
     *
     * @deprecated Use {@link InstructionConst#IOR}.
     */
    @Deprecated
    ArithmeticInstruction IOR = InstructionConst.IOR;

    /**
     * LOR instruction.
     *
     * @deprecated Use {@link InstructionConst#LOR}.
     */
    @Deprecated
    ArithmeticInstruction LOR = InstructionConst.LOR;

    /**
     * IXOR instruction.
     *
     * @deprecated Use {@link InstructionConst#IXOR}.
     */
    @Deprecated
    ArithmeticInstruction IXOR = InstructionConst.IXOR;

    /**
     * LXOR instruction.
     *
     * @deprecated Use {@link InstructionConst#LXOR}.
     */
    @Deprecated
    ArithmeticInstruction LXOR = InstructionConst.LXOR;

    /**
     * I2L instruction.
     *
     * @deprecated Use {@link InstructionConst#I2L}.
     */
    @Deprecated
    ConversionInstruction I2L = InstructionConst.I2L;

    /**
     * I2F instruction.
     *
     * @deprecated Use {@link InstructionConst#I2F}.
     */
    @Deprecated
    ConversionInstruction I2F = InstructionConst.I2F;

    /**
     * I2D instruction.
     *
     * @deprecated Use {@link InstructionConst#I2D}.
     */
    @Deprecated
    ConversionInstruction I2D = InstructionConst.I2D;

    /**
     * L2I instruction.
     *
     * @deprecated Use {@link InstructionConst#L2I}.
     */
    @Deprecated
    ConversionInstruction L2I = InstructionConst.L2I;

    /**
     * L2F instruction.
     *
     * @deprecated Use {@link InstructionConst#L2F}.
     */
    @Deprecated
    ConversionInstruction L2F = InstructionConst.L2F;

    /**
     * L2D instruction.
     *
     * @deprecated Use {@link InstructionConst#L2D}.
     */
    @Deprecated
    ConversionInstruction L2D = InstructionConst.L2D;

    /**
     * F2I instruction.
     *
     * @deprecated Use {@link InstructionConst#F2I}.
     */
    @Deprecated
    ConversionInstruction F2I = InstructionConst.F2I;

    /**
     * F2L instruction.
     *
     * @deprecated Use {@link InstructionConst#F2L}.
     */
    @Deprecated
    ConversionInstruction F2L = InstructionConst.F2L;

    /**
     * F2D instruction.
     *
     * @deprecated Use {@link InstructionConst#F2D}.
     */
    @Deprecated
    ConversionInstruction F2D = InstructionConst.F2D;

    /**
     * D2I instruction.
     *
     * @deprecated Use {@link InstructionConst#D2I}.
     */
    @Deprecated
    ConversionInstruction D2I = InstructionConst.D2I;

    /**
     * D2L instruction.
     *
     * @deprecated Use {@link InstructionConst#D2L}.
     */
    @Deprecated
    ConversionInstruction D2L = InstructionConst.D2L;

    /**
     * D2F instruction.
     *
     * @deprecated Use {@link InstructionConst#D2F}.
     */
    @Deprecated
    ConversionInstruction D2F = InstructionConst.D2F;

    /**
     * I2B instruction.
     *
     * @deprecated Use {@link InstructionConst#I2B}.
     */
    @Deprecated
    ConversionInstruction I2B = InstructionConst.I2B;

    /**
     * I2C instruction.
     *
     * @deprecated Use {@link InstructionConst#I2C}.
     */
    @Deprecated
    ConversionInstruction I2C = InstructionConst.I2C;

    /**
     * I2S instruction.
     *
     * @deprecated Use {@link InstructionConst#I2S}.
     */
    @Deprecated
    ConversionInstruction I2S = InstructionConst.I2S;

    /**
     * LCMP instruction.
     *
     * @deprecated Use {@link InstructionConst#LCMP}.
     */
    @Deprecated
    Instruction LCMP = InstructionConst.LCMP;

    /**
     * FCMPL instruction.
     *
     * @deprecated Use {@link InstructionConst#FCMPL}.
     */
    @Deprecated
    Instruction FCMPL = InstructionConst.FCMPL;

    /**
     * FCMPG instruction.
     *
     * @deprecated Use {@link InstructionConst#FCMPG}.
     */
    @Deprecated
    Instruction FCMPG = InstructionConst.FCMPG;

    /**
     * DCMPL instruction.
     *
     * @deprecated Use {@link InstructionConst#DCMPL}.
     */
    @Deprecated
    Instruction DCMPL = InstructionConst.DCMPL;

    /**
     * DCMPG instruction.
     *
     * @deprecated Use {@link InstructionConst#DCMPG}.
     */
    @Deprecated
    Instruction DCMPG = InstructionConst.DCMPG;

    /**
     * IRETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#IRETURN}.
     */
    @Deprecated
    ReturnInstruction IRETURN = InstructionConst.IRETURN;

    /**
     * LRETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#LRETURN}.
     */
    @Deprecated
    ReturnInstruction LRETURN = InstructionConst.LRETURN;

    /**
     * FRETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#FRETURN}.
     */
    @Deprecated
    ReturnInstruction FRETURN = InstructionConst.FRETURN;

    /**
     * DRETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#DRETURN}.
     */
    @Deprecated
    ReturnInstruction DRETURN = InstructionConst.DRETURN;

    /**
     * ARETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#ARETURN}.
     */
    @Deprecated
    ReturnInstruction ARETURN = InstructionConst.ARETURN;

    /**
     * RETURN instruction.
     *
     * @deprecated Use {@link InstructionConst#RETURN}.
     */
    @Deprecated
    ReturnInstruction RETURN = InstructionConst.RETURN;

    /**
     * ARRAYLENGTH instruction.
     *
     * @deprecated Use {@link InstructionConst#ARRAYLENGTH}.
     */
    @Deprecated
    Instruction ARRAYLENGTH = InstructionConst.ARRAYLENGTH;

    /**
     * ATHROW instruction.
     *
     * @deprecated Use {@link InstructionConst#ATHROW}.
     */
    @Deprecated
    Instruction ATHROW = InstructionConst.ATHROW;

    /**
     * MONITORENTER instruction.
     *
     * @deprecated Use {@link InstructionConst#MONITORENTER}.
     */
    @Deprecated
    Instruction MONITORENTER = InstructionConst.MONITORENTER;

    /**
     * MONITOREXIT instruction.
     *
     * @deprecated Use {@link InstructionConst#MONITOREXIT}.
     */
    @Deprecated
    Instruction MONITOREXIT = InstructionConst.MONITOREXIT;

    /**
     * You can use these constants in multiple places safely, if you can guarantee that you will never alter their internal values, for example call setIndex().
     *
     * @deprecated Use {@link InstructionConst#THIS}.
     */
    @Deprecated
    LocalVariableInstruction THIS = InstructionConst.THIS;

    /**
     * ALOAD_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#ALOAD_0}.
     */
    @Deprecated
    LocalVariableInstruction ALOAD_0 = InstructionConst.ALOAD_0;

    /**
     * ALOAD_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ALOAD_1}.
     */
    @Deprecated
    LocalVariableInstruction ALOAD_1 = InstructionConst.ALOAD_1;

    /**
     * ALOAD_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#ALOAD_2}.
     */
    @Deprecated
    LocalVariableInstruction ALOAD_2 = InstructionConst.ALOAD_2;

    /**
     * ILOAD_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#ILOAD_0}.
     */
    @Deprecated
    LocalVariableInstruction ILOAD_0 = InstructionConst.ILOAD_0;

    /**
     * ILOAD_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ILOAD_1}.
     */
    @Deprecated
    LocalVariableInstruction ILOAD_1 = InstructionConst.ILOAD_1;

    /**
     * ILOAD_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#ILOAD_2}.
     */
    @Deprecated
    LocalVariableInstruction ILOAD_2 = InstructionConst.ILOAD_2;

    /**
     * ASTORE_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#ASTORE_0}.
     */
    @Deprecated
    LocalVariableInstruction ASTORE_0 = InstructionConst.ASTORE_0;

    /**
     * ASTORE_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ASTORE_1}.
     */
    @Deprecated
    LocalVariableInstruction ASTORE_1 = InstructionConst.ASTORE_1;

    /**
     * ASTORE_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#ASTORE_2}.
     */
    @Deprecated
    LocalVariableInstruction ASTORE_2 = InstructionConst.ASTORE_2;

    /**
     * ISTORE_0 instruction.
     *
     * @deprecated Use {@link InstructionConst#ISTORE_0}.
     */
    @Deprecated
    LocalVariableInstruction ISTORE_0 = InstructionConst.ISTORE_0;

    /**
     * ISTORE_1 instruction.
     *
     * @deprecated Use {@link InstructionConst#ISTORE_1}.
     */
    @Deprecated
    LocalVariableInstruction ISTORE_1 = InstructionConst.ISTORE_1;

    /**
     * ISTORE_2 instruction.
     *
     * @deprecated Use {@link InstructionConst#ISTORE_2}.
     */
    @Deprecated
    LocalVariableInstruction ISTORE_2 = InstructionConst.ISTORE_2;

    /**
     * Gets object via its opcode, for immutable instructions like branch instructions entries are set to null.
     *
     * @deprecated Use {@link InstructionConst#INSTRUCTIONS}.
     */
    @Deprecated
    Instruction[] INSTRUCTIONS = InstructionConst.INSTRUCTIONS;

    /**
     * Interfaces may have no static initializers, so we simulate this with an inner class.
     */
    Clinit bla = new Clinit();
}
