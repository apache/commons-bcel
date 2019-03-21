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
 *
 */

package org.apache.bcel;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NOP;
import org.junit.Test;

import junit.framework.AssertionFailedError;

/**
 * Test for https://issues.apache.org/jira/browse/BCEL-267 "Race conditions on
 * static fields in BranchHandle and InstructionHandle".
 */
public class HandleTestCase {

    static Throwable exception;
    static final int MAXI = 100;
    static final int MAXJ = 1000;

    /**
     * Asserts that branch handles can be added an instruction list, without
     * corrupting the list.
     */
    static void branchHandles() {
        for (int i = 0; i < MAXI; i++) {
            final InstructionList list = new InstructionList();
            final InstructionHandle start = list.append(new NOP());
            try {
                for (int j = 0; j < MAXJ; j++) {
                    list.append(new GOTO(start));
                }
                final InstructionHandle[] instructionHandles = list.getInstructionHandles();
                for (int j = 0; j < instructionHandles.length; j++) {
                    final InstructionHandle handle = instructionHandles[j];
                    if (j > 0) {
                        checkLinkage(handle, j);
                        if (start != ((GOTO) handle.getInstruction()).getTarget()) {
                            final AssertionFailedError error = new AssertionFailedError(
                                    "unexpected instruction at index " + j);
                            exception = error;
                            throw error;
                        }
                    }
                }
                if (exception != null) {
                    return;
                }
            } catch (final NullPointerException e) {
                System.out.println("NPE at i=" + i);
                exception = e;
                throw e;
            }
            list.dispose(); // this initializes caching of unused instruction handles
        }
    }

    /**
     * Assert that opposite next/prev pairs always match.
     */
    static void checkLinkage(final InstructionHandle ih, final int index) {
        final InstructionHandle prev = ih.getPrev();
        final InstructionHandle next = ih.getNext();
        if ((prev != null && prev.getNext() != ih) || (next != null && next.getPrev() != ih)) {
            final AssertionFailedError error = new AssertionFailedError("corrupt instruction list at index " + index);
            exception = error;
            throw error;
        }
    }

    /**
     * Asserts that instruction handles can be added an instruction list, without
     * corrupting the list.
     */
    static void handles() {
        for (int i = 0; i < MAXI; i++) {
            final InstructionList list = new InstructionList();
            try {
                for (int j = 0; j < MAXJ; j++) {
                    list.append(new ILOAD(j));
                }
                final InstructionHandle[] instructionHandles = list.getInstructionHandles();
                for (int j = 0; j < instructionHandles.length; j++) {
                    final InstructionHandle handle = instructionHandles[j];
                    checkLinkage(handle, j);
                    if (j != ((ILOAD) handle.getInstruction()).getIndex()) {
                        final AssertionFailedError error = new AssertionFailedError("unexpected instruction at index " + j);
                        exception = error;
                        throw error;
                    }
                }
                if (exception != null) {
                    return;
                }
            } catch (final NullPointerException e) {
                System.out.println("NPE at i=" + i);
                exception = e;
                throw e;
            }
            list.dispose(); // this initializes caching of unused instruction handles
        }
    }

    /**
     * Concurrently run the given runnable in two threads.
     */
    private void perform(final Runnable r) throws Throwable {
        exception = null;
        final Thread t1 = new Thread(r);
        final Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Assert that two independent instruction lists can be modified concurrently.
     * Here: inserting branch instructions.
     */
    @Test
    public void testBranchHandle() throws Throwable {
        perform(HandleTestCase::branchHandles);
    }

    /**
     * Assert that two independent instruction lists can be modified concurrently.
     * Here: inserting regular instructions.
     */
    @Test
    public void testInstructionHandle() throws Throwable {
        perform(HandleTestCase::handles);
    }
}