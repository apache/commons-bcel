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
package org.apache.bcel.verifier.statics;


import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

/**
 * A utility class holding the information about
 * the names and the types of the local variables in
 * a given method.
 */
public class LocalVariablesInfo{

    /** The information about the local variables is stored here. */
    private final LocalVariableInfo[] localVariableInfos;

    /** The constructor. */
    LocalVariablesInfo(final int max_locals) {
        localVariableInfos = new LocalVariableInfo[max_locals];
        for (int i=0; i<max_locals; i++) {
            localVariableInfos[i] = new LocalVariableInfo();
        }
    }

    /**
     * Returns the LocalVariableInfo for the given slot.
     *
     * @param slot Slot to query.
     * @return The LocalVariableInfo for the given slot.
     */
    public LocalVariableInfo getLocalVariableInfo(final int slot) {
        if (slot < 0 || slot >= localVariableInfos.length) {
            throw new AssertionViolatedException("Slot number for local variable information out of range.");
        }
        return localVariableInfos[slot];
    }

    /**
     * Adds information about the local variable in slot 'slot'. Automatically
     * adds information for slot+1 if 't' is Type.LONG or Type.DOUBLE.
     *
     * @param name variable name
     * @param startPc Range in which the variable is valid.
     * @param length length of ...
     * @param type variable type
     * @throws LocalVariableInfoInconsistentException if the new information conflicts
     *         with already gathered information.
     */
    public void add(final int slot, final String name, final int startPc, final int length, final Type type) throws LocalVariableInfoInconsistentException{
        // The add operation on LocalVariableInfo may throw the '...Inconsistent...' exception, we don't throw it explicitely here.

        if (slot < 0 || slot >= localVariableInfos.length) {
            throw new AssertionViolatedException("Slot number for local variable information out of range.");
        }

        localVariableInfos[slot].add(name, startPc, length, type);
        if (type == Type.LONG) {
            localVariableInfos[slot+1].add(name, startPc, length, LONG_Upper.theInstance());
        }
        if (type == Type.DOUBLE) {
            localVariableInfos[slot+1].add(name, startPc, length, DOUBLE_Upper.theInstance());
        }
    }
}
