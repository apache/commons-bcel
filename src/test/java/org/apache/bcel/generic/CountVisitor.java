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

import java.util.Objects;

public class CountVisitor implements Visitor {

    private int visitAALOADCount;
    private int visitAASTORECount;
    private int visitACONST_NULLCount;
    private int visitAllocationInstructionCount;
    private int visitALOADCount;
    private int visitANEWARRAYCount;
    private int visitARETURNCount;
    private int visitArithmeticInstructionCount;
    private int visitArrayInstructionCount;
    private int visitARRAYLENGTHCount;
    private int visitASTORECount;
    private int visitATHROWCount;
    private int visitBALOADCount;
    private int visitBASTORECount;
    private int visitBIPUSHCount;
    private int visitBranchInstructionCount;
    private int visitBREAKPOINTCount;
    private int visitCALOADCount;
    private int visitCASTORECount;
    private int visitCHECKCASTCount;
    private int visitConstantPushInstructionCount;
    private int visitConversionInstructionCount;
    private int visitCPInstructionCount;
    private int visitD2FCount;
    private int visitD2ICount;
    private int visitD2LCount;
    private int visitDADDCount;
    private int visitDALOADCount;
    private int visitDASTORECount;
    private int visitDCMPGCount;
    private int visitDCMPLCount;
    private int visitDCONSTCount;
    private int visitDDIVCount;
    private int visitDLOADCount;
    private int visitDMULCount;
    private int visitDNEGCount;
    private int visitDREMCount;
    private int visitDRETURNCount;
    private int visitDSTORECount;
    private int visitDSUBCount;
    private int visitDUPCount;
    private int visitDUP_X1Count;
    private int visitDUP_X2Count;
    private int visitDUP2Count;
    private int visitDUP2_X1Count;
    private int visitDUP2_X2Count;
    private int visitExceptionThrowerCount;
    private int visitF2DCount;
    private int visitF2ICount;
    private int visitF2LCount;
    private int visitFADDCount;
    private int visitFALOADCount;
    private int visitFASTORECount;
    private int visitFCMPGCount;
    private int visitFCMPLCount;
    private int visitFCONSTCount;
    private int visitFDIVCount;
    private int visitFieldInstructionCount;
    private int visitFieldOrMethodCount;
    private int visitFLOADCount;
    private int visitFMULCount;
    private int visitFNEGCount;
    private int visitFREMCount;
    private int visitFRETURNCount;
    private int visitFSTORECount;
    private int visitFSUBCount;
    private int visitGETFIELDCount;
    private int visitGETSTATICCount;
    private int visitGOTOCount;
    private int visitGOTO_WCount;
    private int visitGotoInstructionCount;
    private int visitI2BCount;
    private int visitI2CCount;
    private int visitI2DCount;
    private int visitI2FCount;
    private int visitI2LCount;
    private int visitI2SCount;
    private int visitIADDCount;
    private int visitIALOADCount;
    private int visitIANDCount;
    private int visitIASTORECount;
    private int visitICONSTCount;
    private int visitIDIVCount;
    private int visitIF_ACMPEQCount;
    private int visitIF_ACMPNECount;
    private int visitIF_ICMPEQCount;
    private int visitIF_ICMPGECount;
    private int visitIF_ICMPGTCount;
    private int visitIF_ICMPLECount;
    private int visitIF_ICMPLTCount;
    private int visitIF_ICMPNECount;
    private int visitIFEQCount;
    private int visitIFGECount;
    private int visitIFGTCount;
    private int visitIfInstructionCount;
    private int visitIFLECount;
    private int visitIFLTCount;
    private int visitIFNECount;
    private int visitIFNONNULLCount;
    private int visitIFNULLCount;
    private int visitIINCCount;
    private int visitILOADCount;
    private int visitIMPDEP1Count;
    private int visitIMPDEP2Count;
    private int visitIMULCount;
    private int visitINEGCount;
    private int visitINSTANCEOFCount;
    private int visitINVOKEDYNAMICCount;
    private int visitInvokeInstructionCount;
    private int visitINVOKEINTERFACECount;
    private int visitINVOKESPECIALCount;
    private int visitINVOKESTATICCount;
    private int visitINVOKEVIRTUALCount;
    private int visitIORCount;
    private int visitIREMCount;
    private int visitIRETURNCount;
    private int visitISHLCount;
    private int visitISHRCount;
    private int visitISTORECount;
    private int visitISUBCount;
    private int visitIUSHRCount;
    private int visitIXORCount;
    private int visitJSRCount;
    private int visitJSR_WCount;
    private int visitJsrInstructionCount;
    private int visitL2DCount;
    private int visitL2FCount;
    private int visitL2ICount;
    private int visitLADDCount;
    private int visitLALOADCount;
    private int visitLANDCount;
    private int visitLASTORECount;
    private int visitLCMPCount;
    private int visitLCONSTCount;
    private int visitLDCCount;
    private int visitLDC2_WCount;
    private int visitLDIVCount;
    private int visitLLOADCount;
    private int visitLMULCount;
    private int visitLNEGCount;
    private int visitLoadClassCount;
    private int visitLoadInstructionCount;
    private int visitLocalVariableInstructionCount;
    private int visitLOOKUPSWITCHCount;
    private int visitLORCount;
    private int visitLREMCount;
    private int visitLRETURNCount;
    private int visitLSHLCount;
    private int visitLSHRCount;
    private int visitLSTORECount;
    private int visitLSUBCount;
    private int visitLUSHRCount;
    private int visitLXORCount;
    private int visitMONITORENTERCount;
    private int visitMONITOREXITCount;
    private int visitMULTIANEWARRAYCount;
    private int visitNEWCount;
    private int visitNEWARRAYCount;
    private int visitNOPCount;
    private int visitPOPCount;
    private int visitPOP2Count;
    private int visitPopInstructionCount;
    private int visitPushInstructionCount;
    private int visitPUTFIELDCount;
    private int visitPUTSTATICCount;
    private int visitRETCount;
    private int visitRETURNCount;
    private int visitReturnInstructionCount;
    private int visitSALOADCount;
    private int visitSASTORECount;
    private int visitSelectCount;
    private int visitSIPUSHCount;
    private int visitStackConsumerCount;
    private int visitStackInstructionCount;
    private int visitStackProducerCount;
    private int visitStoreInstructionCount;
    private int visitSWAPCount;
    private int visitTABLESWITCHCount;
    private int visitTypedInstructionCount;
    private int visitUnconditionalBranchCount;
    private int visitVariableLengthInstructionCount;

    public int getVisitAALOADCount() {
        return visitAALOADCount;
    }

    public void setVisitAALOADCount(int visitAALOADCount) {
        this.visitAALOADCount = visitAALOADCount;
    }

    public int getVisitAASTORECount() {
        return visitAASTORECount;
    }

    public void setVisitAASTORECount(int visitAASTORECount) {
        this.visitAASTORECount = visitAASTORECount;
    }

    public int getVisitACONST_NULLCount() {
        return visitACONST_NULLCount;
    }

    public void setVisitACONST_NULLCount(int visitACONST_NULLCount) {
        this.visitACONST_NULLCount = visitACONST_NULLCount;
    }

    public int getVisitAllocationInstructionCount() {
        return visitAllocationInstructionCount;
    }

    public void setVisitAllocationInstructionCount(int visitAllocationInstructionCount) {
        this.visitAllocationInstructionCount = visitAllocationInstructionCount;
    }

    public int getVisitALOADCount() {
        return visitALOADCount;
    }

    public void setVisitALOADCount(int visitALOADCount) {
        this.visitALOADCount = visitALOADCount;
    }

    public int getVisitANEWARRAYCount() {
        return visitANEWARRAYCount;
    }

    public void setVisitANEWARRAYCount(int visitANEWARRAYCount) {
        this.visitANEWARRAYCount = visitANEWARRAYCount;
    }

    public int getVisitARETURNCount() {
        return visitARETURNCount;
    }

    public void setVisitARETURNCount(int visitARETURNCount) {
        this.visitARETURNCount = visitARETURNCount;
    }

    public int getVisitArithmeticInstructionCount() {
        return visitArithmeticInstructionCount;
    }

    public void setVisitArithmeticInstructionCount(int visitArithmeticInstructionCount) {
        this.visitArithmeticInstructionCount = visitArithmeticInstructionCount;
    }

    public int getVisitArrayInstructionCount() {
        return visitArrayInstructionCount;
    }

    public void setVisitArrayInstructionCount(int visitArrayInstructionCount) {
        this.visitArrayInstructionCount = visitArrayInstructionCount;
    }

    public int getVisitARRAYLENGTHCount() {
        return visitARRAYLENGTHCount;
    }

    public void setVisitARRAYLENGTHCount(int visitARRAYLENGTHCount) {
        this.visitARRAYLENGTHCount = visitARRAYLENGTHCount;
    }

    public int getVisitASTORECount() {
        return visitASTORECount;
    }

    public void setVisitASTORECount(int visitASTORECount) {
        this.visitASTORECount = visitASTORECount;
    }

    public int getVisitATHROWCount() {
        return visitATHROWCount;
    }

    public void setVisitATHROWCount(int visitATHROWCount) {
        this.visitATHROWCount = visitATHROWCount;
    }

    public int getVisitBALOADCount() {
        return visitBALOADCount;
    }

    public void setVisitBALOADCount(int visitBALOADCount) {
        this.visitBALOADCount = visitBALOADCount;
    }

    public int getVisitBASTORECount() {
        return visitBASTORECount;
    }

    public void setVisitBASTORECount(int visitBASTORECount) {
        this.visitBASTORECount = visitBASTORECount;
    }

    public int getVisitBIPUSHCount() {
        return visitBIPUSHCount;
    }

    public void setVisitBIPUSHCount(int visitBIPUSHCount) {
        this.visitBIPUSHCount = visitBIPUSHCount;
    }

    public int getVisitBranchInstructionCount() {
        return visitBranchInstructionCount;
    }

    public void setVisitBranchInstructionCount(int visitBranchInstructionCount) {
        this.visitBranchInstructionCount = visitBranchInstructionCount;
    }

    public int getVisitBREAKPOINTCount() {
        return visitBREAKPOINTCount;
    }

    public void setVisitBREAKPOINTCount(int visitBREAKPOINTCount) {
        this.visitBREAKPOINTCount = visitBREAKPOINTCount;
    }

    public int getVisitCALOADCount() {
        return visitCALOADCount;
    }

    public void setVisitCALOADCount(int visitCALOADCount) {
        this.visitCALOADCount = visitCALOADCount;
    }

    public int getVisitCASTORECount() {
        return visitCASTORECount;
    }

    public void setVisitCASTORECount(int visitCASTORECount) {
        this.visitCASTORECount = visitCASTORECount;
    }

    public int getVisitCHECKCASTCount() {
        return visitCHECKCASTCount;
    }

    public void setVisitCHECKCASTCount(int visitCHECKCASTCount) {
        this.visitCHECKCASTCount = visitCHECKCASTCount;
    }

    public int getVisitConstantPushInstructionCount() {
        return visitConstantPushInstructionCount;
    }

    public void setVisitConstantPushInstructionCount(int visitConstantPushInstructionCount) {
        this.visitConstantPushInstructionCount = visitConstantPushInstructionCount;
    }

    public int getVisitConversionInstructionCount() {
        return visitConversionInstructionCount;
    }

    public void setVisitConversionInstructionCount(int visitConversionInstructionCount) {
        this.visitConversionInstructionCount = visitConversionInstructionCount;
    }

