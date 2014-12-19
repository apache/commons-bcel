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
package org.apache.bcel.classfile;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @version $Id: ElementValue
 * @author <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 6.0
 */
public abstract class ElementValue
{
    protected int type; // TODO should be final

    protected ConstantPool cpool; // TODO should be final

    @Override
    public String toString()
    {
        return stringifyValue();
    }

    protected ElementValue(int type, ConstantPool cpool)
    {
        this.type = type;
        this.cpool = cpool;
    }

    public int getElementValueType()
    {
        return type;
    }

    public abstract String stringifyValue();

    public abstract void dump(DataOutputStream dos) throws IOException;

    public static final int STRING = 's';

    public static final int ENUM_CONSTANT = 'e';

    public static final int CLASS = 'c';

    public static final int ANNOTATION = '@';

    public static final int ARRAY = '[';

    public static final int PRIMITIVE_INT = 'I';

    public static final int PRIMITIVE_BYTE = 'B';

    public static final int PRIMITIVE_CHAR = 'C';

    public static final int PRIMITIVE_DOUBLE = 'D';

    public static final int PRIMITIVE_FLOAT = 'F';

    public static final int PRIMITIVE_LONG = 'J';

    public static final int PRIMITIVE_SHORT = 'S';

    public static final int PRIMITIVE_BOOLEAN = 'Z';

    public static ElementValue readElementValue(DataInput input,
            ConstantPool cpool) throws IOException
    {
        byte type = input.readByte();
        switch (type)
        {
        case 'B': // byte
            return new SimpleElementValue(PRIMITIVE_BYTE, input
                    .readUnsignedShort(), cpool);
        case 'C': // char
            return new SimpleElementValue(PRIMITIVE_CHAR, input
                    .readUnsignedShort(), cpool);
        case 'D': // double
            return new SimpleElementValue(PRIMITIVE_DOUBLE, input
                    .readUnsignedShort(), cpool);
        case 'F': // float
            return new SimpleElementValue(PRIMITIVE_FLOAT, input
                    .readUnsignedShort(), cpool);
        case 'I': // int
            return new SimpleElementValue(PRIMITIVE_INT, input
                    .readUnsignedShort(), cpool);
        case 'J': // long
            return new SimpleElementValue(PRIMITIVE_LONG, input
                    .readUnsignedShort(), cpool);
        case 'S': // short
            return new SimpleElementValue(PRIMITIVE_SHORT, input
                    .readUnsignedShort(), cpool);
        case 'Z': // boolean
            return new SimpleElementValue(PRIMITIVE_BOOLEAN, input
                    .readUnsignedShort(), cpool);
        case 's': // String
            return new SimpleElementValue(STRING, input.readUnsignedShort(),
                    cpool);
        case 'e': // Enum constant
            return new EnumElementValue(ENUM_CONSTANT, input.readUnsignedShort(),
                    input.readUnsignedShort(), cpool);
        case 'c': // Class
            return new ClassElementValue(CLASS, input.readUnsignedShort(), cpool);
        case '@': // Annotation
            // TODO isRuntimeVisible
            return new AnnotationElementValue(ANNOTATION, AnnotationEntry.read(
                    input, cpool, false), cpool);
        case '[': // Array
            int numArrayVals = input.readUnsignedShort();
            ElementValue[] evalues = new ElementValue[numArrayVals];
            for (int j = 0; j < numArrayVals; j++)
            {
                evalues[j] = ElementValue.readElementValue(input, cpool);
            }
            return new ArrayElementValue(ARRAY, evalues, cpool);
        default:
            throw new RuntimeException(
                    "Unexpected element value kind in annotation: " + type);
        }
    }

    public String toShortString()
    {
        return stringifyValue();
    }
}
