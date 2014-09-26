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
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

/** 
 * LOOKUPSWITCH - Switch with unordered set of values
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @see SWITCH
 */
public class LOOKUPSWITCH extends Select {

    private static final long serialVersionUID = -8263209830709467636L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    LOOKUPSWITCH() {
    }


    public LOOKUPSWITCH(int[] match, InstructionHandle[] targets, InstructionHandle defaultTarget) {
        super(org.apache.bcel.Constants.LOOKUPSWITCH, match, targets, defaultTarget);
        length = (short) (9 + match_length * 8); /* alignment remainder assumed
         * 0 here, until dump time. */
        fixed_length = length;
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        super.dump(out);
        out.writeInt(match_length); // npairs
        for (int i = 0; i < match_length; i++) {
            out.writeInt(match[i]); // match-offset pairs
            out.writeInt(indices[i] = getTargetOffset(targets[i]));
        }
    }


    /**
     * Read needed data (e.g. index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        super.initFromFile(bytes, wide); // reads padding
        match_length = bytes.readInt();
        fixed_length = (short) (9 + match_length * 8);
        length = (short) (fixed_length + padding);
        match = new int[match_length];
        indices = new int[match_length];
        targets = new InstructionHandle[match_length];
        for (int i = 0; i < match_length; i++) {
            match[i] = bytes.readInt();
            indices[i] = bytes.readInt();
        }
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        v.visitVariableLengthInstruction(this);
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitSelect(this);
        v.visitLOOKUPSWITCH(this);
    }
}
