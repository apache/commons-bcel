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
package org.apache.bcel.generic;

import java.util.Objects;

import org.apache.bcel.classfile.LineNumber;

/**
 * This class represents a line number within a method, that is, give an instruction a line number corresponding to the
 * source code line.
 *
 * @see LineNumber
 * @see MethodGen
 */
public class LineNumberGen implements InstructionTargeter, Cloneable {

    static final LineNumberGen[] EMPTY_ARRAY = {};

    private InstructionHandle ih;
    private int srcLine;

    /**
     * Create a line number.
     *
     * @param ih instruction handle to reference.
     * @param srcLine source line number.
     */
    public LineNumberGen(final InstructionHandle ih, final int srcLine) {
        setInstruction(ih);
        setSourceLine(srcLine);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Clone Not Supported", e); // never happens
        }
    }

    /**
     * @return true, if ih is target of this line number.
     */
    @Override
    public boolean containsTarget(final InstructionHandle ih) {
        return this.ih == ih;
    }

    /**
     * Gets the instruction handle.
     *
     * @return the instruction handle.
     */
    public InstructionHandle getInstruction() {
        return ih;
    }

    /**
     * Gets LineNumber attribute.
     *
     * This relies on that the instruction list has already been dumped to byte code or that the 'setPositions' methods
     * has been called for the instruction list.
     *
     * @return the line number attribute.
     */
    public LineNumber getLineNumber() {
        return new LineNumber(ih.getPosition(), srcLine);
    }

    /**
     * Gets the source line number.
     *
     * @return the source line number.
     */
    public int getSourceLine() {
        return srcLine;
    }

    /**
     * Sets the instruction handle.
     *
     * @param instructionHandle the instruction handle to set.
     */
    public void setInstruction(final InstructionHandle instructionHandle) { // TODO could be package-protected?
        Objects.requireNonNull(instructionHandle, "instructionHandle");
        BranchInstruction.notifyTarget(this.ih, instructionHandle, this);
        this.ih = instructionHandle;
    }

    /**
     * Sets the source line number.
     *
     * @param srcLine the source line number to set.
     */
    public void setSourceLine(final int srcLine) { // TODO could be package-protected?
        this.srcLine = srcLine;
    }

    /**
     * @param oldIh old target.
     * @param newIh new target.
     */
    @Override
    public void updateTarget(final InstructionHandle oldIh, final InstructionHandle newIh) {
        if (oldIh != ih) {
            throw new ClassGenException("Not targeting " + oldIh + ", but " + ih + "}");
        }
        setInstruction(newIh);
    }
}
