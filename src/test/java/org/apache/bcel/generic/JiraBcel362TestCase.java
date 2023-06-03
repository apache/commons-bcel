/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

/**
 * Verify that a constant pool containing dynamic constants (condy) - in this case created by JaCoCo - can be read and
 * the corresponding constant is found after parsing.
 */
public class JiraBcel362TestCase extends AbstractTestCase {

    @Test
    public void testProcessConstantPoolWithCondyEntry() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass("issue362.Bcel362");
        final ConstantPoolGen constantPoolGen = assertDoesNotThrow(() -> new ConstantPoolGen(clazz.getConstantPool()));
        assertTrue(constantPoolGen.lookupUtf8("$jacocoData") != -1);
    }

}
