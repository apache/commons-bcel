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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.AbstractTest;
import org.apache.bcel.verifier.statics.StringRepresentation;
import org.junit.jupiter.api.Test;

class PermittedSubclassesTest extends AbstractTest {

    private static final String SEALED_JAR_PATH = "src/test/resources/sealed/sealed-demo-jdk21.0.8.jar";

    private static final String SHAPE_CLASS_ENTRY = "org/jd/core/v1/SealedDemo$Shape.class";

    private JavaClass parseJarClass(final String entryName) throws IOException {
        try (JarFile jar = new JarFile(SEALED_JAR_PATH)) {
            final JarEntry entry = jar.getJarEntry(entryName);
            assertNotNull(entry, "Missing jar entry: " + entryName);
            try (InputStream inputStream = jar.getInputStream(entry)) {
                return new ClassParser(inputStream, entryName).parse();
            }
        }
    }

    @Test
    void readsPermittedSubclassesAttribute() throws IOException {
        final JavaClass clazz = parseJarClass(SHAPE_CLASS_ENTRY);
        final Attribute[] attributes = findAttribute("PermittedSubclasses", clazz);
        assertEquals(1, attributes.length, "Expected one PermittedSubclasses attribute");
        final PermittedSubclasses permittedSubclasses = (PermittedSubclasses) attributes[0];
        final List<String> classNames = Arrays.asList(permittedSubclasses.getClassNames());
        assertEquals(2, classNames.size(), "Expected two permitted subclasses");
        assertTrue(classNames.contains("org.jd.core.v1.SealedDemo$Circle"), "Missing permitted subclass Circle");
        assertTrue(classNames.contains("org.jd.core.v1.SealedDemo$Rectangle"), "Missing permitted subclass Rectangle");
    }

    @Test
    void stringRepresentationHandlesPermittedSubclasses() throws IOException {
        final JavaClass clazz = parseJarClass(SHAPE_CLASS_ENTRY);
        final Attribute[] attributes = findAttribute("PermittedSubclasses", clazz);
        final PermittedSubclasses permittedSubclasses = (PermittedSubclasses) attributes[0];
        assertEquals(permittedSubclasses.toString(), new StringRepresentation(permittedSubclasses).toString());
    }
}
