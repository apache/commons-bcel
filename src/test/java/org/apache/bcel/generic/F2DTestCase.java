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
package org.apache.bcel.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class F2DTestCase {
    @Test
    public void testAccept() {
        final CountingVisitor countVisitor = new CountingVisitor();
        final F2D f2d = new F2D();
        f2d.accept(countVisitor);
        final CountingVisitor expected = new CountingVisitor();
        expected.setTypedInstruction(1);
        expected.setStackProducer(1);
        expected.setStackConsumer(1);
        expected.setConversionInstruction(1);
        expected.setF2D(1);
        assertEquals(expected, countVisitor);
    }

}
