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
 */
package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.DataInput;
import java.util.Map;
import java.util.Optional;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Repository;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

/**
 *  Tests signatures, including primitives, generics, and bad signatures for exceptions.
 */
public class SignatureTestCase extends AbstractTestCase {

    @Test
    public void testBadSignatures() throws Exception {
        assertThrowsExactly(IllegalArgumentException.class, () -> Signature.translate("<"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Signature.translate("<>"));
    }

    @Test
    public void testMap() throws Exception {
        final JavaClass jc = Repository.lookupClass(Map.class);
        final Signature classSignature = (Signature) findAttribute("Signature", jc.getAttributes());
        final String translatedSignature = Signature.translate(classSignature.getSignature());
        assertEquals("<K, V>java.lang.Object", translatedSignature);
        testMethod(jc, "(java.lang.Object)V", Map.class, "get", Object.class);
        testMethod(jc, "(K, V)V", Map.class, "put", Object.class, Object.class);
    }

    void testMethod(final JavaClass jc, final String expected, final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws Exception {
        final Method method = jc.getMethod(clazz.getMethod(methodName, paramTypes));
        final String methodSignature = Optional.ofNullable(method.getGenericSignature()).orElse(method.getSignature());
        final String translatedMethodSignature = Signature.translate(methodSignature);
        assertEquals(expected, translatedMethodSignature);
    }

    @Test
    public void testString() throws Exception {
        final JavaClass jc = Repository.lookupClass(String.class);
        final Signature classSignature = (Signature) findAttribute("Signature", jc.getAttributes());
        final String translatedSignature = Signature.translate(classSignature.getSignature());
        assertEquals("java.lang.Object", translatedSignature);
        testMethod(jc, "()java.lang.String", String.class, "trim");

    }

    @Test
    public void testType() throws Exception {
        assertEquals("(I)I", Type.getSignature(Math.class.getMethod("abs", int.class)));
        assertEquals("(J)J", Type.getSignature(Math.class.getMethod("abs", long.class)));
        assertEquals("(D)D", Type.getSignature(Math.class.getMethod("abs", double.class)));
        assertEquals("(F)F", Type.getSignature(Math.class.getMethod("abs", float.class)));
        assertEquals("(Ljava/lang/String;)[Ljava/lang/String;", Type.getSignature(String.class.getMethod("split", String.class)));
        assertEquals("(I)C", Type.getSignature(String.class.getMethod("charAt", int.class)));
        assertEquals("()Ljava/lang/String;", Type.getSignature(String.class.getMethod("trim")));
        assertEquals("()V", Type.getSignature(Object.class.getMethod("notify")));
        assertEquals("(Ljava/lang/Object;)Z", Type.getSignature(Object.class.getMethod("equals", Object.class)));
        assertEquals("()B", Type.getSignature(DataInput.class.getMethod("readByte")));
        assertEquals("()S", Type.getSignature(DataInput.class.getMethod("readShort")));
    }
}
