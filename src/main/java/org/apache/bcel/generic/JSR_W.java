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

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.util.ByteSequence;

/**
 * JSR_W - Jump to subroutine
 */
public class JSR_W extends JsrInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction. Not to be used otherwise.
     */
    JSR_W() {
    }

    public JSR_W(final InstructionHandle target) {
        super(org.apache.bcel.Const.JSR_W, target);
        super.setLength(5);
    }

    /**
     * Call corresponding visitor method(s). The order is: Call visitor methods of implemented interfaces first, then call
     * methods according to the class hierarchy in descending order, i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitStackProducer(this);
        v.visitBranchInstruction(this);
        v.visitJsrInstruction(this);
        v.visitJSR_W(this);
    }

    /**
     * Dump instruction as byte code to stream out.
     *
     * @param out Output stream
     */
    @Override
    public void dump(final DataOutputStream out) throws IOException {
        super.setIndex(getTargetOffset());
        out.writeByte(super.getOpcode());
        out.writeInt(super.getIndex());
    }

    /**
     * Reads needed data (for example index) from file.
     */
    @Override
    protected void initFromFile(final ByteSequence bytes, final boolean wide) throws IOException {
        super.setIndex(bytes.readInt());
        super.setLength(5);
    }
}
