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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

/**
 * Instances of this class contain information about the subroutines
 * found in a code array of a method.
 * This implementation considers the top-level (the instructions
 * reachable without a JSR or JSR_W starting off from the first
 * instruction in a code array of a method) being a special subroutine;
 * see getTopLevel() for that.
 * Please note that the definition of subroutines in the Java Virtual
 * Machine Specification, Second Edition is somewhat incomplete.
 * Therefore, JustIce uses an own, more rigid notion.
 * Basically, a subroutine is a piece of code that starts at the target
 * of a JSR of JSR_W instruction and ends at a corresponding RET
 * instruction. Note also that the control flow of a subroutine
 * may be complex and non-linear; and that subroutines may be nested.
 * JustIce also mandates subroutines not to be protected by exception
 * handling code (for the sake of control flow predictability).
 * To understand JustIce's notion of subroutines, please read
 *
 * TODO: refer to the paper.
 *
 * @see #getTopLevel()
 */
public class Subroutines{
    /**
     * This inner class implements the Subroutine interface.
     */
    private class SubroutineImpl implements Subroutine{
        /**
         * UNSET, a symbol for an uninitialized localVariable
         * field. This is used for the "top-level" Subroutine;
         * i.e. no subroutine.
         */
        private static final int UNSET = -1;

        /**
         * The Local Variable slot where the first
         * instruction of this subroutine (an ASTORE) stores
         * the JsrInstruction's ReturnAddress in and
         * the RET of this subroutine operates on.
         */
        private int localVariable = UNSET;

        /** The instructions that belong to this subroutine. */
        private final Set<InstructionHandle> instructions = new HashSet<>(); // Elements: InstructionHandle

        /*
         * Refer to the Subroutine interface for documentation.
         */
        @Override
        public boolean contains(final InstructionHandle inst) {
            return instructions.contains(inst);
        }

        /**
         * The JSR or JSR_W instructions that define this
         * subroutine by targeting it.
         */
        private final Set<InstructionHandle> theJSRs = new HashSet<>();

        /**
         * The RET instruction that leaves this subroutine.
         */
        private InstructionHandle theRET;

        /**
         * Returns a String representation of this object, merely
         * for debugging purposes.
         * (Internal) Warning: Verbosity on a problematic subroutine may cause
         * stack overflow errors due to recursive subSubs() calls.
         * Don't use this, then.
         */
        @Override
        public String toString() {
            final StringBuilder ret = new StringBuilder();
            ret.append("Subroutine: Local variable is '").append(localVariable);
            ret.append("', JSRs are '").append(theJSRs);
            ret.append("', RET is '").append(theRET);
            ret.append("', Instructions: '").append(instructions).append("'.");

            ret.append(" Accessed local variable slots: '");
            int[] alv = getAccessedLocalsIndices();
            for (final int element : alv) {
                ret.append(element);ret.append(" ");
            }
            ret.append("'.");

            ret.append(" Recursively (via subsub...routines) accessed local variable slots: '");
            alv = getRecursivelyAccessedLocalsIndices();
            for (final int element : alv) {
                ret.append(element);ret.append(" ");
            }
            ret.append("'.");

            return ret.toString();
        }

        /**
         * Sets the leaving RET instruction. Must be invoked after all instructions are added.
         * Must not be invoked for top-level 'subroutine'.
         */
        void setLeavingRET() {
            if (localVariable == UNSET) {
                throw new AssertionViolatedException(
                    "setLeavingRET() called for top-level 'subroutine' or forgot to set local variable first.");
            }
            InstructionHandle ret = null;
            for (final InstructionHandle actual : instructions) {
                if (actual.getInstruction() instanceof RET) {
                    if (ret != null) {
                        throw new StructuralCodeConstraintException(
                            "Subroutine with more then one RET detected: '"+ret+"' and '"+actual+"'.");
                    }
                    ret = actual;
                }
            }
            if (ret == null) {
                throw new StructuralCodeConstraintException("Subroutine without a RET detected.");
            }
            if (((RET) ret.getInstruction()).getIndex() != localVariable) {
                throw new StructuralCodeConstraintException(
                    "Subroutine uses '"+ret+"' which does not match the correct local variable '"+localVariable+"'.");
            }
            theRET = ret;
        }

        /*
         * Refer to the Subroutine interface for documentation.
         */
        @Override
        public InstructionHandle[] getEnteringJsrInstructions() {
            if (this == getTopLevel()) {
                throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
            }
            final InstructionHandle[] jsrs = new InstructionHandle[theJSRs.size()];
            return theJSRs.toArray(jsrs);
        }

