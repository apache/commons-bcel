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

/** 
 * IRETURN -  Return int from method
 * <PRE>Stack: ..., value -&gt; &lt;empty&gt;</PRE>
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class IRETURN extends ReturnInstruction {

    private static final long serialVersionUID = 4067606299818510584L;


    /** Return int from method
     */
    public IRETURN() {
        super(org.apache.bcel.Constants.IRETURN);
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
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitReturnInstruction(this);
        v.visitIRETURN(this);
    }
}
