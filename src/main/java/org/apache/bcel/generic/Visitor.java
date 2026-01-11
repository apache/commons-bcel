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
 * Interface implementing the Visitor pattern programming style. I.e., a class that implements this interface can handle
 * all types of instructions with the properly typed methods just by calling the accept() method.
 */
public interface Visitor {

    /**
     * Visits an AALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitAALOAD(AALOAD obj);

    /**
     * Visits an AASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitAASTORE(AASTORE obj);

    /**
     * Visits an ACONST_NULL instruction.
     *
     * @param obj the instruction.
     */
    void visitACONST_NULL(ACONST_NULL obj);

    /**
     * Visits an AllocationInstruction.
     *
     * @param obj the instruction.
     */
    void visitAllocationInstruction(AllocationInstruction obj);

    /**
     * Visits an ALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitALOAD(ALOAD obj);

    /**
     * Visits an ANEWARRAY instruction.
     *
     * @param obj the instruction.
     */
    void visitANEWARRAY(ANEWARRAY obj);

    /**
     * Visits an ARETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitARETURN(ARETURN obj);

    /**
     * Visits an ArithmeticInstruction.
     *
     * @param obj the instruction.
     */
    void visitArithmeticInstruction(ArithmeticInstruction obj);

    /**
     * Visits an ArrayInstruction.
     *
     * @param obj the instruction.
     */
    void visitArrayInstruction(ArrayInstruction obj);

    /**
     * Visits an ARRAYLENGTH instruction.
     *
     * @param obj the instruction.
     */
    void visitARRAYLENGTH(ARRAYLENGTH obj);

    /**
     * Visits an ASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitASTORE(ASTORE obj);

    /**
     * Visits an ATHROW instruction.
     *
     * @param obj the instruction.
     */
    void visitATHROW(ATHROW obj);

    /**
     * Visits a BALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitBALOAD(BALOAD obj);

    void visitBASTORE(BASTORE obj);

    /**
     * Visits a BIPUSH instruction.
     *
     * @param obj the instruction.
     */
    void visitBIPUSH(BIPUSH obj);

    /**
     * Visits a BranchInstruction.
     *
     * @param obj the instruction.
     */
    void visitBranchInstruction(BranchInstruction obj);

    /**
     * Visits a BREAKPOINT instruction.
     *
     * @param obj the instruction.
     */
    void visitBREAKPOINT(BREAKPOINT obj);

    /**
     * Visits a CALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitCALOAD(CALOAD obj);

    /**
     * Visits a CASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitCASTORE(CASTORE obj);

    /**
     * Visits a CHECKCAST instruction.
     *
     * @param obj the instruction.
     */
    void visitCHECKCAST(CHECKCAST obj);

    /**
     * Visits a ConstantPushInstruction.
     *
     * @param obj the instruction.
     */
    void visitConstantPushInstruction(ConstantPushInstruction obj);

    /**
     * Visits a ConversionInstruction.
     *
     * @param obj the instruction.
     */
    void visitConversionInstruction(ConversionInstruction obj);

    /**
     * Visits a CPInstruction.
     *
     * @param obj the instruction.
     */
    void visitCPInstruction(CPInstruction obj);

    /**
     * Visits a D2F instruction.
     *
     * @param obj the instruction.
     */
    void visitD2F(D2F obj);

    /**
     * Visits a D2I instruction.
     *
     * @param obj the instruction.
     */
    void visitD2I(D2I obj);

    /**
     * Visits a D2L instruction.
     *
     * @param obj the instruction.
     */
    void visitD2L(D2L obj);

    /**
     * Visits a DADD instruction.
     *
     * @param obj the instruction.
     */
    void visitDADD(DADD obj);

    /**
     * Visits a DALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitDALOAD(DALOAD obj);

    /**
     * Visits a DASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitDASTORE(DASTORE obj);

    /**
     * Visits a DCMPG instruction.
     *
     * @param obj the instruction.
     */
    void visitDCMPG(DCMPG obj);

