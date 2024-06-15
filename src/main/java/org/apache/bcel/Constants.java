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
package org.apache.bcel;

/**
 * Constants for the project, mostly defined in the JVM specification.
 *
 * @deprecated (since 6.0) DO NOT USE - use {@link Const} instead.
 */
@Deprecated
public interface Constants {

    /**
     * Major version number of class files for Java 1.1.
     *
     * @see #MINOR_1_1
     */
    short MAJOR_1_1 = Const.MAJOR_1_1;

    /**
     * Minor version number of class files for Java 1.1.
     *
     * @see #MAJOR_1_1
     */
    short MINOR_1_1 = Const.MINOR_1_1;

    /**
     * Major version number of class files for Java 1.2.
     *
     * @see #MINOR_1_2
     */
    short MAJOR_1_2 = Const.MAJOR_1_2;

    /**
     * Minor version number of class files for Java 1.2.
     *
     * @see #MAJOR_1_2
     */
    short MINOR_1_2 = Const.MINOR_1_2;

    /**
     * Major version number of class files for Java 1.2.
     *
     * @see #MINOR_1_2
     */
    short MAJOR_1_3 = Const.MAJOR_1_3;

    /**
     * Minor version number of class files for Java 1.3.
     *
     * @see #MAJOR_1_3
     */
    short MINOR_1_3 = Const.MINOR_1_3;

    /**
     * Major version number of class files for Java 1.3.
     *
     * @see #MINOR_1_3
     */
    short MAJOR_1_4 = Const.MAJOR_1_4;

    /**
     * Minor version number of class files for Java 1.4.
     *
     * @see #MAJOR_1_4
     */
    short MINOR_1_4 = Const.MINOR_1_4;

    /**
     * Major version number of class files for Java 1.4.
     *
     * @see #MINOR_1_4
     */
    short MAJOR_1_5 = Const.MAJOR_1_5;

    /**
     * Minor version number of class files for Java 1.5.
     *
     * @see #MAJOR_1_5
     */
    short MINOR_1_5 = Const.MINOR_1_5;

    /**
     * Default major version number. Class file is for Java 1.1.
     *
     * @see #MAJOR_1_1
     */
    short MAJOR = Const.MAJOR;

    /**
     * Default major version number. Class file is for Java 1.1.
     *
     * @see #MAJOR_1_1
     */
    short MINOR = Const.MINOR;

    /**
     * Maximum value for an unsigned short.
     */
    int MAX_SHORT = Const.MAX_SHORT; // 2^16 - 1

    /**
     * Maximum value for an unsigned byte.
     */
    int MAX_BYTE = Const.MAX_BYTE; // 2^8 - 1

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5"> Flag definitions for Fields
     *      in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6"> Flag definitions for Methods
     *      in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6-300-D.1-D.1"> Flag
     *      definitions for Classes in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
     */
    short ACC_PUBLIC = Const.ACC_PUBLIC;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_PRIVATE = Const.ACC_PRIVATE;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_PROTECTED = Const.ACC_PROTECTED;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_STATIC = Const.ACC_STATIC;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_FINAL = Const.ACC_FINAL;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_SYNCHRONIZED = Const.ACC_SYNCHRONIZED;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_VOLATILE = Const.ACC_VOLATILE;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_BRIDGE = Const.ACC_BRIDGE;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_TRANSIENT = Const.ACC_TRANSIENT;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_VARARGS = Const.ACC_VARARGS;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_NATIVE = Const.ACC_NATIVE;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_INTERFACE = Const.ACC_INTERFACE;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_ABSTRACT = Const.ACC_ABSTRACT;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_STRICT = Const.ACC_STRICT;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_SYNTHETIC = Const.ACC_SYNTHETIC;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_ANNOTATION = Const.ACC_ANNOTATION;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_ENUM = Const.ACC_ENUM;

    // Applies to classes compiled by new compilers only
    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short ACC_SUPER = Const.ACC_SUPER;

    /**
     * One of the access flags for fields, methods, or classes.
     *
     * @see #ACC_PUBLIC
     */
    short MAX_ACC_FLAG = Const.MAX_ACC_FLAG;

    /** The names of the access flags. */
    String[] ACCESS_NAMES = {"public", "private", "protected", "static", "final", "synchronized", "volatile", "transient", "native", "interface", "abstract",
        "strictfp", "synthetic", "annotation", "enum"};

