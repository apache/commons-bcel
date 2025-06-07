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

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.data.EmptyClass;
import org.junit.jupiter.api.Test;

public class AnonymousClassTest extends AbstractTest {
    @Test
    void testAnonymousInnerClassIsAnonymous() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnonymousClassTest$1");
        assertTrue(clazz.isAnonymous(), "anonymous inner classes are anonymous");
        assertTrue(clazz.isNested(), "anonymous inner classes are anonymous");
    }

    @Test
    void testNamedInnerClassIsNotAnonymous() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnonymousClassTest$X");
        assertFalse(clazz.isAnonymous(), "regular inner classes are not anonymous");
        assertTrue(clazz.isNested(), "regular inner classes are nested");
    }

    @Test
    void testRegularClassInnerClasses() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnonymousClassTest");
        final InnerClasses innerClasses = clazz.getAttribute(Const.ATTR_INNER_CLASSES);
        final InnerClass[] innerClassArray = innerClasses.getInnerClasses();
        assertEquals(3, innerClassArray.length);
        assertNull(Repository.lookupClass(EmptyClass.class).getAttribute(Const.ATTR_INNER_CLASSES));
    }

    @Test
    void testRegularClassIsNotAnonymous() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnonymousClassTest");
        assertFalse(clazz.isAnonymous(), "regular outer classes are not anonymous");
        assertFalse(clazz.isNested(), "regular outer classes are not nested");
    }

    @Test
    void testStaticInnerClassIsNotAnonymous() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnonymousClassTest$Y");
        assertFalse(clazz.isAnonymous(), "regular static inner classes are not anonymous");
        assertTrue(clazz.isNested(), "regular static inner classes are nested");
    }
}