    public int getVisitCPInstructionCount() {
        return visitCPInstructionCount;
    }

    public void setVisitCPInstructionCount(int visitCPInstructionCount) {
        this.visitCPInstructionCount = visitCPInstructionCount;
    }

    public int getVisitD2FCount() {
        return visitD2FCount;
    }

    public void setVisitD2FCount(int visitD2FCount) {
        this.visitD2FCount = visitD2FCount;
    }

    public int getVisitD2ICount() {
        return visitD2ICount;
    }

    public void setVisitD2ICount(int visitD2ICount) {
        this.visitD2ICount = visitD2ICount;
    }

    public int getVisitD2LCount() {
        return visitD2LCount;
    }

    public void setVisitD2LCount(int visitD2LCount) {
        this.visitD2LCount = visitD2LCount;
    }

    public int getVisitDADDCount() {
        return visitDADDCount;
    }

    public void setVisitDADDCount(int visitDADDCount) {
        this.visitDADDCount = visitDADDCount;
    }

    public int getVisitDALOADCount() {
        return visitDALOADCount;
    }

    public void setVisitDALOADCount(int visitDALOADCount) {
        this.visitDALOADCount = visitDALOADCount;
    }

    public int getVisitDASTORECount() {
        return visitDASTORECount;
    }

    public void setVisitDASTORECount(int visitDASTORECount) {
        this.visitDASTORECount = visitDASTORECount;
    }

    public int getVisitDCMPGCount() {
        return visitDCMPGCount;
    }

    public void setVisitDCMPGCount(int visitDCMPGCount) {
        this.visitDCMPGCount = visitDCMPGCount;
    }

    public int getVisitDCMPLCount() {
        return visitDCMPLCount;
    }

    public void setVisitDCMPLCount(int visitDCMPLCount) {
        this.visitDCMPLCount = visitDCMPLCount;
    }

    public int getVisitDCONSTCount() {
        return visitDCONSTCount;
    }

    public void setVisitDCONSTCount(int visitDCONSTCount) {
        this.visitDCONSTCount = visitDCONSTCount;
    }

    public int getVisitDDIVCount() {
        return visitDDIVCount;
    }

    public void setVisitDDIVCount(int visitDDIVCount) {
        this.visitDDIVCount = visitDDIVCount;
    }

    public int getVisitDLOADCount() {
        return visitDLOADCount;
    }

    public void setVisitDLOADCount(int visitDLOADCount) {
        this.visitDLOADCount = visitDLOADCount;
    }

    public int getVisitDMULCount() {
        return visitDMULCount;
    }

    public void setVisitDMULCount(int visitDMULCount) {
        this.visitDMULCount = visitDMULCount;
    }

    public int getVisitDNEGCount() {
        return visitDNEGCount;
    }

    public void setVisitDNEGCount(int visitDNEGCount) {
        this.visitDNEGCount = visitDNEGCount;
    }

    public int getVisitDREMCount() {
        return visitDREMCount;
    }

    public void setVisitDREMCount(int visitDREMCount) {
        this.visitDREMCount = visitDREMCount;
    }

    public int getVisitDRETURNCount() {
        return visitDRETURNCount;
    }

    public void setVisitDRETURNCount(int visitDRETURNCount) {
        this.visitDRETURNCount = visitDRETURNCount;
    }

    public int getVisitDSTORECount() {
        return visitDSTORECount;
    }

    public void setVisitDSTORECount(int visitDSTORECount) {
        this.visitDSTORECount = visitDSTORECount;
    }

    public int getVisitDSUBCount() {
        return visitDSUBCount;
    }

    public void setVisitDSUBCount(int visitDSUBCount) {
        this.visitDSUBCount = visitDSUBCount;
    }

    public int getVisitDUPCount() {
        return visitDUPCount;
    }

    public void setVisitDUPCount(int visitDUPCount) {
        this.visitDUPCount = visitDUPCount;
    }

    public int getVisitDUP_X1Count() {
        return visitDUP_X1Count;
    }

    public void setVisitDUP_X1Count(int visitDUP_X1Count) {
        this.visitDUP_X1Count = visitDUP_X1Count;
    }

    public int getVisitDUP_X2Count() {
        return visitDUP_X2Count;
    }

    public void setVisitDUP_X2Count(int visitDUP_X2Count) {
        this.visitDUP_X2Count = visitDUP_X2Count;
    }

    public int getVisitDUP2Count() {
        return visitDUP2Count;
    }

    public void setVisitDUP2Count(int visitDUP2Count) {
        this.visitDUP2Count = visitDUP2Count;
    }

    public int getVisitDUP2_X1Count() {
        return visitDUP2_X1Count;
    }

    public void setVisitDUP2_X1Count(int visitDUP2_X1Count) {
        this.visitDUP2_X1Count = visitDUP2_X1Count;
    }

    public int getVisitDUP2_X2Count() {
        return visitDUP2_X2Count;
    }

    public void setVisitDUP2_X2Count(int visitDUP2_X2Count) {
        this.visitDUP2_X2Count = visitDUP2_X2Count;
    }

    public int getVisitExceptionThrowerCount() {
        return visitExceptionThrowerCount;
    }

    public void setVisitExceptionThrowerCount(int visitExceptionThrowerCount) {
        this.visitExceptionThrowerCount = visitExceptionThrowerCount;
    }

    public int getVisitF2DCount() {
        return visitF2DCount;
    }

    public void setVisitF2DCount(int visitF2DCount) {
        this.visitF2DCount = visitF2DCount;
    }

    public int getVisitF2ICount() {
        return visitF2ICount;
    }

    public void setVisitF2ICount(int visitF2ICount) {
        this.visitF2ICount = visitF2ICount;
    }

    public int getVisitF2LCount() {
        return visitF2LCount;
    }

    public void setVisitF2LCount(int visitF2LCount) {
        this.visitF2LCount = visitF2LCount;
    }

    public int getVisitFADDCount() {
        return visitFADDCount;
    }

    public void setVisitFADDCount(int visitFADDCount) {
        this.visitFADDCount = visitFADDCount;
    }

    public int getVisitFALOADCount() {
        return visitFALOADCount;
    }

    public void setVisitFALOADCount(int visitFALOADCount) {
        this.visitFALOADCount = visitFALOADCount;
    }

    public int getVisitFASTORECount() {
        return visitFASTORECount;
    }

    public void setVisitFASTORECount(int visitFASTORECount) {
        this.visitFASTORECount = visitFASTORECount;
    }

    public int getVisitFCMPGCount() {
        return visitFCMPGCount;
    }

    public void setVisitFCMPGCount(int visitFCMPGCount) {
        this.visitFCMPGCount = visitFCMPGCount;
    }

    public int getVisitFCMPLCount() {
        return visitFCMPLCount;
    }

    public void setVisitFCMPLCount(int visitFCMPLCount) {
        this.visitFCMPLCount = visitFCMPLCount;
    }

    public int getVisitFCONSTCount() {
        return visitFCONSTCount;
    }

    public void setVisitFCONSTCount(int visitFCONSTCount) {
        this.visitFCONSTCount = visitFCONSTCount;
    }

    public int getVisitFDIVCount() {
        return visitFDIVCount;
    }

    public void setVisitFDIVCount(int visitFDIVCount) {
        this.visitFDIVCount = visitFDIVCount;
    }

    public int getVisitFieldInstructionCount() {
        return visitFieldInstructionCount;
    }

    public void setVisitFieldInstructionCount(int visitFieldInstructionCount) {
        this.visitFieldInstructionCount = visitFieldInstructionCount;
    }

    public int getVisitFieldOrMethodCount() {
        return visitFieldOrMethodCount;
    }

    public void setVisitFieldOrMethodCount(int visitFieldOrMethodCount) {
        this.visitFieldOrMethodCount = visitFieldOrMethodCount;
    }

    public int getVisitFLOADCount() {
        return visitFLOADCount;
    }

    public void setVisitFLOADCount(int visitFLOADCount) {
        this.visitFLOADCount = visitFLOADCount;
    }

    public int getVisitFMULCount() {
        return visitFMULCount;
    }

    public void setVisitFMULCount(int visitFMULCount) {
        this.visitFMULCount = visitFMULCount;
    }

    public int getVisitFNEGCount() {
        return visitFNEGCount;
    }

    public void setVisitFNEGCount(int visitFNEGCount) {
        this.visitFNEGCount = visitFNEGCount;
    }

    public int getVisitFREMCount() {
        return visitFREMCount;
    }

    public void setVisitFREMCount(int visitFREMCount) {
        this.visitFREMCount = visitFREMCount;
    }

    public int getVisitFRETURNCount() {
        return visitFRETURNCount;
    }

    public void setVisitFRETURNCount(int visitFRETURNCount) {
        this.visitFRETURNCount = visitFRETURNCount;
    }

    public int getVisitFSTORECount() {
        return visitFSTORECount;
    }

    public void setVisitFSTORECount(int visitFSTORECount) {
        this.visitFSTORECount = visitFSTORECount;
    }

    public int getVisitFSUBCount() {
        return visitFSUBCount;
    }

    public void setVisitFSUBCount(int visitFSUBCount) {
        this.visitFSUBCount = visitFSUBCount;
    }

    public int getVisitGETFIELDCount() {
        return visitGETFIELDCount;
    }

    public void setVisitGETFIELDCount(int visitGETFIELDCount) {
        this.visitGETFIELDCount = visitGETFIELDCount;
    }

    public int getVisitGETSTATICCount() {
        return visitGETSTATICCount;
    }

    public void setVisitGETSTATICCount(int visitGETSTATICCount) {
        this.visitGETSTATICCount = visitGETSTATICCount;
    }

    public int getVisitGOTOCount() {
        return visitGOTOCount;
    }

    public void setVisitGOTOCount(int visitGOTOCount) {
        this.visitGOTOCount = visitGOTOCount;
    }

    public int getVisitGOTO_WCount() {
        return visitGOTO_WCount;
    }

    public void setVisitGOTO_WCount(int visitGOTO_WCount) {
        this.visitGOTO_WCount = visitGOTO_WCount;
    }

    public int getVisitGotoInstructionCount() {
        return visitGotoInstructionCount;
    }

    public void setVisitGotoInstructionCount(int visitGotoInstructionCount) {
        this.visitGotoInstructionCount = visitGotoInstructionCount;
    }

    public int getVisitI2BCount() {
        return visitI2BCount;
    }

    public void setVisitI2BCount(int visitI2BCount) {
        this.visitI2BCount = visitI2BCount;
    }

    public int getVisitI2CCount() {
        return visitI2CCount;
    }

    public void setVisitI2CCount(int visitI2CCount) {
        this.visitI2CCount = visitI2CCount;
    }

    public int getVisitI2DCount() {
        return visitI2DCount;
    }

    public void setVisitI2DCount(int visitI2DCount) {
        this.visitI2DCount = visitI2DCount;
    }

    public int getVisitI2FCount() {
        return visitI2FCount;
    }

    public void setVisitI2FCount(int visitI2FCount) {
        this.visitI2FCount = visitI2FCount;
    }

    public int getVisitI2LCount() {
        return visitI2LCount;
    }

    public void setVisitI2LCount(int visitI2LCount) {
        this.visitI2LCount = visitI2LCount;
    }

    public int getVisitI2SCount() {
        return visitI2SCount;
    }

    public void setVisitI2SCount(int visitI2SCount) {
        this.visitI2SCount = visitI2SCount;
    }

    public int getVisitIADDCount() {
        return visitIADDCount;
    }

