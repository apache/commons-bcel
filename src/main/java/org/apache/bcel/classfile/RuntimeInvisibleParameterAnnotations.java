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
import java.io.IOException;
import org.apache.bcel.Constants;

/**
 * Represents a parameter annotation that is represented in the class file
 * but is not provided to the JVM.
 * 
 * @version $Id: RuntimeInvisibleParameterAnnotations
 * @author  <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 6.0
 */
public class RuntimeInvisibleParameterAnnotations extends ParameterAnnotations {

    private static final long serialVersionUID = 270153155050617200L;
    
    /**
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     */
    public RuntimeInvisibleParameterAnnotations(int name_index, int length, DataInput input, ConstantPool constant_pool)
            throws IOException {
        super(Constants.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, name_index, length, input, constant_pool);
    }
}
