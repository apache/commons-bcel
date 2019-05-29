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
package org.apache.bcel.verifier.structurals;


import org.apache.bcel.Const;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantString;
// CHECKSTYLE:OFF (there are lots of references!)
import org.apache.bcel.generic.*;
//CHECKSTYLE:ON

/**
 * This Visitor class may be used for a type-based Java Virtual Machine
 * simulation.
 *
 * <p>It does not check for correct types on the OperandStack or in the
 * LocalVariables; nor does it check their sizes are sufficiently big.
 * Thus, to use this Visitor for bytecode verifying, you have to make sure
 * externally that the type constraints of the Java Virtual Machine instructions
 * are satisfied. An InstConstraintVisitor may be used for this.
 * Anyway, this Visitor does not mandate it. For example, when you
 * visitIADD(IADD o), then there are two stack slots popped and one
 * stack slot containing a Type.INT is pushed (where you could also
 * pop only one slot if you know there are two Type.INT on top of the
 * stack). Monitor-specific behavior is not simulated.</p>
 *
 * <b>Conventions:</b>
 *
 * <p>Type.VOID will never be pushed onto the stack. Type.DOUBLE and Type.LONG
 * that would normally take up two stack slots (like Double_HIGH and
 * Double_LOW) are represented by a simple single Type.DOUBLE or Type.LONG
 * object on the stack here.</p>
 *
 * <p>If a two-slot type is stored into a local variable, the next variable
 * is given the type Type.UNKNOWN.</p>
 *
 * @see #visitDSTORE(DSTORE o)
 * @see InstConstraintVisitor
 */
public class ExecutionVisitor extends EmptyVisitor{

    /**
     * The executionframe we're operating on.
     */
    private Frame frame = null;

    /**
     * The ConstantPoolGen we're working with.
     * @see #setConstantPoolGen(ConstantPoolGen)
     */
    private ConstantPoolGen cpg = null;

    /**
     * Constructor. Constructs a new instance of this class.
     */
    public ExecutionVisitor() {}

    /**
     * The OperandStack from the current Frame we're operating on.
     * @see #setFrame(Frame)
     */
    private OperandStack stack() {
        return frame.getStack();
    }

    /**
     * The LocalVariables from the current Frame we're operating on.
     * @see #setFrame(Frame)
     */
    private LocalVariables locals() {
        return frame.getLocals();
    }

    /**
     * Sets the ConstantPoolGen needed for symbolic execution.
     */
    public void setConstantPoolGen(final ConstantPoolGen cpg) { // TODO could be package-protected?
        this.cpg = cpg;
    }

    /**
     * The only method granting access to the single instance of
     * the ExecutionVisitor class. Before actively using this
     * instance, <B>SET THE ConstantPoolGen FIRST</B>.
     * @see #setConstantPoolGen(ConstantPoolGen)
     */
    public void setFrame(final Frame f) { // TODO could be package-protected?
        this.frame = f;
    }

