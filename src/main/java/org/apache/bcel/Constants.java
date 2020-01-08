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
package org.apache.bcel;

/**
 * Constants for the project, mostly defined in the JVM specification.
 *
 * @deprecated (since 6.0) DO NOT USE - use Const instead
 */
@Deprecated
public interface Constants {

  /** Major version number of class files for Java 1.1.
   *  @see #MINOR_1_1
   *  */
  short MAJOR_1_1 = 45;

  /** Minor version number of class files for Java 1.1.
   *  @see #MAJOR_1_1
   *  */
  short MINOR_1_1 = 3;

  /** Major version number of class files for Java 1.2.
   *  @see #MINOR_1_2
   *  */
  short MAJOR_1_2 = 46;

  /** Minor version number of class files for Java 1.2.
   *  @see #MAJOR_1_2
   *  */
  short MINOR_1_2 = 0;

  /** Major version number of class files for Java 1.2.
   *  @see #MINOR_1_2
   *  */
  short MAJOR_1_3 = 47;

  /** Minor version number of class files for Java 1.3.
   *  @see #MAJOR_1_3
   *  */
  short MINOR_1_3 = 0;

  /** Major version number of class files for Java 1.3.
   *  @see #MINOR_1_3
   *  */
  short MAJOR_1_4 = 48;

  /** Minor version number of class files for Java 1.4.
   *  @see #MAJOR_1_4
   *  */
  short MINOR_1_4 = 0;

  /** Major version number of class files for Java 1.4.
   *  @see #MINOR_1_4
   *  */
  short MAJOR_1_5 = 49;

  /** Minor version number of class files for Java 1.5.
   *  @see #MAJOR_1_5
   *  */
  short MINOR_1_5 = 0;


  /** Default major version number.  Class file is for Java 1.1.
   *  @see #MAJOR_1_1
   *  */
  short MAJOR = MAJOR_1_1;

  /** Default major version number.  Class file is for Java 1.1.
   *  @see #MAJOR_1_1
   *  */
  short MINOR     = MINOR_1_1;

  /** Maximum value for an unsigned short.
   */
  int MAX_SHORT = 65535; // 2^16 - 1

  /** Maximum value for an unsigned byte.
   */
  int MAX_BYTE  = 255; // 2^8 - 1

  /** One of the access flags for fields, methods, or classes.
   *
   *  @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5">
   *  Flag definitions for Fields in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
   *  @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6">
   *  Flag definitions for Methods in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
   *  @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6-300-D.1-D.1">
   *  Flag definitions for Classes in the Java Virtual Machine Specification (Java SE 8 Edition).</a>
   */
  short ACC_PUBLIC       = 0x0001;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_PRIVATE      = 0x0002;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_PROTECTED    = 0x0004;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_STATIC       = 0x0008;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_FINAL        = 0x0010;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_SYNCHRONIZED = 0x0020;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_VOLATILE     = 0x0040;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_BRIDGE       = 0x0040;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_TRANSIENT    = 0x0080;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_VARARGS      = 0x0080;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_NATIVE       = 0x0100;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_INTERFACE    = 0x0200;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_ABSTRACT     = 0x0400;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_STRICT       = 0x0800;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_SYNTHETIC    = 0x1000;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_ANNOTATION   = 0x2000;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_ENUM         = 0x4000;

  // Applies to classes compiled by new compilers only
  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short ACC_SUPER        = 0x0020;

  /** One of the access flags for fields, methods, or classes.
   *  @see #ACC_PUBLIC
   */
  short MAX_ACC_FLAG     = ACC_ENUM;

  /** The names of the access flags. */
  String[] ACCESS_NAMES = {
    "public", "private", "protected", "static", "final", "synchronized",
    "volatile", "transient", "native", "interface", "abstract", "strictfp",
    "synthetic", "annotation", "enum"
  };

  /** Marks a constant pool entry as type UTF-8.  */
  byte CONSTANT_Utf8               = 1;

  /** Marks a constant pool entry as type Integer.  */
  byte CONSTANT_Integer            = 3;

  /** Marks a constant pool entry as type Float.  */
  byte CONSTANT_Float              = 4;

  /** Marks a constant pool entry as type Long.  */
  byte CONSTANT_Long               = 5;

  /** Marks a constant pool entry as type Double.  */
  byte CONSTANT_Double             = 6;

  /** Marks a constant pool entry as a Class.  */
  byte CONSTANT_Class              = 7;

  /** Marks a constant pool entry as a Field Reference.  */
  byte CONSTANT_Fieldref           = 9;

  /** Marks a constant pool entry as type String.  */
  byte CONSTANT_String             = 8;

  /** Marks a constant pool entry as a Method Reference.  */
  byte CONSTANT_Methodref          = 10;

  /** Marks a constant pool entry as an Interface Method Reference.  */
  byte CONSTANT_InterfaceMethodref = 11;

  /** Marks a constant pool entry as a name and type.  */
  byte CONSTANT_NameAndType        = 12;

  /** The names of the types of entries in a constant pool. */
  String[] CONSTANT_NAMES = {
    "", "CONSTANT_Utf8", "", "CONSTANT_Integer",
    "CONSTANT_Float", "CONSTANT_Long", "CONSTANT_Double",
    "CONSTANT_Class", "CONSTANT_String", "CONSTANT_Fieldref",
    "CONSTANT_Methodref", "CONSTANT_InterfaceMethodref",
    "CONSTANT_NameAndType" };

  /** The name of the static initializer, also called &quot;class
   *  initialization method&quot; or &quot;interface initialization
   *   method&quot;. This is &quot;&lt;clinit&gt;&quot;.
   */
  String STATIC_INITIALIZER_NAME = "<clinit>";

  /** The name of every constructor method in a class, also called
   * &quot;instance initialization method&quot;. This is &quot;&lt;init&gt;&quot;.
   */
  String CONSTRUCTOR_NAME = "<init>";

