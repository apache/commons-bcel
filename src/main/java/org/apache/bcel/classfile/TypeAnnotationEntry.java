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
package org.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

import org.apache.bcel.util.Args;

/**
 * Represents one type annotation of a {@link TypeAnnotations} attribute (JVMS 4.7.20): an annotation, the kind of program element it applies to
 * (the target type and its target info), and the path to the annotated type inside that element.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7.20">JVMS 4.7.20 The RuntimeVisibleTypeAnnotations attribute</a>
 * @since 6.14.0
 */
public class TypeAnnotationEntry {

    /** Target type: type parameter declaration of a generic class or interface. */
    public static final int TARGET_CLASS_TYPE_PARAMETER = 0x00;

    /** Target type: type parameter declaration of a generic method or constructor. */
    public static final int TARGET_METHOD_TYPE_PARAMETER = 0x01;

    /** Target type: type in an extends or implements clause of a class or interface declaration. */
    public static final int TARGET_CLASS_EXTENDS = 0x10;

    /** Target type: bound of a type parameter of a generic class or interface. */
    public static final int TARGET_CLASS_TYPE_PARAMETER_BOUND = 0x11;

    /** Target type: bound of a type parameter of a generic method or constructor. */
    public static final int TARGET_METHOD_TYPE_PARAMETER_BOUND = 0x12;

    /** Target type: type in a field or record component declaration. */
    public static final int TARGET_FIELD = 0x13;

    /** Target type: return type of a method, or type of a newly constructed object. */
    public static final int TARGET_METHOD_RETURN = 0x14;

    /** Target type: receiver type of a method or constructor. */
    public static final int TARGET_METHOD_RECEIVER = 0x15;

    /** Target type: type in a formal parameter declaration of a method, constructor, or lambda expression. */
    public static final int TARGET_METHOD_FORMAL_PARAMETER = 0x16;

    /** Target type: type in a throws clause of a method or constructor. */
    public static final int TARGET_THROWS = 0x17;

    /** Target type: type in a local variable declaration. */
    public static final int TARGET_LOCAL_VARIABLE = 0x40;

    /** Target type: type in a resource variable declaration. */
    public static final int TARGET_RESOURCE_VARIABLE = 0x41;

    /** Target type: type in an exception parameter declaration. */
    public static final int TARGET_EXCEPTION_PARAMETER = 0x42;

    /** Target type: type in an instanceof expression. */
    public static final int TARGET_INSTANCEOF = 0x43;

    /** Target type: type in a new expression. */
    public static final int TARGET_NEW = 0x44;

    /** Target type: type in a method reference expression using ::new. */
    public static final int TARGET_CONSTRUCTOR_REFERENCE = 0x45;

    /** Target type: type in a method reference expression using ::Identifier. */
    public static final int TARGET_METHOD_REFERENCE = 0x46;

    /** Target type: type in a cast expression. */
    public static final int TARGET_CAST = 0x47;

    /** Target type: type argument for a generic constructor in a new expression or an explicit constructor invocation. */
    public static final int TARGET_CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 0x48;

    /** Target type: type argument for a generic method in a method invocation expression. */
    public static final int TARGET_METHOD_INVOCATION_TYPE_ARGUMENT = 0x49;

    /** Target type: type argument for a generic constructor in a method reference expression using ::new. */
    public static final int TARGET_CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 0x4A;

    /** Target type: type argument for a generic method in a method reference expression using ::Identifier. */
    public static final int TARGET_METHOD_REFERENCE_TYPE_ARGUMENT = 0x4B;

    /**
     * Empty array of TypeAnnotationEntry objects.
     */
    public static final TypeAnnotationEntry[] EMPTY_ARRAY = {};

    /** Maximum number of steps of a type path, which its length of one byte limits. */
    private static final int MAX_TYPE_PATH_LENGTH = 255;

    /** Number of bytes of a {@code localvar_target} entry: start_pc, length and index. */
    private static final int LOCAL_VARIABLE_RANGE_SIZE = 6;

