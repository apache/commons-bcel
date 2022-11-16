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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class VerifyJavaHomesTestCase extends AbstractVerifierTestCase {

    static int count;

    /**
     * Eventually runs out of memory? Super now calls VerifierFactory.clear();
     * 
     * <pre>
      164800................................................................................
      165600...........Exception in thread "fork-1-event-thread" .java.lang.OutOfMemoryError: Java heap space
        Exception in thread "fork-1-event-thread" java.lang.OutOfMemoryError: Java heap space
        at java.lang.AbstractStringBuilder.<init>(AbstractStringBuilder.java:68)
        at java.lang.StringBuilder.<init>(StringBuilder.java:106)
        at org.apache.maven.surefire.api.stream.AbstractStreamDecoder.toString(AbstractStreamDecoder.java:364)
        at org.apache.maven.surefire.api.stream.AbstractStreamDecoder.readString(AbstractStreamDecoder.java:336)
        at org.apache.maven.surefire.api.stream.AbstractStreamDecoder.readString(AbstractStreamDecoder.java:196)
        at org.apache.maven.surefire.stream.EventDecoder.decode(EventDecoder.java:176)
        at org.apache.maven.plugin.surefire.extensions.EventConsumerThread.run(EventConsumerThread.java:73)
     * </pre>
     * 
     * @param name
     * @throws ClassNotFoundException
     */
    @Disabled("Run once in a while, it takes a very long time.")
    @ParameterizedTest
    // @Execution(ExecutionMode.CONCURRENT)
    @MethodSource("org.apache.bcel.generic.JavaHome#streamJarEntryClassName")
    public void testJarEntryClassName(final String name) throws ClassNotFoundException {
        // System.out.println(jarEntry.getName());
        // Skip $ classes for now
        count++;
        if (count % 10 == 0) {
            System.out.print('.');
        }
        if (count % 800 == 0) {
            System.out.println();
            System.out.print(count);
        }
        if (!name.contains("$")) {
            doAllPasses(name);
        }
    }

}