        /**
         * Adds a new JSR or JSR_W that has this subroutine as its target.
         */
        public void addEnteringJsrInstruction(final InstructionHandle jsrInst) {
            if ( (jsrInst == null) || (! (jsrInst.getInstruction() instanceof JsrInstruction))) {
                throw new AssertionViolatedException("Expecting JsrInstruction InstructionHandle.");
            }
            if (localVariable == UNSET) {
                throw new AssertionViolatedException("Set the localVariable first!");
            }
            // Something is wrong when an ASTORE is targeted that does not operate on the same local variable than the rest of the
            // JsrInstruction-targets and the RET.
            // (We don't know out leader here so we cannot check if we're really targeted!)
            if (localVariable != ((ASTORE) (((JsrInstruction) jsrInst.getInstruction()).getTarget().getInstruction())).getIndex()) {
                throw new AssertionViolatedException("Setting a wrong JsrInstruction.");
            }
            theJSRs.add(jsrInst);
        }

        /*
         * Refer to the Subroutine interface for documentation.
         */
        @Override
        public InstructionHandle getLeavingRET() {
            if (this == getTopLevel()) {
                throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
            }
            return theRET;
        }

        /*
         * Refer to the Subroutine interface for documentation.
         */
        @Override
        public InstructionHandle[] getInstructions() {
            final InstructionHandle[] ret = new InstructionHandle[instructions.size()];
            return instructions.toArray(ret);
        }

        /*
         * Adds an instruction to this subroutine.
         * All instructions must have been added before invoking setLeavingRET().
         * @see #setLeavingRET
         */
        void addInstruction(final InstructionHandle ih) {
            if (theRET != null) {
                throw new AssertionViolatedException("All instructions must have been added before invoking setLeavingRET().");
            }
            instructions.add(ih);
        }

        /* Satisfies Subroutine.getRecursivelyAccessedLocalsIndices(). */
        @Override
        public int[] getRecursivelyAccessedLocalsIndices() {
            final Set<Integer> s = new HashSet<>();
            final int[] lvs = getAccessedLocalsIndices();
            for (final int lv : lvs) {
                s.add(Integer.valueOf(lv));
            }
            _getRecursivelyAccessedLocalsIndicesHelper(s, this.subSubs());
            final int[] ret = new int[s.size()];
            int j=-1;
            for (final Integer index : s) {
                j++;
                ret[j] = index.intValue();
            }
            return ret;
        }

        /**
         * A recursive helper method for getRecursivelyAccessedLocalsIndices().
         * @see #getRecursivelyAccessedLocalsIndices()
         */
        private void _getRecursivelyAccessedLocalsIndicesHelper(final Set<Integer> s, final Subroutine[] subs) {
            for (final Subroutine sub : subs) {
                final int[] lvs = sub.getAccessedLocalsIndices();
                for (final int lv : lvs) {
                    s.add(Integer.valueOf(lv));
                }
                if(sub.subSubs().length != 0) {
                    _getRecursivelyAccessedLocalsIndicesHelper(s, sub.subSubs());
                }
            }
        }

        /*
         * Satisfies Subroutine.getAccessedLocalIndices().
         */
        @Override
        public int[] getAccessedLocalsIndices() {
            //TODO: Implement caching.
            final Set<Integer> acc = new HashSet<>();
            if (theRET == null && this != getTopLevel()) {
                throw new AssertionViolatedException(
                    "This subroutine object must be built up completely before calculating accessed locals.");
            }
            {
                for (final InstructionHandle ih : instructions) {
                    // RET is not a LocalVariableInstruction in the current version of BCEL.
                    if (ih.getInstruction() instanceof LocalVariableInstruction || ih.getInstruction() instanceof RET) {
                        final int idx = ((IndexedInstruction) (ih.getInstruction())).getIndex();
                        acc.add(Integer.valueOf(idx));
                        // LONG? DOUBLE?.
                        try{
                            // LocalVariableInstruction instances are typed without the need to look into
                            // the constant pool.
                            if (ih.getInstruction() instanceof LocalVariableInstruction) {
                                final int s = ((LocalVariableInstruction) ih.getInstruction()).getType(null).getSize();
                                if (s==2) {
                                    acc.add(Integer.valueOf(idx+1));
                                }
                            }
                        }
                        catch(final RuntimeException re) {
                            throw new AssertionViolatedException("BCEL did not like NULL as a ConstantPoolGen object.", re);
                        }
                    }
                }
            }

            {
                final int[] ret = new int[acc.size()];
                int j=-1;
                for (final Integer accessedLocal : acc) {
                    j++;
                    ret[j] = accessedLocal.intValue();
                }
                return ret;
            }
        }

