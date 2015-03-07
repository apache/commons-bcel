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

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.verifier.structurals.LocalVariables;
import org.apache.bcel.verifier.structurals.OperandStack;

/**
 * Abstract super class for all possible java types, namely basic types
 * such as int, object types like String and array types, e.g. int[]
 *
 * @version $Id$
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public abstract class Type implements java.io.Serializable {

    private static final long serialVersionUID = -1985077286871826895L;
    protected byte type;
    protected String signature; // signature for the type
    /** Predefined constants
     */
    public static final BasicType VOID = new BasicType(Constants.T_VOID);
    public static final BasicType BOOLEAN = new BasicType(Constants.T_BOOLEAN);
    public static final BasicType INT = new BasicType(Constants.T_INT);
    public static final BasicType SHORT = new BasicType(Constants.T_SHORT);
    public static final BasicType BYTE = new BasicType(Constants.T_BYTE);
    public static final BasicType LONG = new BasicType(Constants.T_LONG);
    public static final BasicType DOUBLE = new BasicType(Constants.T_DOUBLE);
    public static final BasicType FLOAT = new BasicType(Constants.T_FLOAT);
    public static final BasicType CHAR = new BasicType(Constants.T_CHAR);
    public static final ObjectType OBJECT = ObjectType.getInstance("java.lang.Object");
    public static final ObjectType CLASS = ObjectType.getInstance("java.lang.Class");
    public static final ObjectType STRING = ObjectType.getInstance("java.lang.String");
    public static final ObjectType STRINGBUFFER = ObjectType.getInstance("java.lang.StringBuffer");
    public static final ObjectType THROWABLE = ObjectType.getInstance("java.lang.Throwable");
    public static final Type[] NO_ARGS = new Type[0];
    public static final ReferenceType NULL = new ReferenceType() {

        private static final long serialVersionUID = 4526765862386946282L;
    };
    public static final Type UNKNOWN = new Type(Constants.T_UNKNOWN, "<unknown object>") {

        private static final long serialVersionUID = 1321113605813486066L;
    };


    protected Type(byte t, String s) {
        type = t;
        signature = s;
    }


    /**
     * @return hashcode of Type
     */
    @Override
    public int hashCode() {
        return type ^ signature.hashCode();
    }


    /**
     * @return whether the Types are equal
     */
    @Override
    public boolean equals(Object o) {
          if (o instanceof Type) {
              Type t = (Type)o;
              return (type == t.type) && signature.equals(t.signature);
          }
          return false;
    }


    /**
     * @return signature for given type.
     */
    public String getSignature() {
        return signature;
    }


    /**
     * @return type as defined in Constants
     */
    public byte getType() {
        return type;
    }

    /**
     * boolean, short and char variable are considered as int in the stack or local variable area.
     * Returns {@link Type#INT} for {@link Type#BOOLEAN}, {@link Type#SHORT} or {@link Type#CHAR}, otherwise
     * returns the given type.
     * @see OperandStack#push(Type)
     * @see LocalVariables#set(int, Type)
     */
    public Type normalizeForStackOrLocal(){
        if (this == Type.BOOLEAN || this == Type.BYTE || this == Type.SHORT || this == Type.CHAR){
            return Type.INT;
        }
        return this;
    }

    /**
     * @return stack size of this type (2 for long and double, 0 for void, 1 otherwise)
     */
    public int getSize() {
        switch (type) {
            case Constants.T_DOUBLE:
            case Constants.T_LONG:
                return 2;
            case Constants.T_VOID:
                return 0;
            default:
                return 1;
        }
    }


    /**
     * @return Type string, e.g. `int[]'
     */
    @Override
    public String toString() {
        return ((this.equals(Type.NULL) || (type >= Constants.T_UNKNOWN))) ? signature : Utility
                .signatureToString(signature, false);
    }


    /**
     * Convert type to Java method signature, e.g. int[] f(java.lang.String x)
     * becomes (Ljava/lang/String;)[I
     *
     * @param return_type what the method returns
     * @param arg_types what are the argument types
     * @return method signature for given type(s).
     */
    public static String getMethodSignature( Type return_type, Type[] arg_types ) {
        StringBuilder buf = new StringBuilder("(");
        if (arg_types != null) {
            for (Type arg_type : arg_types) {
                buf.append(arg_type.getSignature());
            }
        }
        buf.append(')');
        buf.append(return_type.getSignature());
        return buf.toString();
    }

    private static final ThreadLocal<Integer> consumed_chars = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return Integer.valueOf(0);
        }
    };//int consumed_chars=0; // Remember position in string, see getArgumentTypes


    private static int unwrap( ThreadLocal<Integer> tl ) {
        return tl.get().intValue();
    }


    private static void wrap( ThreadLocal<Integer> tl, int value ) {
        tl.set(Integer.valueOf(value));
    }


    /**
     * Convert signature to a Type object.
     * @param signature signature string such as Ljava/lang/String;
     * @return type object
     */
    public static Type getType( String signature ) throws StringIndexOutOfBoundsException {
        byte type = Utility.typeOfSignature(signature);
        if (type <= Constants.T_VOID) {
            //corrected concurrent private static field acess
            wrap(consumed_chars, 1);
            return BasicType.getType(type);
        } else if (type == Constants.T_ARRAY) {
            int dim = 0;
            do { // Count dimensions
                dim++;
            } while (signature.charAt(dim) == '[');
            // Recurse, but just once, if the signature is ok
            Type t = getType(signature.substring(dim));
            //corrected concurrent private static field acess
            //  consumed_chars += dim; // update counter - is replaced by
            int _temp = unwrap(consumed_chars) + dim;
            wrap(consumed_chars, _temp);
            return new ArrayType(t, dim);
        } else { // type == T_REFERENCE
            int index = signature.indexOf(';'); // Look for closing `;'
            if (index < 0) {
                throw new ClassFormatException("Invalid signature: " + signature);
            }
            //corrected concurrent private static field acess
            wrap(consumed_chars, index + 1); // "Lblabla;" `L' and `;' are removed
            return ObjectType.getInstance(signature.substring(1, index).replace('/', '.'));
        }
    }


    /**
     * Convert return value of a method (signature) to a Type object.
     *
     * @param signature signature string such as (Ljava/lang/String;)V
     * @return return type
     */
    public static Type getReturnType( String signature ) {
        try {
            // Read return type after `)'
            int index = signature.lastIndexOf(')') + 1;
            return getType(signature.substring(index));
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
    }


    /**
     * Convert arguments of a method (signature) to an array of Type objects.
     * @param signature signature string such as (Ljava/lang/String;)V
     * @return array of argument types
     */
    public static Type[] getArgumentTypes( String signature ) {
        List<Type> vec = new ArrayList<Type>();
        int index;
        Type[] types;
        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            index = 1; // current string position
            while (signature.charAt(index) != ')') {
                vec.add(getType(signature.substring(index)));
                //corrected concurrent private static field acess
                index += unwrap(consumed_chars); // update position
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
        types = new Type[vec.size()];
        vec.toArray(types);
        return types;
    }


    /** Convert runtime java.lang.Class to BCEL Type object.
     * @param cl Java class
     * @return corresponding Type object
     */
    public static Type getType( java.lang.Class<?> cl ) {
        if (cl == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        /* That's an amzingly easy case, because getName() returns
         * the signature. That's what we would have liked anyway.
         */
        if (cl.isArray()) {
            return getType(cl.getName());
        } else if (cl.isPrimitive()) {
            if (cl == Integer.TYPE) {
                return INT;
            } else if (cl == Void.TYPE) {
                return VOID;
            } else if (cl == Double.TYPE) {
                return DOUBLE;
            } else if (cl == Float.TYPE) {
                return FLOAT;
            } else if (cl == Boolean.TYPE) {
                return BOOLEAN;
            } else if (cl == Byte.TYPE) {
                return BYTE;
            } else if (cl == Short.TYPE) {
                return SHORT;
            } else if (cl == Byte.TYPE) {
                return BYTE;
            } else if (cl == Long.TYPE) {
                return LONG;
            } else if (cl == Character.TYPE) {
                return CHAR;
            } else {
                throw new IllegalStateException("Ooops, what primitive type is " + cl);
            }
        } else { // "Real" class
            return ObjectType.getInstance(cl.getName());
        }
    }


    /**
     * Convert runtime java.lang.Class[] to BCEL Type objects.
     * @param classes an array of runtime class objects
     * @return array of corresponding Type objects
     */
    public static Type[] getTypes( java.lang.Class<?>[] classes ) {
        Type[] ret = new Type[classes.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getType(classes[i]);
        }
        return ret;
    }


    public static String getSignature( java.lang.reflect.Method meth ) {
        StringBuilder sb = new StringBuilder("(");
        Class<?>[] params = meth.getParameterTypes(); // avoid clone
        for (Class<?> param : params) {
            sb.append(getType(param).getSignature());
        }
        sb.append(")");
        sb.append(getType(meth.getReturnType()).getSignature());
        return sb.toString();
    }

    static int size(int coded) {
        return coded & 3;
    }

    static int consumed(int coded) {
        return coded >> 2;
    }

    static int encode(int size, int consumed) {
        return consumed << 2 | size;
    }

    static int getArgumentTypesSize( String signature ) {
        int res = 0;
        int index;
        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            index = 1; // current string position
            while (signature.charAt(index) != ')') {
                int coded = getTypeSize(signature.substring(index));
                res += size(coded);
                index += consumed(coded);
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid method signature: " + signature, e);
        }
        return res;
    }

    static int getTypeSize( String signature ) throws StringIndexOutOfBoundsException {
        byte type = Utility.typeOfSignature(signature);
        if (type <= Constants.T_VOID) {
            return encode(BasicType.getType(type).getSize(), 1);
        } else if (type == Constants.T_ARRAY) {
            int dim = 0;
            do { // Count dimensions
                dim++;
            } while (signature.charAt(dim) == '[');
            // Recurse, but just once, if the signature is ok
            int consumed = consumed(getTypeSize(signature.substring(dim)));
            return encode(1, dim + consumed);
        } else { // type == T_REFERENCE
            int index = signature.indexOf(';'); // Look for closing `;'
            if (index < 0) {
                throw new ClassFormatException("Invalid signature: " + signature);
            }
            return encode(1, index + 1);
        }
    }


    static int getReturnTypeSize(String signature) {
        int index = signature.lastIndexOf(')') + 1;
        return Type.size(getTypeSize(signature.substring(index)));
    }
}
