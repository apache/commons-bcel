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
 * an annotation's element value pair
 * 
 * @version $Id: ElementValuePair
 * @author  <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 */
public class ElementValuePair {

    private int element_name_index;
    private ElementValue value;


    /**
     * Construct object from file stream.
     * @param file Input stream
     * @param constant_pool the constant pool
     * @throws IOException
     */
    ElementValuePair(DataInputStream file, ConstantPool constant_pool) throws IOException {
        element_name_index = (file.readUnsignedShort());
        value = new ElementValue(file, constant_pool);
    }
}
