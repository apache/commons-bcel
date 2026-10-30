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

package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.bcel.Const;
import org.junit.jupiter.api.Test;

/**
 * Tests that attribute objects report their own tag via {@link Attribute#getTag()}, which {@code getAttribute(byte)}
 * relies on for lookups.
 */
class AttributeTagTest {

    @Test
    void moduleMainClassReportsModuleMainClassTag() {
        final ModuleMainClass attribute = new ModuleMainClass(0, 2, 0, new ConstantPool());
        assertEquals(Const.ATTR_MODULE_MAIN_CLASS, attribute.getTag());
        assertEquals("ModuleMainClass", Const.getAttributeName(attribute.getTag()));
    }

    @Test
    void nestHostReportsNestHostTag() {
        final NestHost attribute = new NestHost(0, 2, 0, new ConstantPool());
        assertEquals(Const.ATTR_NEST_HOST, attribute.getTag());
        assertEquals("NestHost", Const.getAttributeName(attribute.getTag()));
    }
}