  /** The names of the interfaces implemented by arrays */
  String[] INTERFACES_IMPLEMENTED_BY_ARRAYS = {"java.lang.Cloneable", "java.io.Serializable"};

  /**
   * One of the limitations of the Java Virtual Machine.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.11">
   * The Java Virtual Machine Specification, Second Edition, page 152, chapter 4.10.</a>
   */
  int MAX_CP_ENTRIES     = 65535;

  /**
   * One of the limitations of the Java Virtual Machine.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.11">
   * The Java Virtual Machine Specification, Second Edition, page 152, chapter 4.10.</a>
   */
  int MAX_CODE_SIZE      = 65536; //bytes

  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short NOP              = 0;

  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ACONST_NULL      = 1;

  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_M1        = 2;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_0         = 3;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_1         = 4;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_2         = 5;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_3         = 6;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_4         = 7;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ICONST_5         = 8;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LCONST_0         = 9;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LCONST_1         = 10;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FCONST_0         = 11;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FCONST_1         = 12;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FCONST_2         = 13;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DCONST_0         = 14;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DCONST_1         = 15;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short BIPUSH           = 16;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short SIPUSH           = 17;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LDC              = 18;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LDC_W            = 19;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LDC2_W           = 20;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ILOAD            = 21;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LLOAD            = 22;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FLOAD            = 23;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DLOAD            = 24;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ALOAD            = 25;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ILOAD_0          = 26;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ILOAD_1          = 27;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ILOAD_2          = 28;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ILOAD_3          = 29;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LLOAD_0          = 30;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LLOAD_1          = 31;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LLOAD_2          = 32;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LLOAD_3          = 33;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FLOAD_0          = 34;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FLOAD_1          = 35;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FLOAD_2          = 36;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FLOAD_3          = 37;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DLOAD_0          = 38;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DLOAD_1          = 39;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DLOAD_2          = 40;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DLOAD_3          = 41;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ALOAD_0          = 42;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ALOAD_1          = 43;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ALOAD_2          = 44;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ALOAD_3          = 45;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IALOAD           = 46;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LALOAD           = 47;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FALOAD           = 48;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DALOAD           = 49;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short AALOAD           = 50;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short BALOAD           = 51;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short CALOAD           = 52;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short SALOAD           = 53;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISTORE           = 54;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSTORE           = 55;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSTORE           = 56;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSTORE           = 57;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ASTORE           = 58;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISTORE_0         = 59;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISTORE_1         = 60;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISTORE_2         = 61;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISTORE_3         = 62;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSTORE_0         = 63;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSTORE_1         = 64;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSTORE_2         = 65;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSTORE_3         = 66;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSTORE_0         = 67;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSTORE_1         = 68;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSTORE_2         = 69;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSTORE_3         = 70;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSTORE_0         = 71;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSTORE_1         = 72;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSTORE_2         = 73;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSTORE_3         = 74;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ASTORE_0         = 75;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ASTORE_1         = 76;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ASTORE_2         = 77;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ASTORE_3         = 78;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IASTORE          = 79;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LASTORE          = 80;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FASTORE          = 81;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DASTORE          = 82;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short AASTORE          = 83;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short BASTORE          = 84;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short CASTORE          = 85;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short SASTORE          = 86;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short POP              = 87;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short POP2             = 88;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP              = 89;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP_X1           = 90;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP_X2           = 91;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP2             = 92;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP2_X1          = 93;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DUP2_X2          = 94;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short SWAP             = 95;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IADD             = 96;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LADD             = 97;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FADD             = 98;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DADD             = 99;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISUB             = 100;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSUB             = 101;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FSUB             = 102;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DSUB             = 103;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IMUL             = 104;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LMUL             = 105;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FMUL             = 106;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DMUL             = 107;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IDIV             = 108;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LDIV             = 109;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FDIV             = 110;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DDIV             = 111;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IREM             = 112;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LREM             = 113;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FREM             = 114;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DREM             = 115;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INEG             = 116;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LNEG             = 117;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FNEG             = 118;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DNEG             = 119;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISHL             = 120;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSHL             = 121;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ISHR             = 122;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LSHR             = 123;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IUSHR            = 124;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LUSHR            = 125;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IAND             = 126;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LAND             = 127;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IOR              = 128;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LOR              = 129;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IXOR             = 130;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LXOR             = 131;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IINC             = 132;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2L              = 133;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2F              = 134;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2D              = 135;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short L2I              = 136;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short L2F              = 137;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short L2D              = 138;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short F2I              = 139;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short F2L              = 140;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short F2D              = 141;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short D2I              = 142;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short D2L              = 143;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short D2F              = 144;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2B              = 145;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INT2BYTE         = 145; // Old notion
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2C              = 146;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INT2CHAR         = 146; // Old notion
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short I2S              = 147;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INT2SHORT        = 147; // Old notion
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LCMP             = 148;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FCMPL            = 149;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FCMPG            = 150;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DCMPL            = 151;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DCMPG            = 152;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFEQ             = 153;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFNE             = 154;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFLT             = 155;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFGE             = 156;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFGT             = 157;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFLE             = 158;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPEQ        = 159;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPNE        = 160;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPLT        = 161;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPGE        = 162;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPGT        = 163;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ICMPLE        = 164;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ACMPEQ        = 165;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IF_ACMPNE        = 166;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short GOTO             = 167;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short JSR              = 168;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short RET              = 169;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short TABLESWITCH      = 170;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LOOKUPSWITCH     = 171;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IRETURN          = 172;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short LRETURN          = 173;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short FRETURN          = 174;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short DRETURN          = 175;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ARETURN          = 176;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short RETURN           = 177;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short GETSTATIC        = 178;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short PUTSTATIC        = 179;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short GETFIELD         = 180;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short PUTFIELD         = 181;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKEVIRTUAL    = 182;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKESPECIAL    = 183;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKENONVIRTUAL = 183; // Old name in JDK 1.0
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKESTATIC     = 184;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKEINTERFACE  = 185;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INVOKEDYNAMIC   = 186;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short NEW              = 187;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short NEWARRAY         = 188;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ANEWARRAY        = 189;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ARRAYLENGTH      = 190;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short ATHROW           = 191;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short CHECKCAST        = 192;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short INSTANCEOF       = 193;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short MONITORENTER     = 194;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short MONITOREXIT      = 195;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short WIDE             = 196;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short MULTIANEWARRAY   = 197;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFNULL           = 198;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short IFNONNULL        = 199;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short GOTO_W           = 200;
  /** Java VM opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
   * Opcode definitions in The Java Virtual Machine Specification</a> */
  short JSR_W            = 201;

