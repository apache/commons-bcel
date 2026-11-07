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

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

class VerifierInvokeTest extends AbstractVerifierTest {

    private void assertInvokeVirtual(final String owner, final String name, final Type returnType, final Type[] argumentTypes,
        final boolean accepted) throws ClassNotFoundException {
        final String className = TEST_PACKAGE + "SignaturePolymorphicCaller";
        final ClassGen classGen = new ClassGen(className, "java.lang.Object", "SignaturePolymorphicCaller.java", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        final ConstantPoolGen constantPool = classGen.getConstantPool();
        final InstructionFactory factory = new InstructionFactory(classGen);
        final InstructionList instructions = new InstructionList();
        final Type[] parameters = new Type[argumentTypes.length + 1];
        parameters[0] = new ObjectType(owner);
        System.arraycopy(argumentTypes, 0, parameters, 1, argumentTypes.length);
        int slot = 0;
        for (final Type parameter : parameters) {
            instructions.append(InstructionFactory.createLoad(parameter, slot));
            slot += parameter.getSize();
        }
        instructions.append(factory.createInvoke(owner, name, returnType, argumentTypes, Const.INVOKEVIRTUAL));
        instructions.append(InstructionFactory.createReturn(returnType));
        final MethodGen method = new MethodGen(Const.ACC_PUBLIC | Const.ACC_STATIC, returnType, parameters, null, "call", className,
            instructions, constantPool);
        method.setMaxStack();
        method.setMaxLocals();
        classGen.addMethod(method.getMethod());
        Repository.addClass(classGen.getJavaClass());
        VerifierFactory.clear();
        try {
            final Verifier verifier = VerifierFactory.getVerifier(className);
            assertEquals(VerificationResult.VR_OK, verifier.doPass1());
            assertEquals(VerificationResult.VR_OK, verifier.doPass2());
            final VerificationResult result = verifier.doPass3a(0);
            assertEquals(accepted ? VerificationResult.VERIFIED_OK : VerificationResult.VERIFIED_REJECTED, result.getStatus(), result::getMessage);
            if (accepted) {
                assertEquals(VerificationResult.VR_OK, verifier.doPass3b(0));
            }
        } finally {
            instructions.dispose();
            Repository.removeClass(className);
            VerifierFactory.clear();
        }
    }

    @Test
    void testMethodHandleSignatures() throws ClassNotFoundException {
        for (final String name : new String[] { "invoke", "invokeExact" }) {
            assertInvokeVirtual("java.lang.invoke.MethodHandle", name, Type.OBJECT,
                new Type[] { new ArrayType(Type.BYTE, 1), new ArrayType(Type.OBJECT, 1) }, true);
            assertInvokeVirtual("java.lang.invoke.MethodHandle", name, Type.LONG, new Type[] { Type.LONG, Type.DOUBLE }, true);
            assertInvokeVirtual("java.lang.invoke.MethodHandle", name, Type.VOID, Type.NO_ARGS, true);
        }
    }

    @Test
    void testOrdinaryMethodSignatures() throws ClassNotFoundException {
        assertInvokeVirtual("java.lang.invoke.MethodHandle", "invokeWithArguments", Type.OBJECT, new Type[] { Type.INT }, false);
        assertInvokeVirtual("java.lang.invoke.MethodHandle", "missingMethod", Type.OBJECT, Type.NO_ARGS, false);
        assertInvokeVirtual("java.lang.Object", "toString", Type.INT, Type.NO_ARGS, false);
    }

    @Test
    @DisabledForJreRange(max = JRE.JAVA_8)
    void testVarHandleSignatures() throws ClassNotFoundException {
        assertInvokeVirtual("java.lang.invoke.VarHandle", "get", Type.INT, new Type[] { Type.OBJECT }, true);
        assertInvokeVirtual("java.lang.invoke.VarHandle", "set", Type.VOID, new Type[] { Type.OBJECT, Type.INT }, true);
        assertInvokeVirtual("java.lang.invoke.VarHandle", "compareAndSet", Type.BOOLEAN, new Type[] { Type.OBJECT, Type.INT, Type.INT }, true);
        assertInvokeVirtual("java.lang.invoke.VarHandle", "toString", Type.INT, Type.NO_ARGS, false);
    }

    @Test
    void testLegalInvokeInterface() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeInterface01", "Verification of invokeinterface on a method defined in a superinterface must pass.");
    }

    @Test
    void testLegalInvokeSpecial() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeSpecial01", "Verification of invokespecial on a method defined in a superclass must pass.");
        assertVerifyOK("TestLegalInvokeSpecial02", "Verification of invokespecial on a method defined in a superclass must pass.");
    }

    @Test
    void testLegalInvokeStatic() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeStatic01", "Verification of invokestatic on a method defined in a superclass must pass.");
    }

    @Test
    void testLegalInvokeVirtual() throws ClassNotFoundException {
        assertVerifyOK("TestLegalInvokeVirtual01", "Verification of invokevirtual on a method defined in a superclass must pass.");
        assertVerifyOK("TestLegalInvokeVirtual02", "Verification of invokevirtual on a method defined in a superinterface must pass.");
    }
}