        /*
         * Satisfies Subroutine.subSubs().
         */
        @Override
        public Subroutine[] subSubs() {
            final Set<Subroutine> h = new HashSet<>();

            for (final InstructionHandle ih : instructions) {
                final Instruction inst = ih.getInstruction();
                if (inst instanceof JsrInstruction) {
                    final InstructionHandle targ = ((JsrInstruction) inst).getTarget();
                    h.add(getSubroutine(targ));
                }
            }
            final Subroutine[] ret = new Subroutine[h.size()];
            return h.toArray(ret);
        }

        /*
         * Sets the local variable slot the ASTORE that is targeted
         * by the JsrInstructions of this subroutine operates on.
         * This subroutine's RET operates on that same local variable
         * slot, of course.
         */
        void setLocalVariable(final int i) {
            if (localVariable != UNSET) {
                throw new AssertionViolatedException("localVariable set twice.");
            }
            localVariable = i;
        }

        /**
         * The default constructor.
         */
        public SubroutineImpl() {
        }

    }// end Inner Class SubrouteImpl

    //Node coloring constants
    private enum ColourConstants{
        WHITE,
        GRAY,
        BLACK
    }

    /**
     * The map containing the subroutines found.
     * Key: InstructionHandle of the leader of the subroutine.
     * Elements: SubroutineImpl objects.
     */
    private final Map<InstructionHandle, Subroutine> subroutines = new HashMap<>();

    /**
     * This is referring to a special subroutine, namely the
     * top level. This is not really a subroutine but we use
     * it to distinguish between top level instructions and
     * unreachable instructions.
     */
    // CHECKSTYLE:OFF
    public final Subroutine TOPLEVEL; // TODO can this be made private?
    // CHECKSTYLE:ON

    /**
     * Constructor.
     * @param mg A MethodGen object representing method to
     * create the Subroutine objects of.
     * Assumes that JustIce strict checks are needed.
     */
    public Subroutines(final MethodGen mg) {
        this(mg, true);
    }