  /** JVM internal opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2">
   * Reserved opcodes in the Java Virtual Machine Specification</a> */
  short BREAKPOINT                = 202;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short LDC_QUICK                 = 203;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short LDC_W_QUICK               = 204;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short LDC2_W_QUICK              = 205;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short GETFIELD_QUICK            = 206;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short PUTFIELD_QUICK            = 207;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short GETFIELD2_QUICK           = 208;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short PUTFIELD2_QUICK           = 209;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short GETSTATIC_QUICK           = 210;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short PUTSTATIC_QUICK           = 211;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short GETSTATIC2_QUICK          = 212;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short PUTSTATIC2_QUICK          = 213;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKEVIRTUAL_QUICK       = 214;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKENONVIRTUAL_QUICK    = 215;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKESUPER_QUICK         = 216;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKESTATIC_QUICK        = 217;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKEINTERFACE_QUICK     = 218;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKEVIRTUALOBJECT_QUICK = 219;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short NEW_QUICK                 = 221;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short ANEWARRAY_QUICK           = 222;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short MULTIANEWARRAY_QUICK      = 223;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short CHECKCAST_QUICK           = 224;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INSTANCEOF_QUICK          = 225;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short INVOKEVIRTUAL_QUICK_W     = 226;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short GETFIELD_QUICK_W          = 227;
  /** JVM internal opcode.
   * @see <a href="https://web.archive.org/web/20120108031230/http://java.sun.com/docs/books/jvms/first_edition/html/Quick.doc.html">
   * Specification of _quick opcodes in the Java Virtual Machine Specification (version 1)</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se5.0/html/ChangesAppendix.doc.html#448885">
   * Why the _quick opcodes were removed from the second version of the Java Virtual Machine Specification.</a> */
  short PUTFIELD_QUICK_W          = 228;
  /** JVM internal opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2">
   * Reserved opcodes in the Java Virtual Machine Specification</a> */
  short IMPDEP1                   = 254;
  /** JVM internal opcode.
   * @see <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.2">
   * Reserved opcodes in the Java Virtual Machine Specification</a> */
  short IMPDEP2                   = 255;

  /**
   * BCEL virtual instruction for pushing an arbitrary data type onto the stack.  Will be converted to the appropriate JVM
   * opcode when the class is dumped.
   */
  short PUSH             = 4711;
  /**
   * BCEL virtual instruction for either LOOKUPSWITCH or TABLESWITCH.  Will be converted to the appropriate JVM
   * opcode when the class is dumped.
   */
  short SWITCH           = 4712;

  /** Illegal opcode. */
  short  UNDEFINED      = -1;
  /** Illegal opcode. */
  short  UNPREDICTABLE  = -2;
  /** Illegal opcode. */
  short  RESERVED       = -3;
  /** Mnemonic for an illegal opcode. */
  String ILLEGAL_OPCODE = "<illegal opcode>";
  /** Mnemonic for an illegal type. */
  String ILLEGAL_TYPE   = "<illegal type>";

  /** Boolean data type. */
  byte T_BOOLEAN = 4;
  /** Char data type. */
  byte T_CHAR    = 5;
  /** Float data type. */
  byte T_FLOAT   = 6;
  /** Double data type. */
  byte T_DOUBLE  = 7;
  /** Byte data type. */
  byte T_BYTE    = 8;
  /** Short data type. */
  byte T_SHORT   = 9;
  /** Int data type. */
  byte T_INT     = 10;
  /** Long data type. */
  byte T_LONG    = 11;

  /** Void data type (non-standard). */
  byte T_VOID      = 12; // Non-standard
  /** Array data type. */
  byte T_ARRAY     = 13;
  /** Object data type. */
  byte T_OBJECT    = 14;
  /** Reference data type (deprecated). */
  byte T_REFERENCE = 14; // Deprecated
  /** Unknown data type. */
  byte T_UNKNOWN   = 15;
  /** Address data type. */
  byte T_ADDRESS   = 16;

  /** The primitive type names corresponding to the T_XX constants,
   * e.g., TYPE_NAMES[T_INT] = "int"
   */
  String[] TYPE_NAMES = {
    ILLEGAL_TYPE, ILLEGAL_TYPE,  ILLEGAL_TYPE, ILLEGAL_TYPE,
    "boolean", "char", "float", "double", "byte", "short", "int", "long",
    "void", "array", "object", "unknown", "address"
  };

  /** The primitive class names corresponding to the T_XX constants,
   * e.g., CLASS_TYPE_NAMES[T_INT] = "java.lang.Integer"
   */
  String[] CLASS_TYPE_NAMES = {
    ILLEGAL_TYPE, ILLEGAL_TYPE,  ILLEGAL_TYPE, ILLEGAL_TYPE,
    "java.lang.Boolean", "java.lang.Character", "java.lang.Float",
    "java.lang.Double", "java.lang.Byte", "java.lang.Short",
    "java.lang.Integer", "java.lang.Long", "java.lang.Void",
    ILLEGAL_TYPE, ILLEGAL_TYPE,  ILLEGAL_TYPE,  ILLEGAL_TYPE
  };

