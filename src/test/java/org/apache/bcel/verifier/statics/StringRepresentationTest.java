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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Module;
import org.apache.bcel.classfile.ModuleMainClass;
import org.apache.bcel.classfile.ModulePackages;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.verifier.VerificationResult;
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

    private static Attribute findAttribute(final JavaClass javaClass, final String name) {
        for (final Attribute attribute : javaClass.getAttributes()) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        fail("Missing attribute: " + name);
        return null;
    }

    private static int findConstantIndex(final ConstantPool pool, final byte tag) {
        for (int i = 1; i < pool.getLength(); i++) {
            final Constant constant = pool.getConstant(i);
            if (constant != null && constant.getTag() == tag) {
                return i;
            }
        }
        fail("Missing constant tag: " + tag);
        return -1;
    }

    private static JavaClass parseModuleInfo() throws IOException {
        try (InputStream inputStream = Files.newInputStream(MODULE_INFO_PATH)) {
            return new ClassParser(inputStream, MODULE_INFO_PATH.toString()).parse();
        }
    }

    @AfterEach
    void afterEach() {
        VerifierFactory.clear();
    }

    @Test
    void testMalformedModulePackages() throws IOException {
        final JavaClass moduleInfo = parseModuleInfo();
        final ConstantPoolGen pool = new ConstantPoolGen(moduleInfo.getConstantPool());
        final int nameIndex = pool.addUtf8("ModulePackages");
        final ModulePackages modulePackages = new ModulePackages(nameIndex, 4, new int[] { moduleInfo.getClassNameIndex() }, pool.getConstantPool());
        assertThrows(ClassFormatException.class, modulePackages::toString);
        assertEquals("<<ModulePackages>>", new StringRepresentation(modulePackages).toString());
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
        assertTrue(module.getRequiresTable().length > 0);
        assertTrue(module.getExportsTable().length > 0);
        entries.addAll(Arrays.asList(module.getRequiresTable()));
        entries.addAll(Arrays.asList(module.getExportsTable()));
        assertFalse(entries.isEmpty(), "The module descriptor is expected to have entries");
        for (final Node entry : entries) {
            assertEquals(entry.toString(), new StringRepresentation(entry).toString());
        }
    }

    @Test
    void testModuleOpensAndProvides() throws IOException {
        final JavaClass moduleInfo = parseModuleInfo();
        final Module module = (Module) findAttribute(moduleInfo, "Module");
        final ConstantPoolGen pool = new ConstantPoolGen(moduleInfo.getConstantPool());
        final int serviceIndex = pool.addClass("example.Service");
        final int implementationIndex = pool.addClass("example.ServiceImpl");
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DataOutputStream output = new DataOutputStream(bytes)) {
            output.writeShort(module.getNameIndex());
            output.writeInt(28); // Module attribute body length.
            output.writeShort(findConstantIndex(moduleInfo.getConstantPool(), Const.CONSTANT_Module));
            output.writeShort(0); // module_flags
            output.writeShort(0); // module_version_index
            output.writeShort(0); // requires_count
            output.writeShort(0); // exports_count
            output.writeShort(1); // opens_count
            output.writeShort(findConstantIndex(moduleInfo.getConstantPool(), Const.CONSTANT_Package));
            output.writeShort(0); // opens_flags
            output.writeShort(0); // opens_to_count (unqualified opens)
            output.writeShort(0); // uses_count
            output.writeShort(1); // provides_count
            output.writeShort(serviceIndex);
            output.writeShort(1); // provides_with_count
            output.writeShort(implementationIndex);
        }
        // Parse attribute bytes rather than compiling module source, so this also runs on Java 8.
        final Module populated;
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            populated = (Module) Attribute.readAttribute(input, pool.getConstantPool());
        }
        assertEquals(1, populated.getOpensTable().length);
        assertEquals(1, populated.getProvidesTable().length);
        for (final Node entry : Arrays.asList(populated.getOpensTable()[0], populated.getProvidesTable()[0])) {
            assertEquals(entry.toString(), new StringRepresentation(entry).toString());
        }
    }

    @Test
    void testOtherModuleDescriptorAttributes() throws IOException {
        final JavaClass moduleInfo = parseModuleInfo();
        final ConstantPoolGen pool = new ConstantPoolGen(moduleInfo.getConstantPool());
        final int packagesNameIndex = pool.addUtf8("ModulePackages");
        final int mainClassNameIndex = pool.addUtf8("ModuleMainClass");
        final int mainClassIndex = pool.addClass("example.Main");
        final ConstantPool constantPool = pool.getConstantPool();
        final ModulePackages modulePackages = new ModulePackages(packagesNameIndex, 4,
                new int[] { findConstantIndex(moduleInfo.getConstantPool(), Const.CONSTANT_Package) }, constantPool);
        final ModuleMainClass moduleMainClass = new ModuleMainClass(mainClassNameIndex, 2, mainClassIndex, constantPool);
        for (final Node node : Arrays.asList(modulePackages, moduleMainClass)) {
            assertEquals(node.toString(), new StringRepresentation(node).toString());
        }
    }

    @Test
    void testVerifierOnAModuleDescriptor() throws IOException, ClassNotFoundException {
        final JavaClass moduleInfo = parseModuleInfo();
        final JavaClass previous = Repository.addClass(moduleInfo);
        try {
            final Verifier verifier = VerifierFactory.getVerifier(moduleInfo.getClassName());
            verifier.flush();
            final VerificationResult result = assertDoesNotThrow(verifier::doPass2);
            // Module verification is still unsupported; this change only fixes diagnostic rendering.
            assertEquals(VerificationResult.VERIFIED_REJECTED, result.getStatus());
            assertEquals("Superclass of 'module-info' missing but not java.lang.Object itself!", result.getMessage());
            assertTrue(Arrays.stream(verifier.getMessages())
                    .anyMatch(message -> message.contains("Module:") && message.contains("is unknown and will therefore be ignored")));
        } finally {
            Repository.removeClass(moduleInfo);
            if (previous != null) {
                Repository.addClass(previous);
            }
        }
    }
}
