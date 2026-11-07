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

package org.apache.bcel.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.verifier.input.FieldVerifierChildClass;
import org.apache.bcel.verifier.input.StaticFieldVerifierChildClass;
import org.apache.commons.lang3.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class VerifierTest {

    private static File getJarFile(final Class<?> clazz) throws URISyntaxException {
        return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    private static void testDefaultMethodValidation(final String className, final String... excludes) throws ClassNotFoundException {
        if (Strings.CS.endsWithAny(className, excludes)) {
            return;
        }
        final Verifier verifier = VerifierFactory.getVerifier(className);
        VerificationResult result = verifier.doPass1();

        assertEquals(VerificationResult.VERIFIED_OK, result.getStatus(), "Pass 1 verification of " + className + " failed: " + result.getMessage());

        result = verifier.doPass2();

        assertEquals(VerificationResult.VERIFIED_OK, result.getStatus(), "Pass 2 verification of " + className + " failed: " + result.getMessage());

        if (result == VerificationResult.VR_OK) {
            final JavaClass jc = org.apache.bcel.Repository.lookupClass(className);
            for (int i = 0; i < jc.getMethods().length; i++) {
                result = verifier.doPass3a(i);
                assertEquals(VerificationResult.VR_OK, result, "Pass 3a, method number " + i + " ['" + jc.getMethods()[i] + "']:\n" + result);
                result = verifier.doPass3b(i);
                assertEquals(VerificationResult.VR_OK, result, "Pass 3b, method number " + i + " ['" + jc.getMethods()[i] + "']:\n" + result);
            }
        }
    }

    private static void testJarFile(final File file, final String... excludes) throws IOException, ClassNotFoundException {
        try (JarFile jarFile = new JarFile(file)) {
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(JavaClass.EXTENSION)) {
                    entryName = entryName.replaceFirst("\\.class$", "");
                    entryName = Utility.compactClassName(entryName, false);
                    testDefaultMethodValidation(entryName, excludes);
                }
            }
        }
    }

    @AfterEach
    public void afterEach() {
        VerifierFactory.clear();
    }

    @Test
    void testArrayUtils() throws ClassNotFoundException {
        testDefaultMethodValidation("org.apache.commons.lang.ArrayUtils");
    }

    @Test
    void testCollection() throws ClassNotFoundException {
        testDefaultMethodValidation("java.util.Collection");
    }

    @Test
    void testCommonsLang2() throws IOException, URISyntaxException, ClassNotFoundException {
        testJarFile(getJarFile(org.apache.commons.lang.StringUtils.class), "ArrayUtils", "SerializationUtils");
    }

    @Test
    void testDefinitionImpl() throws ClassNotFoundException {
        testDefaultMethodValidation("com.ibm.wsdl.DefinitionImpl");
    }

    @Test
    void testJvmOpCodes() throws ClassNotFoundException {
        testDefaultMethodValidation("org.apache.bcel.verifier.tests.JvmOpCodes");
    }

    @Test
    void testObjectInputStreamPass2() {
        // Exercise NestHost diagnostics without depending on JDK-specific bytecode and unsupported pass 3 instructions.
        final Verifier verifier = VerifierFactory.getVerifier("java.io.ObjectInputStream");
        assertEquals(VerificationResult.VR_OK, verifier.doPass1());
        assertEquals(VerificationResult.VR_OK, verifier.doPass2());
    }

    @Test
    void testPackagePrivateField() throws ClassNotFoundException {
        testDefaultMethodValidation(FieldVerifierChildClass.class.getName());
    }

    @Test
    void testPackagePrivateStaticField() throws ClassNotFoundException {
        testDefaultMethodValidation(StaticFieldVerifierChildClass.class.getName());
    }

    @Test
    void testWSDL() throws IOException, URISyntaxException, ClassNotFoundException {
        testJarFile(getJarFile(javax.wsdl.Port.class), "WSDLReaderImpl", "DefinitionImpl");
    }
}
