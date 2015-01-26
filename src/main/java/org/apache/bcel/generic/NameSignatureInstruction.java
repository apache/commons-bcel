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

import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

/**
 * Super class for FieldOrMethod and INVOKEDYNAMIC, since they both have
 * names and signatures 
 *
 * @version $Id: FieldOrMethod.java 1481383 2013-05-11 17:34:32Z dbrosius $
 * @author  <A HREF="mailto:bill.pugh@gmail.com">Bill Pugh</A>
 * @since 6.0
 */
public abstract class NameSignatureInstruction extends CPInstruction {

    private static final long serialVersionUID = 1L;

    public NameSignatureInstruction() {
        super();
    }

    public NameSignatureInstruction(short opcode, int index) {
        super(opcode, index);
    }

    public ConstantNameAndType getNameAndType(ConstantPoolGen cpg) {
        ConstantPool cp = cpg.getConstantPool();
        ConstantCP cmr = (ConstantCP) cp.getConstant(index);
        return  (ConstantNameAndType) cp.getConstant(cmr.getNameAndTypeIndex());
    }
    /** @return signature of referenced method/field.
     */
    public String getSignature(ConstantPoolGen cpg) {
        ConstantPool cp = cpg.getConstantPool();
        ConstantNameAndType cnat = getNameAndType(cpg);
        return ((ConstantUtf8) cp.getConstant(cnat.getSignatureIndex())).getBytes();
    }

    /** @return name of referenced method/field.
     */
    public String getName(ConstantPoolGen cpg) {
        ConstantPool cp = cpg.getConstantPool();
        ConstantNameAndType cnat = getNameAndType(cpg);
        return ((ConstantUtf8) cp.getConstant(cnat.getNameIndex())).getBytes();
    }

}