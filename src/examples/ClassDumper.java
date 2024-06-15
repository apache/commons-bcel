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
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.BCELifier;

/**
 * Display Java .class file data. Output is based on javap tool. Built using the BCEL libary.
 */
final class ClassDumper {

    private final FileImageInputStream file;
    private final String fileName;
    private int classNameIndex;
    private int superclassNameIndex;
    private int major;
    private int minor; // Compiler version
    private int accessFlags; // Access rights of parsed class
    private int[] interfaces; // Names of implemented interfaces
    private ConstantPool constantPool; // collection of constants
    private Constant[] constantItems; // collection of constants
    private Field[] fields; // class fields, i.e., its variables
    private Method[] methods; // methods defined in the class
    private Attribute[] attributes; // attributes defined in the class

    /**
     * Parses class from the given stream.
     *
     * @param file Input stream
     * @param fileName File name
     */
    public ClassDumper(final FileImageInputStream file, final String fileName) {
        this.fileName = fileName;
        this.file = file;
    }

    private final String constantToString(final int index) {
        final Constant c = constantItems[index];
        return constantPool.constantToString(c);
    }

    /**
     * Parses the given Java class file and return an object that represents the contained data, i.e., constants, methods,
     * fields and commands. A <em>ClassFormatException</em> is raised, if the file is not a valid .class file. (This does
     * not include verification of the byte code as it is performed by the Java interpreter).
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    public void dump() throws IOException, ClassFormatException {
        try {
            // Check magic tag of class file
            processID();
            // Get compiler version
            processVersion();
            // process constant pool entries
            processConstantPool();
            // Get class information
            processClassInfo();
            // Get interface information, i.e., implemented interfaces
            processInterfaces();
            // process class fields, i.e., the variables of the class
            processFields();
            // process class methods, i.e., the functions in the class
            processMethods();
            // process class attributes
            processAttributes();
        } finally {
            // Processed everything of interest, so close the file
            try {
                if (file != null) {
                    file.close();
                }
            } catch (final IOException ioe) {
                // ignore close exceptions
            }
        }
    }

    /**
     * Processes information about the attributes of the class.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processAttributes() throws IOException, ClassFormatException {
        final int attributesCount = file.readUnsignedShort();
        attributes = new Attribute[attributesCount];

        System.out.printf("%nAttributes(%d):%n", attributesCount);

        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = Attribute.readAttribute(file, constantPool);
            // indent all lines by two spaces
            final String[] lines = attributes[i].toString().split("\\r?\\n");
            for (final String line : lines) {
                System.out.println("  " + line);
            }
        }
    }

    /**
     * Processes information about the class and its super class.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processClassInfo() throws IOException, ClassFormatException {
        accessFlags = file.readUnsignedShort();
        /*
         * Interfaces are implicitly abstract, the flag should be set according to the JVM specification.
         */
        if ((accessFlags & Const.ACC_INTERFACE) != 0) {
            accessFlags |= Const.ACC_ABSTRACT;
        }
        if ((accessFlags & Const.ACC_ABSTRACT) != 0 && (accessFlags & Const.ACC_FINAL) != 0) {
            throw new ClassFormatException("Class " + fileName + " can't be both final and abstract");
        }

        System.out.printf("%nClass info:%n");
        System.out.println("  flags: " + BCELifier.printFlags(accessFlags, BCELifier.FLAGS.CLASS));
        classNameIndex = file.readUnsignedShort();
        System.out.printf("  this_class: %d (", classNameIndex);
        System.out.println(constantToString(classNameIndex) + ")");

