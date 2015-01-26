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
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;

/**
 * @since 6.0
 */
public class ArrayElementValueGen extends ElementValueGen
{
    // J5TODO: Should we make this an array or a list? A list would be easier to
    // modify ...
    private final List<ElementValueGen> evalues;

    public ArrayElementValueGen(ConstantPoolGen cp)
    {
        super(ARRAY, cp);
        evalues = new ArrayList<ElementValueGen>();
    }

    public ArrayElementValueGen(int type, ElementValue[] datums,
            ConstantPoolGen cpool)
    {
        super(type, cpool);
        if (type != ARRAY) {
            throw new RuntimeException(
                    "Only element values of type array can be built with this ctor - type specified: " + type);
        }
        this.evalues = new ArrayList<ElementValueGen>();
        for (ElementValue datum : datums) {
            evalues.add(ElementValueGen.copy(datum, cpool, true));
        }
    }

    /**
     * Return immutable variant of this ArrayElementValueGen
     */
    @Override
    public ElementValue getElementValue()
    {
        ElementValue[] immutableData = new ElementValue[evalues.size()];
        int i = 0;
        for (ElementValueGen element : evalues) {
            immutableData[i++] = element.getElementValue();
        }
        return new ArrayElementValue(type, immutableData, cpGen
                .getConstantPool());
    }

    /**
     * @param value
     * @param cpool
     */
    public ArrayElementValueGen(ArrayElementValue value, ConstantPoolGen cpool,
            boolean copyPoolEntries)
    {
        super(ARRAY, cpool);
        evalues = new ArrayList<ElementValueGen>();
        ElementValue[] in = value.getElementValuesArray();
        for (ElementValue element : in) {
            evalues.add(ElementValueGen.copy(element, cpool, copyPoolEntries));
        }
    }

    @Override
    public void dump(DataOutputStream dos) throws IOException
    {
        dos.writeByte(type); // u1 type of value (ARRAY == '[')
        dos.writeShort(evalues.size());
        for (ElementValueGen element : evalues) {
            element.dump(dos);
        }
    }

    @Override
    public String stringifyValue()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String comma = "";
        for (ElementValueGen element : evalues) {
            sb.append(comma);
            comma = ",";
            sb.append(element.stringifyValue());
        }
        sb.append("]");
        return sb.toString();
    }

    public List<ElementValueGen> getElementValues()
    {
        return evalues;
    }

    public int getElementValuesSize()
    {
        return evalues.size();
    }

    public void addElement(ElementValueGen gen)
    {
        evalues.add(gen);
    }
}