    /** Marks a constant pool entry as type UTF-8. */
    byte CONSTANT_Utf8 = Const.CONSTANT_Utf8;

    /** Marks a constant pool entry as type Integer. */
    byte CONSTANT_Integer = Const.CONSTANT_Integer;

    /** Marks a constant pool entry as type Float. */
    byte CONSTANT_Float = Const.CONSTANT_Float;

    /** Marks a constant pool entry as type Long. */
    byte CONSTANT_Long = Const.CONSTANT_Long;

    /** Marks a constant pool entry as type Double. */
    byte CONSTANT_Double = Const.CONSTANT_Double;

    /** Marks a constant pool entry as a Class. */
    byte CONSTANT_Class = Const.CONSTANT_Class;

    /** Marks a constant pool entry as a Field Reference. */
    byte CONSTANT_Fieldref = Const.CONSTANT_Fieldref;

    /** Marks a constant pool entry as type String. */
    byte CONSTANT_String = Const.CONSTANT_String;

    /** Marks a constant pool entry as a Method Reference. */
    byte CONSTANT_Methodref = Const.CONSTANT_Methodref;

    /** Marks a constant pool entry as an Interface Method Reference. */
    byte CONSTANT_InterfaceMethodref = Const.CONSTANT_InterfaceMethodref;

    /** Marks a constant pool entry as a name and type. */
    byte CONSTANT_NameAndType = Const.CONSTANT_NameAndType;

    /** The names of the types of entries in a constant pool. */
    String[] CONSTANT_NAMES = {"", "CONSTANT_Utf8", "", "CONSTANT_Integer", "CONSTANT_Float", "CONSTANT_Long", "CONSTANT_Double", "CONSTANT_Class",
        "CONSTANT_String", "CONSTANT_Fieldref", "CONSTANT_Methodref", "CONSTANT_InterfaceMethodref", "CONSTANT_NameAndType"};

    /**
     * The name of the static initializer, also called &quot;class initialization method&quot; or &quot;interface
     * initialization method&quot;. This is &quot;&lt;clinit&gt;&quot;.
     */
    String STATIC_INITIALIZER_NAME = Const.STATIC_INITIALIZER_NAME;

    /**
     * The name of every constructor method in a class, also called &quot;instance initialization method&quot;. This is
     * &quot;&lt;init&gt;&quot;.
     */
    String CONSTRUCTOR_NAME = Const.CONSTRUCTOR_NAME;

    /** The names of the interfaces implemented by arrays */
    String[] INTERFACES_IMPLEMENTED_BY_ARRAYS = {"java.lang.Cloneable", "java.io.Serializable"};

    /**
     * One of the limitations of the Java Virtual Machine.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.11"> The Java Virtual Machine
     *      Specification, Second Edition, page 152, chapter 4.10.</a>
     */
    int MAX_CP_ENTRIES = Const.MAX_CP_ENTRIES;

