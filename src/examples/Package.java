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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassPath;

/**
 * Package the client. Creates a jar file in the current directory
 * that contains a minimal set of classes needed to run the client.
 *
 * Use BCEL to extract class names and read/write classes
 *
 * @author First Hop Ltd / Torsten Rueger
 */
public class Package {

    /**
     * The name of the resulting jar is Client.jar
     */
    static String defaultJar = "Client.jar";

    /*
     * See usage() for arguments. Create an instance and run that
     *(just so not all members have to be static)
     */
    static void main(String args[]) {
        Package instance = new Package();
        try {
            instance.go(args);
        } catch (Exception e) {
            e.printStackTrace();
            instance.usage();
        }
    }

    /**
     * We use a "default ClassPath object which uses the environments
     * CLASSPATH
     */
    ClassPath classPath = ClassPath.SYSTEM_CLASS_PATH;

    /**
     * A map for all Classes, the ones we're going to package.
     * Store class name against the JavaClass. From the JavaClass
     * we get the bytes to create the jar.
     */
    Map<String, JavaClass> allClasses = new TreeMap<String, JavaClass>();

    /**
     * We start at the root classes, put them in here, then go through
     * this list, putting dependent classes in here and from there
     * into allClasses. Store class names against class names of their dependents
     */
    TreeMap<String, String> dependents = new TreeMap<String, String>();

    /**
     * Collect all classes that could not be found in the classpath.
     * Store class names against class names of their dependents
     */
    TreeMap<String, String> notFound = new TreeMap<String, String>();

    /**
     * See wheather we print the classes that were not found (default = false)
     */
    boolean showNotFound = false;
    /**
     * Remember wheather to print allClasses at the end (default = false)
     */
    boolean printClasses = false;
    /**
     * Wheather we log classes during processing (default = false)
     */
    boolean log = false;

    public void usage() {
        System.out.println(" This program packages classes and all their dependents");
        System.out.println(" into one jar. Give all starting classes (your main)");
        System.out.println(" on the command line. Use / as separator, the .class is");
        System.out.println(" optional. We use the environments CLASSPATH to resolve");
        System.out.println(" classes. Anything but java.* packages are packaged.");
        System.out.println(" If you use Class.forName (or similar), be sure to");
        System.out.println(" include the classes that you load dynamically on the");
        System.out.println(" command line.\n");
        System.out.println(" These options are recognized:");
        System.out.println(" -e -error  Show errors, meaning classes that could not ");
        System.out.println("   resolved + the classes that referenced them.");
        System.out.println(" -l -log  Show classes as they are processed. This will");
        System.out.println("   include doubles, java classes and is difficult to");
        System.out.println("   read. I use it as a sort of progress monitor");
        System.out.println(" -s -show  Prints all the classes that were packaged");
        System.out.println("   in alphabetical order, which is ordered by package");
        System.out.println("   for the most part.");
    }

