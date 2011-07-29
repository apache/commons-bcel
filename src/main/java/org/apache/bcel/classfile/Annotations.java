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
package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * base class for annotations
 * 
 * @version $Id: Annotations
 * @author <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 5.3
 */
public abstract class Annotations extends Attribute {

    private static final long serialVersionUID = 1L;

    private AnnotationEntry[] annotation_table;
    private final boolean isRuntimeVisible;

    /**
     * @param annotation_type the subclass type of the annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param file Input stream
     * @param constant_pool Array of constants
     */
    public Annotations(byte annotation_type, int name_index, int length, DataInputStream file, ConstantPool constant_pool, boolean isRuntimeVisible) throws IOException {
        this(annotation_type, name_index, length, (AnnotationEntry[]) null, constant_pool, isRuntimeVisible);
        final int annotation_table_length = (file.readUnsignedShort());
        annotation_table = new AnnotationEntry[annotation_table_length];
        for (int i = 0; i < annotation_table_length; i++) {
            annotation_table[i] = AnnotationEntry.read(file, constant_pool, isRuntimeVisible);
        }
    }

    /**
     * @param annotation_type the subclass type of the annotation
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param annotation_table the actual annotations
     * @param constant_pool Array of constants
     */
    public Annotations(byte annotation_type, int name_index, int length, AnnotationEntry[] annotation_table, ConstantPool constant_pool, boolean isRuntimeVisible) {
        super(annotation_type, name_index, length, constant_pool);
        setAnnotationTable(annotation_table);
        this.isRuntimeVisible = isRuntimeVisible;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely defined by the contents of a Java class.
     * I.e., the hierarchy of methods, fields, attributes, etc. spawns a tree of objects.
     * 
     * @param v Visitor object
     */
    @Override
    public void accept(Visitor v) {
        v.visitAnnotation(this);
    }

    /**
     * @param annotation_table the entries to set in this annotation
     */
    public final void setAnnotationTable(AnnotationEntry[] annotation_table) {
        this.annotation_table = annotation_table;
    }

    /**
     * returns the array of annotation entries in this annotation
     */
    public AnnotationEntry[] getAnnotationEntries() {
        return annotation_table;
    }

    /**
     * @return the number of annotation entries in this annotation
     */
    public final int getNumAnnotations() {
        if (annotation_table == null) {
            return 0;
        }
        return annotation_table.length;
    }

    public boolean isRuntimeVisible() {
        return isRuntimeVisible;
    }

    protected void writeAnnotations(DataOutputStream dos) throws IOException {
        if (annotation_table == null) {
            return;
        }
        dos.writeShort(annotation_table.length);
        for (int i = 0; i < annotation_table.length; i++) {
            annotation_table[i].dump(dos);
        }
    }
}
