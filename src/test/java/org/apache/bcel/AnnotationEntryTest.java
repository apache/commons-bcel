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

package org.apache.bcel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.SimpleElementValue;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link AnnotationEntry}.
 */
public class AnnotationEntryTest {

    @Test
    public void testAddElementNameValuePair() {
        final AnnotationEntry annotationEntry = new AnnotationEntry(0, null, false);
        annotationEntry.addElementNameValuePair(new ElementValuePair(0, new SimpleElementValue(0, 0, null), null));
        assertEquals(1, annotationEntry.getNumElementValuePairs());
    }

    @Test
    public void testDump() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AnnotationEntry(0, null, false).dump(new DataOutputStream(out));
        assertArrayEquals(new byte[4], out.toByteArray());
    }

    @Test
    public void testGetElementValuePairs() {
        assertEquals(0, new AnnotationEntry(0, null, false).getElementValuePairs().length);
    }

    @Test
    public void testGetNumElementValuePairs() {
        assertEquals(0, new AnnotationEntry(0, null, false).getNumElementValuePairs());
    }
}
