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
package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TypeTestCase {
    @Test
    public void testBCEL243() {
        // expectedValue = "Ljava/util/Map<TX;Ljava/util/List<TY;>;>;";
        // The line commented out above is the correct expected value; however,
        // the constructor for ObjectType is yet another place where BCEL does
        // not understand generics so we need to substitute the modified value below.
        final String expectedValue = "Ljava/util/Map<X, java/util/List<Y>>;";
        final String actualValue = Type.getType("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;").getSignature();
        assertEquals(expectedValue, actualValue, "Type.getType");
    }

    @Test
    public void testInternalTypeNametoSignature() {
        assertEquals(null, Type.internalTypeNameToSignature(null));
        assertEquals("", Type.internalTypeNameToSignature(""));
        assertEquals("TT;", Type.internalTypeNameToSignature("TT;"));
        assertEquals("Ljava/lang/String;", Type.internalTypeNameToSignature("Ljava/lang/String;"));
        assertEquals("[Ljava/lang/String;", Type.internalTypeNameToSignature("[Ljava/lang/String;"));
        assertEquals("Ljava/lang/String;", Type.internalTypeNameToSignature("java/lang/String"));
        assertEquals("I", Type.internalTypeNameToSignature("I"));
        assertEquals("LT;", Type.internalTypeNameToSignature("T"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "java/io/Externalizable",
        "java/io/ObjectOutputStream",
        "java/io/Serializable",
        "java/lang/Cloneable",
        "java/lang/RuntimeException",
        "java/lang/String",
        "java/lang/System",
        "java/lang/Throwable",
        "java/net/URI",
        "java/sql/Statement",
        "java/util/ArrayList",
        "java/util/Calendar",
        "java/util/EnumMap",
        "java/util/HashSet",
        "java/util/Iterator",
        "java/util/LinkedList",
        "java/util/List",
        "java/util/Map",
        "java/util/concurrent/ConcurrentMap",
        "java/util/concurrent/ExecutorService",
        "org/apache/bcel/classfile/JavaClass",
        "org/apache/bcel/classfile/Method",
        "org/apache/bcel/classfile/Synthetic",
        "org/apache/bcel/generic/ConstantPoolGen",
        "org/apache/bcel/generic/MethodGen",
        "com/foo/Foo"})
    // @formatter:on
    public void testLDC(final String className) throws Exception {
        final JavaClass jc = Repository.lookupClass(className);
        final ConstantPoolGen cpg = new ConstantPoolGen(jc.getConstantPool());
        for (final Method method : jc.getMethods()) {
            final Code code = method.getCode();
            if (code != null) {
                final InstructionList instructionList = new InstructionList(code.getCode());
                for (final InstructionHandle instructionHandle : instructionList) {
                    instructionHandle.accept(new EmptyVisitor() {
                        @Override
                        public void visitLDC(final LDC obj) {
                            assertNotNull(obj.getValue(cpg));
                        }
                    });
                }
            }
        }
    }
}
