/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.IntFunction;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.BootstrapMethod;
import org.apache.bcel.classfile.BootstrapMethods;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodHandle;
import org.apache.bcel.classfile.ConstantMethodType;
import org.apache.bcel.classfile.ConstantModule;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPackage;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class takes a given JavaClass object and converts it to a Java program that creates that very class using BCEL.
 * This gives new users of BCEL a useful example showing how things are done with BCEL. It does not cover all features
 * of BCEL, but tries to mimic hand-written code as close as possible.
 */
public class BCELifier extends org.apache.bcel.classfile.EmptyVisitor {

    /**
     * Enum corresponding to flag source.
     */
    public enum FLAGS {

        /** Unknown flag source. */
        UNKNOWN,

        /** Class flag source. */
        CLASS,

        /** Method flag source. */
        METHOD,
    }

    // The base package name for imports; assumes Const is at the top level
    // N.B we use the class so renames will be detected by the compiler/IDE
    private static final String BASE_PACKAGE = Const.class.getPackage().getName();
    private static final String CONSTANT_PREFIX = Const.class.getSimpleName() + ".";

    // Bound each bootstrap expression so a batch of array assignments stays below the JVM's method-size limit.
    private static final int MAX_INLINE_BOOTSTRAP_ARGUMENTS = 32;

    /**
     * Checks that a name from the parsed class file is a dotted sequence of valid Java identifiers before it is
     * emitted in identifier position of the generated source. The class file format allows characters in names (for
     * example braces, parentheses or newlines) that the Java language does not, so an unchecked name from a crafted
     * class file could inject arbitrary code into the generated program.
     *
     * @param name the class or package name to check.
     * @return {@code name} if it is safe to emit as a Java identifier.
     * @throws IllegalArgumentException Thrown if the name is not a dotted sequence of valid Java identifiers.
     */
    private static String checkJavaName(final String name) {
        boolean expectStart = true;
        for (int i = 0; i < name.length(); i++) {
            final char ch = name.charAt(i);
            if (expectStart) {
                if (!Character.isJavaIdentifierStart(ch)) {
                    throw new IllegalArgumentException("Invalid Java identifier in class file: " + Utility.convertString(name));
                }
                expectStart = false;
            } else if (ch == '.') {
                expectStart = true;
            } else if (!Character.isJavaIdentifierPart(ch)) {
                throw new IllegalArgumentException("Invalid Java identifier in class file: " + Utility.convertString(name));
            }
        }
        if (expectStart) {
            throw new IllegalArgumentException("Invalid Java identifier in class file: " + Utility.convertString(name));
        }
        return name;
    }