    /**
     * One of the limitations of the Java Virtual Machine.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.11"> The Java Virtual Machine
     *      Specification, Second Edition, page 152, chapter 4.10.</a>
     */
    int MAX_CODE_SIZE = Const.MAX_CODE_SIZE; // bytes

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short NOP = Const.NOP;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ACONST_NULL = Const.ACONST_NULL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_M1 = Const.ICONST_M1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_0 = Const.ICONST_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_1 = Const.ICONST_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_2 = Const.ICONST_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_3 = Const.ICONST_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_4 = Const.ICONST_4;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ICONST_5 = Const.ICONST_5;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LCONST_0 = Const.LCONST_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LCONST_1 = Const.LCONST_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FCONST_0 = Const.FCONST_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FCONST_1 = Const.FCONST_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FCONST_2 = Const.FCONST_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DCONST_0 = Const.DCONST_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DCONST_1 = Const.DCONST_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short BIPUSH = Const.BIPUSH;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short SIPUSH = Const.SIPUSH;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LDC = Const.LDC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LDC_W = Const.LDC_W;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LDC2_W = Const.LDC2_W;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ILOAD = Const.ILOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LLOAD = Const.LLOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FLOAD = Const.FLOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DLOAD = Const.DLOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ALOAD = Const.ALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ILOAD_0 = Const.ILOAD_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ILOAD_1 = Const.ILOAD_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ILOAD_2 = Const.ILOAD_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ILOAD_3 = Const.ILOAD_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LLOAD_0 = Const.LLOAD_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LLOAD_1 = Const.LLOAD_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LLOAD_2 = Const.LLOAD_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LLOAD_3 = Const.LLOAD_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FLOAD_0 = Const.FLOAD_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FLOAD_1 = Const.FLOAD_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FLOAD_2 = Const.FLOAD_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FLOAD_3 = Const.FLOAD_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DLOAD_0 = Const.DLOAD_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DLOAD_1 = Const.DLOAD_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DLOAD_2 = Const.DLOAD_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DLOAD_3 = Const.DLOAD_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ALOAD_0 = Const.ALOAD_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ALOAD_1 = Const.ALOAD_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ALOAD_2 = Const.ALOAD_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ALOAD_3 = Const.ALOAD_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IALOAD = Const.IALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LALOAD = Const.LALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FALOAD = Const.FALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DALOAD = Const.DALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short AALOAD = Const.AALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short BALOAD = Const.BALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short CALOAD = Const.CALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short SALOAD = Const.SALOAD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISTORE = Const.ISTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSTORE = Const.LSTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSTORE = Const.FSTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSTORE = Const.DSTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ASTORE = Const.ASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISTORE_0 = Const.ISTORE_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISTORE_1 = Const.ISTORE_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISTORE_2 = Const.ISTORE_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISTORE_3 = Const.ISTORE_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSTORE_0 = Const.LSTORE_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSTORE_1 = Const.LSTORE_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSTORE_2 = Const.LSTORE_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSTORE_3 = Const.LSTORE_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSTORE_0 = Const.FSTORE_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSTORE_1 = Const.FSTORE_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSTORE_2 = Const.FSTORE_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSTORE_3 = Const.FSTORE_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSTORE_0 = Const.DSTORE_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSTORE_1 = Const.DSTORE_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSTORE_2 = Const.DSTORE_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSTORE_3 = Const.DSTORE_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ASTORE_0 = Const.ASTORE_0;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ASTORE_1 = Const.ASTORE_1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ASTORE_2 = Const.ASTORE_2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ASTORE_3 = Const.ASTORE_3;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IASTORE = Const.IASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LASTORE = Const.LASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FASTORE = Const.FASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DASTORE = Const.DASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short AASTORE = Const.AASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short BASTORE = Const.BASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short CASTORE = Const.CASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short SASTORE = Const.SASTORE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short POP = Const.POP;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short POP2 = Const.POP2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP = Const.DUP;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP_X1 = Const.DUP_X1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP_X2 = Const.DUP_X2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP2 = Const.DUP2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP2_X1 = Const.DUP2_X1;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DUP2_X2 = Const.DUP2_X2;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short SWAP = Const.SWAP;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IADD = Const.IADD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LADD = Const.LADD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FADD = Const.FADD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DADD = Const.DADD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISUB = Const.ISUB;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSUB = Const.LSUB;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FSUB = Const.FSUB;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DSUB = Const.DSUB;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IMUL = Const.IMUL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LMUL = Const.LMUL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FMUL = Const.FMUL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DMUL = Const.DMUL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IDIV = Const.IDIV;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LDIV = Const.LDIV;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FDIV = Const.FDIV;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DDIV = Const.DDIV;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IREM = Const.IREM;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LREM = Const.LREM;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FREM = Const.FREM;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DREM = Const.DREM;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INEG = Const.INEG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LNEG = Const.LNEG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FNEG = Const.FNEG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DNEG = Const.DNEG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISHL = Const.ISHL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSHL = Const.LSHL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ISHR = Const.ISHR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LSHR = Const.LSHR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IUSHR = Const.IUSHR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LUSHR = Const.LUSHR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IAND = Const.IAND;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LAND = Const.LAND;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IOR = Const.IOR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LOR = Const.LOR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IXOR = Const.IXOR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LXOR = Const.LXOR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IINC = Const.IINC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2L = Const.I2L;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2F = Const.I2F;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2D = Const.I2D;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short L2I = Const.L2I;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short L2F = Const.L2F;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short L2D = Const.L2D;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short F2I = Const.F2I;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short F2L = Const.F2L;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short F2D = Const.F2D;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short D2I = Const.D2I;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short D2L = Const.D2L;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short D2F = Const.D2F;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2B = Const.I2B;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INT2BYTE = Const.INT2BYTE; // Old notion

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2C = Const.I2C;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INT2CHAR = Const.INT2CHAR; // Old notion

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short I2S = Const.I2S;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INT2SHORT = Const.INT2SHORT; // Old notion

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LCMP = Const.LCMP;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FCMPL = Const.FCMPL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FCMPG = Const.FCMPG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DCMPL = Const.DCMPL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DCMPG = Const.DCMPG;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFEQ = Const.IFEQ;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFNE = Const.IFNE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFLT = Const.IFLT;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFGE = Const.IFGE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFGT = Const.IFGT;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFLE = Const.IFLE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPEQ = Const.IF_ICMPEQ;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPNE = Const.IF_ICMPNE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPLT = Const.IF_ICMPLT;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPGE = Const.IF_ICMPGE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPGT = Const.IF_ICMPGT;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ICMPLE = Const.IF_ICMPLE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ACMPEQ = Const.IF_ACMPEQ;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IF_ACMPNE = Const.IF_ACMPNE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short GOTO = Const.GOTO;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short JSR = Const.JSR;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short RET = Const.RET;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short TABLESWITCH = Const.TABLESWITCH;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LOOKUPSWITCH = Const.LOOKUPSWITCH;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IRETURN = Const.IRETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short LRETURN = Const.LRETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short FRETURN = Const.FRETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short DRETURN = Const.DRETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ARETURN = Const.ARETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short RETURN = Const.RETURN;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short GETSTATIC = Const.GETSTATIC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short PUTSTATIC = Const.PUTSTATIC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short GETFIELD = Const.GETFIELD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short PUTFIELD = Const.PUTFIELD;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKEVIRTUAL = Const.INVOKEVIRTUAL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKESPECIAL = Const.INVOKESPECIAL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKENONVIRTUAL = Const.INVOKENONVIRTUAL; // Old name in JDK 1.0

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKESTATIC = Const.INVOKESTATIC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKEINTERFACE = Const.INVOKEINTERFACE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INVOKEDYNAMIC = Const.INVOKEDYNAMIC;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short NEW = Const.NEW;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short NEWARRAY = Const.NEWARRAY;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ANEWARRAY = Const.ANEWARRAY;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ARRAYLENGTH = Const.ARRAYLENGTH;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short ATHROW = Const.ATHROW;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short CHECKCAST = Const.CHECKCAST;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short INSTANCEOF = Const.INSTANCEOF;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short MONITORENTER = Const.MONITORENTER;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short MONITOREXIT = Const.MONITOREXIT;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short WIDE = Const.WIDE;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short MULTIANEWARRAY = Const.MULTIANEWARRAY;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFNULL = Const.IFNULL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short IFNONNULL = Const.IFNONNULL;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short GOTO_W = Const.GOTO_W;

