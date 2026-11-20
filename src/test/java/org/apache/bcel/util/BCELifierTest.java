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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.AbstractTest;
import org.apache.bcel.Const;
import org.apache.bcel.HelloWorldCreator;
import org.apache.bcel.classfile.BootstrapMethod;
import org.apache.bcel.classfile.BootstrapMethods;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.BinaryOpCreator;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang3.SystemProperties;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BCELifierTest extends AbstractTest {

    private static Pattern CANON1 = Pattern.compile("#\\d+"); // numbers may vary in length
    private static Pattern CANON2 = Pattern.compile(" +"); // collapse spaces
    private static Pattern CANON3 = Pattern.compile("//.+");

    private static final String EOL = System.lineSeparator();
    public static final String CLASSPATH = "." + File.pathSeparator + SystemProperties.getJavaClassPath();

    /**
     * Checks two executable round trips without changing the fixture's bootstrap table.
     *
     * @param mainBody the Java statements to compile in {@code main}.
     * @param java8 whether to target Java 8 bytecode.
     * @param padding the number of additional UTF-8 constants.
     * @param directory the temporary work directory.
     * @throws Exception if compilation, generation, or execution fails.
     */
    private void assertInvokeDynamicRoundTrip(final String mainBody, final boolean java8, final int padding, final Path directory) throws Exception {
        assertInvokeDynamicRoundTrip(mainBody, java8, padding, directory, bootstrapMethods -> {
            // Keep the compiler's bootstrap table unchanged.
        });
    }

    /**
     * Compiles a fixture in an isolated directory, then regenerates and executes it twice with only the regenerated class on the runtime classpath.
     *
     * <p>
     * Both generations must preserve the bootstrap table and every original constant at its original index. The optional padding exercises constant-pool
     * initialization large enough to exceed a single generated method's code-size limit. Special floating-point values and two-slot constants check that
     * seeding the pool preserves their values and indices as well.
     * </p>
     *
     * @param mainBody the Java statements to compile in {@code main}.
     * @param java8 whether to target Java 8 bytecode.
     * @param padding the number of additional UTF-8 constants.
     * @param directory the temporary work directory.
     * @param bootstrapModifier the bootstrap-table customization to apply before verification and generation.
     * @throws Exception if compilation, generation, or execution fails.
     */
    private void assertInvokeDynamicRoundTrip(final String mainBody, final boolean java8, final int padding, final Path directory,
        final Consumer<BootstrapMethods> bootstrapModifier) throws Exception {
        final String className = "InvokeDynamicExample";
        final Path inputDirectory = Files.createDirectory(directory.resolve("input"));
        final Path source = inputDirectory.resolve(className + ".java");
        Files.write(source, ("public class " + className + " { public static void main(String[] args) { " + mainBody + " } }")
            .getBytes(StandardCharsets.UTF_8));
        if (java8) {
            exec(inputDirectory.toFile(), getAppJavaC(), "-g", "-source", "8", "-target", "8", source.toString());
        } else {
            exec(inputDirectory.toFile(), getAppJavaC(), "-g", source.toString());
        }
        final Path inputFile = inputDirectory.resolve(className + JavaClass.EXTENSION);
        JavaClass original = new ClassParser(inputFile.toString()).parse();
        final ConstantPoolGen pool = new ConstantPoolGen(original.getConstantPool());
        pool.addInteger(Integer.MIN_VALUE);
        pool.addLong(Long.MIN_VALUE);
        pool.addDouble(Double.NaN);
        pool.addDouble(Double.NEGATIVE_INFINITY);
        pool.addDouble(-0.0);
        pool.addFloat(Float.NaN);
        pool.addFloat(Float.POSITIVE_INFINITY);
        pool.addFloat(-0.0f);
        pool.addString("quote\" slash\\ newline\n");
        for (int i = 0; i < padding; i++) {
            pool.addUtf8("padding" + i);
        }
        original.setConstantPool(pool.getFinalConstantPool());
        bootstrapModifier.accept(Arrays.stream(original.getAttributes()).filter(BootstrapMethods.class::isInstance)
            .map(BootstrapMethods.class::cast).findFirst().get());
        original.dump(inputFile.toString());
        assertEquals("lambda works" + EOL, exec(inputDirectory.toFile(), getAppJava(), "-cp", ".", className, "lambda"));
        for (int pass = 1; pass <= 2; pass++) {
            final Path outputDirectory = Files.createDirectory(directory.resolve("pass" + pass));
            final ByteArrayOutputStream creator = new ByteArrayOutputStream();
            new BCELifier(original, creator).start();
            Files.write(outputDirectory.resolve(className + "Creator.java"), creator.toByteArray());
            final File workDir = outputDirectory.toFile();
            assertEquals("", exec(workDir, getAppJavaC(), "-Xlint:deprecation", "-cp", CLASSPATH, className + "Creator.java"));
            assertEquals("", exec(workDir, getAppJava(), "-cp", CLASSPATH, className + "Creator"));
            assertEquals("lambda works" + EOL, exec(workDir, getAppJava(), "-Xverify:all", "-cp", ".", className, "lambda"));
            final JavaClass regenerated = new ClassParser(outputDirectory.resolve(className + JavaClass.EXTENSION).toString()).parse();
            final Constant[] constants = original.getConstantPool().getConstantPool();
            for (int i = 1; i < constants.length; i++) {
                assertEquals(String.valueOf(constants[i]), String.valueOf(regenerated.getConstantPool().getConstantPool()[i]), "Constant " + i);
            }
            final BootstrapMethods bootstrapMethods = Arrays.stream(original.getAttributes()).filter(BootstrapMethods.class::isInstance)
                .map(BootstrapMethods.class::cast).findFirst().get();
            final BootstrapMethods regeneratedBootstrapMethods = Arrays.stream(regenerated.getAttributes())
                .filter(BootstrapMethods.class::isInstance).map(BootstrapMethods.class::cast).findFirst().get();
            final ByteArrayOutputStream expectedBootstrap = new ByteArrayOutputStream();
            bootstrapMethods.dump(new DataOutputStream(expectedBootstrap));
            final ByteArrayOutputStream actualBootstrap = new ByteArrayOutputStream();
            regeneratedBootstrapMethods.dump(new DataOutputStream(actualBootstrap));
            assertArrayEquals(expectedBootstrap.toByteArray(), actualBootstrap.toByteArray());
            original = regenerated;
        }
    }

    // Canonicalise the javap output so it compares better
    private String canonHashRef(String input) {
        input = CANON1.matcher(input).replaceAll("#n");
        input = CANON2.matcher(input).replaceAll(" ");
        return CANON3.matcher(input).replaceAll("");
    }

    private String exec(final File workDir, final String... args) throws Exception {
        // System.out.print(workDir + ": ");
        // Stream.of(args).forEach(e -> System.out.print(e + " "));
        // System.out.println();
        final ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(workDir);
        pb.redirectErrorStream(true);
        final Process proc = pb.start();
        try (BufferedInputStream is = new BufferedInputStream(proc.getInputStream())) {
            final byte[] buff = new byte[2048];
            int len;

            final StringBuilder sb = new StringBuilder();
            while ((len = is.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
            final String output = sb.toString();
            assertEquals(0, proc.waitFor(), output);
            return output;
        }
    }

    private String getAppJava() {
        return getJavaHomeApp("java");
    }

    private String getAppJavaC() {
        return getJavaHomeApp("javac");
    }

    private String getAppJavaP() {
        return getJavaHomeApp("javap");
    }

    private String getJavaHomeApp(final String app) {
        final Path path = getJavaHomeBinPath().resolve(app);
        if (Files.exists(path)) {
            return path.toAbsolutePath().toString();
        }
        if (SystemUtils.IS_OS_WINDOWS && Files.exists(getJavaHomeBinPath().resolve(app + ".exe"))) {
            return path.toAbsolutePath().toString();
        }
        return app;
    }

    private Path getJavaHomeBinPath() {
        return SystemUtils.getJavaHomePath().resolve("bin");
    }

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "iadd 3 2 = 5",
        "isub 3 2 = 1",
        "imul 3 2 = 6",
        "idiv 3 2 = 1",
        "irem 3 2 = 1",
        "iand 3 2 = 2",
        "ior 3 2 = 3",
        "ixor 3 2 = 1",
        "ishl 4 1 = 8",
        "ishr 4 1 = 2",
        "iushr 4 1 = 2",
        "ladd 3 2 = 5",
        "lsub 3 2 = 1",
        "lmul 3 2 = 6",
        "ldiv 3 2 = 1",
        "lrem 3 2 = 1",
        "land 3 2 = 2",
        "lor 3 2 = 3",
        "lxor 3 2 = 1",
        "lshl 4 1 = 8",
        "lshr 4 1 = 2",
        "lushr 4 1 = 2",
        "fadd 3 2 = 5.0",
        "fsub 3 2 = 1.0",
        "fmul 3 2 = 6.0",
        "fdiv 3 2 = 1.5",
        "frem 3 2 = 1.0",
        "dadd 3 2 = 5.0",
        "dsub 3 2 = 1.0",
        "dmul 3 2 = 6.0",
        "ddiv 3 2 = 1.5",
        "drem 3 2 = 1.0"
    // @formatter:on
    })
    void testBinaryOp(final String exp) throws Exception {
        BinaryOpCreator.main(new String[] {});
        final File workDir = new File("target");
        final Pattern pattern = Pattern.compile("([a-z]{3,5}) ([-+]?\\d*\\.?\\d+) ([-+]?\\d*\\.?\\d+) = ([-+]?\\d*\\.?\\d+)");
        final Matcher matcher = pattern.matcher(exp);
        assertTrue(matcher.matches());
        final String op = matcher.group(1);
        final String a = matcher.group(2);
        final String b = matcher.group(3);
        final String expected = matcher.group(4);
        final String javaAgent = getJavaAgent();
        if (javaAgent == null) {
            assertEquals(expected + EOL, exec(workDir, getAppJava(), "-cp", CLASSPATH, "org.apache.bcel.generic.BinaryOp", op, a, b));
        } else {
            final String runtimeExecJavaAgent = javaAgent.replace("jacoco.exec", "jacoco_org.apache.bcel.generic.BinaryOp.exec");
            assertEquals(expected + EOL, exec(workDir, getAppJava(), runtimeExecJavaAgent, "-cp", CLASSPATH, "org.apache.bcel.generic.BinaryOp", op, a, b));
        }
    }

    @Test
    void testBooleanConstantFieldInitValue() throws Exception {
        // javac stores "static final boolean ON = true;" as a CONSTANT_Integer ConstantValue; the generated creator
        // must call FieldGen.setInitValue(boolean), since setInitValue(int) throws for boolean fields.
        final ClassGen cg = new ClassGen("Example", "java.lang.Object", "Example.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        final ConstantPoolGen cp = cg.getConstantPool();
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.BOOLEAN, "ON", cp);
        fg.setInitValue(true);
        cg.addField(fg.getField());

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BCELifier(cg.getJavaClass(), os).start();
        final String source = new String(os.toByteArray(), StandardCharsets.UTF_8);

        assertTrue(source.contains("field.setInitValue(true);"), source);
        assertFalse(source.contains("field.setInitValue(1);"), source);

        final ByteArrayOutputStream compiledClassOutput = new ByteArrayOutputStream();
        new BCELifier(BCELifier.getJavaClass("BooleanConstants"), compiledClassOutput).start();
        final String compiledClassSource = new String(compiledClassOutput.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(compiledClassSource.contains("field.setInitValue(true);"), compiledClassSource);
        assertTrue(compiledClassSource.contains("field.setInitValue(false);"), compiledClassSource);
        assertFalse(compiledClassSource.contains("field.setInitValue(0);"), compiledClassSource);
    }

    @ParameterizedTest
    @CsvSource({ "0, false", "1, true", "2, false", "3, true", "-2, false", "-1, true" })
    void testBooleanConstantFieldLowBit(final int value, final boolean expected) {
        final ClassGen cg = new ClassGen("Example", "java.lang.Object", "Example.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        final ConstantPoolGen cp = cg.getConstantPool();
        final FieldGen fg = new FieldGen(Const.ACC_PUBLIC | Const.ACC_STATIC | Const.ACC_FINAL, Type.BOOLEAN, "VALUE", cp);
        fg.addAttribute(new ConstantValue(cp.addUtf8("ConstantValue"), 2, cp.addInteger(value), cp.getConstantPool()));
        cg.addField(fg.getField());

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BCELifier(cg.getJavaClass(), os).start();
        final String source = new String(os.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(source.contains("field.setInitValue(" + expected + ");"), source);
    }

    @Test
    void testClassNameRejectedWhenNotJavaIdentifier() {
        // Class file names may contain characters the Java language forbids (JVMS 4.2.2 bans only . ; [ /).
        // BCELifier must refuse to emit such a name in identifier position rather than let a crafted
        // this_class inject statements into the generated source.
        final ClassGen cg = new ClassGen("Evil {}\nclass Injected {}//", "java.lang.Object", "Evil.java", Const.ACC_PUBLIC | Const.ACC_SUPER,
            new String[] {});
        final BCELifier bcelifier = new BCELifier(cg.getJavaClass(), new ByteArrayOutputStream());
        assertThrows(IllegalArgumentException.class, bcelifier::start);
    }

    @Test
    void testClassNamesEscapedInOutput() throws Exception {
        // Superclass and source file names are constant-pool derived and can hold any UTF-8.
        final String toEscapeSuper = "java.lang.Object\"); System.exit(1); _cg = new ClassGen(\"x";
        final String toEscapeSource = "Example.java\"); System.exit(2); String s = (\"";
        final ClassGen cg = new ClassGen("Example", toEscapeSuper, toEscapeSource, Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BCELifier(cg.getJavaClass(), os).start();
        final String source = new String(os.toByteArray(), StandardCharsets.UTF_8);

        assertTrue(source.contains(Utility.convertString(toEscapeSuper)), source);
        assertTrue(source.contains(Utility.convertString(toEscapeSource)), source);
        assertFalse(source.contains('"' + toEscapeSuper + '"'), source);
        assertFalse(source.contains('"' + toEscapeSource + '"'), source);
    }

    private File testClassOnPath(final String javaClassName, final Path directory) throws Exception {
        final Path inputDirectory = Files.createDirectory(directory.resolve("input"));
        final String resourceName = Utility.packageToPath(javaClassName) + JavaClass.EXTENSION;
        final Path inputFile = inputDirectory.resolve(resourceName);
        Files.createDirectories(inputFile.getParent());
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            assertNotNull(input, resourceName);
            Files.copy(input, inputFile);
        }
        final JavaClass javaClass = new ClassParser(inputFile.toString()).parse();
        final File workDir = Files.createDirectory(directory.resolve("output")).toFile();
        // Both subprocesses must read the same fixture, never a class left by a previous round trip.
        final String inputClassPath = inputDirectory + File.pathSeparator + SystemProperties.getJavaClassPath();
        final String javapOutInitial = exec(workDir, getAppJavaP(), "-p", "-c", inputFile.toString());
        final String outCreatorFileName = javaClass.getSourceFilePath().replace(".java", "Creator.java");
        final File outCreatorFile = new File(workDir, outCreatorFileName);
        Files.createDirectories(outCreatorFile.getParentFile().toPath());
        final String javaAgent = getJavaAgent();
        final String bcelifierClassName = BCELifier.class.getName();
        final String creatorJavaSource;
        // Creates the BCEL Java app source in creatorJavaSource
        if (javaAgent == null) {
            creatorJavaSource = exec(workDir, getAppJava(), "-cp", inputClassPath, bcelifierClassName, javaClassName);
        } else {
            final String runtimeExecJavaAgent = javaAgent.replace("jacoco.exec", "jacoco_" + javaClassName + ".exec");
            creatorJavaSource = exec(workDir, getAppJava(), runtimeExecJavaAgent, "-cp", inputClassPath, bcelifierClassName, javaClassName);
        }
        // Write the BCEL Java app source to a file
        Files.write(outCreatorFile.toPath(), creatorJavaSource.getBytes(StandardCharsets.UTF_8));
        // Compiles the BCEL Java app
        assertEquals("", exec(workDir, getAppJavaC(), "-cp", CLASSPATH, outCreatorFileName.toString()));
        final String creatorClassName = javaClassName + "Creator";
        // Runs the BCEL Java app to create a class file
        if (javaAgent == null) {
            assertEquals("", exec(workDir, getAppJava(), "-cp", CLASSPATH, creatorClassName));
        } else {
            final String runtimeExecJavaAgent = javaAgent.replace("jacoco.exec", "jacoco_" + Utility.pathToPackage(outCreatorFileName) + ".exec");
            assertEquals("", exec(workDir, getAppJava(), runtimeExecJavaAgent, "-cp", CLASSPATH, creatorClassName));
        }
        // Runs javap on the BCEL generated class file
        final File regeneratedFile = new File(workDir, javaClassName + JavaClass.EXTENSION);
        final String javapOutput = exec(workDir, getAppJavaP(), "-p", "-c", regeneratedFile.getAbsolutePath());
        // Finally compares the output of the roundtrip
        assertEquals(canonHashRef(javapOutInitial), canonHashRef(javapOutput));
        // javap -p -c does not show ConstantValue attributes. Check boolean constants explicitly.
        final JavaClass regenerated = new ClassParser(regeneratedFile.getPath()).parse();
        final Field[] originalFields = javaClass.getFields();
        final Field[] regeneratedFields = regenerated.getFields();
        assertEquals(originalFields.length, regeneratedFields.length);
        for (int i = 0; i < originalFields.length; i++) {
            final Field original = originalFields[i];
            if (original.getType() == Type.BOOLEAN && original.getConstantValue() != null) {
                final Field actual = regeneratedFields[i];
                assertEquals(original.getName(), actual.getName());
                assertEquals(original.getSignature(), actual.getSignature());
                assertNotNull(actual.getConstantValue(), original.getName());
                final int expectedValue = original.getConstantPool().getConstantInteger(original.getConstantValue().getConstantValueIndex()).getBytes() & 1;
                assertEquals(expectedValue, actual.getConstantPool().getConstantInteger(actual.getConstantValue().getConstantValueIndex()).getBytes(),
                    original.getName());
            }
        }
        return workDir;
    }

    @ParameterizedTest
    @CsvSource({"true, 45, 2", "false, 45, 2", "true, 45, 3", "true, 46, 0"})
    void testCompactCodeHeaderRoundTrip(final boolean compactCodeHeader, final int major, final int minor, @TempDir final Path directory) throws Exception {
        final ClassParser parser = new ClassParser("src/test/resources/Java1OakExample.class");
        parser.setCompactCodeHeaderEnabled(true);
        final JavaClass original = parser.parse();
        original.setMajor(major);
        original.setMinor(minor);
        for (final Method method : original.getMethods()) {
            method.getCode().setCompactCodeHeader(compactCodeHeader);
        }
        final ByteArrayOutputStream source = new ByteArrayOutputStream();
        new BCELifier(original, source).start();
        Files.write(directory.resolve("Java1OakExampleCreator.java"), source.toByteArray());
        final File workDir = directory.toFile();
        exec(workDir, getAppJavaC(), "-source", "8", "-target", "8", "-cp", CLASSPATH, "Java1OakExampleCreator.java");
        exec(workDir, getAppJava(), "-cp", CLASSPATH, "Java1OakExampleCreator");
        final boolean expectedCompactCodeHeader = compactCodeHeader && major == 45 && minor < 3;
        final ClassParser generatedParser = new ClassParser(directory.resolve("Java1OakExample.class").toString());
        generatedParser.setCompactCodeHeaderEnabled(expectedCompactCodeHeader);
        final JavaClass generated = generatedParser.parse();
        assertEquals(major, generated.getMajor());
        assertEquals(minor, generated.getMinor());
        assertEquals(original.getMethods().length, generated.getMethods().length);
        for (final Method method : generated.getMethods()) {
            assertEquals(expectedCompactCodeHeader, method.getCode().isCompactCodeHeader());
            assertEquals(1, method.getCode().getMaxLocals());
        }
    }

    @Test
    void testCreateInvokeEscapesConstantPoolName() throws Exception {
        // A hostile constant pool can hold any UTF-8 as a referenced method name.
        final String toEscapeName = "evil\"); System.exit(1); il.append(\"";
        final ClassGen cg = new ClassGen("Example", "java.lang.Object", "Example.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionFactory factory = new InstructionFactory(cg, cp);
        final InstructionList il = new InstructionList();
        il.append(InstructionConst.ALOAD_0);
        il.append(factory.createInvoke("java.lang.Object", toEscapeName, Type.VOID, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, "m", "Example", il, cp);
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BCELifier(cg.getJavaClass(), os).start();
        final String source = new String(os.toByteArray(), StandardCharsets.UTF_8);

        assertTrue(source.contains("_factory.createInvoke(\"java.lang.Object\", \"" + Utility.convertString(toEscapeName) + "\", "), source);
        assertFalse(source.contains('"' + toEscapeName + '"'), source);
    }

    @Test
    void testHelloWorld() throws Exception {
        HelloWorldCreator.main(new String[] {});
        final File workDir = new File("target");
        final String javaAgent = getJavaAgent();
        if (javaAgent == null) {
            assertEquals("Hello World!" + EOL, exec(workDir, getAppJava(), "-cp", CLASSPATH, "org.apache.bcel.HelloWorld"));
        } else {
            final String runtimeExecJavaAgent = javaAgent.replace("jacoco.exec", "jacoco_org.apache.bcel.HelloWorld.exec");
            assertEquals("Hello World!" + EOL, exec(workDir, getAppJava(), runtimeExecJavaAgent, "-cp", CLASSPATH, "org.apache.bcel.HelloWorld"));
        }
    }

    /**
     * Verifies that large bootstrap tables and argument lists compile and survive two executable round trips.
     *
     * <p>
     * The active lambda keeps its original bootstrap entry. Additional unused entries and arguments contain valid constant-pool references and exercise
     * initialization beyond the JVM's per-method code-size limit. The original class is executed before regeneration to check that the expanded table is valid.
     * </p>
     *
     * @param methodCount the number of bootstrap entries.
     * @param argumentCount the number of arguments in the final, unused entry.
     * @param directory the temporary work directory.
     * @throws Exception if compilation, generation, or execution fails.
     */
    @ParameterizedTest
    @CsvSource({"4000, 3", "2, 12000"})
    void testInvokeDynamicLargeBootstrapMethods(final int methodCount, final int argumentCount, @TempDir final Path directory) throws Exception {
        assertInvokeDynamicRoundTrip("Runnable task = () -> System.out.println(\"lambda works\"); task.run();", true, 0, directory, bootstrapMethods -> {
            final BootstrapMethod template = bootstrapMethods.getBootstrapMethods()[0];
            final BootstrapMethod[] methods = new BootstrapMethod[methodCount];
            for (int i = 0; i < methods.length; i++) {
                methods[i] = template.copy();
            }
            final int[] arguments = new int[argumentCount];
            Arrays.fill(arguments, template.getBootstrapArguments()[0]);
            methods[methods.length - 1].setBootstrapArguments(arguments);
            bootstrapMethods.setBootstrapMethods(methods);
            bootstrapMethods.setLength(2 + Arrays.stream(methods).mapToInt(method -> 4 + 2 * method.getNumBootstrapArguments()).sum());
        });
    }

    /**
     * Verifies that preserving a large constant pool splits generated initialization into methods that javac can compile.
     *
     * @param directory the temporary work directory.
     * @throws Exception if compilation, generation, or execution fails.
     */
    @Test
    void testInvokeDynamicLargeConstantPool(@TempDir final Path directory) throws Exception {
        assertInvokeDynamicRoundTrip("Runnable task = () -> System.out.println(\"lambda works\"); task.run();", true, 6000, directory);
    }

    /**
     * Verifies BCEL-378 with ordinary and captured lambdas, a method reference, a serializable lambda, and a static interface method call.
     *
     * <p>
     * The smallest case is {@code Runnable task = () -> System.out.println("lambda works"); task.run();}. Captured values require a nonempty invokedynamic
     * descriptor; method references require preserving their target handle; serializable lambdas use {@code altMetafactory} with integer bootstrap arguments
     * and generate branching deserialization code with stack maps. Each fixture is compiled for Java 8, regenerated twice, and executed after each pass.
     * </p>
     *
     * @param mainBody the Java statements to compile in {@code main}.
     * @param directory the temporary work directory.
     * @throws Exception if compilation, generation, or execution fails.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "Runnable task = () -> System.out.println(\"lambda works\"); task.run();",
        "String prefix = args[0]; java.util.function.Supplier<String> task = () -> prefix + \" works\"; System.out.println(task.get());",
        "java.util.function.Function<String, String> task = String::trim; System.out.println(task.apply(\" lambda works \"));",
        "Runnable task = (Runnable & java.io.Serializable) () -> System.out.println(\"lambda works\"); task.run();",
        "java.util.function.Function<String, String> task = java.util.function.Function.<String>identity().andThen(value -> value + \" works\"); "
            + "System.out.println(task.apply(args[0]));"
    })
    void testInvokeDynamicRoundTrip(final String mainBody, @TempDir final Path directory) throws Exception {
        assertInvokeDynamicRoundTrip(mainBody, true, 0, directory);
    }

    /**
     * Verifies the StringConcatFactory bootstrap emitted by javac on Java 9 and later, including its string bootstrap argument.
     *
     * @param directory the temporary work directory.
     * @throws Exception if compilation, generation, or execution fails.
     */
    @Test
    @EnabledForJreRange(min = JRE.JAVA_9)
    void testInvokeDynamicStringConcatenation(@TempDir final Path directory) throws Exception {
        assertInvokeDynamicRoundTrip("System.out.println(args[0] + \" works\");", false, 0, directory);
    }

    /*
     * Dumps a class using "javap" and compare with the same class recreated using BCELifier, "javac", "java" and dumped with "javap".
     *
     * TODO: detect if JDK present and skip test if not
     */
    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "org.apache.commons.lang.math.Fraction",
        "org.apache.commons.lang.exception.NestableDelegate",
        "org.apache.commons.lang.builder.CompareToBuilder",
        "org.apache.commons.lang.builder.ToStringBuilder",
        "org.apache.commons.lang.SerializationUtils",
        "org.apache.commons.lang.ArrayUtils",
        "Java4Example",
        "BooleanConstants"
    // @formatter:on
    })
    void testJavapCompare(final String className, @TempDir final Path directory) throws Exception {
        testClassOnPath(className, directory);
    }

    /**
     * Round-trips the existing Java 8 fixtures, including their invokedynamic instructions, on all supported JDKs.
     *
     * @param className the fixture class name.
     * @param directory the temporary work directory.
     * @throws Exception if generation, compilation, or bytecode comparison fails.
     */
    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "Java8Example",
        "Java8Example2",
    // @formatter:on
    })
    void testJavapCompareJava8(final String className, @TempDir final Path directory) throws Exception {
        testClassOnPath(className, directory);
    }

    @Test
    void testMainNoArg() throws Exception {
        final PrintStream sysout = System.out;
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            BCELifier.main(new String[0]);
            final String outputNoArgs = new String(out.toByteArray());
            assertEquals("Usage: BCELifier className" + EOL + "\tThe class must exist on the classpath" + EOL, outputNoArgs);
        } finally {
            System.setOut(sysout);
        }
    }

    @Test
    void testMethodNameEscapedInOutput() throws Exception {
        // A hostile constant pool can hold any UTF-8 as a method name.
        final String evilName = "evil\"); System.exit(1); String x = (\"";
        final ClassGen cg = new ClassGen("Example", "java.lang.Object", "Example.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        il.append(InstructionConst.RETURN);
        final MethodGen mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {}, evilName, "Example", il, cp);
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new BCELifier(cg.getJavaClass(), os).start();
        final String source = new String(os.toByteArray(), StandardCharsets.UTF_8);

        assertTrue(source.contains("\"" + Utility.convertString(evilName) + "\", \"Example\", il, _cp);"), source);
        assertFalse(source.contains('"' + evilName + '"'), source);
    }

    @ParameterizedTest
    @ValueSource(strings = { "StackMapExample", "StackMapExample2" })
    void testStackMap(final String className, @TempDir final Path directory) throws Exception {
        final File workDir = testClassOnPath(className, directory);
        assertEquals("Hello World" + EOL, exec(workDir, getAppJava(), "-cp", CLASSPATH, className, "Hello"));
    }

    @Test
    void testStart() throws Exception {
        final OutputStream os = new ByteArrayOutputStream();
        final JavaClass javaClass = BCELifier.getJavaClass("Java8Example");
        assertNotNull(javaClass);
        final BCELifier bcelifier = new BCELifier(javaClass, os);
        bcelifier.start();
    }
}