  /** The signature characters corresponding to primitive types,
   * e.g., SHORT_TYPE_NAMES[T_INT] = "I"
   */
  String[] SHORT_TYPE_NAMES = {
    ILLEGAL_TYPE, ILLEGAL_TYPE,  ILLEGAL_TYPE, ILLEGAL_TYPE,
    "Z", "C", "F", "D", "B", "S", "I", "J",
    "V", ILLEGAL_TYPE, ILLEGAL_TYPE, ILLEGAL_TYPE
  };

  /**
   * Number of byte code operands for each opcode, i.e., number of bytes after the tag byte
   * itself.  Indexed by opcode, so NO_OF_OPERANDS[BIPUSH] = the number of operands for a bipush
   * instruction.
   */
  short[] NO_OF_OPERANDS = {
    0/*nop*/, 0/*aconst_null*/, 0/*iconst_m1*/, 0/*iconst_0*/,
    0/*iconst_1*/, 0/*iconst_2*/, 0/*iconst_3*/, 0/*iconst_4*/,
    0/*iconst_5*/, 0/*lconst_0*/, 0/*lconst_1*/, 0/*fconst_0*/,
    0/*fconst_1*/, 0/*fconst_2*/, 0/*dconst_0*/, 0/*dconst_1*/,
    1/*bipush*/, 2/*sipush*/, 1/*ldc*/, 2/*ldc_w*/, 2/*ldc2_w*/,
    1/*iload*/, 1/*lload*/, 1/*fload*/, 1/*dload*/, 1/*aload*/,
    0/*iload_0*/, 0/*iload_1*/, 0/*iload_2*/, 0/*iload_3*/,
    0/*lload_0*/, 0/*lload_1*/, 0/*lload_2*/, 0/*lload_3*/,
    0/*fload_0*/, 0/*fload_1*/, 0/*fload_2*/, 0/*fload_3*/,
    0/*dload_0*/, 0/*dload_1*/, 0/*dload_2*/, 0/*dload_3*/,
    0/*aload_0*/, 0/*aload_1*/, 0/*aload_2*/, 0/*aload_3*/,
    0/*iaload*/, 0/*laload*/, 0/*faload*/, 0/*daload*/,
    0/*aaload*/, 0/*baload*/, 0/*caload*/, 0/*saload*/,
    1/*istore*/, 1/*lstore*/, 1/*fstore*/, 1/*dstore*/,
    1/*astore*/, 0/*istore_0*/, 0/*istore_1*/, 0/*istore_2*/,
    0/*istore_3*/, 0/*lstore_0*/, 0/*lstore_1*/, 0/*lstore_2*/,
    0/*lstore_3*/, 0/*fstore_0*/, 0/*fstore_1*/, 0/*fstore_2*/,
    0/*fstore_3*/, 0/*dstore_0*/, 0/*dstore_1*/, 0/*dstore_2*/,
    0/*dstore_3*/, 0/*astore_0*/, 0/*astore_1*/, 0/*astore_2*/,
    0/*astore_3*/, 0/*iastore*/, 0/*lastore*/, 0/*fastore*/,
    0/*dastore*/, 0/*aastore*/, 0/*bastore*/, 0/*castore*/,
    0/*sastore*/, 0/*pop*/, 0/*pop2*/, 0/*dup*/, 0/*dup_x1*/,
    0/*dup_x2*/, 0/*dup2*/, 0/*dup2_x1*/, 0/*dup2_x2*/, 0/*swap*/,
    0/*iadd*/, 0/*ladd*/, 0/*fadd*/, 0/*dadd*/, 0/*isub*/,
    0/*lsub*/, 0/*fsub*/, 0/*dsub*/, 0/*imul*/, 0/*lmul*/,
    0/*fmul*/, 0/*dmul*/, 0/*idiv*/, 0/*ldiv*/, 0/*fdiv*/,
    0/*ddiv*/, 0/*irem*/, 0/*lrem*/, 0/*frem*/, 0/*drem*/,
    0/*ineg*/, 0/*lneg*/, 0/*fneg*/, 0/*dneg*/, 0/*ishl*/,
    0/*lshl*/, 0/*ishr*/, 0/*lshr*/, 0/*iushr*/, 0/*lushr*/,
    0/*iand*/, 0/*land*/, 0/*ior*/, 0/*lor*/, 0/*ixor*/, 0/*lxor*/,
    2/*iinc*/, 0/*i2l*/, 0/*i2f*/, 0/*i2d*/, 0/*l2i*/, 0/*l2f*/,
    0/*l2d*/, 0/*f2i*/, 0/*f2l*/, 0/*f2d*/, 0/*d2i*/, 0/*d2l*/,
    0/*d2f*/, 0/*i2b*/, 0/*i2c*/, 0/*i2s*/, 0/*lcmp*/, 0/*fcmpl*/,
    0/*fcmpg*/, 0/*dcmpl*/, 0/*dcmpg*/, 2/*ifeq*/, 2/*ifne*/,
    2/*iflt*/, 2/*ifge*/, 2/*ifgt*/, 2/*ifle*/, 2/*if_icmpeq*/,
    2/*if_icmpne*/, 2/*if_icmplt*/, 2/*if_icmpge*/, 2/*if_icmpgt*/,
    2/*if_icmple*/, 2/*if_acmpeq*/, 2/*if_acmpne*/, 2/*goto*/,
    2/*jsr*/, 1/*ret*/, UNPREDICTABLE/*tableswitch*/, UNPREDICTABLE/*lookupswitch*/,
    0/*ireturn*/, 0/*lreturn*/, 0/*freturn*/,
    0/*dreturn*/, 0/*areturn*/, 0/*return*/,
    2/*getstatic*/, 2/*putstatic*/, 2/*getfield*/,
    2/*putfield*/, 2/*invokevirtual*/, 2/*invokespecial*/, 2/*invokestatic*/,
    4/*invokeinterface*/, 4/*invokedynamic*/, 2/*new*/,
    1/*newarray*/, 2/*anewarray*/,
    0/*arraylength*/, 0/*athrow*/, 2/*checkcast*/,
    2/*instanceof*/, 0/*monitorenter*/,
    0/*monitorexit*/, UNPREDICTABLE/*wide*/, 3/*multianewarray*/,
    2/*ifnull*/, 2/*ifnonnull*/, 4/*goto_w*/,
    4/*jsr_w*/, 0/*breakpoint*/, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, RESERVED/*impdep1*/, RESERVED/*impdep2*/
  };

