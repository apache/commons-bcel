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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.bcel.Constants;
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
 * Disassemble Java class object into the <a href="http://jasmin.sourceforge.net">
 * Jasmin</a> format.
 *
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 */
public class JasminVisitor extends org.apache.bcel.classfile.EmptyVisitor {
    private JavaClass clazz;
    private PrintWriter out;
    private String class_name;
    private ConstantPoolGen cp;

    public JasminVisitor(JavaClass clazz, OutputStream out) {
        this.clazz = clazz;
        this.out = new PrintWriter(out);
        this.class_name = clazz.getClassName();
        this.cp = new ConstantPoolGen(clazz.getConstantPool());
    }

    /**
     * Start traversal using DefaultVisitor pattern.
     */
    public void disassemble() {
        new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
        out.close();
    }

    @Override
    public void visitJavaClass(JavaClass clazz) {
        out.println(";; Produced by JasminVisitor (BCEL)");
        out.println(";; http://commons.apache.org/bcel/");
        out.println(";; " + new Date() + "\n");

        out.println(".source " + clazz.getSourceFileName());
        out.println("." + Utility.classOrInterface(clazz.getAccessFlags()) + " " +
                Utility.accessToString(clazz.getAccessFlags(), true) +
                " " + clazz.getClassName().replace('.', '/'));
        out.println(".super " + clazz.getSuperclassName().replace('.', '/'));

        for (String iface : clazz.getInterfaceNames()) {
            out.println(".implements " + iface.replace('.', '/'));
        }

        out.print("\n");
    }

    @Override
    public void visitField(Field field) {
        out.print(".field " + Utility.accessToString(field.getAccessFlags()) +
                " \"" + field.getName() + "\"" + field.getSignature());
        if (field.getAttributes().length == 0) {
            out.print("\n");
        }
    }

    @Override
    public void visitConstantValue(ConstantValue cv) {
        out.println(" = " + cv);
    }

    private Method _method;

    /**
     * Unfortunately Jasmin expects ".end method" after each method. Thus we've to check
     * for every of the method's attributes if it's the last one and print ".end method"
     * then.
     */
    private void printEndMethod(Attribute attr) {
        Attribute[] attributes = _method.getAttributes();

        if (attr == attributes[attributes.length - 1]) {
            out.println(".end method");
        }
    }

    @Override
    public void visitDeprecated(Deprecated attribute) {
        printEndMethod(attribute);
    }

    @Override
    public void visitSynthetic(Synthetic attribute) {
        if (_method != null) {
            printEndMethod(attribute);
        }
    }

    @Override
    public void visitMethod(Method method) {
        this._method = method; // Remember for use in subsequent visitXXX calls

        out.println("\n.method " + Utility.accessToString(_method.getAccessFlags()) +
                " " + _method.getName() + _method.getSignature());

        Attribute[] attributes = _method.getAttributes();
        if ((attributes == null) || (attributes.length == 0)) {
            out.println(".end method");
        }
    }

    @Override
    public void visitExceptionTable(ExceptionTable e) {
        for (String name : e.getExceptionNames()) {
            out.println(".throws " + name.replace('.', '/'));
        }

        printEndMethod(e);
    }

    private Hashtable<InstructionHandle, String> map;

