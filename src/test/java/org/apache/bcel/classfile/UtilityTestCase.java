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

import junit.framework.TestCase;

public class UtilityTestCase extends TestCase {
    
    public void testSignatureToStringWithGenerics() throws Exception {
        assertEquals("generic signature", "java.util.Map<X, java.util.List<Y>>", Utility.signatureToString("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;"));
        assertEquals("generic signature", "java.util.Set<? extends java.nio.file.OpenOption>", Utility.signatureToString("Ljava/util/Set<+Ljava/nio/file/OpenOption;>;"));
        assertEquals("generic signature", "java.nio.file.attribute.FileAttribute<?>...[]", Utility.signatureToString("[Ljava/nio/file/attribute/FileAttribute<*>;"));
    }
}
