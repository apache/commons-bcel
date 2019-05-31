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
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
//CHECKSTYLE:OFF (there are lots of references!)
import org.apache.bcel.generic.*;
//CHECKSTYLE:ON
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;


/**
 * A Visitor class testing for valid preconditions of JVM instructions.
 * The instance of this class will throw a StructuralCodeConstraintException
 * instance if an instruction is visitXXX()ed which has preconditions that are
 * not satisfied.
 * TODO: Currently, the JVM's behavior concerning monitors (MONITORENTER,
 * MONITOREXIT) is not modeled in JustIce.
 *
 * @see StructuralCodeConstraintException
 */
public class InstConstraintVisitor extends EmptyVisitor{

    private static final ObjectType GENERIC_ARRAY = ObjectType.getInstance(GenericArray.class.getName());

    /**
     * The constructor. Constructs a new instance of this class.
     */
    public InstConstraintVisitor() {}

    /**
     * The Execution Frame we're working on.
     *
     * @see #setFrame(Frame f)
     * @see #locals()
     * @see #stack()
     */
    private Frame frame = null;

    /**
     * The ConstantPoolGen we're working on.
     *
     * @see #setConstantPoolGen(ConstantPoolGen cpg)
     */
    private ConstantPoolGen cpg = null;

    /**
     * The MethodGen we're working on.
     *
     * @see #setMethodGen(MethodGen mg)
     */
    private MethodGen mg = null;

    /**
     * The OperandStack we're working on.
     *
     * @see #setFrame(Frame f)
     */
    private OperandStack stack() {
        return frame.getStack();
    }

    /**
     * The LocalVariables we're working on.
     *
     * @see #setFrame(Frame f)
     */
    private LocalVariables locals() {
        return frame.getLocals();
    }

    /**
   * This method is called by the visitXXX() to notify the acceptor of this InstConstraintVisitor
   * that a constraint violation has occured. This is done by throwing an instance of a
   * StructuralCodeConstraintException.
   * @throws StructuralCodeConstraintException always.
   */
    private void constraintViolated(final Instruction violator, final String description) {
        final String fq_classname = violator.getClass().getName();
        throw new StructuralCodeConstraintException(
            "Instruction "+ fq_classname.substring(fq_classname.lastIndexOf('.')+1) +" constraint violated: " + description);
    }

    /**
     * This returns the single instance of the InstConstraintVisitor class.
     * To operate correctly, other values must have been set before actually
     * using the instance.
     * Use this method for performance reasons.
     *
     * @see #setConstantPoolGen(ConstantPoolGen cpg)
     * @see #setMethodGen(MethodGen mg)
     */
    public void setFrame(final Frame f) { // TODO could be package-protected?
        this.frame = f;
        //if (singleInstance.mg == null || singleInstance.cpg == null)
        // throw new AssertionViolatedException("Forgot to set important values first.");
    }

    /**
     * Sets the ConstantPoolGen instance needed for constraint
     * checking prior to execution.
     */
    public void setConstantPoolGen(final ConstantPoolGen cpg) { // TODO could be package-protected?
        this.cpg = cpg;
    }

    /**
     * Sets the MethodGen instance needed for constraint
     * checking prior to execution.
     */
    public void setMethodGen(final MethodGen mg) {
        this.mg = mg;
    }

    /**
     * Assures index is of type INT.
     * @throws StructuralCodeConstraintException if the above constraint is not satisfied.
     */
    private void indexOfInt(final Instruction o, final Type index) {
        if (! index.equals(Type.INT)) {
            constraintViolated(o, "The 'index' is not of type int but of type "+index+".");
        }
    }

    /**
     * Assures the ReferenceType r is initialized (or Type.NULL).
     * Formally, this means (!(r instanceof UninitializedObjectType)), because
     * there are no uninitialized array types.
     * @throws StructuralCodeConstraintException if the above constraint is not satisfied.
     */
    private void referenceTypeIsInitialized(final Instruction o, final ReferenceType r) {
        if (r instanceof UninitializedObjectType) {
            constraintViolated(o, "Working on an uninitialized object '"+r+"'.");
        }
    }

    /** Assures value is of type INT. */
    private void valueOfInt(final Instruction o, final Type value) {
        if (! value.equals(Type.INT)) {
            constraintViolated(o, "The 'value' is not of type int but of type "+value+".");
        }
    }

    /**
     * Assures arrayref is of ArrayType or NULL;
     * returns true if and only if arrayref is non-NULL.
     * @throws StructuralCodeConstraintException if the above constraint is violated.
      */
    private boolean arrayrefOfArrayType(final Instruction o, final Type arrayref) {
        if (! ((arrayref instanceof ArrayType) || arrayref.equals(Type.NULL)) ) {
            constraintViolated(o, "The 'arrayref' does not refer to an array but is of type "+arrayref+".");
        }
        return arrayref instanceof ArrayType;
    }

    /***************************************************************/
    /* MISC                                                        */
    /***************************************************************/
    /**
     * Ensures the general preconditions of an instruction that accesses the stack.
     * This method is here because BCEL has no such superinterface for the stack
     * accessing instructions; and there are funny unexpected exceptions in the
     * semantices of the superinterfaces and superclasses provided.
     * E.g. SWAP is a StackConsumer, but DUP_X1 is not a StackProducer.
     * Therefore, this method is called by all StackProducer, StackConsumer,
     * and StackInstruction instances via their visitXXX() method.
     * Unfortunately, as the superclasses and superinterfaces overlap, some instructions
     * cause this method to be called two or three times. [TODO: Fix this.]
     *
     * @see #visitStackConsumer(StackConsumer o)
     * @see #visitStackProducer(StackProducer o)
     * @see #visitStackInstruction(StackInstruction o)
     */
    private void _visitStackAccessor(final Instruction o) {
        final int consume = o.consumeStack(cpg); // Stack values are always consumed first; then produced.
        if (consume > stack().slotsUsed()) {
            constraintViolated(o,
                "Cannot consume "+consume+" stack slots: only "+stack().slotsUsed()+" slot(s) left on stack!\nStack:\n"+stack());
        }

        final int produce = o.produceStack(cpg) - o.consumeStack(cpg); // Stack values are always consumed first; then produced.
        if ( produce + stack().slotsUsed() > stack().maxStack() ) {
            constraintViolated(o, "Cannot produce "+produce+" stack slots: only "+(stack().maxStack()-stack().slotsUsed())+
                    " free stack slot(s) left.\nStack:\n"+stack());
        }
    }

    /***************************************************************/
    /* "generic"visitXXXX methods where XXXX is an interface       */
    /* therefore, we don't know the order of visiting; but we know */
    /* these methods are called before the visitYYYY methods below */
    /***************************************************************/

    /**
     * Assures the generic preconditions of a LoadClass instance.
     * The referenced class is loaded and pass2-verified.
     */
    @Override
    public void visitLoadClass(final LoadClass o) {
        final ObjectType t = o.getLoadClassType(cpg);
        if (t != null) {// null means "no class is loaded"
            final Verifier v = VerifierFactory.getVerifier(t.getClassName());
            final VerificationResult vr = v.doPass2();
            if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                constraintViolated((Instruction) o, "Class '"+o.getLoadClassType(cpg).getClassName()+
                    "' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
            }
        }
    }

    /**
     * Ensures the general preconditions of a StackConsumer instance.
     */
    @Override
    public void visitStackConsumer(final StackConsumer o) {
        _visitStackAccessor((Instruction) o);
    }

    /**
     * Ensures the general preconditions of a StackProducer instance.
     */
    @Override
    public void visitStackProducer(final StackProducer o) {
        _visitStackAccessor((Instruction) o);
    }


