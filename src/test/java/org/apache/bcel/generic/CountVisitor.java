/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.bcel.generic;

public class CountVisitor implements Visitor {

    public int getVisitAALOADCount() {
        return visitAALOADCount;
    }

    public int getVisitAASTORECount() {
        return visitAASTORECount;
    }

    public int getVisitACONST_NULLCount() {
        return visitACONST_NULLCount;
    }

    public int getVisitAllocationInstructionCount() {
        return visitAllocationInstructionCount;
    }

    public int getVisitALOADCount() {
        return visitALOADCount;
    }

    public int getVisitANEWARRAYCount() {
        return visitANEWARRAYCount;
    }

    public int getVisitARETURNCount() {
        return visitARETURNCount;
    }

    public int getVisitArithmeticInstructionCount() {
        return visitArithmeticInstructionCount;
    }

    public int getVisitArrayInstructionCount() {
        return visitArrayInstructionCount;
    }

    public int getVisitARRAYLENGTHCount() {
        return visitARRAYLENGTHCount;
    }

    public int getVisitASTORECount() {
        return visitASTORECount;
    }

    public int getVisitATHROWCount() {
        return visitATHROWCount;
    }

    public int getVisitBALOADCount() {
        return visitBALOADCount;
    }

    public int getVisitBASTORECount() {
        return visitBASTORECount;
    }

    public int getVisitBIPUSHCount() {
        return visitBIPUSHCount;
    }

    public int getVisitBranchInstructionCount() {
        return visitBranchInstructionCount;
    }

    public int getVisitBREAKPOINTCount() {
        return visitBREAKPOINTCount;
    }

    public int getVisitCALOADCount() {
        return visitCALOADCount;
    }

    public int getVisitCASTORECount() {
        return visitCASTORECount;
    }

    public int getVisitCHECKCASTCount() {
        return visitCHECKCASTCount;
    }

    public int getVisitConstantPushInstructionCount() {
        return visitConstantPushInstructionCount;
    }

    public int getVisitConversionInstructionCount() {
        return visitConversionInstructionCount;
    }

    public int getVisitCPInstructionCount() {
        return visitCPInstructionCount;
    }

    public int getVisitD2FCount() {
        return visitD2FCount;
    }

    public int getVisitD2ICount() {
        return visitD2ICount;
    }

    public int getVisitD2LCount() {
        return visitD2LCount;
    }

    public int getVisitDADDCount() {
        return visitDADDCount;
    }

    public int getVisitDALOADCount() {
        return visitDALOADCount;
    }

    public int getVisitDASTORECount() {
        return visitDASTORECount;
    }

    public int getVisitDCMPGCount() {
        return visitDCMPGCount;
    }

    public int getVisitDCMPLCount() {
        return visitDCMPLCount;
    }

    public int getVisitDCONSTCount() {
        return visitDCONSTCount;
    }

    public int getVisitDDIVCount() {
        return visitDDIVCount;
    }

    public int getVisitDLOADCount() {
        return visitDLOADCount;
    }

    public int getVisitDMULCount() {
        return visitDMULCount;
    }

    public int getVisitDNEGCount() {
        return visitDNEGCount;
    }

    public int getVisitDREMCount() {
        return visitDREMCount;
    }

    public int getVisitDRETURNCount() {
        return visitDRETURNCount;
    }

    public int getVisitDSTORECount() {
        return visitDSTORECount;
    }

    public int getVisitDSUBCount() {
        return visitDSUBCount;
    }

    public int getVisitDUPCount() {
        return visitDUPCount;
    }

    public int getVisitDUP_X1Count() {
        return visitDUP_X1Count;
    }

    public int getVisitDUP_X2Count() {
        return visitDUP_X2Count;
    }

    public int getVisitDUP2Count() {
        return visitDUP2Count;
    }

    public int getVisitDUP2_X1Count() {
        return visitDUP2_X1Count;
    }

    public int getVisitDUP2_X2Count() {
        return visitDUP2_X2Count;
    }

    public int getVisitExceptionThrowerCount() {
        return visitExceptionThrowerCount;
    }

    public int getVisitF2DCount() {
        return visitF2DCount;
    }

    public int getVisitF2ICount() {
        return visitF2ICount;
    }

    public int getVisitF2LCount() {
        return visitF2LCount;
    }

    public int getVisitFADDCount() {
        return visitFADDCount;
    }

    public int getVisitFALOADCount() {
        return visitFALOADCount;
    }

    public int getVisitFASTORECount() {
        return visitFASTORECount;
    }

    public int getVisitFCMPGCount() {
        return visitFCMPGCount;
    }

    public int getVisitFCMPLCount() {
        return visitFCMPLCount;
    }

    public int getVisitFCONSTCount() {
        return visitFCONSTCount;
    }

    public int getVisitFDIVCount() {
        return visitFDIVCount;
    }

    public int getVisitFieldInstructionCount() {
        return visitFieldInstructionCount;
    }

    public int getVisitFieldOrMethodCount() {
        return visitFieldOrMethodCount;
    }

    public int getVisitFLOADCount() {
        return visitFLOADCount;
    }

    public int getVisitFMULCount() {
        return visitFMULCount;
    }