    /**
     * Visits a DCMPL instruction.
     *
     * @param obj the instruction.
     */
    void visitDCMPL(DCMPL obj);

    /**
     * Visits a DCONST instruction.
     *
     * @param obj the instruction.
     */
    void visitDCONST(DCONST obj);

    void visitDDIV(DDIV obj);

    /**
     * Visits a DLOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitDLOAD(DLOAD obj);

    /**
     * Visits a DMUL instruction.
     *
     * @param obj the instruction.
     */
    void visitDMUL(DMUL obj);

    void visitDNEG(DNEG obj);

    void visitDREM(DREM obj);

    void visitDRETURN(DRETURN obj);

    void visitDSTORE(DSTORE obj);

    void visitDSUB(DSUB obj);

    void visitDUP(DUP obj);

    void visitDUP_X1(DUP_X1 obj);

    void visitDUP_X2(DUP_X2 obj);

    void visitDUP2(DUP2 obj);

    void visitDUP2_X1(DUP2_X1 obj);

    void visitDUP2_X2(DUP2_X2 obj);

    void visitExceptionThrower(ExceptionThrower obj);

    void visitF2D(F2D obj);

    void visitF2I(F2I obj);

    void visitF2L(F2L obj);

    void visitFADD(FADD obj);

    void visitFALOAD(FALOAD obj);

    void visitFASTORE(FASTORE obj);

    void visitFCMPG(FCMPG obj);

    void visitFCMPL(FCMPL obj);

    void visitFCONST(FCONST obj);

    void visitFDIV(FDIV obj);

    void visitFieldInstruction(FieldInstruction obj);

    void visitFieldOrMethod(FieldOrMethod obj);

    void visitFLOAD(FLOAD obj);

    void visitFMUL(FMUL obj);

    void visitFNEG(FNEG obj);

    void visitFREM(FREM obj);

    void visitFRETURN(FRETURN obj);

    void visitFSTORE(FSTORE obj);

    void visitFSUB(FSUB obj);

    void visitGETFIELD(GETFIELD obj);

    void visitGETSTATIC(GETSTATIC obj);

    void visitGOTO(GOTO obj);

    void visitGOTO_W(GOTO_W obj);

    void visitGotoInstruction(GotoInstruction obj);

    void visitI2B(I2B obj);

    void visitI2C(I2C obj);

    void visitI2D(I2D obj);

    void visitI2F(I2F obj);

    void visitI2L(I2L obj);

    void visitI2S(I2S obj);

    void visitIADD(IADD obj);

    void visitIALOAD(IALOAD obj);

    void visitIAND(IAND obj);

    void visitIASTORE(IASTORE obj);

    void visitICONST(ICONST obj);

    void visitIDIV(IDIV obj);

    void visitIF_ACMPEQ(IF_ACMPEQ obj);

    void visitIF_ACMPNE(IF_ACMPNE obj);

    void visitIF_ICMPEQ(IF_ICMPEQ obj);

    void visitIF_ICMPGE(IF_ICMPGE obj);

    void visitIF_ICMPGT(IF_ICMPGT obj);

    void visitIF_ICMPLE(IF_ICMPLE obj);

    void visitIF_ICMPLT(IF_ICMPLT obj);

    void visitIF_ICMPNE(IF_ICMPNE obj);

    void visitIFEQ(IFEQ obj);

    void visitIFGE(IFGE obj);

    void visitIFGT(IFGT obj);

    void visitIfInstruction(IfInstruction obj);

    void visitIFLE(IFLE obj);

    void visitIFLT(IFLT obj);

    void visitIFNE(IFNE obj);

    void visitIFNONNULL(IFNONNULL obj);

    void visitIFNULL(IFNULL obj);

    void visitIINC(IINC obj);

    void visitILOAD(ILOAD obj);

    void visitIMPDEP1(IMPDEP1 obj);

    void visitIMPDEP2(IMPDEP2 obj);

