/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * Read class file(s) and display its contents. The command line usage is:
 *
 * <pre>
 * java ListClass [-constants] [-code] [-brief] [-dependencies] [-nocontents] [-recurse] class... [-exclude <list>]
 * </pre>
 *
 * where
 * <ul>
 * <li>{@code -code} List byte code of methods</li>
 * <li>{@code -brief} List byte codes briefly</li>
 * <li>{@code -constants} Print constants table (constant pool)</li>
 * <li>{@code -recurse} Usually intended to be used along with {@code -dependencies} When this flag is set, listclass
 * will also print information about all classes which the target class depends on.</li>
 *
 * <li>{@code -dependencies} Setting this flag makes listclass print a list of all classes which the target class
 * depends on. Generated from getting all CONSTANT_Class constants from the constant pool.</li>
 *
 * <li>{@code -exclude} All non-flag arguments after this flag are added to an 'exclusion list'. Target classes are
 * compared with the members of the exclusion list. Any target class whose fully qualified name begins with a name in
 * the exclusion list will not be analyzed/listed. This is meant primarily when using both {@code -recurse} to exclude
 * java, javax, and sun classes, and is recommended as otherwise the output from {@code -recurse} gets quite long and
 * most of it is not interesting. Note that {@code -exclude} prevents listing of classes, it does not prevent class
 * names from being printed in the {@code -dependencies} list.</li>
 * <li>{@code -nocontents} Do not print JavaClass.toString() for the class. I added this because sometimes I'm only
 * interested in dependency information.</li>
 * </ul>
 * <p>
 * Here's a couple examples of how I typically use ListClass:
 * </p>
 *
 * <pre>
 * java ListClass -code MyClass
 * </pre>
 *
 * Print information about the class and the byte code of the methods
 *
 * <pre>
 * java ListClass -nocontents -dependencies MyClass
 * </pre>
 *
 * Print a list of all classes which MyClass depends on.
 *
 * <pre>
 * java ListClass -nocontents -recurse MyClass -exclude java. javax. sun.
 * </pre>
 *
 * Print a recursive listing of all classes which MyClass depends on. Do not analyze classes beginning with "java.",
 * "javax.", or "sun.".
 *
 * <pre>
 * java ListClass -nocontents -dependencies -recurse MyClass -exclude java.javax. sun.
 * </pre>
 * <p>
 * Print a recursive listing of dependency information for MyClass and its dependents. Do not analyze classes beginning
 * with "java.", "javax.", or "sun."
 * </p>
 *
 * <a href="mailto:twheeler@objectspace.com">Thomas Wheeler</A>
 */
public class ListClass {

    public static String[] getClassDependencies(final ConstantPool pool) {
        final String[] tempArray = new String[pool.getLength()];
        int size = 0;
        final StringBuilder buf = new StringBuilder();

        for (int idx = 0; idx < pool.getLength(); idx++) {
            final Constant c = pool.getConstant(idx);
            if (c != null && c.getTag() == Const.CONSTANT_Class) {
                final ConstantUtf8 c1 = (ConstantUtf8) pool.getConstant(((ConstantClass) c).getNameIndex());
                buf.setLength(0);
                buf.append(c1.getBytes());
                for (int n = 0; n < buf.length(); n++) {
                    if (buf.charAt(n) == '/') {
                        buf.setCharAt(n, '.');
                    }
                }

                tempArray[size++] = buf.toString();
            }
        }

        final String[] dependencies = new String[size];
        System.arraycopy(tempArray, 0, dependencies, 0, size);
        return dependencies;
    }

