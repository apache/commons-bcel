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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityTestCase {

    @Test
    public void testSignatureToStringWithGenerics() throws Exception {
    // tests for BCEL-197
        assertEquals("java.util.Map<X, java.util.List<Y>>",
                Utility.signatureToString("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;"),
                "generic signature");
        assertEquals("java.util.Set<? extends java.nio.file.OpenOption>",
                Utility.signatureToString("Ljava/util/Set<+Ljava/nio/file/OpenOption;>;"),
                "generic signature");
        assertEquals("java.nio.file.attribute.FileAttribute<?>[]",
                Utility.signatureToString("[Ljava/nio/file/attribute/FileAttribute<*>;"),
                "generic signature");

    // tests for BCEL-286
        assertEquals("boofcv.alg.tracker.tld.TldTracker<boofcv.struct.image.ImageGray<boofcv.struct.image.GrayU8>, boofcv.struct.image.GrayI<boofcv.struct.image.GrayU8>>",
                Utility.signatureToString("Lboofcv/alg/tracker/tld/TldTracker<Lboofcv/struct/image/ImageGray<Lboofcv/struct/image/GrayU8;>;Lboofcv/struct/image/GrayI<Lboofcv/struct/image/GrayU8;>;>;"),
                "generic signature");
        assertEquals("java.util.Map<?, ?>", Utility.signatureToString("Ljava/util/Map<**>;"),
                "generic signature");
        assertEquals("com.jme3.util.IntMap<T>.IntMapIterator",
                Utility.signatureToString("Lcom/jme3/util/IntMap<TT;>.IntMapIterator;"),
                "generic signature");

    // tests for BCEL-279
        assertEquals("<T extends java.lang.Object>(com.google.common.io.ByteProcessor<T>, int)T",
                Utility.signatureToString("<T:Ljava/lang/Object;>(Lcom/google/common/io/ByteProcessor<TT;>;I)TT;", false),
                "type parameters signature");
        assertEquals("<T extends Object>(com.google.common.io.ByteProcessor<T>, int)T",
                Utility.signatureToString("<T:Ljava/lang/Object;>(Lcom/google/common/io/ByteProcessor<TT;>;I)TT;", true),
                "type parameters signature");
        assertEquals("<M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member>(M)void",
                Utility.signatureToString("<M:Ljava/lang/reflect/AccessibleObject;:Ljava/lang/reflect/Member;>(TM;)V"),
                "type parameters signature");
        assertEquals("<K1 extends K, V1 extends V>()com.google.common.cache.Weigher<K1, V1>",
                Utility.signatureToString("<K1:TK;V1:TV;>()Lcom/google/common/cache/Weigher<TK1;TV1;>;"),
                "type parameters signature");
        assertEquals("<K1 extends K, V1 extends V>(com.google.common.cache.Weigher<? super K1, ? super V1>)com.google.common.cache.CacheBuilder<K1, V1>",
                Utility.signatureToString("<K1:TK;V1:TV;>(Lcom/google/common/cache/Weigher<-TK1;-TV1;>;)Lcom/google/common/cache/CacheBuilder<TK1;TV1;>;"),
                "type parameters signature");
        assertEquals("<N extends java.lang.Object, E extends java.lang.Object> extends java.lang.Object implements com.google.common.graph.Network<N, E>",
                Utility.signatureToString("<N:Ljava/lang/Object;E:Ljava/lang/Object;>Ljava/lang/Object;Lcom/google/common/graph/Network<TN;TE;>;", false),
                "class signature");
        assertEquals("<K extends Object, V extends Object> extends Object",
                Utility.signatureToString("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;"),
                "class signature");
    }
}
