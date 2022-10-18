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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EnclosingMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.Test;

public class EnclosingMethodAttributeTestCase extends AbstractTestCase {
    /**
     * Check that we can save and load the attribute correctly.
     */
    @Test
    public void testAttributeSerializtion() throws ClassNotFoundException, IOException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AttributeTestClassEM02$1");
        final ConstantPool pool = clazz.getConstantPool();
        final Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
        assertEquals(1, encMethodAttrs.length, "Wrong number of EnclosingMethod attributes");
        // Write it out
        final File tfile = createTestdataFile("AttributeTestClassEM02$1.class");
        clazz.dump(tfile);
        // Read in the new version and check it is OK
        final SyntheticRepository repos2 = createRepos(".");
        final JavaClass clazz2 = repos2.loadClass("AttributeTestClassEM02$1");
        assertNotNull(clazz2); // Use the variable to avoid a warning
        final EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
        final String enclosingClassName = em.getEnclosingClass().getBytes(pool);
        assertEquals(em.getEnclosingMethodIndex(), 0, "The class is not within a method, so method_index should be null");
        assertEquals(PACKAGE_BASE_SIG + "/data/AttributeTestClassEM02", enclosingClassName, "Wrong class name");
        tfile.deleteOnExit();
    }

    /**
     * Verify for an inner class declared at the type level that the EnclosingMethod attribute is set correctly (i.e. to a
     * null value)
     */
    @Test
    public void testCheckClassLevelNamedInnerClass() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AttributeTestClassEM02$1");
        final ConstantPool pool = clazz.getConstantPool();
        final Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
        assertEquals(1, encMethodAttrs.length, "Expected 1 EnclosingMethod attribute but found " + encMethodAttrs.length);
        final EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
        final String enclosingClassName = em.getEnclosingClass().getBytes(pool);
        assertEquals(em.getEnclosingMethodIndex(), 0, "The class is not within a method, so method_index should be null");
        assertEquals(PACKAGE_BASE_SIG + "/data/AttributeTestClassEM02", enclosingClassName, "Wrong class name");
    }

    /**
     * Verify for an inner class declared inside the 'main' method that the enclosing method attribute is set correctly.
     */
    @Test
    public void testCheckMethodLevelNamedInnerClass() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AttributeTestClassEM01$1S");
        final ConstantPool pool = clazz.getConstantPool();
        final Attribute[] encMethodAttrs = findAttribute("EnclosingMethod", clazz);
        assertEquals(1, encMethodAttrs.length, "Wrong number of EnclosingMethod attributes");
        final EnclosingMethod em = (EnclosingMethod) encMethodAttrs[0];
        final String enclosingClassName = em.getEnclosingClass().getBytes(pool);
        final String enclosingMethodName = em.getEnclosingMethod().getName(pool);
        assertEquals(PACKAGE_BASE_SIG + "/data/AttributeTestClassEM01", enclosingClassName, "Wrong class name");
        assertEquals(enclosingMethodName, "main", "Wrong method name");
    }
}