    ///** Symbolically executes the corresponding Java Virtual Machine instruction. */
    //public void visitWIDE(WIDE o) {
    // The WIDE instruction is modelled as a flag
    // of the embedded instructions in BCEL.
    // Therefore BCEL checks for possible errors
    // when parsing in the .class file: We don't
    // have even the possibilty to care for WIDE
    // here.
    //}

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitAALOAD(final AALOAD o) {
        stack().pop();                                                        // pop the index int
//System.out.print(stack().peek());
        final Type t = stack().pop(); // Pop Array type
        if (t == Type.NULL) {
            stack().push(Type.NULL);
        }    // Do nothing stackwise --- a NullPointerException is thrown at Run-Time
        else{
            final ArrayType at = (ArrayType) t;
            stack().push(at.getElementType());
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitAASTORE(final AASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitACONST_NULL(final ACONST_NULL o) {
        stack().push(Type.NULL);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitALOAD(final ALOAD o) {
        stack().push(locals().get(o.getIndex()));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitANEWARRAY(final ANEWARRAY o) {
        stack().pop(); //count
        stack().push( new ArrayType(o.getType(cpg), 1) );
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitARETURN(final ARETURN o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitARRAYLENGTH(final ARRAYLENGTH o) {
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitASTORE(final ASTORE o) {
        locals().set(o.getIndex(), stack().pop());
        //System.err.println("TODO-DEBUG:    set LV '"+o.getIndex()+"' to '"+locals().get(o.getIndex())+"'.");
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitATHROW(final ATHROW o) {
        final Type t = stack().pop();
        stack().clear();
        if (t.equals(Type.NULL)) {
            stack().push(Type.getType("Ljava/lang/NullPointerException;"));
        } else {
            stack().push(t);
        }
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitBALOAD(final BALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitBASTORE(final BASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitBIPUSH(final BIPUSH o) {
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitCALOAD(final CALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitCASTORE(final CASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitCHECKCAST(final CHECKCAST o) {
        // It's possibly wrong to do so, but SUN's
        // ByteCode verifier seems to do (only) this, too.
        // TODO: One could use a sophisticated analysis here to check
        //       if a type cannot possibly be cated to another and by
        //       so doing predict the ClassCastException at run-time.
        stack().pop();
        stack().push(o.getType(cpg));
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitD2F(final D2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitD2I(final D2I o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitD2L(final D2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDADD(final DADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDALOAD(final DALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDASTORE(final DASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDCMPG(final DCMPG o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDCMPL(final DCMPL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDCONST(final DCONST o) {
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDDIV(final DDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDLOAD(final DLOAD o) {
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDMUL(final DMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDNEG(final DNEG o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDREM(final DREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDRETURN(final DRETURN o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDSTORE(final DSTORE o) {
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex()+1, Type.UNKNOWN);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDSUB(final DSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP(final DUP o) {
        final Type t = stack().pop();
        stack().push(t);
        stack().push(t);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP_X1(final DUP_X1 o) {
        final Type w1 = stack().pop();
        final Type w2 = stack().pop();
        stack().push(w1);
        stack().push(w2);
        stack().push(w1);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP_X2(final DUP_X2 o) {
        final Type w1 = stack().pop();
        final Type w2 = stack().pop();
        if (w2.getSize() == 2) {
            stack().push(w1);
            stack().push(w2);
            stack().push(w1);
        }
        else{
            final Type w3 = stack().pop();
            stack().push(w1);
            stack().push(w3);
            stack().push(w2);
            stack().push(w1);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP2(final DUP2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            stack().push(t);
            stack().push(t);
        }
        else{ // t.getSize() is 1
            final Type u = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(u);
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP2_X1(final DUP2_X1 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            final Type u = stack().pop();
            stack().push(t);
            stack().push(u);
            stack().push(t);
        }
        else{ //t.getSize() is1
            final Type u = stack().pop();
            final Type v = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(v);
            stack().push(u);
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitDUP2_X2(final DUP2_X2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            final Type u = stack().pop();
            if (u.getSize() == 2) {
                stack().push(t);
                stack().push(u);
                stack().push(t);
            }else{
                final Type v = stack().pop();
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }
        }
        else{ //t.getSize() is 1
            final Type u = stack().pop();
            final Type v = stack().pop();
            if (v.getSize() == 2) {
                stack().push(u);
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }else{
                final Type w = stack().pop();
                stack().push(u);
                stack().push(t);
                stack().push(w);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitF2D(final F2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitF2I(final F2I o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitF2L(final F2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFADD(final FADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFALOAD(final FALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFASTORE(final FASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFCMPG(final FCMPG o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFCMPL(final FCMPL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFCONST(final FCONST o) {
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFDIV(final FDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFLOAD(final FLOAD o) {
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFMUL(final FMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFNEG(final FNEG o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFREM(final FREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFRETURN(final FRETURN o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFSTORE(final FSTORE o) {
        locals().set(o.getIndex(), stack().pop());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitFSUB(final FSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitGETFIELD(final GETFIELD o) {
        stack().pop();
        Type t = o.getFieldType(cpg);
        if (    t.equals(Type.BOOLEAN)    ||
                    t.equals(Type.CHAR)            ||
                    t.equals(Type.BYTE)         ||
                    t.equals(Type.SHORT)        ) {
            t = Type.INT;
        }
        stack().push(t);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitGETSTATIC(final GETSTATIC o) {
        Type t = o.getFieldType(cpg);
        if (    t.equals(Type.BOOLEAN)    ||
                    t.equals(Type.CHAR)            ||
                    t.equals(Type.BYTE)         ||
                    t.equals(Type.SHORT)        ) {
            t = Type.INT;
        }
        stack().push(t);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitGOTO(final GOTO o) {
        // no stack changes.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitGOTO_W(final GOTO_W o) {
        // no stack changes.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2B(final I2B o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2C(final I2C o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2D(final I2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2F(final I2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2L(final I2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitI2S(final I2S o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIADD(final IADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIALOAD(final IALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIAND(final IAND o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIASTORE(final IASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitICONST(final ICONST o) {
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIDIV(final IDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ACMPEQ(final IF_ACMPEQ o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ACMPNE(final IF_ACMPNE o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPEQ(final IF_ICMPEQ o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPGE(final IF_ICMPGE o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPGT(final IF_ICMPGT o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPLE(final IF_ICMPLE o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPLT(final IF_ICMPLT o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIF_ICMPNE(final IF_ICMPNE o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFEQ(final IFEQ o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFGE(final IFGE o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFGT(final IFGT o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFLE(final IFLE o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFLT(final IFLT o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFNE(final IFNE o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFNONNULL(final IFNONNULL o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIFNULL(final IFNULL o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIINC(final IINC o) {
        // stack is not changed.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitILOAD(final ILOAD o) {
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIMUL(final IMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINEG(final INEG o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINSTANCEOF(final INSTANCEOF o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /**
     * Symbolically executes the corresponding Java Virtual Machine instruction.
     * @since 6.0
     */
    @Override
    public void visitINVOKEDYNAMIC(final INVOKEDYNAMIC o) {
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (    t.equals(Type.BOOLEAN)    ||
                        t.equals(Type.CHAR)            ||
                        t.equals(Type.BYTE)         ||
                        t.equals(Type.SHORT)        ) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINVOKEINTERFACE(final INVOKEINTERFACE o) {
        stack().pop();    //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (    t.equals(Type.BOOLEAN)    ||
                        t.equals(Type.CHAR)            ||
                        t.equals(Type.BYTE)         ||
                        t.equals(Type.SHORT)        ) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINVOKESPECIAL(final INVOKESPECIAL o) {
        if (o.getMethodName(cpg).equals(Const.CONSTRUCTOR_NAME)) {
            final UninitializedObjectType t = (UninitializedObjectType) stack().peek(o.getArgumentTypes(cpg).length);
            if (t == Frame.getThis()) {
                Frame.setThis(null);
            }
            stack().initializeObject(t);
            locals().initializeObject(t);
        }
        stack().pop();    //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (    t.equals(Type.BOOLEAN)    ||
                        t.equals(Type.CHAR)            ||
                        t.equals(Type.BYTE)         ||
                        t.equals(Type.SHORT)        ) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINVOKESTATIC(final INVOKESTATIC o) {
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (    t.equals(Type.BOOLEAN)    ||
                        t.equals(Type.CHAR)            ||
                        t.equals(Type.BYTE)         ||
                        t.equals(Type.SHORT)        ) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitINVOKEVIRTUAL(final INVOKEVIRTUAL o) {
        stack().pop(); //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (    t.equals(Type.BOOLEAN)    ||
                        t.equals(Type.CHAR)            ||
                        t.equals(Type.BYTE)         ||
                        t.equals(Type.SHORT)        ) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIOR(final IOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIREM(final IREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIRETURN(final IRETURN o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitISHL(final ISHL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitISHR(final ISHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitISTORE(final ISTORE o) {
        locals().set(o.getIndex(), stack().pop());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitISUB(final ISUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIUSHR(final IUSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitIXOR(final IXOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitJSR(final JSR o) {
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
//System.err.println("TODO-----------:"+o.physicalSuccessor());
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitJSR_W(final JSR_W o) {
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitL2D(final L2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitL2F(final L2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitL2I(final L2I o) {
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLADD(final LADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLALOAD(final LALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLAND(final LAND o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLASTORE(final LASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLCMP(final LCMP o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLCONST(final LCONST o) {
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLDC(final LDC o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger) {
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            stack().push(Type.CLASS);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    public void visitLDC_W(final LDC_W o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger) {
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            stack().push(Type.CLASS);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLDC2_W(final LDC2_W o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantLong) {
            stack().push(Type.LONG);
        }
        if (c instanceof ConstantDouble) {
            stack().push(Type.DOUBLE);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLDIV(final LDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLLOAD(final LLOAD o) {
        stack().push(locals().get(o.getIndex()));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLMUL(final LMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLNEG(final LNEG o) {
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLOOKUPSWITCH(final LOOKUPSWITCH o) {
        stack().pop(); //key
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLOR(final LOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLREM(final LREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLRETURN(final LRETURN o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLSHL(final LSHL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLSHR(final LSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLSTORE(final LSTORE o) {
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex()+1, Type.UNKNOWN);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLSUB(final LSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLUSHR(final LUSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitLXOR(final LXOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitMONITORENTER(final MONITORENTER o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitMONITOREXIT(final MONITOREXIT o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitMULTIANEWARRAY(final MULTIANEWARRAY o) {
        for (int i=0; i<o.getDimensions(); i++) {
            stack().pop();
        }
        stack().push(o.getType(cpg));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitNEW(final NEW o) {
        stack().push(new UninitializedObjectType((ObjectType) (o.getType(cpg))));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitNEWARRAY(final NEWARRAY o) {
        stack().pop();
        stack().push(o.getType());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitNOP(final NOP o) {
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitPOP(final POP o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitPOP2(final POP2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 1) {
            stack().pop();
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitPUTFIELD(final PUTFIELD o) {
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitPUTSTATIC(final PUTSTATIC o) {
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitRET(final RET o) {
        // do nothing, return address
        // is in in the local variables.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitRETURN(final RETURN o) {
        // do nothing.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitSALOAD(final SALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitSASTORE(final SASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitSIPUSH(final SIPUSH o) {
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitSWAP(final SWAP o) {
        final Type t = stack().pop();
        final Type u = stack().pop();
        stack().push(t);
        stack().push(u);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */
    @Override
    public void visitTABLESWITCH(final TABLESWITCH o) {
        stack().pop();
    }
}
