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
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.BCELifier;

/** 
 * Display Java .class file data.
 * Output is based on javap tool.
 * Built using the BCEL libary.
 *
 */


class ClassDumper {

    private FileImageInputStream file;
    private String file_name;
    private int class_name_index;
    private int superclass_name_index;
    private int major;
    private int minor; // Compiler version
    private int access_flags; // Access rights of parsed class
    private int[] interfaces; // Names of implemented interfaces
    private ConstantPool constant_pool; // collection of constants
    private Constant[] constant_items; // collection of constants
    private Field[] fields; // class fields, i.e., its variables
    private Method[] methods; // methods defined in the class
    private Attribute[] attributes; // attributes defined in the class

    /**
     * Parse class from the given stream.
     *
     * @param file Input stream
     * @param file_name File name
     */
    public ClassDumper (FileImageInputStream file, String file_name) {
        this.file_name = file_name;
        this.file = file;
    }

    /**
     * Parse the given Java class file and return an object that represents
     * the contained data, i.e., constants, methods, fields and commands.
     * A <em>ClassFormatException</em> is raised, if the file is not a valid
     * .class file. (This does not include verification of the byte code as it
     * is performed by the java interpreter).
     *
     * @throws  IOException
     * @throws  ClassFormatException
     */
    public void dump () throws IOException, ClassFormatException {
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
            } catch (IOException ioe) {
                //ignore close exceptions
            }
        }
    }

    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processID () throws IOException, ClassFormatException {
        final int magic = file.readInt();
        if (magic != Constants.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException(file_name + " is not a Java .class file");
        }
        System.out.println("Java Class Dump"); 
        System.out.println("  file: " + file_name); 
        System.out.printf("%nClass header:%n"); 
        System.out.printf("  magic: %X%n", magic); 
    }

    /**
     * Process major and minor version of compiler which created the file.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processVersion () throws IOException, ClassFormatException {
        minor = file.readUnsignedShort();
        System.out.printf("  minor version: %s%n", minor); 

        major = file.readUnsignedShort();
        System.out.printf("  major version: %s%n", major); 
    }

    /**
     * Process constant pool entries.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processConstantPool () throws IOException, ClassFormatException {
        byte tag;
        int constant_pool_count = file.readUnsignedShort();
        constant_items = new Constant[constant_pool_count];
        constant_pool = new ConstantPool(constant_items);

        // constant_pool[0] is unused by the compiler
        System.out.printf("%nConstant pool(%d):%n", constant_pool_count - 1); 

        for (int i = 1; i < constant_pool_count; i++) {
            constant_items[i] = Constant.readConstant(file);
            // i'm sure there is a better way to do this
            if (i < 10) {
                System.out.printf("    #%1d = ", i); 
            } else if (i <100) {
                System.out.printf("   #%2d = ", i); 
            } else {
                System.out.printf("  #%d = ", i); 
            }    
            System.out.println(constant_items[i]); 

            // All eight byte constants take up two spots in the constant pool
            tag = constant_items[i].getTag();
            if ((tag == Constants.CONSTANT_Double) ||
                    (tag == Constants.CONSTANT_Long)) {
                i++;
            }
        }
    }

    /**
     * Process information about the class and its super class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processClassInfo () throws IOException, ClassFormatException {
        access_flags = file.readUnsignedShort();
        /* Interfaces are implicitely abstract, the flag should be set
         * according to the JVM specification.
         */
        if ((access_flags & Constants.ACC_INTERFACE) != 0) {
            access_flags |= Constants.ACC_ABSTRACT;
        }
        if (((access_flags & Constants.ACC_ABSTRACT) != 0)
                && ((access_flags & Constants.ACC_FINAL) != 0)) {
            throw new ClassFormatException("Class " + file_name +
                    " can't be both final and abstract");
        }

        System.out.printf("%nClass info:%n"); 
        System.out.println("  flags: " + BCELifier.printFlags(access_flags,
                BCELifier.FLAGS.CLASS));
        class_name_index = file.readUnsignedShort();
        System.out.printf("  this_class: %d (", class_name_index); 
        System.out.println(constantToString(class_name_index) + ")"); 

        superclass_name_index = file.readUnsignedShort();
        System.out.printf("  super_class: %d (", superclass_name_index); 
        System.out.println(constantToString(superclass_name_index) + ")"); 
    }

    /**
     * Process information about the interfaces implemented by this class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processInterfaces () throws IOException, ClassFormatException {
        int interfaces_count;
        interfaces_count = file.readUnsignedShort();
        interfaces = new int[interfaces_count];

        System.out.printf("%nInterfaces(%d):%n", interfaces_count); 

        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = file.readUnsignedShort();
            // i'm sure there is a better way to do this
            if (i < 10) {
                System.out.printf("   #%1d = ", i); 
            } else if (i <100) {
                System.out.printf("  #%2d = ", i); 
            } else {
                System.out.printf(" #%d = ", i); 
            }
            System.out.println(interfaces[i] + " (" +
                    constant_pool.getConstantString(interfaces[i],
                            Constants.CONSTANT_Class) + ")"); 
        }
    }

    /**
     * Process information about the fields of the class, i.e., its variables.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processFields () throws IOException, ClassFormatException {
        int fields_count;
        fields_count = file.readUnsignedShort();
        fields = new Field[fields_count];

        // sometimes fields[0] is magic used for serialization 
        System.out.printf("%nFields(%d):%n", fields_count); 

        for (int i = 0; i < fields_count; i++) {
            processFieldOrMethod();
            if (i < fields_count - 1) {
                System.out.println(); 
            }
        }
    }

    /**
     * Process information about the methods of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processMethods () throws IOException, ClassFormatException {
        int methods_count;
        methods_count = file.readUnsignedShort();
        methods = new Method[methods_count];

        System.out.printf("%nMethods(%d):%n", methods_count); 

        for (int i = 0; i < methods_count; i++) {
            processFieldOrMethod();
            if (i < methods_count - 1) {
                System.out.println(); 
            }
        }
    }

    /**
     * Process information about the attributes of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void processAttributes () throws IOException, ClassFormatException {
        int attributes_count;
        attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];

        System.out.printf("%nAttributes(%d):%n", attributes_count); 

        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
            System.out.printf("  %s%n", attributes[i]); 
        }
    }

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    private final void processFieldOrMethod () throws IOException, ClassFormatException {
        int access_flags = file.readUnsignedShort();
        int name_index = file.readUnsignedShort();
        System.out.printf("  name_index: %d (", name_index); 
        System.out.println(constantToString(name_index) + ")"); 
        System.out.println("  access_flags: " + BCELifier.printFlags(access_flags,
                BCELifier.FLAGS.METHOD));
        int descriptor_index = file.readUnsignedShort();
        System.out.printf("  descriptor_index: %d (", descriptor_index); 
        System.out.println(constantToString(descriptor_index) + ")"); 

        int attributes_count = file.readUnsignedShort();
        Attribute[] attributes = new Attribute[attributes_count];
        System.out.println("  attribute count: " + attributes_count); 

        for (int i = 0; i < attributes_count; i++) {
            // going to peek ahead a bit
            file.mark();
            int attribute_name_index = file.readUnsignedShort();
            int attribute_length = file.readInt();
            // restore file location
            file.reset();
            // Usefull for debugging
            // System.out.printf("  attribute_name_index: %d (", attribute_name_index); 
            // System.out.println(constantToString(attribute_name_index) + ")"); 
            // System.out.printf("  atribute offset in file: %x%n", + file.getStreamPosition()); 
            // System.out.println("  atribute_length: " + attribute_length); 

            // A stronger verification test would be to read attribute_length bytes
            // into a buffer.  Then pass that buffer to readAttribute and also
            // verify we're at EOF of the buffer on return.

            long pos1 = file.getStreamPosition();
            attributes[i] = Attribute.readAttribute(file, constant_pool);
            long pos2 = file.getStreamPosition();
            if ((pos2 - pos1) != (attribute_length + 6)) {
                System.out.printf("%nWHOOPS attribute_length: %d pos2-pos1-6: %d pos1: %x(%d) pos2: %x(%d)%n",
                        attribute_length, pos2-pos1-6, pos1, pos1, pos2, pos2); 
            }
            System.out.printf("  "); 
            System.out.println(attributes[i]); 
        }
    }

    private final String constantToString (int index) {
        Constant c = constant_items[index]; 
        return constant_pool.constantToString(c); 
    }    

}

class DumpClass {

    public static void main (String[] args) throws IOException {

        if (args.length != 1) {
            throw new RuntimeException("Require filename as only argument");
        }

        FileImageInputStream file = new FileImageInputStream(new File(args[0]));

        ClassDumper cd = new ClassDumper(file, args[0]);
        cd.dump();

        System.out.printf("End of Class Dump%n"); 

    }
}
