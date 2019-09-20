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


import java.util.Hashtable;

import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

/**
 * A utility class holding the information about
 * the name and the type of a local variable in
 * a given slot (== index). This information
 * often changes in course of byte code offsets.
 */
public class LocalVariableInfo{

    /** The types database. KEY: String representing the offset integer. */
    private final Hashtable<String, Type> types = new Hashtable<>();

    /** The names database. KEY: String representing the offset integer. */
    private final Hashtable<String, String> names = new Hashtable<>();

    /**
     * Adds a name of a local variable and a certain slot to our 'names'
     * (Hashtable) database.
     */
    private void setName(final int offset, final String name) {
        names.put(Integer.toString(offset), name);
    }

    /**
     * Adds a type of a local variable and a certain slot to our 'types'
     * (Hashtable) database.
     */
    private void setType(final int offset, final Type t) {
        types.put(Integer.toString(offset), t);
    }

    /**
     * Returns the type of the local variable that uses this local variable slot at the given bytecode offset. Care for
     * legal bytecode offsets yourself, otherwise the return value might be wrong. May return 'null' if nothing is known
     * about the type of this local variable slot at the given bytecode offset.
     *
     * @param offset bytecode offset.
     * @return the type of the local variable that uses this local variable slot at the given bytecode offset.
     */
    public Type getType(final int offset) {
        return types.get(Integer.toString(offset));
    }

    /**
     * Returns the name of the local variable that uses this local variable slot at the given bytecode offset. Care for
     * legal bytecode offsets yourself, otherwise the return value might be wrong. May return 'null' if nothing is known
     * about the type of this local variable slot at the given bytecode offset.
     *
     * @param offset bytecode offset.
     * @return the name of the local variable that uses this local variable slot at the given bytecode offset.
     */
    public String getName(final int offset) {
        return names.get(Integer.toString(offset));
    }

    /**
     * Adds some information about this local variable (slot).
     *
     * @param name variable name
     * @param startPc Range in which the variable is valid.
     * @param length length of ...
     * @param type variable type
     *
     * @throws LocalVariableInfoInconsistentException if the new information conflicts
     *         with already gathered information.
     */
    public void add(final String name, final int startPc, final int length, final Type type)
            throws LocalVariableInfoInconsistentException {
        for (int i = startPc; i <= startPc + length; i++) { // incl/incl-notation!
            add(i, name, type);
        }
    }

    /**
     * Adds information about name and type for a given offset.
     *
     * @throws LocalVariableInfoInconsistentException if the new information conflicts
     *         with already gathered information.
     */
    private void add(final int offset, final String name, final Type t) throws LocalVariableInfoInconsistentException {
        if (getName(offset) != null) {
            if (!getName(offset).equals(name)) {
                throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset
                        + "' a local variable has two different names: '" + getName(offset) + "' and '" + name + "'.");
            }
        }
        if (getType(offset) != null) {
            if (!getType(offset).equals(t)) {
                throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset
                        + "' a local variable has two different types: '" + getType(offset) + "' and '" + t + "'.");
            }
        }
        setName(offset, name);
        setType(offset, t);
    }
}
