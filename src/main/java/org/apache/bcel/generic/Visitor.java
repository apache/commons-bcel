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

    /**
     * Visits a BASTORE instruction.
     *
     * @param obj the instruction.
     */
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

    /**
     * Visits a DDIV instruction.
     *
     * @param obj the instruction.
     */
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

    /**
     * Visits a DNEG instruction.
     *
     * @param obj the instruction.
     */
    void visitDNEG(DNEG obj);

    /**
     * Visits a DREM instruction.
     *
     * @param obj the instruction.
     */
    void visitDREM(DREM obj);

    /**
     * Visits a DRETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitDRETURN(DRETURN obj);

    /**
     * Visits a DSTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitDSTORE(DSTORE obj);

    /**
     * Visits a DSUB instruction.
     *
     * @param obj the instruction.
     */
    void visitDSUB(DSUB obj);

    /**
     * Visits a DUP instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP(DUP obj);

    /**
     * Visits a DUP_X1 instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP_X1(DUP_X1 obj);

    /**
     * Visits a DUP_X2 instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP_X2(DUP_X2 obj);

    /**
     * Visits a DUP2 instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP2(DUP2 obj);

    /**
     * Visits a DUP2_X1 instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP2_X1(DUP2_X1 obj);

    /**
     * Visits a DUP2_X2 instruction.
     *
     * @param obj the instruction.
     */
    void visitDUP2_X2(DUP2_X2 obj);

    /**
     * Visits an ExceptionThrower.
     *
     * @param obj the instruction.
     */
    void visitExceptionThrower(ExceptionThrower obj);

    /**
     * Visits an F2D instruction.
     *
     * @param obj the instruction.
     */
    void visitF2D(F2D obj);

    /**
     * Visits an F2I instruction.
     *
     * @param obj the instruction.
     */
    void visitF2I(F2I obj);

    /**
     * Visits an F2L instruction.
     *
     * @param obj the instruction.
     */
    void visitF2L(F2L obj);

    /**
     * Visits an FADD instruction.
     *
     * @param obj the instruction.
     */
    void visitFADD(FADD obj);

    /**
     * Visits an FALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitFALOAD(FALOAD obj);

    /**
     * Visits an FASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitFASTORE(FASTORE obj);

    /**
     * Visits an FCMPG instruction.
     *
     * @param obj the instruction.
     */
    void visitFCMPG(FCMPG obj);

    /**
     * Visits an FCMPL instruction.
     *
     * @param obj the instruction.
     */
    void visitFCMPL(FCMPL obj);

    /**
     * Visits an FCONST instruction.
     *
     * @param obj the instruction.
     */
    void visitFCONST(FCONST obj);

    /**
     * Visits an FDIV instruction.
     *
     * @param obj the instruction.
     */
    void visitFDIV(FDIV obj);

    /**
     * Visits a FieldInstruction.
     *
     * @param obj the instruction.
     */
    void visitFieldInstruction(FieldInstruction obj);

    /**
     * Visits a FieldOrMethod.
     *
     * @param obj the instruction.
     */
    void visitFieldOrMethod(FieldOrMethod obj);

    /**
     * Visits an FLOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitFLOAD(FLOAD obj);

    /**
     * Visits an FMUL instruction.
     *
     * @param obj the instruction.
     */
    void visitFMUL(FMUL obj);

    /**
     * Visits an FNEG instruction.
     *
     * @param obj the instruction.
     */
    void visitFNEG(FNEG obj);

    /**
     * Visits an FREM instruction.
     *
     * @param obj the instruction.
     */
    void visitFREM(FREM obj);

    /**
     * Visits an FRETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitFRETURN(FRETURN obj);

    /**
     * Visits an FSTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitFSTORE(FSTORE obj);

    /**
     * Visits an FSUB instruction.
     *
     * @param obj the instruction.
     */
    void visitFSUB(FSUB obj);

    /**
     * Visits a GETFIELD instruction.
     *
     * @param obj the instruction.
     */
    void visitGETFIELD(GETFIELD obj);

    /**
     * Visits a GETSTATIC instruction.
     *
     * @param obj the instruction.
     */
    void visitGETSTATIC(GETSTATIC obj);

    /**
     * Visits a GOTO instruction.
     *
     * @param obj the instruction.
     */
    void visitGOTO(GOTO obj);

    /**
     * Visits a GOTO_W instruction.
     *
     * @param obj the instruction.
     */
    void visitGOTO_W(GOTO_W obj);

    /**
     * Visits a GotoInstruction.
     *
     * @param obj the instruction.
     */
    void visitGotoInstruction(GotoInstruction obj);

    /**
     * Visits an I2B instruction.
     *
     * @param obj the instruction.
     */
    void visitI2B(I2B obj);

    /**
     * Visits an I2C instruction.
     *
     * @param obj the instruction.
     */
    void visitI2C(I2C obj);

    /**
     * Visits an I2D instruction.
     *
     * @param obj the instruction.
     */
    void visitI2D(I2D obj);

    /**
     * Visits an I2F instruction.
     *
     * @param obj the instruction.
     */
    void visitI2F(I2F obj);

    /**
     * Visits an I2L instruction.
     *
     * @param obj the instruction.
     */
    void visitI2L(I2L obj);

    /**
     * Visits an I2S instruction.
     *
     * @param obj the instruction.
     */
    void visitI2S(I2S obj);

    /**
     * Visits an IADD instruction.
     *
     * @param obj the instruction.
     */
    void visitIADD(IADD obj);

    /**
     * Visits an IALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitIALOAD(IALOAD obj);

    /**
     * Visits an IAND instruction.
     *
     * @param obj the instruction.
     */
    void visitIAND(IAND obj);

    /**
     * Visits an IASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitIASTORE(IASTORE obj);

    /**
     * Visits an ICONST instruction.
     *
     * @param obj the instruction.
     */
    void visitICONST(ICONST obj);

    /**
     * Visits an IDIV instruction.
     *
     * @param obj the instruction.
     */
    void visitIDIV(IDIV obj);

    /**
     * Visits an IF_ACMPEQ instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ACMPEQ(IF_ACMPEQ obj);

    /**
     * Visits an IF_ACMPNE instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ACMPNE(IF_ACMPNE obj);

    /**
     * Visits an IF_ICMPEQ instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPEQ(IF_ICMPEQ obj);

    /**
     * Visits an IF_ICMPGE instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPGE(IF_ICMPGE obj);

    /**
     * Visits an IF_ICMPGT instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPGT(IF_ICMPGT obj);

    /**
     * Visits an IF_ICMPLE instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPLE(IF_ICMPLE obj);

    /**
     * Visits an IF_ICMPLT instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPLT(IF_ICMPLT obj);

    /**
     * Visits an IF_ICMPNE instruction.
     *
     * @param obj the instruction.
     */
    void visitIF_ICMPNE(IF_ICMPNE obj);

    /**
     * Visits an IFEQ instruction.
     *
     * @param obj the instruction.
     */
    void visitIFEQ(IFEQ obj);

    /**
     * Visits an IFGE instruction.
     *
     * @param obj the instruction.
     */
    void visitIFGE(IFGE obj);

    /**
     * Visits an IFGT instruction.
     *
     * @param obj the instruction.
     */
    void visitIFGT(IFGT obj);

    /**
     * Visits an IfInstruction.
     *
     * @param obj the instruction.
     */
    void visitIfInstruction(IfInstruction obj);

    /**
     * Visits an IFLE instruction.
     *
     * @param obj the instruction.
     */
    void visitIFLE(IFLE obj);

    /**
     * Visits an IFLT instruction.
     *
     * @param obj the instruction.
     */
    void visitIFLT(IFLT obj);

    /**
     * Visits an IFNE instruction.
     *
     * @param obj the instruction.
     */
    void visitIFNE(IFNE obj);

    /**
     * Visits an IFNONNULL instruction.
     *
     * @param obj the instruction.
     */
    void visitIFNONNULL(IFNONNULL obj);

    /**
     * Visits an IFNULL instruction.
     *
     * @param obj the instruction.
     */
    void visitIFNULL(IFNULL obj);

    /**
     * Visits an IINC instruction.
     *
     * @param obj the instruction.
     */
    void visitIINC(IINC obj);

    /**
     * Visits an ILOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitILOAD(ILOAD obj);

    /**
     * Visits an IMPDEP1 instruction.
     *
     * @param obj the instruction.
     */
    void visitIMPDEP1(IMPDEP1 obj);

    /**
     * Visits an IMPDEP2 instruction.
     *
     * @param obj the instruction.
     */
    void visitIMPDEP2(IMPDEP2 obj);

    /**
     * Visits an IMUL instruction.
     *
     * @param obj the instruction.
     */
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
     * Visits an INVOKEDYNAMIC instruction.
     *
     * @param obj the instruction.
     * @since 6.0
     */
    void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);

    /**
     * Visits an InvokeInstruction.
     *
     * @param obj the instruction.
     */
    void visitInvokeInstruction(InvokeInstruction obj);

    /**
     * Visits an INVOKEINTERFACE instruction.
     *
     * @param obj the instruction.
     */
    void visitINVOKEINTERFACE(INVOKEINTERFACE obj);

    /**
     * Visits an INVOKESPECIAL instruction.
     *
     * @param obj the instruction.
     */
    void visitINVOKESPECIAL(INVOKESPECIAL obj);

    /**
     * Visits an INVOKESTATIC instruction.
     *
     * @param obj the instruction.
     */
    void visitINVOKESTATIC(INVOKESTATIC obj);

    /**
     * Visits an INVOKEVIRTUAL instruction.
     *
     * @param obj the instruction.
     */
    void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj);

    /**
     * Visits an IOR instruction.
     *
     * @param obj the instruction.
     */
    void visitIOR(IOR obj);

    /**
     * Visits an IREM instruction.
     *
     * @param obj the instruction.
     */
    void visitIREM(IREM obj);

    /**
     * Visits an IRETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitIRETURN(IRETURN obj);

    /**
     * Visits an ISHL instruction.
     *
     * @param obj the instruction.
     */
    void visitISHL(ISHL obj);

    /**
     * Visits an ISHR instruction.
     *
     * @param obj the instruction.
     */
    void visitISHR(ISHR obj);

    /**
     * Visits an ISTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitISTORE(ISTORE obj);

    /**
     * Visits an ISUB instruction.
     *
     * @param obj the instruction.
     */
    void visitISUB(ISUB obj);

    /**
     * Visits an IUSHR instruction.
     *
     * @param obj the instruction.
     */
    void visitIUSHR(IUSHR obj);

    /**
     * Visits an IXOR instruction.
     *
     * @param obj the instruction.
     */
    void visitIXOR(IXOR obj);

    /**
     * Visits a JSR instruction.
     *
     * @param obj the instruction.
     */
    void visitJSR(JSR obj);

    /**
     * Visits a JSR_W instruction.
     *
     * @param obj the instruction.
     */
    void visitJSR_W(JSR_W obj);

    /**
     * Visits a JsrInstruction.
     *
     * @param obj the instruction.
     */
    void visitJsrInstruction(JsrInstruction obj);

    /**
     * Visits an L2D instruction.
     *
     * @param obj the instruction.
     */
    void visitL2D(L2D obj);

    /**
     * Visits an L2F instruction.
     *
     * @param obj the instruction.
     */
    void visitL2F(L2F obj);

    /**
     * Visits an L2I instruction.
     *
     * @param obj the instruction.
     */
    void visitL2I(L2I obj);

    /**
     * Visits an LADD instruction.
     *
     * @param obj the instruction.
     */
    void visitLADD(LADD obj);

    /**
     * Visits an LALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitLALOAD(LALOAD obj);

    /**
     * Visits an LAND instruction.
     *
     * @param obj the instruction.
     */
    void visitLAND(LAND obj);

    /**
     * Visits an LASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitLASTORE(LASTORE obj);

    /**
     * Visits an LCMP instruction.
     *
     * @param obj the instruction.
     */
    void visitLCMP(LCMP obj);

    /**
     * Visits an LCONST instruction.
     *
     * @param obj the instruction.
     */
    void visitLCONST(LCONST obj);

    /**
     * Visits an LDC instruction.
     *
     * @param obj the instruction.
     */
    void visitLDC(LDC obj);

    /**
     * Visits an LDC2_W instruction.
     *
     * @param obj the instruction.
     */
    void visitLDC2_W(LDC2_W obj);

    /**
     * Visits an LDIV instruction.
     *
     * @param obj the instruction.
     */
    void visitLDIV(LDIV obj);

    /**
     * Visits an LLOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitLLOAD(LLOAD obj);

    /**
     * Visits an LMUL instruction.
     *
     * @param obj the instruction.
     */
    void visitLMUL(LMUL obj);

    /**
     * Visits an LNEG instruction.
     *
     * @param obj the instruction.
     */
    void visitLNEG(LNEG obj);

    /**
     * Visits a LoadClass.
     *
     * @param obj the load class.
     */
    void visitLoadClass(LoadClass obj);

    /**
     * Visits a LoadInstruction.
     *
     * @param obj the instruction.
     */
    void visitLoadInstruction(LoadInstruction obj);

    /**
     * Visits a LocalVariableInstruction.
     *
     * @param obj the instruction.
     */
    void visitLocalVariableInstruction(LocalVariableInstruction obj);

    /**
     * Visits a LOOKUPSWITCH instruction.
     *
     * @param obj the instruction.
     */
    void visitLOOKUPSWITCH(LOOKUPSWITCH obj);

    /**
     * Visits an LOR instruction.
     *
     * @param obj the instruction.
     */
    void visitLOR(LOR obj);

    /**
     * Visits an LREM instruction.
     *
     * @param obj the instruction.
     */
    void visitLREM(LREM obj);

    /**
     * Visits an LRETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitLRETURN(LRETURN obj);

    /**
     * Visits an LSHL instruction.
     *
     * @param obj the instruction.
     */
    void visitLSHL(LSHL obj);

    /**
     * Visits an LSHR instruction.
     *
     * @param obj the instruction.
     */
    void visitLSHR(LSHR obj);

    /**
     * Visits an LSTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitLSTORE(LSTORE obj);

    /**
     * Visits an LSUB instruction.
     *
     * @param obj the instruction.
     */
    void visitLSUB(LSUB obj);

    /**
     * Visits an LUSHR instruction.
     *
     * @param obj the instruction.
     */
    void visitLUSHR(LUSHR obj);

    /**
     * Visits an LXOR instruction.
     *
     * @param obj the instruction.
     */
    void visitLXOR(LXOR obj);

    /**
     * Visits a MONITORENTER instruction.
     *
     * @param obj the instruction.
     */
    void visitMONITORENTER(MONITORENTER obj);

    /**
     * Visits a MONITOREXIT instruction.
     *
     * @param obj the instruction.
     */
    void visitMONITOREXIT(MONITOREXIT obj);

    /**
     * Visits a MULTIANEWARRAY instruction.
     *
     * @param obj the instruction.
     */
    void visitMULTIANEWARRAY(MULTIANEWARRAY obj);

    /**
     * Visits a NEW instruction.
     *
     * @param obj the instruction.
     */
    void visitNEW(NEW obj);

    /**
     * Visits a NEWARRAY instruction.
     *
     * @param obj the instruction.
     */
    void visitNEWARRAY(NEWARRAY obj);

    /**
     * Visits a NOP instruction.
     *
     * @param obj the instruction.
     */
    void visitNOP(NOP obj);

    /**
     * Visits a POP instruction.
     *
     * @param obj the instruction.
     */
    void visitPOP(POP obj);

    /**
     * Visits a POP2 instruction.
     *
     * @param obj the instruction.
     */
    void visitPOP2(POP2 obj);

    /**
     * Visits a PopInstruction.
     *
     * @param obj the instruction.
     */
    void visitPopInstruction(PopInstruction obj);

    /**
     * Visits a PushInstruction.
     *
     * @param obj the instruction.
     */
    void visitPushInstruction(PushInstruction obj);

    /**
     * Visits a PUTFIELD instruction.
     *
     * @param obj the instruction.
     */
    void visitPUTFIELD(PUTFIELD obj);

    /**
     * Visits a PUTSTATIC instruction.
     *
     * @param obj the instruction.
     */
    void visitPUTSTATIC(PUTSTATIC obj);

    /**
     * Visits a RET instruction.
     *
     * @param obj the instruction.
     */
    void visitRET(RET obj);

    /**
     * Visits a RETURN instruction.
     *
     * @param obj the instruction.
     */
    void visitRETURN(RETURN obj);

    /**
     * Visits a ReturnInstruction.
     *
     * @param obj the instruction.
     */
    void visitReturnInstruction(ReturnInstruction obj);

    /**
     * Visits a SALOAD instruction.
     *
     * @param obj the instruction.
     */
    void visitSALOAD(SALOAD obj);

    /**
     * Visits a SASTORE instruction.
     *
     * @param obj the instruction.
     */
    void visitSASTORE(SASTORE obj);

    /**
     * Visits a Select instruction.
     *
     * @param obj the instruction.
     */
    void visitSelect(Select obj);

    /**
     * Visits a SIPUSH instruction.
     *
     * @param obj the instruction.
     */
    void visitSIPUSH(SIPUSH obj);

    /**
     * Visits a StackConsumer.
     *
     * @param obj the instruction.
     */
    void visitStackConsumer(StackConsumer obj);

    /**
     * Visits a StackInstruction.
     *
     * @param obj the instruction.
     */
    void visitStackInstruction(StackInstruction obj);

    /**
     * Visits a StackProducer.
     *
     * @param obj the instruction.
     */
    void visitStackProducer(StackProducer obj);

    /**
     * Visits a StoreInstruction.
     *
     * @param obj the instruction.
     */
    void visitStoreInstruction(StoreInstruction obj);

    /**
     * Visits a SWAP instruction.
     *
     * @param obj the instruction.
     */
    void visitSWAP(SWAP obj);

    /**
     * Visits a TABLESWITCH instruction.
     *
     * @param obj the instruction.
     */
    void visitTABLESWITCH(TABLESWITCH obj);

    /**
     * Visits a TypedInstruction.
     *
     * @param obj the instruction.
     */
    void visitTypedInstruction(TypedInstruction obj);

    /**
     * Visits an UnconditionalBranch.
     *
     * @param obj the instruction.
     */
    void visitUnconditionalBranch(UnconditionalBranch obj);

    /**
     * Visits a VariableLengthInstruction.
     *
     * @param obj the instruction.
     */
    void visitVariableLengthInstruction(VariableLengthInstruction obj);
}