    /***************************************************************/
    /* "generic" visitYYYY methods where YYYY is a superclass.     */
    /* therefore, we know the order of visiting; we know           */
    /* these methods are called after the visitXXXX methods above. */
    /***************************************************************/
    /**
     * Ensures the general preconditions of a CPInstruction instance.
     */
    @Override
    public void visitCPInstruction(final CPInstruction o) {
        final int idx = o.getIndex();
        if ((idx < 0) || (idx >= cpg.getSize())) {
            throw new AssertionViolatedException(
                "Huh?! Constant pool index of instruction '"+o+"' illegal? Pass 3a should have checked this!");
        }
    }

    /**
     * Ensures the general preconditions of a FieldInstruction instance.
     */
     @Override
    public void visitFieldInstruction(final FieldInstruction o) {
         // visitLoadClass(o) has been called before: Every FieldOrMethod
         // implements LoadClass.
         // visitCPInstruction(o) has been called before.
        // A FieldInstruction may be: GETFIELD, GETSTATIC, PUTFIELD, PUTSTATIC
            final Constant c = cpg.getConstant(o.getIndex());
            if (!(c instanceof ConstantFieldref)) {
                constraintViolated(o,
                    "Index '"+o.getIndex()+"' should refer to a CONSTANT_Fieldref_info structure, but refers to '"+c+"'.");
            }
            // the o.getClassType(cpg) type has passed pass 2; see visitLoadClass(o).
            final Type t = o.getType(cpg);
            if (t instanceof ObjectType) {
                final String name = ((ObjectType)t).getClassName();
                final Verifier v = VerifierFactory.getVerifier( name );
                final VerificationResult vr = v.doPass2();
                if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                    constraintViolated(o, "Class '"+name+"' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
                }
            }
     }

    /**
     * Ensures the general preconditions of an InvokeInstruction instance.
     */
     @Override
    public void visitInvokeInstruction(final InvokeInstruction o) {
         // visitLoadClass(o) has been called before: Every FieldOrMethod
         // implements LoadClass.
         // visitCPInstruction(o) has been called before.
        //TODO
     }

    /**
     * Ensures the general preconditions of a StackInstruction instance.
     */
    @Override
    public void visitStackInstruction(final StackInstruction o) {
        _visitStackAccessor(o);
    }

    /**
     * Assures the generic preconditions of a LocalVariableInstruction instance.
     * That is, the index of the local variable must be valid.
     */
    @Override
    public void visitLocalVariableInstruction(final LocalVariableInstruction o) {
        if (locals().maxLocals() <= (o.getType(cpg).getSize()==1? o.getIndex() : o.getIndex()+1) ) {
            constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
        }
    }

    /**
     * Assures the generic preconditions of a LoadInstruction instance.
     */
    @Override
    public void visitLoadInstruction(final LoadInstruction o) {
        //visitLocalVariableInstruction(o) is called before, because it is more generic.

        // LOAD instructions must not read Type.UNKNOWN
        if (locals().get(o.getIndex()) == Type.UNKNOWN) {
            constraintViolated(o, "Read-Access on local variable "+o.getIndex()+" with unknown content.");
        }

        // LOAD instructions, two-slot-values at index N must have Type.UNKNOWN
        // as a symbol for the higher halve at index N+1
        // [suppose some instruction put an int at N+1--- our double at N is defective]
        if (o.getType(cpg).getSize() == 2) {
            if (locals().get(o.getIndex()+1) != Type.UNKNOWN) {
                constraintViolated(o,
                    "Reading a two-locals value from local variables "+o.getIndex()+
                    " and "+(o.getIndex()+1)+" where the latter one is destroyed.");
            }
        }

        // LOAD instructions must read the correct type.
        if (!(o instanceof ALOAD)) {
            if (locals().get(o.getIndex()) != o.getType(cpg) ) {
                constraintViolated(o,"Local Variable type and LOADing Instruction type mismatch: Local Variable: '"+
                    locals().get(o.getIndex())+"'; Instruction type: '"+o.getType(cpg)+"'.");
            }
        }
        else{ // we deal with an ALOAD
            if (!(locals().get(o.getIndex()) instanceof ReferenceType)) {
                constraintViolated(o, "Local Variable type and LOADing Instruction type mismatch: Local Variable: '"+
                    locals().get(o.getIndex())+"'; Instruction expects a ReferenceType.");
            }
            // ALOAD __IS ALLOWED__ to put uninitialized objects onto the stack!
            //referenceTypeIsInitialized(o, (ReferenceType) (locals().get(o.getIndex())));
        }

        // LOAD instructions must have enough free stack slots.
        if ((stack().maxStack() - stack().slotsUsed()) < o.getType(cpg).getSize()) {
            constraintViolated(o, "Not enough free stack slots to load a '"+o.getType(cpg)+"' onto the OperandStack.");
        }
    }