    @Override
    public void visitCode(Code code) {
        int label_counter = 0;

        out.println(".limit stack " + code.getMaxStack());
        out.println(".limit locals " + code.getMaxLocals());

        MethodGen mg = new MethodGen(_method, class_name, cp);
        InstructionList il = mg.getInstructionList();
        InstructionHandle[] ihs = il.getInstructionHandles();

    /* Pass 1: Give all referenced instruction handles a symbolic name, i.e. a
     * label.
     */
        map = new Hashtable<InstructionHandle, String>();

        for (InstructionHandle ih1 : ihs) {
            if (ih1 instanceof BranchHandle) {
                BranchInstruction bi = (BranchInstruction) ih1.getInstruction();

                if (bi instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
                    for (InstructionHandle target : ((Select) bi).getTargets()) {
                        put(target, "Label" + label_counter++ + ":");
                    }
                }

                InstructionHandle ih = bi.getTarget();
                put(ih, "Label" + label_counter++ + ":");
            }
        }

        LocalVariableGen[] lvs = mg.getLocalVariables();
        for (LocalVariableGen lv : lvs) {
            InstructionHandle ih = lv.getStart();
            put(ih, "Label" + label_counter++ + ":");
            ih = lv.getEnd();
            put(ih, "Label" + label_counter++ + ":");
        }

        CodeExceptionGen[] ehs = mg.getExceptionHandlers();
        for (CodeExceptionGen c : ehs) {
            InstructionHandle ih = c.getStartPC();

            put(ih, "Label" + label_counter++ + ":");
            ih = c.getEndPC();
            put(ih, "Label" + label_counter++ + ":");
            ih = c.getHandlerPC();
            put(ih, "Label" + label_counter++ + ":");
        }

        LineNumberGen[] lns = mg.getLineNumbers();
        for (LineNumberGen ln : lns) {
            InstructionHandle ih = ln.getInstruction();
            put(ih, ".line " + ln.getSourceLine());
        }

        // Pass 2: Output code.
        for (LocalVariableGen l : lvs) {
            out.println(".var " + l.getIndex() + " is " + l.getName() + " " +
                    l.getType().getSignature() +
                    " from " + get(l.getStart()) +
                    " to " + get(l.getEnd()));
        }

        out.print("\n");

        for (InstructionHandle ih : ihs) {
            Instruction inst = ih.getInstruction();
            String str = map.get(ih);

            if (str != null) {
                out.println(str);
            }

            if (inst instanceof BranchInstruction) {
                if (inst instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
                    Select s = (Select) inst;
                    int[] matchs = s.getMatchs();
                    InstructionHandle[] targets = s.getTargets();

                    if (s instanceof TABLESWITCH) {
                        out.println("\ttableswitch " + matchs[0] + " " + matchs[matchs.length - 1]);

                        for (InstructionHandle target : targets) {
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
                    BranchInstruction bi = (BranchInstruction) inst;
                    ih = bi.getTarget();
                    str = get(ih);
                    out.println("\t" + Constants.OPCODE_NAMES[bi.getOpcode()] + " " + str);
                }
            } else {
                out.println("\t" + inst.toString(cp.getConstantPool()));
            }
        }

        out.print("\n");

        for (CodeExceptionGen c : ehs) {
            ObjectType caught = c.getCatchType();
            String class_name = (caught == null) ?  // catch any exception, used when compiling finally
                    "all" : caught.getClassName().replace('.', '/');

            out.println(".catch " + class_name + " from " +
                    get(c.getStartPC()) + " to " + get(c.getEndPC()) +
                    " using " + get(c.getHandlerPC()));
        }

        printEndMethod(code);
    }

    private String get(InstructionHandle ih) {
        String str = new StringTokenizer(map.get(ih), "\n").nextToken();
        return str.substring(0, str.length() - 1);
    }

    private void put(InstructionHandle ih, String line) {
        String str = map.get(ih);

        if (str == null) {
            map.put(ih, line);
        } else {
            if (line.startsWith("Label") || str.endsWith(line)) {
                return;
            }

            map.put(ih, str + "\n" + line); // append
        }
    }

    public static void main(String[] argv) throws Exception {
        JavaClass java_class;

        if (argv.length == 0) {
            System.err.println("disassemble: No input files specified");
            return;
        }

        for (String arg : argv) {
            if ((java_class = Repository.lookupClass(arg)) == null) {
                java_class = new ClassParser(arg).parse();
            }

            String class_name = java_class.getClassName();
            int index = class_name.lastIndexOf('.');
            String path = class_name.substring(0, index + 1).replace('.', File.separatorChar);
            class_name = class_name.substring(index + 1);

            if (!path.equals("")) {
                File f = new File(path);
                f.mkdirs();
            }

            String name = path + class_name + ".j";
            FileOutputStream out = new FileOutputStream(name);
            new JasminVisitor(java_class, out).disassemble();
            System.out.println("File dumped to: " + name);
        }
    }
}
