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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LineNumberGen;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.TABLESWITCH;

/**
 * Disassemble Java class object into the <a href="https://jasmin.sourceforge.net"> Jasmin</a> format.
 */
public class JasminVisitor extends org.apache.bcel.classfile.EmptyVisitor {
    public static void main(final String[] argv) throws Exception {
        JavaClass javaClass;

        if (argv.length == 0) {
            System.err.println("disassemble: No input files specified");
            return;
        }

        for (final String arg : argv) {
            if ((javaClass = Repository.lookupClass(arg)) == null) {
                javaClass = new ClassParser(arg).parse();
            }

            String className = javaClass.getClassName();
            final int index = className.lastIndexOf('.');
            final String path = className.substring(0, index + 1).replace('.', File.separatorChar);
            className = className.substring(index + 1);

            if (!path.isEmpty()) {
                final File f = new File(path);
                f.mkdirs();
            }

            final String name = path + className + ".j";
            try (final FileOutputStream out = new FileOutputStream(name)) {
                new JasminVisitor(javaClass, out).disassemble();
                System.out.println("File dumped to: " + name);
            }
        }
    }

    private final JavaClass clazz;
    private final PrintWriter out;
    private final String className;

    private final ConstantPoolGen cp;

    private Method _method;

    private Hashtable<InstructionHandle, String> map;

    public JasminVisitor(final JavaClass clazz, final OutputStream out) {
        this.clazz = clazz;
        this.out = new PrintWriter(out);
        this.className = clazz.getClassName();
        this.cp = new ConstantPoolGen(clazz.getConstantPool());
    }

    /**
     * Start traversal using DefaultVisitor pattern.
     */
    public void disassemble() {
        new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
        out.close();
    }

    private String get(final InstructionHandle ih) {
        final String str = new StringTokenizer(map.get(ih), "\n").nextToken();
        return str.substring(0, str.length() - 1);
    }

    /**
     * Unfortunately Jasmin expects ".end method" after each method. Thus we've to check for every of the method's
     * attributes if it's the last one and print ".end method" then.
     */
    private void printEndMethod(final Attribute attr) {
        final Attribute[] attributes = _method.getAttributes();

        if (attr == attributes[attributes.length - 1]) {
            out.println(".end method");
        }
    }

    private void put(final InstructionHandle ih, final String line) {
        final String str = map.get(ih);

        if (str == null) {
            map.put(ih, line);
        } else {
            if (line.startsWith("Label") || str.endsWith(line)) {
                return;
            }

            map.put(ih, str + "\n" + line); // append
        }
    }