    public void setVisitIADDCount(int visitIADDCount) {
        this.visitIADDCount = visitIADDCount;
    }

    public int getVisitIALOADCount() {
        return visitIALOADCount;
    }

    public void setVisitIALOADCount(int visitIALOADCount) {
        this.visitIALOADCount = visitIALOADCount;
    }

    public int getVisitIANDCount() {
        return visitIANDCount;
    }

    public void setVisitIANDCount(int visitIANDCount) {
        this.visitIANDCount = visitIANDCount;
    }

    public int getVisitIASTORECount() {
        return visitIASTORECount;
    }

    public void setVisitIASTORECount(int visitIASTORECount) {
        this.visitIASTORECount = visitIASTORECount;
    }

    public int getVisitICONSTCount() {
        return visitICONSTCount;
    }

    public void setVisitICONSTCount(int visitICONSTCount) {
        this.visitICONSTCount = visitICONSTCount;
    }

    public int getVisitIDIVCount() {
        return visitIDIVCount;
    }

    public void setVisitIDIVCount(int visitIDIVCount) {
        this.visitIDIVCount = visitIDIVCount;
    }

    public int getVisitIF_ACMPEQCount() {
        return visitIF_ACMPEQCount;
    }

    public void setVisitIF_ACMPEQCount(int visitIF_ACMPEQCount) {
        this.visitIF_ACMPEQCount = visitIF_ACMPEQCount;
    }

    public int getVisitIF_ACMPNECount() {
        return visitIF_ACMPNECount;
    }

    public void setVisitIF_ACMPNECount(int visitIF_ACMPNECount) {
        this.visitIF_ACMPNECount = visitIF_ACMPNECount;
    }

    public int getVisitIF_ICMPEQCount() {
        return visitIF_ICMPEQCount;
    }

    public void setVisitIF_ICMPEQCount(int visitIF_ICMPEQCount) {
        this.visitIF_ICMPEQCount = visitIF_ICMPEQCount;
    }

    public int getVisitIF_ICMPGECount() {
        return visitIF_ICMPGECount;
    }

    public void setVisitIF_ICMPGECount(int visitIF_ICMPGECount) {
        this.visitIF_ICMPGECount = visitIF_ICMPGECount;
    }

    public int getVisitIF_ICMPGTCount() {
        return visitIF_ICMPGTCount;
    }

    public void setVisitIF_ICMPGTCount(int visitIF_ICMPGTCount) {
        this.visitIF_ICMPGTCount = visitIF_ICMPGTCount;
    }

    public int getVisitIF_ICMPLECount() {
        return visitIF_ICMPLECount;
    }

    public void setVisitIF_ICMPLECount(int visitIF_ICMPLECount) {
        this.visitIF_ICMPLECount = visitIF_ICMPLECount;
    }

    public int getVisitIF_ICMPLTCount() {
        return visitIF_ICMPLTCount;
    }

    public void setVisitIF_ICMPLTCount(int visitIF_ICMPLTCount) {
        this.visitIF_ICMPLTCount = visitIF_ICMPLTCount;
    }

    public int getVisitIF_ICMPNECount() {
        return visitIF_ICMPNECount;
    }

    public void setVisitIF_ICMPNECount(int visitIF_ICMPNECount) {
        this.visitIF_ICMPNECount = visitIF_ICMPNECount;
    }

    public int getVisitIFEQCount() {
        return visitIFEQCount;
    }

    public void setVisitIFEQCount(int visitIFEQCount) {
        this.visitIFEQCount = visitIFEQCount;
    }

    public int getVisitIFGECount() {
        return visitIFGECount;
    }

    public void setVisitIFGECount(int visitIFGECount) {
        this.visitIFGECount = visitIFGECount;
    }

    public int getVisitIFGTCount() {
        return visitIFGTCount;
    }

    public void setVisitIFGTCount(int visitIFGTCount) {
        this.visitIFGTCount = visitIFGTCount;
    }

    public int getVisitIfInstructionCount() {
        return visitIfInstructionCount;
    }

    public void setVisitIfInstructionCount(int visitIfInstructionCount) {
        this.visitIfInstructionCount = visitIfInstructionCount;
    }

    public int getVisitIFLECount() {
        return visitIFLECount;
    }

    public void setVisitIFLECount(int visitIFLECount) {
        this.visitIFLECount = visitIFLECount;
    }

    public int getVisitIFLTCount() {
        return visitIFLTCount;
    }

    public void setVisitIFLTCount(int visitIFLTCount) {
        this.visitIFLTCount = visitIFLTCount;
    }

    public int getVisitIFNECount() {
        return visitIFNECount;
    }

    public void setVisitIFNECount(int visitIFNECount) {
        this.visitIFNECount = visitIFNECount;
    }

    public int getVisitIFNONNULLCount() {
        return visitIFNONNULLCount;
    }

    public void setVisitIFNONNULLCount(int visitIFNONNULLCount) {
        this.visitIFNONNULLCount = visitIFNONNULLCount;
    }

    public int getVisitIFNULLCount() {
        return visitIFNULLCount;
    }

    public void setVisitIFNULLCount(int visitIFNULLCount) {
        this.visitIFNULLCount = visitIFNULLCount;
    }

    public int getVisitIINCCount() {
        return visitIINCCount;
    }

    public void setVisitIINCCount(int visitIINCCount) {
        this.visitIINCCount = visitIINCCount;
    }

    public int getVisitILOADCount() {
        return visitILOADCount;
    }

    public void setVisitILOADCount(int visitILOADCount) {
        this.visitILOADCount = visitILOADCount;
    }

    public int getVisitIMPDEP1Count() {
        return visitIMPDEP1Count;
    }

    public void setVisitIMPDEP1Count(int visitIMPDEP1Count) {
        this.visitIMPDEP1Count = visitIMPDEP1Count;
    }

    public int getVisitIMPDEP2Count() {
        return visitIMPDEP2Count;
    }

    public void setVisitIMPDEP2Count(int visitIMPDEP2Count) {
        this.visitIMPDEP2Count = visitIMPDEP2Count;
    }

    public int getVisitIMULCount() {
        return visitIMULCount;
    }

    public void setVisitIMULCount(int visitIMULCount) {
        this.visitIMULCount = visitIMULCount;
    }

    public int getVisitINEGCount() {
        return visitINEGCount;
    }

    public void setVisitINEGCount(int visitINEGCount) {
        this.visitINEGCount = visitINEGCount;
    }

    public int getVisitINSTANCEOFCount() {
        return visitINSTANCEOFCount;
    }

    public void setVisitINSTANCEOFCount(int visitINSTANCEOFCount) {
        this.visitINSTANCEOFCount = visitINSTANCEOFCount;
    }

    public int getVisitINVOKEDYNAMICCount() {
        return visitINVOKEDYNAMICCount;
    }

    public void setVisitINVOKEDYNAMICCount(int visitINVOKEDYNAMICCount) {
        this.visitINVOKEDYNAMICCount = visitINVOKEDYNAMICCount;
    }

    public int getVisitInvokeInstructionCount() {
        return visitInvokeInstructionCount;
    }

    public void setVisitInvokeInstructionCount(int visitInvokeInstructionCount) {
        this.visitInvokeInstructionCount = visitInvokeInstructionCount;
    }

    public int getVisitINVOKEINTERFACECount() {
        return visitINVOKEINTERFACECount;
    }

    public void setVisitINVOKEINTERFACECount(int visitINVOKEINTERFACECount) {
        this.visitINVOKEINTERFACECount = visitINVOKEINTERFACECount;
    }

    public int getVisitINVOKESPECIALCount() {
        return visitINVOKESPECIALCount;
    }

    public void setVisitINVOKESPECIALCount(int visitINVOKESPECIALCount) {
        this.visitINVOKESPECIALCount = visitINVOKESPECIALCount;
    }

    public int getVisitINVOKESTATICCount() {
        return visitINVOKESTATICCount;
    }

    public void setVisitINVOKESTATICCount(int visitINVOKESTATICCount) {
        this.visitINVOKESTATICCount = visitINVOKESTATICCount;
    }

    public int getVisitINVOKEVIRTUALCount() {
        return visitINVOKEVIRTUALCount;
    }

    public void setVisitINVOKEVIRTUALCount(int visitINVOKEVIRTUALCount) {
        this.visitINVOKEVIRTUALCount = visitINVOKEVIRTUALCount;
    }

    public int getVisitIORCount() {
        return visitIORCount;
    }

    public void setVisitIORCount(int visitIORCount) {
        this.visitIORCount = visitIORCount;
    }

    public int getVisitIREMCount() {
        return visitIREMCount;
    }

    public void setVisitIREMCount(int visitIREMCount) {
        this.visitIREMCount = visitIREMCount;
    }

    public int getVisitIRETURNCount() {
        return visitIRETURNCount;
    }

    public void setVisitIRETURNCount(int visitIRETURNCount) {
        this.visitIRETURNCount = visitIRETURNCount;
    }

    public int getVisitISHLCount() {
        return visitISHLCount;
    }

    public void setVisitISHLCount(int visitISHLCount) {
        this.visitISHLCount = visitISHLCount;
    }

    public int getVisitISHRCount() {
        return visitISHRCount;
    }

    public void setVisitISHRCount(int visitISHRCount) {
        this.visitISHRCount = visitISHRCount;
    }

    public int getVisitISTORECount() {
        return visitISTORECount;
    }

    public void setVisitISTORECount(int visitISTORECount) {
        this.visitISTORECount = visitISTORECount;
    }

    public int getVisitISUBCount() {
        return visitISUBCount;
    }

    public void setVisitISUBCount(int visitISUBCount) {
        this.visitISUBCount = visitISUBCount;
    }

    public int getVisitIUSHRCount() {
        return visitIUSHRCount;
    }

    public void setVisitIUSHRCount(int visitIUSHRCount) {
        this.visitIUSHRCount = visitIUSHRCount;
    }

    public int getVisitIXORCount() {
        return visitIXORCount;
    }

    public void setVisitIXORCount(int visitIXORCount) {
        this.visitIXORCount = visitIXORCount;
    }

    public int getVisitJSRCount() {
        return visitJSRCount;
    }

    public void setVisitJSRCount(int visitJSRCount) {
        this.visitJSRCount = visitJSRCount;
    }

    public int getVisitJSR_WCount() {
        return visitJSR_WCount;
    }

    public void setVisitJSR_WCount(int visitJSR_WCount) {
        this.visitJSR_WCount = visitJSR_WCount;
    }

    public int getVisitJsrInstructionCount() {
        return visitJsrInstructionCount;
    }

    public void setVisitJsrInstructionCount(int visitJsrInstructionCount) {
        this.visitJsrInstructionCount = visitJsrInstructionCount;
    }

    public int getVisitL2DCount() {
        return visitL2DCount;
    }

    public void setVisitL2DCount(int visitL2DCount) {
        this.visitL2DCount = visitL2DCount;
    }

    public int getVisitL2FCount() {
        return visitL2FCount;
    }

    public void setVisitL2FCount(int visitL2FCount) {
        this.visitL2FCount = visitL2FCount;
    }

    public int getVisitL2ICount() {
        return visitL2ICount;
    }

    public void setVisitL2ICount(int visitL2ICount) {
        this.visitL2ICount = visitL2ICount;
    }

    public int getVisitLADDCount() {
        return visitLADDCount;
    }

    public void setVisitLADDCount(int visitLADDCount) {
        this.visitLADDCount = visitLADDCount;
    }

