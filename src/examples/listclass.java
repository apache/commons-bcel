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
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.Constants;
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
 * <pre>java listclass [-constants] [-code] [-brief] [-dependencies] [-nocontents] [-recurse] class... [-exclude <list>]</pre>
 * where
 * <ul>
 * <li>{@code -code} List byte code of methods</li>
 * <li>{@code -brief} List byte codes briefly</li>
 * <li>{@code -constants} Print constants table (constant pool)</li>
 * <li>{@code -recurse}  Usually intended to be used along with
 * {@code -dependencies}  When this flag is set, listclass will also print information
 * about all classes which the target class depends on.</li>
 *
 * <li>{@code -dependencies}  Setting this flag makes listclass print a list of all
 * classes which the target class depends on.  Generated from getting all
 * CONSTANT_Class constants from the constant pool.</li>
 *
 * <li>{@code -exclude}  All non-flag arguments after this flag are added to an
 * 'exclusion list'.  Target classes are compared with the members of the
 * exclusion list.  Any target class whose fully qualified name begins with a
 * name in the exclusion list will not be analyzed/listed.  This is meant
 * primarily when using both {@code -recurse} to exclude java, javax, and sun classes,
 * and is recommended as otherwise the output from {@code -recurse} gets quite long and
 * most of it is not interesting.  Note that {@code -exclude} prevents listing of
 * classes, it does not prevent class names from being printed in the
 * {@code -dependencies} list.</li>
 * <li>{@code -nocontents} Do not print JavaClass.toString() for the class. I added
 * this because sometimes I'm only interested in dependency information.</li>
 * </ul>
 * <p>Here's a couple examples of how I typically use listclass:<br>
 * <pre>java listclass -code MyClass</pre>
 * Print information about the class and the byte code of the methods
 * <pre>java listclass -nocontents -dependencies MyClass</pre>
 * Print a list of all classes which MyClass depends on.
 * <pre>java listclass -nocontents -recurse MyClass -exclude java. javax. sun.</pre>
 * Print a recursive listing of all classes which MyClass depends on.  Do not
 * analyze classes beginning with "java.", "javax.", or "sun.".
 * <pre>java listclass -nocontents -dependencies -recurse MyClass -exclude java.javax. sun.</pre>
 * Print a recursive listing of dependency information for MyClass and its
 * dependents.  Do not analyze classes beginning with "java.", "javax.", or "sun."
 * </p>
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>,
 *         <a href="mailto:twheeler@objectspace.com">Thomas Wheeler</A>
 * @version $Id$
 */
public class listclass {

    boolean code;
    boolean constants;
    boolean verbose;
    boolean classdep;
    boolean nocontents;
    boolean recurse;
    Map<String, String> listedClasses;
    List<String> exclude_name;

    public static void main(String[] argv) {
        List<String> file_name = new ArrayList<String>();
        List<String> exclude_name = new ArrayList<String>();
        boolean code = false;
        boolean constants = false;
        boolean verbose = true;
        boolean classdep = false;
        boolean nocontents = false;
        boolean recurse = false;
        boolean exclude = false;
        String name;

        // Parse command line arguments.
        for (String arg : argv) {
            if (arg.charAt(0) == '-') {  // command line switch
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
                    System.out.println("Usage: java listclass [-constants] [-code] [-brief] " +
                            "[-dependencies] [-nocontents] [-recurse] class... " +
                            "[-exclude <list>]\n" +
                            "-constants       Print constants table (constant pool)\n" +
                            "-code            Dump byte code of methods\n" +
                            "-brief           Brief listing\n" +
                            "-dependencies    Show class dependencies\n" +
                            "-nocontents      Do not print field/method information\n" +
                            "-recurse         Recurse into dependent classes\n" +
                            "-exclude <list>  Do not list classes beginning with " +
                            "strings in <list>");
                    System.exit(0);
                } else {
                    System.err.println("Unknown switch " + arg + " ignored.");
                }
            } else { // add file name to list
                if (exclude) {
                    exclude_name.add(arg);
                } else {
                    file_name.add(arg);
                }
            }
        }

        if (file_name.size() == 0) {
            System.err.println("list: No input files specified");
        } else {
            listclass listClass = new listclass(code, constants, verbose, classdep,
                    nocontents, recurse, exclude_name);

            for (int i = 0; i < file_name.size(); i++) {
                name = file_name.get(i);

                listClass.list(name);
            }
        }
    }

    public listclass(boolean code, boolean constants, boolean verbose, boolean classdep,
                     boolean nocontents, boolean recurse, List<String> exclude_name) {
        this.code = code;
        this.constants = constants;
        this.verbose = verbose;
        this.classdep = classdep;
        this.nocontents = nocontents;
        this.recurse = recurse;
        this.listedClasses = new HashMap<String, String>();
        this.exclude_name = exclude_name;
    }

    /**
     * Print the given class on screen
     */
    public void list(String name) {
        try {
            JavaClass java_class;

            if ((listedClasses.get(name) != null) || name.startsWith("[")) {
                return;
            }

            for (int idx = 0; idx < exclude_name.size(); idx++) {
                if (name.startsWith(exclude_name.get(idx))) {
                    return;
                }
            }

            if (name.endsWith(".class")) {
                java_class = new ClassParser(name).parse(); // May throw IOException
            } else {
                java_class = Repository.lookupClass(name);
            }

            if (nocontents) {
                System.out.println(java_class.getClassName());
            } else {
                System.out.println(java_class);             // Dump the contents
            }

            if (constants) {
                System.out.println(java_class.getConstantPool());
            }

            if (code) {
                printCode(java_class.getMethods(), verbose);
            }

            if (classdep) {
                printClassDependencies(java_class.getConstantPool());
            }

            listedClasses.put(name, name);

            if (recurse) {
                String[] dependencies = getClassDependencies(java_class.getConstantPool());

                for (String dependency : dependencies) {
                    list(dependency);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading class " + name + " (" + e.getMessage() + ")");
        } catch (Exception e) {
            System.out.println("Error processing class " + name + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Dump the list of classes this class is dependent on
     */
    public static void printClassDependencies(ConstantPool pool) {
        System.out.println("Dependencies:");
        for (String name : getClassDependencies(pool)) {
            System.out.println("\t" + name);
        }
    }

    public static String[] getClassDependencies(ConstantPool pool) {
        String[] tempArray = new String[pool.getLength()];
        int size = 0;
        StringBuilder buf = new StringBuilder();

        for (int idx = 0; idx < pool.getLength(); idx++) {
            Constant c = pool.getConstant(idx);
            if (c != null && c.getTag() == Constants.CONSTANT_Class) {
                ConstantUtf8 c1 = (ConstantUtf8) pool.getConstant(((ConstantClass) c).getNameIndex());
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

        String[] dependencies = new String[size];
        System.arraycopy(tempArray, 0, dependencies, 0, size);
        return dependencies;
    }

    /**
     * Dump the disassembled code of all methods in the class.
     */
    public static void printCode(Method[] methods, boolean verbose) {
        for (Method method : methods) {
            System.out.println(method);

            Code code = method.getCode();
            if (code != null) {
                System.out.println(code.toString(verbose));
            }
        }
    }
}
