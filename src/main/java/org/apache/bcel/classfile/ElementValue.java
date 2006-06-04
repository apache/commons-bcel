/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

import java.io.DataInputStream;
import java.io.IOException;

/**
 * an ElementValuePair's element value. This class will be broken out into
 * different subclasses. This is a temporary implementation.
 * 
 * @version $Id: ElementValue
 * @author  <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 5.2
 */
public class ElementValue {

    private byte tag;
    private int const_value_index;
    private int type_name_index;
    private int const_name_index;
    private int class_info_index;
    private AnnotationEntry annotation;
    private int num_values;
    private ElementValue[] values;


    /**
     * Construct object from file stream.
     * @param file Input stream
     * @param constant_pool the constant pool
     * @throws IOException
     */
    ElementValue(DataInputStream file, ConstantPool constant_pool) throws IOException {
        tag = (file.readByte());
        switch (tag) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's':
                const_value_index = (file.readUnsignedShort());
                break;
            case 'e':
                type_name_index = (file.readUnsignedShort());
                const_name_index = (file.readUnsignedShort());
                break;
            case 'c':
                class_info_index = (file.readUnsignedShort());
                break;
            case '@':
                annotation = new AnnotationEntry(file, constant_pool);
                break;
            case '[':
                num_values = (file.readUnsignedShort());
                values = new ElementValue[num_values];
                for (int i = 0; i < num_values; i++) {
                    values[i] = new ElementValue(file, constant_pool);
                }
                break;
            default:
                throw new IOException("Invalid ElementValue tag: " + tag);
        }
    }
}