    @Override
    public void visitCode(final Code code) {
        int labelCounter = 0;

        out.println(".limit stack " + code.getMaxStack());
        out.println(".limit locals " + code.getMaxLocals());

        final MethodGen mg = new MethodGen(_method, className, cp);
        final InstructionList il = mg.getInstructionList();
        final InstructionHandle[] ihs = il.getInstructionHandles();

        /*
         * Pass 1: Give all referenced instruction handles a symbolic name, i.e. a label.
         */
        map = new Hashtable<>();

        for (final InstructionHandle ih1 : ihs) {
            if (ih1 instanceof BranchHandle) {
                final BranchInstruction bi = (BranchInstruction) ih1.getInstruction();

                if (bi instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
                    for (final InstructionHandle target : ((Select) bi).getTargets()) {
                        put(target, "Label" + labelCounter++ + ":");
                    }
                }

                final InstructionHandle ih = bi.getTarget();
                put(ih, "Label" + labelCounter++ + ":");
            }
        }

        final LocalVariableGen[] lvs = mg.getLocalVariables();
        for (final LocalVariableGen lv : lvs) {
            InstructionHandle ih = lv.getStart();
            put(ih, "Label" + labelCounter++ + ":");
            ih = lv.getEnd();
            put(ih, "Label" + labelCounter++ + ":");
        }

        final CodeExceptionGen[] ehs = mg.getExceptionHandlers();
        for (final CodeExceptionGen c : ehs) {
            InstructionHandle ih = c.getStartPC();

            put(ih, "Label" + labelCounter++ + ":");
            ih = c.getEndPC();
            put(ih, "Label" + labelCounter++ + ":");
            ih = c.getHandlerPC();
            put(ih, "Label" + labelCounter++ + ":");
        }

        final LineNumberGen[] lns = mg.getLineNumbers();
        for (final LineNumberGen ln : lns) {
            final InstructionHandle ih = ln.getInstruction();
            put(ih, ".line " + ln.getSourceLine());
        }

        // Pass 2: Output code.
        for (final LocalVariableGen l : lvs) {
            out.println(
                ".var " + l.getIndex() + " is " + l.getName() + " " + l.getType().getSignature() + " from " + get(l.getStart()) + " to " + get(l.getEnd()));
        }

        out.print("\n");

        for (InstructionHandle ih : ihs) {
            final Instruction inst = ih.getInstruction();
            String str = map.get(ih);

            if (str != null) {
                out.println(str);
            }

            if (inst instanceof BranchInstruction) {
                if (inst instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
                    final Select s = (Select) inst;
                    final int[] matchs = s.getMatchs();
                    final InstructionHandle[] targets = s.getTargets();

                    if (s instanceof TABLESWITCH) {
                        out.println("\ttableswitch " + matchs[0] + " " + matchs[matchs.length - 1]);

                        for (final InstructionHandle target : targets) {
                            out.println("\t\t" + get(target));
                        }

                    } else { // LOOKUPSWITCH
                        out.println("\tlookupswitch ");

                        for (int j = 0; j < targets.length; j++) {
                            out.println("\t\t" + matchs[j] + " : " + get(targets[j]));
                        }
                    }

                    out.println("\t\tdefault: " + get(s.getTarget())); // Applies for both
                } else {
                    final BranchInstruction bi = (BranchInstruction) inst;
                    ih = bi.getTarget();
                    str = get(ih);
                    out.println("\t" + Const.getOpcodeName(bi.getOpcode()) + " " + str);
                }
            } else {
                out.println("\t" + inst.toString(cp.getConstantPool()));
            }
        }

        out.print("\n");

        for (final CodeExceptionGen c : ehs) {
            final ObjectType caught = c.getCatchType();
            final String className = caught == null ? // catch any exception, used when compiling finally
                "all" : Utility.packageToPath(caught.getClassName());

            out.println(".catch " + className + " from " + get(c.getStartPC()) + " to " + get(c.getEndPC()) + " using " + get(c.getHandlerPC()));
        }

        printEndMethod(code);
    }

    @Override
    public void visitConstantValue(final ConstantValue cv) {
        out.println(" = " + cv);
    }

    @Override
    public void visitDeprecated(final Deprecated attribute) {
        printEndMethod(attribute);
    }

    @Override
    public void visitExceptionTable(final ExceptionTable e) {
        for (final String name : e.getExceptionNames()) {
            out.println(".throws " + Utility.packageToPath(name));
        }

        printEndMethod(e);
    }

    @Override
    public void visitField(final Field field) {
        out.print(".field " + Utility.accessToString(field.getAccessFlags()) + " \"" + field.getName() + "\"" + field.getSignature());
        if (field.getAttributes().length == 0) {
            out.print("\n");
        }
    }

    @Override
    public void visitJavaClass(final JavaClass clazz) {
        out.println(";; Produced by JasminVisitor (BCEL)");
        out.println(";; https://commons.apache.org/bcel/");
        out.println(";; " + new Date() + "\n");

        out.println(".source " + clazz.getSourceFileName());
        out.println("." + Utility.classOrInterface(clazz.getAccessFlags()) + " " + Utility.accessToString(clazz.getAccessFlags(), true) + " "
            + Utility.packageToPath(clazz.getClassName()));
        out.println(".super " + Utility.packageToPath(clazz.getSuperclassName()));

        for (final String iface : clazz.getInterfaceNames()) {
            out.println(".implements " + Utility.packageToPath(iface));
        }

        out.print("\n");
    }

    @Override
    public void visitMethod(final Method method) {
        this._method = method; // Remember for use in subsequent visitXXX calls

        out.println("\n.method " + Utility.accessToString(_method.getAccessFlags()) + " " + _method.getName() + _method.getSignature());

        final Attribute[] attributes = _method.getAttributes();
        if (attributes == null || attributes.length == 0) {
            out.println(".end method");
        }
    }

    @Override
    public void visitSynthetic(final Synthetic attribute) {
        if (_method != null) {
            printEndMethod(attribute);
        }
    }
}