    /**
     * Gets the number of bytes of the target info that follows a target type.
     *
     * @param targetType The target type.
     * @return the size of the target info, or -1 if it depends on its content (the table of a {@code localvar_target}).
     * @throws ClassFormatException Thrown if the target type is not defined.
     */
    private static int targetInfoSize(final int targetType) {
        switch (targetType) {
        case TARGET_CLASS_TYPE_PARAMETER:
        case TARGET_METHOD_TYPE_PARAMETER:
        case TARGET_METHOD_FORMAL_PARAMETER:
            return 1;
        case TARGET_CLASS_EXTENDS:
        case TARGET_CLASS_TYPE_PARAMETER_BOUND:
        case TARGET_METHOD_TYPE_PARAMETER_BOUND:
        case TARGET_THROWS:
        case TARGET_EXCEPTION_PARAMETER:
        case TARGET_INSTANCEOF:
        case TARGET_NEW:
        case TARGET_CONSTRUCTOR_REFERENCE:
        case TARGET_METHOD_REFERENCE:
            return 2;
        case TARGET_FIELD:
        case TARGET_METHOD_RETURN:
        case TARGET_METHOD_RECEIVER:
            return 0;
        case TARGET_LOCAL_VARIABLE:
        case TARGET_RESOURCE_VARIABLE:
            return -1;
        case TARGET_CAST:
        case TARGET_CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
        case TARGET_METHOD_INVOCATION_TYPE_ARGUMENT:
        case TARGET_CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
        case TARGET_METHOD_REFERENCE_TYPE_ARGUMENT:
            return 3;
        default:
            throw new ClassFormatException("Invalid type annotation target type: 0x" + Integer.toHexString(targetType));
        }
    }

    private static void checkTargetInfo(final int targetType, final byte[] targetInfo) {
        final int size = targetInfoSize(targetType);
        if (size >= 0) {
            if (targetInfo.length != size) {
                throw new IllegalArgumentException("The target info of the target type 0x" + Integer.toHexString(targetType) + " must have " + size
                        + " bytes, not " + targetInfo.length);
            }
        } else if (targetInfo.length < 2 || targetInfo.length != 2 + ((targetInfo[0] & 0xFF) << 8 | targetInfo[1] & 0xFF) * LOCAL_VARIABLE_RANGE_SIZE) {
            throw new IllegalArgumentException("The target info of the target type 0x" + Integer.toHexString(targetType)
                    + " must be a length followed by that many ranges of 6 bytes");
        }
    }

    /**
     * Factory method to create a TypeAnnotationEntry from a DataInput.
     *
     * @param input The input stream.
     * @param constantPool The constant pool.
     * @param isRuntimeVisible whether the annotation is runtime visible.
     * @return The entry.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public static TypeAnnotationEntry read(final DataInput input, final ConstantPool constantPool, final boolean isRuntimeVisible) throws IOException {
        final int targetType = input.readUnsignedByte();
        final int size = targetInfoSize(targetType);
        final byte[] targetInfo;
        if (size >= 0) {
            targetInfo = new byte[size];
            input.readFully(targetInfo);
        } else {
            final int tableLength = input.readUnsignedShort();
            targetInfo = new byte[2 + tableLength * LOCAL_VARIABLE_RANGE_SIZE];
            targetInfo[0] = (byte) (tableLength >>> 8);
            targetInfo[1] = (byte) tableLength;
            input.readFully(targetInfo, 2, targetInfo.length - 2);
        }
        final byte[] typePath = new byte[input.readUnsignedByte() * 2];
        input.readFully(typePath);
        return new TypeAnnotationEntry(targetType, targetInfo, typePath, AnnotationEntry.read(input, constantPool, isRuntimeVisible));
    }

    private final int targetType;

    private final byte[] targetInfo;

    private final byte[] typePath;

    private final AnnotationEntry annotationEntry;

    /**
     * Constructs a TypeAnnotationEntry.
     *
     * @param targetType The target type, one of the {@code TARGET_*} constants.
     * @param targetInfo The target info, as it is stored in the class file.
     * @param typePath The type path, as it is stored in the class file without its length: a pair of bytes (path kind, type argument index) per step.
     * @param annotationEntry The annotation.
     * @throws ClassFormatException Thrown if the target type is not defined.
     * @throws IllegalArgumentException Thrown if the target info does not have the size its target type requires, or if the type path is not a sequence of pairs
     *         of bytes of at most 255 steps.
     */
    public TypeAnnotationEntry(final int targetType, final byte[] targetInfo, final byte[] typePath, final AnnotationEntry annotationEntry) {
        checkTargetInfo(targetType, targetInfo);
        if (typePath.length % 2 != 0 || typePath.length > MAX_TYPE_PATH_LENGTH * 2) {
            throw new IllegalArgumentException("The type path must be at most " + MAX_TYPE_PATH_LENGTH + " pairs of bytes (kind, argument index), not "
                    + typePath.length + " bytes");
        }
        this.targetType = targetType;
        this.targetInfo = targetInfo.clone();
        this.typePath = typePath.clone();
        this.annotationEntry = annotationEntry;
    }

