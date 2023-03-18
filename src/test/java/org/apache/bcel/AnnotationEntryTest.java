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

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.SimpleElementValue;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AnnotationEntryTest {

    static final ElementValuePair[] EMPTY_ARRAY = {};

    @Test
    public void testGetElementValuePairs() {
        final AnnotationEntry annotationEntry = new AnnotationEntry(0, null, false);
        assertArrayEquals(EMPTY_ARRAY, annotationEntry.getElementValuePairs());
    }

    @Test
    public void testGetNumElementValuePairs() {
        final AnnotationEntry annotationEntry = new AnnotationEntry(0, null, false);
        assertEquals(0, annotationEntry.getNumElementValuePairs());
    }

    @Test
    public void testAddElementNameValuePair() {
        final AnnotationEntry annotationEntry = new AnnotationEntry(0, null, false);
        annotationEntry.addElementNameValuePair(new ElementValuePair(0, new SimpleElementValue(0, 0, null), null));
        assertEquals(1, annotationEntry.getNumElementValuePairs());
    }

    @Test
    public void testDump() {
        final AnnotationEntry annotationEntry = new AnnotationEntry(0, null, false);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            annotationEntry.dump(new DataOutputStream(out));
            assertArrayEquals(new byte[4], out.toByteArray());
        } catch (final IOException e) {
            fail(e);
        }
    }
}
