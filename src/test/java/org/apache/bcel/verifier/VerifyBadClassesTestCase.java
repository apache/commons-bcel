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

package org.apache.bcel.verifier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Test a number of BCEL issues related to running the Verifier on a bad or malformed .class file and having it die with
 * an exception rather than report a verification failure.
 */
public class VerifyBadClassesTestCase {

    private List<String> buildVerifyCommand(final String className, final String testDir) {
        final List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-ea");

        command.add("-classpath");
        command.add(System.getProperty("java.class.path") + ":" + testDir);

        command.add("org.apache.bcel.verifier.Verifier");
        command.add(className);

        return command;
    }

    /**
     * Runs the given command synchronously in the given directory. If the command completes normally, returns a
     * {@link Status} object capturing the command, exit status, and output from the process.
     *
     * @param command the command to be run in the process
     * @return a String capturing the error output of executing the command
     * @throws ExecuteException if executor fails
     * @throws IOException if executor fails
     */
    private String run(final List<String> command) throws ExecuteException, IOException {

        /** The process timeout in milliseconds. Defaults to 30 seconds. */
        final long timeout = 30 * 1000;

        final String[] args = command.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        final CommandLine cmdLine = new CommandLine(args[0]); // constructor requires executable name
        cmdLine.addArguments(Arrays.copyOfRange(args, 1, args.length));

        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        final DefaultExecutor executor = new DefaultExecutor();

        final ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        executor.setWatchdog(watchdog);

        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        final PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine, resultHandler);

        int exitValue = -1;
        try {
            resultHandler.waitFor();
            exitValue = resultHandler.getExitValue();
        } catch (final InterruptedException e) {
            // Ignore exception, but watchdog.killedProcess() records that the process timed out.
        }
        final boolean timedOut = executor.isFailure(exitValue) && watchdog.killedProcess();
        if (timedOut) {
            return "Command timed out.";
        }

        // return "stdout: " + outStream.toString() + "\nstderr: " + errStream.toString();
        return errStream.toString();
    }

    /**
     * BCEL-303: AssertionViolatedException in Pass 3A Verification of invoke instructions
     */
    @Test
    public void testB303() {
        testVerify("issue303/example", "A");
    }

    /**
     * BCEL-307: ClassFormatException thrown in Pass 3A verification
     */
    @Test
    public void testB307() {
        testVerify("issue307/example", "A");
    }

    /**
     * BCEL-308: NullPointerException in Verifier Pass 3A
     */
    @Test
    public void testB308() {
        testVerify("issue308", "Hello");
    }

    /**
     * BCEL-309: NegativeArraySizeException when Code attribute length is negative
     */
    @Test
    public void testB309() {
        testVerify("issue309", "Hello");
    }

    /**
     * BCEL-310: ArrayIndexOutOfBounds in Verifier Pass 3A
     */
    @Test
    public void testB310() {
        testVerify("issue310", "Hello");
    }

    /**
     * BCEL-311: ClassCastException in Verifier Pass 2
     */
    @Test
    public void testB311() {
        testVerify("issue311", "Hello");
    }

    /**
     * BCEL-312: AssertionViolation: INTERNAL ERROR Please adapt StringRepresentation to deal with ConstantPackage in
     * Verifier Pass 2
     */
    @Test
    public void testB312() {
        testVerify("issue312", "Hello");
    }

    /**
     * BCEL-313: ClassFormatException: Invalid signature: Ljava/lang/String)V in Verifier Pass 3A
     */
    @Test
    public void testB313() {
        testVerify("issue313", "Hello");
    }

    /**
     * BCEL-337: StringIndexOutOfBounds in Pass 2 Verification of empty method names in the constant pool
     */
    @Test
    public void testB337() {
        testVerify("issue337/example", "A");
    }

    /**
     * Note that the test classes are bad or malformed and this causes the animal-sniffer-maven-plugin to fail during the
     * build/verification process. I was not able to figure out the right incantations to get it to ignore these files.
     * Hence, their file extension is '.classx' to avoid this problem. As part of the test process we rename them to
     * '.class' and then back to '.classx' after the test. If we can get animal-sniffer to ignore the files, these steps
     * could be omitted.
     */
    private void testVerify(final String directory, final String className) {
        final String baseDir = "target/test-classes";
        final String testDir = baseDir + (directory.isEmpty() ? "" : File.separator + directory);

        final File origFile = new File(testDir, className + ".classx");
        final File testFile = new File(testDir, className + JavaClass.EXTENSION);

        if (!origFile.renameTo(testFile)) {
            fail("Failed to rename orig file");
        }

        String result;
        try {
            result = run(buildVerifyCommand(className, testDir));
        } catch (final Exception e) {
            result = e.getMessage();
        }

        if (!testFile.renameTo(origFile)) {
            fail("Failed to rename test file");
        }

        assertTrue(result.isEmpty(), result);
    }
}
