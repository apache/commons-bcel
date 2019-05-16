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

import org.apache.bcel.Const;

/**
 * This interface contains shareable instruction objects.
 *
 * In order to save memory you can use some instructions multiply,
 * since they have an immutable state and are directly derived from
 * Instruction.  I.e. they have no instance fields that could be
 * changed. Since some of these instructions like ICONST_0 occur
 * very frequently this can save a lot of time and space. This
 * feature is an adaptation of the FlyWeight design pattern, we
 * just use an array instead of a factory.
 *
 * The Instructions can also accessed directly under their names, so
 * it's possible to write il.append(Instruction.ICONST_0);
 *
 * @deprecated (since 6.0) Do not use. Use InstructionConst instead.
 */
@Deprecated
public interface InstructionConstants {

    /** Predefined instruction objects
     */
    /*
     * NOTE these are not currently immutable, because Instruction
     * has mutable protected fields opcode and length.
     */
    Instruction NOP = new NOP();
    Instruction ACONST_NULL = new ACONST_NULL();
    Instruction ICONST_M1 = new ICONST(-1);
    Instruction ICONST_0 = new ICONST(0);
    Instruction ICONST_1 = new ICONST(1);
    Instruction ICONST_2 = new ICONST(2);
    Instruction ICONST_3 = new ICONST(3);
    Instruction ICONST_4 = new ICONST(4);
    Instruction ICONST_5 = new ICONST(5);
    Instruction LCONST_0 = new LCONST(0);
    Instruction LCONST_1 = new LCONST(1);
    Instruction FCONST_0 = new FCONST(0);
    Instruction FCONST_1 = new FCONST(1);
    Instruction FCONST_2 = new FCONST(2);
    Instruction DCONST_0 = new DCONST(0);
    Instruction DCONST_1 = new DCONST(1);
    ArrayInstruction IALOAD = new IALOAD();
    ArrayInstruction LALOAD = new LALOAD();
    ArrayInstruction FALOAD = new FALOAD();
    ArrayInstruction DALOAD = new DALOAD();
    ArrayInstruction AALOAD = new AALOAD();
    ArrayInstruction BALOAD = new BALOAD();
    ArrayInstruction CALOAD = new CALOAD();
    ArrayInstruction SALOAD = new SALOAD();
    ArrayInstruction IASTORE = new IASTORE();
    ArrayInstruction LASTORE = new LASTORE();
    ArrayInstruction FASTORE = new FASTORE();
    ArrayInstruction DASTORE = new DASTORE();
    ArrayInstruction AASTORE = new AASTORE();
    ArrayInstruction BASTORE = new BASTORE();
    ArrayInstruction CASTORE = new CASTORE();
    ArrayInstruction SASTORE = new SASTORE();
    StackInstruction POP = new POP();
    StackInstruction POP2 = new POP2();
    StackInstruction DUP = new DUP();
    StackInstruction DUP_X1 = new DUP_X1();
    StackInstruction DUP_X2 = new DUP_X2();
    StackInstruction DUP2 = new DUP2();
    StackInstruction DUP2_X1 = new DUP2_X1();
    StackInstruction DUP2_X2 = new DUP2_X2();
    StackInstruction SWAP = new SWAP();
    ArithmeticInstruction IADD = new IADD();
    ArithmeticInstruction LADD = new LADD();
    ArithmeticInstruction FADD = new FADD();
    ArithmeticInstruction DADD = new DADD();
    ArithmeticInstruction ISUB = new ISUB();
    ArithmeticInstruction LSUB = new LSUB();
    ArithmeticInstruction FSUB = new FSUB();
    ArithmeticInstruction DSUB = new DSUB();
    ArithmeticInstruction IMUL = new IMUL();
    ArithmeticInstruction LMUL = new LMUL();
    ArithmeticInstruction FMUL = new FMUL();
    ArithmeticInstruction DMUL = new DMUL();
    ArithmeticInstruction IDIV = new IDIV();
    ArithmeticInstruction LDIV = new LDIV();
    ArithmeticInstruction FDIV = new FDIV();
    ArithmeticInstruction DDIV = new DDIV();
    ArithmeticInstruction IREM = new IREM();
    ArithmeticInstruction LREM = new LREM();
    ArithmeticInstruction FREM = new FREM();
    ArithmeticInstruction DREM = new DREM();
    ArithmeticInstruction INEG = new INEG();
    ArithmeticInstruction LNEG = new LNEG();
    ArithmeticInstruction FNEG = new FNEG();
    ArithmeticInstruction DNEG = new DNEG();
    ArithmeticInstruction ISHL = new ISHL();
    ArithmeticInstruction LSHL = new LSHL();
    ArithmeticInstruction ISHR = new ISHR();
    ArithmeticInstruction LSHR = new LSHR();
    ArithmeticInstruction IUSHR = new IUSHR();
    ArithmeticInstruction LUSHR = new LUSHR();
    ArithmeticInstruction IAND = new IAND();
    ArithmeticInstruction LAND = new LAND();
    ArithmeticInstruction IOR = new IOR();
    ArithmeticInstruction LOR = new LOR();
    ArithmeticInstruction IXOR = new IXOR();
    ArithmeticInstruction LXOR = new LXOR();
    ConversionInstruction I2L = new I2L();
    ConversionInstruction I2F = new I2F();
    ConversionInstruction I2D = new I2D();
    ConversionInstruction L2I = new L2I();
    ConversionInstruction L2F = new L2F();
    ConversionInstruction L2D = new L2D();
    ConversionInstruction F2I = new F2I();
    ConversionInstruction F2L = new F2L();
    ConversionInstruction F2D = new F2D();
    ConversionInstruction D2I = new D2I();
    ConversionInstruction D2L = new D2L();
    ConversionInstruction D2F = new D2F();
    ConversionInstruction I2B = new I2B();
    ConversionInstruction I2C = new I2C();
    ConversionInstruction I2S = new I2S();
    Instruction LCMP = new LCMP();
    Instruction FCMPL = new FCMPL();
    Instruction FCMPG = new FCMPG();
    Instruction DCMPL = new DCMPL();
    Instruction DCMPG = new DCMPG();
    ReturnInstruction IRETURN = new IRETURN();
    ReturnInstruction LRETURN = new LRETURN();
    ReturnInstruction FRETURN = new FRETURN();
    ReturnInstruction DRETURN = new DRETURN();
    ReturnInstruction ARETURN = new ARETURN();
    ReturnInstruction RETURN = new RETURN();
    Instruction ARRAYLENGTH = new ARRAYLENGTH();
    Instruction ATHROW = new ATHROW();
    Instruction MONITORENTER = new MONITORENTER();
    Instruction MONITOREXIT = new MONITOREXIT();
    /** You can use these constants in multiple places safely, if you can guarantee
     * that you will never alter their internal values, e.g. call setIndex().
     */
    LocalVariableInstruction THIS = new ALOAD(0);
    LocalVariableInstruction ALOAD_0 = THIS;
    LocalVariableInstruction ALOAD_1 = new ALOAD(1);
    LocalVariableInstruction ALOAD_2 = new ALOAD(2);
    LocalVariableInstruction ILOAD_0 = new ILOAD(0);
    LocalVariableInstruction ILOAD_1 = new ILOAD(1);
    LocalVariableInstruction ILOAD_2 = new ILOAD(2);
    LocalVariableInstruction ASTORE_0 = new ASTORE(0);
    LocalVariableInstruction ASTORE_1 = new ASTORE(1);
    LocalVariableInstruction ASTORE_2 = new ASTORE(2);
    LocalVariableInstruction ISTORE_0 = new ISTORE(0);
    LocalVariableInstruction ISTORE_1 = new ISTORE(1);
    LocalVariableInstruction ISTORE_2 = new ISTORE(2);
    /** Get object via its opcode, for immutable instructions like
     * branch instructions entries are set to null.
     */
    Instruction[] INSTRUCTIONS = new Instruction[256];
    /** Interfaces may have no static initializers, so we simulate this
     * with an inner class.
     */
    Clinit bla = new Clinit();