    /**
     * Creates a deep copy of this entry, whose annotation is independent of this one and refers to the given constant pool. The indexes it holds are kept, as they
     * are in {@link Attribute#copy(ConstantPool)}: the constant pool must have the same constants as the one of this entry at these indexes.
     *
     * @param constantPool The constant pool of the copy.
     * @return A deep copy of this entry.
     */
    public TypeAnnotationEntry copy(final ConstantPool constantPool) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            try (DataOutputStream dos = new DataOutputStream(out)) {
                dump(dos);
            }
            return read(new DataInputStream(new ByteArrayInputStream(out.toByteArray())), constantPool, annotationEntry.isRuntimeVisible());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes this entry to a DataOutputStream.
     *
     * @param dos The data output stream.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public void dump(final DataOutputStream dos) throws IOException {
        dos.writeByte(targetType);
        dos.write(targetInfo);
        dos.writeByte(Args.requireU1(typePath.length / 2, "typePath.length"));
        dos.write(typePath);
        annotationEntry.dump(dos);
    }

    /**
     * Gets the annotation.
     *
     * @return The annotation.
     */
    public AnnotationEntry getAnnotationEntry() {
        return annotationEntry;
    }

    /**
     * Gets the target info, which depends on the target type (JVMS table 4.7.20-A): for instance the index of the type parameter for
     * {@link #TARGET_CLASS_TYPE_PARAMETER}, the index of the type parameter then the index of the bound for {@link #TARGET_CLASS_TYPE_PARAMETER_BOUND}, or the
     * index of the parameter for {@link #TARGET_METHOD_FORMAL_PARAMETER}.
     *
     * @return A copy of the target info, as it is stored in the class file.
     */
    public byte[] getTargetInfo() {
        return targetInfo.clone();
    }

    /**
     * Gets the kind of program element the annotation applies to.
     *
     * @return One of the {@code TARGET_*} constants.
     */
    public int getTargetType() {
        return targetType;
    }

    /**
     * Gets the number of steps in the type path.
     *
     * <p>
     * The path leads from the type of the target to the part of it that is annotated. An empty path identifies the outermost part of the type where an
     * annotation is allowed. For a non-static inner class, {@code @A Outer.Inner} has an empty path, while {@code Outer.@A Inner} requires one step.
     * For a static nested class, {@code Outer.@A Nested} has an empty path.
     * </p>
     *
     * @return The number of steps of the type path.
     */
    public int getTypePathLength() {
        return typePath.length / 2;
    }

    /**
     * Gets the kind of a step of the type path: 0 deeper in an array type, 1 deeper in a nested type, 2 on the bound of a wildcard type argument, 3 on a type
     * argument of a parameterized type.
     *
     * @param step Index of the step, from 0 to {@link #getTypePathLength()} excluded.
     * @return The kind of the step.
     */
    public int getTypePathKind(final int step) {
        return typePath[step * 2] & 0xFF;
    }

    /**
     * Gets the index of the type argument for a step of the type path of kind 3, else 0.
     *
     * @param step Index of the step, from 0 to {@link #getTypePathLength()} excluded.
     * @return The index of the type argument.
     */
    public int getTypePathArgumentIndex(final int step) {
        return typePath[step * 2 + 1] & 0xFF;
    }

    @Override
    public String toString() {
        return "TypeAnnotationEntry(target=0x" + Integer.toHexString(targetType) + ", info=" + Arrays.toString(targetInfo) + ", path=" + Arrays.toString(typePath)
                + ", " + annotationEntry + ")";
    }
}
