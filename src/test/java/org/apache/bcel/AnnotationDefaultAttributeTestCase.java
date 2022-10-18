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

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SimpleElementValue;
import org.junit.jupiter.api.Test;

public class AnnotationDefaultAttributeTestCase extends AbstractTestCase {
    /**
     * For values in an annotation that have default values, we should be able to query the AnnotationDefault attribute
     * against the method to discover the default value that was originally declared.
     */
    @Test
    public void testMethodAnnotations() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleAnnotation");
        final Method m = getMethod(clazz, "fruit");
        final AnnotationDefault a = (AnnotationDefault) findAttribute("AnnotationDefault", m.getAttributes());
        final SimpleElementValue val = (SimpleElementValue) a.getDefaultValue();
        assertEquals(ElementValue.STRING, val.getElementValueType(), "Wrong element value type");
        assertEquals("bananas", val.getValueString(), "Wrong default");
    }
}
