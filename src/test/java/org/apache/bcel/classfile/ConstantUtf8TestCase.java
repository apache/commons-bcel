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
import org.apache.bcel.classfile.ConstantUtf8.CachingGenerator;
import org.junit.After;
import org.junit.Test;

public class ConstantUtf8TestCase extends TestCase {
    @After
    public void cleanup() {
        System.clearProperty(ConstantUtf8.MAX_CACHED_SIZE_PROPERTY_KEY);
    }

    @Test
    public void testDefaultBehavior() {
        ConstantUtf8.Generator defaultGenerator = ConstantUtf8.createGenerator();
        assertTrue("Default generator should be NormalGenerator",
                defaultGenerator instanceof ConstantUtf8.NormalGenerator);
    }
    @Test
    public void testUncachedInstance()  {
        ConstantUtf8.Generator normalGenerator = new ConstantUtf8.NormalGenerator();
        ConstantUtf8 instance1 = normalGenerator.getInstance("java.lang.String");
        ConstantUtf8 instance2 = normalGenerator.getInstance("java.lang.String");
        assertNotSame(instance1, instance2);
    }

    @Test
    public void testCachedInstance()  {
        System.setProperty(ConstantUtf8.MAX_CACHED_SIZE_PROPERTY_KEY, "200");
        ConstantUtf8.Generator cachedGenerator = new CachingGenerator();
        ConstantUtf8 instance1 = cachedGenerator.getInstance("java.lang.String");
        ConstantUtf8 instance2 = cachedGenerator.getInstance("java.lang.String");
        assertSame(instance1, instance2);
    }
}