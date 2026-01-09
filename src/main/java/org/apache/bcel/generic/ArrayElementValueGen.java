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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;
import org.apache.commons.lang3.stream.Streams;

/**
 * Generates array element values in annotations.
 *
 * @since 6.0
 */
public class ArrayElementValueGen extends ElementValueGen {
    // J5TODO: Should we make this an array or a list? A list would be easier to
    // modify ...
    private final List<ElementValueGen> evalues;

    /**
     * Constructs an ArrayElementValueGen.
     *
     * @param value the array element value.
     * @param cpool the constant pool generator.
     * @param copyPoolEntries whether to copy pool entries.
     */
    public ArrayElementValueGen(final ArrayElementValue value, final ConstantPoolGen cpool, final boolean copyPoolEntries) {
        super(ARRAY, cpool);
        evalues = new ArrayList<>();
        final ElementValue[] in = value.getElementValuesArray();
        for (final ElementValue element : in) {
            evalues.add(copy(element, cpool, copyPoolEntries));
        }
    }

    /**
     * Constructs an ArrayElementValueGen.
     *
     * @param cp the constant pool generator.
     */
    public ArrayElementValueGen(final ConstantPoolGen cp) {
        super(ARRAY, cp);
        evalues = new ArrayList<>();
    }

    /**
     * Constructs an ArrayElementValueGen.
     *
     * @param type the type.
     * @param elementValues the element values.
     * @param cpool the constant pool generator.
     */
    public ArrayElementValueGen(final int type, final ElementValue[] elementValues, final ConstantPoolGen cpool) {
        super(type, cpool);
        if (type != ARRAY) {
            throw new IllegalArgumentException("Only element values of type array can be built with this ctor - type specified: " + type);
        }
        this.evalues = Streams.of(elementValues).map(e -> copy(e, cpool, true)).collect(Collectors.toList());
    }

    /**
     * Adds an element.
     *
     * @param gen the element value generator.
     */
    public void addElement(final ElementValueGen gen) {
        evalues.add(gen);
    }

    @Override
    public void dump(final DataOutputStream dos) throws IOException {
        dos.writeByte(super.getElementValueType()); // u1 type of value (ARRAY == '[')
        dos.writeShort(evalues.size());
        for (final ElementValueGen element : evalues) {
            element.dump(dos);
        }
    }

    /**
     * Return immutable variant of this ArrayElementValueGen.
     *
     * @return immutable variant of this ArrayElementValueGen.
     */
    @Override
    public ElementValue getElementValue() {
        final ElementValue[] immutableData = new ElementValue[evalues.size()];
        int i = 0;
        for (final ElementValueGen element : evalues) {
            immutableData[i++] = element.getElementValue();
        }
        return new ArrayElementValue(super.getElementValueType(), immutableData, getConstantPool().getConstantPool());
    }

    /**
     * Gets the element values.
     *
     * @return the element values.
     */
    public List<ElementValueGen> getElementValues() {
        return evalues;
    }

    /**
     * Gets the element values size.
     *
     * @return the element values size.
     */
    public int getElementValuesSize() {
        return evalues.size();
    }

    @Override
    public String stringifyValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        String comma = "";
        for (final ElementValueGen element : evalues) {
            sb.append(comma);
            comma = ",";
            sb.append(element.stringifyValue());
        }
        sb.append("]");
        return sb.toString();
    }
}