    public int getVisitFNEGCount() {
        return visitFNEGCount;
    }

    public int getVisitFREMCount() {
        return visitFREMCount;
    }

    public int getVisitFRETURNCount() {
        return visitFRETURNCount;
    }

    public int getVisitFSTORECount() {
        return visitFSTORECount;
    }

    public int getVisitFSUBCount() {
        return visitFSUBCount;
    }

    public int getVisitGETFIELDCount() {
        return visitGETFIELDCount;
    }

    public int getVisitGETSTATICCount() {
        return visitGETSTATICCount;
    }

    public int getVisitGOTOCount() {
        return visitGOTOCount;
    }

    public int getVisitGOTO_WCount() {
        return visitGOTO_WCount;
    }

    public int getVisitGotoInstructionCount() {
        return visitGotoInstructionCount;
    }

    public int getVisitI2BCount() {
        return visitI2BCount;
    }

    public int getVisitI2CCount() {
        return visitI2CCount;
    }

    public int getVisitI2DCount() {
        return visitI2DCount;
    }

    public int getVisitI2FCount() {
        return visitI2FCount;
    }

    public int getVisitI2LCount() {
        return visitI2LCount;
    }

    public int getVisitI2SCount() {
        return visitI2SCount;
    }

    public int getVisitIADDCount() {
        return visitIADDCount;
    }

    public int getVisitIALOADCount() {
        return visitIALOADCount;
    }

    public int getVisitIANDCount() {
        return visitIANDCount;
    }

    public int getVisitIASTORECount() {
        return visitIASTORECount;
    }

    public int getVisitICONSTCount() {
        return visitICONSTCount;
    }

    public int getVisitIDIVCount() {
        return visitIDIVCount;
    }

    public int getVisitIF_ACMPEQCount() {
        return visitIF_ACMPEQCount;
    }

    public int getVisitIF_ACMPNECount() {
        return visitIF_ACMPNECount;
    }

    public int getVisitIF_ICMPEQCount() {
        return visitIF_ICMPEQCount;
    }

    public int getVisitIF_ICMPGECount() {
        return visitIF_ICMPGECount;
    }

    public int getVisitIF_ICMPGTCount() {
        return visitIF_ICMPGTCount;
    }

    public int getVisitIF_ICMPLECount() {
        return visitIF_ICMPLECount;
    }

    public int getVisitIF_ICMPLTCount() {
        return visitIF_ICMPLTCount;
    }

    public int getVisitIF_ICMPNECount() {
        return visitIF_ICMPNECount;
    }

    public int getVisitIFEQCount() {
        return visitIFEQCount;
    }

    public int getVisitIFGECount() {
        return visitIFGECount;
    }

    public int getVisitIFGTCount() {
        return visitIFGTCount;
    }

    public int getVisitIfInstructionCount() {
        return visitIfInstructionCount;
    }

    public int getVisitIFLECount() {
        return visitIFLECount;
    }

    public int getVisitIFLTCount() {
        return visitIFLTCount;
    }

    public int getVisitIFNECount() {
        return visitIFNECount;
    }

    public int getVisitIFNONNULLCount() {
        return visitIFNONNULLCount;
    }

    public int getVisitIFNULLCount() {
        return visitIFNULLCount;
    }

    public int getVisitIINCCount() {
        return visitIINCCount;
    }

    public int getVisitILOADCount() {
        return visitILOADCount;
    }

    public int getVisitIMPDEP1Count() {
        return visitIMPDEP1Count;
    }

    public int getVisitIMPDEP2Count() {
        return visitIMPDEP2Count;
    }

    public int getVisitIMULCount() {
        return visitIMULCount;
    }

    public int getVisitINEGCount() {
        return visitINEGCount;
    }

    public int getVisitINSTANCEOFCount() {
        return visitINSTANCEOFCount;
    }

    public int getVisitINVOKEDYNAMICCount() {
        return visitINVOKEDYNAMICCount;
    }

    public int getVisitInvokeInstructionCount() {
        return visitInvokeInstructionCount;
    }

    public int getVisitINVOKEINTERFACECount() {
        return visitINVOKEINTERFACECount;
    }

    public int getVisitINVOKESPECIALCount() {
        return visitINVOKESPECIALCount;
    }

    public int getVisitINVOKESTATICCount() {
        return visitINVOKESTATICCount;
    }

    public int getVisitINVOKEVIRTUALCount() {
        return visitINVOKEVIRTUALCount;
    }

    public int getVisitIORCount() {
        return visitIORCount;
    }

    public int getVisitIREMCount() {
        return visitIREMCount;
    }

    public int getVisitIRETURNCount() {
        return visitIRETURNCount;
    }

    public int getVisitISHLCount() {
        return visitISHLCount;
    }

    public int getVisitISHRCount() {
        return visitISHRCount;
    }

    public int getVisitISTORECount() {
        return visitISTORECount;
    }

    public int getVisitISUBCount() {
        return visitISUBCount;
    }

    public int getVisitIUSHRCount() {
        return visitIUSHRCount;
    }

    public int getVisitIXORCount() {
        return visitIXORCount;
    }

    public int getVisitJSRCount() {
        return visitJSRCount;
    }

