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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.tests.TestFieldAccess01Creator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VerifierFieldAccessTest extends AbstractVerifierTest {

    private static final String FIELD_CLASS_NAME = TEST_PACKAGE + "TestFieldAccessOwner";
    private static final String SUBCLASS_NAME = TEST_PACKAGE + "TestFieldAccessSubclass";
    private static final ObjectType FIELD_CLASS_TYPE = ObjectType.getInstance(FIELD_CLASS_NAME);
    private static final ObjectType SUBCLASS_TYPE = ObjectType.getInstance(SUBCLASS_NAME);

    static Stream<Arguments> fieldAccessTypes() {
        return Stream.of(Const.GETFIELD, Const.PUTFIELD)
            .flatMap(opcode -> Stream.of(Type.INT, Type.LONG, Type.DOUBLE).map(fieldType -> Arguments.of(opcode, fieldType)));
    }

    private void assertFieldAccess(final short opcode, final Type fieldType, final String fieldClassName, final Type objectrefType, final boolean accepted)
        throws IOException, ClassNotFoundException {
        final ClassGen owner = new ClassGen(FIELD_CLASS_NAME, "java.lang.Object", "TestFieldAccessOwner.java", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        owner.addField(new FieldGen(Const.ACC_PUBLIC, fieldType, "x", owner.getConstantPool()).getField());
        Repository.addClass(owner.getJavaClass());
        final ClassGen subclass = new ClassGen(SUBCLASS_NAME, FIELD_CLASS_NAME, "TestFieldAccessSubclass.java", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        Repository.addClass(subclass.getJavaClass());

        final TestFieldAccess01Creator creator = new TestFieldAccess01Creator(opcode, fieldType, fieldClassName, objectrefType);
        creator.create();
        final String className = TEST_PACKAGE + creator.getSimpleClassName();
        final JavaClass javaClass = Repository.lookupClass(className);
        final Verifier verifier = VerifierFactory.getVerifier(className);
        assertEquals(VerificationResult.VR_OK, verifier.doPass1());
        assertEquals(VerificationResult.VR_OK, verifier.doPass2());
        final Method[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            assertEquals(VerificationResult.VR_OK, verifier.doPass3a(i));
            final boolean rejected = !accepted && "test".equals(methods[i].getName());
            final VerificationResult result = verifier.doPass3b(i);
            assertEquals(rejected ? VerificationResult.VERIFIED_REJECTED : VerificationResult.VERIFIED_OK, result.getStatus(), result::getMessage);
            if (rejected) {
                assertTrue(result.getMessage().contains("The 'objectref' item"), result::getMessage);
                assertTrue(result.getMessage().contains("is not an instance of '" + fieldClassName + "'"), result::getMessage);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testFieldAccessOnDeclaringClassObjectref(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, FIELD_CLASS_NAME, FIELD_CLASS_TYPE, true);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testFieldAccessOnNullObjectref(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, FIELD_CLASS_NAME, Type.NULL, true);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testFieldAccessOnSubclassObjectref(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, FIELD_CLASS_NAME, SUBCLASS_TYPE, true);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testFieldAccessOnUnrelatedObjectref(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, FIELD_CLASS_NAME, Type.STRING, false);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testInheritedFieldAccess(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, SUBCLASS_NAME, SUBCLASS_TYPE, true);
    }

    @ParameterizedTest
    @MethodSource("fieldAccessTypes")
    void testInheritedFieldAccessOnSuperclassObjectref(final short opcode, final Type fieldType) throws IOException, ClassNotFoundException {
        assertFieldAccess(opcode, fieldType, SUBCLASS_NAME, FIELD_CLASS_TYPE, false);
    }
}