    private static String[] escape(final String[] names) {
        if (names == null) {
            return null;
        }
        final String[] escaped = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            escaped[i] = names[i] == null ? null : Utility.convertString(names[i]);
        }
        return escaped;
    }

    // Needs to be accessible from unit test code
    static JavaClass getJavaClass(final String name) throws ClassNotFoundException, IOException {
        JavaClass javaClass;
        if ((javaClass = Repository.lookupClass(name)) == null) {
            javaClass = new ClassParser(name).parse(); // May throw IOException
        }
        return javaClass;
    }

    /**
     * Default main method.
     *
     * @param argv command line arguments.
     * @throws Exception Thrown if an error occurs.
     */
    public static void main(final String[] argv) throws Exception {
        if (argv.length != 1) {
            System.out.println("Usage: BCELifier className");
            System.out.println("\tThe class must exist on the classpath");
            return;
        }
        final BCELifier bcelifier = new BCELifier(getJavaClass(argv[0]), System.out);
        bcelifier.start();
    }

    static String printArgumentTypes(final Type[] argTypes) {
        if (argTypes.length == 0) {
            return "Type.NO_ARGS";
        }
        final StringBuilder args = new StringBuilder();
        for (int i = 0; i < argTypes.length; i++) {
            args.append(printType(argTypes[i]));
            if (i < argTypes.length - 1) {
                args.append(", ");
            }
        }
        return "new Type[] { " + args.toString() + " }";
    }

    static String printFlags(final int flags) {
        return printFlags(flags, FLAGS.UNKNOWN);
    }

    /**
     * Return a string with the flag settings
     *
     * @param flags The flags field to interpret.
     * @param location The item type.
     * @return The formatted string.
     * @since 6.0 made public
     */
    public static String printFlags(final int flags, final FLAGS location) {
        if (flags == 0) {
            return "0";
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 0, pow = 1; pow <= Const.MAX_ACC_FLAG_I; i++) {
            if ((flags & pow) != 0) {
                if (pow == Const.ACC_SYNCHRONIZED && location == FLAGS.CLASS) {
                    buf.append(CONSTANT_PREFIX).append("ACC_SUPER | ");
                } else if (pow == Const.ACC_VOLATILE && location == FLAGS.METHOD) {
                    buf.append(CONSTANT_PREFIX).append("ACC_BRIDGE | ");
                } else if (pow == Const.ACC_TRANSIENT && location == FLAGS.METHOD) {
                    buf.append(CONSTANT_PREFIX).append("ACC_VARARGS | ");
                } else if (i < Const.ACCESS_NAMES_LENGTH) {
                    buf.append(CONSTANT_PREFIX).append("ACC_").append(StringUtils.toRootUpperCase(Const.getAccessName(i))).append(" | ");
                } else {
                    buf.append(String.format(CONSTANT_PREFIX + "ACC_BIT %x | ", pow));
                }
            }
            pow <<= 1;
        }
        final String str = buf.toString();
        return str.substring(0, str.length() - 3);
    }

    static String printType(final String signature) {
        final Type type = Type.getType(signature);
        final byte t = type.getType();
        if (t <= Const.T_VOID) {
            return "Type." + StringUtils.toRootUpperCase(Const.getTypeName(t));
        }
        if (type.toString().equals("java.lang.String")) {
            return "Type.STRING";
        }
        if (type.toString().equals("java.lang.Object")) {
            return "Type.OBJECT";
        }
        if (type.toString().equals("java.lang.StringBuffer")) {
            return "Type.STRINGBUFFER";
        }
        if (type instanceof ArrayType) {
            final ArrayType at = (ArrayType) type;
            return "new ArrayType(" + printType(at.getBasicType()) + ", " + at.getDimensions() + ")";
        }
        return "new ObjectType(\"" + Utility.signatureToString(signature, false) + "\")";
    }

    static String printType(final Type type) {
        return printType(type.getSignature());
    }

    private final JavaClass clazz;

    private final PrintWriter printWriter;

    private final ConstantPoolGen constantPoolGen;

    private final boolean preserveConstantPool;

    /**
     * Constructs a new instance.
     *
     * @param clazz Java class to "decompile".
     * @param out where to print the Java program in UTF-8.
     */
    public BCELifier(final JavaClass clazz, final OutputStream out) {
        this.clazz = clazz;
        this.printWriter = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), false);
        this.constantPoolGen = new ConstantPoolGen(this.clazz.getConstantPool());
        this.preserveConstantPool = Arrays.stream(clazz.getAttributes()).anyMatch(BootstrapMethods.class::isInstance);
    }

    /**
     * Prints an array factory with initialization split across bounded methods.
     *
     * @param name the generated factory method name.
     * @param type the Java array element type.
     * @param length the array length.
     * @param initializer the expression for an element, or null to leave it uninitialized.
     */
    private void printArray(final String name, final String type, final int length, final IntFunction<String> initializer) {
        final int batchSize = 100;
        printWriter.println("  private static " + type + "[] " + name + "() {");
        printWriter.println("    " + type + "[] values = new " + type + "[" + length + "];");
        for (int start = 0; start < length; start += batchSize) {
            printWriter.println("    " + name + "_" + start + "(values);");
        }
        printWriter.println("    return values;");
        printWriter.println("  }");
        printWriter.println();
        for (int start = 0; start < length; start += batchSize) {
            printWriter.println("  private static void " + name + "_" + start + "(" + type + "[] values) {");
            for (int i = start; i < Math.min(start + batchSize, length); i++) {
                final String expression = initializer.apply(i);
                if (expression != null) {
                    printWriter.println("    values[" + i + "] = " + expression + ";");
                }
            }
            printWriter.println("  }");
            printWriter.println();
        }
    }

    /**
     * Prints bootstrap-table initialization, splitting large tables and argument lists into bounded methods.
     *
     * @param attribute the bootstrap-methods attribute to recreate.
     */
    private void printBootstrapMethods(final BootstrapMethods attribute) {
        final BootstrapMethod[] methods = attribute.getBootstrapMethods();
        printArray("createBootstrapMethods", "BootstrapMethod", methods.length, index -> {
            final BootstrapMethod method = methods[index];
            final int[] arguments = method.getBootstrapArguments();
            final StringBuilder expression = new StringBuilder("new BootstrapMethod(").append(method.getBootstrapMethodRef()).append(", ");
            if (arguments.length > MAX_INLINE_BOOTSTRAP_ARGUMENTS) {
                expression.append("createBootstrapArguments_").append(index).append("()");
            } else {
                expression.append("new int[] {");
                for (int i = 0; i < arguments.length; i++) {
                    if (i > 0) {
                        expression.append(", ");
                    }
                    expression.append(arguments[i]);
                }
                expression.append('}');
            }
            return expression.append(')').toString();
        });
        for (int i = 0; i < methods.length; i++) {
            final int[] arguments = methods[i].getBootstrapArguments();
            if (arguments.length > MAX_INLINE_BOOTSTRAP_ARGUMENTS) {
                printArray("createBootstrapArguments_" + i, "int", arguments.length, index -> Integer.toString(arguments[index]));
            }
        }
    }

    /**
     * Prints a constant constructor using the original indices.
     *
     * <p>
     * Bootstrap arguments can refer to any loadable constant, including another dynamic constant.
     * </p>
     *
     * @param constant the constant to recreate.
     * @return the Java constructor expression.
     */
    private String printConstant(final Constant constant) {
        final String arguments;
        if (constant instanceof ConstantCP) {
            final ConstantCP reference = (ConstantCP) constant;
            // For dynamic constants, classIndex holds the bootstrap-method index.
            arguments = reference.getClassIndex() + ", " + reference.getNameAndTypeIndex();
        } else if (constant instanceof ConstantClass) {
            arguments = Integer.toString(((ConstantClass) constant).getNameIndex());
        } else if (constant instanceof ConstantDouble) {
            arguments = "Double.longBitsToDouble(" + Double.doubleToLongBits(((ConstantDouble) constant).getBytes()) + "L)";
        } else if (constant instanceof ConstantFloat) {
            arguments = "Float.intBitsToFloat(" + Float.floatToIntBits(((ConstantFloat) constant).getBytes()) + ")";
        } else if (constant instanceof ConstantInteger) {
            arguments = Integer.toString(((ConstantInteger) constant).getBytes());
        } else if (constant instanceof ConstantLong) {
            arguments = ((ConstantLong) constant).getBytes() + "L";
        } else if (constant instanceof ConstantMethodHandle) {
            final ConstantMethodHandle handle = (ConstantMethodHandle) constant;
            arguments = handle.getReferenceKind() + ", " + handle.getReferenceIndex();
        } else if (constant instanceof ConstantMethodType) {
            arguments = Integer.toString(((ConstantMethodType) constant).getDescriptorIndex());
        } else if (constant instanceof ConstantModule) {
            arguments = Integer.toString(((ConstantModule) constant).getNameIndex());
        } else if (constant instanceof ConstantNameAndType) {
            final ConstantNameAndType nameAndType = (ConstantNameAndType) constant;
            arguments = nameAndType.getNameIndex() + ", " + nameAndType.getSignatureIndex();
        } else if (constant instanceof ConstantPackage) {
            arguments = Integer.toString(((ConstantPackage) constant).getNameIndex());
        } else if (constant instanceof ConstantString) {
            arguments = Integer.toString(((ConstantString) constant).getStringIndex());
        } else if (constant instanceof ConstantUtf8) {
            arguments = "\"" + Utility.convertString(((ConstantUtf8) constant).getBytes()) + "\"";
        } else {
            throw new IllegalArgumentException("Unsupported constant: " + constant);
        }
        return "new " + constant.getClass().getSimpleName() + "(" + arguments + ")";
    }

    /**
     * Preserves the constant pool for bootstrap methods so all instruction, method-handle, and bootstrap-argument indices remain valid.
     */
    private void printConstantPool() {
        final Constant[] constants = clazz.getConstantPool().getConstantPool();
        printWriter.println("  private static ConstantPoolGen createConstantPool() {");
        printWriter.println("    return new ConstantPoolGen(createConstants());");
        printWriter.println("  }");
        printWriter.println();
        printArray("createConstants", "Constant", constants.length, index -> constants[index] == null ? null : printConstant(constants[index]));
    }

    private void printCreate() {
        printWriter.println("  public void create(OutputStream out) throws IOException {");
        final Field[] fields = clazz.getFields();
        if (fields.length > 0) {
            printWriter.println("    createFields();");
        }
        final Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            printWriter.println("    createMethod_" + i + "();");
        }
        printWriter.println("    _cg.getJavaClass().dump(out);");
        printWriter.println("  }");
        printWriter.println();
    }

    private void printMain() {
        final String className = checkJavaName(clazz.getClassName());
        printWriter.println("  public static void main(String[] args) throws Exception {");
        printWriter.println("    " + className + "Creator creator = new " + className + "Creator();");
        printWriter.println("    creator.create(new FileOutputStream(\"" + Utility.convertString(className) + ".class\"));");
        printWriter.println("  }");
    }

    /**
     * Start Java code generation
     */
    public void start() {
        visitJavaClass(clazz);
        printWriter.flush();
    }

    @Override
    public void visitBootstrapMethods(final BootstrapMethods bootstrapMethods) {
        printWriter.println("    _cg.addAttribute(new BootstrapMethods(" + bootstrapMethods.getNameIndex() + ", " + bootstrapMethods.getLength()
            + ", createBootstrapMethods(), _cp.getConstantPool()));");
    }

    @Override
    public void visitField(final Field field) {
        printWriter.println();
        printWriter.println("    field = new FieldGen(" + printFlags(field.getAccessFlags()) + ", " + printType(field.getSignature()) + ", \""
            + Utility.convertString(field.getName()) + "\", _cp);");
        final ConstantValue cv = field.getConstantValue();
        if (cv != null) {
            printWriter.print("    field.setInitValue(");
            if (field.getType() == Type.CHAR) {
                printWriter.print("(char)");
            }
            if (field.getType() == Type.SHORT) {
                printWriter.print("(short)");
            }
            if (field.getType() == Type.BYTE) {
                printWriter.print("(byte)");
            }
            if (field.getType() == Type.BOOLEAN) {
                // A boolean ConstantValue is stored as a CONSTANT_Integer, but FieldGen.setInitValue(int) rejects boolean fields.
                // Match HotSpot initialization for noncanonical integer encodings by using the low bit.
                printWriter.print((field.getConstantPool().getConstantInteger(cv.getConstantValueIndex()).getBytes() & 1) != 0);
            } else {
                printWriter.print(cv);
            }
            if (field.getType() == Type.LONG) {
                printWriter.print("L");
            }
            if (field.getType() == Type.FLOAT) {
                printWriter.print("F");
            }
            if (field.getType() == Type.DOUBLE) {
                printWriter.print("D");
            }
            printWriter.println(");");
        }
        printWriter.println("    _cg.addField(field.getField());");
    }

    @Override
    public void visitJavaClass(final JavaClass clazz) {
        String className = checkJavaName(clazz.getClassName());
        final String superName = clazz.getSuperclassName();
        final String packageName = clazz.getPackageName();
        final String inter = Utility.printArray(escape(clazz.getInterfaceNames()), false, true);
        if (StringUtils.isNotEmpty(packageName)) {
            className = className.substring(packageName.length() + 1);
            printWriter.println("package " + packageName + ";");
            printWriter.println();
        }
        printWriter.println("import " + BASE_PACKAGE + ".generic.*;");
        printWriter.println("import " + BASE_PACKAGE + ".classfile.*;");
        printWriter.println("import " + BASE_PACKAGE + ".*;");
        printWriter.println("import java.io.*;");
        printWriter.println();
        printWriter.println("public class " + className + "Creator {");
        printWriter.println("  private InstructionFactory _factory;");
        printWriter.println("  private ConstantPoolGen    _cp;");
        printWriter.println("  private ClassGen           _cg;");
        printWriter.println();
        printWriter.println("  public " + className + "Creator() {");
        printWriter.println("    _cg = new ClassGen(\"" + Utility.convertString(packageName.isEmpty() ? className : packageName + "." + className) + "\", \""
            + Utility.convertString(superName) + "\", \"" + Utility.convertString(clazz.getSourceFileName()) + "\", "
            + printFlags(clazz.getAccessFlags(), FLAGS.CLASS) + ", " + "new String[] { " + inter + " }"
            + (preserveConstantPool ? ", createConstantPool()" : "") + ");");
        printWriter.println("    _cg.setMajor(" + clazz.getMajor() + ");");
        printWriter.println("    _cg.setMinor(" + clazz.getMinor() + ");");
        printWriter.println();
        printWriter.println("    _cp = _cg.getConstantPool();");
        printWriter.println("    _factory = new InstructionFactory(_cg, _cp);");
        for (final Attribute attribute : clazz.getAttributes()) {
            if (attribute instanceof BootstrapMethods) {
                attribute.accept(this);
            }
        }
        printWriter.println("  }");
        printWriter.println();
        if (preserveConstantPool) {
            printConstantPool();
            for (final Attribute attribute : clazz.getAttributes()) {
                if (attribute instanceof BootstrapMethods) {
                    printBootstrapMethods((BootstrapMethods) attribute);
                }
            }
        }
        printCreate();
        final Field[] fields = clazz.getFields();
        if (fields.length > 0) {
            printWriter.println("  private void createFields() {");
            printWriter.println("    FieldGen field;");
            for (final Field field : fields) {
                field.accept(this);
            }
            printWriter.println("  }");
            printWriter.println();
        }
        final Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            printWriter.println("  private void createMethod_" + i + "() {");
            methods[i].accept(this);
            printWriter.println("  }");
            printWriter.println();
        }
        printMain();
        printWriter.println("}");
    }

    @Override
    public void visitMethod(final Method method) {
        final MethodGen mg = new MethodGen(method, clazz.getClassName(), constantPoolGen);
        printWriter.println("    InstructionList il = new InstructionList();");
        printWriter.println("    MethodGen method = new MethodGen(" + printFlags(method.getAccessFlags(), FLAGS.METHOD) + ", " + printType(mg.getReturnType())
            + ", " + printArgumentTypes(mg.getArgumentTypes()) + ", new String[] { " + Utility.printArray(escape(mg.getArgumentNames()), false, true) + " }, \""
            + Utility.convertString(method.getName()) + "\", \"" + Utility.convertString(clazz.getClassName()) + "\", il, _cp);");
        final ExceptionTable exceptionTable = method.getExceptionTable();
        if (exceptionTable != null) {
            final String[] exceptionNames = exceptionTable.getExceptionNames();
            for (final String exceptionName : exceptionNames) {
                printWriter.print("    method.addException(\"");
                printWriter.print(Utility.convertString(exceptionName));
                printWriter.println("\");");
            }
        }
        final Code code = method.getCode();
        if (code != null) {
            // Preserve this method's compact header setting only for versions that support it; JavaClass.dump selects the class-wide layout.
            if (code.isCompactCodeHeader() && clazz.getMajor() == Const.MAJOR_1_1 && clazz.getMinor() < Const.MINOR_1_1) {
                printWriter.println("    method.setCompactCodeHeader(true);");
            }
            final StackMap stackMap = code.getStackMap();
            if (stackMap != null) {
                stackMap.accept(this);
            }
        }
        printWriter.println();
        final BCELFactory factory = new BCELFactory(mg, printWriter, preserveConstantPool);
        factory.start();
        printWriter.println("    method.setMaxStack();");
        printWriter.println("    method.setMaxLocals();");
        printWriter.println("    _cg.addMethod(method.getMethod());");
        printWriter.println("    il.dispose();");
    }

    @Override
    public void visitStackMap(final StackMap stackMap) {
        super.visitStackMap(stackMap);
        printWriter.print("    method.addCodeAttribute(");
        printWriter.print("new StackMap(_cp.addUtf8(\"");
        printWriter.print(Utility.convertString(stackMap.getName()));
        printWriter.print("\"), ");
        printWriter.print(stackMap.getLength());
        printWriter.print(", ");
        printWriter.print("new StackMapEntry[] {");
        final StackMapEntry[] table = stackMap.getStackMap();
        for (int i = 0; i < table.length; i++) {
            table[i].accept(this);
            if (i < table.length - 1) {
                printWriter.print(", ");
            } else {
                printWriter.print(" }");
            }
        }
        printWriter.print(", _cp.getConstantPool())");
        printWriter.println(");");
    }

    @Override
    public void visitStackMapEntry(final StackMapEntry stackMapEntry) {
        super.visitStackMapEntry(stackMapEntry);
        printWriter.print("new StackMapEntry(");
        printWriter.print(stackMapEntry.getFrameType());
        printWriter.print(", ");
        printWriter.print(stackMapEntry.getByteCodeOffset());
        printWriter.print(", ");
        visitStackMapTypeArray(stackMapEntry.getTypesOfLocals());
        printWriter.print(", ");
        visitStackMapTypeArray(stackMapEntry.getTypesOfStackItems());
        printWriter.print(", _cp.getConstantPool())");
    }

    /**
     * Visits a {@link StackMapType} object.
     *
     * @param stackMapType object to visit.
     * @since 6.7.1
     */
    @Override
    public void visitStackMapType(final StackMapType stackMapType) {
        super.visitStackMapType(stackMapType);
        printWriter.print("new StackMapType((byte)");
        printWriter.print(stackMapType.getType());
        printWriter.print(", ");
        if (stackMapType.hasIndex()) {
            printWriter.print("_cp.addClass(\"");
            printWriter.print(Utility.convertString(stackMapType.getClassName()));
            printWriter.print("\")");
        } else {
            printWriter.print("-1");
        }
        printWriter.print(", _cp.getConstantPool())");
    }

    private void visitStackMapTypeArray(final StackMapType[] types) {
        if (ArrayUtils.isEmpty(types)) {
            printWriter.print("null"); // null translates to StackMapType.EMPTY_ARRAY
        } else {
            printWriter.print("new StackMapType[] {");
            for (int i = 0; i < types.length; i++) {
                types[i].accept(this);
                if (i < types.length - 1) {
                    printWriter.print(", ");
                } else {
                    printWriter.print(" }");
                }
            }
        }
    }
}
