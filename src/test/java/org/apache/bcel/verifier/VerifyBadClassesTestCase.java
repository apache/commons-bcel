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

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyBadClassesTestCase {

    /**
     * There are a number of BCEL issues related to running the Verifier
     * on a bad or malformed .class file and having it die with an exception
     * rather than report a verification failure.
     */

    /**
     * BCEL-303:
     */
    @Test
    public void testB303() throws Exception
    {
        testVerify("issue303/example", "A");
    }

    /**
     * BCEL-307:
     */
    @Test
    public void testB307() throws Exception
    {
        testVerify("issue307/example", "A");
    }

    /**
     * BCEL-337:
     */
    @Test
    public void testB337() throws Exception
    {
        testVerify("issue337/example", "A");
    }

    /**
     * BCEL-308:
     */
    @Test
    public void testB308() throws Exception
    {
        testVerify("issue308", "Hello");
    }

    /**
     * BCEL-309:
     */
    @Test
    public void testB309() throws Exception
    {
        testVerify("issue309", "Hello");
    }

    /**
     * BCEL-310:
     */
    @Test
    public void testB310() throws Exception
    {
        testVerify("issue310", "Hello");
    }

    /**
     * BCEL-311:
     */
    @Test
    public void testB311() throws Exception
    {
        testVerify("issue311", "Hello");
    }

    /**
     * BCEL-312:
     */
    @Test
    public void testB312() throws Exception
    {
        testVerify("issue312", "Hello");
    }

    /**
     * BCEL-313:
     */
    @Test
    public void testB313() throws Exception
    {
        testVerify("issue313", "Hello");
    }

    /**
     * Note that the test classes are bad or malformed and this causes the
     * animal-sniffer-maven-plugin to fail during the build/verification
     * process. I was not able to figure out the right incantations to get
     * it to ignore these files.  Hence, their file extension is '.classx'
     * to avoid this problem.  As part of the test process we rename them
     * to '.class' and then back to '.classx' after the test.  If we can
     * get animal-sniffer to ignore the files, these steps could be omitted.
     */
    private void testVerify(String directory, String className) throws Exception {
        final String baseDir = "target/test-classes";
        String testDir = baseDir + (directory.isEmpty() ? "" : "/" + directory);

        File origFile = new File(testDir + "/" + className + ".classx");
        File testFile = new File(testDir + "/" + className + ".class");

         if (!origFile.renameTo(testFile)) {
             throw new Exception("Failed to rename orig file");
         }

         String result = run(buildVerifyCommand(className, testDir));

         if (!testFile.renameTo(origFile)) {
             throw new Exception("Failed to rename test file");
         }

         assertTrue(result.isEmpty(), result);
    }

    private List<String> buildVerifyCommand(String className, String testDir) {
      List<String> command = new ArrayList<>();
      command.add("java");
      command.add("-ea");

      command.add("-classpath");
      command.add(System.getProperty("java.class.path") + ":" + testDir);

      command.add("org.apache.bcel.verifier.Verifier");
      command.add(className);

      return command;
    }

    /**
     * Runs the given command synchronously in the given directory. If the
     * command completes normally, returns a {@link Status} object capturing the command, exit status,
     * and output from the process.
     *
     * @param command the command to be run in the process
     * @return a String capturing the error output of executing the command
     * @throws if there is an error running the command
     */
    private String run(List<String> command) throws Exception {

      /** The process timeout in milliseconds. Defaults to 30 seconds. */
      long timeout = 30 * 1000;

      String[] args = command.toArray(new String[0]);
      CommandLine cmdLine = new CommandLine(args[0]); // constructor requires executable name
      cmdLine.addArguments(Arrays.copyOfRange(args, 1, args.length));

      DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
      DefaultExecutor executor = new DefaultExecutor();

      ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
      executor.setWatchdog(watchdog);

      final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      final ByteArrayOutputStream errStream = new ByteArrayOutputStream();
      PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
      executor.setStreamHandler(streamHandler);

      try {
        executor.execute(cmdLine, resultHandler);
      } catch (IOException e) {
        throw new Exception("Exception starting process", e);
      }

      int exitValue = -1;
      try {
        resultHandler.waitFor();
        exitValue = resultHandler.getExitValue();
      } catch (InterruptedException e) {
        // Ignore exception, but watchdog.killedProcess() records that the process timed out.
      }
      boolean timedOut = executor.isFailure(exitValue) && watchdog.killedProcess();
      if (timedOut) {
        return "Command timed out.";
      }

      //return "stdout: " + outStream.toString() + "\nstderr: " + errStream.toString();
      return errStream.toString();
    }
}
