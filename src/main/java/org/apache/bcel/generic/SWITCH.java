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
 */
package org.apache.bcel.generic;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * SWITCH - Branch depending on int value, generates either LOOKUPSWITCH or TABLESWITCH instruction, depending on
 * whether the match values (int[]) can be sorted with no gaps between the numbers.
 */
public final class SWITCH implements CompoundInstruction {

    private int[] match;
    private InstructionHandle[] targets;
    private final Select instruction;
    private final int matchLength;

    public SWITCH(final int[] match, final InstructionHandle[] targets, final InstructionHandle target) {
        this(match, targets, target, 1);
    }

    /**
     * Template for switch() constructs. If the match array can be sorted in ascending order with gaps no larger than
     * maxGap between the numbers, a TABLESWITCH instruction is generated, and a LOOKUPSWITCH otherwise. The former may be
     * more efficient, but needs more space.
     *
     * Note, that the key array always will be sorted, though we leave the original arrays unaltered.
     *
     * @param match array of match values (case 2: ... case 7: ..., etc.)
     * @param targets the instructions to be branched to for each case
     * @param target the default target
     * @param maxGap maximum gap that may between case branches
     */
    public SWITCH(final int[] match, final InstructionHandle[] targets, final InstructionHandle target, final int maxGap) {
        this.match = match.clone();
        this.targets = targets.clone();
        if ((matchLength = match.length) < 2) {
            instruction = new TABLESWITCH(match, targets, target);
        } else {
            sort(0, matchLength - 1);
            if (matchIsOrdered(maxGap)) {
                fillup(maxGap, target);
                instruction = new TABLESWITCH(this.match, this.targets, target);
            } else {
                instruction = new LOOKUPSWITCH(this.match, this.targets, target);
            }
        }
    }

    private void fillup(final int maxGap, final InstructionHandle target) {
        final int maxSize = matchLength + matchLength * maxGap;
        final int[] mVec = new int[maxSize];
        final InstructionHandle[] tVec = new InstructionHandle[maxSize];
        int count = 1;
        mVec[0] = match[0];
        tVec[0] = targets[0];
        for (int i = 1; i < matchLength; i++) {
            final int prev = match[i - 1];
            final int gap = match[i] - prev;
            for (int j = 1; j < gap; j++) {
                mVec[count] = prev + j;
                tVec[count] = target;
                count++;
            }
            mVec[count] = match[i];
            tVec[count] = targets[i];
            count++;
        }
        match = Arrays.copyOf(mVec, count);
        targets = Arrays.copyOf(tVec, count);
    }

    public Instruction getInstruction() {
        return instruction;
    }

    @Override
    public InstructionList getInstructionList() {
        return new InstructionList(instruction);
    }

    /**
     * @return match is sorted in ascending order with no gap bigger than maxGap?
     */
    private boolean matchIsOrdered(final int maxGap) {
        return IntStream.range(1, matchLength).noneMatch(i -> match[i] - match[i - 1] > maxGap);
    }

    /**
     * Sort match and targets array with QuickSort.
     */
    private void sort(final int l, final int r) {
        int i = l;
        int j = r;
        int h;
        final int m = match[l + r >>> 1];
        InstructionHandle h2;
        do {
            while (match[i] < m) {
                i++;
            }
            while (m < match[j]) {
                j--;
            }
            if (i <= j) {
                h = match[i];
                match[i] = match[j];
                match[j] = h; // Swap elements
                h2 = targets[i];
                targets[i] = targets[j];
                targets[j] = h2; // Swap instructions, too
                i++;
                j--;
            }
        } while (i <= j);
        if (l < j) {
            sort(l, j);
        }
        if (i < r) {
            sort(i, r);
        }
    }
}
