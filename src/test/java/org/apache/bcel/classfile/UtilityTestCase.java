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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.junit.jupiter.api.Test;

public class UtilityTestCase {

    @Test
    public void testClearBit() {
        assertEquals(0, Utility.clearBit(0, 0));
        assertEquals(0, Utility.clearBit(1, 0), "1 bit 0 set to 0 -> 0");
        assertEquals(1, Utility.clearBit(1, 1), "1 bit 1 is 0 hence no change");
        assertEquals(8, Utility.clearBit(8, 4), "1000 only has 4 bit hence no change");
        assertEquals(1, Utility.clearBit(9, 3), "1001 bit 3 set to 0 -> 0001");
        assertEquals(-2, Utility.clearBit(-1, 0), "111...11 set bit 0 to 0 -> 111..10");
        assertEquals(0, Utility.clearBit(Integer.MIN_VALUE, 31), "100...00 set bit 31 to 0 -> 000..00");
    }

    @Test
    public void testCodeToString() throws Exception {
        final class CodeToString {
            int[][] a = {};

            CodeToString() {
                if (a instanceof int[][]) {
                    System.out.print(Arrays.asList(a).size());
                }
            }
        }
        final JavaClass javaClass = Repository.lookupClass(CodeToString.class);
        assertNotNull(javaClass);
        for (final Method method : javaClass.getMethods()) {
            assertEquals("<init>", method.getName());
            final String code = method.getCode().toString(false);
            assertTrue(code.contains("0:    aload_0"), code);
            assertTrue(code.contains("1:    aload_1"), code);
            assertTrue(code.contains("2:    putfield\t\torg.apache.bcel.classfile.UtilityTestCase$1CodeToString.this$0 Lorg/apache/bcel/classfile/UtilityTestCase;"), code);
            assertTrue(code.contains("5:    aload_0"), code);
            assertTrue(code.contains("6:    invokespecial\tjava.lang.Object.<init> ()V"), code);
            assertTrue(code.contains("9:    aload_0"), code);
            assertTrue(code.contains("10:   iconst_0"), code);
            assertTrue(code.contains("11:   iconst_0"), code);
            assertTrue(code.contains("12:   multianewarray\t<[[I>\t2"), code);
            assertTrue(code.contains("16:   putfield\t\torg.apache.bcel.classfile.UtilityTestCase$1CodeToString.a [[I"), code);
            assertTrue(code.contains("19:   aload_0"), code);
            assertTrue(code.contains("20:   getfield\t\torg.apache.bcel.classfile.UtilityTestCase$1CodeToString.a [[I"), code);
            assertTrue(code.contains("23:   instanceof\t<[[I>"), code);
            assertTrue(code.contains("26:   ifeq\t\t#47"), code);
            assertTrue(code.contains("29:   getstatic\t\tjava.lang.System.out Ljava/io/PrintStream;"), code);
            assertTrue(code.contains("32:   aload_0"), code);
            assertTrue(code.contains("33:   getfield\t\torg.apache.bcel.classfile.UtilityTestCase$1CodeToString.a [[I"), code);
            assertTrue(code.contains("36:   invokestatic\tjava.util.Arrays.asList ([Ljava/lang/Object;)Ljava/util/List;"), code);
            assertTrue(code.contains("39:   invokeinterface\tjava.util.List.size ()I1\t0"), code);
            assertTrue(code.contains("44:   invokevirtual\tjava.io.PrintStream.print (I)V"), code);
            assertTrue(code.contains("47:   return"), code);
        }
    }

    @Test
    public void testConvertString() {
        assertEquals("\\n", Utility.convertString("\n"));
        assertEquals("\\r", Utility.convertString("\r"));
        assertEquals("\\\"", Utility.convertString("\""));
        assertEquals("\\'", Utility.convertString("'"));
        assertEquals("\\\\", Utility.convertString("\\"));
        assertEquals("abc", Utility.convertString("abc"));
    }

    @Test
    public void testIsSet() {
        assertTrue(Utility.isSet(1, 0));
        assertTrue(Utility.isSet(7, 1));
        assertTrue(Utility.isSet(8, 3));
        assertTrue(Utility.isSet(9, 0));
        assertTrue(Utility.isSet(Integer.MIN_VALUE, 31));
        assertFalse(Utility.isSet(0, 0));
        assertFalse(Utility.isSet(8, 4));
        assertFalse(Utility.isSet(9, 1));
    }

    @Test
    public void testPrintArray() {
        assertEquals(null, Utility.printArray(null, false, false));
        assertEquals("", Utility.printArray(new Object[0], false, false));
        assertEquals("{}", Utility.printArray(new Object[0], true, false));
        assertEquals("null", Utility.printArray(new Object[]{null}, false, false));
        assertEquals("a, b", Utility.printArray(new String[]{"a", "b"}, false, false));
        assertEquals("{a, b}", Utility.printArray(new String[]{"a", "b"}, true, false));
        assertEquals("\"a\", \"b\"", Utility.printArray(new String[]{"a", "b"}, false, true));
        assertEquals("{\"a\", \"b\"}", Utility.printArray(new String[]{"a", "b"}, true, true));
    }

