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
package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.bcel.AbstractTest;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.AnnotationElementValue;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.ParameterAnnotationEntry;
import org.junit.jupiter.api.Test;

/**
 * Checks that the runtime-visible flag is propagated when parameter annotations and nested annotation values are parsed
 * from a class file.
 */
class ParameterAnnotationVisibilityTest extends AbstractTest {

    private Method getConstructor(final JavaClass jc) {
        for (final Method method : jc.getMethods()) {
            if (Const.CONSTRUCTOR_NAME.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    private JavaClass loadTestClass() throws ClassNotFoundException {
        return getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedWithCombinedAnnotation");
    }

    @Test
    void testNestedAnnotationValueIsRuntimeVisible() throws ClassNotFoundException {
        final JavaClass jc = loadTestClass();
        boolean found = false;
        for (final AnnotationEntry outer : jc.getAnnotationEntries()) {
            for (final ElementValuePair pair : outer.getElementValuePairs()) {
                final ElementValue value = pair.getValue();
                if (value instanceof ArrayElementValue) {
                    for (final ElementValue inner : ((ArrayElementValue) value).getElementValuesArray()) {
                        if (inner instanceof AnnotationElementValue) {
                            found = true;
                            assertTrue(((AnnotationElementValue) inner).getAnnotationEntry().isRuntimeVisible(),
                                "nested annotation value parsed from a runtime-visible annotation reported as invisible");
                        }
                    }
                }
            }
        }
        assertTrue(found, "no nested annotation values found in test data");
    }

    @Test
    void testParameterAnnotationVisibilitySurvivesMethodGen() throws ClassNotFoundException {
        final JavaClass jc = loadTestClass();
        final Method init = getConstructor(jc);
        final ConstantPoolGen cp = new ConstantPoolGen(jc.getConstantPool());
        final MethodGen mg = new MethodGen(init, jc.getClassName(), cp);
        mg.getAnnotationsOnParameter(1); // unpack the existing parameter annotations before rebuilding
        final Method out = mg.getMethod();
        assertNotNull(out.getAttribute(Const.ATTR_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS),
            "runtime-visible parameter annotation dropped on MethodGen round-trip");
        assertNull(out.getAttribute(Const.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS),
            "runtime-visible parameter annotation downgraded to invisible on MethodGen round-trip");
    }

    @Test
    void testParsedParameterAnnotationIsRuntimeVisible() throws ClassNotFoundException {
        final JavaClass jc = loadTestClass();
        final Method init = getConstructor(jc);
        assertNotNull(init);
        boolean found = false;
        for (final ParameterAnnotationEntry pae : init.getParameterAnnotationEntries()) {
            for (final AnnotationEntry ae : pae.getAnnotationEntries()) {
                found = true;
                assertTrue(ae.isRuntimeVisible(), "runtime-visible parameter annotation parsed as invisible");
            }
        }
        assertTrue(found, "no parameter annotations found in test data");
    }
}