    /**
     * Java VM opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5"> Opcode definitions in The
     *      Java Virtual Machine Specification</a>
     */
    short JSR_W = Const.JSR_W;

    /**
     * JVM internal opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2"> Reserved opcodes in the Java
     *      Virtual Machine Specification</a>
     */
    short BREAKPOINT = Const.BREAKPOINT;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short LDC_QUICK = Const.LDC_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short LDC_W_QUICK = Const.LDC_W_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short LDC2_W_QUICK = Const.LDC2_W_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short GETFIELD_QUICK = Const.GETFIELD_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short PUTFIELD_QUICK = Const.PUTFIELD_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short GETFIELD2_QUICK = Const.GETFIELD2_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short PUTFIELD2_QUICK = Const.PUTFIELD2_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short GETSTATIC_QUICK = Const.GETSTATIC_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short PUTSTATIC_QUICK = Const.PUTSTATIC_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short GETSTATIC2_QUICK = Const.GETSTATIC2_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short PUTSTATIC2_QUICK = Const.PUTSTATIC2_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKEVIRTUAL_QUICK = Const.INVOKEVIRTUAL_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKENONVIRTUAL_QUICK = Const.INVOKENONVIRTUAL_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKESUPER_QUICK = Const.INVOKESUPER_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKESTATIC_QUICK = Const.INVOKESTATIC_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKEINTERFACE_QUICK = Const.INVOKEINTERFACE_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKEVIRTUALOBJECT_QUICK = Const.INVOKEVIRTUALOBJECT_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short NEW_QUICK = Const.NEW_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short ANEWARRAY_QUICK = Const.ANEWARRAY_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short MULTIANEWARRAY_QUICK = Const.MULTIANEWARRAY_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short CHECKCAST_QUICK = Const.CHECKCAST_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INSTANCEOF_QUICK = Const.INSTANCEOF_QUICK;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short INVOKEVIRTUAL_QUICK_W = Const.INVOKEVIRTUAL_QUICK_W;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short GETFIELD_QUICK_W = Const.GETFIELD_QUICK_W;