    /**
     * the main of this class
     */
    void go(String[] args) throws IOException {
        JavaClass clazz;
        // sort the options
        for (String arg : args) {
            if (arg.startsWith("-e")) {
                showNotFound = true;
                continue;
            }
            if (arg.startsWith("-s")) {
                printClasses = true;
                continue;
            }
            if (arg.startsWith("-l")) {
                log = true;
                continue;
            }
            String clName = arg;
            if (clName.endsWith(".class")) {
                clName = clName.substring(0, clName.length() - 6);
            }
            clName = clName.replace('.', '/');
            clazz = new ClassParser(classPath.getInputStream(clName), clName).parse();
            // here we create the root set of classes to process
            addDependents(clazz);
            System.out.println("Packaging for class: " + clName);
        }

        if (dependents.isEmpty()) {
            usage();
            return;
        }

        System.out.println("Creating jar file: " + defaultJar);

        // starting processing: Grab from the dependents list an add back to it
        // and the allClasses list. see addDependents
        while (!dependents.isEmpty()) {
            String name = dependents.firstKey();
            String from = dependents.remove(name);
            if (allClasses.get(name) == null) {
                try {
                    InputStream is = classPath.getInputStream(name);
                    clazz = new ClassParser(is, name).parse();
                    addDependents(clazz);
                } catch (IOException e) {
                    //System.err.println("Error, class not found " + name );
                    notFound.put(name, from);
                }
            }
        }

        if (printClasses) { // if wanted show all classes
            printAllClasses();
        }

        // create the jar
        JarOutputStream jarFile = new JarOutputStream(new FileOutputStream(defaultJar));
        jarFile.setLevel(5); // use compression
        int written = 0;
        for (String name : allClasses.keySet()) { // add entries for every class
            JavaClass claz = allClasses.get(name);
            ZipEntry zipEntry = new ZipEntry(name + ".class");
            byte[] bytes = claz.getBytes();
            int length = bytes.length;
            jarFile.putNextEntry(zipEntry);
            jarFile.write(bytes, 0, length);
            written += length;  // for logging
        }
        jarFile.close();
        System.err.println("The jar file contains " + allClasses.size()
                + " classes and contains " + written + " bytes");

        if (!notFound.isEmpty()) {
            System.err.println(notFound.size() + " classes could not be found");
            if (showNotFound) { // if wanted show the actual classes that we not found
                while (!notFound.isEmpty()) {
                    String name = notFound.firstKey();
                    System.err.println(name + " (" + notFound.remove(name) + ")");
                }
            } else {
                System.err.println("Use '-e' option to view classes that were not found");
            }
        }
    }

    /**
     * Print all classes that were packaged. Sort alphabetically for better
     * overview. Enabled by -s option
     */
    void printAllClasses() {
        List<String> names = new ArrayList<String>(allClasses.keySet());
        Collections.sort(names);
        for (int i = 0; i < names.size(); i++) {
            String cl = names.get(i);
            System.err.println(cl);
        }
    }

    /**
     * Add this class to allClasses. Then go through all its dependents
     * and add them to the dependents list if they are not in allClasses
     */
    void addDependents(JavaClass clazz) throws IOException {
        String name = clazz.getClassName().replace('.', '/');
        allClasses.put(name, clazz);
        ConstantPool pool = clazz.getConstantPool();
        for (int i = 1; i < pool.getLength(); i++) {
            Constant cons = pool.getConstant(i);
            //System.out.println("("+i+") " + cons );
            if (cons != null && cons.getTag() == Constants.CONSTANT_Class) {
                int idx = ((ConstantClass) pool.getConstant(i)).getNameIndex();
                String clas = ((ConstantUtf8) pool.getConstant(idx)).getBytes();
                addClassString(clas, name);
            }
        }
    }

    /**
     * add given class to dependents (from is where its dependent from)
     * some fiddeling to be done because of array class notation
     */
    void addClassString(String clas, String from) throws IOException {
        if (log) {
            System.out.println("processing: " + clas + " referenced by " + from);
        }

        // must check if it's an arrary (start with "[")
        if (clas.startsWith("[")) {
            if (clas.length() == 2) {
                // it's an array of built in type, ignore
                return;
            }
            if ('L' == clas.charAt(1)) {
                // it's an array of objects, the class name is between [L and ;
                // like    [Ljava/lang/Object;
                addClassString(clas.substring(2, clas.length() - 1), from);
                return;
            }
            if ('[' == clas.charAt(1)) {
                // it's an array of arrays, call recursive
                addClassString(clas.substring(1), from);
                return;
            }
            throw new IOException("Can't recognize class name =" + clas);
        }

        if (!clas.startsWith("java/") && allClasses.get(clas) == null) {
            dependents.put(clas, from);
            //      System.out.println("       yes" );
        } else {
            //      System.out.println("       no" );
        }
    }
}