    /**
     * Assures the generic preconditions of a StoreInstruction instance.
     */
    @Override
    public void visitStoreInstruction(final StoreInstruction o) {
        //visitLocalVariableInstruction(o) is called before, because it is more generic.

        if (stack().isEmpty()) { // Don't bother about 1 or 2 stack slots used. This check is implicitly done below while type checking.
            constraintViolated(o, "Cannot STORE: Stack to read from is empty.");
        }

        if ( !(o instanceof ASTORE) ) {
            if (! (stack().peek() == o.getType(cpg)) ) {// the other xSTORE types are singletons in BCEL.
                constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '"+stack().peek()+
                    "'; Instruction type: '"+o.getType(cpg)+"'.");
            }
        }
        else{ // we deal with ASTORE
            final Type stacktop = stack().peek();
            if ( (!(stacktop instanceof ReferenceType)) && (!(stacktop instanceof ReturnaddressType)) ) {
                constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '"+stack().peek()+
                    "'; Instruction expects a ReferenceType or a ReturnadressType.");
            }
            //if (stacktop instanceof ReferenceType) {
            //    referenceTypeIsInitialized(o, (ReferenceType) stacktop);
            //}
        }
    }

    /**
     * Assures the generic preconditions of a ReturnInstruction instance.
     */
    @Override
    public void visitReturnInstruction(final ReturnInstruction o) {
        Type method_type = mg.getType();
        if (method_type == Type.BOOLEAN ||
            method_type == Type.BYTE ||
            method_type == Type.SHORT ||
            method_type == Type.CHAR) {
                method_type = Type.INT;
            }

        if (o instanceof RETURN) {
            if (method_type != Type.VOID) {
                constraintViolated(o, "RETURN instruction in non-void method.");
            }
            else{
                return;
            }
        }
        if (o instanceof ARETURN) {
            if (method_type == Type.VOID) {
                constraintViolated(o, "ARETURN instruction in void method.");
            }
            if (stack().peek() == Type.NULL) {
                return;
            }
            if (! (stack().peek() instanceof ReferenceType)) {
                constraintViolated(o, "Reference type expected on top of stack, but is: '"+stack().peek()+"'.");
            }
            referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()));
            //ReferenceType objectref = (ReferenceType) (stack().peek());
            // TODO: This can only be checked if using Staerk-et-al's "set of object types" instead of a
            // "wider cast object type" created during verification.
            //if (! (objectref.isAssignmentCompatibleWith(mg.getType())) ) {
            //    constraintViolated(o, "Type on stack top which should be returned is a '"+stack().peek()+
            //    "' which is not assignment compatible with the return type of this method, '"+mg.getType()+"'.");
            //}
        }
        else{
            if (! ( method_type.equals( stack().peek() ))) {
                constraintViolated(o, "Current method has return type of '"+mg.getType()+"' expecting a '"+method_type+
                    "' on top of the stack. But stack top is a '"+stack().peek()+"'.");
            }
        }
    }

    /***************************************************************/
    /* "special"visitXXXX methods for one type of instruction each */
    /***************************************************************/

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitAALOAD(final AALOAD o) {
        final Type arrayref = stack().peek(1);
        final Type index    = stack().peek(0);

        indexOfInt(o, index);
        if (arrayrefOfArrayType(o, arrayref)) {
            if (! (((ArrayType) arrayref).getElementType() instanceof ReferenceType)) {
                constraintViolated(o,
                    "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of "+
                    ((ArrayType) arrayref).getElementType()+".");
            }
            //referenceTypeIsInitialized(o, (ReferenceType) (((ArrayType) arrayref).getElementType()));
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitAASTORE(final AASTORE o) {
        final Type arrayref = stack().peek(2);
        final Type index    = stack().peek(1);
        final Type value    = stack().peek(0);

        indexOfInt(o, index);
        if (!(value instanceof ReferenceType)) {
            constraintViolated(o, "The 'value' is not of a ReferenceType but of type "+value+".");
        }else{
            //referenceTypeIsInitialized(o, (ReferenceType) value);
        }
        // Don't bother further with "referenceTypeIsInitialized()", there are no arrays
        // of an uninitialized object type.
        if (arrayrefOfArrayType(o, arrayref)) {
            if (! (((ArrayType) arrayref).getElementType() instanceof ReferenceType)) {
                constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of "+
                    ((ArrayType) arrayref).getElementType()+".");
            }
            // No check for array element assignment compatibility. This is done at runtime.
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitACONST_NULL(final ACONST_NULL o) {
        // Nothing needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitALOAD(final ALOAD o) {
        //visitLoadInstruction(LoadInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitANEWARRAY(final ANEWARRAY o) {
        if (!stack().peek().equals(Type.INT)) {
            constraintViolated(o, "The 'count' at the stack top is not of type '"+Type.INT+"' but of type '"+stack().peek()+"'.");
        // The runtime constant pool item at that index must be a symbolic reference to a class,
        // array, or interface type. See Pass 3a.
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitARETURN(final ARETURN o) {
        if (! (stack().peek() instanceof ReferenceType) ) {
            constraintViolated(o, "The 'objectref' at the stack top is not of a ReferenceType but of type '"+stack().peek()+"'.");
        }
        final ReferenceType objectref = (ReferenceType) (stack().peek());
        referenceTypeIsInitialized(o, objectref);

        // The check below should already done via visitReturnInstruction(ReturnInstruction), see there.
        // It cannot be done using Staerk-et-al's "set of object types" instead of a
        // "wider cast object type", anyway.
        //if (! objectref.isAssignmentCompatibleWith(mg.getReturnType() )) {
        //    constraintViolated(o, "The 'objectref' type "+objectref+
        // " at the stack top is not assignment compatible with the return type '"+mg.getReturnType()+"' of the method.");
        //}
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitARRAYLENGTH(final ARRAYLENGTH o) {
        final Type arrayref = stack().peek(0);
        arrayrefOfArrayType(o, arrayref);
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitASTORE(final ASTORE o) {
        if (! ( (stack().peek() instanceof ReferenceType) || (stack().peek() instanceof ReturnaddressType) ) ) {
            constraintViolated(o, "The 'objectref' is not of a ReferenceType or of ReturnaddressType but of "+stack().peek()+".");
        }
        //if (stack().peek() instanceof ReferenceType) {
        //    referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
        //}
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitATHROW(final ATHROW o) {
        try {
        // It's stated that 'objectref' must be of a ReferenceType --- but since Throwable is
        // not derived from an ArrayType, it follows that 'objectref' must be of an ObjectType or Type.NULL.
        if (! ((stack().peek() instanceof ObjectType) || (stack().peek().equals(Type.NULL))) ) {
            constraintViolated(o, "The 'objectref' is not of an (initialized) ObjectType but of type "+stack().peek()+".");
        }

        // NULL is a subclass of every class, so to speak.
        if (stack().peek().equals(Type.NULL)) {
            return;
        }

        final ObjectType exc = (ObjectType) (stack().peek());
        final ObjectType throwable = (ObjectType) (Type.getType("Ljava/lang/Throwable;"));
        if ( (! (exc.subclassOf(throwable)) ) && (! (exc.equals(throwable))) ) {
            constraintViolated(o,
                "The 'objectref' is not of class Throwable or of a subclass of Throwable, but of '"+stack().peek()+"'.");
        }
        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitBALOAD(final BALOAD o) {
        final Type arrayref = stack().peek(1);
        final Type index    = stack().peek(0);
        indexOfInt(o, index);
        if (arrayrefOfArrayType(o, arrayref)) {
            if (! ( (((ArrayType) arrayref).getElementType().equals(Type.BOOLEAN)) ||
                    (((ArrayType) arrayref).getElementType().equals(Type.BYTE)) ) ) {
                constraintViolated(o,
                    "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '"+
                    ((ArrayType) arrayref).getElementType()+"'.");
            }
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitBASTORE(final BASTORE o) {
        final Type arrayref = stack().peek(2);
        final Type index    = stack().peek(1);
        final Type value    = stack().peek(0);

        indexOfInt(o, index);
        valueOfInt(o, value);
        if (arrayrefOfArrayType(o, arrayref)) {
            if (! ( (((ArrayType) arrayref).getElementType().equals(Type.BOOLEAN)) ||
                    (((ArrayType) arrayref).getElementType().equals(Type.BYTE)) ) ) {
                constraintViolated(o,
                    "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '"+
                    ((ArrayType) arrayref).getElementType()+"'.");
            }
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitBIPUSH(final BIPUSH o) {
        // Nothing to do...
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitBREAKPOINT(final BREAKPOINT o) {
        throw new AssertionViolatedException(
            "In this JustIce verification pass there should not occur an illegal instruction such as BREAKPOINT.");
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitCALOAD(final CALOAD o) {
        final Type arrayref = stack().peek(1);
        final Type index = stack().peek(0);

        indexOfInt(o, index);
        arrayrefOfArrayType(o, arrayref);
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitCASTORE(final CASTORE o) {
        final Type arrayref = stack().peek(2);
        final Type index = stack().peek(1);
        final Type value = stack().peek(0);

        indexOfInt(o, index);
        valueOfInt(o, value);
        if (arrayrefOfArrayType(o, arrayref)) {
            if (! ((ArrayType) arrayref).getElementType().equals(Type.CHAR) ) {
                constraintViolated(o, "The 'arrayref' does not refer to an array with elements of type char but to an array of type "+
                    ((ArrayType) arrayref).getElementType()+".");
            }
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitCHECKCAST(final CHECKCAST o) {
        // The objectref must be of type reference.
        final Type objectref = stack().peek(0);
        if (!(objectref instanceof ReferenceType)) {
            constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type "+objectref+".");
        }
        //else{
        //    referenceTypeIsInitialized(o, (ReferenceType) objectref);
        //}
        // The unsigned indexbyte1 and indexbyte2 are used to construct an index into the runtime constant pool of the
        // current class (�3.6), where the value of the index is (indexbyte1 << 8) | indexbyte2. The runtime constant
        // pool item at the index must be a symbolic reference to a class, array, or interface type.
        final Constant c = cpg.getConstant(o.getIndex());
        if (! (c instanceof ConstantClass)) {
            constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '"+c+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitD2F(final D2F o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitD2I(final D2I o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitD2L(final D2L o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDADD(final DADD o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDALOAD(final DALOAD o) {
        indexOfInt(o, stack().peek());
        if (stack().peek(1) == Type.NULL) {
            return;
        }
        if (! (stack().peek(1) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-top must be of type double[] but is '"+stack().peek(1)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(1))).getBasicType();
        if (t != Type.DOUBLE) {
            constraintViolated(o, "Stack next-to-top must be of type double[] but is '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDASTORE(final DASTORE o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        indexOfInt(o, stack().peek(1));
        if (stack().peek(2) == Type.NULL) {
            return;
        }
        if (! (stack().peek(2) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '"+stack().peek(2)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(2))).getBasicType();
        if (t != Type.DOUBLE) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '"+stack().peek(2)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDCMPG(final DCMPG o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDCMPL(final DCMPL o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDCONST(final DCONST o) {
        // There's nothing to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDDIV(final DDIV o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDLOAD(final DLOAD o) {
        //visitLoadInstruction(LoadInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDMUL(final DMUL o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDNEG(final DNEG o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDREM(final DREM o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDRETURN(final DRETURN o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDSTORE(final DSTORE o) {
        //visitStoreInstruction(StoreInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDSUB(final DSUB o) {
        if (stack().peek() != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack top is not of type 'double', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.DOUBLE) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP(final DUP o) {
        if (stack().peek().getSize() != 1) {
            constraintViolated(o, "Won't DUP type on stack top '"+stack().peek()+
                "' because it must occupy exactly one slot, not '"+stack().peek().getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP_X1(final DUP_X1 o) {
        if (stack().peek().getSize() != 1) {
            constraintViolated(o,
                "Type on stack top '"+stack().peek()+"' should occupy exactly one slot, not '"+stack().peek().getSize()+"'.");
        }
        if (stack().peek(1).getSize() != 1) {
            constraintViolated(o,
                "Type on stack next-to-top '"+stack().peek(1)+
                "' should occupy exactly one slot, not '"+stack().peek(1).getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP_X2(final DUP_X2 o) {
        if (stack().peek().getSize() != 1) {
            constraintViolated(o,
                "Stack top type must be of size 1, but is '"+stack().peek()+"' of size '"+stack().peek().getSize()+"'.");
        }
        if (stack().peek(1).getSize() == 2) {
            return; // Form 2, okay.
        }
        //stack().peek(1).getSize == 1.
        if (stack().peek(2).getSize() != 1) {
            constraintViolated(o,
                "If stack top's size is 1 and stack next-to-top's size is 1,"+
                " stack next-to-next-to-top's size must also be 1, but is: '"+
                stack().peek(2)+"' of size '"+stack().peek(2).getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP2(final DUP2 o) {
        if (stack().peek().getSize() == 2) {
            return; // Form 2, okay.
        }
        //stack().peek().getSize() == 1.
        if (stack().peek(1).getSize() != 1) {
            constraintViolated(o,
                "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '"+stack().peek(1)+
                "' of size '"+stack().peek(1).getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP2_X1(final DUP2_X1 o) {
        if (stack().peek().getSize() == 2) {
            if (stack().peek(1).getSize() != 1) {
                constraintViolated(o, "If stack top's size is 2, then stack next-to-top's size must be 1. But it is '"+
                    stack().peek(1)+"' of size '"+stack().peek(1).getSize()+"'.");
            }
            else{
                return; // Form 2
            }
        }
        else{ // stack top is of size 1
            if ( stack().peek(1).getSize() != 1 ) {
                constraintViolated(o, "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '"+
                    stack().peek(1)+"' of size '"+stack().peek(1).getSize()+"'.");
            }
            if ( stack().peek(2).getSize() != 1 ) {
                constraintViolated(o, "If stack top's size is 1, then stack next-to-next-to-top's size must also be 1. But it is '"+
                    stack().peek(2)+"' of size '"+stack().peek(2).getSize()+"'.");
            }
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitDUP2_X2(final DUP2_X2 o) {

        if (stack().peek(0).getSize() == 2) {
             if (stack().peek(1).getSize() == 2) {
                return; // Form 4
            }
            // stack top size is 2, next-to-top's size is 1
            if ( stack().peek(2).getSize() != 1 ) {
                constraintViolated(o, "If stack top's size is 2 and stack-next-to-top's size is 1,"+
                    " then stack next-to-next-to-top's size must also be 1. But it is '"+stack().peek(2)+
                    "' of size '"+stack().peek(2).getSize()+"'.");
            }
            else{
                return; // Form 2
            }
        }
        else{// stack top is of size 1
            if (stack().peek(1).getSize() == 1) {
                if ( stack().peek(2).getSize() == 2 ) {
                    return; // Form 3
                }
                if ( stack().peek(3).getSize() == 1) {
                    return; // Form 1
                }
            }
        }
        constraintViolated(o, "The operand sizes on the stack do not match any of the four forms of usage of this instruction.");
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitF2D(final F2D o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitF2I(final F2I o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitF2L(final F2L o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFADD(final FADD o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFALOAD(final FALOAD o) {
        indexOfInt(o, stack().peek());
        if (stack().peek(1) == Type.NULL) {
            return;
        }
        if (! (stack().peek(1) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-top must be of type float[] but is '"+stack().peek(1)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(1))).getBasicType();
        if (t != Type.FLOAT) {
            constraintViolated(o, "Stack next-to-top must be of type float[] but is '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFASTORE(final FASTORE o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        indexOfInt(o, stack().peek(1));
        if (stack().peek(2) == Type.NULL) {
            return;
        }
        if (! (stack().peek(2) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '"+stack().peek(2)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(2))).getBasicType();
        if (t != Type.FLOAT) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '"+stack().peek(2)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFCMPG(final FCMPG o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFCMPL(final FCMPL o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFCONST(final FCONST o) {
        // nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFDIV(final FDIV o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFLOAD(final FLOAD o) {
        //visitLoadInstruction(LoadInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFMUL(final FMUL o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFNEG(final FNEG o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFREM(final FREM o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFRETURN(final FRETURN o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFSTORE(final FSTORE o) {
        //visitStoreInstruction(StoreInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitFSUB(final FSUB o) {
        if (stack().peek() != Type.FLOAT) {
            constraintViolated(o, "The value at the stack top is not of type 'float', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.FLOAT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '"+stack().peek(1)+"'.");
        }
    }

    private ObjectType getObjectType(final FieldInstruction o) {
        final ReferenceType rt = o.getReferenceType(cpg);
        if(rt instanceof ObjectType) {
            return (ObjectType)rt;
        }
        constraintViolated(o, "expecting ObjectType but got "+rt);
        return null;
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitGETFIELD(final GETFIELD o) {
        try {
        final Type objectref = stack().peek();
        if (! ( (objectref instanceof ObjectType) || (objectref == Type.NULL) ) ) {
            constraintViolated(o, "Stack top should be an object reference that's not an array reference, but is '"+objectref+"'.");
        }

        final String field_name = o.getFieldName(cpg);

        final JavaClass jc = Repository.lookupClass(getObjectType(o).getClassName());
        Field[] fields = jc.getFields();
        Field f = null;
        for (final Field field : fields) {
            if (field.getName().equals(field_name)) {
                  final Type f_type = Type.getType(field.getSignature());
                  final Type o_type = o.getType(cpg);
                    /* TODO: Check if assignment compatibility is sufficient.
                   * What does Sun do?
                   */
                  if (f_type.equals(o_type)) {
                        f = field;
                        break;
                    }
            }
        }

        if (f == null) {
            final JavaClass[] superclasses = jc.getSuperClasses();
            outer:
            for (final JavaClass superclass : superclasses) {
                fields = superclass.getFields();
                for (final Field field : fields) {
                    if (field.getName().equals(field_name)) {
                        final Type f_type = Type.getType(field.getSignature());
                        final Type o_type = o.getType(cpg);
                        if (f_type.equals(o_type)) {
                            f = field;
                            if ((f.getAccessFlags() & (Const.ACC_PUBLIC | Const.ACC_PROTECTED)) == 0) {
                                f = null;
                            }
                            break outer;
                        }
                    }
                }
            }
            if (f == null) {
                throw new AssertionViolatedException("Field '" + field_name + "' not found in " + jc.getClassName());
            }
        }

        if (f.isProtected()) {
            final ObjectType classtype = getObjectType(o);
            final ObjectType curr = ObjectType.getInstance(mg.getClassName());

            if (    classtype.equals(curr) ||
                        curr.subclassOf(classtype)    ) {
                final Type t = stack().peek();
                if (t == Type.NULL) {
                    return;
                }
                if (! (t instanceof ObjectType) ) {
                    constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '"+t+"'.");
                }
                final ObjectType objreftype = (ObjectType) t;
                if (! ( objreftype.equals(curr) ||
                            objreftype.subclassOf(curr) ) ) {
                    //TODO: One day move to Staerk-et-al's "Set of object types" instead of "wider" object types
                    //      created during the verification.
                    //      "Wider" object types don't allow us to check for things like that below.
                    //constraintViolated(o, "The referenced field has the ACC_PROTECTED modifier, "+
                    // "and it's a member of the current class or a superclass of the current class."+
                    // " However, the referenced object type '"+stack().peek()+
                    // "' is not the current class or a subclass of the current class.");
                }
            }
        }

        // TODO: Could go into Pass 3a.
        if (f.isStatic()) {
            constraintViolated(o, "Referenced field '"+f+"' is static which it shouldn't be.");
        }

        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitGETSTATIC(final GETSTATIC o) {
        // Field must be static: see Pass 3a.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitGOTO(final GOTO o) {
        // nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitGOTO_W(final GOTO_W o) {
        // nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2B(final I2B o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2C(final I2C o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2D(final I2D o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2F(final I2F o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2L(final I2L o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitI2S(final I2S o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIADD(final IADD o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIALOAD(final IALOAD o) {
        indexOfInt(o, stack().peek());
        if (stack().peek(1) == Type.NULL) {
            return;
        }
        if (! (stack().peek(1) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-top must be of type int[] but is '"+stack().peek(1)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(1))).getBasicType();
        if (t != Type.INT) {
            constraintViolated(o, "Stack next-to-top must be of type int[] but is '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIAND(final IAND o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIASTORE(final IASTORE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        indexOfInt(o, stack().peek(1));
        if (stack().peek(2) == Type.NULL) {
            return;
        }
        if (! (stack().peek(2) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '"+stack().peek(2)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(2))).getBasicType();
        if (t != Type.INT) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '"+stack().peek(2)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitICONST(final ICONST o) {
        //nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIDIV(final IDIV o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ACMPEQ(final IF_ACMPEQ o) {
        if (!(stack().peek() instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '"+stack().peek()+"'.");
        }
        //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );

        if (!(stack().peek(1) instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '"+stack().peek(1)+"'.");
        }
        //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek(1)) );

    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ACMPNE(final IF_ACMPNE o) {
        if (!(stack().peek() instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '"+stack().peek()+"'.");
            //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
        }
        if (!(stack().peek(1) instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '"+stack().peek(1)+"'.");
            //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek(1)) );
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPEQ(final IF_ICMPEQ o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPGE(final IF_ICMPGE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPGT(final IF_ICMPGT o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPLE(final IF_ICMPLE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPLT(final IF_ICMPLT o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIF_ICMPNE(final IF_ICMPNE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFEQ(final IFEQ o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFGE(final IFGE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFGT(final IFGT o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFLE(final IFLE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFLT(final IFLT o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFNE(final IFNE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFNONNULL(final IFNONNULL o) {
        if (!(stack().peek() instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '"+stack().peek()+"'.");
        }
        referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIFNULL(final IFNULL o) {
        if (!(stack().peek() instanceof ReferenceType)) {
            constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '"+stack().peek()+"'.");
        }
        referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIINC(final IINC o) {
        // Mhhh. In BCEL, at this time "IINC" is not a LocalVariableInstruction.
        if (locals().maxLocals() <= (o.getType(cpg).getSize()==1? o.getIndex() : o.getIndex()+1) ) {
            constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
        }

        indexOfInt(o, locals().get(o.getIndex()));
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitILOAD(final ILOAD o) {
        // All done by visitLocalVariableInstruction(), visitLoadInstruction()
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIMPDEP1(final IMPDEP1 o) {
        throw new AssertionViolatedException(
            "In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP1.");
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIMPDEP2(final IMPDEP2 o) {
        throw new AssertionViolatedException(
            "In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP2.");
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIMUL(final IMUL o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINEG(final INEG o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINSTANCEOF(final INSTANCEOF o) {
        // The objectref must be of type reference.
        final Type objectref = stack().peek(0);
        if (!(objectref instanceof ReferenceType)) {
            constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type "+objectref+".");
        }
        //else{
        //    referenceTypeIsInitialized(o, (ReferenceType) objectref);
        //}
        // The unsigned indexbyte1 and indexbyte2 are used to construct an index into the runtime constant pool of the
        // current class (�3.6), where the value of the index is (indexbyte1 << 8) | indexbyte2. The runtime constant
        // pool item at the index must be a symbolic reference to a class, array, or interface type.
        final Constant c = cpg.getConstant(o.getIndex());
        if (! (c instanceof ConstantClass)) {
            constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '"+c+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     * @since 6.0
     */
    @Override
    public void visitINVOKEDYNAMIC(final INVOKEDYNAMIC o) {
        throw new RuntimeException("INVOKEDYNAMIC instruction is not supported at this time");
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINVOKEINTERFACE(final INVOKEINTERFACE o) {
        // Method is not native, otherwise pass 3 would not happen.

        final int count = o.getCount();
        if (count == 0) {
            constraintViolated(o, "The 'count' argument must not be 0.");
        }
        // It is a ConstantInterfaceMethodref, Pass 3a made it sure.
        // TODO: Do we want to do anything with it?
        //ConstantInterfaceMethodref cimr = (ConstantInterfaceMethodref) (cpg.getConstant(o.getIndex()));

        // the o.getClassType(cpg) type has passed pass 2; see visitLoadClass(o).

        final Type t = o.getType(cpg);
        if (t instanceof ObjectType) {
            final String name = ((ObjectType)t).getClassName();
            final Verifier v = VerifierFactory.getVerifier( name );
            final VerificationResult vr = v.doPass2();
            if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                constraintViolated(o, "Class '"+name+"' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
            }
        }


        final Type[] argtypes = o.getArgumentTypes(cpg);
        final int nargs = argtypes.length;

        for (int i=nargs-1; i>=0; i--) {
            final Type fromStack = stack().peek( (nargs-1) - i );    // 0 to nargs-1
            Type fromDesc = argtypes[i];
            if (fromDesc == Type.BOOLEAN ||
                    fromDesc == Type.BYTE ||
                    fromDesc == Type.CHAR ||
                    fromDesc == Type.SHORT) {
                fromDesc = Type.INT;
            }
            if (! fromStack.equals(fromDesc)) {
                if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
                    final ReferenceType rFromStack = (ReferenceType) fromStack;
                    //ReferenceType rFromDesc = (ReferenceType) fromDesc;
                    // TODO: This can only be checked when using Staerk-et-al's "set of object types"
                    // instead of a "wider cast object type" created during verification.
                    //if ( ! rFromStack.isAssignmentCompatibleWith(rFromDesc) ) {
                    //    constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+
                    //    "' on the stack (which is not assignment compatible).");
                    //}
                    referenceTypeIsInitialized(o, rFromStack);
                }
                else{
                    constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+"' on the stack.");
                }
            }
        }

        Type objref = stack().peek(nargs);
        if (objref == Type.NULL) {
            return;
        }
        if (! (objref instanceof ReferenceType) ) {
            constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '"+objref+"'.");
        }
        referenceTypeIsInitialized(o, (ReferenceType) objref);
        if (!(objref instanceof ObjectType)) {
            if (!(objref instanceof ArrayType)) { // could be a ReturnaddressType
                constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '"+objref+"'.");
            }
            else{
                objref = GENERIC_ARRAY;
            }
        }

        // String objref_classname = ((ObjectType) objref).getClassName();
        // String theInterface = o.getClassName(cpg);
        // TODO: This can only be checked if we're using Staerk-et-al's "set of object types"
        //       instead of "wider cast object types" generated during verification.
        //if ( ! Repository.implementationOf(objref_classname, theInterface) ) {
        //    constraintViolated(o, "The 'objref' item '"+objref+"' does not implement '"+theInterface+"' as expected.");
        //}

        int counted_count = 1; // 1 for the objectref
        for (int i=0; i<nargs; i++) {
            counted_count += argtypes[i].getSize();
        }
        if (count != counted_count) {
            constraintViolated(o, "The 'count' argument should probably read '"+counted_count+"' but is '"+count+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINVOKESPECIAL(final INVOKESPECIAL o) {
        try {
        // Don't init an object twice.
        if ( (o.getMethodName(cpg).equals(Const.CONSTRUCTOR_NAME)) &&
             (!(stack().peek(o.getArgumentTypes(cpg).length) instanceof UninitializedObjectType)) ) {
            constraintViolated(o, "Possibly initializing object twice."+
                 " A valid instruction sequence must not have an uninitialized object on the operand stack or in a local variable"+
                 " during a backwards branch, or in a local variable in code protected by an exception handler."+
                 " Please see The Java Virtual Machine Specification, Second Edition, 4.9.4 (pages 147 and 148) for details.");
        }

        // the o.getClassType(cpg) type has passed pass 2; see visitLoadClass(o).

        final Type t = o.getType(cpg);
        if (t instanceof ObjectType) {
            final String name = ((ObjectType)t).getClassName();
            final Verifier v = VerifierFactory.getVerifier( name );
            final VerificationResult vr = v.doPass2();
            if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                constraintViolated(o, "Class '"+name+"' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
            }
        }


        final Type[] argtypes = o.getArgumentTypes(cpg);
        final int nargs = argtypes.length;

        for (int i=nargs-1; i>=0; i--) {
            final Type fromStack = stack().peek( (nargs-1) - i );    // 0 to nargs-1
            Type fromDesc = argtypes[i];
            if (fromDesc == Type.BOOLEAN ||
                    fromDesc == Type.BYTE ||
                    fromDesc == Type.CHAR ||
                    fromDesc == Type.SHORT) {
                fromDesc = Type.INT;
            }
            if (! fromStack.equals(fromDesc)) {
                if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
                    final ReferenceType rFromStack = (ReferenceType) fromStack;
                    final ReferenceType rFromDesc = (ReferenceType) fromDesc;
                    // TODO: This can only be checked using Staerk-et-al's "set of object types", not
                    // using a "wider cast object type".
                    if ( ! rFromStack.isAssignmentCompatibleWith(rFromDesc) ) {
                        constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+
                            "' on the stack (which is not assignment compatible).");
                    }
                    referenceTypeIsInitialized(o, rFromStack);
                }
                else{
                    constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+"' on the stack.");
                }
            }
        }

        Type objref = stack().peek(nargs);
        if (objref == Type.NULL) {
            return;
        }
        if (! (objref instanceof ReferenceType) ) {
            constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '"+objref+"'.");
        }
        String objref_classname = null;
        if ( !(o.getMethodName(cpg).equals(Const.CONSTRUCTOR_NAME))) {
            referenceTypeIsInitialized(o, (ReferenceType) objref);
            if (!(objref instanceof ObjectType)) {
                if (!(objref instanceof ArrayType)) { // could be a ReturnaddressType
                    constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '"+objref+"'.");
                }
                else{
                    objref = GENERIC_ARRAY;
                }
            }

            objref_classname = ((ObjectType) objref).getClassName();
        }
        else{
            if (!(objref instanceof UninitializedObjectType)) {
                constraintViolated(o, "Expecting an UninitializedObjectType as 'objectref' on the stack, not a '"+objref+
                    "'. Otherwise, you couldn't invoke a method since an array has no methods (not to speak of a return address).");
            }
            objref_classname = ((UninitializedObjectType) objref).getInitialized().getClassName();
        }


        final String theClass = o.getClassName(cpg);
        if ( ! Repository.instanceOf(objref_classname, theClass) ) {
            constraintViolated(o, "The 'objref' item '"+objref+"' does not implement '"+theClass+"' as expected.");
        }

        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINVOKESTATIC(final INVOKESTATIC o) {
        try {
        // Method is not native, otherwise pass 3 would not happen.

        final Type t = o.getType(cpg);
        if (t instanceof ObjectType) {
            final String name = ((ObjectType)t).getClassName();
            final Verifier v = VerifierFactory.getVerifier( name );
            final VerificationResult vr = v.doPass2();
            if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                constraintViolated(o, "Class '"+name+"' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
            }
        }

        final Type[] argtypes = o.getArgumentTypes(cpg);
        final int nargs = argtypes.length;

        for (int i=nargs-1; i>=0; i--) {
            final Type fromStack = stack().peek( (nargs-1) - i );    // 0 to nargs-1
            Type fromDesc = argtypes[i];
            if (fromDesc == Type.BOOLEAN ||
                    fromDesc == Type.BYTE ||
                    fromDesc == Type.CHAR ||
                    fromDesc == Type.SHORT) {
                fromDesc = Type.INT;
            }
            if (! fromStack.equals(fromDesc)) {
                if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
                    final ReferenceType rFromStack = (ReferenceType) fromStack;
                    final ReferenceType rFromDesc = (ReferenceType) fromDesc;
                    // TODO: This check can possibly only be done using Staerk-et-al's "set of object types"
                    // instead of a "wider cast object type" created during verification.
                    if ( ! rFromStack.isAssignmentCompatibleWith(rFromDesc) ) {
                        constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+
                            "' on the stack (which is not assignment compatible).");
                    }
                    referenceTypeIsInitialized(o, rFromStack);
                }
                else{
                    constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+"' on the stack.");
                }
            }
        }
        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitINVOKEVIRTUAL(final INVOKEVIRTUAL o) {
        try {
        // the o.getClassType(cpg) type has passed pass 2; see visitLoadClass(o).

        final Type t = o.getType(cpg);
        if (t instanceof ObjectType) {
            final String name = ((ObjectType)t).getClassName();
            final Verifier v = VerifierFactory.getVerifier( name );
            final VerificationResult vr = v.doPass2();
            if (vr.getStatus() != VerificationResult.VERIFIED_OK) {
                constraintViolated(o, "Class '"+name+"' is referenced, but cannot be loaded and resolved: '"+vr+"'.");
            }
        }


        final Type[] argtypes = o.getArgumentTypes(cpg);
        final int nargs = argtypes.length;

        for (int i=nargs-1; i>=0; i--) {
            final Type fromStack = stack().peek( (nargs-1) - i );    // 0 to nargs-1
            Type fromDesc = argtypes[i];
            if (fromDesc == Type.BOOLEAN ||
                    fromDesc == Type.BYTE ||
                    fromDesc == Type.CHAR ||
                    fromDesc == Type.SHORT) {
                fromDesc = Type.INT;
            }
            if (! fromStack.equals(fromDesc)) {
                if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
                    final ReferenceType rFromStack = (ReferenceType) fromStack;
                    final ReferenceType rFromDesc = (ReferenceType) fromDesc;
                    // TODO: This can possibly only be checked when using Staerk-et-al's "set of object types" instead
                    // of a single "wider cast object type" created during verification.
                    if ( ! rFromStack.isAssignmentCompatibleWith(rFromDesc) ) {
                        constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+
                            "' on the stack (which is not assignment compatible).");
                    }
                    referenceTypeIsInitialized(o, rFromStack);
                }
                else{
                    constraintViolated(o, "Expecting a '"+fromDesc+"' but found a '"+fromStack+"' on the stack.");
                }
            }
        }

        Type objref = stack().peek(nargs);
        if (objref == Type.NULL) {
            return;
        }
        if (! (objref instanceof ReferenceType) ) {
            constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '"+objref+"'.");
        }
        referenceTypeIsInitialized(o, (ReferenceType) objref);
        if (!(objref instanceof ObjectType)) {
            if (!(objref instanceof ArrayType)) { // could be a ReturnaddressType
                constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '"+objref+"'.");
            }
            else{
                objref = GENERIC_ARRAY;
            }
        }

        final String objref_classname = ((ObjectType) objref).getClassName();

        final String theClass = o.getClassName(cpg);

        if ( ! Repository.instanceOf(objref_classname, theClass) ) {
            constraintViolated(o, "The 'objref' item '"+objref+"' does not implement '"+theClass+"' as expected.");
        }
        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIOR(final IOR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIREM(final IREM o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIRETURN(final IRETURN o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitISHL(final ISHL o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitISHR(final ISHR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitISTORE(final ISTORE o) {
        //visitStoreInstruction(StoreInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitISUB(final ISUB o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIUSHR(final IUSHR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitIXOR(final IXOR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.INT) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitJSR(final JSR o) {
        // nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitJSR_W(final JSR_W o) {
        // nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitL2D(final L2D o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitL2F(final L2F o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitL2I(final L2I o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLADD(final LADD o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLALOAD(final LALOAD o) {
        indexOfInt(o, stack().peek());
        if (stack().peek(1) == Type.NULL) {
            return;
        }
        if (! (stack().peek(1) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-top must be of type long[] but is '"+stack().peek(1)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(1))).getBasicType();
        if (t != Type.LONG) {
            constraintViolated(o, "Stack next-to-top must be of type long[] but is '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLAND(final LAND o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLASTORE(final LASTORE o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        indexOfInt(o, stack().peek(1));
        if (stack().peek(2) == Type.NULL) {
            return;
        }
        if (! (stack().peek(2) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '"+stack().peek(2)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(2))).getBasicType();
        if (t != Type.LONG) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '"+stack().peek(2)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLCMP(final LCMP o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLCONST(final LCONST o) {
        // Nothing to do here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLDC(final LDC o) {
        // visitCPInstruction is called first.

        final Constant c = cpg.getConstant(o.getIndex());
        if     (!    (    ( c instanceof ConstantInteger) ||
                    ( c instanceof ConstantFloat    )    ||
                    ( c instanceof ConstantString    )    ||
                    ( c instanceof ConstantClass    ) )    ) {
            constraintViolated(o,
                "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float, a CONSTANT_String or a CONSTANT_Class, but is '"+
                 c + "'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    public void visitLDC_W(final LDC_W o) {
        // visitCPInstruction is called first.

        final Constant c = cpg.getConstant(o.getIndex());
        if     (!    (    ( c instanceof ConstantInteger) ||
                    ( c instanceof ConstantFloat    )    ||
                    ( c instanceof ConstantString    )    ||
                    ( c instanceof ConstantClass    ) )    ) {
            constraintViolated(o,
                "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float, a CONSTANT_String or a CONSTANT_Class, but is '"+
                 c + "'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLDC2_W(final LDC2_W o) {
        // visitCPInstruction is called first.

        final Constant c = cpg.getConstant(o.getIndex());
        if     (!    (    ( c instanceof ConstantLong) ||
                            ( c instanceof ConstantDouble )    )    ) {
            constraintViolated(o,
                    "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '"+c+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLDIV(final LDIV o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLLOAD(final LLOAD o) {
        //visitLoadInstruction(LoadInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLMUL(final LMUL o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLNEG(final LNEG o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLOOKUPSWITCH(final LOOKUPSWITCH o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        // See also pass 3a.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLOR(final LOR o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLREM(final LREM o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLRETURN(final LRETURN o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLSHL(final LSHL o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLSHR(final LSHR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLSTORE(final LSTORE o) {
        //visitStoreInstruction(StoreInstruction) is called before.

        // Nothing else needs to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLSUB(final LSUB o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLUSHR(final LUSHR o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitLXOR(final LXOR o) {
        if (stack().peek() != Type.LONG) {
            constraintViolated(o, "The value at the stack top is not of type 'long', but of type '"+stack().peek()+"'.");
        }
        if (stack().peek(1) != Type.LONG) {
            constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitMONITORENTER(final MONITORENTER o) {
        if (! ((stack().peek()) instanceof ReferenceType)) {
            constraintViolated(o, "The stack top should be of a ReferenceType, but is '"+stack().peek()+"'.");
        }
        //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitMONITOREXIT(final MONITOREXIT o) {
        if (! ((stack().peek()) instanceof ReferenceType)) {
            constraintViolated(o, "The stack top should be of a ReferenceType, but is '"+stack().peek()+"'.");
        }
        //referenceTypeIsInitialized(o, (ReferenceType) (stack().peek()) );
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitMULTIANEWARRAY(final MULTIANEWARRAY o) {
        final int dimensions = o.getDimensions();
        // Dimensions argument is okay: see Pass 3a.
        for (int i=0; i<dimensions; i++) {
            if (stack().peek(i) != Type.INT) {
                constraintViolated(o, "The '"+dimensions+"' upper stack types should be 'int' but aren't.");
            }
        }
        // The runtime constant pool item at that index must be a symbolic reference to a class,
        // array, or interface type. See Pass 3a.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitNEW(final NEW o) {
        //visitCPInstruction(CPInstruction) has been called before.
        //visitLoadClass(LoadClass) has been called before.

        final Type t = o.getType(cpg);
        if (! (t instanceof ReferenceType)) {
            throw new AssertionViolatedException("NEW.getType() returning a non-reference type?!");
        }
        if (! (t instanceof ObjectType)) {
            constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '"+t+"'.");
        }
        final ObjectType obj = (ObjectType) t;

        //e.g.: Don't instantiate interfaces
        try {
            if (! obj.referencesClassExact()) {
                constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '"+obj+"'.");
            }
        } catch (final ClassNotFoundException e) {
            constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '"+obj+"'." + " which threw " + e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitNEWARRAY(final NEWARRAY o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitNOP(final NOP o) {
        // nothing is to be done here.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitPOP(final POP o) {
        if (stack().peek().getSize() != 1) {
            constraintViolated(o,
                "Stack top size should be 1 but stack top is '"+stack().peek()+"' of size '"+stack().peek().getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitPOP2(final POP2 o) {
        if (stack().peek().getSize() != 2) {
            constraintViolated(o,
                "Stack top size should be 2 but stack top is '"+stack().peek()+"' of size '"+stack().peek().getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitPUTFIELD(final PUTFIELD o) {
        try {

        final Type objectref = stack().peek(1);
        if (! ( (objectref instanceof ObjectType) || (objectref == Type.NULL) ) ) {
            constraintViolated(o,
                "Stack next-to-top should be an object reference that's not an array reference, but is '"+objectref+"'.");
        }

        final String field_name = o.getFieldName(cpg);

        final JavaClass jc = Repository.lookupClass(getObjectType(o).getClassName());
        final Field[] fields = jc.getFields();
        Field f = null;
        for (final Field field : fields) {
            if (field.getName().equals(field_name)) {
                  final Type f_type = Type.getType(field.getSignature());
                  final Type o_type = o.getType(cpg);
                    /* TODO: Check if assignment compatibility is sufficient.
                   * What does Sun do?
                   */
                  if (f_type.equals(o_type)) {
                        f = field;
                        break;
                    }
            }
        }
        if (f == null) {
            throw new AssertionViolatedException("Field '" + field_name + "' not found in " + jc.getClassName());
        }

        final Type value = stack().peek();
        final Type t = Type.getType(f.getSignature());
        Type shouldbe = t;
        if (shouldbe == Type.BOOLEAN ||
                shouldbe == Type.BYTE ||
                shouldbe == Type.CHAR ||
                shouldbe == Type.SHORT) {
            shouldbe = Type.INT;
        }
        if (t instanceof ReferenceType) {
            ReferenceType rvalue = null;
            if (value instanceof ReferenceType) {
                rvalue = (ReferenceType) value;
                referenceTypeIsInitialized(o, rvalue);
            }
            else{
                constraintViolated(o, "The stack top type '"+value+"' is not of a reference type as expected.");
            }
            // TODO: This can possibly only be checked using Staerk-et-al's "set-of-object types", not
            // using "wider cast object types" created during verification.
            // Comment it out if you encounter problems. See also the analogon at visitPUTSTATIC.
            if (!(rvalue.isAssignmentCompatibleWith(shouldbe))) {
                constraintViolated(o, "The stack top type '"+value+"' is not assignment compatible with '"+shouldbe+"'.");
            }
        }
        else{
            if (shouldbe != value) {
                constraintViolated(o, "The stack top type '"+value+"' is not of type '"+shouldbe+"' as expected.");
            }
        }

        if (f.isProtected()) {
            final ObjectType classtype = getObjectType(o);
            final ObjectType curr = ObjectType.getInstance(mg.getClassName());

            if (    classtype.equals(curr) ||
                        curr.subclassOf(classtype)    ) {
                final Type tp = stack().peek(1);
                if (tp == Type.NULL) {
                    return;
                }
                if (! (tp instanceof ObjectType) ) {
                    constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '"+tp+"'.");
                }
                final ObjectType objreftype = (ObjectType) tp;
                if (! ( objreftype.equals(curr) ||
                            objreftype.subclassOf(curr) ) ) {
                    constraintViolated(o,
                        "The referenced field has the ACC_PROTECTED modifier, and it's a member of the current class or"+
                        " a superclass of the current class. However, the referenced object type '"+stack().peek()+
                        "' is not the current class or a subclass of the current class.");
                }
            }
        }

        // TODO: Could go into Pass 3a.
        if (f.isStatic()) {
            constraintViolated(o, "Referenced field '"+f+"' is static which it shouldn't be.");
        }

        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitPUTSTATIC(final PUTSTATIC o) {
        try {
        final String field_name = o.getFieldName(cpg);
        final JavaClass jc = Repository.lookupClass(getObjectType(o).getClassName());
        final Field[] fields = jc.getFields();
        Field f = null;
        for (final Field field : fields) {
            if (field.getName().equals(field_name)) {
                    final Type f_type = Type.getType(field.getSignature());
                  final Type o_type = o.getType(cpg);
                    /* TODO: Check if assignment compatibility is sufficient.
                   * What does Sun do?
                   */
                  if (f_type.equals(o_type)) {
                        f = field;
                        break;
                    }
            }
        }
        if (f == null) {
            throw new AssertionViolatedException("Field '" + field_name + "' not found in " + jc.getClassName());
        }
        final Type value = stack().peek();
        final Type t = Type.getType(f.getSignature());
        Type shouldbe = t;
        if (shouldbe == Type.BOOLEAN ||
                shouldbe == Type.BYTE ||
                shouldbe == Type.CHAR ||
                shouldbe == Type.SHORT) {
            shouldbe = Type.INT;
        }
        if (t instanceof ReferenceType) {
            ReferenceType rvalue = null;
            if (value instanceof ReferenceType) {
                rvalue = (ReferenceType) value;
                referenceTypeIsInitialized(o, rvalue);
            }
            else{
                constraintViolated(o, "The stack top type '"+value+"' is not of a reference type as expected.");
            }
            // TODO: This can possibly only be checked using Staerk-et-al's "set-of-object types", not
            // using "wider cast object types" created during verification.
            // Comment it out if you encounter problems. See also the analogon at visitPUTFIELD.
            if (!(rvalue.isAssignmentCompatibleWith(shouldbe))) {
                constraintViolated(o, "The stack top type '"+value+"' is not assignment compatible with '"+shouldbe+"'.");
            }
        }
        else{
            if (shouldbe != value) {
                constraintViolated(o, "The stack top type '"+value+"' is not of type '"+shouldbe+"' as expected.");
            }
        }
        // TODO: Interface fields may be assigned to only once. (Hard to implement in
        //       JustIce's execution model). This may only happen in <clinit>, see Pass 3a.

        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitRET(final RET o) {
        if (! (locals().get(o.getIndex()) instanceof ReturnaddressType)) {
            constraintViolated(o, "Expecting a ReturnaddressType in local variable "+o.getIndex()+".");
        }
        if (locals().get(o.getIndex()) == ReturnaddressType.NO_TARGET) {
            throw new AssertionViolatedException("Oops: RET expecting a target!");
        }
        // Other constraints such as non-allowed overlapping subroutines are enforced
        // while building the Subroutines data structure.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitRETURN(final RETURN o) {
        if (mg.getName().equals(Const.CONSTRUCTOR_NAME)) {// If we leave an <init> method
            if ((Frame.getThis() != null) && (!(mg.getClassName().equals(Type.OBJECT.getClassName()))) ) {
                constraintViolated(o, "Leaving a constructor that itself did not call a constructor.");
            }
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitSALOAD(final SALOAD o) {
        indexOfInt(o, stack().peek());
        if (stack().peek(1) == Type.NULL) {
            return;
        }
        if (! (stack().peek(1) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-top must be of type short[] but is '"+stack().peek(1)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(1))).getBasicType();
        if (t != Type.SHORT) {
            constraintViolated(o, "Stack next-to-top must be of type short[] but is '"+stack().peek(1)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitSASTORE(final SASTORE o) {
        if (stack().peek() != Type.INT) {
            constraintViolated(o, "The value at the stack top is not of type 'int', but of type '"+stack().peek()+"'.");
        }
        indexOfInt(o, stack().peek(1));
        if (stack().peek(2) == Type.NULL) {
            return;
        }
        if (! (stack().peek(2) instanceof ArrayType)) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '"+stack().peek(2)+"'.");
        }
        final Type t = ((ArrayType) (stack().peek(2))).getBasicType();
        if (t != Type.SHORT) {
            constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '"+stack().peek(2)+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitSIPUSH(final SIPUSH o) {
        // nothing to do here. Generic visitXXX() methods did the trick before.
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitSWAP(final SWAP o) {
        if (stack().peek().getSize() != 1) {
            constraintViolated(o, "The value at the stack top is not of size '1', but of size '"+stack().peek().getSize()+"'.");
        }
        if (stack().peek(1).getSize() != 1) {
            constraintViolated(o,
                "The value at the stack next-to-top is not of size '1', but of size '"+stack().peek(1).getSize()+"'.");
        }
    }

    /**
     * Ensures the specific preconditions of the said instruction.
     */
    @Override
    public void visitTABLESWITCH(final TABLESWITCH o) {
        indexOfInt(o, stack().peek());
        // See Pass 3a.
    }

}

