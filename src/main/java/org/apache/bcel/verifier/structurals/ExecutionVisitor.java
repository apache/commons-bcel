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


import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.generic.*;

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
 * stack). Monitor-specific behaviour is not simulated.</p>
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
 * @version $Id$
 * @author Enver Haase
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
    public ExecutionVisitor(){}

    /**
     * The OperandStack from the current Frame we're operating on.
     * @see #setFrame(Frame)
     */
    private OperandStack stack(){
        return frame.getStack();
    }

    /**
     * The LocalVariables from the current Frame we're operating on.
     * @see #setFrame(Frame)
     */
    private LocalVariables locals(){
        return frame.getLocals();
    }

    /**
     * Sets the ConstantPoolGen needed for symbolic execution.
     */
    public void setConstantPoolGen(ConstantPoolGen cpg){
        this.cpg = cpg;
    }

    /**
     * The only method granting access to the single instance of
     * the ExecutionVisitor class. Before actively using this
     * instance, <B>SET THE ConstantPoolGen FIRST</B>.
     * @see #setConstantPoolGen(ConstantPoolGen)
     */
    public void setFrame(Frame f){
        this.frame = f;
    }

    ///** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    //public void visitWIDE(WIDE o){
    // The WIDE instruction is modelled as a flag
    // of the embedded instructions in BCEL.
    // Therefore BCEL checks for possible errors
    // when parsing in the .class file: We don't
    // have even the possibilty to care for WIDE
    // here.
    //}

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitAALOAD(AALOAD o){
        stack().pop();                                                        // pop the index int
//System.out.print(stack().peek());
        Type t = stack().pop(); // Pop Array type
        if (t == Type.NULL){
            stack().push(Type.NULL);
        }    // Do nothing stackwise --- a NullPointerException is thrown at Run-Time
        else{
            ArrayType at = (ArrayType) t;    
            stack().push(at.getElementType());
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitAASTORE(AASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitACONST_NULL(ACONST_NULL o){
        stack().push(Type.NULL);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitALOAD(ALOAD o){
        stack().push(locals().get(o.getIndex()));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitANEWARRAY(ANEWARRAY o){
        stack().pop(); //count
        stack().push( new ArrayType(o.getType(cpg), 1) );
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitARETURN(ARETURN o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH o){
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitASTORE(ASTORE o){
        locals().set(o.getIndex(), stack().pop());
        //System.err.println("TODO-DEBUG:    set LV '"+o.getIndex()+"' to '"+locals().get(o.getIndex())+"'.");
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitATHROW(ATHROW o){
        Type t = stack().pop();
        stack().clear();
        if (t.equals(Type.NULL)) {
            stack().push(Type.getType("Ljava/lang/NullPointerException;"));
        } else {
            stack().push(t);
        }
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitBALOAD(BALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitBASTORE(BASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitBIPUSH(BIPUSH o){
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitCALOAD(CALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitCASTORE(CASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitCHECKCAST(CHECKCAST o){
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
    public void visitD2F(D2F o){
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitD2I(D2I o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitD2L(D2L o){
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDADD(DADD o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDALOAD(DALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDASTORE(DASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDCMPG(DCMPG o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDCMPL(DCMPL o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDCONST(DCONST o){
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDDIV(DDIV o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDLOAD(DLOAD o){
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDMUL(DMUL o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDNEG(DNEG o){
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDREM(DREM o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDRETURN(DRETURN o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDSTORE(DSTORE o){
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex()+1, Type.UNKNOWN);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDSUB(DSUB o){
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP(DUP o){
        Type t = stack().pop();
        stack().push(t);
        stack().push(t);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP_X1(DUP_X1 o){
        Type w1 = stack().pop();
        Type w2 = stack().pop();
        stack().push(w1);
        stack().push(w2);
        stack().push(w1);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP_X2(DUP_X2 o){
        Type w1 = stack().pop();
        Type w2 = stack().pop();
        if (w2.getSize() == 2){
            stack().push(w1);
            stack().push(w2);
            stack().push(w1);
        }
        else{
            Type w3 = stack().pop();
            stack().push(w1);
            stack().push(w3);
            stack().push(w2);
            stack().push(w1);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP2(DUP2 o){
        Type t = stack().pop();
        if (t.getSize() == 2){
            stack().push(t);
            stack().push(t);
        }
        else{ // t.getSize() is 1
            Type u = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(u);
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP2_X1(DUP2_X1 o){
        Type t = stack().pop();
        if (t.getSize() == 2){
            Type u = stack().pop();
            stack().push(t);
            stack().push(u);
            stack().push(t);
        }
        else{ //t.getSize() is1
            Type u = stack().pop();
            Type v = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(v);
            stack().push(u);
            stack().push(t);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitDUP2_X2(DUP2_X2 o){
        Type t = stack().pop();
        if (t.getSize() == 2){
            Type u = stack().pop();
            if (u.getSize() == 2){
                stack().push(t);
                stack().push(u);
                stack().push(t);
            }else{
                Type v = stack().pop();
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }
        }
        else{ //t.getSize() is 1
            Type u = stack().pop();
            Type v = stack().pop();
            if (v.getSize() == 2){
                stack().push(u);
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }else{
                Type w = stack().pop();
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
    public void visitF2D(F2D o){
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitF2I(F2I o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitF2L(F2L o){
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFADD(FADD o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFALOAD(FALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFASTORE(FASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFCMPG(FCMPG o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFCMPL(FCMPL o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFCONST(FCONST o){
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFDIV(FDIV o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFLOAD(FLOAD o){
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFMUL(FMUL o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFNEG(FNEG o){
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFREM(FREM o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFRETURN(FRETURN o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFSTORE(FSTORE o){
        locals().set(o.getIndex(), stack().pop());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitFSUB(FSUB o){
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitGETFIELD(GETFIELD o){
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
    public void visitGETSTATIC(GETSTATIC o){
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
    public void visitGOTO(GOTO o){
        // no stack changes.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitGOTO_W(GOTO_W o){
        // no stack changes.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2B(I2B o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2C(I2C o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2D(I2D o){
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2F(I2F o){
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2L(I2L o){
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitI2S(I2S o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIADD(IADD o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIALOAD(IALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIAND(IAND o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIASTORE(IASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitICONST(ICONST o){
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIDIV(IDIV o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ACMPEQ(IF_ACMPEQ o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ACMPNE(IF_ACMPNE o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPEQ(IF_ICMPEQ o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPGE(IF_ICMPGE o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPGT(IF_ICMPGT o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPLE(IF_ICMPLE o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPLT(IF_ICMPLT o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIF_ICMPNE(IF_ICMPNE o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFEQ(IFEQ o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFGE(IFGE o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFGT(IFGT o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFLE(IFLE o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFLT(IFLT o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFNE(IFNE o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFNONNULL(IFNONNULL o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIFNULL(IFNULL o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIINC(IINC o){
        // stack is not changed.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitILOAD(ILOAD o){
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIMUL(IMUL o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitINEG(INEG o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitINSTANCEOF(INSTANCEOF o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE o){
        stack().pop();    //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++){
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID){
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
    public void visitINVOKESPECIAL(INVOKESPECIAL o){
        if (o.getMethodName(cpg).equals(Constants.CONSTRUCTOR_NAME)){
            UninitializedObjectType t = (UninitializedObjectType) stack().peek(o.getArgumentTypes(cpg).length);
            if (t == Frame._this){    
                Frame._this = null;
            }
            stack().initializeObject(t);
            locals().initializeObject(t);
        }
        stack().pop();    //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++){
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID){
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
    public void visitINVOKESTATIC(INVOKESTATIC o){
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++){
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID){
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
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL o){
        stack().pop(); //objectref
        for (int i=0; i<o.getArgumentTypes(cpg).length; i++){
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID){
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
    public void visitIOR(IOR o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIREM(IREM o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIRETURN(IRETURN o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitISHL(ISHL o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitISHR(ISHR o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitISTORE(ISTORE o){
        locals().set(o.getIndex(), stack().pop());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitISUB(ISUB o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIUSHR(IUSHR o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitIXOR(IXOR o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitJSR(JSR o){
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
//System.err.println("TODO-----------:"+o.physicalSuccessor());
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitJSR_W(JSR_W o){
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
    }

    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitL2D(L2D o){
        stack().pop();
        stack().push(Type.DOUBLE);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitL2F(L2F o){
        stack().pop();
        stack().push(Type.FLOAT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitL2I(L2I o){
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLADD(LADD o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLALOAD(LALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLAND(LAND o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLASTORE(LASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLCMP(LCMP o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLCONST(LCONST o){
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLDC(LDC o){
        Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger){
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat){
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString){
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass){
            stack().push(Type.CLASS);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    public void visitLDC_W(LDC_W o){
        Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger){
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat){
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString){
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass){
            stack().push(Type.CLASS);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLDC2_W(LDC2_W o){
        Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantLong){
            stack().push(Type.LONG);
        }
        if (c instanceof ConstantDouble){
            stack().push(Type.DOUBLE);
        }
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLDIV(LDIV o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLLOAD(LLOAD o){
        stack().push(locals().get(o.getIndex()));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLMUL(LMUL o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLNEG(LNEG o){
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLOOKUPSWITCH(LOOKUPSWITCH o){
        stack().pop(); //key
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLOR(LOR o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLREM(LREM o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLRETURN(LRETURN o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLSHL(LSHL o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLSHR(LSHR o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLSTORE(LSTORE o){
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex()+1, Type.UNKNOWN);        
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLSUB(LSUB o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLUSHR(LUSHR o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitLXOR(LXOR o){
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitMONITORENTER(MONITORENTER o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitMONITOREXIT(MONITOREXIT o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitMULTIANEWARRAY(MULTIANEWARRAY o){
        for (int i=0; i<o.getDimensions(); i++){
            stack().pop();
        }
        stack().push(o.getType(cpg));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitNEW(NEW o){
        stack().push(new UninitializedObjectType((ObjectType) (o.getType(cpg))));
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitNEWARRAY(NEWARRAY o){
        stack().pop();
        stack().push(o.getType());
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitNOP(NOP o){
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitPOP(POP o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitPOP2(POP2 o){
        Type t = stack().pop();
        if (t.getSize() == 1){
            stack().pop();
        }        
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitPUTFIELD(PUTFIELD o){
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitPUTSTATIC(PUTSTATIC o){
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitRET(RET o){
        // do nothing, return address
        // is in in the local variables.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitRETURN(RETURN o){
        // do nothing.
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitSALOAD(SALOAD o){
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitSASTORE(SASTORE o){
        stack().pop();
        stack().pop();
        stack().pop();
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitSIPUSH(SIPUSH o){
        stack().push(Type.INT);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitSWAP(SWAP o){
        Type t = stack().pop();
        Type u = stack().pop();
        stack().push(t);
        stack().push(u);
    }
    /** Symbolically executes the corresponding Java Virtual Machine instruction. */ 
    @Override
    public void visitTABLESWITCH(TABLESWITCH o){
        stack().pop();
    }
}
