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
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnonymousClassTestCase extends AbstractTestCase
{
    @Test
    public void testRegularClassIsNotAnonymous() throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.AnonymousClassTest");
        assertFalse("regular outer classes are not anonymous", clazz
                .isAnonymous());
        assertFalse("regular outer classes are not nested", clazz.isNested());
    }

    @Test
    public void testNamedInnerClassIsNotAnonymous()
            throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.AnonymousClassTest$X");
        assertFalse("regular inner classes are not anonymous", clazz
                .isAnonymous());
        assertTrue("regular inner classes are nested", clazz.isNested());
    }

    @Test
    public void testStaticInnerClassIsNotAnonymous()
            throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.AnonymousClassTest$Y");
        assertFalse("regular static inner classes are not anonymous", clazz
                .isAnonymous());
        assertTrue("regular static inner classes are nested", clazz.isNested());
    }

    @Test
    public void testAnonymousInnerClassIsAnonymous()
            throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.AnonymousClassTest$1");
        assertTrue("anonymous inner classes are anonymous", clazz.isAnonymous());
        assertTrue("anonymous inner classes are anonymous", clazz.isNested());
    }
}
