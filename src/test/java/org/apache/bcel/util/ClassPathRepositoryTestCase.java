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
package org.apache.bcel.util;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests {@link ClassPathRepository} and {@link MemorySensitiveClassPathRepository}.
 */
public class ClassPathRepositoryTestCase {

    private void verifyCaching(AbstractClassPathRepository repository) throws ClassNotFoundException {
        JavaClass class1 = repository.loadClass("java.lang.String");
        Assert.assertNotNull(class1);
        JavaClass class2 = repository.loadClass("java.lang.Long");
        Assert.assertNotNull(class2);

        Assert.assertEquals(class1, repository.findClass("java.lang.String"));
        Assert.assertEquals(class2, repository.findClass("java.lang.Long"));
    }

    @Test
    public void testClassPathRepository() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            verifyCaching(new ClassPathRepository(classPath));
        }
    }

    @Test
    public void testMemorySensitiveClassPathRepository() throws ClassNotFoundException, IOException {
        try (final ClassPath classPath = new ClassPath("")) {
            verifyCaching(new MemorySensitiveClassPathRepository(classPath));
        }
    }
}