    @Test
    public void testSearchOpcode() {
        assertEquals(Const.ALOAD, Utility.searchOpcode("aload"));
        assertEquals(Const.NOP, Utility.searchOpcode("nop"));
        assertEquals(Const.BREAKPOINT, Utility.searchOpcode("breakpoint"));
        assertEquals(Const.IMPDEP2, Utility.searchOpcode("impdep2"));
        assertEquals(Const.I2D, Utility.searchOpcode("I2D"), "case insensitive");
        assertEquals(Const.UNDEFINED, Utility.searchOpcode("???"), "not found");
    }

    @Test
    public void testSetBit() {
        assertEquals(1, Utility.setBit(0, 0), "0 bit 0 set to 1 -> 1");
        assertEquals(1, Utility.setBit(1, 0), "1 bit 0 is 1 hence no change");
        assertEquals(3, Utility.setBit(1, 1), "1 bit 1 set to 1 -> 3");
        assertEquals(8, Utility.setBit(8, 3), "1000 bit 3 is 1 hence no change");
        assertEquals(9, Utility.setBit(1, 3), "0001 bit 3 set to 1 -> 1001");
        assertEquals(-1, Utility.setBit(-2, 0), "111...10 set bit 0 to 1 -> 111..11");
        assertEquals(Integer.MIN_VALUE, Utility.setBit(0, 31), "000...00 set bit 31 to 0 -> 100..00");
    }

    @Test
    public void testSignatureToStringWithGenerics() throws Exception {
        // tests for BCEL-197
        assertEquals("java.util.Map<X, java.util.List<Y>>", Utility.signatureToString("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;"), "generic signature");
        assertEquals("java.util.Set<? extends java.nio.file.OpenOption>", Utility.signatureToString("Ljava/util/Set<+Ljava/nio/file/OpenOption;>;"),
            "generic signature");
        assertEquals("java.nio.file.attribute.FileAttribute<?>[]", Utility.signatureToString("[Ljava/nio/file/attribute/FileAttribute<*>;"),
            "generic signature");

        // tests for BCEL-286
        assertEquals(
            "boofcv.alg.tracker.tld.TldTracker<boofcv.struct.image.ImageGray<boofcv.struct.image.GrayU8>, boofcv.struct.image.GrayI<boofcv.struct.image.GrayU8>>",
            Utility.signatureToString(
                "Lboofcv/alg/tracker/tld/TldTracker<Lboofcv/struct/image/ImageGray<Lboofcv/struct/image/GrayU8;>;Lboofcv/struct/image/GrayI<Lboofcv/struct/image/GrayU8;>;>;"),
            "generic signature");
        assertEquals("java.util.Map<?, ?>", Utility.signatureToString("Ljava/util/Map<**>;"), "generic signature");
        assertEquals("com.jme3.util.IntMap<T>.IntMapIterator", Utility.signatureToString("Lcom/jme3/util/IntMap<TT;>.IntMapIterator;"), "generic signature");

        // tests for BCEL-279
        assertEquals("<T extends java.lang.Object>(com.google.common.io.ByteProcessor<T>, int)T",
            Utility.signatureToString("<T:Ljava/lang/Object;>(Lcom/google/common/io/ByteProcessor<TT;>;I)TT;", false), "type parameters signature");
        assertEquals("<T extends Object>(com.google.common.io.ByteProcessor<T>, int)T",
            Utility.signatureToString("<T:Ljava/lang/Object;>(Lcom/google/common/io/ByteProcessor<TT;>;I)TT;", true), "type parameters signature");
        assertEquals("<M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member>(M)void",
            Utility.signatureToString("<M:Ljava/lang/reflect/AccessibleObject;:Ljava/lang/reflect/Member;>(TM;)V"), "type parameters signature");
        assertEquals("<K1 extends K, V1 extends V>()com.google.common.cache.Weigher<K1, V1>",
            Utility.signatureToString("<K1:TK;V1:TV;>()Lcom/google/common/cache/Weigher<TK1;TV1;>;"), "type parameters signature");
        assertEquals("<K1 extends K, V1 extends V>(com.google.common.cache.Weigher<? super K1, ? super V1>)com.google.common.cache.CacheBuilder<K1, V1>",
            Utility.signatureToString("<K1:TK;V1:TV;>(Lcom/google/common/cache/Weigher<-TK1;-TV1;>;)Lcom/google/common/cache/CacheBuilder<TK1;TV1;>;"),
            "type parameters signature");
        assertEquals("<N extends java.lang.Object, E extends java.lang.Object> extends java.lang.Object implements com.google.common.graph.Network<N, E>",
            Utility.signatureToString("<N:Ljava/lang/Object;E:Ljava/lang/Object;>Ljava/lang/Object;Lcom/google/common/graph/Network<TN;TE;>;", false),
            "class signature");
        assertEquals("<K extends Object, V extends Object> extends Object",
            Utility.signatureToString("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;"), "class signature");
    }
}