    /**
     * Constructor.
     * @param mg A MethodGen object representing method to
     * create the Subroutine objects of.
     * @param enableJustIceCheck whether to enable additional JustIce checks
     * @since 6.0
     */
    public Subroutines(final MethodGen mg, final boolean enableJustIceCheck) {
        final InstructionHandle[] all = mg.getInstructionList().getInstructionHandles();
        final CodeExceptionGen[] handlers = mg.getExceptionHandlers();

        // Define our "Toplevel" fake subroutine.
        TOPLEVEL = new SubroutineImpl();

        // Calculate "real" subroutines.
        final Set<InstructionHandle> sub_leaders = new HashSet<>(); // Elements: InstructionHandle
        for (final InstructionHandle element : all) {
            final Instruction inst = element.getInstruction();
            if (inst instanceof JsrInstruction) {
                sub_leaders.add(((JsrInstruction) inst).getTarget());
            }
        }

        // Build up the database.
        for (final InstructionHandle astore : sub_leaders) {
            final SubroutineImpl sr = new SubroutineImpl();
            sr.setLocalVariable( ((ASTORE) (astore.getInstruction())).getIndex() );
            subroutines.put(astore, sr);
        }

        // Fake it a bit. We want a virtual "TopLevel" subroutine.
        subroutines.put(all[0], TOPLEVEL);
        sub_leaders.add(all[0]);

        // Tell the subroutines about their JsrInstructions.
        // Note that there cannot be a JSR targeting the top-level
        // since "Jsr 0" is disallowed in Pass 3a.
        // Instructions shared by a subroutine and the toplevel are
        // disallowed and checked below, after the BFS.
        for (final InstructionHandle element : all) {
            final Instruction inst = element.getInstruction();
            if (inst instanceof JsrInstruction) {
                final InstructionHandle leader = ((JsrInstruction) inst).getTarget();
                ((SubroutineImpl) getSubroutine(leader)).addEnteringJsrInstruction(element);
            }
        }

        // Now do a BFS from every subroutine leader to find all the
        // instructions that belong to a subroutine.
        // we don't want to assign an instruction to two or more Subroutine objects.
        final Set<InstructionHandle> instructions_assigned = new HashSet<>();

        //Graph colouring. Key: InstructionHandle, Value: ColourConstants enum .
        final Map<InstructionHandle, ColourConstants> colors = new HashMap<>();

        final List<InstructionHandle> Q = new ArrayList<>();
        for (final InstructionHandle actual : sub_leaders) {
            // Do some BFS with "actual" as the root of the graph.
            // Init colors
            for (final InstructionHandle element : all) {
                colors.put(element, ColourConstants.WHITE);
            }
            colors.put(actual, ColourConstants.GRAY);
            // Init Queue

            Q.clear();
            Q.add(actual); // add(Obj) adds to the end, remove(0) removes from the start.

            /*
             * BFS ALGORITHM MODIFICATION:
             * Start out with multiple "root" nodes, as exception handlers are starting points of top-level code, too.
             * [why top-level?
             * TODO: Refer to the special JustIce notion of subroutines.]
             */
            if (actual == all[0]) {
                for (final CodeExceptionGen handler : handlers) {
                    colors.put(handler.getHandlerPC(), ColourConstants.GRAY);
                    Q.add(handler.getHandlerPC());
                }
            }
            /* CONTINUE NORMAL BFS ALGORITHM */

            // Loop until Queue is empty
            while (Q.size() != 0) {
                final InstructionHandle u = Q.remove(0);
                final InstructionHandle[] successors = getSuccessors(u);
                for (final InstructionHandle successor : successors) {
                    if (colors.get(successor) == ColourConstants.WHITE) {
                        colors.put(successor, ColourConstants.GRAY);
                        Q.add(successor);
                    }
                }
                colors.put(u, ColourConstants.BLACK);
            }
            // BFS ended above.
            for (final InstructionHandle element : all) {
                if (colors.get(element) == ColourConstants.BLACK) {
                    ((SubroutineImpl) (actual==all[0]?getTopLevel():getSubroutine(actual))).addInstruction(element);
                    if (instructions_assigned.contains(element)) {
                        throw new StructuralCodeConstraintException("Instruction '"+element+
                            "' is part of more than one subroutine (or of the top level and a subroutine).");
                    }
                    instructions_assigned.add(element);
                }
            }
            if (actual != all[0]) {// If we don't deal with the top-level 'subroutine'
                ((SubroutineImpl) getSubroutine(actual)).setLeavingRET();
            }
        }

        if (enableJustIceCheck) {
            // Now make sure no instruction of a Subroutine is protected by exception handling code
            // as is mandated by JustIces notion of subroutines.
            for (final CodeExceptionGen handler : handlers) {
                InstructionHandle _protected = handler.getStartPC();
                while (_protected != handler.getEndPC().getNext()) {
                    // Note the inclusive/inclusive notation of "generic API" exception handlers!
                    for (final Subroutine sub : subroutines.values()) {
                        if (sub != subroutines.get(all[0])) {    // We don't want to forbid top-level exception handlers.
                            if (sub.contains(_protected)) {
                                throw new StructuralCodeConstraintException("Subroutine instruction '"+_protected+
                                    "' is protected by an exception handler, '"+handler+
                                    "'. This is forbidden by the JustIce verifier due to its clear definition of subroutines.");
                            }
                        }
                    }
                    _protected = _protected.getNext();
                }
            }
        }

        // Now make sure no subroutine is calling a subroutine
        // that uses the same local variable for the RET as themselves
        // (recursively).
        // This includes that subroutines may not call themselves
        // recursively, even not through intermediate calls to other
        // subroutines.
        noRecursiveCalls(getTopLevel(), new HashSet<Integer>());

    }

    /**
     * This (recursive) utility method makes sure that
     * no subroutine is calling a subroutine
     * that uses the same local variable for the RET as themselves
     * (recursively).
     * This includes that subroutines may not call themselves
     * recursively, even not through intermediate calls to other
     * subroutines.
     *
     * @throws StructuralCodeConstraintException if the above constraint is not satisfied.
     */
    private void noRecursiveCalls(final Subroutine sub, final Set<Integer> set) {
        final Subroutine[] subs = sub.subSubs();

        for (final Subroutine sub2 : subs) {
            final int index = ((RET) (sub2.getLeavingRET().getInstruction())).getIndex();

            if (!set.add(Integer.valueOf(index))) {
                // Don't use toString() here because of possibly infinite recursive subSubs() calls then.
                final SubroutineImpl si = (SubroutineImpl) sub2;
                throw new StructuralCodeConstraintException("Subroutine with local variable '"+si.localVariable+"', JSRs '"+
                si.theJSRs+"', RET '"+si.theRET+
                "' is called by a subroutine which uses the same local variable index as itself; maybe even a recursive call?"+
                " JustIce's clean definition of a subroutine forbids both.");
            }

            noRecursiveCalls(sub2, set);

            set.remove(Integer.valueOf(index));
        }
    }