    /**
     * JVM internal opcode.
     *
     * @see <a href=
     *      "https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
     *      Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885"> Why the _quick
     *      opcodes were removed from the second version of the Java Virtual Machine Specification.</a>
     */
    short PUTFIELD_QUICK_W = Const.PUTFIELD_QUICK_W;

    /**
     * JVM internal opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2"> Reserved opcodes in the Java
     *      Virtual Machine Specification</a>
     */
    short IMPDEP1 = Const.IMPDEP1;

    /**
     * JVM internal opcode.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2"> Reserved opcodes in the Java
     *      Virtual Machine Specification</a>
     */
    short IMPDEP2 = Const.IMPDEP2;

    /**
     * BCEL virtual instruction for pushing an arbitrary data type onto the stack. Will be converted to the appropriate JVM
     * opcode when the class is dumped.
     */
    short PUSH = Const.PUSH;

    /**
     * BCEL virtual instruction for either LOOKUPSWITCH or TABLESWITCH. Will be converted to the appropriate JVM opcode when
     * the class is dumped.
     */
    short SWITCH = Const.SWITCH;

    /** Illegal opcode. */
    short UNDEFINED = Const.UNDEFINED;

    /** Illegal opcode. */
    short UNPREDICTABLE = Const.UNPREDICTABLE;

    /** Illegal opcode. */
    short RESERVED = Const.RESERVED;

    /** Mnemonic for an illegal opcode. */
    String ILLEGAL_OPCODE = Const.ILLEGAL_OPCODE;

    /** Mnemonic for an illegal type. */
    String ILLEGAL_TYPE = Const.ILLEGAL_TYPE;

    /** Boolean data type. */
    byte T_BOOLEAN = Const.T_BOOLEAN;

    /** Char data type. */
    byte T_CHAR = Const.T_CHAR;

    /** Float data type. */
    byte T_FLOAT = Const.T_FLOAT;

    /** Double data type. */
    byte T_DOUBLE = Const.T_DOUBLE;

    /** Byte data type. */
    byte T_BYTE = Const.T_BYTE;

    /** Short data type. */
    byte T_SHORT = Const.T_SHORT;

    /** Int data type. */
    byte T_INT = Const.T_INT;

    /** Long data type. */
    byte T_LONG = Const.T_LONG;

    /** Void data type (non-standard). */
    byte T_VOID = Const.T_VOID; // Non-standard

    /** Array data type. */
    byte T_ARRAY = Const.T_ARRAY;

    /** Object data type. */
    byte T_OBJECT = Const.T_OBJECT;

    /** Reference data type (deprecated). */
    byte T_REFERENCE = Const.T_REFERENCE; // Deprecated

    /** Unknown data type. */
    byte T_UNKNOWN = Const.T_UNKNOWN;

    /** Address data type. */
    byte T_ADDRESS = Const.T_ADDRESS;

    /**
     * The primitive type names corresponding to the T_XX constants, e.g., TYPE_NAMES[T_INT] = "int"
     */
    String[] TYPE_NAMES = {ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, "boolean", "char", "float", "double", "byte", "short", "int", "long", "void",
        "array", "object", "unknown", "address"};

    /**
     * The primitive class names corresponding to the T_XX constants, e.g., CLASS_TYPE_NAMES[T_INT] = "java.lang.Integer"
     */
    String[] CLASS_TYPE_NAMES = {ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, "java.lang.Boolean", "java.lang.Character", "java.lang.Float",
        "java.lang.Double", "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Void", ILLEGAL_TYPE, ILLEGAL_TYPE,
        ILLEGAL_TYPE, ILLEGAL_TYPE};

    /**
     * The signature characters corresponding to primitive types, e.g., SHORT_TYPE_NAMES[T_INT] = "I"
     */
    String[] SHORT_TYPE_NAMES = {ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE, "Z", "C", "F", "D", "B", "S", "I", "J", "V", ILLEGAL_TYPE,
        ILLEGAL_TYPE, ILLEGAL_TYPE};