    public int getVisitLALOADCount() {
        return visitLALOADCount;
    }

    public void setVisitLALOADCount(int visitLALOADCount) {
        this.visitLALOADCount = visitLALOADCount;
    }

    public int getVisitLANDCount() {
        return visitLANDCount;
    }

    public void setVisitLANDCount(int visitLANDCount) {
        this.visitLANDCount = visitLANDCount;
    }

    public int getVisitLASTORECount() {
        return visitLASTORECount;
    }

    public void setVisitLASTORECount(int visitLASTORECount) {
        this.visitLASTORECount = visitLASTORECount;
    }

    public int getVisitLCMPCount() {
        return visitLCMPCount;
    }

    public void setVisitLCMPCount(int visitLCMPCount) {
        this.visitLCMPCount = visitLCMPCount;
    }

    public int getVisitLCONSTCount() {
        return visitLCONSTCount;
    }

    public void setVisitLCONSTCount(int visitLCONSTCount) {
        this.visitLCONSTCount = visitLCONSTCount;
    }

    public int getVisitLDCCount() {
        return visitLDCCount;
    }

    public void setVisitLDCCount(int visitLDCCount) {
        this.visitLDCCount = visitLDCCount;
    }

    public int getVisitLDC2_WCount() {
        return visitLDC2_WCount;
    }

    public void setVisitLDC2_WCount(int visitLDC2_WCount) {
        this.visitLDC2_WCount = visitLDC2_WCount;
    }

    public int getVisitLDIVCount() {
        return visitLDIVCount;
    }

    public void setVisitLDIVCount(int visitLDIVCount) {
        this.visitLDIVCount = visitLDIVCount;
    }

    public int getVisitLLOADCount() {
        return visitLLOADCount;
    }

    public void setVisitLLOADCount(int visitLLOADCount) {
        this.visitLLOADCount = visitLLOADCount;
    }

    public int getVisitLMULCount() {
        return visitLMULCount;
    }

    public void setVisitLMULCount(int visitLMULCount) {
        this.visitLMULCount = visitLMULCount;
    }

    public int getVisitLNEGCount() {
        return visitLNEGCount;
    }

    public void setVisitLNEGCount(int visitLNEGCount) {
        this.visitLNEGCount = visitLNEGCount;
    }

    public int getVisitLoadClassCount() {
        return visitLoadClassCount;
    }

    public void setVisitLoadClassCount(int visitLoadClassCount) {
        this.visitLoadClassCount = visitLoadClassCount;
    }

    public int getVisitLoadInstructionCount() {
        return visitLoadInstructionCount;
    }

    public void setVisitLoadInstructionCount(int visitLoadInstructionCount) {
        this.visitLoadInstructionCount = visitLoadInstructionCount;
    }

    public int getVisitLocalVariableInstructionCount() {
        return visitLocalVariableInstructionCount;
    }

    public void setVisitLocalVariableInstructionCount(int visitLocalVariableInstructionCount) {
        this.visitLocalVariableInstructionCount = visitLocalVariableInstructionCount;
    }

    public int getVisitLOOKUPSWITCHCount() {
        return visitLOOKUPSWITCHCount;
    }

    public void setVisitLOOKUPSWITCHCount(int visitLOOKUPSWITCHCount) {
        this.visitLOOKUPSWITCHCount = visitLOOKUPSWITCHCount;
    }

    public int getVisitLORCount() {
        return visitLORCount;
    }

    public void setVisitLORCount(int visitLORCount) {
        this.visitLORCount = visitLORCount;
    }

    public int getVisitLREMCount() {
        return visitLREMCount;
    }

    public void setVisitLREMCount(int visitLREMCount) {
        this.visitLREMCount = visitLREMCount;
    }

    public int getVisitLRETURNCount() {
        return visitLRETURNCount;
    }

    public void setVisitLRETURNCount(int visitLRETURNCount) {
        this.visitLRETURNCount = visitLRETURNCount;
    }

    public int getVisitLSHLCount() {
        return visitLSHLCount;
    }

    public void setVisitLSHLCount(int visitLSHLCount) {
        this.visitLSHLCount = visitLSHLCount;
    }

    public int getVisitLSHRCount() {
        return visitLSHRCount;
    }

    public void setVisitLSHRCount(int visitLSHRCount) {
        this.visitLSHRCount = visitLSHRCount;
    }

    public int getVisitLSTORECount() {
        return visitLSTORECount;
    }

    public void setVisitLSTORECount(int visitLSTORECount) {
        this.visitLSTORECount = visitLSTORECount;
    }

    public int getVisitLSUBCount() {
        return visitLSUBCount;
    }

    public void setVisitLSUBCount(int visitLSUBCount) {
        this.visitLSUBCount = visitLSUBCount;
    }

    public int getVisitLUSHRCount() {
        return visitLUSHRCount;
    }

    public void setVisitLUSHRCount(int visitLUSHRCount) {
        this.visitLUSHRCount = visitLUSHRCount;
    }

    public int getVisitLXORCount() {
        return visitLXORCount;
    }

    public void setVisitLXORCount(int visitLXORCount) {
        this.visitLXORCount = visitLXORCount;
    }

    public int getVisitMONITORENTERCount() {
        return visitMONITORENTERCount;
    }

    public void setVisitMONITORENTERCount(int visitMONITORENTERCount) {
        this.visitMONITORENTERCount = visitMONITORENTERCount;
    }

    public int getVisitMONITOREXITCount() {
        return visitMONITOREXITCount;
    }

    public void setVisitMONITOREXITCount(int visitMONITOREXITCount) {
        this.visitMONITOREXITCount = visitMONITOREXITCount;
    }

    public int getVisitMULTIANEWARRAYCount() {
        return visitMULTIANEWARRAYCount;
    }

    public void setVisitMULTIANEWARRAYCount(int visitMULTIANEWARRAYCount) {
        this.visitMULTIANEWARRAYCount = visitMULTIANEWARRAYCount;
    }

    public int getVisitNEWCount() {
        return visitNEWCount;
    }

    public void setVisitNEWCount(int visitNEWCount) {
        this.visitNEWCount = visitNEWCount;
    }

    public int getVisitNEWARRAYCount() {
        return visitNEWARRAYCount;
    }

    public void setVisitNEWARRAYCount(int visitNEWARRAYCount) {
        this.visitNEWARRAYCount = visitNEWARRAYCount;
    }

    public int getVisitNOPCount() {
        return visitNOPCount;
    }

    public void setVisitNOPCount(int visitNOPCount) {
        this.visitNOPCount = visitNOPCount;
    }

    public int getVisitPOPCount() {
        return visitPOPCount;
    }

    public void setVisitPOPCount(int visitPOPCount) {
        this.visitPOPCount = visitPOPCount;
    }

    public int getVisitPOP2Count() {
        return visitPOP2Count;
    }

    public void setVisitPOP2Count(int visitPOP2Count) {
        this.visitPOP2Count = visitPOP2Count;
    }

    public int getVisitPopInstructionCount() {
        return visitPopInstructionCount;
    }

    public void setVisitPopInstructionCount(int visitPopInstructionCount) {
        this.visitPopInstructionCount = visitPopInstructionCount;
    }

    public int getVisitPushInstructionCount() {
        return visitPushInstructionCount;
    }

    public void setVisitPushInstructionCount(int visitPushInstructionCount) {
        this.visitPushInstructionCount = visitPushInstructionCount;
    }

    public int getVisitPUTFIELDCount() {
        return visitPUTFIELDCount;
    }

    public void setVisitPUTFIELDCount(int visitPUTFIELDCount) {
        this.visitPUTFIELDCount = visitPUTFIELDCount;
    }

    public int getVisitPUTSTATICCount() {
        return visitPUTSTATICCount;
    }

    public void setVisitPUTSTATICCount(int visitPUTSTATICCount) {
        this.visitPUTSTATICCount = visitPUTSTATICCount;
    }

    public int getVisitRETCount() {
        return visitRETCount;
    }

    public void setVisitRETCount(int visitRETCount) {
        this.visitRETCount = visitRETCount;
    }

    public int getVisitRETURNCount() {
        return visitRETURNCount;
    }

    public void setVisitRETURNCount(int visitRETURNCount) {
        this.visitRETURNCount = visitRETURNCount;
    }

    public int getVisitReturnInstructionCount() {
        return visitReturnInstructionCount;
    }

    public void setVisitReturnInstructionCount(int visitReturnInstructionCount) {
        this.visitReturnInstructionCount = visitReturnInstructionCount;
    }

    public int getVisitSALOADCount() {
        return visitSALOADCount;
    }

    public void setVisitSALOADCount(int visitSALOADCount) {
        this.visitSALOADCount = visitSALOADCount;
    }

    public int getVisitSASTORECount() {
        return visitSASTORECount;
    }

    public void setVisitSASTORECount(int visitSASTORECount) {
        this.visitSASTORECount = visitSASTORECount;
    }

    public int getVisitSelectCount() {
        return visitSelectCount;
    }

    public void setVisitSelectCount(int visitSelectCount) {
        this.visitSelectCount = visitSelectCount;
    }

    public int getVisitSIPUSHCount() {
        return visitSIPUSHCount;
    }

    public void setVisitSIPUSHCount(int visitSIPUSHCount) {
        this.visitSIPUSHCount = visitSIPUSHCount;
    }

    public int getVisitStackConsumerCount() {
        return visitStackConsumerCount;
    }

    public void setVisitStackConsumerCount(int visitStackConsumerCount) {
        this.visitStackConsumerCount = visitStackConsumerCount;
    }

    public int getVisitStackInstructionCount() {
        return visitStackInstructionCount;
    }

    public void setVisitStackInstructionCount(int visitStackInstructionCount) {
        this.visitStackInstructionCount = visitStackInstructionCount;
    }

    public int getVisitStackProducerCount() {
        return visitStackProducerCount;
    }

    public void setVisitStackProducerCount(int visitStackProducerCount) {
        this.visitStackProducerCount = visitStackProducerCount;
    }

    public int getVisitStoreInstructionCount() {
        return visitStoreInstructionCount;
    }

    public void setVisitStoreInstructionCount(int visitStoreInstructionCount) {
        this.visitStoreInstructionCount = visitStoreInstructionCount;
    }

    public int getVisitSWAPCount() {
        return visitSWAPCount;
    }

    public void setVisitSWAPCount(int visitSWAPCount) {
        this.visitSWAPCount = visitSWAPCount;
    }

    public int getVisitTABLESWITCHCount() {
        return visitTABLESWITCHCount;
    }

    public void setVisitTABLESWITCHCount(int visitTABLESWITCHCount) {
        this.visitTABLESWITCHCount = visitTABLESWITCHCount;
    }

    public int getVisitTypedInstructionCount() {
        return visitTypedInstructionCount;
    }

    public void setVisitTypedInstructionCount(int visitTypedInstructionCount) {
        this.visitTypedInstructionCount = visitTypedInstructionCount;
    }

    public int getVisitUnconditionalBranchCount() {
        return visitUnconditionalBranchCount;
    }

    public void setVisitUnconditionalBranchCount(int visitUnconditionalBranchCount) {
        this.visitUnconditionalBranchCount = visitUnconditionalBranchCount;
    }

    public int getVisitVariableLengthInstructionCount() {
        return visitVariableLengthInstructionCount;
    }

    public void setVisitVariableLengthInstructionCount(int visitVariableLengthInstructionCount) {
        this.visitVariableLengthInstructionCount = visitVariableLengthInstructionCount;
    }

