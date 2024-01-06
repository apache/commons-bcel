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

package org.apache.bcel.data;

import java.lang.reflect.Constructor;

/*
 * A test for SWAP instruction.
 * To be compiled with Eclipse Compiler (ecj) with source/target 1.4.
 * This is a requirement for SWAP instruction to be generated.
 * If compiled with javac source/target 1.1+ or ecj source/target 1.5+, no SWAP instruction is generated.
 */
public class SWAP {
    public static Constructor getTestConstructor(final Class theClass) throws NoSuchMethodException {
        final Class[] args = { String.class };
        try {
            return theClass.getConstructor(args);
        } catch (final NoSuchMethodException e) {
        }
        return theClass.getConstructor();
    }
}
