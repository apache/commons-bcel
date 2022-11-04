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
    private int aaload;
    private int aastore;
    private int aconstNull;
    private int allocationInstruction;
    private int aload;
    private int anewarray;
    private int areturn;
    private int arithmeticInstruction;
    private int arrayInstruction;
    private int arraylength;
    private int astore;
    private int athrow;
    private int baload;
    private int bastore;
    private int bipush;
    private int branchInstruction;
    private int breakpoint;
    private int caload;
    private int castore;
    private int checkcast;
    private int constantPushInstruction;
    private int conversionInstruction;
    private int cpInstruction;
    private int d2F;
    private int d2I;
    private int d2L;
    private int dadd;
    private int daload;
    private int dastore;
    private int dcmpg;
    private int dcmpl;
    private int dconst;
    private int ddiv;
    private int dload;
    private int dmul;
    private int dneg;
    private int drem;
    private int dreturn;
    private int dstore;
    private int dsub;
    private int dup;
    private int dupX1;
    private int dupX2;
    private int dup2;
    private int dup2X1;
    private int dup2X2;
    private int exceptionThrower;
    private int f2D;
    private int f2I;
    private int f2L;
    private int fadd;
    private int faload;
    private int fastore;
    private int fcmpg;
    private int fcmpl;
    private int fconst;
    private int fdiv;
    private int fieldInstruction;
    private int fieldOrMethod;
    private int fload;
    private int fmul;
    private int fneg;
    private int frem;
    private int freturn;
    private int fstore;
    private int fsub;
    private int getfield;
    private int getstatic;
    // TBC
    private int goTo;
    private int gotoW;
    private int gotoInstruction;
    private int i2B;
    private int i2C;
    private int i2D;
    private int i2F;
    private int i2L;
    private int i2S;
    private int iadd;
    private int iaload;
    private int iand;
    private int iastore;
    private int iconst;
    private int idiv;
    private int ifAcmpeq;
    private int ifAcmpne;
    private int ifIcmpeq;
    private int ifIcmpge;
    private int ifIcmpgt;
    private int ifIcmple;
    private int ifIcmplt;
    private int ifIcmpne;
    private int ifeq;
    private int ifge;
    private int ifgt;
    private int ifInstruction;
    private int ifle;
    private int iflt;
    private int ifne;
    private int ifnonnull;
    private int ifnull;
    private int iinc;
    private int iload;
    private int impdep1;
    private int impdep2;
    private int imul;
    private int ineg;
    private int instanceOf;
    private int invokedynamic;
    private int invokeInstruction;
    private int invokeinterface;
    private int invokespecial;
    private int invokestatic;
    private int invokevirtual;
    private int ior;
    private int irem;
    private int ireturn;
    private int ishl;
    private int ishr;
    private int istore;
    private int isub;
    private int iushr;
    private int ixor;
    private int jsr;
    private int jsrW;
    private int jsrInstruction;
    private int l2D;
    private int l2F;
    private int l2I;
    private int ladd;
    private int laload;
    private int land;
    private int lastore;
    private int lcmp;
    private int lconst;
    private int ldc;
    private int ldc2W;
    private int ldiv;
    private int lload;
    private int lmul;
    private int lneg;
    private int loadClass;
    private int loadInstruction;
    private int localVariableInstruction;
    private int lookupswitch;
    private int lor;
    private int lrem;
    private int lreturn;
    private int lshl;
    private int lshr;
    private int lstore;
    private int lsub;
    private int lushr;
    private int lxor;
    private int monitorenter;
    private int monitorexit;
    private int multianewarray;
    // TBC
    private int nEW;
    private int newarray;
    private int nop;
    private int pop;
    private int pop2;
    private int popInstruction;
    private int pushInstruction;
    private int putfield;
    private int putstatic;
    private int ret;
    // TBC
    private int rETURN;
    private int returnInstruction;
    private int saload;
    private int sastore;
    private int select;
    private int sipush;
    private int stackConsumer;
    private int stackInstruction;
    private int stackProducer;
    private int storeInstruction;
    private int swap;
    private int tableswitch;
    private int typedInstruction;
    private int unconditionalBranch;
    private int variableLengthInstruction;

    @Override
    public String toString() {
        return "CountVisitor{" +
                "aaload=" + aaload +
                ", aastore=" + aastore +
                ", aconstNull=" + aconstNull +
                ", allocationInstruction=" + allocationInstruction +
                ", aload=" + aload +
                ", anewarray=" + anewarray +
                ", areturn=" + areturn +
                ", arithmeticInstruction=" + arithmeticInstruction +
                ", arrayInstruction=" + arrayInstruction +
                ", arraylength=" + arraylength +
                ", astore=" + astore +
                ", athrow=" + athrow +
                ", baload=" + baload +
                ", bastore=" + bastore +
                ", bipush=" + bipush +
                ", branchInstruction=" + branchInstruction +
                ", breakpoint=" + breakpoint +
                ", caload=" + caload +
                ", castore=" + castore +
                ", checkcast=" + checkcast +
                ", constantPushInstruction=" + constantPushInstruction +
                ", conversionInstruction=" + conversionInstruction +
                ", cpInstruction=" + cpInstruction +
                ", d2F=" + d2F +
                ", d2I=" + d2I +
                ", d2L=" + d2L +
                ", dadd=" + dadd +
                ", daload=" + daload +
                ", dastore=" + dastore +
                ", dcmpg=" + dcmpg +
                ", dcmpl=" + dcmpl +
                ", dconst=" + dconst +
                ", ddiv=" + ddiv +
                ", dload=" + dload +
                ", dmul=" + dmul +
                ", dneg=" + dneg +
                ", drem=" + drem +
                ", dreturn=" + dreturn +
                ", dstore=" + dstore +
                ", dsub=" + dsub +
                ", dup=" + dup +
                ", dupX1=" + dupX1 +
                ", dupX2=" + dupX2 +
                ", dup2=" + dup2 +
                ", dup2X1=" + dup2X1 +
                ", dup2X2=" + dup2X2 +
                ", exceptionThrower=" + exceptionThrower +
                ", f2D=" + f2D +
                ", f2I=" + f2I +
                ", f2L=" + f2L +
                ", fadd=" + fadd +
                ", faload=" + faload +
                ", fastore=" + fastore +
                ", fcmpg=" + fcmpg +
                ", fcmpl=" + fcmpl +
                ", fconst=" + fconst +
                ", fdiv=" + fdiv +
                ", fieldInstruction=" + fieldInstruction +
                ", fieldOrMethod=" + fieldOrMethod +
                ", fload=" + fload +
                ", fmul=" + fmul +
                ", fneg=" + fneg +
                ", frem=" + frem +
                ", freturn=" + freturn +
                ", fstore=" + fstore +
                ", fsub=" + fsub +
                ", getfield=" + getfield +
                ", getstatic=" + getstatic +
                ", goTo=" + goTo +
                ", gotoW=" + gotoW +
                ", gotoInstruction=" + gotoInstruction +
                ", i2B=" + i2B +
                ", i2C=" + i2C +
                ", i2D=" + i2D +
                ", i2F=" + i2F +
                ", i2L=" + i2L +
                ", i2S=" + i2S +
                ", iadd=" + iadd +
                ", iaload=" + iaload +
                ", iand=" + iand +
                ", iastore=" + iastore +
                ", iconst=" + iconst +
                ", idiv=" + idiv +
                ", ifAcmpeq=" + ifAcmpeq +
                ", ifAcmpne=" + ifAcmpne +
                ", ifIcmpeq=" + ifIcmpeq +
                ", ifIcmpge=" + ifIcmpge +
                ", ifIcmpgt=" + ifIcmpgt +
                ", ifIcmple=" + ifIcmple +
                ", ifIcmplt=" + ifIcmplt +
                ", ifIcmpne=" + ifIcmpne +
                ", ifeq=" + ifeq +
                ", ifge=" + ifge +
                ", ifgt=" + ifgt +
                ", ifInstruction=" + ifInstruction +
                ", ifle=" + ifle +
                ", iflt=" + iflt +
                ", ifne=" + ifne +
                ", ifnonnull=" + ifnonnull +
                ", ifnull=" + ifnull +
                ", iinc=" + iinc +
                ", iload=" + iload +
                ", impdep1=" + impdep1 +
                ", impdep2=" + impdep2 +
                ", imul=" + imul +
                ", ineg=" + ineg +
                ", instanceOf=" + instanceOf +
                ", invokedynamic=" + invokedynamic +
                ", invokeInstruction=" + invokeInstruction +
                ", invokeinterface=" + invokeinterface +
                ", invokespecial=" + invokespecial +
                ", invokestatic=" + invokestatic +
                ", invokevirtual=" + invokevirtual +
                ", ior=" + ior +
                ", irem=" + irem +
                ", ireturn=" + ireturn +
                ", ishl=" + ishl +
                ", ishr=" + ishr +
                ", istore=" + istore +
                ", isub=" + isub +
                ", iushr=" + iushr +
                ", ixor=" + ixor +
                ", jsr=" + jsr +
                ", jsrW=" + jsrW +
                ", jsrInstruction=" + jsrInstruction +
                ", l2D=" + l2D +
                ", l2F=" + l2F +
                ", l2I=" + l2I +
                ", ladd=" + ladd +
                ", laload=" + laload +
                ", land=" + land +
                ", lastore=" + lastore +
                ", lcmp=" + lcmp +
                ", lconst=" + lconst +
                ", ldc=" + ldc +
                ", ldc2W=" + ldc2W +
                ", ldiv=" + ldiv +
                ", lload=" + lload +
                ", lmul=" + lmul +
                ", lneg=" + lneg +
                ", loadClass=" + loadClass +
                ", loadInstruction=" + loadInstruction +
                ", localVariableInstruction=" + localVariableInstruction +
                ", lookupswitch=" + lookupswitch +
                ", lor=" + lor +
                ", lrem=" + lrem +
                ", lreturn=" + lreturn +
                ", lshl=" + lshl +
                ", lshr=" + lshr +
                ", lstore=" + lstore +
                ", lsub=" + lsub +
                ", lushr=" + lushr +
                ", lxor=" + lxor +
                ", monitorenter=" + monitorenter +
                ", monitorexit=" + monitorexit +
                ", multianewarray=" + multianewarray +
                ", nEW=" + nEW +
                ", newarray=" + newarray +
                ", nop=" + nop +
                ", pop=" + pop +
                ", pop2=" + pop2 +
                ", popInstruction=" + popInstruction +
                ", pushInstruction=" + pushInstruction +
                ", putfield=" + putfield +
                ", putstatic=" + putstatic +
                ", ret=" + ret +
                ", rETURN=" + rETURN +
                ", returnInstruction=" + returnInstruction +
                ", saload=" + saload +
                ", sastore=" + sastore +
                ", select=" + select +
                ", sipush=" + sipush +
                ", stackConsumer=" + stackConsumer +
                ", stackInstruction=" + stackInstruction +
                ", stackProducer=" + stackProducer +
                ", storeInstruction=" + storeInstruction +
                ", swap=" + swap +
                ", tableswitch=" + tableswitch +
                ", typedInstruction=" + typedInstruction +
                ", unconditionalBranch=" + unconditionalBranch +
                ", variableLengthInstruction=" + variableLengthInstruction +
                '}';
    }

    public int getAaload() {
        return aaload;
    }

    public void setAaload(int aaload) {
        this.aaload = aaload;
    }

    public int getAastore() {
        return aastore;
    }

    public void setAastore(int aastore) {
        this.aastore = aastore;
    }

    public int getAconstNull() {
        return aconstNull;
    }

    public void setAconstNull(int aconstNull) {
        this.aconstNull = aconstNull;
    }

    public int getAllocationInstruction() {
        return allocationInstruction;
    }

    public void setAllocationInstruction(int allocationInstruction) {
        this.allocationInstruction = allocationInstruction;
    }

    public int getAload() {
        return aload;
    }

    public void setAload(int aload) {
        this.aload = aload;
    }

    public int getAnewarray() {
        return anewarray;
    }

    public void setAnewarray(int anewarray) {
        this.anewarray = anewarray;
    }

    public int getAreturn() {
        return areturn;
    }

    public void setAreturn(int areturn) {
        this.areturn = areturn;
    }

    public int getArithmeticInstruction() {
        return arithmeticInstruction;
    }

    public void setArithmeticInstruction(int arithmeticInstruction) {
        this.arithmeticInstruction = arithmeticInstruction;
    }

    public int getArrayInstruction() {
        return arrayInstruction;
    }

    public void setArrayInstruction(int arrayInstruction) {
        this.arrayInstruction = arrayInstruction;
    }

    public int getArraylength() {
        return arraylength;
    }

    public void setArraylength(int arraylength) {
        this.arraylength = arraylength;
    }

    public int getAstore() {
        return astore;
    }

    public void setAstore(int astore) {
        this.astore = astore;
    }

    public int getAthrow() {
        return athrow;
    }

    public void setAthrow(int athrow) {
        this.athrow = athrow;
    }

    public int getBaload() {
        return baload;
    }

    public void setBaload(int baload) {
        this.baload = baload;
    }

    public int getBastore() {
        return bastore;
    }

    public void setBastore(int bastore) {
        this.bastore = bastore;
    }

    public int getBipush() {
        return bipush;
    }

    public void setBipush(int bipush) {
        this.bipush = bipush;
    }

    public int getBranchInstruction() {
        return branchInstruction;
    }

    public void setBranchInstruction(int branchInstruction) {
        this.branchInstruction = branchInstruction;
    }

    public int getBreakpoint() {
        return breakpoint;
    }

    public void setBreakpoint(int breakpoint) {
        this.breakpoint = breakpoint;
    }

    public int getCaload() {
        return caload;
    }

    public void setCaload(int caload) {
        this.caload = caload;
    }

    public int getCastore() {
        return castore;
    }

    public void setCastore(int castore) {
        this.castore = castore;
    }

    public int getCheckcast() {
        return checkcast;
    }

    public void setCheckcast(int checkcast) {
        this.checkcast = checkcast;
    }

    public int getConstantPushInstruction() {
        return constantPushInstruction;
    }

    public void setConstantPushInstruction(int constantPushInstruction) {
        this.constantPushInstruction = constantPushInstruction;
    }

    public int getConversionInstruction() {
        return conversionInstruction;
    }

    public void setConversionInstruction(int conversionInstruction) {
        this.conversionInstruction = conversionInstruction;
    }

    public int getCpInstruction() {
        return cpInstruction;
    }

    public void setCpInstruction(int cpInstruction) {
        this.cpInstruction = cpInstruction;
    }

    public int getD2F() {
        return d2F;
    }

    public void setD2F(int d2F) {
        this.d2F = d2F;
    }

    public int getD2I() {
        return d2I;
    }

    public void setD2I(int d2I) {
        this.d2I = d2I;
    }

    public int getD2L() {
        return d2L;
    }

    public void setD2L(int d2L) {
        this.d2L = d2L;
    }

    public int getDadd() {
        return dadd;
    }

    public void setDadd(int dadd) {
        this.dadd = dadd;
    }

    public int getDaload() {
        return daload;
    }

    public void setDaload(int daload) {
        this.daload = daload;
    }

    public int getDastore() {
        return dastore;
    }

    public void setDastore(int dastore) {
        this.dastore = dastore;
    }

    public int getDcmpg() {
        return dcmpg;
    }

    public void setDcmpg(int dcmpg) {
        this.dcmpg = dcmpg;
    }

    public int getDcmpl() {
        return dcmpl;
    }

    public void setDcmpl(int dcmpl) {
        this.dcmpl = dcmpl;
    }

    public int getDconst() {
        return dconst;
    }

    public void setDconst(int dconst) {
        this.dconst = dconst;
    }

    public int getDdiv() {
        return ddiv;
    }

    public void setDdiv(int ddiv) {
        this.ddiv = ddiv;
    }

    public int getDload() {
        return dload;
    }

    public void setDload(int dload) {
        this.dload = dload;
    }

    public int getDmul() {
        return dmul;
    }

    public void setDmul(int dmul) {
        this.dmul = dmul;
    }

    public int getDneg() {
        return dneg;
    }

    public void setDneg(int dneg) {
        this.dneg = dneg;
    }

    public int getDrem() {
        return drem;
    }

    public void setDrem(int drem) {
        this.drem = drem;
    }

    public int getDreturn() {
        return dreturn;
    }

    public void setDreturn(int dreturn) {
        this.dreturn = dreturn;
    }

    public int getDstore() {
        return dstore;
    }

    public void setDstore(int dstore) {
        this.dstore = dstore;
    }

    public int getDsub() {
        return dsub;
    }

    public void setDsub(int dsub) {
        this.dsub = dsub;
    }

    public int getDup() {
        return dup;
    }

    public void setDup(int dup) {
        this.dup = dup;
    }

    public int getDupX1() {
        return dupX1;
    }

    public void setDupX1(int dupX1) {
        this.dupX1 = dupX1;
    }

    public int getDupX2() {
        return dupX2;
    }

    public void setDupX2(int dupX2) {
        this.dupX2 = dupX2;
    }

    public int getDup2() {
        return dup2;
    }

    public void setDup2(int dup2) {
        this.dup2 = dup2;
    }

    public int getDup2X1() {
        return dup2X1;
    }

    public void setDup2X1(int dup2X1) {
        this.dup2X1 = dup2X1;
    }

    public int getDup2X2() {
        return dup2X2;
    }

    public void setDup2X2(int dup2X2) {
        this.dup2X2 = dup2X2;
    }

    public int getExceptionThrower() {
        return exceptionThrower;
    }

    public void setExceptionThrower(int exceptionThrower) {
        this.exceptionThrower = exceptionThrower;
    }

    public int getF2D() {
        return f2D;
    }

    public void setF2D(int f2D) {
        this.f2D = f2D;
    }

    public int getF2I() {
        return f2I;
    }

    public void setF2I(int f2I) {
        this.f2I = f2I;
    }

    public int getF2L() {
        return f2L;
    }

    public void setF2L(int f2L) {
        this.f2L = f2L;
    }

    public int getFadd() {
        return fadd;
    }

    public void setFadd(int fadd) {
        this.fadd = fadd;
    }

    public int getFaload() {
        return faload;
    }

    public void setFaload(int faload) {
        this.faload = faload;
    }

    public int getFastore() {
        return fastore;
    }

    public void setFastore(int fastore) {
        this.fastore = fastore;
    }

    public int getFcmpg() {
        return fcmpg;
    }

    public void setFcmpg(int fcmpg) {
        this.fcmpg = fcmpg;
    }

    public int getFcmpl() {
        return fcmpl;
    }

    public void setFcmpl(int fcmpl) {
        this.fcmpl = fcmpl;
    }

    public int getFconst() {
        return fconst;
    }

    public void setFconst(int fconst) {
        this.fconst = fconst;
    }

    public int getFdiv() {
        return fdiv;
    }

    public void setFdiv(int fdiv) {
        this.fdiv = fdiv;
    }

    public int getFieldInstruction() {
        return fieldInstruction;
    }

    public void setFieldInstruction(int fieldInstruction) {
        this.fieldInstruction = fieldInstruction;
    }

    public int getFieldOrMethod() {
        return fieldOrMethod;
    }

    public void setFieldOrMethod(int fieldOrMethod) {
        this.fieldOrMethod = fieldOrMethod;
    }

    public int getFload() {
        return fload;
    }

    public void setFload(int fload) {
        this.fload = fload;
    }

    public int getFmul() {
        return fmul;
    }

    public void setFmul(int fmul) {
        this.fmul = fmul;
    }

    public int getFneg() {
        return fneg;
    }

    public void setFneg(int fneg) {
        this.fneg = fneg;
    }

    public int getFrem() {
        return frem;
    }

    public void setFrem(int frem) {
        this.frem = frem;
    }

    public int getFreturn() {
        return freturn;
    }

    public void setFreturn(int freturn) {
        this.freturn = freturn;
    }

    public int getFstore() {
        return fstore;
    }

    public void setFstore(int fstore) {
        this.fstore = fstore;
    }

    public int getFsub() {
        return fsub;
    }

    public void setFsub(int fsub) {
        this.fsub = fsub;
    }

    public int getGetfield() {
        return getfield;
    }

    public void setGetfield(int getfield) {
        this.getfield = getfield;
    }

    public int getGetstatic() {
        return getstatic;
    }

    public void setGetstatic(int getstatic) {
        this.getstatic = getstatic;
    }

    public int getGoTo() {
        return goTo;
    }

    public void setGoTo(int goTo) {
        this.goTo = goTo;
    }

    public int getGotoW() {
        return gotoW;
    }

    public void setGotoW(int gotoW) {
        this.gotoW = gotoW;
    }

    public int getGotoInstruction() {
        return gotoInstruction;
    }

    public void setGotoInstruction(int gotoInstruction) {
        this.gotoInstruction = gotoInstruction;
    }

    public int getI2B() {
        return i2B;
    }

    public void setI2B(int i2B) {
        this.i2B = i2B;
    }

    public int getI2C() {
        return i2C;
    }

    public void setI2C(int i2C) {
        this.i2C = i2C;
    }

    public int getI2D() {
        return i2D;
    }

    public void setI2D(int i2D) {
        this.i2D = i2D;
    }

    public int getI2F() {
        return i2F;
    }

    public void setI2F(int i2F) {
        this.i2F = i2F;
    }

    public int getI2L() {
        return i2L;
    }

    public void setI2L(int i2L) {
        this.i2L = i2L;
    }

    public int getI2S() {
        return i2S;
    }

    public void setI2S(int i2S) {
        this.i2S = i2S;
    }

    public int getIadd() {
        return iadd;
    }

    public void setIadd(int iadd) {
        this.iadd = iadd;
    }

    public int getIaload() {
        return iaload;
    }

    public void setIaload(int iaload) {
        this.iaload = iaload;
    }

    public int getIand() {
        return iand;
    }

    public void setIand(int iand) {
        this.iand = iand;
    }

    public int getIastore() {
        return iastore;
    }

    public void setIastore(int iastore) {
        this.iastore = iastore;
    }

    public int getIconst() {
        return iconst;
    }

    public void setIconst(int iconst) {
        this.iconst = iconst;
    }

    public int getIdiv() {
        return idiv;
    }

    public void setIdiv(int idiv) {
        this.idiv = idiv;
    }

    public int getIfAcmpeq() {
        return ifAcmpeq;
    }

    public void setIfAcmpeq(int ifAcmpeq) {
        this.ifAcmpeq = ifAcmpeq;
    }

    public int getIfAcmpne() {
        return ifAcmpne;
    }

    public void setIfAcmpne(int ifAcmpne) {
        this.ifAcmpne = ifAcmpne;
    }

    public int getIfIcmpeq() {
        return ifIcmpeq;
    }

    public void setIfIcmpeq(int ifIcmpeq) {
        this.ifIcmpeq = ifIcmpeq;
    }

    public int getIfIcmpge() {
        return ifIcmpge;
    }

    public void setIfIcmpge(int ifIcmpge) {
        this.ifIcmpge = ifIcmpge;
    }

    public int getIfIcmpgt() {
        return ifIcmpgt;
    }

    public void setIfIcmpgt(int ifIcmpgt) {
        this.ifIcmpgt = ifIcmpgt;
    }

    public int getIfIcmple() {
        return ifIcmple;
    }

    public void setIfIcmple(int ifIcmple) {
        this.ifIcmple = ifIcmple;
    }

    public int getIfIcmplt() {
        return ifIcmplt;
    }

    public void setIfIcmplt(int ifIcmplt) {
        this.ifIcmplt = ifIcmplt;
    }

    public int getIfIcmpne() {
        return ifIcmpne;
    }

    public void setIfIcmpne(int ifIcmpne) {
        this.ifIcmpne = ifIcmpne;
    }

    public int getIfeq() {
        return ifeq;
    }

    public void setIfeq(int ifeq) {
        this.ifeq = ifeq;
    }

    public int getIfge() {
        return ifge;
    }

    public void setIfge(int ifge) {
        this.ifge = ifge;
    }

    public int getIfgt() {
        return ifgt;
    }

    public void setIfgt(int ifgt) {
        this.ifgt = ifgt;
    }

    public int getIfInstruction() {
        return ifInstruction;
    }

    public void setIfInstruction(int ifInstruction) {
        this.ifInstruction = ifInstruction;
    }

    public int getIfle() {
        return ifle;
    }

    public void setIfle(int ifle) {
        this.ifle = ifle;
    }

    public int getIflt() {
        return iflt;
    }

    public void setIflt(int iflt) {
        this.iflt = iflt;
    }

    public int getIfne() {
        return ifne;
    }

    public void setIfne(int ifne) {
        this.ifne = ifne;
    }

    public int getIfnonnull() {
        return ifnonnull;
    }

    public void setIfnonnull(int ifnonnull) {
        this.ifnonnull = ifnonnull;
    }

    public int getIfnull() {
        return ifnull;
    }

    public void setIfnull(int ifnull) {
        this.ifnull = ifnull;
    }

    public int getIinc() {
        return iinc;
    }

    public void setIinc(int iinc) {
        this.iinc = iinc;
    }

    public int getIload() {
        return iload;
    }

    public void setIload(int iload) {
        this.iload = iload;
    }

    public int getImpdep1() {
        return impdep1;
    }

    public void setImpdep1(int impdep1) {
        this.impdep1 = impdep1;
    }

    public int getImpdep2() {
        return impdep2;
    }

    public void setImpdep2(int impdep2) {
        this.impdep2 = impdep2;
    }

    public int getImul() {
        return imul;
    }

    public void setImul(int imul) {
        this.imul = imul;
    }

    public int getIneg() {
        return ineg;
    }

    public void setIneg(int ineg) {
        this.ineg = ineg;
    }

    public int getInstanceOf() {
        return instanceOf;
    }

    public void setInstanceOf(int instanceOf) {
        this.instanceOf = instanceOf;
    }

    public int getInvokedynamic() {
        return invokedynamic;
    }

    public void setInvokedynamic(int invokedynamic) {
        this.invokedynamic = invokedynamic;
    }

    public int getInvokeInstruction() {
        return invokeInstruction;
    }

    public void setInvokeInstruction(int invokeInstruction) {
        this.invokeInstruction = invokeInstruction;
    }

    public int getInvokeinterface() {
        return invokeinterface;
    }

    public void setInvokeinterface(int invokeinterface) {
        this.invokeinterface = invokeinterface;
    }

    public int getInvokespecial() {
        return invokespecial;
    }

    public void setInvokespecial(int invokespecial) {
        this.invokespecial = invokespecial;
    }

    public int getInvokestatic() {
        return invokestatic;
    }

    public void setInvokestatic(int invokestatic) {
        this.invokestatic = invokestatic;
    }

    public int getInvokevirtual() {
        return invokevirtual;
    }

    public void setInvokevirtual(int invokevirtual) {
        this.invokevirtual = invokevirtual;
    }

    public int getIor() {
        return ior;
    }

    public void setIor(int ior) {
        this.ior = ior;
    }

    public int getIrem() {
        return irem;
    }

    public void setIrem(int irem) {
        this.irem = irem;
    }

    public int getIreturn() {
        return ireturn;
    }

    public void setIreturn(int ireturn) {
        this.ireturn = ireturn;
    }

    public int getIshl() {
        return ishl;
    }

    public void setIshl(int ishl) {
        this.ishl = ishl;
    }

    public int getIshr() {
        return ishr;
    }

    public void setIshr(int ishr) {
        this.ishr = ishr;
    }

    public int getIstore() {
        return istore;
    }

    public void setIstore(int istore) {
        this.istore = istore;
    }

    public int getIsub() {
        return isub;
    }

    public void setIsub(int isub) {
        this.isub = isub;
    }

    public int getIushr() {
        return iushr;
    }

    public void setIushr(int iushr) {
        this.iushr = iushr;
    }

    public int getIxor() {
        return ixor;
    }

    public void setIxor(int ixor) {
        this.ixor = ixor;
    }

    public int getJsr() {
        return jsr;
    }

    public void setJsr(int jsr) {
        this.jsr = jsr;
    }

    public int getJsrW() {
        return jsrW;
    }

    public void setJsrW(int jsrW) {
        this.jsrW = jsrW;
    }

    public int getJsrInstruction() {
        return jsrInstruction;
    }

    public void setJsrInstruction(int jsrInstruction) {
        this.jsrInstruction = jsrInstruction;
    }

    public int getL2D() {
        return l2D;
    }

    public void setL2D(int l2D) {
        this.l2D = l2D;
    }

    public int getL2F() {
        return l2F;
    }

    public void setL2F(int l2F) {
        this.l2F = l2F;
    }

    public int getL2I() {
        return l2I;
    }

    public void setL2I(int l2I) {
        this.l2I = l2I;
    }

    public int getLadd() {
        return ladd;
    }

    public void setLadd(int ladd) {
        this.ladd = ladd;
    }

    public int getLaload() {
        return laload;
    }

    public void setLaload(int laload) {
        this.laload = laload;
    }

    public int getLand() {
        return land;
    }

    public void setLand(int land) {
        this.land = land;
    }

    public int getLastore() {
        return lastore;
    }

    public void setLastore(int lastore) {
        this.lastore = lastore;
    }

    public int getLcmp() {
        return lcmp;
    }

    public void setLcmp(int lcmp) {
        this.lcmp = lcmp;
    }

    public int getLconst() {
        return lconst;
    }

    public void setLconst(int lconst) {
        this.lconst = lconst;
    }

    public int getLdc() {
        return ldc;
    }

    public void setLdc(int ldc) {
        this.ldc = ldc;
    }

    public int getLdc2W() {
        return ldc2W;
    }

    public void setLdc2W(int ldc2W) {
        this.ldc2W = ldc2W;
    }

    public int getLdiv() {
        return ldiv;
    }

    public void setLdiv(int ldiv) {
        this.ldiv = ldiv;
    }

    public int getLload() {
        return lload;
    }

    public void setLload(int lload) {
        this.lload = lload;
    }

    public int getLmul() {
        return lmul;
    }

    public void setLmul(int lmul) {
        this.lmul = lmul;
    }

    public int getLneg() {
        return lneg;
    }

    public void setLneg(int lneg) {
        this.lneg = lneg;
    }

    public int getLoadClass() {
        return loadClass;
    }

    public void setLoadClass(int loadClass) {
        this.loadClass = loadClass;
    }

    public int getLoadInstruction() {
        return loadInstruction;
    }

    public void setLoadInstruction(int loadInstruction) {
        this.loadInstruction = loadInstruction;
    }

    public int getLocalVariableInstruction() {
        return localVariableInstruction;
    }

    public void setLocalVariableInstruction(int localVariableInstruction) {
        this.localVariableInstruction = localVariableInstruction;
    }

    public int getLookupswitch() {
        return lookupswitch;
    }

    public void setLookupswitch(int lookupswitch) {
        this.lookupswitch = lookupswitch;
    }

    public int getLor() {
        return lor;
    }

    public void setLor(int lor) {
        this.lor = lor;
    }

    public int getLrem() {
        return lrem;
    }

    public void setLrem(int lrem) {
        this.lrem = lrem;
    }

    public int getLreturn() {
        return lreturn;
    }

    public void setLreturn(int lreturn) {
        this.lreturn = lreturn;
    }

    public int getLshl() {
        return lshl;
    }

    public void setLshl(int lshl) {
        this.lshl = lshl;
    }

    public int getLshr() {
        return lshr;
    }

    public void setLshr(int lshr) {
        this.lshr = lshr;
    }

    public int getLstore() {
        return lstore;
    }

    public void setLstore(int lstore) {
        this.lstore = lstore;
    }

    public int getLsub() {
        return lsub;
    }

    public void setLsub(int lsub) {
        this.lsub = lsub;
    }

    public int getLushr() {
        return lushr;
    }

    public void setLushr(int lushr) {
        this.lushr = lushr;
    }

    public int getLxor() {
        return lxor;
    }

    public void setLxor(int lxor) {
        this.lxor = lxor;
    }

    public int getMonitorenter() {
        return monitorenter;
    }

    public void setMonitorenter(int monitorenter) {
        this.monitorenter = monitorenter;
    }

    public int getMonitorexit() {
        return monitorexit;
    }

    public void setMonitorexit(int monitorexit) {
        this.monitorexit = monitorexit;
    }

    public int getMultianewarray() {
        return multianewarray;
    }

    public void setMultianewarray(int multianewarray) {
        this.multianewarray = multianewarray;
    }

    public int getnEW() {
        return nEW;
    }

    public void setnEW(int nEW) {
        this.nEW = nEW;
    }

    public int getNewarray() {
        return newarray;
    }

    public void setNewarray(int newarray) {
        this.newarray = newarray;
    }

    public int getNop() {
        return nop;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getPop2() {
        return pop2;
    }

    public void setPop2(int pop2) {
        this.pop2 = pop2;
    }

    public int getPopInstruction() {
        return popInstruction;
    }

    public void setPopInstruction(int popInstruction) {
        this.popInstruction = popInstruction;
    }

    public int getPushInstruction() {
        return pushInstruction;
    }

    public void setPushInstruction(int pushInstruction) {
        this.pushInstruction = pushInstruction;
    }

    public int getPutfield() {
        return putfield;
    }

    public void setPutfield(int putfield) {
        this.putfield = putfield;
    }

    public int getPutstatic() {
        return putstatic;
    }

    public void setPutstatic(int putstatic) {
        this.putstatic = putstatic;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getrETURN() {
        return rETURN;
    }

    public void setrETURN(int rETURN) {
        this.rETURN = rETURN;
    }

    public int getReturnInstruction() {
        return returnInstruction;
    }

    public void setReturnInstruction(int returnInstruction) {
        this.returnInstruction = returnInstruction;
    }

    public int getSaload() {
        return saload;
    }

    public void setSaload(int saload) {
        this.saload = saload;
    }

    public int getSastore() {
        return sastore;
    }

    public void setSastore(int sastore) {
        this.sastore = sastore;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getSipush() {
        return sipush;
    }

    public void setSipush(int sipush) {
        this.sipush = sipush;
    }

    public int getStackConsumer() {
        return stackConsumer;
    }

    public void setStackConsumer(int stackConsumer) {
        this.stackConsumer = stackConsumer;
    }

    public int getStackInstruction() {
        return stackInstruction;
    }

    public void setStackInstruction(int stackInstruction) {
        this.stackInstruction = stackInstruction;
    }

    public int getStackProducer() {
        return stackProducer;
    }

    public void setStackProducer(int stackProducer) {
        this.stackProducer = stackProducer;
    }

    public int getStoreInstruction() {
        return storeInstruction;
    }

    public void setStoreInstruction(int storeInstruction) {
        this.storeInstruction = storeInstruction;
    }

    public int getSwap() {
        return swap;
    }

    public void setSwap(int swap) {
        this.swap = swap;
    }

    public int getTableswitch() {
        return tableswitch;
    }

    public void setTableswitch(int tableswitch) {
        this.tableswitch = tableswitch;
    }

    public int getTypedInstruction() {
        return typedInstruction;
    }

    public void setTypedInstruction(int typedInstruction) {
        this.typedInstruction = typedInstruction;
    }

    public int getUnconditionalBranch() {
        return unconditionalBranch;
    }

    public void setUnconditionalBranch(int unconditionalBranch) {
        this.unconditionalBranch = unconditionalBranch;
    }

    public int getVariableLengthInstruction() {
        return variableLengthInstruction;
    }

    public void setVariableLengthInstruction(int variableLengthInstruction) {
        this.variableLengthInstruction = variableLengthInstruction;
    }

    @Override
    public void visitAALOAD(AALOAD obj) {
        aaload++;
    }

    @Override
    public void visitAASTORE(AASTORE obj) {
        aastore++;
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        aconstNull++;
    }

    @Override
    public void visitAllocationInstruction(AllocationInstruction obj) {
        allocationInstruction++;
    }

    @Override
    public void visitALOAD(ALOAD obj) {
        aload++;
    }

    @Override
    public void visitANEWARRAY(ANEWARRAY obj) {
        anewarray++;
    }

    @Override
    public void visitARETURN(ARETURN obj) {
        areturn++;
    }

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {
        arithmeticInstruction++;
    }

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {
        arrayInstruction++;
    }

    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH obj) {
        arraylength++;
    }

    @Override
    public void visitASTORE(ASTORE obj) {
        astore++;
    }

    @Override
    public void visitATHROW(ATHROW obj) {
        athrow++;
    }

    @Override
    public void visitBALOAD(BALOAD obj) {
        baload++;
    }

    @Override
    public void visitBASTORE(BASTORE obj) {
        bastore++;
    }

    @Override
    public void visitBIPUSH(BIPUSH obj) {
        bipush++;
    }

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {
        branchInstruction++;
    }

    @Override
    public void visitBREAKPOINT(BREAKPOINT obj) {
        breakpoint++;
    }

    @Override
    public void visitCALOAD(CALOAD obj) {
        caload++;
    }

    @Override
    public void visitCASTORE(CASTORE obj) {
        castore++;
    }

    @Override
    public void visitCHECKCAST(CHECKCAST obj) {
        checkcast++;
    }

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {
        constantPushInstruction++;
    }

    @Override
    public void visitConversionInstruction(ConversionInstruction obj) {
        conversionInstruction++;
    }

    @Override
    public void visitCPInstruction(CPInstruction obj) {
        cpInstruction++;
    }

    @Override
    public void visitD2F(D2F obj) {
        d2F++;
    }

    @Override
    public void visitD2I(D2I obj) {
        d2I++;
    }

    @Override
    public void visitD2L(D2L obj) {
        d2L++;
    }

    @Override
    public void visitDADD(DADD obj) {
        dadd++;
    }

    @Override
    public void visitDALOAD(DALOAD obj) {
        daload++;
    }

    @Override
    public void visitDASTORE(DASTORE obj) {
        dastore++;
    }

    @Override
    public void visitDCMPG(DCMPG obj) {
        dcmpg++;
    }

    @Override
    public void visitDCMPL(DCMPL obj) {
        dcmpl++;
    }

    @Override
    public void visitDCONST(DCONST obj) {
        dconst++;
    }

    @Override
    public void visitDDIV(DDIV obj) {
        ddiv++;
    }

    @Override
    public void visitDLOAD(DLOAD obj) {
        dload++;
    }

    @Override
    public void visitDMUL(DMUL obj) {
        dmul++;
    }

    @Override
    public void visitDNEG(DNEG obj) {
        dneg++;
    }

    @Override
    public void visitDREM(DREM obj) {
        drem++;
    }

    @Override
    public void visitDRETURN(DRETURN obj) {
        dreturn++;
    }

    @Override
    public void visitDSTORE(DSTORE obj) {
        dstore++;
    }

    @Override
    public void visitDSUB(DSUB obj) {
        dsub++;
    }

    @Override
    public void visitDUP(DUP obj) {
        dup++;
    }

    @Override
    public void visitDUP_X1(DUP_X1 obj) {
        dupX1++;
    }

    @Override
    public void visitDUP_X2(DUP_X2 obj) {
        dupX2++;
    }

    @Override
    public void visitDUP2(DUP2 obj) {
        dup2++;
    }

    @Override
    public void visitDUP2_X1(DUP2_X1 obj) {
        dup2X1++;
    }

    @Override
    public void visitDUP2_X2(DUP2_X2 obj) {
        dup2X2++;
    }

    @Override
    public void visitExceptionThrower(ExceptionThrower obj) {
        exceptionThrower++;
    }

    @Override
    public void visitF2D(F2D obj) {
        f2D++;
    }

    @Override
    public void visitF2I(F2I obj) {
        f2I++;
    }

    @Override
    public void visitF2L(F2L obj) {
        f2L++;
    }

    @Override
    public void visitFADD(FADD obj) {
        fadd++;
    }

    @Override
    public void visitFALOAD(FALOAD obj) {
        faload++;
    }

    @Override
    public void visitFASTORE(FASTORE obj) {
        fastore++;
    }

    @Override
    public void visitFCMPG(FCMPG obj) {
        fcmpg++;
    }

    @Override
    public void visitFCMPL(FCMPL obj) {
        fcmpl++;
    }

    @Override
    public void visitFCONST(FCONST obj) {
        fconst++;
    }

    @Override
    public void visitFDIV(FDIV obj) {
        fdiv++;
    }

    @Override
    public void visitFieldInstruction(FieldInstruction obj) {
        fieldInstruction++;
    }

    @Override
    public void visitFieldOrMethod(FieldOrMethod obj) {
        fieldOrMethod++;
    }

    @Override
    public void visitFLOAD(FLOAD obj) {
        fload++;
    }

    @Override
    public void visitFMUL(FMUL obj) {
        fmul++;
    }

    @Override
    public void visitFNEG(FNEG obj) {
        fneg++;
    }

    @Override
    public void visitFREM(FREM obj) {
        frem++;
    }

    @Override
    public void visitFRETURN(FRETURN obj) {
        freturn++;
    }

    @Override
    public void visitFSTORE(FSTORE obj) {
        fstore++;
    }

    @Override
    public void visitFSUB(FSUB obj) {
        fsub++;
    }

    @Override
    public void visitGETFIELD(GETFIELD obj) {
        getfield++;
    }

    @Override
    public void visitGETSTATIC(GETSTATIC obj) {
        getstatic++;
    }

    @Override
    public void visitGOTO(GOTO obj) {
        goTo++;
    }

    @Override
    public void visitGOTO_W(GOTO_W obj) {
        gotoW++;
    }

    @Override
    public void visitGotoInstruction(GotoInstruction obj) {
        gotoInstruction++;
    }

    @Override
    public void visitI2B(I2B obj) {
        i2B++;
    }

    @Override
    public void visitI2C(I2C obj) {
        i2C++;
    }

    @Override
    public void visitI2D(I2D obj) {
        i2D++;
    }

    @Override
    public void visitI2F(I2F obj) {
        i2F++;
    }

    @Override
    public void visitI2L(I2L obj) {
        i2L++;
    }

    @Override
    public void visitI2S(I2S obj) {
        i2S++;
    }

    @Override
    public void visitIADD(IADD obj) {
        iadd++;
    }

    @Override
    public void visitIALOAD(IALOAD obj) {
        iaload++;
    }

    @Override
    public void visitIAND(IAND obj) {
        iand++;
    }

    @Override
    public void visitIASTORE(IASTORE obj) {
        iastore++;
    }

    @Override
    public void visitICONST(ICONST obj) {
        iconst++;
    }

    @Override
    public void visitIDIV(IDIV obj) {
        idiv++;
    }

    @Override
    public void visitIF_ACMPEQ(IF_ACMPEQ obj) {
        ifAcmpeq++;
    }

    @Override
    public void visitIF_ACMPNE(IF_ACMPNE obj) {
        ifAcmpne++;
    }

    @Override
    public void visitIF_ICMPEQ(IF_ICMPEQ obj) {
        ifIcmpeq++;
    }

    @Override
    public void visitIF_ICMPGE(IF_ICMPGE obj) {
        ifIcmpge++;
    }

    @Override
    public void visitIF_ICMPGT(IF_ICMPGT obj) {
        ifIcmpgt++;
    }

    @Override
    public void visitIF_ICMPLE(IF_ICMPLE obj) {
        ifIcmple++;
    }

    @Override
    public void visitIF_ICMPLT(IF_ICMPLT obj) {
        ifIcmplt++;
    }

    @Override
    public void visitIF_ICMPNE(IF_ICMPNE obj) {
        ifIcmpne++;
    }

    @Override
    public void visitIFEQ(IFEQ obj) {
        ifeq++;
    }

    @Override
    public void visitIFGE(IFGE obj) {
        ifge++;
    }

    @Override
    public void visitIFGT(IFGT obj) {
        ifgt++;
    }

    @Override
    public void visitIfInstruction(IfInstruction obj) {
        ifInstruction++;
    }

    @Override
    public void visitIFLE(IFLE obj) {
        ifle++;
    }

    @Override
    public void visitIFLT(IFLT obj) {
        iflt++;
    }

    @Override
    public void visitIFNE(IFNE obj) {
        ifne++;
    }

    @Override
    public void visitIFNONNULL(IFNONNULL obj) {
        ifnonnull++;
    }

    @Override
    public void visitIFNULL(IFNULL obj) {
        ifnull++;
    }

    @Override
    public void visitIINC(IINC obj) {
        iinc++;
    }

    @Override
    public void visitILOAD(ILOAD obj) {
        iload++;
    }

    @Override
    public void visitIMPDEP1(IMPDEP1 obj) {
        impdep1++;
    }

    @Override
    public void visitIMPDEP2(IMPDEP2 obj) {
        impdep2++;
    }

    @Override
    public void visitIMUL(IMUL obj) {
        imul++;
    }

    @Override
    public void visitINEG(INEG obj) {
        ineg++;
    }

    @Override
    public void visitINSTANCEOF(INSTANCEOF obj) {
        instanceOf++;
    }

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj) {
        invokedynamic++;
    }

    @Override
    public void visitInvokeInstruction(InvokeInstruction obj) {
        invokeInstruction++;
    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {
        invokeinterface++;
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        invokespecial++;
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        invokestatic++;
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        invokevirtual++;
    }

    @Override
    public void visitIOR(IOR obj) {
        ior++;
    }

    @Override
    public void visitIREM(IREM obj) {
        irem++;
    }

    @Override
    public void visitIRETURN(IRETURN obj) {
        ireturn++;
    }

    @Override
    public void visitISHL(ISHL obj) {
        ishl++;
    }

    @Override
    public void visitISHR(ISHR obj) {
        ishr++;
    }

    @Override
    public void visitISTORE(ISTORE obj) {
        istore++;
    }

    @Override
    public void visitISUB(ISUB obj) {
        isub++;
    }

    @Override
    public void visitIUSHR(IUSHR obj) {
        iushr++;
    }

    @Override
    public void visitIXOR(IXOR obj) {
        ixor++;
    }

    @Override
    public void visitJSR(JSR obj) {
        jsr++;
    }

    @Override
    public void visitJSR_W(JSR_W obj) {
        jsrW++;
    }

    @Override
    public void visitJsrInstruction(JsrInstruction obj) {
        jsrInstruction++;
    }

    @Override
    public void visitL2D(L2D obj) {
        l2D++;
    }

    @Override
    public void visitL2F(L2F obj) {
        l2F++;
    }

    @Override
    public void visitL2I(L2I obj) {
        l2I++;
    }

    @Override
    public void visitLADD(LADD obj) {
        ladd++;
    }

    @Override
    public void visitLALOAD(LALOAD obj) {
        laload++;
    }

    @Override
    public void visitLAND(LAND obj) {
        land++;
    }

    @Override
    public void visitLASTORE(LASTORE obj) {
        lastore++;
    }

    @Override
    public void visitLCMP(LCMP obj) {
        lcmp++;
    }

    @Override
    public void visitLCONST(LCONST obj) {
        lconst++;
    }

    @Override
    public void visitLDC(LDC obj) {
        ldc++;
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        ldc2W++;
    }

    @Override
    public void visitLDIV(LDIV obj) {
        ldiv++;
    }

    @Override
    public void visitLLOAD(LLOAD obj) {
        lload++;
    }

    @Override
    public void visitLMUL(LMUL obj) {
        lmul++;
    }

    @Override
    public void visitLNEG(LNEG obj) {
        lneg++;
    }

    @Override
    public void visitLoadClass(LoadClass obj) {
        loadClass++;
    }

    @Override
    public void visitLoadInstruction(LoadInstruction obj) {
        loadInstruction++;
    }

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {
        localVariableInstruction++;
    }

    @Override
    public void visitLOOKUPSWITCH(LOOKUPSWITCH obj) {
        lookupswitch++;
    }

    @Override
    public void visitLOR(LOR obj) {
        lor++;
    }

    @Override
    public void visitLREM(LREM obj) {
        lrem++;
    }

    @Override
    public void visitLRETURN(LRETURN obj) {
        lreturn++;
    }

    @Override
    public void visitLSHL(LSHL obj) {
        lshl++;
    }

    @Override
    public void visitLSHR(LSHR obj) {
        lshr++;
    }

    @Override
    public void visitLSTORE(LSTORE obj) {
        lstore++;
    }

    @Override
    public void visitLSUB(LSUB obj) {
        lsub++;
    }

    @Override
    public void visitLUSHR(LUSHR obj) {
        lushr++;
    }

    @Override
    public void visitLXOR(LXOR obj) {
        lxor++;
    }

    @Override
    public void visitMONITORENTER(MONITORENTER obj) {
        monitorenter++;
    }

    @Override
    public void visitMONITOREXIT(MONITOREXIT obj) {
        monitorexit++;
    }

    @Override
    public void visitMULTIANEWARRAY(MULTIANEWARRAY obj) {
        multianewarray++;
    }

    @Override
    public void visitNEW(NEW obj) {
        nEW++;
    }

    @Override
    public void visitNEWARRAY(NEWARRAY obj) {
        newarray++;
    }

    @Override
    public void visitNOP(NOP obj) {
        nop++;
    }

    @Override
    public void visitPOP(POP obj) {
        pop++;
    }

    @Override
    public void visitPOP2(POP2 obj) {
        pop2++;
    }

    @Override
    public void visitPopInstruction(PopInstruction obj) {
        popInstruction++;
    }

    @Override
    public void visitPushInstruction(PushInstruction obj) {
        pushInstruction++;
    }

    @Override
    public void visitPUTFIELD(PUTFIELD obj) {
        putfield++;
    }

    @Override
    public void visitPUTSTATIC(PUTSTATIC obj) {
        putstatic++;
    }

    @Override
    public void visitRET(RET obj) {
        ret++;
    }

    @Override
    public void visitRETURN(RETURN obj) {
        rETURN++;
    }

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {
        returnInstruction++;
    }

    @Override
    public void visitSALOAD(SALOAD obj) {
        saload++;
    }

    @Override
    public void visitSASTORE(SASTORE obj) {
        sastore++;
    }

    @Override
    public void visitSelect(Select obj) {
        select++;
    }

    @Override
    public void visitSIPUSH(SIPUSH obj) {
        sipush++;
    }

    @Override
    public void visitStackConsumer(StackConsumer obj) {
        stackConsumer++;
    }

    @Override
    public void visitStackInstruction(StackInstruction obj) {
        stackInstruction++;
    }

    @Override
    public void visitStackProducer(StackProducer obj) {
        stackProducer++;
    }

    @Override
    public void visitStoreInstruction(StoreInstruction obj) {
        storeInstruction++;
    }

    @Override
    public void visitSWAP(SWAP obj) {
        swap++;
    }

    @Override
    public void visitTABLESWITCH(TABLESWITCH obj) {
        tableswitch++;
    }

    @Override
    public void visitTypedInstruction(TypedInstruction obj) {
        typedInstruction++;
    }

    @Override
    public void visitUnconditionalBranch(UnconditionalBranch obj) {
        unconditionalBranch++;
    }

    @Override
    public void visitVariableLengthInstruction(VariableLengthInstruction obj) {
        variableLengthInstruction++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountVisitor that = (CountVisitor) o;
        return aaload == that.aaload &&
                aastore == that.aastore &&
                aconstNull == that.aconstNull &&
                allocationInstruction == that.allocationInstruction &&
                aload == that.aload &&
                anewarray == that.anewarray &&
                areturn == that.areturn &&
                arithmeticInstruction == that.arithmeticInstruction &&
                arrayInstruction == that.arrayInstruction &&
                arraylength == that.arraylength &&
                astore == that.astore &&
                athrow == that.athrow &&
                baload == that.baload &&
                bastore == that.bastore &&
                bipush == that.bipush &&
                branchInstruction == that.branchInstruction &&
                breakpoint == that.breakpoint &&
                caload == that.caload &&
                castore == that.castore &&
                checkcast == that.checkcast &&
                constantPushInstruction == that.constantPushInstruction &&
                conversionInstruction == that.conversionInstruction &&
                cpInstruction == that.cpInstruction &&
                d2F == that.d2F &&
                d2I == that.d2I &&
                d2L == that.d2L &&
                dadd == that.dadd &&
                daload == that.daload &&
                dastore == that.dastore &&
                dcmpg == that.dcmpg &&
                dcmpl == that.dcmpl &&
                dconst == that.dconst &&
                ddiv == that.ddiv &&
                dload == that.dload &&
                dmul == that.dmul &&
                dneg == that.dneg &&
                drem == that.drem &&
                dreturn == that.dreturn &&
                dstore == that.dstore &&
                dsub == that.dsub &&
                dup == that.dup &&
                dupX1 == that.dupX1 &&
                dupX2 == that.dupX2 &&
                dup2 == that.dup2 &&
                dup2X1 == that.dup2X1 &&
                dup2X2 == that.dup2X2 &&
                exceptionThrower == that.exceptionThrower &&
                f2D == that.f2D &&
                f2I == that.f2I &&
                f2L == that.f2L &&
                fadd == that.fadd &&
                faload == that.faload &&
                fastore == that.fastore &&
                fcmpg == that.fcmpg &&
                fcmpl == that.fcmpl &&
                fconst == that.fconst &&
                fdiv == that.fdiv &&
                fieldInstruction == that.fieldInstruction &&
                fieldOrMethod == that.fieldOrMethod &&
                fload == that.fload &&
                fmul == that.fmul &&
                fneg == that.fneg &&
                frem == that.frem &&
                freturn == that.freturn &&
                fstore == that.fstore &&
                fsub == that.fsub &&
                getfield == that.getfield &&
                getstatic == that.getstatic &&
                goTo == that.goTo &&
                gotoW == that.gotoW &&
                gotoInstruction == that.gotoInstruction &&
                i2B == that.i2B &&
                i2C == that.i2C &&
                i2D == that.i2D &&
                i2F == that.i2F &&
                i2L == that.i2L &&
                i2S == that.i2S &&
                iadd == that.iadd &&
                iaload == that.iaload &&
                iand == that.iand &&
                iastore == that.iastore &&
                iconst == that.iconst &&
                idiv == that.idiv &&
                ifAcmpeq == that.ifAcmpeq &&
                ifAcmpne == that.ifAcmpne &&
                ifIcmpeq == that.ifIcmpeq &&
                ifIcmpge == that.ifIcmpge &&
                ifIcmpgt == that.ifIcmpgt &&
                ifIcmple == that.ifIcmple &&
                ifIcmplt == that.ifIcmplt &&
                ifIcmpne == that.ifIcmpne &&
                ifeq == that.ifeq &&
                ifge == that.ifge &&
                ifgt == that.ifgt &&
                ifInstruction == that.ifInstruction &&
                ifle == that.ifle &&
                iflt == that.iflt &&
                ifne == that.ifne &&
                ifnonnull == that.ifnonnull &&
                ifnull == that.ifnull &&
                iinc == that.iinc &&
                iload == that.iload &&
                impdep1 == that.impdep1 &&
                impdep2 == that.impdep2 &&
                imul == that.imul &&
                ineg == that.ineg &&
                instanceOf == that.instanceOf &&
                invokedynamic == that.invokedynamic &&
                invokeInstruction == that.invokeInstruction &&
                invokeinterface == that.invokeinterface &&
                invokespecial == that.invokespecial &&
                invokestatic == that.invokestatic &&
                invokevirtual == that.invokevirtual &&
                ior == that.ior &&
                irem == that.irem &&
                ireturn == that.ireturn &&
                ishl == that.ishl &&
                ishr == that.ishr &&
                istore == that.istore &&
                isub == that.isub &&
                iushr == that.iushr &&
                ixor == that.ixor &&
                jsr == that.jsr &&
                jsrW == that.jsrW &&
                jsrInstruction == that.jsrInstruction &&
                l2D == that.l2D &&
                l2F == that.l2F &&
                l2I == that.l2I &&
                ladd == that.ladd &&
                laload == that.laload &&
                land == that.land &&
                lastore == that.lastore &&
                lcmp == that.lcmp &&
                lconst == that.lconst &&
                ldc == that.ldc &&
                ldc2W == that.ldc2W &&
                ldiv == that.ldiv &&
                lload == that.lload &&
                lmul == that.lmul &&
                lneg == that.lneg &&
                loadClass == that.loadClass &&
                loadInstruction == that.loadInstruction &&
                localVariableInstruction == that.localVariableInstruction &&
                lookupswitch == that.lookupswitch &&
                lor == that.lor &&
                lrem == that.lrem &&
                lreturn == that.lreturn &&
                lshl == that.lshl &&
                lshr == that.lshr &&
                lstore == that.lstore &&
                lsub == that.lsub &&
                lushr == that.lushr &&
                lxor == that.lxor &&
                monitorenter == that.monitorenter &&
                monitorexit == that.monitorexit &&
                multianewarray == that.multianewarray &&
                nEW == that.nEW &&
                newarray == that.newarray &&
                nop == that.nop &&
                pop == that.pop &&
                pop2 == that.pop2 &&
                popInstruction == that.popInstruction &&
                pushInstruction == that.pushInstruction &&
                putfield == that.putfield &&
                putstatic == that.putstatic &&
                ret == that.ret &&
                rETURN == that.rETURN &&
                returnInstruction == that.returnInstruction &&
                saload == that.saload &&
                sastore == that.sastore &&
                select == that.select &&
                sipush == that.sipush &&
                stackConsumer == that.stackConsumer &&
                stackInstruction == that.stackInstruction &&
                stackProducer == that.stackProducer &&
                storeInstruction == that.storeInstruction &&
                swap == that.swap &&
                tableswitch == that.tableswitch &&
                typedInstruction == that.typedInstruction &&
                unconditionalBranch == that.unconditionalBranch &&
                variableLengthInstruction == that.variableLengthInstruction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aaload,
                aastore,
                aconstNull,
                allocationInstruction,
                aload,
                anewarray,
                areturn,
                arithmeticInstruction,
                arrayInstruction,
                arraylength,
                astore,
                athrow,
                baload,
                bastore,
                bipush,
                branchInstruction,
                breakpoint,
                caload,
                castore,
                checkcast,
                constantPushInstruction,
                conversionInstruction,
                cpInstruction,
                d2F,
                d2I,
                d2L,
                dadd,
                daload,
                dastore,
                dcmpg,
                dcmpl,
                dconst,
                ddiv,
                dload,
                dmul,
                dneg,
                drem,
                dreturn,
                dstore,
                dsub,
                dup,
                dupX1,
                dupX2,
                dup2,
                dup2X1,
                dup2X2,
                exceptionThrower,
                f2D,
                f2I,
                f2L,
                fadd,
                faload,
                fastore,
                fcmpg,
                fcmpl,
                fconst,
                fdiv,
                fieldInstruction,
                fieldOrMethod,
                fload,
                fmul,
                fneg,
                frem,
                freturn,
                fstore,
                fsub,
                getfield,
                getstatic,
                goTo,
                gotoW,
                gotoInstruction,
                i2B,
                i2C,
                i2D,
                i2F,
                i2L,
                i2S,
                iadd,
                iaload,
                iand,
                iastore,
                iconst,
                idiv,
                ifAcmpeq,
                ifAcmpne,
                ifIcmpeq,
                ifIcmpge,
                ifIcmpgt,
                ifIcmple,
                ifIcmplt,
                ifIcmpne,
                ifeq,
                ifge,
                ifgt,
                ifInstruction,
                ifle,
                iflt,
                ifne,
                ifnonnull,
                ifnull,
                iinc,
                iload,
                impdep1,
                impdep2,
                imul,
                ineg,
                instanceOf,
                invokedynamic,
                invokeInstruction,
                invokeinterface,
                invokespecial,
                invokestatic,
                invokevirtual,
                ior,
                irem,
                ireturn,
                ishl,
                ishr,
                istore,
                isub,
                iushr,
                ixor,
                jsr,
                jsrW,
                jsrInstruction,
                l2D,
                l2F,
                l2I,
                ladd,
                laload,
                land,
                lastore,
                lcmp,
                lconst,
                ldc,
                ldc2W,
                ldiv,
                lload,
                lmul,
                lneg,
                loadClass,
                loadInstruction,
                localVariableInstruction,
                lookupswitch,
                lor,
                lrem,
                lreturn,
                lshl,
                lshr,
                lstore,
                lsub,
                lushr,
                lxor,
                monitorenter,
                monitorexit,
                multianewarray,
                nEW,
                newarray,
                nop,
                pop,
                pop2,
                popInstruction,
                pushInstruction,
                putfield,
                putstatic,
                ret,
                rETURN,
                returnInstruction,
                saload,
                sastore,
                select,
                sipush,
                stackConsumer,
                stackInstruction,
                stackProducer,
                storeInstruction,
                swap,
                tableswitch,
                typedInstruction,
                unconditionalBranch,
                variableLengthInstruction);
    }

}