    /**
     * Returns the Subroutine object associated with the given
     * leader (that is, the first instruction of the subroutine).
     * You must not use this to get the top-level instructions
     * modeled as a Subroutine object.
     *
     * @see #getTopLevel()
     */
    public Subroutine getSubroutine(final InstructionHandle leader) {
        final Subroutine ret = subroutines.get(leader);

        if (ret == null) {
            throw new AssertionViolatedException(
                "Subroutine requested for an InstructionHandle that is not a leader of a subroutine.");
        }

        if (ret == TOPLEVEL) {
            throw new AssertionViolatedException("TOPLEVEL special subroutine requested; use getTopLevel().");
        }

        return ret;
    }

    /**
     * Returns the subroutine object associated with the
     * given instruction. This is a costly operation, you
     * should consider using getSubroutine(InstructionHandle).
     * Returns 'null' if the given InstructionHandle lies
     * in so-called 'dead code', i.e. code that can never
     * be executed.
     *
     * @see #getSubroutine(InstructionHandle)
     * @see #getTopLevel()
     */
    public Subroutine subroutineOf(final InstructionHandle any) {
        for (final Subroutine s : subroutines.values()) {
            if (s.contains(any)) {
                return s;
            }
        }
        System.err.println("DEBUG: Please verify '"+any.toString(true)+"' lies in dead code.");
        return null;
        //throw new AssertionViolatedException("No subroutine for InstructionHandle found (DEAD CODE?).");
    }

    /**
     * For easy handling, the piece of code that is <B>not</B> a
     * subroutine, the top-level, is also modeled as a Subroutine
     * object.
     * It is a special Subroutine object where <B>you must not invoke
     * getEnteringJsrInstructions() or getLeavingRET()</B>.
     *
     * @see Subroutine#getEnteringJsrInstructions()
     * @see Subroutine#getLeavingRET()
     */
    public Subroutine getTopLevel() {
        return TOPLEVEL;
    }
    /**
     * A utility method that calculates the successors of a given InstructionHandle
     * <B>in the same subroutine</B>. That means, a RET does not have any successors
     * as defined here. A JsrInstruction has its physical successor as its successor
     * (opposed to its target) as defined here.
     */
    private static InstructionHandle[] getSuccessors(final InstructionHandle instruction) {
        final InstructionHandle[] empty = new InstructionHandle[0];
        final InstructionHandle[] single = new InstructionHandle[1];

        final Instruction inst = instruction.getInstruction();

        if (inst instanceof RET) {
            return empty;
        }

        // Terminates method normally.
        if (inst instanceof ReturnInstruction) {
            return empty;
        }

        // Terminates method abnormally, because JustIce mandates
        // subroutines not to be protected by exception handlers.
        if (inst instanceof ATHROW) {
            return empty;
        }

        // See method comment.
        if (inst instanceof JsrInstruction) {
            single[0] = instruction.getNext();
            return single;
        }

        if (inst instanceof GotoInstruction) {
            single[0] = ((GotoInstruction) inst).getTarget();
            return single;
        }

        if (inst instanceof BranchInstruction) {
            if (inst instanceof Select) {
                // BCEL's getTargets() returns only the non-default targets,
                // thanks to Eli Tilevich for reporting.
                final InstructionHandle[] matchTargets = ((Select) inst).getTargets();
                final InstructionHandle[] ret = new InstructionHandle[matchTargets.length+1];
                ret[0] = ((Select) inst).getTarget();
                System.arraycopy(matchTargets, 0, ret, 1, matchTargets.length);
                return ret;
            }
            final InstructionHandle[] pair = new InstructionHandle[2];
            pair[0] = instruction.getNext();
            pair[1] = ((BranchInstruction) inst).getTarget();
            return pair;
        }

        // default case: Fall through.
        single[0] = instruction.getNext();
        return single;
    }

    /**
     * Returns a String representation of this object; merely for debugging puposes.
     */
    @Override
    public String toString() {
        return "---\n"+subroutines+"\n---\n";
    }
}
