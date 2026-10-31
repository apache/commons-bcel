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
package org.apache.bcel.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.bcel.AbstractTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ClassPathTest extends AbstractTest {

    private static int lastIndexOf(final byte[] haystack, final byte[] needle) {
        outer: for (int i = haystack.length - needle.length; i >= 0; i--) {
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static void writeLittleEndianInt(final byte[] bytes, final int offset, final int value) {
        bytes[offset] = (byte) value;
        bytes[offset + 1] = (byte) (value >>> 8);
        bytes[offset + 2] = (byte) (value >>> 16);
        bytes[offset + 3] = (byte) (value >>> 24);
    }

    @Test
    void testClose() throws IOException {
        try (ClassPath cp = new ClassPath(ClassPath.getClassPath())) {
            assertNotNull(cp);
        }
    }

    /**
     * The buffer for {@code getBytes} must be sized from the actual entry content, not from the archive's declared
     * uncompressed-size field, which is attacker-controlled and may be forged (huge: forged allocation; small: silent
     * truncation).
     */
    @Test
    void testGetBytesIgnoresForgedUncompressedSize(@TempDir final Path tempDir) throws IOException {
        final byte[] content = new byte[256];
        for (int i = 0; i < content.length; i++) {
            content[i] = (byte) i;
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            zos.putNextEntry(new ZipEntry("Foo.class"));
            zos.write(content);
            zos.closeEntry();
        }
        final byte[] jar = baos.toByteArray();
        // Forge the uncompressed-size field (offset 24) of the central directory file header (PK\1\2),
        // which is where java.util.zip.ZipFile reads entry sizes from.
        final int cen = lastIndexOf(jar, new byte[] { 0x50, 0x4B, 0x01, 0x02 });
        assertTrue(cen >= 0);
        writeLittleEndianInt(jar, cen + 24, 64 * 1024 * 1024);
        final Path jarFile = tempDir.resolve("forged.jar");
        Files.write(jarFile, jar);
        try (ClassPath classPath = new ClassPath(jarFile.toString())) {
            assertArrayEquals(content, classPath.getBytes("Foo"));
        }
    }

    @Test
    void testGetClassFile() throws IOException {
        assertNotNull(ClassPath.SYSTEM_CLASS_PATH.getClassFile("java.lang.String"));
    }

    @Test
    void testGetResource() {
        assertNotNull(ClassPath.SYSTEM_CLASS_PATH.getResource("java/lang/String.class"));
    }

    @Test
    void testGetResourceAsStream() throws IOException {
        try (InputStream inputStream = ClassPath.SYSTEM_CLASS_PATH.getResourceAsStream("java/lang/String.class")) {
            assertNotNull(inputStream);
        }
    }

    @Test
    void testGetResources() {
        assertTrue(ClassPath.SYSTEM_CLASS_PATH.getResources("java/lang/String.class").hasMoreElements());
    }
}