        superclassNameIndex = file.readUnsignedShort();
        System.out.printf("  super_class: %d (", superclassNameIndex);
        if (superclassNameIndex > 0) {
            System.out.printf("%s", constantToString(superclassNameIndex));
        }
        System.out.println(")");
    }

    /**
     * Processes constant pool entries.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processConstantPool() throws IOException, ClassFormatException {
        byte tag;
        final int constantPoolCount = file.readUnsignedShort();
        constantItems = new Constant[constantPoolCount];
        constantPool = new ConstantPool(constantItems);

        // constantPool[0] is unused by the compiler
        System.out.printf("%nConstant pool(%d):%n", constantPoolCount - 1);

        for (int i = 1; i < constantPoolCount; i++) {
            constantItems[i] = Constant.readConstant(file);
            // i'm sure there is a better way to do this
            if (i < 10) {
                System.out.printf("    #%1d = ", i);
            } else if (i < 100) {
                System.out.printf("   #%2d = ", i);
            } else {
                System.out.printf("  #%d = ", i);
            }
            System.out.println(constantItems[i]);

            // All eight byte constants take up two spots in the constant pool
            tag = constantItems[i].getTag();
            if (tag == Const.CONSTANT_Double || tag == Const.CONSTANT_Long) {
                i++;
            }
        }
    }

    /**
     * Constructs object from file stream.
     *
     * @param file Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processFieldOrMethod() throws IOException, ClassFormatException {
        final int accessFlags = file.readUnsignedShort();
        final int nameIndex = file.readUnsignedShort();
        System.out.printf("  nameIndex: %d (", nameIndex);
        System.out.println(constantToString(nameIndex) + ")");
        System.out.println("  accessFlags: " + BCELifier.printFlags(accessFlags, BCELifier.FLAGS.METHOD));
        final int descriptorIndex = file.readUnsignedShort();
        System.out.printf("  descriptorIndex: %d (", descriptorIndex);
        System.out.println(constantToString(descriptorIndex) + ")");

        final int attributesCount = file.readUnsignedShort();
        final Attribute[] attributes = new Attribute[attributesCount];
        System.out.println("  attribute count: " + attributesCount);

        for (int i = 0; i < attributesCount; i++) {
            // going to peek ahead a bit
            file.mark();
            final int attributeNameIndex = file.readUnsignedShort();
            final int attributeLength = file.readInt();
            // restore file location
            file.reset();
            // Usefull for debugging
            // System.out.printf(" attribute_name_index: %d (", attribute_name_index);
            // System.out.println(constantToString(attribute_name_index) + ")");
            // System.out.printf(" atribute offset in file: %x%n", + file.getStreamPosition());
            // System.out.println(" atribute_length: " + attribute_length);

            // A stronger verification test would be to read attribute_length bytes
            // into a buffer. Then pass that buffer to readAttribute and also
            // verify we're at EOF of the buffer on return.

            final long pos1 = file.getStreamPosition();
            attributes[i] = Attribute.readAttribute(file, constantPool);
            final long pos2 = file.getStreamPosition();
            if (pos2 - pos1 != attributeLength + 6) {
                System.out.printf("%nattributeLength: %d pos2-pos1-6: %d pos1: %x(%d) pos2: %x(%d)%n", attributeLength, pos2 - pos1 - 6, pos1, pos1, pos2,
                    pos2);
            }
            System.out.printf("  ");
            System.out.println(attributes[i]);
        }
    }

    /**
     * Processes information about the fields of the class, i.e., its variables.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processFields() throws IOException, ClassFormatException {
        final int fieldsCount = file.readUnsignedShort();
        fields = new Field[fieldsCount];

        // sometimes fields[0] is magic used for serialization
        System.out.printf("%nFields(%d):%n", fieldsCount);

        for (int i = 0; i < fieldsCount; i++) {
            processFieldOrMethod();
            if (i < fieldsCount - 1) {
                System.out.println();
            }
        }
    }

    /**
     * Checks whether the header of the file is ok. Of course, this has to be the first action on successive file reads.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processID() throws IOException, ClassFormatException {
        final int magic = file.readInt();
        if (magic != Const.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException(fileName + " is not a Java .class file");
        }
        System.out.println("Java Class Dump");
        System.out.println("  file: " + fileName);
        System.out.printf("%nClass header:%n");
        System.out.printf("  magic: %X%n", magic);
    }

    /**
     * Processes information about the interfaces implemented by this class.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processInterfaces() throws IOException, ClassFormatException {
        final int interfacesCount = file.readUnsignedShort();
        interfaces = new int[interfacesCount];

        System.out.printf("%nInterfaces(%d):%n", interfacesCount);

        for (int i = 0; i < interfacesCount; i++) {
            interfaces[i] = file.readUnsignedShort();
            // i'm sure there is a better way to do this
            if (i < 10) {
                System.out.printf("   #%1d = ", i);
            } else if (i < 100) {
                System.out.printf("  #%2d = ", i);
            } else {
                System.out.printf(" #%d = ", i);
            }
            System.out.println(interfaces[i] + " (" + constantPool.getConstantString(interfaces[i], Const.CONSTANT_Class) + ")");
        }
    }

    /**
     * Processes information about the methods of the class.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processMethods() throws IOException, ClassFormatException {
        final int methodsCount = file.readUnsignedShort();
        methods = new Method[methodsCount];

        System.out.printf("%nMethods(%d):%n", methodsCount);

        for (int i = 0; i < methodsCount; i++) {
            processFieldOrMethod();
            if (i < methodsCount - 1) {
                System.out.println();
            }
        }
    }

    /**
     * Processes major and minor version of compiler which created the file.
     *
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processVersion() throws IOException, ClassFormatException {
        minor = file.readUnsignedShort();
        System.out.printf("  minor version: %s%n", minor);

        major = file.readUnsignedShort();
        System.out.printf("  major version: %s%n", major);
    }

}

final class DumpClass {

    public static void main(final String[] args) throws IOException {

        if (args.length != 1) {
            throw new IllegalArgumentException("Require file name as only argument");
        }

        try (FileImageInputStream file = new FileImageInputStream(new File(args[0]))) {

            final ClassDumper cd = new ClassDumper(file, args[0]);
            cd.dump();
        }

        System.out.printf("End of Class Dump%n");

    }
}