  /**
   * How the byte code operands are to be interpreted for each opcode.
   * Indexed by opcode.  TYPE_OF_OPERANDS[ILOAD] = an array of shorts
   * describing the data types for the instruction.
   */
  short[][] TYPE_OF_OPERANDS = {
    {}/*nop*/, {}/*aconst_null*/, {}/*iconst_m1*/, {}/*iconst_0*/,
    {}/*iconst_1*/, {}/*iconst_2*/, {}/*iconst_3*/, {}/*iconst_4*/,
    {}/*iconst_5*/, {}/*lconst_0*/, {}/*lconst_1*/, {}/*fconst_0*/,
    {}/*fconst_1*/, {}/*fconst_2*/, {}/*dconst_0*/, {}/*dconst_1*/,
    {T_BYTE}/*bipush*/, {T_SHORT}/*sipush*/, {T_BYTE}/*ldc*/,
    {T_SHORT}/*ldc_w*/, {T_SHORT}/*ldc2_w*/,
    {T_BYTE}/*iload*/, {T_BYTE}/*lload*/, {T_BYTE}/*fload*/,
    {T_BYTE}/*dload*/, {T_BYTE}/*aload*/, {}/*iload_0*/,
    {}/*iload_1*/, {}/*iload_2*/, {}/*iload_3*/, {}/*lload_0*/,
    {}/*lload_1*/, {}/*lload_2*/, {}/*lload_3*/, {}/*fload_0*/,
    {}/*fload_1*/, {}/*fload_2*/, {}/*fload_3*/, {}/*dload_0*/,
    {}/*dload_1*/, {}/*dload_2*/, {}/*dload_3*/, {}/*aload_0*/,
    {}/*aload_1*/, {}/*aload_2*/, {}/*aload_3*/, {}/*iaload*/,
    {}/*laload*/, {}/*faload*/, {}/*daload*/, {}/*aaload*/,
    {}/*baload*/, {}/*caload*/, {}/*saload*/, {T_BYTE}/*istore*/,
    {T_BYTE}/*lstore*/, {T_BYTE}/*fstore*/, {T_BYTE}/*dstore*/,
    {T_BYTE}/*astore*/, {}/*istore_0*/, {}/*istore_1*/,
    {}/*istore_2*/, {}/*istore_3*/, {}/*lstore_0*/, {}/*lstore_1*/,
    {}/*lstore_2*/, {}/*lstore_3*/, {}/*fstore_0*/, {}/*fstore_1*/,
    {}/*fstore_2*/, {}/*fstore_3*/, {}/*dstore_0*/, {}/*dstore_1*/,
    {}/*dstore_2*/, {}/*dstore_3*/, {}/*astore_0*/, {}/*astore_1*/,
    {}/*astore_2*/, {}/*astore_3*/, {}/*iastore*/, {}/*lastore*/,
    {}/*fastore*/, {}/*dastore*/, {}/*aastore*/, {}/*bastore*/,
    {}/*castore*/, {}/*sastore*/, {}/*pop*/, {}/*pop2*/, {}/*dup*/,
    {}/*dup_x1*/, {}/*dup_x2*/, {}/*dup2*/, {}/*dup2_x1*/,
    {}/*dup2_x2*/, {}/*swap*/, {}/*iadd*/, {}/*ladd*/, {}/*fadd*/,
    {}/*dadd*/, {}/*isub*/, {}/*lsub*/, {}/*fsub*/, {}/*dsub*/,
    {}/*imul*/, {}/*lmul*/, {}/*fmul*/, {}/*dmul*/, {}/*idiv*/,
    {}/*ldiv*/, {}/*fdiv*/, {}/*ddiv*/, {}/*irem*/, {}/*lrem*/,
    {}/*frem*/, {}/*drem*/, {}/*ineg*/, {}/*lneg*/, {}/*fneg*/,
    {}/*dneg*/, {}/*ishl*/, {}/*lshl*/, {}/*ishr*/, {}/*lshr*/,
    {}/*iushr*/, {}/*lushr*/, {}/*iand*/, {}/*land*/, {}/*ior*/,
    {}/*lor*/, {}/*ixor*/, {}/*lxor*/, {T_BYTE, T_BYTE}/*iinc*/,
    {}/*i2l*/, {}/*i2f*/, {}/*i2d*/, {}/*l2i*/, {}/*l2f*/, {}/*l2d*/,
    {}/*f2i*/, {}/*f2l*/, {}/*f2d*/, {}/*d2i*/, {}/*d2l*/, {}/*d2f*/,
    {}/*i2b*/, {}/*i2c*/,{}/*i2s*/, {}/*lcmp*/, {}/*fcmpl*/,
    {}/*fcmpg*/, {}/*dcmpl*/, {}/*dcmpg*/, {T_SHORT}/*ifeq*/,
    {T_SHORT}/*ifne*/, {T_SHORT}/*iflt*/, {T_SHORT}/*ifge*/,
    {T_SHORT}/*ifgt*/, {T_SHORT}/*ifle*/, {T_SHORT}/*if_icmpeq*/,
    {T_SHORT}/*if_icmpne*/, {T_SHORT}/*if_icmplt*/,
    {T_SHORT}/*if_icmpge*/, {T_SHORT}/*if_icmpgt*/,
    {T_SHORT}/*if_icmple*/, {T_SHORT}/*if_acmpeq*/,
    {T_SHORT}/*if_acmpne*/, {T_SHORT}/*goto*/, {T_SHORT}/*jsr*/,
    {T_BYTE}/*ret*/, {}/*tableswitch*/, {}/*lookupswitch*/,
    {}/*ireturn*/, {}/*lreturn*/, {}/*freturn*/, {}/*dreturn*/,
    {}/*areturn*/, {}/*return*/, {T_SHORT}/*getstatic*/,
    {T_SHORT}/*putstatic*/, {T_SHORT}/*getfield*/,
    {T_SHORT}/*putfield*/, {T_SHORT}/*invokevirtual*/,
    {T_SHORT}/*invokespecial*/, {T_SHORT}/*invokestatic*/,
    {T_SHORT, T_BYTE, T_BYTE}/*invokeinterface*/, {T_SHORT, T_BYTE, T_BYTE}/*invokedynamic*/,
    {T_SHORT}/*new*/, {T_BYTE}/*newarray*/,
    {T_SHORT}/*anewarray*/, {}/*arraylength*/, {}/*athrow*/,
    {T_SHORT}/*checkcast*/, {T_SHORT}/*instanceof*/,
    {}/*monitorenter*/, {}/*monitorexit*/, {T_BYTE}/*wide*/,
    {T_SHORT, T_BYTE}/*multianewarray*/, {T_SHORT}/*ifnull*/,
    {T_SHORT}/*ifnonnull*/, {T_INT}/*goto_w*/, {T_INT}/*jsr_w*/,
    {}/*breakpoint*/, {}, {}, {}, {}, {}, {}, {},
    {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
    {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
    {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {},
    {}/*impdep1*/, {}/*impdep2*/
  };

  /**
   * Names of opcodes.  Indexed by opcode.  OPCODE_NAMES[ALOAD] = "aload".
   */
  String[] OPCODE_NAMES = {
    "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1",
    "iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0",
    "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0",
    "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w", "iload",
    "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2",
    "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0",
    "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2",
    "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload",
    "laload", "faload", "daload", "aaload", "baload", "caload", "saload",
    "istore", "lstore", "fstore", "dstore", "astore", "istore_0",
    "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1",
    "lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2",
    "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3",
    "astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore",
    "fastore", "dastore", "aastore", "bastore", "castore", "sastore",
    "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1",
    "dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub",
    "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv",
    "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg",
    "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
    "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f",
    "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
    "i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg",
    "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
    "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt",
    "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret",
    "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn",
    "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield",
    "putfield", "invokevirtual", "invokespecial", "invokestatic",
    "invokeinterface", "invokedynamic", "new", "newarray", "anewarray",
    "arraylength", "athrow", "checkcast", "instanceof", "monitorenter",
    "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull",
    "goto_w", "jsr_w", "breakpoint", ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE, ILLEGAL_OPCODE,
    ILLEGAL_OPCODE, "impdep1", "impdep2"
  };

  /**
   * Number of words consumed on operand stack by instructions.
   * Indexed by opcode.  CONSUME_STACK[FALOAD] = number of words
   * consumed from the stack by a faload instruction.
   */
  int[] CONSUME_STACK = {
    0/*nop*/, 0/*aconst_null*/, 0/*iconst_m1*/, 0/*iconst_0*/, 0/*iconst_1*/,
    0/*iconst_2*/, 0/*iconst_3*/, 0/*iconst_4*/, 0/*iconst_5*/, 0/*lconst_0*/,
    0/*lconst_1*/, 0/*fconst_0*/, 0/*fconst_1*/, 0/*fconst_2*/, 0/*dconst_0*/,
    0/*dconst_1*/, 0/*bipush*/, 0/*sipush*/, 0/*ldc*/, 0/*ldc_w*/, 0/*ldc2_w*/, 0/*iload*/,
    0/*lload*/, 0/*fload*/, 0/*dload*/, 0/*aload*/, 0/*iload_0*/, 0/*iload_1*/, 0/*iload_2*/,
    0/*iload_3*/, 0/*lload_0*/, 0/*lload_1*/, 0/*lload_2*/, 0/*lload_3*/, 0/*fload_0*/,
    0/*fload_1*/, 0/*fload_2*/, 0/*fload_3*/, 0/*dload_0*/, 0/*dload_1*/, 0/*dload_2*/,
    0/*dload_3*/, 0/*aload_0*/, 0/*aload_1*/, 0/*aload_2*/, 0/*aload_3*/, 2/*iaload*/,
    2/*laload*/, 2/*faload*/, 2/*daload*/, 2/*aaload*/, 2/*baload*/, 2/*caload*/, 2/*saload*/,
    1/*istore*/, 2/*lstore*/, 1/*fstore*/, 2/*dstore*/, 1/*astore*/, 1/*istore_0*/,
    1/*istore_1*/, 1/*istore_2*/, 1/*istore_3*/, 2/*lstore_0*/, 2/*lstore_1*/,
    2/*lstore_2*/, 2/*lstore_3*/, 1/*fstore_0*/, 1/*fstore_1*/, 1/*fstore_2*/,
    1/*fstore_3*/, 2/*dstore_0*/, 2/*dstore_1*/, 2/*dstore_2*/, 2/*dstore_3*/,
    1/*astore_0*/, 1/*astore_1*/, 1/*astore_2*/, 1/*astore_3*/, 3/*iastore*/, 4/*lastore*/,
    3/*fastore*/, 4/*dastore*/, 3/*aastore*/, 3/*bastore*/, 3/*castore*/, 3/*sastore*/,
    1/*pop*/, 2/*pop2*/, 1/*dup*/, 2/*dup_x1*/, 3/*dup_x2*/, 2/*dup2*/, 3/*dup2_x1*/,
    4/*dup2_x2*/, 2/*swap*/, 2/*iadd*/, 4/*ladd*/, 2/*fadd*/, 4/*dadd*/, 2/*isub*/, 4/*lsub*/,
    2/*fsub*/, 4/*dsub*/, 2/*imul*/, 4/*lmul*/, 2/*fmul*/, 4/*dmul*/, 2/*idiv*/, 4/*ldiv*/,
    2/*fdiv*/, 4/*ddiv*/, 2/*irem*/, 4/*lrem*/, 2/*frem*/, 4/*drem*/, 1/*ineg*/, 2/*lneg*/,
    1/*fneg*/, 2/*dneg*/, 2/*ishl*/, 3/*lshl*/, 2/*ishr*/, 3/*lshr*/, 2/*iushr*/, 3/*lushr*/,
    2/*iand*/, 4/*land*/, 2/*ior*/, 4/*lor*/, 2/*ixor*/, 4/*lxor*/, 0/*iinc*/,
    1/*i2l*/, 1/*i2f*/, 1/*i2d*/, 2/*l2i*/, 2/*l2f*/, 2/*l2d*/, 1/*f2i*/, 1/*f2l*/,
    1/*f2d*/, 2/*d2i*/, 2/*d2l*/, 2/*d2f*/, 1/*i2b*/, 1/*i2c*/, 1/*i2s*/,
    4/*lcmp*/, 2/*fcmpl*/, 2/*fcmpg*/, 4/*dcmpl*/, 4/*dcmpg*/, 1/*ifeq*/, 1/*ifne*/,
    1/*iflt*/, 1/*ifge*/, 1/*ifgt*/, 1/*ifle*/, 2/*if_icmpeq*/, 2/*if_icmpne*/, 2/*if_icmplt*/,
    2 /*if_icmpge*/, 2/*if_icmpgt*/, 2/*if_icmple*/, 2/*if_acmpeq*/, 2/*if_acmpne*/,
    0/*goto*/, 0/*jsr*/, 0/*ret*/, 1/*tableswitch*/, 1/*lookupswitch*/, 1/*ireturn*/,
    2/*lreturn*/, 1/*freturn*/, 2/*dreturn*/, 1/*areturn*/, 0/*return*/, 0/*getstatic*/,
    UNPREDICTABLE/*putstatic*/, 1/*getfield*/, UNPREDICTABLE/*putfield*/,
    UNPREDICTABLE/*invokevirtual*/, UNPREDICTABLE/*invokespecial*/,
    UNPREDICTABLE/*invokestatic*/,
    UNPREDICTABLE/*invokeinterface*/, UNPREDICTABLE/*invokedynamic*/, 0/*new*/, 1/*newarray*/, 1/*anewarray*/,
    1/*arraylength*/, 1/*athrow*/, 1/*checkcast*/, 1/*instanceof*/, 1/*monitorenter*/,
    1/*monitorexit*/, 0/*wide*/, UNPREDICTABLE/*multianewarray*/, 1/*ifnull*/, 1/*ifnonnull*/,
    0/*goto_w*/, 0/*jsr_w*/, 0/*breakpoint*/, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNPREDICTABLE/*impdep1*/, UNPREDICTABLE/*impdep2*/
  };

  /**
   * Number of words produced onto operand stack by instructions.
   * Indexed by opcode.  CONSUME_STACK[DALOAD] = number of words
   * consumed from the stack by a daload instruction.
   */
  int[] PRODUCE_STACK = {
    0/*nop*/, 1/*aconst_null*/, 1/*iconst_m1*/, 1/*iconst_0*/, 1/*iconst_1*/,
    1/*iconst_2*/, 1/*iconst_3*/, 1/*iconst_4*/, 1/*iconst_5*/, 2/*lconst_0*/,
    2/*lconst_1*/, 1/*fconst_0*/, 1/*fconst_1*/, 1/*fconst_2*/, 2/*dconst_0*/,
    2/*dconst_1*/, 1/*bipush*/, 1/*sipush*/, 1/*ldc*/, 1/*ldc_w*/, 2/*ldc2_w*/, 1/*iload*/,
    2/*lload*/, 1/*fload*/, 2/*dload*/, 1/*aload*/, 1/*iload_0*/, 1/*iload_1*/, 1/*iload_2*/,
    1/*iload_3*/, 2/*lload_0*/, 2/*lload_1*/, 2/*lload_2*/, 2/*lload_3*/, 1/*fload_0*/,
    1/*fload_1*/, 1/*fload_2*/, 1/*fload_3*/, 2/*dload_0*/, 2/*dload_1*/, 2/*dload_2*/,
    2/*dload_3*/, 1/*aload_0*/, 1/*aload_1*/, 1/*aload_2*/, 1/*aload_3*/, 1/*iaload*/,
    2/*laload*/, 1/*faload*/, 2/*daload*/, 1/*aaload*/, 1/*baload*/, 1/*caload*/, 1/*saload*/,
    0/*istore*/, 0/*lstore*/, 0/*fstore*/, 0/*dstore*/, 0/*astore*/, 0/*istore_0*/,
    0/*istore_1*/, 0/*istore_2*/, 0/*istore_3*/, 0/*lstore_0*/, 0/*lstore_1*/,
    0/*lstore_2*/, 0/*lstore_3*/, 0/*fstore_0*/, 0/*fstore_1*/, 0/*fstore_2*/,
    0/*fstore_3*/, 0/*dstore_0*/, 0/*dstore_1*/, 0/*dstore_2*/, 0/*dstore_3*/,
    0/*astore_0*/, 0/*astore_1*/, 0/*astore_2*/, 0/*astore_3*/, 0/*iastore*/, 0/*lastore*/,
    0/*fastore*/, 0/*dastore*/, 0/*aastore*/, 0/*bastore*/, 0/*castore*/, 0/*sastore*/,
    0/*pop*/, 0/*pop2*/, 2/*dup*/, 3/*dup_x1*/, 4/*dup_x2*/, 4/*dup2*/, 5/*dup2_x1*/,
    6/*dup2_x2*/, 2/*swap*/, 1/*iadd*/, 2/*ladd*/, 1/*fadd*/, 2/*dadd*/, 1/*isub*/, 2/*lsub*/,
    1/*fsub*/, 2/*dsub*/, 1/*imul*/, 2/*lmul*/, 1/*fmul*/, 2/*dmul*/, 1/*idiv*/, 2/*ldiv*/,
    1/*fdiv*/, 2/*ddiv*/, 1/*irem*/, 2/*lrem*/, 1/*frem*/, 2/*drem*/, 1/*ineg*/, 2/*lneg*/,
    1/*fneg*/, 2/*dneg*/, 1/*ishl*/, 2/*lshl*/, 1/*ishr*/, 2/*lshr*/, 1/*iushr*/, 2/*lushr*/,
    1/*iand*/, 2/*land*/, 1/*ior*/, 2/*lor*/, 1/*ixor*/, 2/*lxor*/,
    0/*iinc*/, 2/*i2l*/, 1/*i2f*/, 2/*i2d*/, 1/*l2i*/, 1/*l2f*/, 2/*l2d*/, 1/*f2i*/,
    2/*f2l*/, 2/*f2d*/, 1/*d2i*/, 2/*d2l*/, 1/*d2f*/,
    1/*i2b*/, 1/*i2c*/, 1/*i2s*/, 1/*lcmp*/, 1/*fcmpl*/, 1/*fcmpg*/,
    1/*dcmpl*/, 1/*dcmpg*/, 0/*ifeq*/, 0/*ifne*/, 0/*iflt*/, 0/*ifge*/, 0/*ifgt*/, 0/*ifle*/,
    0/*if_icmpeq*/, 0/*if_icmpne*/, 0/*if_icmplt*/, 0/*if_icmpge*/, 0/*if_icmpgt*/,
    0/*if_icmple*/, 0/*if_acmpeq*/, 0/*if_acmpne*/, 0/*goto*/, 1/*jsr*/, 0/*ret*/,
    0/*tableswitch*/, 0/*lookupswitch*/, 0/*ireturn*/, 0/*lreturn*/, 0/*freturn*/,
    0/*dreturn*/, 0/*areturn*/, 0/*return*/, UNPREDICTABLE/*getstatic*/, 0/*putstatic*/,
    UNPREDICTABLE/*getfield*/, 0/*putfield*/, UNPREDICTABLE/*invokevirtual*/,
    UNPREDICTABLE/*invokespecial*/, UNPREDICTABLE/*invokestatic*/,
    UNPREDICTABLE/*invokeinterface*/, UNPREDICTABLE/*invokedynamic*/, 1/*new*/, 1/*newarray*/, 1/*anewarray*/,
    1/*arraylength*/, 1/*athrow*/, 1/*checkcast*/, 1/*instanceof*/, 0/*monitorenter*/,
    0/*monitorexit*/, 0/*wide*/, 1/*multianewarray*/, 0/*ifnull*/, 0/*ifnonnull*/,
    0/*goto_w*/, 1/*jsr_w*/, 0/*breakpoint*/, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED,
    UNDEFINED, UNPREDICTABLE/*impdep1*/, UNPREDICTABLE/*impdep2*/
  };

  /** Attributes and their corresponding names.
   */
  byte ATTR_UNKNOWN                                 = -1;
  byte ATTR_SOURCE_FILE                             = 0;
  byte ATTR_CONSTANT_VALUE                          = 1;
  byte ATTR_CODE                                    = 2;
  byte ATTR_EXCEPTIONS                              = 3;
  byte ATTR_LINE_NUMBER_TABLE                       = 4;
  byte ATTR_LOCAL_VARIABLE_TABLE                    = 5;
  byte ATTR_INNER_CLASSES                           = 6;
  byte ATTR_SYNTHETIC                               = 7;
  byte ATTR_DEPRECATED                              = 8;
  byte ATTR_PMG                                     = 9;
  byte ATTR_SIGNATURE                               = 10;
  byte ATTR_STACK_MAP                               = 11;
  byte ATTR_RUNTIMEVISIBLE_ANNOTATIONS              = 12;
  byte ATTR_RUNTIMEINVISIBLE_ANNOTATIONS            = 13;
  byte ATTR_RUNTIMEVISIBLE_PARAMETER_ANNOTATIONS    = 14;
  byte ATTR_RUNTIMEINVISIBLE_PARAMETER_ANNOTATIONS  = 15;
  byte ATTR_ANNOTATION_DEFAULT                      = 16;

  short KNOWN_ATTRIBUTES = 12;//should be 17


  // TODO: mutable public array!!
  String[] ATTRIBUTE_NAMES = {
    "SourceFile", "ConstantValue", "Code", "Exceptions",
    "LineNumberTable", "LocalVariableTable",
    "InnerClasses", "Synthetic", "Deprecated",
    "PMGClass", "Signature", "StackMap",
    "RuntimeVisibleAnnotations", "RuntimeInvisibleAnnotations",
    "RuntimeVisibleParameterAnnotations", "RuntimeInvisibleParameterAnnotations",
    "AnnotationDefault"
  };

  /** Constants used in the StackMap attribute.
   */
  byte ITEM_Bogus      = 0;
  byte ITEM_Integer    = 1;
  byte ITEM_Float      = 2;
  byte ITEM_Double     = 3;
  byte ITEM_Long       = 4;
  byte ITEM_Null       = 5;
  byte ITEM_InitObject = 6;
  byte ITEM_Object     = 7;
  byte ITEM_NewObject  = 8;

  String[] ITEM_NAMES = {
    "Bogus", "Integer", "Float", "Double", "Long",
    "Null", "InitObject", "Object", "NewObject"
  };

}
