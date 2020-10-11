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
package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;

/**
 * Super class for FieldGen and MethodGen objects, since they have
 * some methods in common!
 *
 */
public abstract class FieldGenOrMethodGen extends AccessFlags implements NamedAndTyped, Cloneable {

    /**
     * @deprecated (since 6.0) will be made private; do not access directly, use getter/setter
     */
    @Deprecated
    protected String name;

    /**
     * @deprecated (since 6.0) will be made private; do not access directly, use getter/setter
     */
    @Deprecated
    protected Type type;

    /**
     * @deprecated (since 6.0) will be made private; do not access directly, use getter/setter
     */
    @Deprecated
    protected ConstantPoolGen cp;

    private final List<Attribute> attributeList = new ArrayList<>();

    // @since 6.0
    private final List<AnnotationEntryGen>       annotationList= new ArrayList<>();


    protected FieldGenOrMethodGen() {
    }


    /**
     * @since 6.0
     */
    protected FieldGenOrMethodGen(final int access_flags) { // TODO could this be package protected?
        super(access_flags);
    }

    @Override
    public void setType( final Type type ) { // TODO could be package-protected?
        if (type.getType() == Const.T_ADDRESS) {
            throw new IllegalArgumentException("Type can not be " + type);
        }
        this.type = type;
    }


    @Override
    public Type getType() {
        return type;
    }


    /** @return name of method/field.
     */
    @Override
    public String getName() {
        return name;
    }


    @Override
    public void setName( final String name ) { // TODO could be package-protected?
        this.name = name;
    }


    public ConstantPoolGen getConstantPool() {
        return cp;
    }


    public void setConstantPool( final ConstantPoolGen cp ) { // TODO could be package-protected?
        this.cp = cp;
    }


    /**
     * Add an attribute to this method. Currently, the JVM knows about
     * the `Code', `ConstantValue', `Synthetic' and `Exceptions'
     * attributes. Other attributes will be ignored by the JVM but do no
     * harm.
     *
     * @param a attribute to be added
     */
    public void addAttribute( final Attribute a ) {
        attributeList.add(a);
    }

    /**
     * @since 6.0
     */
    public void addAnnotationEntry(final AnnotationEntryGen ag)
    {
        annotationList.add(ag);
    }


    /**
     * Remove an attribute.
     */
    public void removeAttribute( final Attribute a ) {
        attributeList.remove(a);
    }

    /**
     * @since 6.0
     */
    public void removeAnnotationEntry(final AnnotationEntryGen ag)
    {
        annotationList.remove(ag);
    }


    /**
     * Remove all attributes.
     */
    public void removeAttributes() {
        attributeList.clear();
    }

    /**
     * @since 6.0
     */
    public void removeAnnotationEntries()
    {
        annotationList.clear();
    }


    /**
     * @return all attributes of this method.
     */
    public Attribute[] getAttributes() {
        final Attribute[] attributes = new Attribute[attributeList.size()];
        attributeList.toArray(attributes);
        return attributes;
    }

    public AnnotationEntryGen[] getAnnotationEntries() {
        final AnnotationEntryGen[] annotations = new AnnotationEntryGen[annotationList.size()];
          annotationList.toArray(annotations);
          return annotations;
      }


    /** @return signature of method/field.
     */
    public abstract String getSignature();


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new Error("Clone Not Supported"); // never happens
        }
    }
}
