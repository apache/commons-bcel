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


import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

/**
 * This class implements an array of local variables used for symbolic JVM
 * simulation.
 */
public class LocalVariables implements Cloneable {

    /** The Type[] containing the local variable slots. */
    private final Type[] locals;

    /**
     * Creates a new LocalVariables object.
     *
     * @param localVariableCount local variable count.
     */
    public LocalVariables(final int localVariableCount) {
        locals = new Type[localVariableCount];
        for (int i=0; i<localVariableCount; i++) {
            locals[i] = Type.UNKNOWN;
        }
    }

    /**
     * Returns a deep copy of this object; i.e. the clone
     * operates on a new local variable array.
     * However, the Type objects in the array are shared.
     */
    @Override
    public Object clone() {
        final LocalVariables lvs = new LocalVariables(locals.length);
        for (int i=0; i<locals.length; i++) {
            lvs.locals[i] = this.locals[i];
        }
        return lvs;
    }

    /**
     * Returns the type of the local variable slot index.
     *
     * @param slotIndex Slot to look up.
     * @return the type of the local variable slot index.
     */
    public Type get(final int slotIndex) {
        return locals[slotIndex];
    }

    /**
     * Returns a (correctly typed) clone of this object.
     * This is equivalent to ((LocalVariables) this.clone()).
     *
     * @return a (correctly typed) clone of this object.
     */
    public LocalVariables getClone() {
        return (LocalVariables) this.clone();
    }

    /**
     * Returns the number of local variable slots.
     *
     * @return the number of local variable slots.
     */
    public int maxLocals() {
        return locals.length;
    }

    /**
     * Sets a new Type for the given local variable slot.
     *
     * @param slotIndex Target slot index.
     * @param type Type to save at the given slot index.
     */
    public void set(final int slotIndex, final Type type) { // TODO could be package-protected?
        if (type == Type.BYTE || type == Type.SHORT || type == Type.BOOLEAN || type == Type.CHAR) {
            throw new AssertionViolatedException("LocalVariables do not know about '"+type+"'. Use Type.INT instead.");
        }
        locals[slotIndex] = type;
    }

    /** @return a hash code value for the object.
     */
    @Override
    public int hashCode() { return locals.length; }

    /*
     * Fulfills the general contract of Object.equals().
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof LocalVariables)) {
            return false;
        }
        final LocalVariables lv = (LocalVariables) o;
        if (this.locals.length != lv.locals.length) {
            return false;
        }
        for (int i=0; i<this.locals.length; i++) {
            if (!this.locals[i].equals(lv.locals[i])) {
                //System.out.println(this.locals[i]+" is not "+lv.locals[i]);
                return false;
            }
        }
        return true;
    }

    /**
     * Merges two local variables sets as described in the Java Virtual Machine Specification,
     * Second Edition, section 4.9.2, page 146.
     *
     * @param localVariable other local variable.
     */
    public void merge(final LocalVariables localVariable) {

        if (this.locals.length != localVariable.locals.length) {
            throw new AssertionViolatedException("Merging LocalVariables of different size?!? From different methods or what?!?");
        }

        for (int i=0; i<locals.length; i++) {
            merge(localVariable, i);
        }
    }

    /**
     * Merges a single local variable.
     *
     * @see #merge(LocalVariables)
     */
    private void merge(final LocalVariables lv, final int i) {
        try {

        // We won't accept an unitialized object if we know it was initialized;
        // compare vmspec2, 4.9.4, last paragraph.
        if ( (!(locals[i] instanceof UninitializedObjectType)) && (lv.locals[i] instanceof UninitializedObjectType) ) {
            throw new StructuralCodeConstraintException(
                "Backwards branch with an uninitialized object in the local variables detected.");
        }
        // Even harder, what about _different_ uninitialized object types?!
        if ( (!(locals[i].equals(lv.locals[i]))) && (locals[i] instanceof UninitializedObjectType) &&
                (lv.locals[i] instanceof UninitializedObjectType) ) {
            throw new StructuralCodeConstraintException(
                "Backwards branch with an uninitialized object in the local variables detected.");
        }
        // If we just didn't know that it was initialized, we have now learned.
        if (locals[i] instanceof UninitializedObjectType) {
            if (! (lv.locals[i] instanceof UninitializedObjectType)) {
                locals[i] = ((UninitializedObjectType) locals[i]).getInitialized();
            }
        }
        if ((locals[i] instanceof ReferenceType) && (lv.locals[i] instanceof ReferenceType)) {
            if (! locals[i].equals(lv.locals[i])) { // needed in case of two UninitializedObjectType instances
                final Type sup = ((ReferenceType) locals[i]).getFirstCommonSuperclass((ReferenceType) (lv.locals[i]));

                if (sup != null) {
                    locals[i] = sup;
                }
                else{
                    // We should have checked this in Pass2!
                    throw new AssertionViolatedException(
                        "Could not load all the super classes of '"+locals[i]+"' and '"+lv.locals[i]+"'.");
                }
            }
        }
        else{
            if (! (locals[i].equals(lv.locals[i])) ) {
/*TODO
                if ((locals[i] instanceof org.apache.bcel.generic.ReturnaddressType) &&
                    (lv.locals[i] instanceof org.apache.bcel.generic.ReturnaddressType)) {
                    //System.err.println("merging "+locals[i]+" and "+lv.locals[i]);
                    throw new AssertionViolatedException("Merging different ReturnAddresses: '"+locals[i]+"' and '"+lv.locals[i]+"'.");
                }
*/
                locals[i] = Type.UNKNOWN;
            }
        }
        } catch (final ClassNotFoundException e) {
        // FIXME: maybe not the best way to handle this
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Returns a String representation of this object.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<locals.length; i++) {
            sb.append(Integer.toString(i));
            sb.append(": ");
            sb.append(locals[i]);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Replaces all occurrences of {@code uninitializedObjectType} in this local variables set
     * with an "initialized" ObjectType.
     *
     * @param uninitializedObjectType the object to match.
     */
    public void initializeObject(final UninitializedObjectType uninitializedObjectType) {
        for (int i=0; i<locals.length; i++) {
            if (locals[i] == uninitializedObjectType) {
                locals[i] = uninitializedObjectType.getInitialized();
            }
        }
    }
}
