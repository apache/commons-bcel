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


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;

/**
 * This PassVerifier verifies a method of class file according to pass 3,
 * so-called structural verification as described in The Java Virtual Machine
 * Specification, 2nd edition.
 * More detailed information is to be found at the do_verify() method's
 * documentation.
 *
 * @see #do_verify()
 */

public final class Pass3bVerifier extends PassVerifier{
    /* TODO:    Throughout pass 3b, upper halves of LONG and DOUBLE
                        are represented by Type.UNKNOWN. This should be changed
                        in favour of LONG_Upper and DOUBLE_Upper as in pass 2. */

    /**
     * An InstructionContextQueue is a utility class that holds
     * (InstructionContext, ArrayList) pairs in a Queue data structure.
     * This is used to hold information about InstructionContext objects
     * externally --- i.e. that information is not saved inside the
     * InstructionContext object itself. This is useful to save the
     * execution path of the symbolic execution of the
     * Pass3bVerifier - this is not information
     * that belongs into the InstructionContext object itself.
     * Only at "execute()"ing
     * time, an InstructionContext object will get the current information
     * we have about its symbolic execution predecessors.
     */
    private static final class InstructionContextQueue {
        // The following two fields together represent the queue.
        /** The first elements from pairs in the queue. */
        private final List<InstructionContext> ics = new Vector<>();
        /** The second elements from pairs in the queue. */
        private final List<ArrayList<InstructionContext>> ecs = new Vector<>();

        /**
         * Adds an (InstructionContext, ExecutionChain) pair to this queue.
         *
         * @param ic the InstructionContext
         * @param executionChain the ExecutionChain
         */
        public void add(final InstructionContext ic, final ArrayList<InstructionContext> executionChain) {
            ics.add(ic);
            ecs.add(executionChain);
        }

        /**
         * Tests if InstructionContext queue is empty.
         *
         * @return true if the InstructionContext queue is empty.
         */
        public boolean isEmpty() {
            return ics.isEmpty();
        }

        /**
         * Removes a specific (InstructionContext, ExecutionChain) pair from their respective queues.
         *
         * @param i the index of the items to be removed
         */
        public void remove(final int i) {
            ics.remove(i);
            ecs.remove(i);
        }

        /**
         * Gets a specific InstructionContext from the queue.
         *
         * @param i the index of the item to be fetched
         * @return the indicated InstructionContext
         */
        public InstructionContext getIC(final int i) {
            return ics.get(i);
        }

        /**
         * Gets a specific ExecutionChain from the queue.
         *
         * @param i the index of the item to be fetched
         * @return the indicated ExecutionChain
         */
        public ArrayList<InstructionContext> getEC(final int i) {
            return ecs.get(i);
        }

        /**
         * Gets the size of the InstructionContext queue.
         *
         * @return the size of the InstructionQueue
         */
        public int size() {
            return ics.size();
        }
    } // end Inner Class InstructionContextQueue

    /** In DEBUG mode, the verification algorithm is not randomized. */
    private static final boolean DEBUG = true;

    /** The Verifier that created this. */
    private final Verifier myOwner;

    /** The method number to verify. */
    private final int methodNo;

    /**
     * This class should only be instantiated by a Verifier.
     *
     * @see org.apache.bcel.verifier.Verifier
     */
    public Pass3bVerifier(final Verifier owner, final int method_no) {
        myOwner = owner;
        this.methodNo = method_no;
    }

