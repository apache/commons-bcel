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

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.util.Args;

/**
 * Base class for the type annotation attributes, {@link RuntimeVisibleTypeAnnotations} and {@link RuntimeInvisibleTypeAnnotations}: the annotations written on
 * the types used in a declaration (such as {@code List<@Nullable String>}, the supertypes of a class, a {@code throws} clause, or the declaration and the
 * bounds of a type parameter as in {@code <T extends @Nullable Object>}) or in the code of a method (such as a cast, an {@code instanceof}, a {@code new}
 * expression or a local variable), which the attributes of the declaration annotations do not hold. They are attributes of a class, a field, a method, a
 * record component or a {@link Code} attribute.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7.20">JVMS 4.7.20 The RuntimeVisibleTypeAnnotations attribute</a>
 * @since 6.14.0
 */
public abstract class TypeAnnotations extends Attribute implements Iterable<TypeAnnotationEntry> {

    private TypeAnnotationEntry[] typeAnnotationTable;
    private final boolean isRuntimeVisible;

    /**
     * Constructs an instance.
     *
     * @param annotationType The subclass type of the attribute.
     * @param nameIndex Index pointing to the name of the attribute.
     * @param length Content length in bytes.
     * @param typeAnnotationTable The actual type annotations.
     * @param constantPool Array of constants.
     * @param isRuntimeVisible whether the annotations are visible at runtime.
     */
    public TypeAnnotations(final byte annotationType, final int nameIndex, final int length, final TypeAnnotationEntry[] typeAnnotationTable,
            final ConstantPool constantPool, final boolean isRuntimeVisible) {
        super(annotationType, nameIndex, length, constantPool);
        setTypeAnnotationTable(typeAnnotationTable);
        this.isRuntimeVisible = isRuntimeVisible;
    }

    /**
     * Constructs an instance.
     *
     * @param annotationType The subclass type of the attribute.
     * @param nameIndex Index pointing to the name of the attribute.
     * @param length Content length in bytes.
     * @param input Input stream.
     * @param constantPool Array of constants.
     * @param isRuntimeVisible whether the annotations are visible at runtime.
     * @throws IOException Thrown if an I/O error occurs.
     */
    TypeAnnotations(final byte annotationType, final int nameIndex, final int length, final DataInput input, final ConstantPool constantPool,
            final boolean isRuntimeVisible) throws IOException {
        this(annotationType, nameIndex, length, (TypeAnnotationEntry[]) null, constantPool, isRuntimeVisible);
        final int tableLength = input.readUnsignedShort();
        typeAnnotationTable = new TypeAnnotationEntry[tableLength];
        for (int i = 0; i < tableLength; i++) {
            typeAnnotationTable[i] = TypeAnnotationEntry.read(input, constantPool, isRuntimeVisible);
        }
    }

    @Override
    public void accept(final Visitor v) {
        v.visitTypeAnnotations(this);
    }

    /**
     * Creates deep copies of the type annotations, which refer to the given constant pool.
     *
     * @param constantPool The constant pool of the copies.
     * @return The copies.
     */
    protected final TypeAnnotationEntry[] copyTypeAnnotations(final ConstantPool constantPool) {
        return Stream.of(typeAnnotationTable).map(e -> e.copy(constantPool)).toArray(TypeAnnotationEntry[]::new);
    }

    /**
     * Gets the number of type annotations.
     *
     * @return The number of type annotations.
     */
    public final int getNumTypeAnnotations() {
        return typeAnnotationTable.length;
    }

    /**
     * Gets the type annotations.
     *
     * @return The type annotations.
     */
    public TypeAnnotationEntry[] getTypeAnnotationEntries() {
        return typeAnnotationTable;
    }

    /**
     * Tests whether the annotations are visible at runtime.
     *
     * @return true if the annotations are runtime visible.
     */
    public boolean isRuntimeVisible() {
        return isRuntimeVisible;
    }

    @Override
    public Iterator<TypeAnnotationEntry> iterator() {
        return Stream.of(typeAnnotationTable).iterator();
    }

    /**
     * Sets the type annotations.
     *
     * @param typeAnnotationTable The type annotations.
     */
    public final void setTypeAnnotationTable(final TypeAnnotationEntry[] typeAnnotationTable) {
        this.typeAnnotationTable = typeAnnotationTable != null ? typeAnnotationTable : TypeAnnotationEntry.EMPTY_ARRAY;
    }

    /**
     * Converts to a String representation.
     *
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder(Const.getAttributeName(getTag()));
        buf.append(":\n");
        for (int i = 0; i < typeAnnotationTable.length; i++) {
            buf.append("  ").append(typeAnnotationTable[i]);
            if (i < typeAnnotationTable.length - 1) {
                buf.append('\n');
            }
        }
        return buf.toString();
    }

    /**
     * Writes the type annotations to a DataOutputStream.
     *
     * @param dos The data output stream.
     * @throws IOException Thrown if an I/O error occurs.
     */
    protected void writeTypeAnnotations(final DataOutputStream dos) throws IOException {
        dos.writeShort(Args.requireU2(typeAnnotationTable.length, "typeAnnotationTable.length"));
        for (final TypeAnnotationEntry element : typeAnnotationTable) {
            element.dump(dos);
        }
    }
}
