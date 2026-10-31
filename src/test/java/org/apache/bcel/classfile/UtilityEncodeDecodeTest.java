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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Utility}.
 */
class UtilityEncodeDecodeTest {

    @Test
    void testDecodeRejectsMalformedEscapes() throws Exception {
        // Second character of a hex escape was never validated: used to throw an undeclared NumberFormatException.
        assertThrows(IOException.class, () -> Utility.decode("$0z", false));
        // Escape characters above 255 used to throw an undeclared ArrayIndexOutOfBoundsException.
        assertThrows(IOException.class, () -> Utility.decode("$\u0141", false));
        // Unmapped escape characters used to silently alias to the "$A" encoding (byte 0).
        assertThrows(IOException.class, () -> Utility.decode("$!", false));
        // Sanity check: valid escapes still decode ('$A' is the special escape for byte 0, '$3a' is hex 0x3a).
        assertEquals(0, Utility.decode("$A", false)[0]);
        assertEquals(0x3a, Utility.decode("$3a", false)[0]);
    }

    @Test
    void testEncodeDecodeRoundTrip() throws Exception {
        final byte[] bytes = new byte[256];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        assertArrayEquals(bytes, Utility.decode(Utility.encode(bytes, false), false));
        assertArrayEquals(bytes, Utility.decode(Utility.encode(bytes, true), true));
    }

    /**
     * Zero-filled data compresses far better than the fixed 3:1 ratio the decode buffer used to assume; the encode/decode round trip must still hold.
     */
    @Test
    void testEncodeDecodeRoundTripHighCompressionRatio() throws Exception {
        final byte[] original = new byte[4096];
        final String encoded = Utility.encode(original, true);
        assertArrayEquals(original, Utility.decode(encoded, true));
    }
}
