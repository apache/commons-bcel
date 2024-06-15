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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.AnnotationEntryGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ElementValueGen;
import org.apache.bcel.generic.ElementValuePairGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.SimpleElementValueGen;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.commons.io.function.Uncheck;

public abstract class AbstractTestCase {

    private static final boolean VERBOSE = false;

    protected static final String PACKAGE_BASE_NAME = AbstractTestCase.class.getPackage().getName();

    // Location of test data
    protected static final File TESTDATA = new File("target", "testdata");

    // package base name in signature format, i.e. with '/' separators instead of '.'
    protected static final String PACKAGE_BASE_SIG = Utility.packageToPath(PACKAGE_BASE_NAME);

    public static void clear() {
        VerifierFactory.clear();
        Repository.clearCache();
        ConstantUtf8.clearCache();
    }

    public AnnotationEntryGen createFruitAnnotationEntry(final ConstantPoolGen cp, final String aFruit, final boolean visibility) {
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.STRING, cp, aFruit);
        final ElementValuePairGen nvGen = new ElementValuePairGen("fruit", evg, cp);
        final ObjectType t = new ObjectType("SimpleStringAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        return new AnnotationEntryGen(t, elements, visibility, cp);
    }

    public SyntheticRepository createRepos(final String cpentry) {
        return Uncheck.get(() -> {
            try (ClassPath cp = new ClassPath("target" + File.separator + "testdata" + File.separator + cpentry + File.separator)) {
                return SyntheticRepository.getInstance(cp);
            }
        });
    }

    /**
     * @param name
     * @return Path to file under the TESTDATA directory
     */
    protected File createTestdataFile(final String name) {
        return new File(TESTDATA, name);
    }

    /**
     * Deletes a file under the TESTDATA directory
     *
     * @param name
     * @return See {@link File#delete()}.
     */
    protected boolean delete(final String name) {
        return new File(TESTDATA, name).delete();
    }

    /**
     * Deletes a directory and file under the TESTDATA directory
     *
     * @param dir
     * @param name
     * @return true if the file was deleted
     */
    protected boolean delete(final String dir, final String name) {
        // The parameter is relative to the TESTDATA dir
        final boolean b = delete(dir + File.separator + name);
        final File testDir = new File(TESTDATA, dir);
        final String[] files = testDir.list();
        if (files == null || files.length == 0) {
            if (!testDir.delete()) {
                System.err.println("Failed to remove: " + testDir);
            }
        } else {
            System.err.println("Non-empty directory: " + testDir);
        }
        return b;
    }

    protected String dumpAnnotationEntries(final AnnotationEntry[] as) {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < as.length; i++) {
            final AnnotationEntry annotation = as[i];
            result.append(annotation.toShortString());
            if (i + 1 < as.length) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    protected String dumpAnnotationEntries(final AnnotationEntryGen[] as) {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < as.length; i++) {
            final AnnotationEntryGen annotation = as[i];
            result.append(annotation.toShortString());
            if (i + 1 < as.length) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    protected Attribute findAttribute(final String name, final Attribute[] all) {
        final List<Attribute> chosenAttrsList = new ArrayList<>();
        for (final Attribute element : all) {
            if (VERBOSE) {
                System.err.println("Attribute: " + element.getName());
            }
            if (element.getName().equals(name)) {
                chosenAttrsList.add(element);
            }
        }
        assertEquals(1, chosenAttrsList.size(), "Wrong number of matches");
        return chosenAttrsList.get(0);
    }

    protected Attribute[] findAttribute(final String name, final JavaClass clazz) {
        final List<Attribute> chosenAttrsList = new ArrayList<>();
        for (final Attribute element : clazz.getAttributes()) {
            if (VERBOSE) {
                System.err.println("Attribute: " + element.getName());
            }
            if (element.getName().equals(name)) {
                chosenAttrsList.add(element);
            }
        }
        return chosenAttrsList.toArray(Attribute.EMPTY_ARRAY);
    }

    /**
     * Gets the javaagent input argument of the current running JVM.
     *
     * @return javaagent input argument of the current running JVM, null if not set.
     */
    protected String getJavaAgent() {
        final List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return jvmArgs.stream().filter(arg -> arg.startsWith("-javaagent")).findFirst().orElse(null);
    }

    protected Method getMethod(final JavaClass cl, final String methodname) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(methodname)) {
                return m;
            }
        }
        return null;
    }

    protected JavaClass getTestJavaClass(final String name) throws ClassNotFoundException {
        return SyntheticRepository.getInstance().loadClass(name);
    }

}
