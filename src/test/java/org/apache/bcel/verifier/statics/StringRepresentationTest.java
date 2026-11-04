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

package org.apache.bcel.verifier.statics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Module;
import org.apache.bcel.classfile.ModuleMainClass;
import org.apache.bcel.classfile.ModulePackages;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringRepresentation} with the attributes of a module descriptor.
 * <p>
 * A module descriptor contains attributes and entries that a class file verifier reports when it finds something it does not know, so
 * {@link StringRepresentation} must be able to represent them instead of failing with an internal error.
 * </p>
 */
class StringRepresentationTest {

    private static final Path MODULE_INFO_PATH = Paths.get("src/test/resources/jpms/java17/commons-io/module-info.class");

    private static JavaClass parseModuleInfo() throws IOException {
        try (InputStream inputStream = Files.newInputStream(MODULE_INFO_PATH)) {
            return new ClassParser(inputStream, MODULE_INFO_PATH.toString()).parse();
        }
    }

    private static Attribute findAttribute(final JavaClass javaClass, final String name) {
        for (final Attribute attribute : javaClass.getAttributes()) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        fail("Missing attribute: " + name);
        return null;
    }

    @AfterEach
    void afterEach() {
        VerifierFactory.clear();
    }

    @Test
    void testModuleAttribute() throws IOException {
        final Attribute module = findAttribute(parseModuleInfo(), "Module");
        assertEquals(module.toString(), new StringRepresentation(module).toString());
    }

    @Test
    void testModuleEntries() throws IOException {
        final Module module = (Module) findAttribute(parseModuleInfo(), "Module");
        final List<Node> entries = new ArrayList<>();
        entries.addAll(Arrays.asList(module.getRequiresTable()));
        entries.addAll(Arrays.asList(module.getExportsTable()));
        entries.addAll(Arrays.asList(module.getOpensTable()));
        entries.addAll(Arrays.asList(module.getProvidesTable()));
        assertFalse(entries.isEmpty(), "The module descriptor is expected to have entries");
        for (final Node entry : entries) {
            assertEquals(entry.toString(), new StringRepresentation(entry).toString());
        }
    }

    @Test
    void testOtherModuleDescriptorAttributes() throws IOException {
        final JavaClass moduleInfo = parseModuleInfo();
        final ConstantPoolGen constantPoolGen = new ConstantPoolGen(moduleInfo.getConstantPool());
        final ConstantPool constantPool = constantPoolGen.getConstantPool();
        final ModulePackages modulePackages = new ModulePackages(constantPoolGen.addUtf8("ModulePackages"), 0,
            new int[] {constantPoolGen.addClass("org.apache.commons.io.IOUtils")}, constantPool);
        final ModuleMainClass moduleMainClass = new ModuleMainClass(constantPoolGen.addUtf8("ModuleMainClass"), 0, moduleInfo.getClassNameIndex(),
            constantPool);
        for (final Node node : Arrays.asList(modulePackages, moduleMainClass)) {
            assertNotNull(new StringRepresentation(node).toString(), node.getClass().getName());
        }
    }

    @Test
    void testVerifierOnAModuleDescriptor() throws IOException {
        final JavaClass moduleInfo = parseModuleInfo();
        Repository.addClass(moduleInfo);
        final Verifier verifier = VerifierFactory.getVerifier(moduleInfo.getClassName());
        assertNotNull(assertDoesNotThrow(verifier::doPass2));
    }
}
