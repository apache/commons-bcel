/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.verifier.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

/**
 * A utility class holding the information about the name and the type of a local variable in a given slot (== index).
 * This information often changes in course of byte code offsets.
 */
public class LocalVariableInfo {

    /**
     * A contiguous, inclusive range of bytecode offsets sharing one variable name and one type.
     */
    private static final class Range {
        private final int start;
        private final int end; // inclusive
        private final String name;
        private final Type type;

        Range(final int start, final int end, final String name, final Type type) {
            this.start = start;
            this.end = end;
            this.name = name;
            this.type = type;
        }
    }

    /**
     * The database of ranges, keyed by their start offset. Invariant: the stored ranges never overlap each other; additions overlapping an existing range
     * with consistent information are coalesced into it, inconsistent ones are rejected. Storing ranges instead of one entry per offset keeps the work and
     * memory proportional to the number of LocalVariableTable entries: the startPc and length fields are attacker-controlled in a malicious class file and
     * would otherwise amplify each 10-byte table entry into up to 65,536 hashtable operations (CWE-407).
     */
    private final NavigableMap<Integer, Range> ranges = new TreeMap<>();

    /**
     * Constructs a new LocalVariableInfo.
     */
    public LocalVariableInfo() {
    }

    /**
     * Adds some information about this local variable (slot).
     *
     * @param name variable name.
     * @param startPc Range in which the variable is valid.
     * @param length length of ...
     * @param type variable type.
     * @throws LocalVariableInfoInconsistentException Thrown if the new information conflicts with already gathered information.
     */
    public void add(final String name, final int startPc, final int length, final Type type) throws LocalVariableInfoInconsistentException {
        final int endPc = startPc + length; // incl/incl-notation!
        int mergedStart = startPc;
        int mergedEnd = endPc;
        // Only ranges starting at or before endPc can overlap [startPc, endPc]; since stored ranges never overlap each other, the first candidate is the
        // last range starting at or before startPc.
        Integer from = ranges.floorKey(startPc);
        if (from == null) {
            from = Integer.valueOf(startPc);
        }
        final List<Integer> merged = new ArrayList<>();
        for (final Map.Entry<Integer, Range> entry : ranges.subMap(from, true, Integer.valueOf(endPc), true).entrySet()) {
            final Range range = entry.getValue();
            if (range.end < startPc) {
                continue; // does not overlap.
            }
            final int offset = Math.max(startPc, range.start);
            if (!range.name.equals(name)) {
                throw new LocalVariableInfoInconsistentException(
                    "At bytecode offset '" + offset + "' a local variable has two different names: '" + range.name + "' and '" + name + "'.");
            }
            if (!range.type.equals(type)) {
                throw new LocalVariableInfoInconsistentException(
                    "At bytecode offset '" + offset + "' a local variable has two different types: '" + range.type + "' and '" + type + "'.");
            }
            // Consistent overlap: coalesce, so the database stays proportional to the number of disjoint ranges.
            mergedStart = Math.min(mergedStart, range.start);
            mergedEnd = Math.max(mergedEnd, range.end);
            merged.add(entry.getKey());
        }
        merged.forEach(ranges::remove);
        ranges.put(Integer.valueOf(mergedStart), new Range(mergedStart, mergedEnd, name, type));
    }

    /**
     * Returns the name of the local variable that uses this local variable slot at the given bytecode offset. Care for
     * legal bytecode offsets yourself, otherwise the return value might be wrong. May return 'null' if nothing is known
     * about the type of this local variable slot at the given bytecode offset.
     *
     * @param offset bytecode offset.
     * @return The name of the local variable that uses this local variable slot at the given bytecode offset.
     */
    public String getName(final int offset) {
        final Range range = lookup(offset);
        return range != null ? range.name : null;
    }

    /**
     * Returns the type of the local variable that uses this local variable slot at the given bytecode offset. Care for
     * legal bytecode offsets yourself, otherwise the return value might be wrong. May return 'null' if nothing is known
     * about the type of this local variable slot at the given bytecode offset.
     *
     * @param offset bytecode offset.
     * @return The type of the local variable that uses this local variable slot at the given bytecode offset.
     */
    public Type getType(final int offset) {
        final Range range = lookup(offset);
        return range != null ? range.type : null;
    }

    /**
     * Returns the range covering the given bytecode offset, or {@code null} if no range covers it. Since the stored ranges never overlap, only the range
     * with the greatest start offset at or below the given offset can cover it.
     */
    private Range lookup(final int offset) {
        final Map.Entry<Integer, Range> entry = ranges.floorEntry(Integer.valueOf(offset));
        return entry != null && entry.getValue().end >= offset ? entry.getValue() : null;
    }
}