    public int getVisitJSR_WCount() {
        return visitJSR_WCount;
    }

    public int getVisitJsrInstructionCount() {
        return visitJsrInstructionCount;
    }

    public int getVisitL2DCount() {
        return visitL2DCount;
    }

    public int getVisitL2FCount() {
        return visitL2FCount;
    }

    public int getVisitL2ICount() {
        return visitL2ICount;
    }

    public int getVisitLADDCount() {
        return visitLADDCount;
    }

    public int getVisitLALOADCount() {
        return visitLALOADCount;
    }

    public int getVisitLANDCount() {
        return visitLANDCount;
    }

    public int getVisitLASTORECount() {
        return visitLASTORECount;
    }

    public int getVisitLCMPCount() {
        return visitLCMPCount;
    }

    public int getVisitLCONSTCount() {
        return visitLCONSTCount;
    }

    public int getVisitLDCCount() {
        return visitLDCCount;
    }

    public int getVisitLDC2_WCount() {
        return visitLDC2_WCount;
    }

    public int getVisitLDIVCount() {
        return visitLDIVCount;
    }

    public int getVisitLLOADCount() {
        return visitLLOADCount;
    }

    public int getVisitLMULCount() {
        return visitLMULCount;
    }

    public int getVisitLNEGCount() {
        return visitLNEGCount;
    }

    public int getVisitLoadClassCount() {
        return visitLoadClassCount;
    }

    public int getVisitLoadInstructionCount() {
        return visitLoadInstructionCount;
    }

    public int getVisitLocalVariableInstructionCount() {
        return visitLocalVariableInstructionCount;
    }

    public int getVisitLOOKUPSWITCHCount() {
        return visitLOOKUPSWITCHCount;
    }

    public int getVisitLORCount() {
        return visitLORCount;
    }

    public int getVisitLREMCount() {
        return visitLREMCount;
    }

    public int getVisitLRETURNCount() {
        return visitLRETURNCount;
    }

    public int getVisitLSHLCount() {
        return visitLSHLCount;
    }

    public int getVisitLSHRCount() {
        return visitLSHRCount;
    }

    public int getVisitLSTORECount() {
        return visitLSTORECount;
    }

    public int getVisitLSUBCount() {
        return visitLSUBCount;
    }

    public int getVisitLUSHRCount() {
        return visitLUSHRCount;
    }

    public int getVisitLXORCount() {
        return visitLXORCount;
    }

    public int getVisitMONITORENTERCount() {
        return visitMONITORENTERCount;
    }

    public int getVisitMONITOREXITCount() {
        return visitMONITOREXITCount;
    }

    public int getVisitMULTIANEWARRAYCount() {
        return visitMULTIANEWARRAYCount;
    }

    public int getVisitNEWCount() {
        return visitNEWCount;
    }

    public int getVisitNEWARRAYCount() {
        return visitNEWARRAYCount;
    }

    public int getVisitNOPCount() {
        return visitNOPCount;
    }

    public int getVisitPOPCount() {
        return visitPOPCount;
    }

    public int getVisitPOP2Count() {
        return visitPOP2Count;
    }

    public int getVisitPopInstructionCount() {
        return visitPopInstructionCount;
    }

    public int getVisitPushInstructionCount() {
        return visitPushInstructionCount;
    }

    public int getVisitPUTFIELDCount() {
        return visitPUTFIELDCount;
    }

    public int getVisitPUTSTATICCount() {
        return visitPUTSTATICCount;
    }

    public int getVisitRETCount() {
        return visitRETCount;
    }

    public int getVisitRETURNCount() {
        return visitRETURNCount;
    }

    public int getVisitReturnInstructionCount() {
        return visitReturnInstructionCount;
    }

    public int getVisitSALOADCount() {
        return visitSALOADCount;
    }

    public int getVisitSASTORECount() {
        return visitSASTORECount;
    }

    public int getVisitSelectCount() {
        return visitSelectCount;
    }

    public int getVisitSIPUSHCount() {
        return visitSIPUSHCount;
    }

    public int getVisitStackConsumerCount() {
        return visitStackConsumerCount;
    }

    public int getVisitStackInstructionCount() {
        return visitStackInstructionCount;
    }

    public int getVisitStackProducerCount() {
        return visitStackProducerCount;
    }

    public int getVisitStoreInstructionCount() {
        return visitStoreInstructionCount;
    }

    public int getVisitSWAPCount() {
        return visitSWAPCount;
    }

    public int getVisitTABLESWITCHCount() {
        return visitTABLESWITCHCount;
    }

    public int getVisitTypedInstructionCount() {
        return visitTypedInstructionCount;
    }

    public int getVisitUnconditionalBranchCount() {
        return visitUnconditionalBranchCount;
    }

    public int getVisitVariableLengthInstructionCount() {
        return visitVariableLengthInstructionCount;
    }

    private int visitAALOADCount = 0;

    @Override
    public void visitAALOAD(AALOAD obj) {
        visitAALOADCount++;
    }

    private int visitAASTORECount = 0;

    @Override
    public void visitAASTORE(AASTORE obj) {
        visitAASTORECount++;
    }

