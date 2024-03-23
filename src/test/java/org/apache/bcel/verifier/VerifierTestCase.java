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

package org.apache.bcel.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.NestHost;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.input.FieldVerifierChildClass;
import org.apache.bcel.verifier.input.StaticFieldVerifierChildClass;
import org.apache.bcel.verifier.statics.StringRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

public class VerifierTestCase {

    private static File getJarFile(final Class<?> clazz) throws URISyntaxException {
        return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    private static void testDefaultMethodValidation(final String className, final String... excludes) throws ClassNotFoundException {
        if (StringUtils.endsWithAny(className, excludes)) {
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

    private static void testNestHostWithJavaVersion(final String className) throws ClassNotFoundException {
        final String version = System.getProperty("java.version");
        assertNotNull(version);
        try {
            testDefaultMethodValidation(className);
            assertTrue(version.startsWith("1."));
        } catch (final AssertionViolatedException e) {
            assertFalse(version.startsWith("1."));
            final StringBuilder expectedMessage = new StringBuilder();
            expectedMessage.append("INTERNAL ERROR: Please adapt '");
            expectedMessage.append(StringRepresentation.class);
            expectedMessage.append("' to deal with objects of class '");
            expectedMessage.append(NestHost.class);
            expectedMessage.append("'.");
            assertEquals(expectedMessage.toString(), e.getCause().getMessage());
        }
    }

    @AfterEach
    public void afterEach() {
        VerifierFactory.clear();
    }

    @Test
    public void testArrayUtils() throws ClassNotFoundException {
        testNestHostWithJavaVersion("org.apache.commons.lang.ArrayUtils");
    }

    @Test
    public void testCollection() throws ClassNotFoundException {
        testDefaultMethodValidation("java.util.Collection");
    }

    @Test
    public void testCommonsLang2() throws IOException, URISyntaxException, ClassNotFoundException {
        testJarFile(getJarFile(org.apache.commons.lang.StringUtils.class), "ArrayUtils", "SerializationUtils");
    }

    @Test
    public void testDefinitionImpl() throws ClassNotFoundException {
        testNestHostWithJavaVersion("com.ibm.wsdl.DefinitionImpl");
    }

    @Test
    public void testJvmOpCodes() throws ClassNotFoundException {
        testDefaultMethodValidation("org.apache.bcel.verifier.tests.JvmOpCodes");
    }

    @Test
    @DisabledForJreRange(max = JRE.JAVA_8)
    public void testObjectInputStream() throws ClassNotFoundException {
        testNestHostWithJavaVersion("java.io.ObjectInputStream");
    }

    @Test
    @DisabledForJreRange(min = JRE.JAVA_9)
    public void testObjectInputStreamJDK8() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> testNestHostWithJavaVersion("java.io.ObjectInputStream"));
    }

    @Test
    public void testPackagePrivateField() throws ClassNotFoundException {
        testDefaultMethodValidation(FieldVerifierChildClass.class.getName());
    }

    @Test
    public void testPackagePrivateStaticField() throws ClassNotFoundException {
        testDefaultMethodValidation(StaticFieldVerifierChildClass.class.getName());
    }

    @Test
    public void testWSDL() throws IOException, URISyntaxException, ClassNotFoundException {
        testJarFile(getJarFile(javax.wsdl.Port.class), "WSDLReaderImpl", "DefinitionImpl");
    }
}