    /**
     * Number of byte code operands for each opcode, i.e., number of bytes after the tag byte itself. Indexed by opcode, so
     * NO_OF_OPERANDS[BIPUSH] = the number of operands for a bipush instruction.
     */
    short[] NO_OF_OPERANDS = Const.NO_OF_OPERANDS;

    /**
     * How the byte code operands are to be interpreted for each opcode. Indexed by opcode. TYPE_OF_OPERANDS[ILOAD] = an
     * array of shorts describing the data types for the instruction.
     */
    short[][] TYPE_OF_OPERANDS = Const.TYPE_OF_OPERANDS;

    /**
     * Names of opcodes. Indexed by opcode. OPCODE_NAMES[ALOAD] = "aload".
     */
    String[] OPCODE_NAMES = Const.OPCODE_NAMES;

    /**
     * Number of words consumed on operand stack by instructions. Indexed by opcode. CONSUME_STACK[FALOAD] = number of words
     * consumed from the stack by a faload instruction.
     */
    int[] CONSUME_STACK = Const.CONSUME_STACK;

    /**
     * Number of words produced onto operand stack by instructions. Indexed by opcode. CONSUME_STACK[DALOAD] = number of
     * words consumed from the stack by a daload instruction.
     */
    int[] PRODUCE_STACK = Const.PRODUCE_STACK;

    /**
     * Attributes and their corresponding names.
     */
    byte ATTR_UNKNOWN = Const.ATTR_UNKNOWN;
    byte ATTR_SOURCE_FILE = Const.ATTR_SOURCE_FILE;
    byte ATTR_CONSTANT_VALUE = Const.ATTR_CONSTANT_VALUE;
    byte ATTR_CODE = Const.ATTR_CODE;
    byte ATTR_EXCEPTIONS = Const.ATTR_EXCEPTIONS;
    byte ATTR_LINE_NUMBER_TABLE = Const.ATTR_LINE_NUMBER_TABLE;
    byte ATTR_LOCAL_VARIABLE_TABLE = Const.ATTR_LOCAL_VARIABLE_TABLE;
    byte ATTR_INNER_CLASSES = Const.ATTR_INNER_CLASSES;
    byte ATTR_SYNTHETIC = Const.ATTR_SYNTHETIC;
    byte ATTR_DEPRECATED = Const.ATTR_DEPRECATED;
    byte ATTR_PMG = Const.ATTR_PMG;
    byte ATTR_SIGNATURE = Const.ATTR_SIGNATURE;
    byte ATTR_STACK_MAP = Const.ATTR_STACK_MAP;
    byte ATTR_RUNTIMEVISIBLE_ANNOTATIONS = 12;
    byte ATTR_RUNTIMEINVISIBLE_ANNOTATIONS = 13;
    byte ATTR_RUNTIMEVISIBLE_PARAMETER_ANNOTATIONS = 14;
    byte ATTR_RUNTIMEINVISIBLE_PARAMETER_ANNOTATIONS = 15;
    byte ATTR_ANNOTATION_DEFAULT = 16;

    short KNOWN_ATTRIBUTES = 12; // should be 17

    // TODO: mutable public array!!
    String[] ATTRIBUTE_NAMES = {"SourceFile", "ConstantValue", "Code", "Exceptions", "LineNumberTable", "LocalVariableTable", "InnerClasses", "Synthetic",
        "Deprecated", "PMGClass", "Signature", "StackMap", "RuntimeVisibleAnnotations", "RuntimeInvisibleAnnotations", "RuntimeVisibleParameterAnnotations",
        "RuntimeInvisibleParameterAnnotations", "AnnotationDefault"};

    /**
     * Constants used in the StackMap attribute.
     */
    byte ITEM_Bogus = Const.ITEM_Bogus;
    byte ITEM_Integer = Const.ITEM_Integer;
    byte ITEM_Float = Const.ITEM_Float;
    byte ITEM_Double = Const.ITEM_Double;
    byte ITEM_Long = Const.ITEM_Long;
    byte ITEM_Null = Const.ITEM_Null;
    byte ITEM_InitObject = Const.ITEM_InitObject;
    byte ITEM_Object = Const.ITEM_Object;
    byte ITEM_NewObject = Const.ITEM_NewObject;

    String[] ITEM_NAMES = {"Bogus", "Integer", "Float", "Double", "Long", "Null", "InitObject", "Object", "NewObject"};

}