    private int visitACONST_NULLCount = 0;

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        visitACONST_NULLCount++;
    }

    private int visitAllocationInstructionCount = 0;

    @Override
    public void visitAllocationInstruction(AllocationInstruction obj) {
        visitAllocationInstructionCount++;
    }

    private int visitALOADCount = 0;

    @Override
    public void visitALOAD(ALOAD obj) {
        visitALOADCount++;
    }

    private int visitANEWARRAYCount = 0;

    @Override
    public void visitANEWARRAY(ANEWARRAY obj) {
        visitANEWARRAYCount++;
    }

    private int visitARETURNCount = 0;

    @Override
    public void visitARETURN(ARETURN obj) {
        visitARETURNCount++;
    }

    private int visitArithmeticInstructionCount = 0;

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {
        visitArithmeticInstructionCount++;
    }

    private int visitArrayInstructionCount = 0;

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {
        visitArrayInstructionCount++;
    }

    private int visitARRAYLENGTHCount = 0;

    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH obj) {
        visitARRAYLENGTHCount++;
    }

    private int visitASTORECount = 0;

    @Override
    public void visitASTORE(ASTORE obj) {
        visitASTORECount++;
    }

    private int visitATHROWCount = 0;

    @Override
    public void visitATHROW(ATHROW obj) {
        visitATHROWCount++;
    }

    private int visitBALOADCount = 0;

    @Override
    public void visitBALOAD(BALOAD obj) {
        visitBALOADCount++;
    }

    private int visitBASTORECount = 0;

    @Override
    public void visitBASTORE(BASTORE obj) {
        visitBASTORECount++;
    }

    private int visitBIPUSHCount = 0;

    @Override
    public void visitBIPUSH(BIPUSH obj) {
        visitBIPUSHCount++;
    }

    private int visitBranchInstructionCount = 0;

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {
        visitBranchInstructionCount++;
    }

    private int visitBREAKPOINTCount = 0;

    @Override
    public void visitBREAKPOINT(BREAKPOINT obj) {
        visitBREAKPOINTCount++;
    }

    private int visitCALOADCount = 0;

    @Override
    public void visitCALOAD(CALOAD obj) {
        visitCALOADCount++;
    }

    private int visitCASTORECount = 0;

    @Override
    public void visitCASTORE(CASTORE obj) {
        visitCASTORECount++;
    }

    private int visitCHECKCASTCount = 0;

    @Override
    public void visitCHECKCAST(CHECKCAST obj) {
        visitCHECKCASTCount++;
    }

    private int visitConstantPushInstructionCount = 0;

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {
        visitConstantPushInstructionCount++;
    }

    private int visitConversionInstructionCount = 0;

    @Override
    public void visitConversionInstruction(ConversionInstruction obj) {
        visitConversionInstructionCount++;
    }

    private int visitCPInstructionCount = 0;

    @Override
    public void visitCPInstruction(CPInstruction obj) {
        visitCPInstructionCount++;
    }

    private int visitD2FCount = 0;

    @Override
    public void visitD2F(D2F obj) {
        visitD2FCount++;
    }

    private int visitD2ICount = 0;

    @Override
    public void visitD2I(D2I obj) {
        visitD2ICount++;
    }

    private int visitD2LCount = 0;

    @Override
    public void visitD2L(D2L obj) {
        visitD2LCount++;
    }

    private int visitDADDCount = 0;

    @Override
    public void visitDADD(DADD obj) {
        visitDADDCount++;
    }

    private int visitDALOADCount = 0;

    @Override
    public void visitDALOAD(DALOAD obj) {
        visitDALOADCount++;
    }

    private int visitDASTORECount = 0;

    @Override
    public void visitDASTORE(DASTORE obj) {
        visitDASTORECount++;
    }

    private int visitDCMPGCount = 0;

    @Override
    public void visitDCMPG(DCMPG obj) {
        visitDCMPGCount++;
    }

    private int visitDCMPLCount = 0;

    @Override
    public void visitDCMPL(DCMPL obj) {
        visitDCMPLCount++;
    }

    private int visitDCONSTCount = 0;

    @Override
    public void visitDCONST(DCONST obj) {
        visitDCONSTCount++;
    }

    private int visitDDIVCount = 0;

    @Override
    public void visitDDIV(DDIV obj) {
        visitDDIVCount++;
    }

    private int visitDLOADCount = 0;

    @Override
    public void visitDLOAD(DLOAD obj) {
        visitDLOADCount++;
    }

    private int visitDMULCount = 0;

    @Override
    public void visitDMUL(DMUL obj) {
        visitDMULCount++;
    }

    private int visitDNEGCount = 0;

    @Override
    public void visitDNEG(DNEG obj) {
        visitDNEGCount++;
    }

    private int visitDREMCount = 0;

    @Override
    public void visitDREM(DREM obj) {
        visitDREMCount++;
    }

    private int visitDRETURNCount = 0;

    @Override
    public void visitDRETURN(DRETURN obj) {
        visitDRETURNCount++;
    }

    private int visitDSTORECount = 0;

    @Override
    public void visitDSTORE(DSTORE obj) {
        visitDSTORECount++;
    }

    private int visitDSUBCount = 0;

    @Override
    public void visitDSUB(DSUB obj) {
        visitDSUBCount++;
    }

    private int visitDUPCount = 0;

    @Override
    public void visitDUP(DUP obj) {
        visitDUPCount++;
    }

    private int visitDUP_X1Count = 0;

    @Override
    public void visitDUP_X1(DUP_X1 obj) {
        visitDUP_X1Count++;
    }

    private int visitDUP_X2Count = 0;

    @Override
    public void visitDUP_X2(DUP_X2 obj) {
        visitDUP_X2Count++;
    }

    private int visitDUP2Count = 0;

    @Override
    public void visitDUP2(DUP2 obj) {
        visitDUP2Count++;
    }

    private int visitDUP2_X1Count = 0;

    @Override
    public void visitDUP2_X1(DUP2_X1 obj) {
        visitDUP2_X1Count++;
    }

    private int visitDUP2_X2Count = 0;

    @Override
    public void visitDUP2_X2(DUP2_X2 obj) {
        visitDUP2_X2Count++;
    }

    private int visitExceptionThrowerCount = 0;

    @Override
    public void visitExceptionThrower(ExceptionThrower obj) {
        visitExceptionThrowerCount++;
    }

    private int visitF2DCount = 0;

    @Override
    public void visitF2D(F2D obj) {
        visitF2DCount++;
    }

    private int visitF2ICount = 0;

    @Override
    public void visitF2I(F2I obj) {
        visitF2ICount++;
    }

    private int visitF2LCount = 0;

    @Override
    public void visitF2L(F2L obj) {
        visitF2LCount++;
    }

    private int visitFADDCount = 0;

    @Override
    public void visitFADD(FADD obj) {
        visitFADDCount++;
    }

    private int visitFALOADCount = 0;

    @Override
    public void visitFALOAD(FALOAD obj) {
        visitFALOADCount++;
    }

    private int visitFASTORECount = 0;

    @Override
    public void visitFASTORE(FASTORE obj) {
        visitFASTORECount++;
    }

    private int visitFCMPGCount = 0;

    @Override
    public void visitFCMPG(FCMPG obj) {
        visitFCMPGCount++;
    }

    private int visitFCMPLCount = 0;

    @Override
    public void visitFCMPL(FCMPL obj) {
        visitFCMPLCount++;
    }

    private int visitFCONSTCount = 0;

    @Override
    public void visitFCONST(FCONST obj) {
        visitFCONSTCount++;
    }

    private int visitFDIVCount = 0;

    @Override
    public void visitFDIV(FDIV obj) {
        visitFDIVCount++;
    }

    private int visitFieldInstructionCount = 0;

    @Override
    public void visitFieldInstruction(FieldInstruction obj) {
        visitFieldInstructionCount++;
    }

    private int visitFieldOrMethodCount = 0;

    @Override
    public void visitFieldOrMethod(FieldOrMethod obj) {
        visitFieldOrMethodCount++;
    }

    private int visitFLOADCount = 0;

    @Override
    public void visitFLOAD(FLOAD obj) {
        visitFLOADCount++;
    }

    private int visitFMULCount = 0;

    @Override
    public void visitFMUL(FMUL obj) {
        visitFMULCount++;
    }

    private int visitFNEGCount = 0;

    @Override
    public void visitFNEG(FNEG obj) {
        visitFNEGCount++;
    }

    private int visitFREMCount = 0;

    @Override
    public void visitFREM(FREM obj) {
        visitFREMCount++;
    }

    private int visitFRETURNCount = 0;

    @Override
    public void visitFRETURN(FRETURN obj) {
        visitFRETURNCount++;
    }

    private int visitFSTORECount = 0;

    @Override
    public void visitFSTORE(FSTORE obj) {
        visitFSTORECount++;
    }

    private int visitFSUBCount = 0;

    @Override
    public void visitFSUB(FSUB obj) {
        visitFSUBCount++;
    }

    private int visitGETFIELDCount = 0;

    @Override
    public void visitGETFIELD(GETFIELD obj) {
        visitGETFIELDCount++;
    }

    private int visitGETSTATICCount = 0;

    @Override
    public void visitGETSTATIC(GETSTATIC obj) {
        visitGETSTATICCount++;
    }

    private int visitGOTOCount = 0;

    @Override
    public void visitGOTO(GOTO obj) {
        visitGOTOCount++;
    }

    private int visitGOTO_WCount = 0;

    @Override
    public void visitGOTO_W(GOTO_W obj) {
        visitGOTO_WCount++;
    }

    private int visitGotoInstructionCount = 0;

    @Override
    public void visitGotoInstruction(GotoInstruction obj) {
        visitGotoInstructionCount++;
    }

    private int visitI2BCount = 0;

    @Override
    public void visitI2B(I2B obj) {
        visitI2BCount++;
    }

    private int visitI2CCount = 0;

    @Override
    public void visitI2C(I2C obj) {
        visitI2CCount++;
    }

    private int visitI2DCount = 0;

    @Override
    public void visitI2D(I2D obj) {
        visitI2DCount++;
    }

    private int visitI2FCount = 0;

    @Override
    public void visitI2F(I2F obj) {
        visitI2FCount++;
    }

    private int visitI2LCount = 0;

    @Override
    public void visitI2L(I2L obj) {
        visitI2LCount++;
    }

    private int visitI2SCount = 0;

    @Override
    public void visitI2S(I2S obj) {
        visitI2SCount++;
    }

    private int visitIADDCount = 0;

    @Override
    public void visitIADD(IADD obj) {
        visitIADDCount++;
    }

    private int visitIALOADCount = 0;

    @Override
    public void visitIALOAD(IALOAD obj) {
        visitIALOADCount++;
    }

    private int visitIANDCount = 0;

    @Override
    public void visitIAND(IAND obj) {
        visitIANDCount++;
    }

    private int visitIASTORECount = 0;

    @Override
    public void visitIASTORE(IASTORE obj) {
        visitIASTORECount++;
    }

    private int visitICONSTCount = 0;

    @Override
    public void visitICONST(ICONST obj) {
        visitICONSTCount++;
    }

    private int visitIDIVCount = 0;

    @Override
    public void visitIDIV(IDIV obj) {
        visitIDIVCount++;
    }

    private int visitIF_ACMPEQCount = 0;

    @Override
    public void visitIF_ACMPEQ(IF_ACMPEQ obj) {
        visitIF_ACMPEQCount++;
    }

    private int visitIF_ACMPNECount = 0;

    @Override
    public void visitIF_ACMPNE(IF_ACMPNE obj) {
        visitIF_ACMPNECount++;
    }

    private int visitIF_ICMPEQCount = 0;

    @Override
    public void visitIF_ICMPEQ(IF_ICMPEQ obj) {
        visitIF_ICMPEQCount++;
    }

    private int visitIF_ICMPGECount = 0;

    @Override
    public void visitIF_ICMPGE(IF_ICMPGE obj) {
        visitIF_ICMPGECount++;
    }

    private int visitIF_ICMPGTCount = 0;

    @Override
    public void visitIF_ICMPGT(IF_ICMPGT obj) {
        visitIF_ICMPGTCount++;
    }

    private int visitIF_ICMPLECount = 0;

    @Override
    public void visitIF_ICMPLE(IF_ICMPLE obj) {
        visitIF_ICMPLECount++;
    }

    private int visitIF_ICMPLTCount = 0;

    @Override
    public void visitIF_ICMPLT(IF_ICMPLT obj) {
        visitIF_ICMPLTCount++;
    }

    private int visitIF_ICMPNECount = 0;

    @Override
    public void visitIF_ICMPNE(IF_ICMPNE obj) {
        visitIF_ICMPNECount++;
    }

    private int visitIFEQCount = 0;

    @Override
    public void visitIFEQ(IFEQ obj) {
        visitIFEQCount++;
    }

    private int visitIFGECount = 0;

    @Override
    public void visitIFGE(IFGE obj) {
        visitIFGECount++;
    }

    private int visitIFGTCount = 0;

    @Override
    public void visitIFGT(IFGT obj) {
        visitIFGTCount++;
    }

    private int visitIfInstructionCount = 0;

    @Override
    public void visitIfInstruction(IfInstruction obj) {
        visitIfInstructionCount++;
    }

    private int visitIFLECount = 0;

    @Override
    public void visitIFLE(IFLE obj) {
        visitIFLECount++;
    }

    private int visitIFLTCount = 0;

    @Override
    public void visitIFLT(IFLT obj) {
        visitIFLTCount++;
    }

    private int visitIFNECount = 0;

    @Override
    public void visitIFNE(IFNE obj) {
        visitIFNECount++;
    }

    private int visitIFNONNULLCount = 0;

    @Override
    public void visitIFNONNULL(IFNONNULL obj) {
        visitIFNONNULLCount++;
    }

    private int visitIFNULLCount = 0;

    @Override
    public void visitIFNULL(IFNULL obj) {
        visitIFNULLCount++;
    }

    private int visitIINCCount = 0;

    @Override
    public void visitIINC(IINC obj) {
        visitIINCCount++;
    }

    private int visitILOADCount = 0;

    @Override
    public void visitILOAD(ILOAD obj) {
        visitILOADCount++;
    }

    private int visitIMPDEP1Count = 0;

    @Override
    public void visitIMPDEP1(IMPDEP1 obj) {
        visitIMPDEP1Count++;
    }

    private int visitIMPDEP2Count = 0;

    @Override
    public void visitIMPDEP2(IMPDEP2 obj) {
        visitIMPDEP2Count++;
    }

    private int visitIMULCount = 0;

    @Override
    public void visitIMUL(IMUL obj) {
        visitIMULCount++;
    }

    private int visitINEGCount = 0;

    @Override
    public void visitINEG(INEG obj) {
        visitINEGCount++;
    }

    private int visitINSTANCEOFCount = 0;

    @Override
    public void visitINSTANCEOF(INSTANCEOF obj) {
        visitINSTANCEOFCount++;
    }

    private int visitINVOKEDYNAMICCount = 0;

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj) {
        visitINVOKEDYNAMICCount++;
    }

    private int visitInvokeInstructionCount = 0;

    @Override
    public void visitInvokeInstruction(InvokeInstruction obj) {
        visitInvokeInstructionCount++;
    }

    private int visitINVOKEINTERFACECount = 0;

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {
        visitINVOKEINTERFACECount++;
    }

    private int visitINVOKESPECIALCount = 0;

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        visitINVOKESPECIALCount++;
    }

    private int visitINVOKESTATICCount = 0;

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        visitINVOKESTATICCount++;
    }

    private int visitINVOKEVIRTUALCount = 0;

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        visitINVOKEVIRTUALCount++;
    }

    private int visitIORCount = 0;

    @Override
    public void visitIOR(IOR obj) {
        visitIORCount++;
    }

    private int visitIREMCount = 0;

    @Override
    public void visitIREM(IREM obj) {
        visitIREMCount++;
    }

    private int visitIRETURNCount = 0;

    @Override
    public void visitIRETURN(IRETURN obj) {
        visitIRETURNCount++;
    }

    private int visitISHLCount = 0;

    @Override
    public void visitISHL(ISHL obj) {
        visitISHLCount++;
    }

    private int visitISHRCount = 0;

    @Override
    public void visitISHR(ISHR obj) {
        visitISHRCount++;
    }

    private int visitISTORECount = 0;

    @Override
    public void visitISTORE(ISTORE obj) {
        visitISTORECount++;
    }

    private int visitISUBCount = 0;

    @Override
    public void visitISUB(ISUB obj) {
        visitISUBCount++;
    }

    private int visitIUSHRCount = 0;

    @Override
    public void visitIUSHR(IUSHR obj) {
        visitIUSHRCount++;
    }

    private int visitIXORCount = 0;

    @Override
    public void visitIXOR(IXOR obj) {
        visitIXORCount++;
    }

    private int visitJSRCount = 0;

    @Override
    public void visitJSR(JSR obj) {
        visitJSRCount++;
    }

    private int visitJSR_WCount = 0;

    @Override
    public void visitJSR_W(JSR_W obj) {
        visitJSR_WCount++;
    }

    private int visitJsrInstructionCount = 0;

    @Override
    public void visitJsrInstruction(JsrInstruction obj) {
        visitJsrInstructionCount++;
    }

    private int visitL2DCount = 0;

    @Override
    public void visitL2D(L2D obj) {
        visitL2DCount++;
    }

    private int visitL2FCount = 0;

    @Override
    public void visitL2F(L2F obj) {
        visitL2FCount++;
    }

    private int visitL2ICount = 0;

    @Override
    public void visitL2I(L2I obj) {
        visitL2ICount++;
    }

    private int visitLADDCount = 0;

    @Override
    public void visitLADD(LADD obj) {
        visitLADDCount++;
    }

    private int visitLALOADCount = 0;

    @Override
    public void visitLALOAD(LALOAD obj) {
        visitLALOADCount++;
    }

    private int visitLANDCount = 0;

    @Override
    public void visitLAND(LAND obj) {
        visitLANDCount++;
    }

    private int visitLASTORECount = 0;

    @Override
    public void visitLASTORE(LASTORE obj) {
        visitLASTORECount++;
    }

    private int visitLCMPCount = 0;

    @Override
    public void visitLCMP(LCMP obj) {
        visitLCMPCount++;
    }

    private int visitLCONSTCount = 0;

    @Override
    public void visitLCONST(LCONST obj) {
        visitLCONSTCount++;
    }

    private int visitLDCCount = 0;

    @Override
    public void visitLDC(LDC obj) {
        visitLDCCount++;
    }

    private int visitLDC2_WCount = 0;

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        visitLDC2_WCount++;
    }

    private int visitLDIVCount = 0;

    @Override
    public void visitLDIV(LDIV obj) {
        visitLDIVCount++;
    }

    private int visitLLOADCount = 0;

    @Override
    public void visitLLOAD(LLOAD obj) {
        visitLLOADCount++;
    }

    private int visitLMULCount = 0;

    @Override
    public void visitLMUL(LMUL obj) {
        visitLMULCount++;
    }

    private int visitLNEGCount = 0;

    @Override
    public void visitLNEG(LNEG obj) {
        visitLNEGCount++;
    }

    private int visitLoadClassCount = 0;

    @Override
    public void visitLoadClass(LoadClass obj) {
        visitLoadClassCount++;
    }

    private int visitLoadInstructionCount = 0;

    @Override
    public void visitLoadInstruction(LoadInstruction obj) {
        visitLoadInstructionCount++;
    }

    private int visitLocalVariableInstructionCount = 0;

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {
        visitLocalVariableInstructionCount++;
    }

    private int visitLOOKUPSWITCHCount = 0;

    @Override
    public void visitLOOKUPSWITCH(LOOKUPSWITCH obj) {
        visitLOOKUPSWITCHCount++;
    }

    private int visitLORCount = 0;

    @Override
    public void visitLOR(LOR obj) {
        visitLORCount++;
    }

    private int visitLREMCount = 0;

    @Override
    public void visitLREM(LREM obj) {
        visitLREMCount++;
    }

    private int visitLRETURNCount = 0;

    @Override
    public void visitLRETURN(LRETURN obj) {
        visitLRETURNCount++;
    }

    private int visitLSHLCount = 0;

    @Override
    public void visitLSHL(LSHL obj) {
        visitLSHLCount++;
    }

    private int visitLSHRCount = 0;

    @Override
    public void visitLSHR(LSHR obj) {
        visitLSHRCount++;
    }

    private int visitLSTORECount = 0;

    @Override
    public void visitLSTORE(LSTORE obj) {
        visitLSTORECount++;
    }

    private int visitLSUBCount = 0;

    @Override
    public void visitLSUB(LSUB obj) {
        visitLSUBCount++;
    }

    private int visitLUSHRCount = 0;

    @Override
    public void visitLUSHR(LUSHR obj) {
        visitLUSHRCount++;
    }

    private int visitLXORCount = 0;

    @Override
    public void visitLXOR(LXOR obj) {
        visitLXORCount++;
    }

    private int visitMONITORENTERCount = 0;

    @Override
    public void visitMONITORENTER(MONITORENTER obj) {
        visitMONITORENTERCount++;
    }

    private int visitMONITOREXITCount = 0;

    @Override
    public void visitMONITOREXIT(MONITOREXIT obj) {
        visitMONITOREXITCount++;
    }

    private int visitMULTIANEWARRAYCount = 0;

    @Override
    public void visitMULTIANEWARRAY(MULTIANEWARRAY obj) {
        visitMULTIANEWARRAYCount++;
    }

    private int visitNEWCount = 0;

    @Override
    public void visitNEW(NEW obj) {
        visitNEWCount++;
    }

    private int visitNEWARRAYCount = 0;

    @Override
    public void visitNEWARRAY(NEWARRAY obj) {
        visitNEWARRAYCount++;
    }

    private int visitNOPCount = 0;

    @Override
    public void visitNOP(NOP obj) {
        visitNOPCount++;
    }

    private int visitPOPCount = 0;

    @Override
    public void visitPOP(POP obj) {
        visitPOPCount++;
    }

    private int visitPOP2Count = 0;

    @Override
    public void visitPOP2(POP2 obj) {
        visitPOP2Count++;
    }

    private int visitPopInstructionCount = 0;

    @Override
    public void visitPopInstruction(PopInstruction obj) {
        visitPopInstructionCount++;
    }

    private int visitPushInstructionCount = 0;

    @Override
    public void visitPushInstruction(PushInstruction obj) {
        visitPushInstructionCount++;
    }

    private int visitPUTFIELDCount = 0;

    @Override
    public void visitPUTFIELD(PUTFIELD obj) {
        visitPUTFIELDCount++;
    }

    private int visitPUTSTATICCount = 0;

    @Override
    public void visitPUTSTATIC(PUTSTATIC obj) {
        visitPUTSTATICCount++;
    }

    private int visitRETCount = 0;

    @Override
    public void visitRET(RET obj) {
        visitRETCount++;
    }

    private int visitRETURNCount = 0;

    @Override
    public void visitRETURN(RETURN obj) {
        visitRETURNCount++;
    }

    private int visitReturnInstructionCount = 0;

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {
        visitReturnInstructionCount++;
    }

    private int visitSALOADCount = 0;

    @Override
    public void visitSALOAD(SALOAD obj) {
        visitSALOADCount++;
    }

    private int visitSASTORECount = 0;

    @Override
    public void visitSASTORE(SASTORE obj) {
        visitSASTORECount++;
    }

    private int visitSelectCount = 0;

    @Override
    public void visitSelect(Select obj) {
        visitSelectCount++;
    }

    private int visitSIPUSHCount = 0;

    @Override
    public void visitSIPUSH(SIPUSH obj) {
        visitSIPUSHCount++;
    }

    private int visitStackConsumerCount = 0;

    @Override
    public void visitStackConsumer(StackConsumer obj) {
        visitStackConsumerCount++;
    }

    private int visitStackInstructionCount = 0;

    @Override
    public void visitStackInstruction(StackInstruction obj) {
        visitStackInstructionCount++;
    }

    private int visitStackProducerCount = 0;

    @Override
    public void visitStackProducer(StackProducer obj) {
        visitStackProducerCount++;
    }

    private int visitStoreInstructionCount = 0;

    @Override
    public void visitStoreInstruction(StoreInstruction obj) {
        visitStoreInstructionCount++;
    }

    private int visitSWAPCount = 0;

    @Override
    public void visitSWAP(SWAP obj) {
        visitSWAPCount++;
    }

    private int visitTABLESWITCHCount = 0;

    @Override
    public void visitTABLESWITCH(TABLESWITCH obj) {
        visitTABLESWITCHCount++;
    }

    private int visitTypedInstructionCount = 0;

    @Override
    public void visitTypedInstruction(TypedInstruction obj) {
        visitTypedInstructionCount++;
    }

    private int visitUnconditionalBranchCount = 0;

    @Override
    public void visitUnconditionalBranch(UnconditionalBranch obj) {
        visitUnconditionalBranchCount++;
    }

    private int visitVariableLengthInstructionCount = 0;

    @Override
    public void visitVariableLengthInstruction(VariableLengthInstruction obj) {
        visitVariableLengthInstructionCount++;
    }
}

