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
package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantObject;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.BCELComparator;

/**
 * Template class for building up a field. The only extraordinary thing one can do is to add a constant value attribute
 * to a field (which must of course be compatible with to the declared type).
 *
 * @see Field
 */
public class FieldGen extends FieldGenOrMethodGen {

    private static BCELComparator<FieldGen> bcelComparator = new BCELComparator<FieldGen>() {

        @Override
        public boolean equals(final FieldGen a, final FieldGen b) {
            return a == b || a != null && b != null && Objects.equals(a.getName(), b.getName()) && Objects.equals(a.getSignature(), b.getSignature());
        }

        @Override
        public int hashCode(final FieldGen o) {
            return o != null ? Objects.hash(o.getSignature(), o.getName()) : 0;
        }
    };

    /**
     * @return Comparison strategy object.
     */
    public static BCELComparator<FieldGen> getComparator() {
        return bcelComparator;
    }

    /**
     * @param comparator Comparison strategy object.
     */
    public static void setComparator(final BCELComparator<FieldGen> comparator) {
        bcelComparator = comparator;
    }

    private Object value;

    private List<FieldObserver> observers;

    /**
     * Instantiate from existing field.
     *
     * @param field Field object.
     * @param cp constant pool (must contain the same entries as the field's constant pool).
     */
    public FieldGen(final Field field, final ConstantPoolGen cp) {
        this(field.getAccessFlags(), Type.getType(field.getSignature()), field.getName(), cp);
        final Attribute[] attrs = field.getAttributes();
        for (final Attribute attr : attrs) {
            if (attr instanceof ConstantValue) {
                setValue(((ConstantValue) attr).getConstantValueIndex());
            } else if (attr instanceof Annotations) {
                final Annotations runtimeAnnotations = (Annotations) attr;
                runtimeAnnotations.forEach(element -> addAnnotationEntry(new AnnotationEntryGen(element, cp, false)));
            } else {
                addAttribute(attr);
            }
        }
    }

    /**
     * Declare a field. If it is static (isStatic() == true) and has a basic type like int or String it may have an initial
     * value associated with it as defined by setInitValue().
     *
     * @param accessFlags access qualifiers
     * @param type field type
     * @param name field name
     * @param cp constant pool
     */
    public FieldGen(final int accessFlags, final Type type, final String name, final ConstantPoolGen cp) {
        super(accessFlags);
        setType(type);
        setName(name);
        setConstantPool(cp);
    }

    private void addAnnotationsAsAttribute(final ConstantPoolGen cp) {
        Stream.of(AnnotationEntryGen.getAnnotationAttributes(cp, super.getAnnotationEntries())).forEach(this::addAttribute);
    }

    private int addConstant() {
        switch (super.getType().getType()) { // sic
        case Const.T_INT:
        case Const.T_CHAR:
        case Const.T_BYTE:
        case Const.T_BOOLEAN:
        case Const.T_SHORT:
            return super.getConstantPool().addInteger(((Integer) value).intValue());
        case Const.T_FLOAT:
            return super.getConstantPool().addFloat(((Float) value).floatValue());
        case Const.T_DOUBLE:
            return super.getConstantPool().addDouble(((Double) value).doubleValue());
        case Const.T_LONG:
            return super.getConstantPool().addLong(((Long) value).longValue());
        case Const.T_REFERENCE:
            return super.getConstantPool().addString((String) value);
        default:
            throw new IllegalStateException("Unhandled : " + super.getType().getType()); // sic
        }
    }

