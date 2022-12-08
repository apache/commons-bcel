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

package org.apache.bcel.classfile;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Repository;
import org.apache.bcel.data.AnnotatedFields;
import org.apache.bcel.data.AnnotatedWithCombinedAnnotation;
import org.apache.bcel.data.AnnotationEnumElement;
import org.apache.bcel.data.SimpleEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldOrMethodTestCase extends AbstractTestCase {

    @Test
    public void testDeclaringClass() throws ClassNotFoundException {
        for (Field field : Repository.lookupClass(AnnotatedFields.class).getFields()) {
            assertEquals(AnnotatedFields.class.getName(), field.getDeclaringClassName());
        }
        for (Field field : Repository.lookupClass(SimpleEnum.class).getFields()) {
            assertEquals(SimpleEnum.class.getName(), field.getDeclaringClassName());
        }
        for (Method method : Repository.lookupClass(AnnotatedWithCombinedAnnotation.class).getMethods()) {
            assertEquals(AnnotatedWithCombinedAnnotation.class.getName(), method.getDeclaringClassName());
        }
        for (Method method : Repository.lookupClass(AnnotationEnumElement.class).getMethods()) {
            assertEquals(AnnotationEnumElement.class.getName(), method.getDeclaringClassName());
        }
    }
}