    /**
     * Whenever the outgoing frame
     * situation of an InstructionContext changes, all its successors are
     * put [back] into the queue [as if they were unvisited].
     * The proof of termination is about the existence of a
     * fix point of frame merging.
     */
    private void circulationPump(final MethodGen m,final ControlFlowGraph cfg, final InstructionContext start,
            final Frame vanillaFrame, final InstConstraintVisitor icv, final ExecutionVisitor ev) {
        final Random random = new Random();
        final InstructionContextQueue icq = new InstructionContextQueue();

        start.execute(vanillaFrame, new ArrayList<InstructionContext>(), icv, ev);
        // new ArrayList() <=>    no Instruction was executed before
        //                                    => Top-Level routine (no jsr call before)
        icq.add(start, new ArrayList<InstructionContext>());

        // LOOP!
        while (!icq.isEmpty()) {
            InstructionContext u;
            ArrayList<InstructionContext> ec;
            if (!DEBUG) {
                final int r = random.nextInt(icq.size());
                u = icq.getIC(r);
                ec = icq.getEC(r);
                icq.remove(r);
            }
            else{
                u  = icq.getIC(0);
                ec = icq.getEC(0);
                icq.remove(0);
            }

            @SuppressWarnings("unchecked") // ec is of type ArrayList<InstructionContext>
            final
            ArrayList<InstructionContext> oldchain = (ArrayList<InstructionContext>) (ec.clone());
            @SuppressWarnings("unchecked") // ec is of type ArrayList<InstructionContext>
            final
            ArrayList<InstructionContext> newchain = (ArrayList<InstructionContext>) (ec.clone());
            newchain.add(u);

            if ((u.getInstruction().getInstruction()) instanceof RET) {
//System.err.println(u);
                // We can only follow _one_ successor, the one after the
                // JSR that was recently executed.
                final RET ret = (RET) (u.getInstruction().getInstruction());
                final ReturnaddressType t = (ReturnaddressType) u.getOutFrame(oldchain).getLocals().get(ret.getIndex());
                final InstructionContext theSuccessor = cfg.contextOf(t.getTarget());

                // Sanity check
                InstructionContext lastJSR = null;
                int skip_jsr = 0;
                for (int ss=oldchain.size()-1; ss >= 0; ss--) {
                    if (skip_jsr < 0) {
                        throw new AssertionViolatedException("More RET than JSR in execution chain?!");
                    }
//System.err.println("+"+oldchain.get(ss));
                    if ((oldchain.get(ss)).getInstruction().getInstruction() instanceof JsrInstruction) {
                        if (skip_jsr == 0) {
                            lastJSR = oldchain.get(ss);
                            break;
                        }
                        skip_jsr--;
                    }
                    if ((oldchain.get(ss)).getInstruction().getInstruction() instanceof RET) {
                        skip_jsr++;
                    }
                }
                if (lastJSR == null) {
                    throw new AssertionViolatedException("RET without a JSR before in ExecutionChain?! EC: '"+oldchain+"'.");
                }
                final JsrInstruction jsr = (JsrInstruction) (lastJSR.getInstruction().getInstruction());
                if ( theSuccessor != (cfg.contextOf(jsr.physicalSuccessor())) ) {
                    throw new AssertionViolatedException("RET '"+u.getInstruction()+"' info inconsistent: jump back to '"+
                        theSuccessor+"' or '"+cfg.contextOf(jsr.physicalSuccessor())+"'?");
                }

                if (theSuccessor.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
                    @SuppressWarnings("unchecked") // newchain is already of type ArrayList<InstructionContext>
                    final
                    ArrayList<InstructionContext> newchainClone = (ArrayList<InstructionContext>) newchain.clone();
                    icq.add(theSuccessor, newchainClone);
                }
            }
            else{// "not a ret"

                // Normal successors. Add them to the queue of successors.
                final InstructionContext[] succs = u.getSuccessors();
                for (final InstructionContext v : succs) {
                    if (v.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
                        @SuppressWarnings("unchecked") // newchain is already of type ArrayList<InstructionContext>
                        final
                        ArrayList<InstructionContext> newchainClone = (ArrayList<InstructionContext>) newchain.clone();
                        icq.add(v, newchainClone);
                    }
                }
            }// end "not a ret"

            // Exception Handlers. Add them to the queue of successors.
            // [subroutines are never protected; mandated by JustIce]
            final ExceptionHandler[] exc_hds = u.getExceptionHandlers();
            for (final ExceptionHandler exc_hd : exc_hds) {
                final InstructionContext v = cfg.contextOf(exc_hd.getHandlerStart());
                // TODO: the "oldchain" and "newchain" is used to determine the subroutine
                // we're in (by searching for the last JSR) by the InstructionContext
                // implementation. Therefore, we should not use this chain mechanism
                // when dealing with exception handlers.
                // Example: a JSR with an exception handler as its successor does not
                // mean we're in a subroutine if we go to the exception handler.
                // We should address this problem later; by now we simply "cut" the chain
                // by using an empty chain for the exception handlers.
                //if (v.execute(new Frame(u.getOutFrame(oldchain).getLocals(),
                // new OperandStack (u.getOutFrame().getStack().maxStack(),
                // (exc_hds[s].getExceptionType()==null? Type.THROWABLE : exc_hds[s].getExceptionType())) ), newchain), icv, ev) {
                    //icq.add(v, (ArrayList) newchain.clone());
                if (v.execute(new Frame(u.getOutFrame(oldchain).getLocals(),
                        new OperandStack (u.getOutFrame(oldchain).getStack().maxStack(),
                        exc_hd.getExceptionType()==null? Type.THROWABLE : exc_hd.getExceptionType())),
                        new ArrayList<InstructionContext>(), icv, ev)) {
                    icq.add(v, new ArrayList<InstructionContext>());
                }
            }

        }// while (!icq.isEmpty()) END

        InstructionHandle ih = start.getInstruction();
        do{
            if ((ih.getInstruction() instanceof ReturnInstruction) && (!(cfg.isDead(ih)))) {
                final InstructionContext ic = cfg.contextOf(ih);
                // TODO: This is buggy, we check only the top-level return instructions this way.
                // Maybe some maniac returns from a method when in a subroutine?
                final Frame f = ic.getOutFrame(new ArrayList<InstructionContext>());
                final LocalVariables lvs = f.getLocals();
                for (int i=0; i<lvs.maxLocals(); i++) {
                    if (lvs.get(i) instanceof UninitializedObjectType) {
                        this.addMessage("Warning: ReturnInstruction '"+ic+
                            "' may leave method with an uninitialized object in the local variables array '"+lvs+"'.");
                    }
                }
                final OperandStack os = f.getStack();
                for (int i=0; i<os.size(); i++) {
                    if (os.peek(i) instanceof UninitializedObjectType) {
                        this.addMessage("Warning: ReturnInstruction '"+ic+
                            "' may leave method with an uninitialized object on the operand stack '"+os+"'.");
                    }
                }
                //see JVM $4.8.2
                Type returnedType = null;
                final OperandStack inStack = ic.getInFrame().getStack();
                if (inStack.size() >= 1) {
                    returnedType = inStack.peek();
                } else {
                    returnedType = Type.VOID;
                }

                if (returnedType != null) {
                    if (returnedType instanceof ReferenceType) {
                        try {
                            if (!((ReferenceType) returnedType).isCastableTo(m.getReturnType())) {
                                invalidReturnTypeError(returnedType, m);
                            }
                        } catch (final ClassNotFoundException e) {
                            // Don't know what do do now, so raise RuntimeException
                            throw new IllegalArgumentException(e);
                        }
                    } else if (!returnedType.equals(m.getReturnType().normalizeForStackOrLocal())) {
                        invalidReturnTypeError(returnedType, m);
                    }
                }
            }
        } while ((ih = ih.getNext()) != null);

     }