    public static void main(final String[] argv) {
        final List<String> fileName = new ArrayList<>();
        final List<String> excludeName = new ArrayList<>();
        boolean code = false;
        boolean constants = false;
        boolean verbose = true;
        boolean classdep = false;
        boolean nocontents = false;
        boolean recurse = false;
        boolean exclude = false;
        String name;

        // Parse command line arguments.
        for (final String arg : argv) {
            if (arg.charAt(0) == '-') { // command line switch
                if (arg.equals("-constants")) {
                    constants = true;
                } else if (arg.equals("-code")) {
                    code = true;
                } else if (arg.equals("-brief")) {
                    verbose = false;
                } else if (arg.equals("-dependencies")) {
                    classdep = true;
                } else if (arg.equals("-nocontents")) {
                    nocontents = true;
                } else if (arg.equals("-recurse")) {
                    recurse = true;
                } else if (arg.equals("-exclude")) {
                    exclude = true;
                } else if (arg.equals("-help")) {
                    System.out.println("Usage: java listclass [-constants] [-code] [-brief] " + "[-dependencies] [-nocontents] [-recurse] class... "
                        + "[-exclude <list>]\n" + "-constants       Print constants table (constant pool)\n" + "-code            Dump byte code of methods\n"
                        + "-brief           Brief listing\n" + "-dependencies    Show class dependencies\n"
                        + "-nocontents      Do not print field/method information\n" + "-recurse         Recurse into dependent classes\n"
                        + "-exclude <list>  Do not list classes beginning with " + "strings in <list>");
                    System.exit(0);
                } else {
                    System.err.println("Unknown switch " + arg + " ignored.");
                }
            } else if (exclude) { // add file name to list
                excludeName.add(arg);
            } else {
                fileName.add(arg);
            }
        }

        if (fileName.isEmpty()) {
            System.err.println("list: No input files specified");
        } else {
            final ListClass listClass = new ListClass(code, constants, verbose, classdep, nocontents, recurse, excludeName);

            for (final String element : fileName) {
                name = element;

                listClass.list(name);
            }
        }
    }

    /**
     * Dump the list of classes this class is dependent on
     */
    public static void printClassDependencies(final ConstantPool pool) {
        System.out.println("Dependencies:");
        for (final String name : getClassDependencies(pool)) {
            System.out.println("\t" + name);
        }
    }

    /**
     * Dump the disassembled code of all methods in the class.
     */
    public static void printCode(final Method[] methods, final boolean verbose) {
        for (final Method method : methods) {
            System.out.println(method);

            final Code code = method.getCode();
            if (code != null) {
                System.out.println(code.toString(verbose));
            }
        }
    }

    boolean code;
    boolean constants;
    boolean verbose;
    boolean classDep;

    boolean noContents;

    boolean recurse;

    Map<String, String> listedClasses;

    List<String> excludeName;

    public ListClass(final boolean code, final boolean constants, final boolean verbose, final boolean classDep, final boolean noContents,
        final boolean recurse, final List<String> excludeName) {
        this.code = code;
        this.constants = constants;
        this.verbose = verbose;
        this.classDep = classDep;
        this.noContents = noContents;
        this.recurse = recurse;
        this.listedClasses = new HashMap<>();
        this.excludeName = excludeName;
    }

    /**
     * Print the given class on screen
     */
    public void list(final String name) {
        try {
            JavaClass javaClass;

            if (listedClasses.get(name) != null || name.startsWith("[")) {
                return;
            }

            for (final String element : excludeName) {
                if (name.startsWith(element)) {
                    return;
                }
            }

            if (name.endsWith(JavaClass.EXTENSION)) {
                javaClass = new ClassParser(name).parse(); // May throw IOException
            } else {
                javaClass = Repository.lookupClass(name);
            }

            if (noContents) {
                System.out.println(javaClass.getClassName());
            } else {
                System.out.println(javaClass); // Dump the contents
            }

            if (constants) {
                System.out.println(javaClass.getConstantPool());
            }

            if (code) {
                printCode(javaClass.getMethods(), verbose);
            }

            if (classDep) {
                printClassDependencies(javaClass.getConstantPool());
            }

            listedClasses.put(name, name);

            if (recurse) {
                final String[] dependencies = getClassDependencies(javaClass.getConstantPool());

                for (final String dependency : dependencies) {
                    list(dependency);
                }
            }
        } catch (final IOException e) {
            System.out.println("Error loading class " + name + " (" + e.getMessage() + ")");
        } catch (final Exception e) {
            System.out.println("Error processing class " + name + " (" + e.getMessage() + ")");
        }
    }
}