    void visitIMUL(IMUL obj);

    /**
     * Visits an INEG instruction.
     *
     * @param obj the instruction.
     */
    void visitINEG(INEG obj);

    /**
     * Visits an INSTANCEOF instruction.
     *
     * @param obj the instruction.
     */
    void visitINSTANCEOF(INSTANCEOF obj);

    /**
     * @since 6.0
     */
    void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);

    void visitInvokeInstruction(InvokeInstruction obj);

    void visitINVOKEINTERFACE(INVOKEINTERFACE obj);

    void visitINVOKESPECIAL(INVOKESPECIAL obj);

    void visitINVOKESTATIC(INVOKESTATIC obj);

    void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj);

    void visitIOR(IOR obj);

    void visitIREM(IREM obj);

    void visitIRETURN(IRETURN obj);

    void visitISHL(ISHL obj);

    void visitISHR(ISHR obj);

    void visitISTORE(ISTORE obj);

    void visitISUB(ISUB obj);

    void visitIUSHR(IUSHR obj);

    void visitIXOR(IXOR obj);

    void visitJSR(JSR obj);

    void visitJSR_W(JSR_W obj);

    void visitJsrInstruction(JsrInstruction obj);

    void visitL2D(L2D obj);

    void visitL2F(L2F obj);

    void visitL2I(L2I obj);

    void visitLADD(LADD obj);

    void visitLALOAD(LALOAD obj);

    void visitLAND(LAND obj);

    void visitLASTORE(LASTORE obj);

    void visitLCMP(LCMP obj);

    void visitLCONST(LCONST obj);

    void visitLDC(LDC obj);

    void visitLDC2_W(LDC2_W obj);

    void visitLDIV(LDIV obj);

    void visitLLOAD(LLOAD obj);

    void visitLMUL(LMUL obj);

    void visitLNEG(LNEG obj);

    void visitLoadClass(LoadClass obj);

    void visitLoadInstruction(LoadInstruction obj);

    void visitLocalVariableInstruction(LocalVariableInstruction obj);

    void visitLOOKUPSWITCH(LOOKUPSWITCH obj);

    void visitLOR(LOR obj);

    void visitLREM(LREM obj);

    void visitLRETURN(LRETURN obj);

    void visitLSHL(LSHL obj);

    void visitLSHR(LSHR obj);

    void visitLSTORE(LSTORE obj);

    void visitLSUB(LSUB obj);

    void visitLUSHR(LUSHR obj);

    void visitLXOR(LXOR obj);

    void visitMONITORENTER(MONITORENTER obj);

    void visitMONITOREXIT(MONITOREXIT obj);

    void visitMULTIANEWARRAY(MULTIANEWARRAY obj);

    void visitNEW(NEW obj);

    void visitNEWARRAY(NEWARRAY obj);

    void visitNOP(NOP obj);

    void visitPOP(POP obj);

    void visitPOP2(POP2 obj);

    void visitPopInstruction(PopInstruction obj);

    void visitPushInstruction(PushInstruction obj);

    void visitPUTFIELD(PUTFIELD obj);

    void visitPUTSTATIC(PUTSTATIC obj);

    void visitRET(RET obj);

    void visitRETURN(RETURN obj);

    void visitReturnInstruction(ReturnInstruction obj);

    void visitSALOAD(SALOAD obj);

    void visitSASTORE(SASTORE obj);

    void visitSelect(Select obj);

    void visitSIPUSH(SIPUSH obj);

    void visitStackConsumer(StackConsumer obj);

    void visitStackInstruction(StackInstruction obj);

    void visitStackProducer(StackProducer obj);

    void visitStoreInstruction(StoreInstruction obj);

    void visitSWAP(SWAP obj);

    void visitTABLESWITCH(TABLESWITCH obj);

    void visitTypedInstruction(TypedInstruction obj);

    void visitUnconditionalBranch(UnconditionalBranch obj);

    void visitVariableLengthInstruction(VariableLengthInstruction obj);
}
