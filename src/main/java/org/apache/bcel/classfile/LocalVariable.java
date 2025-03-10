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

import org.apache.bcel.Constants;
import org.apache.bcel.util.Args;

/**
 * This class represents a local variable within a method. It contains its scope, name, signature and index on the
 * method's frame. It is used both to represent an element of the LocalVariableTable as well as an element of the
 * LocalVariableTypeTable. The nomenclature used here may be a bit confusing; while the two items have the same layout
 * in a class file, a LocalVariableTable attribute contains a descriptor_index, not a signatureIndex. The
 * LocalVariableTypeTable attribute does have a signatureIndex.
 *
 * @see org.apache.bcel.classfile.Utility for more details on the difference.
 * @see LocalVariableTable
 * @see LocalVariableTypeTable
 */
public final class LocalVariable implements Cloneable, Node, Constants {

    static final LocalVariable[] EMPTY_ARRAY = {};

    /** Range in which the variable is valid. */
    private int startPc;

    private int length;

    /** Index in constant pool of variable name. */
    private int nameIndex;

    /**
     * Technically, a decscriptor_index for a local variable table entry and a signatureIndex for a local variable type table entry. Index of variable signature
     */
    private int signatureIndex;

    /*
     * Variable is index'th local variable on this method's frame.
     */
    private int index;

    private ConstantPool constantPool;

    /** Never changes; used to match up with LocalVariableTypeTable entries. */
    private final int origIndex;

    /**
     * Constructs object from file stream.
     *
     * @param file Input stream
     * @throws IOException if an I/O error occurs.
     */
    LocalVariable(final DataInput file, final ConstantPool constantPool) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), constantPool);
    }

    /**
     * @param startPc Range in which the variable
     * @param length ... is valid
     * @param nameIndex Index in constant pool of variable name
     * @param signatureIndex Index of variable's signature
     * @param index Variable is 'index'th local variable on the method's frame
     * @param constantPool Array of constants
     */
    public LocalVariable(final int startPc, final int length, final int nameIndex, final int signatureIndex, final int index, final ConstantPool constantPool) {
        this(startPc, length, nameIndex, signatureIndex, index, constantPool, index);
    }

    /**
     * @param startPc Range in which the variable
     * @param length ... is valid
     * @param nameIndex Index in constant pool of variable name
     * @param signatureIndex Index of variable's signature
     * @param index Variable is 'index'th local variable on the method's frame
     * @param constantPool Array of constants
     * @param origIndex Variable is 'index'th local variable on the method's frame prior to any changes
     */
    public LocalVariable(final int startPc, final int length, final int nameIndex, final int signatureIndex, final int index, final ConstantPool constantPool,
        final int origIndex) {
        this.startPc = Args.requireU2(startPc, "startPc");
        this.length = Args.requireU2(length, "length");
        this.nameIndex = Args.requireU2(nameIndex, "nameIndex");
        this.signatureIndex = Args.requireU2(signatureIndex, "signatureIndex");
        this.index = Args.requireU2(index, "index");
        this.origIndex = Args.requireU2(origIndex, "origIndex");
        this.constantPool = constantPool;
    }

    /**
     * Initializes from another LocalVariable. Note that both objects use the same references (shallow copy). Use copy() for
     * a physical copy.
     *
     * @param localVariable Another LocalVariable.
     */
    public LocalVariable(final LocalVariable localVariable) {
        this(localVariable.getStartPC(), localVariable.getLength(), localVariable.getNameIndex(), localVariable.getSignatureIndex(), localVariable.getIndex(),
            localVariable.getConstantPool());
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitly defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitLocalVariable(this);
    }

    /**
     * @return deep copy of this object
     */
    public LocalVariable copy() {
        try {
            return (LocalVariable) clone();
        } catch (final CloneNotSupportedException e) {
            // TODO should this throw?
        }
        return null;
    }

    /**
     * Dumps local variable to file stream in binary format.
     *
     * @param dataOutputStream Output file stream
     * @throws IOException if an I/O error occurs.
     * @see java.io.FilterOutputStream#out
     */
    public void dump(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(startPc);
        dataOutputStream.writeShort(length);
        dataOutputStream.writeShort(nameIndex);
        dataOutputStream.writeShort(signatureIndex);
        dataOutputStream.writeShort(index);
    }

    /**
     * @return Constant pool used by this object.
     */
    public ConstantPool getConstantPool() {
        return constantPool;
    }

    /**
     * @return index of register where variable is stored
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return Variable is valid within getStartPC() .. getStartPC()+getLength()
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Variable name.
     */
    public String getName() {
        return constantPool.getConstantUtf8(nameIndex).getBytes();
    }

    /**
     * @return Index in constant pool of variable name.
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * @return index of register where variable was originally stored
     */
    public int getOrigIndex() {
        return origIndex;
    }

    /**
     * @return Signature.
     */
    public String getSignature() {
        return constantPool.getConstantUtf8(signatureIndex).getBytes();
    }

    /**
     * @return Index in constant pool of variable signature.
     */
    public int getSignatureIndex() {
        return signatureIndex;
    }

    /**
     * @return Start of range where the variable is valid
     */
    public int getStartPC() {
        return startPc;
    }

    /**
     * @param constantPool Constant pool to be used for this object.
     */
    public void setConstantPool(final ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    /**
     * @param index the index in the local variable table of this variable
     */
    public void setIndex(final int index) { // TODO unused
        this.index = index;
    }

    /**
     * @param length the length of this local variable
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @param nameIndex the index into the constant pool for the name of this variable
     */
    public void setNameIndex(final int nameIndex) { // TODO unused
        this.nameIndex = nameIndex;
    }

    /**
     * @param signatureIndex the index into the constant pool for the signature of this variable
     */
    public void setSignatureIndex(final int signatureIndex) { // TODO unused
        this.signatureIndex = signatureIndex;
    }

    /**
     * @param startPc Specify range where the local variable is valid.
     */
    public void setStartPC(final int startPc) { // TODO unused
        this.startPc = startPc;
    }

    /**
     * @return string representation.
     */
    @Override
    public String toString() {
        return toStringShared(false);
    }

    /*
     * Helper method shared with LocalVariableTypeTable
     */
    String toStringShared(final boolean typeTable) {
        final String name = getName();
        final String signature = Utility.signatureToString(getSignature(), false);
        final String label = "LocalVariable" + (typeTable ? "Types" : "");
        return label + "(startPc = " + startPc + ", length = " + length + ", index = " + index + ":" + signature + " " + name + ")";
    }
}