    /**
     * Add observer for this object.
     */
    public void addObserver(final FieldObserver o) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(o);
    }

    /**
     * Remove any initial value.
     */
    public void cancelInitValue() {
        value = null;
    }

    private void checkType(final Type atype) {
        final Type superType = super.getType();
        if (superType == null) {
            throw new ClassGenException("You haven't defined the type of the field yet");
        }
        if (!isFinal()) {
            throw new ClassGenException("Only final fields may have an initial value!");
        }
        if (!superType.equals(atype)) {
            throw new ClassGenException("Types are not compatible: " + superType + " vs. " + atype);
        }
    }

    /**
     * @return deep copy of this field
     */
    public FieldGen copy(final ConstantPoolGen cp) {
        final FieldGen fg = (FieldGen) clone();
        fg.setConstantPool(cp);
        return fg;
    }

    /**
     * Return value as defined by given BCELComparator strategy. By default two FieldGen objects are said to be equal when
     * their names and signatures are equal.
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof FieldGen && bcelComparator.equals(this, (FieldGen) obj);
    }

    /**
     * Gets field object after having set up all necessary values.
     */
    public Field getField() {
        final String signature = getSignature();
        final int nameIndex = super.getConstantPool().addUtf8(super.getName());
        final int signatureIndex = super.getConstantPool().addUtf8(signature);
        if (value != null) {
            checkType(super.getType());
            final int index = addConstant();
            addAttribute(new ConstantValue(super.getConstantPool().addUtf8("ConstantValue"), 2, index, super.getConstantPool().getConstantPool())); // sic
        }
        addAnnotationsAsAttribute(super.getConstantPool());
        return new Field(super.getAccessFlags(), nameIndex, signatureIndex, getAttributes(), super.getConstantPool().getConstantPool()); // sic
    }

    public String getInitValue() {
        return Objects.toString(value, null);
    }

    @Override
    public String getSignature() {
        return super.getType().getSignature();
    }

    /**
     * Return value as defined by given BCELComparator strategy. By default return the hash code of the field's name XOR
     * signature.
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return bcelComparator.hashCode(this);
    }

    /**
     * Remove observer for this object.
     */
    public void removeObserver(final FieldObserver o) {
        if (observers != null) {
            observers.remove(o);
        }
    }

    public void setInitValue(final boolean b) {
        checkType(Type.BOOLEAN);
        if (b) {
            value = Integer.valueOf(1);
        }
    }

    public void setInitValue(final byte b) {
        checkType(Type.BYTE);
        if (b != 0) {
            value = Integer.valueOf(b);
        }
    }

    public void setInitValue(final char c) {
        checkType(Type.CHAR);
        if (c != 0) {
            value = Integer.valueOf(c);
        }
    }

    public void setInitValue(final double d) {
        checkType(Type.DOUBLE);
        if (d != 0.0) {
            value = Double.valueOf(d);
        }
    }

    public void setInitValue(final float f) {
        checkType(Type.FLOAT);
        if (f != 0.0) {
            value = Float.valueOf(f);
        }
    }

    public void setInitValue(final int i) {
        checkType(Type.INT);
        if (i != 0) {
            value = Integer.valueOf(i);
        }
    }

    public void setInitValue(final long l) {
        checkType(Type.LONG);
        if (l != 0L) {
            value = Long.valueOf(l);
        }
    }

    public void setInitValue(final short s) {
        checkType(Type.SHORT);
        if (s != 0) {
            value = Integer.valueOf(s);
        }
    }

    /**
     * Sets (optional) initial value of field, otherwise it will be set to null/0/false by the JVM automatically.
     */
    public void setInitValue(final String str) {
        checkType(ObjectType.getInstance("java.lang.String"));
        if (str != null) {
            value = str;
        }
    }

    private void setValue(final int index) {
        final ConstantPool cp = super.getConstantPool().getConstantPool();
        final Constant c = cp.getConstant(index);
        value = ((ConstantObject) c).getConstantValue(cp);
    }

    /**
     * Return string representation close to declaration format, for example: 'public static final short MAX = 100'.
     *
     * @return String representation of field
     */
    @Override
    public final String toString() {
        // Short cuts to constant pool
        String access = Utility.accessToString(super.getAccessFlags());
        access = access.isEmpty() ? "" : access + " ";
        final String signature = super.getType().toString();
        final String name = getName();
        final StringBuilder buf = new StringBuilder(32); // CHECKSTYLE IGNORE MagicNumber
        buf.append(access).append(signature).append(" ").append(name);
        final String value = getInitValue();
        if (value != null) {
            buf.append(" = ").append(value);
        }
        return buf.toString();
    }

    /**
     * Call notify() method on all observers. This method is not called automatically whenever the state has changed, but
     * has to be called by the user after they have finished editing the object.
     */
    public void update() {
        if (observers != null) {
            for (final FieldObserver observer : observers) {
                observer.notify(this);
            }
        }
    }
}
