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



/**
 * This class represents a JVM execution frame; that means,
 * a local variable array and an operand stack.
 *
 */

public class Frame{

    /**
     * For instance initialization methods, it is important to remember
     * which instance it is that is not initialized yet. It will be
     * initialized invoking another constructor later.
     * NULL means the instance already *is* initialized.
     * @deprecated Use the getter/setter to access the field as it may
     * be made private in a later release
     */
    @Deprecated
    protected static UninitializedObjectType _this;

    /**
     *
     */
    private final LocalVariables locals;

    /**
     *
     */
    private final OperandStack stack;

    /**
     *
     */
    public Frame(final int maxLocals, final int maxStack) {
        locals = new LocalVariables(maxLocals);
        stack = new OperandStack(maxStack);
    }

    /**
     *
     */
    public Frame(final LocalVariables locals, final OperandStack stack) {
        this.locals = locals;
        this.stack = stack;
    }

    /**
     *
     */
    @Override
    protected Object clone() {
        final Frame f = new Frame(locals.getClone(), stack.getClone());
        return f;
    }

    /**
     *
     */
    public Frame getClone() {
        return (Frame) clone();
    }

    /**
     *
     */
    public LocalVariables getLocals() {
        return locals;
    }

    /**
     *
     */
    public OperandStack getStack() {
        return stack;
    }

    /** @return a hash code value for the object.
     */
    @Override
    public int hashCode() { return stack.hashCode() ^ locals.hashCode(); }

    /**
     *
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Frame)) {
            return false; // implies "null" is non-equal.
        }
        final Frame f = (Frame) o;
        return this.stack.equals(f.stack) && this.locals.equals(f.locals);
    }

    /**
     * Returns a String representation of the Frame instance.
     */
    @Override
    public String toString() {
        String s="Local Variables:\n";
        s += locals;
        s += "OperandStack:\n";
        s += stack;
        return s;
    }

    /**
     * @return the _this
     * @since 6.0
     */
    public static UninitializedObjectType getThis() {
        return _this;
    }

    /**
     * @param _this the _this to set
     * @since 6.0
     */
    public static void setThis(final UninitializedObjectType _this) {
        Frame._this = _this;
    }
}
