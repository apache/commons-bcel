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

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.apache.bcel.util.ModularRuntimeImage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.sun.jna.platform.win32.Advapi32Util;

public class JavaHome {

    private static final String EXTRA_JAVA_HOMES = "ExtraJavaHomes";

    private static final String EXTRA_JAVA_ROOT = "ExtraJavaRoot";
    private static final String KEY_JDK = "SOFTWARE\\JavaSoft\\Java Development Kit";
    private static final String KEY_JDK_9 = "SOFTWARE\\JavaSoft\\JDK";
    private static final String KEY_JRE = "SOFTWARE\\JavaSoft\\Java Runtime Environment";
    private static final String KEY_JRE_9 = "SOFTWARE\\JavaSoft\\JRE";

    private static Stream<Path> find(final Path start, final int maxDepth, final BiPredicate<Path, BasicFileAttributes> matcher,
            final FileVisitOption... options) {
        try {
            // TODO Replace with Apache Commons IO UncheckedFiles later.
            return Files.find(start, maxDepth, matcher, options);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static JavaHome from(final String javaHome) {
        return new JavaHome(Paths.get(javaHome));
    }

    private static Stream<String> streamAllWindowsJavaHomes(final String keyJre) {
        if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJre)) {
            return streamWindowsJavaHomes(keyJre, Advapi32Util.registryGetKeys(HKEY_LOCAL_MACHINE, keyJre));
        }
        return Stream.empty();
    }

    private static Stream<String> streamFromCustomKey(final String key) {
        return streamPropertyAndEnvVarValues(key).flatMap(s -> find(Paths.get(s), 1, (p, a) -> Files.isDirectory(p)).map(Path::toString));
    }

    private static Stream<String> streamFromCustomKeys() {
        return Stream.concat(streamPropertyAndEnvVarValues(EXTRA_JAVA_HOMES), streamFromCustomKey(EXTRA_JAVA_ROOT));
    }

    /**
     * Used from {@code @MethodSource} for tests.
     *
     * @return a stream of Java jar paths.
     */
    public static Stream<Path> streamJarPaths() {
        return streamJavaHomes().flatMap(JavaHome::streamJars);
    }

    /**
     * Used from {@code @MethodSource} for tests.
     *
     * @return a stream of Java homes.
     */
    public static Stream<JavaHome> streamJavaHomes() {
        return streamJavaHomeString().map(JavaHome::from);
    }
    public static Stream<String> streamJavaHomeString() {
        final Stream<String> streamW = SystemUtils.IS_OS_WINDOWS ? streamWindowsStrings() : Stream.empty();
        final Stream<String> streamK = Stream.concat(streamW, streamFromCustomKeys());
        final Stream<String> streamJ = StringUtils.isEmpty(SystemUtils.JAVA_HOME) ? Stream.empty() : Stream.of(SystemUtils.JAVA_HOME);
        return Stream.concat(streamK, streamJ);
    }


    /**
     * Used from {@code @MethodSource} for tests.
     *
     * @return a stream of Java jar paths.
     */
    public static Stream<ModularRuntimeImage> streamModularRuntimeImages() {
        return streamJavaHomes().map(JavaHome::getModularRuntimeImage);
    }

    /**
     * Used from {@code @MethodSource} for tests.
     *
     * @return a stream of Java jar paths.
     */
    public static Stream<Path> streamModulePaths() {
        return streamJavaHomes().flatMap(JavaHome::streamModules);
    }

    private static Stream<String> streamPropertyAndEnvVarValues(final String key) {
        return Stream.concat(toPathStringStream(System.getProperty(key)), toPathStringStream(System.getenv(key)));
    }

    private static Stream<String> streamWindowsJavaHomes(final String keyJavaHome, final String[] keys) {
        final Set<String> javaHomes = new HashSet<>(keys.length);
        for (final String key : keys) {
            if (Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, keyJavaHome + "\\" + key)) {
                final String javaHome = Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE, keyJavaHome + "\\" + key, "JavaHome");
                if (StringUtils.isNoneBlank(javaHome) && new File(javaHome).exists()) {
                    javaHomes.add(javaHome);
                }
            }
        }
        return javaHomes.stream();
    }

    private static Stream<String> streamWindowsStrings() {
        return Stream.concat(Stream.of(KEY_JRE, KEY_JRE_9, KEY_JDK, KEY_JDK_9).flatMap(JavaHome::streamAllWindowsJavaHomes),
                streamPropertyAndEnvVarValues(EXTRA_JAVA_HOMES)).distinct();
    }

    private static Stream<String> toPathStringStream(final String path) {
        return StringUtils.isEmpty(path) ? Stream.empty() : Stream.of(path.split(File.pathSeparator));
    }

    private final Path path;

    private JavaHome(final Path path) {
        this.path = Objects.requireNonNull(path, "path");
    }

    Stream<Path> find(final int maxDepth, final BiPredicate<Path, BasicFileAttributes> matcher, final FileVisitOption... options) {
        return find(path, maxDepth, matcher, options);
    }

    ModularRuntimeImage getModularRuntimeImage() {
        try {
            return new ModularRuntimeImage(path.toString());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Path getPath() {
        return path;
    }

    private Stream<Path> streamEndsWith(final String suffix) {
        return find(10, (p, a) -> p.toString().endsWith(suffix));
    }

    private Stream<Path> streamJars() {
        return streamEndsWith(".jar");
    }

    private Stream<Path> streamModules() {
        return streamEndsWith(".jmod");
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