    @Override
    public void visitAALOAD(AALOAD obj) {
        visitAALOADCount++;
    }

    @Override
    public void visitAASTORE(AASTORE obj) {
        visitAASTORECount++;
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        visitACONST_NULLCount++;
    }

    @Override
    public void visitAllocationInstruction(AllocationInstruction obj) {
        visitAllocationInstructionCount++;
    }

    @Override
    public void visitALOAD(ALOAD obj) {
        visitALOADCount++;
    }

    @Override
    public void visitANEWARRAY(ANEWARRAY obj) {
        visitANEWARRAYCount++;
    }

    @Override
    public void visitARETURN(ARETURN obj) {
        visitARETURNCount++;
    }

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {
        visitArithmeticInstructionCount++;
    }

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {
        visitArrayInstructionCount++;
    }

    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH obj) {
        visitARRAYLENGTHCount++;
    }

    @Override
    public void visitASTORE(ASTORE obj) {
        visitASTORECount++;
    }

    @Override
    public void visitATHROW(ATHROW obj) {
        visitATHROWCount++;
    }

    @Override
    public void visitBALOAD(BALOAD obj) {
        visitBALOADCount++;
    }

    @Override
    public void visitBASTORE(BASTORE obj) {
        visitBASTORECount++;
    }

    @Override
    public void visitBIPUSH(BIPUSH obj) {
        visitBIPUSHCount++;
    }

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {
        visitBranchInstructionCount++;
    }

    @Override
    public void visitBREAKPOINT(BREAKPOINT obj) {
        visitBREAKPOINTCount++;
    }

    @Override
    public void visitCALOAD(CALOAD obj) {
        visitCALOADCount++;
    }

    @Override
    public void visitCASTORE(CASTORE obj) {
        visitCASTORECount++;
    }

    @Override
    public void visitCHECKCAST(CHECKCAST obj) {
        visitCHECKCASTCount++;
    }

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {
        visitConstantPushInstructionCount++;
    }

    @Override
    public void visitConversionInstruction(ConversionInstruction obj) {
        visitConversionInstructionCount++;
    }

    @Override
    public void visitCPInstruction(CPInstruction obj) {
        visitCPInstructionCount++;
    }

    @Override
    public void visitD2F(D2F obj) {
        visitD2FCount++;
    }

    @Override
    public void visitD2I(D2I obj) {
        visitD2ICount++;
    }

    @Override
    public void visitD2L(D2L obj) {
        visitD2LCount++;
    }

    @Override
    public void visitDADD(DADD obj) {
        visitDADDCount++;
    }

    @Override
    public void visitDALOAD(DALOAD obj) {
        visitDALOADCount++;
    }

    @Override
    public void visitDASTORE(DASTORE obj) {
        visitDASTORECount++;
    }

    @Override
    public void visitDCMPG(DCMPG obj) {
        visitDCMPGCount++;
    }

    @Override
    public void visitDCMPL(DCMPL obj) {
        visitDCMPLCount++;
    }

    @Override
    public void visitDCONST(DCONST obj) {
        visitDCONSTCount++;
    }

    @Override
    public void visitDDIV(DDIV obj) {
        visitDDIVCount++;
    }

    @Override
    public void visitDLOAD(DLOAD obj) {
        visitDLOADCount++;
    }

    @Override
    public void visitDMUL(DMUL obj) {
        visitDMULCount++;
    }

    @Override
    public void visitDNEG(DNEG obj) {
        visitDNEGCount++;
    }

    @Override
    public void visitDREM(DREM obj) {
        visitDREMCount++;
    }

    @Override
    public void visitDRETURN(DRETURN obj) {
        visitDRETURNCount++;
    }

    @Override
    public void visitDSTORE(DSTORE obj) {
        visitDSTORECount++;
    }

    @Override
    public void visitDSUB(DSUB obj) {
        visitDSUBCount++;
    }

    @Override
    public void visitDUP(DUP obj) {
        visitDUPCount++;
    }

    @Override
    public void visitDUP_X1(DUP_X1 obj) {
        visitDUP_X1Count++;
    }

    @Override
    public void visitDUP_X2(DUP_X2 obj) {
        visitDUP_X2Count++;
    }

    @Override
    public void visitDUP2(DUP2 obj) {
        visitDUP2Count++;
    }

    @Override
    public void visitDUP2_X1(DUP2_X1 obj) {
        visitDUP2_X1Count++;
    }

    @Override
    public void visitDUP2_X2(DUP2_X2 obj) {
        visitDUP2_X2Count++;
    }

    @Override
    public void visitExceptionThrower(ExceptionThrower obj) {
        visitExceptionThrowerCount++;
    }

    @Override
    public void visitF2D(F2D obj) {
        visitF2DCount++;
    }

    @Override
    public void visitF2I(F2I obj) {
        visitF2ICount++;
    }

    @Override
    public void visitF2L(F2L obj) {
        visitF2LCount++;
    }

    @Override
    public void visitFADD(FADD obj) {
        visitFADDCount++;
    }

    @Override
    public void visitFALOAD(FALOAD obj) {
        visitFALOADCount++;
    }

    @Override
    public void visitFASTORE(FASTORE obj) {
        visitFASTORECount++;
    }

    @Override
    public void visitFCMPG(FCMPG obj) {
        visitFCMPGCount++;
    }

    @Override
    public void visitFCMPL(FCMPL obj) {
        visitFCMPLCount++;
    }

    @Override
    public void visitFCONST(FCONST obj) {
        visitFCONSTCount++;
    }

    @Override
    public void visitFDIV(FDIV obj) {
        visitFDIVCount++;
    }

    @Override
    public void visitFieldInstruction(FieldInstruction obj) {
        visitFieldInstructionCount++;
    }

    @Override
    public void visitFieldOrMethod(FieldOrMethod obj) {
        visitFieldOrMethodCount++;
    }

    @Override
    public void visitFLOAD(FLOAD obj) {
        visitFLOADCount++;
    }

    @Override
    public void visitFMUL(FMUL obj) {
        visitFMULCount++;
    }

    @Override
    public void visitFNEG(FNEG obj) {
        visitFNEGCount++;
    }

    @Override
    public void visitFREM(FREM obj) {
        visitFREMCount++;
    }

    @Override
    public void visitFRETURN(FRETURN obj) {
        visitFRETURNCount++;
    }

    @Override
    public void visitFSTORE(FSTORE obj) {
        visitFSTORECount++;
    }

    @Override
    public void visitFSUB(FSUB obj) {
        visitFSUBCount++;
    }

    @Override
    public void visitGETFIELD(GETFIELD obj) {
        visitGETFIELDCount++;
    }

    @Override
    public void visitGETSTATIC(GETSTATIC obj) {
        visitGETSTATICCount++;
    }

    @Override
    public void visitGOTO(GOTO obj) {
        visitGOTOCount++;
    }

    @Override
    public void visitGOTO_W(GOTO_W obj) {
        visitGOTO_WCount++;
    }

    @Override
    public void visitGotoInstruction(GotoInstruction obj) {
        visitGotoInstructionCount++;
    }

    @Override
    public void visitI2B(I2B obj) {
        visitI2BCount++;
    }

    @Override
    public void visitI2C(I2C obj) {
        visitI2CCount++;
    }

    @Override
    public void visitI2D(I2D obj) {
        visitI2DCount++;
    }

    @Override
    public void visitI2F(I2F obj) {
        visitI2FCount++;
    }

    @Override
    public void visitI2L(I2L obj) {
        visitI2LCount++;
    }

    @Override
    public void visitI2S(I2S obj) {
        visitI2SCount++;
    }

    @Override
    public void visitIADD(IADD obj) {
        visitIADDCount++;
    }

    @Override
    public void visitIALOAD(IALOAD obj) {
        visitIALOADCount++;
    }

    @Override
    public void visitIAND(IAND obj) {
        visitIANDCount++;
    }

    @Override
    public void visitIASTORE(IASTORE obj) {
        visitIASTORECount++;
    }

    @Override
    public void visitICONST(ICONST obj) {
        visitICONSTCount++;
    }

    @Override
    public void visitIDIV(IDIV obj) {
        visitIDIVCount++;
    }

    @Override
    public void visitIF_ACMPEQ(IF_ACMPEQ obj) {
        visitIF_ACMPEQCount++;
    }

    @Override
    public void visitIF_ACMPNE(IF_ACMPNE obj) {
        visitIF_ACMPNECount++;
    }

    @Override
    public void visitIF_ICMPEQ(IF_ICMPEQ obj) {
        visitIF_ICMPEQCount++;
    }

    @Override
    public void visitIF_ICMPGE(IF_ICMPGE obj) {
        visitIF_ICMPGECount++;
    }

    @Override
    public void visitIF_ICMPGT(IF_ICMPGT obj) {
        visitIF_ICMPGTCount++;
    }

    @Override
    public void visitIF_ICMPLE(IF_ICMPLE obj) {
        visitIF_ICMPLECount++;
    }

    @Override
    public void visitIF_ICMPLT(IF_ICMPLT obj) {
        visitIF_ICMPLTCount++;
    }

    @Override
    public void visitIF_ICMPNE(IF_ICMPNE obj) {
        visitIF_ICMPNECount++;
    }

    @Override
    public void visitIFEQ(IFEQ obj) {
        visitIFEQCount++;
    }

    @Override
    public void visitIFGE(IFGE obj) {
        visitIFGECount++;
    }

    @Override
    public void visitIFGT(IFGT obj) {
        visitIFGTCount++;
    }

    @Override
    public void visitIfInstruction(IfInstruction obj) {
        visitIfInstructionCount++;
    }

    @Override
    public void visitIFLE(IFLE obj) {
        visitIFLECount++;
    }

    @Override
    public void visitIFLT(IFLT obj) {
        visitIFLTCount++;
    }

    @Override
    public void visitIFNE(IFNE obj) {
        visitIFNECount++;
    }

    @Override
    public void visitIFNONNULL(IFNONNULL obj) {
        visitIFNONNULLCount++;
    }

    @Override
    public void visitIFNULL(IFNULL obj) {
        visitIFNULLCount++;
    }

    @Override
    public void visitIINC(IINC obj) {
        visitIINCCount++;
    }

    @Override
    public void visitILOAD(ILOAD obj) {
        visitILOADCount++;
    }

    @Override
    public void visitIMPDEP1(IMPDEP1 obj) {
        visitIMPDEP1Count++;
    }

    @Override
    public void visitIMPDEP2(IMPDEP2 obj) {
        visitIMPDEP2Count++;
    }

    @Override
    public void visitIMUL(IMUL obj) {
        visitIMULCount++;
    }

    @Override
    public void visitINEG(INEG obj) {
        visitINEGCount++;
    }

    @Override
    public void visitINSTANCEOF(INSTANCEOF obj) {
        visitINSTANCEOFCount++;
    }

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj) {
        visitINVOKEDYNAMICCount++;
    }

    @Override
    public void visitInvokeInstruction(InvokeInstruction obj) {
        visitInvokeInstructionCount++;
    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {
        visitINVOKEINTERFACECount++;
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        visitINVOKESPECIALCount++;
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        visitINVOKESTATICCount++;
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        visitINVOKEVIRTUALCount++;
    }

    @Override
    public void visitIOR(IOR obj) {
        visitIORCount++;
    }

    @Override
    public void visitIREM(IREM obj) {
        visitIREMCount++;
    }

    @Override
    public void visitIRETURN(IRETURN obj) {
        visitIRETURNCount++;
    }

    @Override
    public void visitISHL(ISHL obj) {
        visitISHLCount++;
    }

    @Override
    public void visitISHR(ISHR obj) {
        visitISHRCount++;
    }

    @Override
    public void visitISTORE(ISTORE obj) {
        visitISTORECount++;
    }

    @Override
    public void visitISUB(ISUB obj) {
        visitISUBCount++;
    }

    @Override
    public void visitIUSHR(IUSHR obj) {
        visitIUSHRCount++;
    }

    @Override
    public void visitIXOR(IXOR obj) {
        visitIXORCount++;
    }

    @Override
    public void visitJSR(JSR obj) {
        visitJSRCount++;
    }

    @Override
    public void visitJSR_W(JSR_W obj) {
        visitJSR_WCount++;
    }

    @Override
    public void visitJsrInstruction(JsrInstruction obj) {
        visitJsrInstructionCount++;
    }

    @Override
    public void visitL2D(L2D obj) {
        visitL2DCount++;
    }

    @Override
    public void visitL2F(L2F obj) {
        visitL2FCount++;
    }

    @Override
    public void visitL2I(L2I obj) {
        visitL2ICount++;
    }

    @Override
    public void visitLADD(LADD obj) {
        visitLADDCount++;
    }

    @Override
    public void visitLALOAD(LALOAD obj) {
        visitLALOADCount++;
    }

    @Override
    public void visitLAND(LAND obj) {
        visitLANDCount++;
    }

    @Override
    public void visitLASTORE(LASTORE obj) {
        visitLASTORECount++;
    }

    @Override
    public void visitLCMP(LCMP obj) {
        visitLCMPCount++;
    }

    @Override
    public void visitLCONST(LCONST obj) {
        visitLCONSTCount++;
    }

    @Override
    public void visitLDC(LDC obj) {
        visitLDCCount++;
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        visitLDC2_WCount++;
    }

    @Override
    public void visitLDIV(LDIV obj) {
        visitLDIVCount++;
    }

    @Override
    public void visitLLOAD(LLOAD obj) {
        visitLLOADCount++;
    }

    @Override
    public void visitLMUL(LMUL obj) {
        visitLMULCount++;
    }

    @Override
    public void visitLNEG(LNEG obj) {
        visitLNEGCount++;
    }

    @Override
    public void visitLoadClass(LoadClass obj) {
        visitLoadClassCount++;
    }

    @Override
    public void visitLoadInstruction(LoadInstruction obj) {
        visitLoadInstructionCount++;
    }

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {
        visitLocalVariableInstructionCount++;
    }

    @Override
    public void visitLOOKUPSWITCH(LOOKUPSWITCH obj) {
        visitLOOKUPSWITCHCount++;
    }

    @Override
    public void visitLOR(LOR obj) {
        visitLORCount++;
    }

    @Override
    public void visitLREM(LREM obj) {
        visitLREMCount++;
    }

    @Override
    public void visitLRETURN(LRETURN obj) {
        visitLRETURNCount++;
    }

    @Override
    public void visitLSHL(LSHL obj) {
        visitLSHLCount++;
    }

    @Override
    public void visitLSHR(LSHR obj) {
        visitLSHRCount++;
    }

    @Override
    public void visitLSTORE(LSTORE obj) {
        visitLSTORECount++;
    }

    @Override
    public void visitLSUB(LSUB obj) {
        visitLSUBCount++;
    }

    @Override
    public void visitLUSHR(LUSHR obj) {
        visitLUSHRCount++;
    }

    @Override
    public void visitLXOR(LXOR obj) {
        visitLXORCount++;
    }

    @Override
    public void visitMONITORENTER(MONITORENTER obj) {
        visitMONITORENTERCount++;
    }

    @Override
    public void visitMONITOREXIT(MONITOREXIT obj) {
        visitMONITOREXITCount++;
    }

    @Override
    public void visitMULTIANEWARRAY(MULTIANEWARRAY obj) {
        visitMULTIANEWARRAYCount++;
    }

    @Override
    public void visitNEW(NEW obj) {
        visitNEWCount++;
    }

    @Override
    public void visitNEWARRAY(NEWARRAY obj) {
        visitNEWARRAYCount++;
    }

    @Override
    public void visitNOP(NOP obj) {
        visitNOPCount++;
    }

    @Override
    public void visitPOP(POP obj) {
        visitPOPCount++;
    }

    @Override
    public void visitPOP2(POP2 obj) {
        visitPOP2Count++;
    }

    @Override
    public void visitPopInstruction(PopInstruction obj) {
        visitPopInstructionCount++;
    }

    @Override
    public void visitPushInstruction(PushInstruction obj) {
        visitPushInstructionCount++;
    }

    @Override
    public void visitPUTFIELD(PUTFIELD obj) {
        visitPUTFIELDCount++;
    }

    @Override
    public void visitPUTSTATIC(PUTSTATIC obj) {
        visitPUTSTATICCount++;
    }

    @Override
    public void visitRET(RET obj) {
        visitRETCount++;
    }

    @Override
    public void visitRETURN(RETURN obj) {
        visitRETURNCount++;
    }

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {
        visitReturnInstructionCount++;
    }

    @Override
    public void visitSALOAD(SALOAD obj) {
        visitSALOADCount++;
    }

    @Override
    public void visitSASTORE(SASTORE obj) {
        visitSASTORECount++;
    }

    @Override
    public void visitSelect(Select obj) {
        visitSelectCount++;
    }

    @Override
    public void visitSIPUSH(SIPUSH obj) {
        visitSIPUSHCount++;
    }

    @Override
    public void visitStackConsumer(StackConsumer obj) {
        visitStackConsumerCount++;
    }

    @Override
    public void visitStackInstruction(StackInstruction obj) {
        visitStackInstructionCount++;
    }

    @Override
    public void visitStackProducer(StackProducer obj) {
        visitStackProducerCount++;
    }

    @Override
    public void visitStoreInstruction(StoreInstruction obj) {
        visitStoreInstructionCount++;
    }

    @Override
    public void visitSWAP(SWAP obj) {
        visitSWAPCount++;
    }

    @Override
    public void visitTABLESWITCH(TABLESWITCH obj) {
        visitTABLESWITCHCount++;
    }

    @Override
    public void visitTypedInstruction(TypedInstruction obj) {
        visitTypedInstructionCount++;
    }

    @Override
    public void visitUnconditionalBranch(UnconditionalBranch obj) {
        visitUnconditionalBranchCount++;
    }

    @Override
    public void visitVariableLengthInstruction(VariableLengthInstruction obj) {
        visitVariableLengthInstructionCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountVisitor that = (CountVisitor) o;
        return visitAALOADCount == that.visitAALOADCount
                && visitAASTORECount == that.visitAASTORECount
                && visitACONST_NULLCount == that.visitACONST_NULLCount
                && visitAllocationInstructionCount == that.visitAllocationInstructionCount
                && visitALOADCount == that.visitALOADCount
                && visitANEWARRAYCount == that.visitANEWARRAYCount
                && visitARETURNCount == that.visitARETURNCount
                && visitArithmeticInstructionCount == that.visitArithmeticInstructionCount
                && visitArrayInstructionCount == that.visitArrayInstructionCount
                && visitARRAYLENGTHCount == that.visitARRAYLENGTHCount
                && visitASTORECount == that.visitASTORECount
                && visitATHROWCount == that.visitATHROWCount
                && visitBALOADCount == that.visitBALOADCount
                && visitBASTORECount == that.visitBASTORECount
                && visitBIPUSHCount == that.visitBIPUSHCount
                && visitBranchInstructionCount == that.visitBranchInstructionCount
                && visitBREAKPOINTCount == that.visitBREAKPOINTCount
                && visitCALOADCount == that.visitCALOADCount
                && visitCASTORECount == that.visitCASTORECount
                && visitCHECKCASTCount == that.visitCHECKCASTCount
                && visitConstantPushInstructionCount == that.visitConstantPushInstructionCount
                && visitConversionInstructionCount == that.visitConversionInstructionCount
                && visitCPInstructionCount == that.visitCPInstructionCount
                && visitD2FCount == that.visitD2FCount
                && visitD2ICount == that.visitD2ICount
                && visitD2LCount == that.visitD2LCount
                && visitDADDCount == that.visitDADDCount
                && visitDALOADCount == that.visitDALOADCount
                && visitDASTORECount == that.visitDASTORECount
                && visitDCMPGCount == that.visitDCMPGCount
                && visitDCMPLCount == that.visitDCMPLCount
                && visitDCONSTCount == that.visitDCONSTCount
                && visitDDIVCount == that.visitDDIVCount
                && visitDLOADCount == that.visitDLOADCount
                && visitDMULCount == that.visitDMULCount
                && visitDNEGCount == that.visitDNEGCount
                && visitDREMCount == that.visitDREMCount
                && visitDRETURNCount == that.visitDRETURNCount
                && visitDSTORECount == that.visitDSTORECount
                && visitDSUBCount == that.visitDSUBCount
                && visitDUPCount == that.visitDUPCount
                && visitDUP_X1Count == that.visitDUP_X1Count
                && visitDUP_X2Count == that.visitDUP_X2Count
                && visitDUP2Count == that.visitDUP2Count
                && visitDUP2_X1Count == that.visitDUP2_X1Count
                && visitDUP2_X2Count == that.visitDUP2_X2Count
                && visitExceptionThrowerCount == that.visitExceptionThrowerCount
                && visitF2DCount == that.visitF2DCount
                && visitF2ICount == that.visitF2ICount
                && visitF2LCount == that.visitF2LCount
                && visitFADDCount == that.visitFADDCount
                && visitFALOADCount == that.visitFALOADCount
                && visitFASTORECount == that.visitFASTORECount
                && visitFCMPGCount == that.visitFCMPGCount
                && visitFCMPLCount == that.visitFCMPLCount
                && visitFCONSTCount == that.visitFCONSTCount
                && visitFDIVCount == that.visitFDIVCount
                && visitFieldInstructionCount == that.visitFieldInstructionCount
                && visitFieldOrMethodCount == that.visitFieldOrMethodCount
                && visitFLOADCount == that.visitFLOADCount
                && visitFMULCount == that.visitFMULCount
                && visitFNEGCount == that.visitFNEGCount
                && visitFREMCount == that.visitFREMCount
                && visitFRETURNCount == that.visitFRETURNCount
                && visitFSTORECount == that.visitFSTORECount
                && visitFSUBCount == that.visitFSUBCount
                && visitGETFIELDCount == that.visitGETFIELDCount
                && visitGETSTATICCount == that.visitGETSTATICCount
                && visitGOTOCount == that.visitGOTOCount
                && visitGOTO_WCount == that.visitGOTO_WCount
                && visitGotoInstructionCount == that.visitGotoInstructionCount
                && visitI2BCount == that.visitI2BCount
                && visitI2CCount == that.visitI2CCount
                && visitI2DCount == that.visitI2DCount
                && visitI2FCount == that.visitI2FCount
                && visitI2LCount == that.visitI2LCount
                && visitI2SCount == that.visitI2SCount
                && visitIADDCount == that.visitIADDCount
                && visitIALOADCount == that.visitIALOADCount
                && visitIANDCount == that.visitIANDCount
                && visitIASTORECount == that.visitIASTORECount
                && visitICONSTCount == that.visitICONSTCount
                && visitIDIVCount == that.visitIDIVCount
                && visitIF_ACMPEQCount == that.visitIF_ACMPEQCount
                && visitIF_ACMPNECount == that.visitIF_ACMPNECount
                && visitIF_ICMPEQCount == that.visitIF_ICMPEQCount
                && visitIF_ICMPGECount == that.visitIF_ICMPGECount
                && visitIF_ICMPGTCount == that.visitIF_ICMPGTCount
                && visitIF_ICMPLECount == that.visitIF_ICMPLECount
                && visitIF_ICMPLTCount == that.visitIF_ICMPLTCount
                && visitIF_ICMPNECount == that.visitIF_ICMPNECount
                && visitIFEQCount == that.visitIFEQCount
                && visitIFGECount == that.visitIFGECount
                && visitIFGTCount == that.visitIFGTCount
                && visitIfInstructionCount == that.visitIfInstructionCount
                && visitIFLECount == that.visitIFLECount
                && visitIFLTCount == that.visitIFLTCount
                && visitIFNECount == that.visitIFNECount
                && visitIFNONNULLCount == that.visitIFNONNULLCount
                && visitIFNULLCount == that.visitIFNULLCount
                && visitIINCCount == that.visitIINCCount
                && visitILOADCount == that.visitILOADCount
                && visitIMPDEP1Count == that.visitIMPDEP1Count
                && visitIMPDEP2Count == that.visitIMPDEP2Count
                && visitIMULCount == that.visitIMULCount
                && visitINEGCount == that.visitINEGCount
                && visitINSTANCEOFCount == that.visitINSTANCEOFCount
                && visitINVOKEDYNAMICCount == that.visitINVOKEDYNAMICCount
                && visitInvokeInstructionCount == that.visitInvokeInstructionCount
                && visitINVOKEINTERFACECount == that.visitINVOKEINTERFACECount
                && visitINVOKESPECIALCount == that.visitINVOKESPECIALCount
                && visitINVOKESTATICCount == that.visitINVOKESTATICCount
                && visitINVOKEVIRTUALCount == that.visitINVOKEVIRTUALCount
                && visitIORCount == that.visitIORCount
                && visitIREMCount == that.visitIREMCount
                && visitIRETURNCount == that.visitIRETURNCount
                && visitISHLCount == that.visitISHLCount
                && visitISHRCount == that.visitISHRCount
                && visitISTORECount == that.visitISTORECount
                && visitISUBCount == that.visitISUBCount
                && visitIUSHRCount == that.visitIUSHRCount
                && visitIXORCount == that.visitIXORCount
                && visitJSRCount == that.visitJSRCount
                && visitJSR_WCount == that.visitJSR_WCount
                && visitJsrInstructionCount == that.visitJsrInstructionCount
                && visitL2DCount == that.visitL2DCount
                && visitL2FCount == that.visitL2FCount
                && visitL2ICount == that.visitL2ICount
                && visitLADDCount == that.visitLADDCount
                && visitLALOADCount == that.visitLALOADCount
                && visitLANDCount == that.visitLANDCount
                && visitLASTORECount == that.visitLASTORECount
                && visitLCMPCount == that.visitLCMPCount
                && visitLCONSTCount == that.visitLCONSTCount
                && visitLDCCount == that.visitLDCCount
                && visitLDC2_WCount == that.visitLDC2_WCount
                && visitLDIVCount == that.visitLDIVCount
                && visitLLOADCount == that.visitLLOADCount
                && visitLMULCount == that.visitLMULCount
                && visitLNEGCount == that.visitLNEGCount
                && visitLoadClassCount == that.visitLoadClassCount
                && visitLoadInstructionCount == that.visitLoadInstructionCount
                && visitLocalVariableInstructionCount == that.visitLocalVariableInstructionCount
                && visitLOOKUPSWITCHCount == that.visitLOOKUPSWITCHCount
                && visitLORCount == that.visitLORCount
                && visitLREMCount == that.visitLREMCount
                && visitLRETURNCount == that.visitLRETURNCount
                && visitLSHLCount == that.visitLSHLCount
                && visitLSHRCount == that.visitLSHRCount
                && visitLSTORECount == that.visitLSTORECount
                && visitLSUBCount == that.visitLSUBCount
                && visitLUSHRCount == that.visitLUSHRCount
                && visitLXORCount == that.visitLXORCount
                && visitMONITORENTERCount == that.visitMONITORENTERCount
                && visitMONITOREXITCount == that.visitMONITOREXITCount
                && visitMULTIANEWARRAYCount == that.visitMULTIANEWARRAYCount
                && visitNEWCount == that.visitNEWCount
                && visitNEWARRAYCount == that.visitNEWARRAYCount
                && visitNOPCount == that.visitNOPCount
                && visitPOPCount == that.visitPOPCount
                && visitPOP2Count == that.visitPOP2Count
                && visitPopInstructionCount == that.visitPopInstructionCount
                && visitPushInstructionCount == that.visitPushInstructionCount
                && visitPUTFIELDCount == that.visitPUTFIELDCount
                && visitPUTSTATICCount == that.visitPUTSTATICCount
                && visitRETCount == that.visitRETCount
                && visitRETURNCount == that.visitRETURNCount
                && visitReturnInstructionCount == that.visitReturnInstructionCount
                && visitSALOADCount == that.visitSALOADCount
                && visitSASTORECount == that.visitSASTORECount
                && visitSelectCount == that.visitSelectCount
                && visitSIPUSHCount == that.visitSIPUSHCount
                && visitStackConsumerCount == that.visitStackConsumerCount
                && visitStackInstructionCount == that.visitStackInstructionCount
                && visitStackProducerCount == that.visitStackProducerCount
                && visitStoreInstructionCount == that.visitStoreInstructionCount
                && visitSWAPCount == that.visitSWAPCount
                && visitTABLESWITCHCount == that.visitTABLESWITCHCount
                && visitTypedInstructionCount == that.visitTypedInstructionCount
                && visitUnconditionalBranchCount == that.visitUnconditionalBranchCount
                && visitVariableLengthInstructionCount == that.visitVariableLengthInstructionCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitAALOADCount
                , visitAASTORECount
                , visitACONST_NULLCount
                , visitAllocationInstructionCount
                , visitALOADCount
                , visitANEWARRAYCount
                , visitARETURNCount
                , visitArithmeticInstructionCount
                , visitArrayInstructionCount
                , visitARRAYLENGTHCount
                , visitASTORECount
                , visitATHROWCount
                , visitBALOADCount
                , visitBASTORECount
                , visitBIPUSHCount
                , visitBranchInstructionCount
                , visitBREAKPOINTCount
                , visitCALOADCount
                , visitCASTORECount
                , visitCHECKCASTCount
                , visitConstantPushInstructionCount
                , visitConversionInstructionCount
                , visitCPInstructionCount
                , visitD2FCount
                , visitD2ICount
                , visitD2LCount
                , visitDADDCount
                , visitDALOADCount
                , visitDASTORECount
                , visitDCMPGCount
                , visitDCMPLCount
                , visitDCONSTCount
                , visitDDIVCount
                , visitDLOADCount
                , visitDMULCount
                , visitDNEGCount
                , visitDREMCount
                , visitDRETURNCount
                , visitDSTORECount
                , visitDSUBCount
                , visitDUPCount
                , visitDUP_X1Count
                , visitDUP_X2Count
                , visitDUP2Count
                , visitDUP2_X1Count
                , visitDUP2_X2Count
                , visitExceptionThrowerCount
                , visitF2DCount
                , visitF2ICount
                , visitF2LCount
                , visitFADDCount
                , visitFALOADCount
                , visitFASTORECount
                , visitFCMPGCount
                , visitFCMPLCount
                , visitFCONSTCount
                , visitFDIVCount
                , visitFieldInstructionCount
                , visitFieldOrMethodCount
                , visitFLOADCount
                , visitFMULCount
                , visitFNEGCount
                , visitFREMCount
                , visitFRETURNCount
                , visitFSTORECount
                , visitFSUBCount
                , visitGETFIELDCount
                , visitGETSTATICCount
                , visitGOTOCount
                , visitGOTO_WCount
                , visitGotoInstructionCount
                , visitI2BCount
                , visitI2CCount
                , visitI2DCount
                , visitI2FCount
                , visitI2LCount
                , visitI2SCount
                , visitIADDCount
                , visitIALOADCount
                , visitIANDCount
                , visitIASTORECount
                , visitICONSTCount
                , visitIDIVCount
                , visitIF_ACMPEQCount
                , visitIF_ACMPNECount
                , visitIF_ICMPEQCount
                , visitIF_ICMPGECount
                , visitIF_ICMPGTCount
                , visitIF_ICMPLECount
                , visitIF_ICMPLTCount
                , visitIF_ICMPNECount
                , visitIFEQCount
                , visitIFGECount
                , visitIFGTCount
                , visitIfInstructionCount
                , visitIFLECount
                , visitIFLTCount
                , visitIFNECount
                , visitIFNONNULLCount
                , visitIFNULLCount
                , visitIINCCount
                , visitILOADCount
                , visitIMPDEP1Count
                , visitIMPDEP2Count
                , visitIMULCount
                , visitINEGCount
                , visitINSTANCEOFCount
                , visitINVOKEDYNAMICCount
                , visitInvokeInstructionCount
                , visitINVOKEINTERFACECount
                , visitINVOKESPECIALCount
                , visitINVOKESTATICCount
                , visitINVOKEVIRTUALCount
                , visitIORCount
                , visitIREMCount
                , visitIRETURNCount
                , visitISHLCount
                , visitISHRCount
                , visitISTORECount
                , visitISUBCount
                , visitIUSHRCount
                , visitIXORCount
                , visitJSRCount
                , visitJSR_WCount
                , visitJsrInstructionCount
                , visitL2DCount
                , visitL2FCount
                , visitL2ICount
                , visitLADDCount
                , visitLALOADCount
                , visitLANDCount
                , visitLASTORECount
                , visitLCMPCount
                , visitLCONSTCount
                , visitLDCCount
                , visitLDC2_WCount
                , visitLDIVCount
                , visitLLOADCount
                , visitLMULCount
                , visitLNEGCount
                , visitLoadClassCount
                , visitLoadInstructionCount
                , visitLocalVariableInstructionCount
                , visitLOOKUPSWITCHCount
                , visitLORCount
                , visitLREMCount
                , visitLRETURNCount
                , visitLSHLCount
                , visitLSHRCount
                , visitLSTORECount
                , visitLSUBCount
                , visitLUSHRCount
                , visitLXORCount
                , visitMONITORENTERCount
                , visitMONITOREXITCount
                , visitMULTIANEWARRAYCount
                , visitNEWCount
                , visitNEWARRAYCount
                , visitNOPCount
                , visitPOPCount
                , visitPOP2Count
                , visitPopInstructionCount
                , visitPushInstructionCount
                , visitPUTFIELDCount
                , visitPUTSTATICCount
                , visitRETCount
                , visitRETURNCount
                , visitReturnInstructionCount
                , visitSALOADCount
                , visitSASTORECount
                , visitSelectCount
                , visitSIPUSHCount
                , visitStackConsumerCount
                , visitStackInstructionCount
                , visitStackProducerCount
                , visitStoreInstructionCount
                , visitSWAPCount
                , visitTABLESWITCHCount
                , visitTypedInstructionCount
                , visitUnconditionalBranchCount
                , visitVariableLengthInstructionCount);
    }

    @Override
    public String toString() {
        return "CountVisitor{" +
                "visitAALOADCount=" + visitAALOADCount +
                ", visitAASTORECount=" + visitAASTORECount +
                ", visitACONST_NULLCount=" + visitACONST_NULLCount +
                ", visitAllocationInstructionCount=" + visitAllocationInstructionCount +
                ", visitALOADCount=" + visitALOADCount +
                ", visitANEWARRAYCount=" + visitANEWARRAYCount +
                ", visitARETURNCount=" + visitARETURNCount +
                ", visitArithmeticInstructionCount=" + visitArithmeticInstructionCount +
                ", visitArrayInstructionCount=" + visitArrayInstructionCount +
                ", visitARRAYLENGTHCount=" + visitARRAYLENGTHCount +
                ", visitASTORECount=" + visitASTORECount +
                ", visitATHROWCount=" + visitATHROWCount +
                ", visitBALOADCount=" + visitBALOADCount +
                ", visitBASTORECount=" + visitBASTORECount +
                ", visitBIPUSHCount=" + visitBIPUSHCount +
                ", visitBranchInstructionCount=" + visitBranchInstructionCount +
                ", visitBREAKPOINTCount=" + visitBREAKPOINTCount +
                ", visitCALOADCount=" + visitCALOADCount +
                ", visitCASTORECount=" + visitCASTORECount +
                ", visitCHECKCASTCount=" + visitCHECKCASTCount +
                ", visitConstantPushInstructionCount=" + visitConstantPushInstructionCount +
                ", visitConversionInstructionCount=" + visitConversionInstructionCount +
                ", visitCPInstructionCount=" + visitCPInstructionCount +
                ", visitD2FCount=" + visitD2FCount +
                ", visitD2ICount=" + visitD2ICount +
                ", visitD2LCount=" + visitD2LCount +
                ", visitDADDCount=" + visitDADDCount +
                ", visitDALOADCount=" + visitDALOADCount +
                ", visitDASTORECount=" + visitDASTORECount +
                ", visitDCMPGCount=" + visitDCMPGCount +
                ", visitDCMPLCount=" + visitDCMPLCount +
                ", visitDCONSTCount=" + visitDCONSTCount +
                ", visitDDIVCount=" + visitDDIVCount +
                ", visitDLOADCount=" + visitDLOADCount +
                ", visitDMULCount=" + visitDMULCount +
                ", visitDNEGCount=" + visitDNEGCount +
                ", visitDREMCount=" + visitDREMCount +
                ", visitDRETURNCount=" + visitDRETURNCount +
                ", visitDSTORECount=" + visitDSTORECount +
                ", visitDSUBCount=" + visitDSUBCount +
                ", visitDUPCount=" + visitDUPCount +
                ", visitDUP_X1Count=" + visitDUP_X1Count +
                ", visitDUP_X2Count=" + visitDUP_X2Count +
                ", visitDUP2Count=" + visitDUP2Count +
                ", visitDUP2_X1Count=" + visitDUP2_X1Count +
                ", visitDUP2_X2Count=" + visitDUP2_X2Count +
                ", visitExceptionThrowerCount=" + visitExceptionThrowerCount +
                ", visitF2DCount=" + visitF2DCount +
                ", visitF2ICount=" + visitF2ICount +
                ", visitF2LCount=" + visitF2LCount +
                ", visitFADDCount=" + visitFADDCount +
                ", visitFALOADCount=" + visitFALOADCount +
                ", visitFASTORECount=" + visitFASTORECount +
                ", visitFCMPGCount=" + visitFCMPGCount +
                ", visitFCMPLCount=" + visitFCMPLCount +
                ", visitFCONSTCount=" + visitFCONSTCount +
                ", visitFDIVCount=" + visitFDIVCount +
                ", visitFieldInstructionCount=" + visitFieldInstructionCount +
                ", visitFieldOrMethodCount=" + visitFieldOrMethodCount +
                ", visitFLOADCount=" + visitFLOADCount +
                ", visitFMULCount=" + visitFMULCount +
                ", visitFNEGCount=" + visitFNEGCount +
                ", visitFREMCount=" + visitFREMCount +
                ", visitFRETURNCount=" + visitFRETURNCount +
                ", visitFSTORECount=" + visitFSTORECount +
                ", visitFSUBCount=" + visitFSUBCount +
                ", visitGETFIELDCount=" + visitGETFIELDCount +
                ", visitGETSTATICCount=" + visitGETSTATICCount +
                ", visitGOTOCount=" + visitGOTOCount +
                ", visitGOTO_WCount=" + visitGOTO_WCount +
                ", visitGotoInstructionCount=" + visitGotoInstructionCount +
                ", visitI2BCount=" + visitI2BCount +
                ", visitI2CCount=" + visitI2CCount +
                ", visitI2DCount=" + visitI2DCount +
                ", visitI2FCount=" + visitI2FCount +
                ", visitI2LCount=" + visitI2LCount +
                ", visitI2SCount=" + visitI2SCount +
                ", visitIADDCount=" + visitIADDCount +
                ", visitIALOADCount=" + visitIALOADCount +
                ", visitIANDCount=" + visitIANDCount +
                ", visitIASTORECount=" + visitIASTORECount +
                ", visitICONSTCount=" + visitICONSTCount +
                ", visitIDIVCount=" + visitIDIVCount +
                ", visitIF_ACMPEQCount=" + visitIF_ACMPEQCount +
                ", visitIF_ACMPNECount=" + visitIF_ACMPNECount +
                ", visitIF_ICMPEQCount=" + visitIF_ICMPEQCount +
                ", visitIF_ICMPGECount=" + visitIF_ICMPGECount +
                ", visitIF_ICMPGTCount=" + visitIF_ICMPGTCount +
                ", visitIF_ICMPLECount=" + visitIF_ICMPLECount +
                ", visitIF_ICMPLTCount=" + visitIF_ICMPLTCount +
                ", visitIF_ICMPNECount=" + visitIF_ICMPNECount +
                ", visitIFEQCount=" + visitIFEQCount +
                ", visitIFGECount=" + visitIFGECount +
                ", visitIFGTCount=" + visitIFGTCount +
                ", visitIfInstructionCount=" + visitIfInstructionCount +
                ", visitIFLECount=" + visitIFLECount +
                ", visitIFLTCount=" + visitIFLTCount +
                ", visitIFNECount=" + visitIFNECount +
                ", visitIFNONNULLCount=" + visitIFNONNULLCount +
                ", visitIFNULLCount=" + visitIFNULLCount +
                ", visitIINCCount=" + visitIINCCount +
                ", visitILOADCount=" + visitILOADCount +
                ", visitIMPDEP1Count=" + visitIMPDEP1Count +
                ", visitIMPDEP2Count=" + visitIMPDEP2Count +
                ", visitIMULCount=" + visitIMULCount +
                ", visitINEGCount=" + visitINEGCount +
                ", visitINSTANCEOFCount=" + visitINSTANCEOFCount +
                ", visitINVOKEDYNAMICCount=" + visitINVOKEDYNAMICCount +
                ", visitInvokeInstructionCount=" + visitInvokeInstructionCount +
                ", visitINVOKEINTERFACECount=" + visitINVOKEINTERFACECount +
                ", visitINVOKESPECIALCount=" + visitINVOKESPECIALCount +
                ", visitINVOKESTATICCount=" + visitINVOKESTATICCount +
                ", visitINVOKEVIRTUALCount=" + visitINVOKEVIRTUALCount +
                ", visitIORCount=" + visitIORCount +
                ", visitIREMCount=" + visitIREMCount +
                ", visitIRETURNCount=" + visitIRETURNCount +
                ", visitISHLCount=" + visitISHLCount +
                ", visitISHRCount=" + visitISHRCount +
                ", visitISTORECount=" + visitISTORECount +
                ", visitISUBCount=" + visitISUBCount +
                ", visitIUSHRCount=" + visitIUSHRCount +
                ", visitIXORCount=" + visitIXORCount +
                ", visitJSRCount=" + visitJSRCount +
                ", visitJSR_WCount=" + visitJSR_WCount +
                ", visitJsrInstructionCount=" + visitJsrInstructionCount +
                ", visitL2DCount=" + visitL2DCount +
                ", visitL2FCount=" + visitL2FCount +
                ", visitL2ICount=" + visitL2ICount +
                ", visitLADDCount=" + visitLADDCount +
                ", visitLALOADCount=" + visitLALOADCount +
                ", visitLANDCount=" + visitLANDCount +
                ", visitLASTORECount=" + visitLASTORECount +
                ", visitLCMPCount=" + visitLCMPCount +
                ", visitLCONSTCount=" + visitLCONSTCount +
                ", visitLDCCount=" + visitLDCCount +
                ", visitLDC2_WCount=" + visitLDC2_WCount +
                ", visitLDIVCount=" + visitLDIVCount +
                ", visitLLOADCount=" + visitLLOADCount +
                ", visitLMULCount=" + visitLMULCount +
                ", visitLNEGCount=" + visitLNEGCount +
                ", visitLoadClassCount=" + visitLoadClassCount +
                ", visitLoadInstructionCount=" + visitLoadInstructionCount +
                ", visitLocalVariableInstructionCount=" + visitLocalVariableInstructionCount +
                ", visitLOOKUPSWITCHCount=" + visitLOOKUPSWITCHCount +
                ", visitLORCount=" + visitLORCount +
                ", visitLREMCount=" + visitLREMCount +
                ", visitLRETURNCount=" + visitLRETURNCount +
                ", visitLSHLCount=" + visitLSHLCount +
                ", visitLSHRCount=" + visitLSHRCount +
                ", visitLSTORECount=" + visitLSTORECount +
                ", visitLSUBCount=" + visitLSUBCount +
                ", visitLUSHRCount=" + visitLUSHRCount +
                ", visitLXORCount=" + visitLXORCount +
                ", visitMONITORENTERCount=" + visitMONITORENTERCount +
                ", visitMONITOREXITCount=" + visitMONITOREXITCount +
                ", visitMULTIANEWARRAYCount=" + visitMULTIANEWARRAYCount +
                ", visitNEWCount=" + visitNEWCount +
                ", visitNEWARRAYCount=" + visitNEWARRAYCount +
                ", visitNOPCount=" + visitNOPCount +
                ", visitPOPCount=" + visitPOPCount +
                ", visitPOP2Count=" + visitPOP2Count +
                ", visitPopInstructionCount=" + visitPopInstructionCount +
                ", visitPushInstructionCount=" + visitPushInstructionCount +
                ", visitPUTFIELDCount=" + visitPUTFIELDCount +
                ", visitPUTSTATICCount=" + visitPUTSTATICCount +
                ", visitRETCount=" + visitRETCount +
                ", visitRETURNCount=" + visitRETURNCount +
                ", visitReturnInstructionCount=" + visitReturnInstructionCount +
                ", visitSALOADCount=" + visitSALOADCount +
                ", visitSASTORECount=" + visitSASTORECount +
                ", visitSelectCount=" + visitSelectCount +
                ", visitSIPUSHCount=" + visitSIPUSHCount +
                ", visitStackConsumerCount=" + visitStackConsumerCount +
                ", visitStackInstructionCount=" + visitStackInstructionCount +
                ", visitStackProducerCount=" + visitStackProducerCount +
                ", visitStoreInstructionCount=" + visitStoreInstructionCount +
                ", visitSWAPCount=" + visitSWAPCount +
                ", visitTABLESWITCHCount=" + visitTABLESWITCHCount +
                ", visitTypedInstructionCount=" + visitTypedInstructionCount +
                ", visitUnconditionalBranchCount=" + visitUnconditionalBranchCount +
                ", visitVariableLengthInstructionCount=" + visitVariableLengthInstructionCount +
                '}';
    }
}

