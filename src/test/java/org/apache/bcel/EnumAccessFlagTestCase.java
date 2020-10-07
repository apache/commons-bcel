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
 *
 */

package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnumAccessFlagTestCase extends AbstractTestCase
{
    /**
     * An enumerated type, once compiled, should result in a class file that is
     * marked such that we can determine from the access flags (through BCEL)
     * that it was originally an enum type declaration.
     */
    @Test
    public void testEnumClassSaysItIs() throws ClassNotFoundException
    {
        JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.SimpleEnum");
        assertTrue(clazz.isEnum(),
                "Expected SimpleEnum class to say it was an enum - but it didn't !");
        clazz = getTestClass(PACKAGE_BASE_NAME+".data.SimpleClass");
        assertFalse(clazz.isEnum(),
                "Expected SimpleClass class to say it was not an enum - but it didn't !");
    }
}
