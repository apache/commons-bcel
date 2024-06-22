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
package Mini;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Vector;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;

public class MiniC {
    private static Vector<String> errors;
    private static Vector<String> warnings;
    private static String file;
    private static int pass;

    final static void addError(final int line, final int column, final String err) {
        if (pass != 2) {
            errors.addElement(file + ":" + fillup(line, 3) + "," + fillup(column, 2) + ": " + err);
        }
    }

    final static void addWarning(final int line, final int column, final String err) {
        warnings.addElement("Warning: " + file + ":" + fillup(line, 3) + "," + fillup(column, 3) + ": " + err);
    }

    final static void addWarning(final String err) {
        warnings.addElement(err);
    }

    final static String fillup(final int n, final int len) {
        final String str = Integer.toString(n);
        final int diff = len - str.length();

        if (diff <= 0) {
            return str;
        }
        final char[] chs = new char[diff];

        for (int i = 0; i < diff; i++) {
            chs[i] = ' ';
        }

        return new String(chs) + str;
    }

    public static void main(final String[] argv) {
        final String[] fileName = new String[argv.length];

        int files = 0;
        MiniParser parser;
        String baseName;
        boolean byteCode = true;

        try {
            /*
             * Parse command line arguments.
             */
            for (final String element : argv) {
                if (element.charAt(0) == '-') { // command line switch
                    if (element.equals("-java")) {
                        byteCode = false;
                    } else if (element.equals("-bytecode")) {
                        byteCode = true;
                    } else {
                        throw new Exception("Unknown option: " + element);
                    }
                } else { // add file name to list
                    fileName[files++] = element;
                }
            }

            if (files == 0) {
                System.err.println("Nothing to compile.");
            }

            for (int j = 0; j < files; j++) {
                errors = new Vector<>();
                warnings = new Vector<>();
                pass = 0;

                if (j == 0) {
                    // side-effects
                    parser = new MiniParser(new FileInputStream(fileName[0]));
                    Objects.requireNonNull(parser, "parser");
                } else {
                    MiniParser.ReInit(new FileInputStream(fileName[j]));
                }

                int index = fileName[j].lastIndexOf('.');
                if (index > 0) {
                    baseName = fileName[j].substring(0, index);
                } else {
                    baseName = fileName[j];
                }

                if ((index = baseName.lastIndexOf(File.separatorChar)) > 0) {
                    baseName = baseName.substring(index + 1);
                }

                file = fileName[j];

                System.out.println("Parsing...");
                MiniParser.Program();
                ASTProgram program = (ASTProgram) MiniParser.jjtree.rootNode();

                System.out.println("Pass 1: Optimizing parse tree...");
                pass = 1;
                program = program.traverse();
                // program.dump(">");

                if (errors.isEmpty()) {
                    System.out.println("Pass 2: Type checking (I)...");
                    program.eval(pass = 2);
                }

                if (errors.isEmpty()) {
                    System.out.println("Pass 3: Type checking (II)...");
                    program.eval(pass = 3);
                }

                errors.forEach(System.out::println);

                warnings.forEach(System.out::println);

                if (errors.isEmpty()) {
                    if (byteCode) {
                        System.out.println("Pass 5: Generating byte code...");
                        final ClassGen classGen = new ClassGen(baseName, "java.lang.Object", fileName[j],
                                Const.ACC_PUBLIC | Const.ACC_FINAL | Const.ACC_SUPER, null);
                        final ConstantPoolGen cp = classGen.getConstantPool();

                        program.byte_code(classGen, cp);
                        final JavaClass clazz = classGen.getJavaClass();
                        clazz.dump(baseName + JavaClass.EXTENSION);
                    } else {
                        System.out.println("Pass 5: Generating Java code...");
                        try (final PrintWriter out = new PrintWriter(baseName + ".java", StandardCharsets.UTF_8.name())) {
                            program.code(out, baseName);
                        }

                        System.out.println("Pass 6: Compiling Java code...");

                        final String[] args = { "javac", baseName + ".java" };
                        // sun.tools.javac.Main compiler = new sun.tools.javac.Main(System.err, "javac");
                        try {
                            final Process p = Runtime.getRuntime().exec(args);
                            p.waitFor();
                        } catch (final Exception e) {
                            System.out.println(e);
                        }

                        // compiler.compile(args);
                    }
                }

                if (!errors.isEmpty() || !warnings.isEmpty()) {
                    System.out.println(errors.size() + " errors and " + warnings.size() + " warnings.");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