    class Clinit {

        Clinit() {
            INSTRUCTIONS[Const.NOP] = NOP;
            INSTRUCTIONS[Const.ACONST_NULL] = ACONST_NULL;
            INSTRUCTIONS[Const.ICONST_M1] = ICONST_M1;
            INSTRUCTIONS[Const.ICONST_0] = ICONST_0;
            INSTRUCTIONS[Const.ICONST_1] = ICONST_1;
            INSTRUCTIONS[Const.ICONST_2] = ICONST_2;
            INSTRUCTIONS[Const.ICONST_3] = ICONST_3;
            INSTRUCTIONS[Const.ICONST_4] = ICONST_4;
            INSTRUCTIONS[Const.ICONST_5] = ICONST_5;
            INSTRUCTIONS[Const.LCONST_0] = LCONST_0;
            INSTRUCTIONS[Const.LCONST_1] = LCONST_1;
            INSTRUCTIONS[Const.FCONST_0] = FCONST_0;
            INSTRUCTIONS[Const.FCONST_1] = FCONST_1;
            INSTRUCTIONS[Const.FCONST_2] = FCONST_2;
            INSTRUCTIONS[Const.DCONST_0] = DCONST_0;
            INSTRUCTIONS[Const.DCONST_1] = DCONST_1;
            INSTRUCTIONS[Const.IALOAD] = IALOAD;
            INSTRUCTIONS[Const.LALOAD] = LALOAD;
            INSTRUCTIONS[Const.FALOAD] = FALOAD;
            INSTRUCTIONS[Const.DALOAD] = DALOAD;
            INSTRUCTIONS[Const.AALOAD] = AALOAD;
            INSTRUCTIONS[Const.BALOAD] = BALOAD;
            INSTRUCTIONS[Const.CALOAD] = CALOAD;
            INSTRUCTIONS[Const.SALOAD] = SALOAD;
            INSTRUCTIONS[Const.IASTORE] = IASTORE;
            INSTRUCTIONS[Const.LASTORE] = LASTORE;
            INSTRUCTIONS[Const.FASTORE] = FASTORE;
            INSTRUCTIONS[Const.DASTORE] = DASTORE;
            INSTRUCTIONS[Const.AASTORE] = AASTORE;
            INSTRUCTIONS[Const.BASTORE] = BASTORE;
            INSTRUCTIONS[Const.CASTORE] = CASTORE;
            INSTRUCTIONS[Const.SASTORE] = SASTORE;
            INSTRUCTIONS[Const.POP] = POP;
            INSTRUCTIONS[Const.POP2] = POP2;
            INSTRUCTIONS[Const.DUP] = DUP;
            INSTRUCTIONS[Const.DUP_X1] = DUP_X1;
            INSTRUCTIONS[Const.DUP_X2] = DUP_X2;
            INSTRUCTIONS[Const.DUP2] = DUP2;
            INSTRUCTIONS[Const.DUP2_X1] = DUP2_X1;
            INSTRUCTIONS[Const.DUP2_X2] = DUP2_X2;
            INSTRUCTIONS[Const.SWAP] = SWAP;
            INSTRUCTIONS[Const.IADD] = IADD;
            INSTRUCTIONS[Const.LADD] = LADD;
            INSTRUCTIONS[Const.FADD] = FADD;
            INSTRUCTIONS[Const.DADD] = DADD;
            INSTRUCTIONS[Const.ISUB] = ISUB;
            INSTRUCTIONS[Const.LSUB] = LSUB;
            INSTRUCTIONS[Const.FSUB] = FSUB;
            INSTRUCTIONS[Const.DSUB] = DSUB;
            INSTRUCTIONS[Const.IMUL] = IMUL;
            INSTRUCTIONS[Const.LMUL] = LMUL;
            INSTRUCTIONS[Const.FMUL] = FMUL;
            INSTRUCTIONS[Const.DMUL] = DMUL;
            INSTRUCTIONS[Const.IDIV] = IDIV;
            INSTRUCTIONS[Const.LDIV] = LDIV;
            INSTRUCTIONS[Const.FDIV] = FDIV;
            INSTRUCTIONS[Const.DDIV] = DDIV;
            INSTRUCTIONS[Const.IREM] = IREM;
            INSTRUCTIONS[Const.LREM] = LREM;
            INSTRUCTIONS[Const.FREM] = FREM;
            INSTRUCTIONS[Const.DREM] = DREM;
            INSTRUCTIONS[Const.INEG] = INEG;
            INSTRUCTIONS[Const.LNEG] = LNEG;
            INSTRUCTIONS[Const.FNEG] = FNEG;
            INSTRUCTIONS[Const.DNEG] = DNEG;
            INSTRUCTIONS[Const.ISHL] = ISHL;
            INSTRUCTIONS[Const.LSHL] = LSHL;
            INSTRUCTIONS[Const.ISHR] = ISHR;
            INSTRUCTIONS[Const.LSHR] = LSHR;
            INSTRUCTIONS[Const.IUSHR] = IUSHR;
            INSTRUCTIONS[Const.LUSHR] = LUSHR;
            INSTRUCTIONS[Const.IAND] = IAND;
            INSTRUCTIONS[Const.LAND] = LAND;
            INSTRUCTIONS[Const.IOR] = IOR;
            INSTRUCTIONS[Const.LOR] = LOR;
            INSTRUCTIONS[Const.IXOR] = IXOR;
            INSTRUCTIONS[Const.LXOR] = LXOR;
            INSTRUCTIONS[Const.I2L] = I2L;
            INSTRUCTIONS[Const.I2F] = I2F;
            INSTRUCTIONS[Const.I2D] = I2D;
            INSTRUCTIONS[Const.L2I] = L2I;
            INSTRUCTIONS[Const.L2F] = L2F;
            INSTRUCTIONS[Const.L2D] = L2D;
            INSTRUCTIONS[Const.F2I] = F2I;
            INSTRUCTIONS[Const.F2L] = F2L;
            INSTRUCTIONS[Const.F2D] = F2D;
            INSTRUCTIONS[Const.D2I] = D2I;
            INSTRUCTIONS[Const.D2L] = D2L;
            INSTRUCTIONS[Const.D2F] = D2F;
            INSTRUCTIONS[Const.I2B] = I2B;
            INSTRUCTIONS[Const.I2C] = I2C;
            INSTRUCTIONS[Const.I2S] = I2S;
            INSTRUCTIONS[Const.LCMP] = LCMP;
            INSTRUCTIONS[Const.FCMPL] = FCMPL;
            INSTRUCTIONS[Const.FCMPG] = FCMPG;
            INSTRUCTIONS[Const.DCMPL] = DCMPL;
            INSTRUCTIONS[Const.DCMPG] = DCMPG;
            INSTRUCTIONS[Const.IRETURN] = IRETURN;
            INSTRUCTIONS[Const.LRETURN] = LRETURN;
            INSTRUCTIONS[Const.FRETURN] = FRETURN;
            INSTRUCTIONS[Const.DRETURN] = DRETURN;
            INSTRUCTIONS[Const.ARETURN] = ARETURN;
            INSTRUCTIONS[Const.RETURN] = RETURN;
            INSTRUCTIONS[Const.ARRAYLENGTH] = ARRAYLENGTH;
            INSTRUCTIONS[Const.ATHROW] = ATHROW;
            INSTRUCTIONS[Const.MONITORENTER] = MONITORENTER;
            INSTRUCTIONS[Const.MONITOREXIT] = MONITOREXIT;
        }
    }
}