    /**
     * Throws an exception indicating the returned type is not compatible with the return type of the given method.
     *
     * @param returnedType the type of the returned expression
     * @param m the method we are processing
     * @throws StructuralCodeConstraintException always
     * @since 6.0
     */
    public void invalidReturnTypeError(final Type returnedType, final MethodGen m) {
        throw new StructuralCodeConstraintException(
            "Returned type "+returnedType+" does not match Method's return type "+m.getReturnType());
    }

    /**
     * Pass 3b implements the data flow analysis as described in the Java Virtual
     * Machine Specification, Second Edition.
     * Later versions will use LocalVariablesInfo objects to verify if the
     * verifier-inferred types and the class file's debug information (LocalVariables
     * attributes) match [TODO].
     *
     * @see org.apache.bcel.verifier.statics.LocalVariablesInfo
     * @see org.apache.bcel.verifier.statics.Pass2Verifier#getLocalVariablesInfo(int)
     */
    @Override
    public VerificationResult do_verify() {
        if (! myOwner.doPass3a(methodNo).equals(VerificationResult.VR_OK)) {
            return VerificationResult.VR_NOTYET;
        }

        // Pass 3a ran before, so it's safe to assume the JavaClass object is
        // in the BCEL repository.
        JavaClass jc;
        try {
            jc = Repository.lookupClass(myOwner.getClassName());
        } catch (final ClassNotFoundException e) {
            // FIXME: maybe not the best way to handle this
            throw new AssertionViolatedException("Missing class: " + e, e);
        }

        final ConstantPoolGen constantPoolGen = new ConstantPoolGen(jc.getConstantPool());
        // Init Visitors
        final InstConstraintVisitor icv = new InstConstraintVisitor();
        icv.setConstantPoolGen(constantPoolGen);

        final ExecutionVisitor ev = new ExecutionVisitor();
        ev.setConstantPoolGen(constantPoolGen);

        final Method[] methods = jc.getMethods(); // Method no "methodNo" exists, we ran Pass3a before on it!

        try{

            final MethodGen mg = new MethodGen(methods[methodNo], myOwner.getClassName(), constantPoolGen);

            icv.setMethodGen(mg);

            ////////////// DFA BEGINS HERE ////////////////
            if (! (mg.isAbstract() || mg.isNative()) ) { // IF mg HAS CODE (See pass 2)

                final ControlFlowGraph cfg = new ControlFlowGraph(mg);

                // Build the initial frame situation for this method.
                final Frame f = new Frame(mg.getMaxLocals(),mg.getMaxStack());
                if ( !mg.isStatic() ) {
                    if (mg.getName().equals(Const.CONSTRUCTOR_NAME)) {
                        Frame.setThis(new UninitializedObjectType(ObjectType.getInstance(jc.getClassName())));
                        f.getLocals().set(0, Frame.getThis());
                    }
                    else{
                        Frame.setThis(null);
                        f.getLocals().set(0, ObjectType.getInstance(jc.getClassName()));
                    }
                }
                final Type[] argtypes = mg.getArgumentTypes();
                int twoslotoffset = 0;
                for (int j=0; j<argtypes.length; j++) {
                    if (argtypes[j] == Type.SHORT || argtypes[j] == Type.BYTE ||
                        argtypes[j] == Type.CHAR || argtypes[j] == Type.BOOLEAN) {
                        argtypes[j] = Type.INT;
                    }
                    f.getLocals().set(twoslotoffset + j + (mg.isStatic()?0:1), argtypes[j]);
                    if (argtypes[j].getSize() == 2) {
                        twoslotoffset++;
                        f.getLocals().set(twoslotoffset + j + (mg.isStatic()?0:1), Type.UNKNOWN);
                    }
                }
                circulationPump(mg,cfg, cfg.contextOf(mg.getInstructionList().getStart()), f, icv, ev);
            }
        }
        catch (final VerifierConstraintViolatedException ce) {
            ce.extendMessage("Constraint violated in method '"+methods[methodNo]+"':\n","");
            return new VerificationResult(VerificationResult.VERIFIED_REJECTED, ce.getMessage());
        }
        catch (final RuntimeException re) {
            // These are internal errors

            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            re.printStackTrace(pw);

            throw new AssertionViolatedException("Some RuntimeException occured while verify()ing class '"+jc.getClassName()+
                "', method '"+methods[methodNo]+"'. Original RuntimeException's stack trace:\n---\n"+sw+"---\n", re);
        }
        return VerificationResult.VR_OK;
    }

    /** Returns the method number as supplied when instantiating. */
    public int getMethodNo() {
        return methodNo;
    }
}
