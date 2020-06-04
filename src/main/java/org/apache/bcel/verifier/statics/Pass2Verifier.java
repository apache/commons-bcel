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
package org.apache.bcel.verifier.statics;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.Const;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.ClassConstraintException;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

/**
 * This PassVerifier verifies a class file according to
 * pass 2 as described in The Java Virtual Machine
 * Specification, 2nd edition.
 * More detailed information is to be found at the do_verify()
 * method's documentation.
 *
 * @see #do_verify()
 */
public final class Pass2Verifier extends PassVerifier implements Constants {

    /**
     * The LocalVariableInfo instances used by Pass3bVerifier.
     * localVariablesInfos[i] denotes the information for the
     * local variables of method number i in the
     * JavaClass this verifier operates on.
     */
    private LocalVariablesInfo[] localVariablesInfos;

    /** The Verifier that created this. */
    private final Verifier myOwner;

    /**
     * Should only be instantiated by a Verifier.
     *
     * @see Verifier
     */
    public Pass2Verifier(final Verifier owner) {
        myOwner = owner;
    }

    /**
     * Returns a LocalVariablesInfo object containing information
     * about the usage of the local variables in the Code attribute
     * of the said method or <B>null</B> if the class file this
     * Pass2Verifier operates on could not be pass-2-verified correctly.
     * The method number method_nr is the method you get using
     * <B>Repository.lookupClass(myOwner.getClassname()).getMethods()[method_nr];</B>.
     * You should not add own information. Leave that to JustIce.
     */
    public LocalVariablesInfo getLocalVariablesInfo(final int methodNr) {
        if (this.verify() != VerificationResult.VR_OK) {
            return null; // It's cached, don't worry.
        }
        if (methodNr < 0 || methodNr >= localVariablesInfos.length) {
            throw new AssertionViolatedException("Method number out of range.");
        }
        return localVariablesInfos[methodNr];
    }

    /**
     * Pass 2 is the pass where static properties of the
     * class file are checked without looking into "Code"
     * arrays of methods.
     * This verification pass is usually invoked when
     * a class is resolved; and it may be possible that
     * this verification pass has to load in other classes
     * such as superclasses or implemented interfaces.
     * Therefore, Pass 1 is run on them.<BR>
     * Note that most referenced classes are <B>not</B> loaded
     * in for verification or for an existance check by this
     * pass; only the syntactical correctness of their names
     * and descriptors (a.k.a. signatures) is checked.<BR>
     * Very few checks that conceptually belong here
     * are delayed until pass 3a in JustIce. JustIce does
     * not only check for syntactical correctness but also
     * for semantical sanity - therefore it needs access to
     * the "Code" array of methods in a few cases. Please
     * see the pass 3a documentation, too.
     *
     * @see Pass3aVerifier
     */
    @Override
    public VerificationResult do_verify() {
        try {
        final VerificationResult vr1 = myOwner.doPass1();
        if (vr1.equals(VerificationResult.VR_OK)) {

            // For every method, we could have information about the local variables out of LocalVariableTable attributes of
            // the Code attributes.
            localVariablesInfos = new LocalVariablesInfo[Repository.lookupClass(myOwner.getClassName()).getMethods().length];

            VerificationResult vr = VerificationResult.VR_OK; // default.
            try{
                constant_pool_entries_satisfy_static_constraints();
                field_and_method_refs_are_valid();
                every_class_has_an_accessible_superclass();
                final_methods_are_not_overridden();
            }
            catch (final ClassConstraintException cce) {
                vr = new VerificationResult(VerificationResult.VERIFIED_REJECTED, cce.getMessage());
            }
            return vr;
        }
        return VerificationResult.VR_NOTYET;

        } catch (final ClassNotFoundException e) {
        // FIXME: this might not be the best way to handle missing classes.
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures that every class has a super class and that
     * <B>final</B> classes are not subclassed.
     * This means, the class this Pass2Verifier operates
     * on has proper super classes (transitively) up to
     * java.lang.Object.
     * The reason for really loading (and Pass1-verifying)
     * all of those classes here is that we need them in
     * Pass2 anyway to verify no final methods are overridden
     * (that could be declared anywhere in the ancestor hierarchy).
     *
     * @throws ClassConstraintException otherwise.
     */
    private void every_class_has_an_accessible_superclass() {
        try {
        final Set<String> hs = new HashSet<>(); // save class names to detect circular inheritance
        JavaClass jc = Repository.lookupClass(myOwner.getClassName());
        int supidx = -1;

        while (supidx != 0) {
            supidx = jc.getSuperclassNameIndex();

            if (supidx == 0) {
                if (jc != Repository.lookupClass(Type.OBJECT.getClassName())) {
                    throw new ClassConstraintException("Superclass of '"+jc.getClassName()+
                            "' missing but not "+Type.OBJECT.getClassName()+" itself!");
                }
            }
            else{
                final String supername = jc.getSuperclassName();
                if (! hs.add(supername)) {    // If supername already is in the list
                    throw new ClassConstraintException("Circular superclass hierarchy detected.");
                }
                final Verifier v = VerifierFactory.getVerifier(supername);
                final VerificationResult vr = v.doPass1();

                if (vr != VerificationResult.VR_OK) {
                    throw new ClassConstraintException("Could not load in ancestor class '"+supername+"'.");
                }
                jc = Repository.lookupClass(supername);

                if (jc.isFinal()) {
                    throw new ClassConstraintException("Ancestor class '"+supername+
                            "' has the FINAL access modifier and must therefore not be subclassed.");
                }
            }
        }

        } catch (final ClassNotFoundException e) {
        // FIXME: this might not be the best way to handle missing classes.
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * Ensures that <B>final</B> methods are not overridden.
     * <B>Precondition to run this method:
     * constant_pool_entries_satisfy_static_constraints() and
     * every_class_has_an_accessible_superclass() have to be invoked before
     * (in that order).</B>
     *
     * @throws ClassConstraintException otherwise.
     * @see #constant_pool_entries_satisfy_static_constraints()
     * @see #every_class_has_an_accessible_superclass()
     */
    private void final_methods_are_not_overridden() {
        try {
        final Map<String, String> hashmap = new HashMap<>();
        JavaClass jc = Repository.lookupClass(myOwner.getClassName());

        int supidx = -1;
        while (supidx != 0) {
            supidx = jc.getSuperclassNameIndex();

            final Method[] methods = jc.getMethods();
            for (final Method method : methods) {
                final String nameAndSig = method.getName() + method.getSignature();

                if (hashmap.containsKey(nameAndSig)) {
                    if (method.isFinal()) {
                        if (!(method.isPrivate())) {
                            throw new ClassConstraintException("Method '" + nameAndSig + "' in class '" + hashmap.get(nameAndSig) +
                                "' overrides the final (not-overridable) definition in class '" + jc.getClassName() + "'.");
                        }
                        addMessage("Method '" + nameAndSig + "' in class '" + hashmap.get(nameAndSig) +
                            "' overrides the final (not-overridable) definition in class '" + jc.getClassName() +
                            "'. This is okay, as the original definition was private; however this constraint leverage"+
                            " was introduced by JLS 8.4.6 (not vmspec2) and the behavior of the Sun verifiers.");
                    } else {
                        if (!method.isStatic()) { // static methods don't inherit
                            hashmap.put(nameAndSig, jc.getClassName());
                        }
                    }
                } else {
                    if (!method.isStatic()) { // static methods don't inherit
                        hashmap.put(nameAndSig, jc.getClassName());
                    }
                }
            }

            jc = Repository.lookupClass(jc.getSuperclassName());
            // Well, for OBJECT this returns OBJECT so it works (could return anything but must not throw an Exception).
        }

        } catch (final ClassNotFoundException e) {
        // FIXME: this might not be the best way to handle missing classes.
        throw new AssertionViolatedException("Missing class: " + e, e);
        }

    }

    /**
     * Ensures that the constant pool entries satisfy the static constraints
     * as described in The Java Virtual Machine Specification, 2nd Edition.
     *
     * @throws ClassConstraintException otherwise.
     */
    private void constant_pool_entries_satisfy_static_constraints() {
        try {
        // Most of the consistency is handled internally by BCEL; here
        // we only have to verify if the indices of the constants point
        // to constants of the appropriate type and such.
        final JavaClass jc = Repository.lookupClass(myOwner.getClassName());
        new CPESSC_Visitor(jc); // constructor implicitly traverses jc

        } catch (final ClassNotFoundException e) {
        // FIXME: this might not be the best way to handle missing classes.
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * A Visitor class that ensures the constant pool satisfies the static
     * constraints.
     * The visitXXX() methods throw ClassConstraintException instances otherwise.
     *
     * @see #constant_pool_entries_satisfy_static_constraints()
     */
    private final class CPESSC_Visitor extends org.apache.bcel.classfile.EmptyVisitor{
        private final Class<?> CONST_Class;
        /*
        private Class<?> CONST_Fieldref;
        private Class<?> CONST_Methodref;
        private Class<?> CONST_InterfaceMethodref;
        */
        private final Class<?> CONST_String;
        private final Class<?> CONST_Integer;
        private final Class<?> CONST_Float;
        private final Class<?> CONST_Long;
        private final Class<?> CONST_Double;
        private final Class<?> CONST_NameAndType;
        private final Class<?> CONST_Utf8;

        private final JavaClass jc;
        private final ConstantPool cp; // ==jc.getConstantPool() -- only here to save typing work and computing power.
        private final int cplen; // == cp.getLength() -- to save computing power.
        private final DescendingVisitor carrier;

        private final Set<String> field_names = new HashSet<>();
        private final Set<String> field_names_and_desc = new HashSet<>();
        private final Set<String> method_names_and_desc = new HashSet<>();

        private CPESSC_Visitor(final JavaClass _jc) {
            jc = _jc;
            cp = _jc.getConstantPool();
            cplen = cp.getLength();

            CONST_Class = ConstantClass.class;
            /*
            CONST_Fieldref = ConstantFieldref.class;
            CONST_Methodref = ConstantMethodref.class;
            CONST_InterfaceMethodref = ConstantInterfaceMethodref.class;
            */
            CONST_String = ConstantString.class;
            CONST_Integer = ConstantInteger.class;
            CONST_Float = ConstantFloat.class;
            CONST_Long = ConstantLong.class;
            CONST_Double = ConstantDouble.class;
            CONST_NameAndType = ConstantNameAndType.class;
            CONST_Utf8 = ConstantUtf8.class;

            carrier = new DescendingVisitor(_jc, this);
            carrier.visit();
        }

        private void checkIndex(final Node referrer, final int index, final Class<?> shouldbe) {
            if ((index < 0) || (index >= cplen)) {
                throw new ClassConstraintException("Invalid index '"+index+"' used by '"+tostring(referrer)+"'.");
            }
            final Constant c = cp.getConstant(index);
            if (! shouldbe.isInstance(c)) {
                /* String isnot = shouldbe.toString().substring(shouldbe.toString().lastIndexOf(".")+1); //Cut all before last "." */
                throw new ClassCastException("Illegal constant '"+tostring(c)+"' at index '"+
                    index+"'. '"+tostring(referrer)+"' expects a '"+shouldbe+"'.");
            }
        }
        ///////////////////////////////////////
        // ClassFile structure (vmspec2 4.1) //
        ///////////////////////////////////////
        @Override
        public void visitJavaClass(final JavaClass obj) {
            final Attribute[] atts = obj.getAttributes();
            boolean foundSourceFile = false;
            boolean foundInnerClasses = false;

            // Is there an InnerClass referenced?
            // This is a costly check; existing verifiers don't do it!
            final boolean hasInnerClass = new InnerClassDetector(jc).innerClassReferenced();

            for (final Attribute att : atts) {
                if ((!(att instanceof SourceFile)) &&
                        (!(att instanceof Deprecated)) &&
                        (!(att instanceof InnerClasses)) &&
                        (!(att instanceof Synthetic))) {
                    addMessage("Attribute '" + tostring(att) + "' as an attribute of the ClassFile structure '" +
                        tostring(obj) + "' is unknown and will therefore be ignored.");
                }

                if (att instanceof SourceFile) {
                    if (!foundSourceFile) {
                        foundSourceFile = true;
                    } else {
                        throw new ClassConstraintException("A ClassFile structure (like '" +
                            tostring(obj) + "') may have no more than one SourceFile attribute."); //vmspec2 4.7.7
                    }
                }

                if (att instanceof InnerClasses) {
                    if (!foundInnerClasses) {
                        foundInnerClasses = true;
                    } else {
                        if (hasInnerClass) {
                            throw new ClassConstraintException("A Classfile structure (like '" + tostring(obj) +
                                "') must have exactly one InnerClasses attribute"+
                                " if at least one Inner Class is referenced (which is the case)."+
                                " More than one InnerClasses attribute was found.");
                        }
                    }
                    if (!hasInnerClass) {
                        addMessage("No referenced Inner Class found, but InnerClasses attribute '" + tostring(att) +
                            "' found. Strongly suggest removal of that attribute.");
                    }
                }

            }
            if (hasInnerClass && !foundInnerClasses) {
                //throw new ClassConstraintException("A Classfile structure (like '"+tostring(obj)+
                // "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case)."+
                // " No InnerClasses attribute was found.");
                //vmspec2, page 125 says it would be a constraint: but existing verifiers
                //don't check it and javac doesn't satisfy it when it comes to anonymous
                //inner classes
                addMessage("A Classfile structure (like '"+tostring(obj)+
                    "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case)."+
                        " No InnerClasses attribute was found.");
            }
        }
        /////////////////////////////
        // CONSTANTS (vmspec2 4.4) //
        /////////////////////////////
        @Override
        public void visitConstantClass(final ConstantClass obj) {
            if (obj.getTag() != Const.CONSTANT_Class) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

        }
        @Override
        public void visitConstantFieldref(final ConstantFieldref obj) {
            if (obj.getTag() != Const.CONSTANT_Fieldref) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getClassIndex(), CONST_Class);
            checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
        }
        @Override
        public void visitConstantMethodref(final ConstantMethodref obj) {
            if (obj.getTag() != Const.CONSTANT_Methodref) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getClassIndex(), CONST_Class);
            checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
        }
        @Override
        public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj) {
            if (obj.getTag() != Const.CONSTANT_InterfaceMethodref) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getClassIndex(), CONST_Class);
            checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
        }
        @Override
        public void visitConstantString(final ConstantString obj) {
            if (obj.getTag() != Const.CONSTANT_String) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getStringIndex(), CONST_Utf8);
        }
        @Override
        public void visitConstantInteger(final ConstantInteger obj) {
            if (obj.getTag() != Const.CONSTANT_Integer) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            // no indices to check
        }
        @Override
        public void visitConstantFloat(final ConstantFloat obj) {
            if (obj.getTag() != Const.CONSTANT_Float) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            //no indices to check
        }
        @Override
        public void visitConstantLong(final ConstantLong obj) {
            if (obj.getTag() != Const.CONSTANT_Long) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            //no indices to check
        }
        @Override
        public void visitConstantDouble(final ConstantDouble obj) {
            if (obj.getTag() != Const.CONSTANT_Double) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            //no indices to check
        }
        @Override
        public void visitConstantNameAndType(final ConstantNameAndType obj) {
            if (obj.getTag() != Const.CONSTANT_NameAndType) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
            //checkIndex(obj, obj.getDescriptorIndex(), CONST_Utf8); //inconsistently named in BCEL, see below.
            checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);
        }
        @Override
        public void visitConstantUtf8(final ConstantUtf8 obj) {
            if (obj.getTag() != Const.CONSTANT_Utf8) {
                throw new ClassConstraintException("Wrong constant tag in '"+tostring(obj)+"'.");
            }
            //no indices to check
        }
        //////////////////////////
        // FIELDS (vmspec2 4.5) //
        //////////////////////////
        @Override
        public void visitField(final Field obj) {

            if (jc.isClass()) {
                int maxone=0;
                if (obj.isPrivate()) {
                    maxone++;
                }
                if (obj.isProtected()) {
                    maxone++;
                }
                if (obj.isPublic()) {
                    maxone++;
                }
                if (maxone > 1) {
                    throw new ClassConstraintException("Field '"+tostring(obj)+
                        "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
                }

                if (obj.isFinal() && obj.isVolatile()) {
                    throw new ClassConstraintException("Field '"+tostring(obj)+
                        "' must only have at most one of its ACC_FINAL, ACC_VOLATILE modifiers set.");
                }
            }
            else{ // isInterface!
                if (!obj.isPublic()) {
                    throw new ClassConstraintException("Interface field '"+tostring(obj)+
                        "' must have the ACC_PUBLIC modifier set but hasn't!");
                }
                if (!obj.isStatic()) {
                    throw new ClassConstraintException("Interface field '"+tostring(obj)+
                        "' must have the ACC_STATIC modifier set but hasn't!");
                }
                if (!obj.isFinal()) {
                    throw new ClassConstraintException("Interface field '"+tostring(obj)+
                        "' must have the ACC_FINAL modifier set but hasn't!");
                }
            }

            if ((obj.getAccessFlags() & ~(Const.ACC_PUBLIC|Const.ACC_PRIVATE|Const.ACC_PROTECTED|Const.ACC_STATIC|
                                          Const.ACC_FINAL|Const.ACC_VOLATILE|Const.ACC_TRANSIENT)) > 0) {
                addMessage("Field '"+tostring(obj)+
                    "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED,"+
                        " ACC_STATIC, ACC_FINAL, ACC_VOLATILE, ACC_TRANSIENT set (ignored).");
            }

            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = obj.getName();
            if (! validFieldName(name)) {
                throw new ClassConstraintException("Field '"+tostring(obj)+"' has illegal name '"+obj.getName()+"'.");
            }

            // A descriptor is often named signature in BCEL
            checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);

            final String sig  = ((ConstantUtf8) (cp.getConstant(obj.getSignatureIndex()))).getBytes(); // Field or Method sig.(=descriptor)

            try{
                Type.getType(sig);  /* Don't need the return value */
            }
            catch (final ClassFormatException cfe) {
                throw new ClassConstraintException("Illegal descriptor (==signature) '"+sig+"' used by '"+tostring(obj)+"'.", cfe);
            }

            final String nameanddesc = name+sig;
            if (field_names_and_desc.contains(nameanddesc)) {
                throw new ClassConstraintException("No two fields (like '"+tostring(obj)+
                    "') are allowed have same names and descriptors!");
            }
            if (field_names.contains(name)) {
                addMessage("More than one field of name '"+name+
                    "' detected (but with different type descriptors). This is very unusual.");
            }
            field_names_and_desc.add(nameanddesc);
            field_names.add(name);

            final Attribute[] atts = obj.getAttributes();
            for (final Attribute att : atts) {
                if ((!(att instanceof ConstantValue)) &&
                        (!(att instanceof Synthetic)) &&
                        (!(att instanceof Deprecated))) {
                    addMessage("Attribute '" + tostring(att) + "' as an attribute of Field '" +
                        tostring(obj) + "' is unknown and will therefore be ignored.");
                }
                if (!(att instanceof ConstantValue)) {
                    addMessage("Attribute '" + tostring(att) + "' as an attribute of Field '" + tostring(obj) +
                        "' is not a ConstantValue and is therefore only of use for debuggers and such.");
                }
            }
        }
        ///////////////////////////
        // METHODS (vmspec2 4.6) //
        ///////////////////////////
        @Override
        public void visitMethod(final Method obj) {

            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = obj.getName();
            if (! validMethodName(name, true)) {
                throw new ClassConstraintException("Method '"+tostring(obj)+"' has illegal name '"+name+"'.");
            }

            // A descriptor is often named signature in BCEL
            checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);

            final String sig  = ((ConstantUtf8) (cp.getConstant(obj.getSignatureIndex()))).getBytes(); // Method's signature(=descriptor)

            Type t;
            Type[] ts; // needed below the try block.
            try{
                t  = Type.getReturnType(sig);
                ts = Type.getArgumentTypes(sig);
            }
            catch (final ClassFormatException cfe) {
                throw new ClassConstraintException(
                    "Illegal descriptor (==signature) '"+sig+"' used by Method '"+tostring(obj)+"'.", cfe);
            }

            // Check if referenced objects exist.
            Type act = t;
            if (act instanceof ArrayType) {
                act = ((ArrayType) act).getBasicType();
            }
            if (act instanceof ObjectType) {
                final Verifier v = VerifierFactory.getVerifier( ((ObjectType) act).getClassName() );
                final VerificationResult vr = v.doPass1();
                if (vr != VerificationResult.VR_OK) {
                    throw new ClassConstraintException(
                        "Method '"+tostring(obj)+"' has a return type that does not pass verification pass 1: '"+vr+"'.");
                }
            }

            for (final Type element : ts) {
                act = element;
                if (act instanceof ArrayType) {
                    act = ((ArrayType) act).getBasicType();
                }
                if (act instanceof ObjectType) {
                    final Verifier v = VerifierFactory.getVerifier( ((ObjectType) act).getClassName() );
                    final VerificationResult vr = v.doPass1();
                    if (vr != VerificationResult.VR_OK) {
                        throw new ClassConstraintException(
                            "Method '"+tostring(obj)+"' has an argument type that does not pass verification pass 1: '"+vr+"'.");
                    }
                }
            }

            // Nearly forgot this! Funny return values are allowed, but a non-empty arguments list makes a different method out of it!
            if (name.equals(Const.STATIC_INITIALIZER_NAME) && (ts.length != 0)) {
                throw new ClassConstraintException(
                    "Method '"+tostring(obj)+"' has illegal name '"+name+"'."+
                    " Its name resembles the class or interface initialization method"+
                    " which it isn't because of its arguments (==descriptor).");
            }

            if (jc.isClass()) {
                int maxone=0;
                if (obj.isPrivate()) {
                    maxone++;
                }
                if (obj.isProtected()) {
                    maxone++;
                }
                if (obj.isPublic()) {
                    maxone++;
                }
                if (maxone > 1) {
                    throw new ClassConstraintException("Method '"+tostring(obj)+
                        "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
                }

                if (obj.isAbstract()) {
                    if (obj.isFinal()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_FINAL modifier set.");
                    }
                    if (obj.isNative()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_NATIVE modifier set.");
                    }
                    if (obj.isPrivate()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_PRIVATE modifier set.");
                    }
                    if (obj.isStatic()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_STATIC modifier set.");
                    }
                    if (obj.isStrictfp()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_STRICT modifier set.");
                    }
                    if (obj.isSynchronized()) {
                        throw new ClassConstraintException(
                            "Abstract method '"+tostring(obj)+"' must not have the ACC_SYNCHRONIZED modifier set.");
                    }
                }

                // A specific instance initialization method... (vmspec2,Page 116).
                if (name.equals(Const.CONSTRUCTOR_NAME)) {
                    //..may have at most one of ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC set: is checked above.
                    //..may also have ACC_STRICT set, but none of the other flags in table 4.5 (vmspec2, page 115)
                    if (obj.isStatic() ||
                            obj.isFinal() ||
                            obj.isSynchronized() ||
                            obj.isNative() ||
                            obj.isAbstract()) {
                        throw new ClassConstraintException("Instance initialization method '" + tostring(obj) + "' must not have" +
                            " any of the ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT modifiers set.");
                    }
                }
            }
            else{ // isInterface!
                if (!name.equals(Const.STATIC_INITIALIZER_NAME)) {//vmspec2, p.116, 2nd paragraph
                    if (jc.getMajor() >= Const.MAJOR_1_8) {
                        if (!(obj.isPublic() ^ obj.isPrivate())) {
                            throw new ClassConstraintException("Interface method '" + tostring(obj) + "' must have" +
                                " exactly one of its ACC_PUBLIC and ACC_PRIVATE modifiers set.");
                        }
                        if (obj.isProtected()
                                || obj.isFinal()
                                || obj.isSynchronized()
                                || obj.isNative()) {
                            throw new ClassConstraintException("Interface method '"+tostring(obj)+ "' must not have" +
                                " any of the ACC_PROTECTED, ACC_FINAL, ACC_SYNCHRONIZED, or ACC_NATIVE modifiers set.");
                        }

                    } else {
                        if (!obj.isPublic()) {
                            throw new ClassConstraintException(
                                "Interface method '"+tostring(obj)+"' must have the ACC_PUBLIC modifier set but hasn't!");
                        }
                        if (!obj.isAbstract()) {
                            throw new ClassConstraintException(
                                "Interface method '"+tostring(obj)+"' must have the ACC_ABSTRACT modifier set but hasn't!");
                        }
                        if (obj.isPrivate()
                                || obj.isProtected()
                                || obj.isStatic()
                                || obj.isFinal()
                                || obj.isSynchronized()
                                || obj.isNative()
                                || obj.isStrictfp() ) {
                            throw new ClassConstraintException("Interface method '"+tostring(obj)+ "' must not have" +
                                " any of the ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED,"+
                                " ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT modifiers set.");
                        }
                    }
                }
            }

            if ((obj.getAccessFlags() &
                    ~(Const.ACC_PUBLIC|Const.ACC_PRIVATE|Const.ACC_PROTECTED|Const.ACC_STATIC|Const.ACC_FINAL|
                      Const.ACC_SYNCHRONIZED|Const.ACC_NATIVE|Const.ACC_ABSTRACT|Const.ACC_STRICT)) > 0) {
                addMessage("Method '"+tostring(obj)+"' has access flag(s) other than"+
                    " ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL,"+
                        " ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT set (ignored).");
            }

            final String nameanddesc = name+sig;
            if (method_names_and_desc.contains(nameanddesc)) {
                throw new ClassConstraintException(
                    "No two methods (like '"+tostring(obj)+"') are allowed have same names and desciptors!");
            }
            method_names_and_desc.add(nameanddesc);

            final Attribute[] atts = obj.getAttributes();
            int num_code_atts = 0;
            for (final Attribute att : atts) {
                if ((!(att instanceof Code)) &&
                        (!(att instanceof ExceptionTable)) &&
                        (!(att instanceof Synthetic)) &&
                        (!(att instanceof Deprecated))) {
                    addMessage("Attribute '" + tostring(att) + "' as an attribute of Method '" + tostring(obj) +
                        "' is unknown and will therefore be ignored.");
                }
                if ((!(att instanceof Code)) &&
                        (!(att instanceof ExceptionTable))) {
                    addMessage("Attribute '" + tostring(att) + "' as an attribute of Method '" + tostring(obj) +
                        "' is neither Code nor Exceptions and is therefore only of use for debuggers and such.");
                }
                if ((att instanceof Code) && (obj.isNative() || obj.isAbstract())) {
                    throw new ClassConstraintException("Native or abstract methods like '" + tostring(obj) +
                        "' must not have a Code attribute like '" + tostring(att) + "'."); //vmspec2 page120, 4.7.3
                }
                if (att instanceof Code) {
                    num_code_atts++;
                }
            }
            if ( !obj.isNative() && !obj.isAbstract() && num_code_atts != 1) {
                throw new ClassConstraintException("Non-native, non-abstract methods like '"+tostring(obj)+
                    "' must have exactly one Code attribute (found: "+num_code_atts+").");
            }
        }
        ///////////////////////////////////////////////////////
        // ClassFile-structure-ATTRIBUTES (vmspec2 4.1, 4.7) //
        ///////////////////////////////////////////////////////
        @Override
        public void visitSourceFile(final SourceFile obj) {//vmspec2 4.7.7

            // zero or one SourceFile attr per ClassFile: see visitJavaClass()

            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("SourceFile")) {
                throw new ClassConstraintException(
                    "The SourceFile attribute '"+tostring(obj)+"' is not correctly named 'SourceFile' but '"+name+"'.");
            }

            checkIndex(obj, obj.getSourceFileIndex(), CONST_Utf8);

            final String sourceFileName = ((ConstantUtf8) cp.getConstant(obj.getSourceFileIndex())).getBytes(); //==obj.getSourceFileName() ?
            final String sourceFileNameLc = sourceFileName.toLowerCase(Locale.ENGLISH);

            if (    (sourceFileName.indexOf('/') != -1) ||
                        (sourceFileName.indexOf('\\') != -1) ||
                        (sourceFileName.indexOf(':') != -1) ||
                        (sourceFileNameLc.lastIndexOf(".java") == -1)    ) {
                addMessage("SourceFile attribute '"+tostring(obj)+
                    "' has a funny name: remember not to confuse certain parsers working on javap's output. Also, this name ('"+
                    sourceFileName+"') is considered an unqualified (simple) file name only.");
            }
        }
        @Override
        public void visitDeprecated(final Deprecated obj) {//vmspec2 4.7.10
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("Deprecated")) {
                throw new ClassConstraintException("The Deprecated attribute '"+tostring(obj)+
                    "' is not correctly named 'Deprecated' but '"+name+"'.");
            }
        }
        @Override
        public void visitSynthetic(final Synthetic obj) {//vmspec2 4.7.6
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("Synthetic")) {
                throw new ClassConstraintException(
                    "The Synthetic attribute '"+tostring(obj)+"' is not correctly named 'Synthetic' but '"+name+"'.");
            }
        }
        @Override
        public void visitInnerClasses(final InnerClasses obj) {//vmspec2 4.7.5

            // exactly one InnerClasses attr per ClassFile if some inner class is refernced: see visitJavaClass()

            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("InnerClasses")) {
                throw new ClassConstraintException(
                    "The InnerClasses attribute '"+tostring(obj)+"' is not correctly named 'InnerClasses' but '"+name+"'.");
            }

            final InnerClass[] ics = obj.getInnerClasses();

            for (final InnerClass ic : ics) {
                checkIndex(obj, ic.getInnerClassIndex(), CONST_Class);
                final int outer_idx = ic.getOuterClassIndex();
                if (outer_idx != 0) {
                    checkIndex(obj, outer_idx, CONST_Class);
                }
                final int innername_idx = ic.getInnerNameIndex();
                if (innername_idx != 0) {
                    checkIndex(obj, innername_idx, CONST_Utf8);
                }
                int acc = ic.getInnerAccessFlags();
                acc = acc & (~ (Const.ACC_PUBLIC | Const.ACC_PRIVATE | Const.ACC_PROTECTED |
                                Const.ACC_STATIC | Const.ACC_FINAL | Const.ACC_INTERFACE | Const.ACC_ABSTRACT));
                if (acc != 0) {
                    addMessage(
                        "Unknown access flag for inner class '"+tostring(ic)+"' set (InnerClasses attribute '"+tostring(obj)+"').");
                }
            }
            // Semantical consistency is not yet checked by Sun, see vmspec2 4.7.5.
            // [marked TODO in JustIce]
        }
        ////////////////////////////////////////////////////////
        // field_info-structure-ATTRIBUTES (vmspec2 4.5, 4.7) //
        ////////////////////////////////////////////////////////
        @Override
        public void visitConstantValue(final ConstantValue obj) {//vmspec2 4.7.2
            // Despite its name, this really is an Attribute,
            // not a constant!
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("ConstantValue")) {
                throw new ClassConstraintException(
                    "The ConstantValue attribute '"+tostring(obj)+"' is not correctly named 'ConstantValue' but '"+name+"'.");
            }

            final Object pred = carrier.predecessor();
            if (pred instanceof Field) { //ConstantValue attributes are quite senseless if the predecessor is not a field.
                final Field f = (Field) pred;
                // Field constraints have been checked before -- so we are safe using their type information.
                final Type field_type = Type.getType(((ConstantUtf8) (cp.getConstant(f.getSignatureIndex()))).getBytes());

                final int index = obj.getConstantValueIndex();
                if ((index < 0) || (index >= cplen)) {
                    throw new ClassConstraintException("Invalid index '"+index+"' used by '"+tostring(obj)+"'.");
                }
                final Constant c = cp.getConstant(index);

                if (CONST_Long.isInstance(c) && field_type.equals(Type.LONG)) {
                    return;
                }
                if (CONST_Float.isInstance(c) && field_type.equals(Type.FLOAT)) {
                    return;
                }
                if (CONST_Double.isInstance(c) && field_type.equals(Type.DOUBLE)) {
                    return;
                }
                if (CONST_Integer.isInstance(c) && (field_type.equals(Type.INT) || field_type.equals(Type.SHORT) ||
                   field_type.equals(Type.CHAR) || field_type.equals(Type.BYTE) || field_type.equals(Type.BOOLEAN))) {
                    return;
                }
                if (CONST_String.isInstance(c) && field_type.equals(Type.STRING)) {
                    return;
                }

                throw new ClassConstraintException("Illegal type of ConstantValue '"+obj+"' embedding Constant '"+c+
                    "'. It is referenced by field '"+tostring(f)+"' expecting a different type: '"+field_type+"'.");
            }
        }
        // SYNTHETIC: see above
        // DEPRECATED: see above
        /////////////////////////////////////////////////////////
        // method_info-structure-ATTRIBUTES (vmspec2 4.6, 4.7) //
        /////////////////////////////////////////////////////////
        @Override
        public void visitCode(final Code obj) {//vmspec2 4.7.3
            try {
            // No code attribute allowed for native or abstract methods: see visitMethod(Method).
            // Code array constraints are checked in Pass3 (3a and 3b).

            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("Code")) {
                throw new ClassConstraintException(
                    "The Code attribute '"+tostring(obj)+"' is not correctly named 'Code' but '"+name+"'.");
            }

            Method m = null; // satisfy compiler
            if (!(carrier.predecessor() instanceof Method)) {
                addMessage("Code attribute '"+tostring(obj)+"' is not declared in a method_info structure but in '"+
                            carrier.predecessor()+"'. Ignored.");
                return;
            }
            m = (Method) carrier.predecessor();    // we can assume this method was visited before;
                                                                                    // i.e. the data consistency was verified.

            if (obj.getCode().length == 0) {
                throw new ClassConstraintException(
                    "Code array of Code attribute '"+tostring(obj)+"' (method '"+m+"') must not be empty.");
            }

            //In JustIce, the check for correct offsets into the code array is delayed to Pass 3a.
            final CodeException[] exc_table = obj.getExceptionTable();
            for (final CodeException element : exc_table) {
                final int exc_index = element.getCatchType();
                if (exc_index != 0) { // if 0, it catches all Throwables
                    checkIndex(obj, exc_index, CONST_Class);
                    final ConstantClass cc = (ConstantClass) (cp.getConstant(exc_index));
                    // cannot be sure this ConstantClass has already been visited (checked)!
                    checkIndex(cc, cc.getNameIndex(), CONST_Utf8);
                    final String cname = ((ConstantUtf8) cp.getConstant(cc.getNameIndex())).getBytes().replace('/','.');

                    Verifier v = VerifierFactory.getVerifier(cname);
                    VerificationResult vr = v.doPass1();

                    if (vr != VerificationResult.VR_OK) {
                        throw new ClassConstraintException("Code attribute '"+tostring(obj)+"' (method '"+m+
                           "') has an exception_table entry '"+tostring(element)+"' that references '"+cname+
                           "' as an Exception but it does not pass verification pass 1: "+vr);
                    }
                    // We cannot safely trust any other "instanceof" mechanism. We need to transitively verify
                    // the ancestor hierarchy.
                    JavaClass e = Repository.lookupClass(cname);
                    final JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());
                    final JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName());
                    while (e != o) {
                        if (e == t) {
                            break; // It's a subclass of Throwable, OKAY, leave.
                        }

                        v = VerifierFactory.getVerifier(e.getSuperclassName());
                        vr = v.doPass1();
                        if (vr != VerificationResult.VR_OK) {
                            throw new ClassConstraintException("Code attribute '"+tostring(obj)+"' (method '"+m+
                                "') has an exception_table entry '"+tostring(element)+"' that references '"+cname+
                                "' as an Exception but '"+e.getSuperclassName()+
                                "' in the ancestor hierachy does not pass verification pass 1: "+vr);
                        }
                        e = Repository.lookupClass(e.getSuperclassName());
                    }
                    if (e != t) {
                        throw new ClassConstraintException("Code attribute '"+tostring(obj)+"' (method '"+m+
                            "') has an exception_table entry '"+tostring(element)+"' that references '"+cname+
                            "' as an Exception but it is not a subclass of '"+t.getClassName()+"'.");
                    }
                }
            }

            // Create object for local variables information
            // This is highly unelegant due to usage of the Visitor pattern.
            // TODO: rework it.
            int method_number = -1;
            final Method[] ms = Repository.lookupClass(myOwner.getClassName()).getMethods();
            for (int mn=0; mn<ms.length; mn++) {
                if (m == ms[mn]) {
                    method_number = mn;
                    break;
                }
            }
            if (method_number < 0) { // Mmmmh. Can we be sure BCEL does not sometimes instantiate new objects?
                throw new AssertionViolatedException(
                    "Could not find a known BCEL Method object in the corresponding BCEL JavaClass object.");
            }
            localVariablesInfos[method_number] = new LocalVariablesInfo(obj.getMaxLocals());

            int num_of_lvt_attribs = 0;
            // Now iterate through the attributes the Code attribute has.
            final Attribute[] atts = obj.getAttributes();
            for (int a=0; a<atts.length; a++) {
                if ((! (atts[a] instanceof LineNumberTable)) &&
                    (! (atts[a] instanceof LocalVariableTable))) {
                    addMessage("Attribute '"+tostring(atts[a])+"' as an attribute of Code attribute '"+tostring(obj)+
                        "' (method '"+m+"') is unknown and will therefore be ignored.");
                }
                else{// LineNumberTable or LocalVariableTable
                    addMessage("Attribute '"+tostring(atts[a])+"' as an attribute of Code attribute '"+tostring(obj)+
                        "' (method '"+m+"') will effectively be ignored and is only useful for debuggers and such.");
                }

                //LocalVariableTable check (partially delayed to Pass3a).
                //Here because its easier to collect the information of the
                //(possibly more than one) LocalVariableTables belonging to
                //one certain Code attribute.
                if (atts[a] instanceof LocalVariableTable) { // checks conforming to vmspec2 4.7.9

                    final LocalVariableTable lvt = (LocalVariableTable) atts[a];

                    checkIndex(lvt, lvt.getNameIndex(), CONST_Utf8);

                    final String lvtname = ((ConstantUtf8) cp.getConstant(lvt.getNameIndex())).getBytes();
                    if (! lvtname.equals("LocalVariableTable")) {
                        throw new ClassConstraintException("The LocalVariableTable attribute '"+tostring(lvt)+
                                "' is not correctly named 'LocalVariableTable' but '"+lvtname+"'.");
                    }

                    final Code code = obj;

                    //In JustIce, the check for correct offsets into the code array is delayed to Pass 3a.
                    final LocalVariable[] localvariables = lvt.getLocalVariableTable();

                    for (final LocalVariable localvariable : localvariables) {
                        checkIndex(lvt, localvariable.getNameIndex(), CONST_Utf8);
                        final String localname = ((ConstantUtf8) cp.getConstant(localvariable.getNameIndex())).getBytes();
                        if (!validJavaIdentifier(localname)) {
                            throw new ClassConstraintException("LocalVariableTable '"+tostring(lvt)+
                                "' references a local variable by the name '"+localname+"' which is not a legal Java simple name.");
                        }

                        checkIndex(lvt, localvariable.getSignatureIndex(), CONST_Utf8);
                        final String localsig  =
                            ((ConstantUtf8) (cp.getConstant(localvariable.getSignatureIndex()))).getBytes(); // Local sig.(=descriptor)
                        Type t;
                        try{
                            t = Type.getType(localsig);
                        }
                        catch (final ClassFormatException cfe) {
                            throw new ClassConstraintException("Illegal descriptor (==signature) '"+localsig+
                                "' used by LocalVariable '"+tostring(localvariable)+"' referenced by '"+tostring(lvt)+"'.", cfe);
                        }
                        final int localindex = localvariable.getIndex();
                        if ( ( (t==Type.LONG || t==Type.DOUBLE)? localindex+1:localindex) >= code.getMaxLocals()) {
                            throw new ClassConstraintException("LocalVariableTable attribute '"+tostring(lvt)+
                                "' references a LocalVariable '"+tostring(localvariable)+
                                "' with an index that exceeds the surrounding Code attribute's max_locals value of '"+
                                code.getMaxLocals()+"'.");
                        }

                        try{
                            localVariablesInfos[method_number].add(localindex, localname, localvariable.getStartPC(),
                                                                   localvariable.getLength(), t);
                        }
                        catch(final LocalVariableInfoInconsistentException lviie) {
                            throw new ClassConstraintException("Conflicting information in LocalVariableTable '"+tostring(lvt)+
                                "' found in Code attribute '"+tostring(obj)+
                                "' (method '"+tostring(m)+"'). "+lviie.getMessage(), lviie);
                        }
                    }// for all local variables localvariables[i] in the LocalVariableTable attribute atts[a] END

                    num_of_lvt_attribs++;
                    if (!m.isStatic() && num_of_lvt_attribs > obj.getMaxLocals()) {
                        throw new ClassConstraintException("Number of LocalVariableTable attributes of Code attribute '"+
                            tostring(obj)+"' (method '"+tostring(m)+"') exceeds number of local variable slots '"+obj.getMaxLocals()+
                            "' ('There may be at most one LocalVariableTable attribute per local variable in the Code attribute.').");
                    }
                }// if atts[a] instanceof LocalVariableTable END
            }// for all attributes atts[a] END

            } catch (final ClassNotFoundException e) {
            // FIXME: this might not be the best way to handle missing classes.
            throw new AssertionViolatedException("Missing class: " + e, e);
            }

        }// visitCode(Code) END

        @Override
        public void visitExceptionTable(final ExceptionTable obj) {//vmspec2 4.7.4
            try {
            // incorrectly named, it's the Exceptions attribute (vmspec2 4.7.4)
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("Exceptions")) {
                throw new ClassConstraintException(
                    "The Exceptions attribute '"+tostring(obj)+"' is not correctly named 'Exceptions' but '"+name+"'.");
            }

            final int[] exc_indices = obj.getExceptionIndexTable();

            for (final int exc_indice : exc_indices) {
                checkIndex(obj, exc_indice, CONST_Class);

                final ConstantClass cc = (ConstantClass) (cp.getConstant(exc_indice));
                checkIndex(cc, cc.getNameIndex(), CONST_Utf8); // can't be sure this ConstantClass has already been visited (checked)!
                //convert internal notation on-the-fly to external notation:
                final String cname = ((ConstantUtf8) cp.getConstant(cc.getNameIndex())).getBytes().replace('/','.');

                Verifier v = VerifierFactory.getVerifier(cname);
                VerificationResult vr = v.doPass1();

                if (vr != VerificationResult.VR_OK) {
                    throw new ClassConstraintException("Exceptions attribute '"+tostring(obj)+"' references '"+cname+
                            "' as an Exception but it does not pass verification pass 1: "+vr);
                }
                // We cannot safely trust any other "instanceof" mechanism. We need to transitively verify
                // the ancestor hierarchy.
                JavaClass e = Repository.lookupClass(cname);
                final JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());
                final JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName());
                while (e != o) {
                    if (e == t) {
                        break; // It's a subclass of Throwable, OKAY, leave.
                    }

                    v = VerifierFactory.getVerifier(e.getSuperclassName());
                    vr = v.doPass1();
                    if (vr != VerificationResult.VR_OK) {
                        throw new ClassConstraintException("Exceptions attribute '"+tostring(obj)+"' references '"+cname+
                                "' as an Exception but '"+e.getSuperclassName()+
                                "' in the ancestor hierachy does not pass verification pass 1: "+vr);
                    }
                    e = Repository.lookupClass(e.getSuperclassName());
                }
                if (e != t) {
                    throw new ClassConstraintException("Exceptions attribute '"+tostring(obj)+"' references '"+cname+
                            "' as an Exception but it is not a subclass of '"+t.getClassName()+"'.");
                }
            }

            } catch (final ClassNotFoundException e) {
            // FIXME: this might not be the best way to handle missing classes.
            throw new AssertionViolatedException("Missing class: " + e, e);
            }
        }
        // SYNTHETIC: see above
        // DEPRECATED: see above
        //////////////////////////////////////////////////////////////
        // code_attribute-structure-ATTRIBUTES (vmspec2 4.7.3, 4.7) //
        //////////////////////////////////////////////////////////////
        @Override
        public void visitLineNumberTable(final LineNumberTable obj) {//vmspec2 4.7.8
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            final String name = ((ConstantUtf8) cp.getConstant(obj.getNameIndex())).getBytes();
            if (! name.equals("LineNumberTable")) {
                throw new ClassConstraintException("The LineNumberTable attribute '"+tostring(obj)+
                        "' is not correctly named 'LineNumberTable' but '"+name+"'.");
            }

            //In JustIce,this check is delayed to Pass 3a.
            //LineNumber[] linenumbers = obj.getLineNumberTable();
            // ...validity check...

        }
        @Override
        public void visitLocalVariableTable(final LocalVariableTable obj) {//vmspec2 4.7.9
            //In JustIce,this check is partially delayed to Pass 3a.
            //The other part can be found in the visitCode(Code) method.
        }
        ////////////////////////////////////////////////////
        // MISC-structure-ATTRIBUTES (vmspec2 4.7.1, 4.7) //
        ////////////////////////////////////////////////////
        @Override
        public void visitUnknown(final Unknown obj) {//vmspec2 4.7.1
            // Represents an unknown attribute.
            checkIndex(obj, obj.getNameIndex(), CONST_Utf8);

            // Maybe only misnamed? Give a (warning) message.
            addMessage("Unknown attribute '"+tostring(obj)+"'. This attribute is not known in any context!");
        }
        //////////
        // BCEL //
        //////////
        @Override
        public void visitLocalVariable(final LocalVariable obj) {
            // This does not represent an Attribute but is only
            // related to internal BCEL data representation.

            // see visitLocalVariableTable(LocalVariableTable)
        }
        @Override
        public void visitCodeException(final CodeException obj) {
            // Code constraints are checked in Pass3 (3a and 3b).
            // This does not represent an Attribute but is only
            // related to internal BCEL data representation.

            // see visitCode(Code)
        }
        @Override
        public void visitConstantPool(final ConstantPool obj) {
            // No need to. We're piggybacked by the DescendingVisitor.
            // This does not represent an Attribute but is only
            // related to internal BCEL data representation.
        }
        @Override
        public void visitInnerClass(final InnerClass obj) {
            // This does not represent an Attribute but is only
            // related to internal BCEL data representation.
        }
        @Override
        public void visitLineNumber(final LineNumber obj) {
            // This does not represent an Attribute but is only
            // related to internal BCEL data representation.

            // see visitLineNumberTable(LineNumberTable)
        }
    }

    /**
     * Ensures that the ConstantCP-subclassed entries of the constant
     * pool are valid. According to "Yellin: Low Level Security in Java",
     * this method does not verify the existence of referenced entities
     * (such as classes) but only the formal correctness (such as well-formed
     * signatures).
     * The visitXXX() methods throw ClassConstraintException instances otherwise.
     * <B>Precondition: index-style cross referencing in the constant
     * pool must be valid. Simply invoke constant_pool_entries_satisfy_static_constraints()
     * before.</B>
     *
     * @throws ClassConstraintException otherwise.
     * @see #constant_pool_entries_satisfy_static_constraints()
     */
    private void field_and_method_refs_are_valid() {
        try {
        final JavaClass jc = Repository.lookupClass(myOwner.getClassName());
        final DescendingVisitor v = new DescendingVisitor(jc, new FAMRAV_Visitor(jc));
        v.visit();

        } catch (final ClassNotFoundException e) {
        // FIXME: this might not be the best way to handle missing classes.
        throw new AssertionViolatedException("Missing class: " + e, e);
        }
    }

    /**
     * A Visitor class that ensures the ConstantCP-subclassed entries
     * of the constant pool are valid.
     * <B>Precondition: index-style cross referencing in the constant
     * pool must be valid.</B>
     *
     * @see #constant_pool_entries_satisfy_static_constraints()
     * @see org.apache.bcel.classfile.ConstantCP
     */
    private final class FAMRAV_Visitor extends EmptyVisitor{
        private final ConstantPool cp; // ==jc.getConstantPool() -- only here to save typing work.
        private FAMRAV_Visitor(final JavaClass _jc) {
            cp = _jc.getConstantPool();
        }

        @Override
        public void visitConstantFieldref(final ConstantFieldref obj) {
            if (obj.getTag() != Const.CONSTANT_Fieldref) {
                throw new ClassConstraintException("ConstantFieldref '"+tostring(obj)+"' has wrong tag!");
            }
            final int name_and_type_index = obj.getNameAndTypeIndex();
            final ConstantNameAndType cnat = (ConstantNameAndType) (cp.getConstant(name_and_type_index));
            final String name = ((ConstantUtf8) (cp.getConstant(cnat.getNameIndex()))).getBytes(); // Field or Method name
            if (!validFieldName(name)) {
                throw new ClassConstraintException("Invalid field name '"+name+"' referenced by '"+tostring(obj)+"'.");
            }

            final int class_index = obj.getClassIndex();
            final ConstantClass cc = (ConstantClass) (cp.getConstant(class_index));
            final String className = ((ConstantUtf8) (cp.getConstant(cc.getNameIndex()))).getBytes(); // Class Name in internal form
            if (! validClassName(className)) {
                throw new ClassConstraintException("Illegal class name '"+className+"' used by '"+tostring(obj)+"'.");
            }

            final String sig  = ((ConstantUtf8) (cp.getConstant(cnat.getSignatureIndex()))).getBytes(); // Field or Method sig.(=descriptor)

            try{
                Type.getType(sig); /* Don't need the return value */
            }
            catch (final ClassFormatException cfe) {
                throw new ClassConstraintException("Illegal descriptor (==signature) '"+sig+"' used by '"+tostring(obj)+"'.", cfe);
            }
        }

        @Override
        public void visitConstantMethodref(final ConstantMethodref obj) {
            if (obj.getTag() != Const.CONSTANT_Methodref) {
                throw new ClassConstraintException("ConstantMethodref '"+tostring(obj)+"' has wrong tag!");
            }
            final int name_and_type_index = obj.getNameAndTypeIndex();
            final ConstantNameAndType cnat = (ConstantNameAndType) (cp.getConstant(name_and_type_index));
            final String name = ((ConstantUtf8) (cp.getConstant(cnat.getNameIndex()))).getBytes(); // Field or Method name
            if (!validClassMethodName(name)) {
                throw new ClassConstraintException(
                    "Invalid (non-interface) method name '"+name+"' referenced by '"+tostring(obj)+"'.");
            }

            final int class_index = obj.getClassIndex();
            final ConstantClass cc = (ConstantClass) (cp.getConstant(class_index));
            final String className = ((ConstantUtf8) (cp.getConstant(cc.getNameIndex()))).getBytes(); // Class Name in internal form
            if (! validClassName(className)) {
                throw new ClassConstraintException("Illegal class name '"+className+"' used by '"+tostring(obj)+"'.");
            }

            final String sig  = ((ConstantUtf8) (cp.getConstant(cnat.getSignatureIndex()))).getBytes(); // Field or Method sig.(=descriptor)

            try{
                final Type   t  = Type.getReturnType(sig);
                if ( name.equals(Const.CONSTRUCTOR_NAME) && (t != Type.VOID) ) {
                    throw new ClassConstraintException("Instance initialization method must have VOID return type.");
                }
            }
            catch (final ClassFormatException cfe) {
                throw new ClassConstraintException("Illegal descriptor (==signature) '"+sig+"' used by '"+tostring(obj)+"'.", cfe);
            }
        }

        @Override
        public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj) {
            if (obj.getTag() != Const.CONSTANT_InterfaceMethodref) {
                throw new ClassConstraintException("ConstantInterfaceMethodref '"+tostring(obj)+"' has wrong tag!");
            }
            final int name_and_type_index = obj.getNameAndTypeIndex();
            final ConstantNameAndType cnat = (ConstantNameAndType) (cp.getConstant(name_and_type_index));
            final String name = ((ConstantUtf8) (cp.getConstant(cnat.getNameIndex()))).getBytes(); // Field or Method name
            if (!validInterfaceMethodName(name)) {
                throw new ClassConstraintException("Invalid (interface) method name '"+name+"' referenced by '"+tostring(obj)+"'.");
            }

            final int class_index = obj.getClassIndex();
            final ConstantClass cc = (ConstantClass) (cp.getConstant(class_index));
            final String className = ((ConstantUtf8) (cp.getConstant(cc.getNameIndex()))).getBytes(); // Class Name in internal form
            if (! validClassName(className)) {
                throw new ClassConstraintException("Illegal class name '"+className+"' used by '"+tostring(obj)+"'.");
            }

            final String sig  = ((ConstantUtf8) (cp.getConstant(cnat.getSignatureIndex()))).getBytes(); // Field or Method sig.(=descriptor)

            try{
                final Type   t  = Type.getReturnType(sig);
                if ( name.equals(Const.STATIC_INITIALIZER_NAME) && (t != Type.VOID) ) {
                    addMessage("Class or interface initialization method '"+Const.STATIC_INITIALIZER_NAME+
                        "' usually has VOID return type instead of '"+t+
                        "'. Note this is really not a requirement of The Java Virtual Machine Specification, Second Edition.");
                }
            }
            catch (final ClassFormatException cfe) {
                throw new ClassConstraintException("Illegal descriptor (==signature) '"+sig+"' used by '"+tostring(obj)+"'.", cfe);
            }

        }

    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid Java class name.
     */
    private static boolean validClassName(final String name) {
        /*
         * TODO: implement.
         * Are there any restrictions?
         */
        return true;
    }
    /**
     * This method returns true if and only if the supplied String
     * represents a valid method name.
     * This is basically the same as a valid identifier name in the
     * Java programming language, but the special name for
     * the instance initialization method is allowed and the special name
     * for the class/interface initialization method may be allowed.
     */
    private static boolean validMethodName(final String name, final boolean allowStaticInit) {
        if (validJavaLangMethodName(name)) {
            return true;
        }

        if (allowStaticInit) {
            return name.equals(Const.CONSTRUCTOR_NAME) || name.equals(Const.STATIC_INITIALIZER_NAME);
        }
        return name.equals(Const.CONSTRUCTOR_NAME);
    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid method name that may be referenced by
     * ConstantMethodref objects.
     */
    private static boolean validClassMethodName(final String name) {
        return validMethodName(name, false);
    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid Java programming language method name stored as a simple
     * (non-qualified) name.
     * Conforming to: The Java Virtual Machine Specification, Second Edition, �2.7, �2.7.1, �2.2.
     */
    private static boolean validJavaLangMethodName(final String name) {
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }

        for (int i=1; i<name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid Java interface method name that may be
     * referenced by ConstantInterfaceMethodref objects.
     */
    private static boolean validInterfaceMethodName(final String name) {
        // I guess we should assume special names forbidden here.
        if (name.startsWith("<")) {
            return false;
        }
        return validJavaLangMethodName(name);
    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid Java identifier (so-called simple name).
     */
    private static boolean validJavaIdentifier(final String name) {
    if  (name.length() == 0) {
        return false; // must not be empty, reported by <francis.andre@easynet.fr>, thanks!
    }

        // vmspec2 2.7, vmspec2 2.2
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }

        for (int i=1; i<name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns true if and only if the supplied String
     * represents a valid Java field name.
     */
    private static boolean validFieldName(final String name) {
        // vmspec2 2.7, vmspec2 2.2
        return validJavaIdentifier(name);
    }

    /**
     * This class serves for finding out if a given JavaClass' ConstantPool
     * references an Inner Class.
     * The Java Virtual Machine Specification, Second Edition is not very precise
     * about when an "InnerClasses" attribute has to appear. However, it states that
     * there has to be exactly one InnerClasses attribute in the ClassFile structure
     * if the constant pool of a class or interface refers to any class or interface
     * "that is not a member of a package". Sun does not mean "member of the default
     * package". In "Inner Classes Specification" they point out how a "bytecode name"
     * is derived so one has to deduce what a class name of a class "that is not a
     * member of a package" looks like: there is at least one character in the byte-
     * code name that cannot be part of a legal Java Language Class name (and not equal
     * to '/'). This assumption is wrong as the delimiter is '$' for which
     * Character.isJavaIdentifierPart() == true.
     * Hence, you really run into trouble if you have a toplevel class called
     * "A$XXX" and another toplevel class called "A" with in inner class called "XXX".
     * JustIce cannot repair this; please note that existing verifiers at this
     * time even fail to detect missing InnerClasses attributes in pass 2.
     */
    private static class InnerClassDetector extends EmptyVisitor{
        private boolean hasInnerClass = false;
        private final JavaClass jc;
        private final ConstantPool cp;

        /** Constructs an InnerClassDetector working on the JavaClass _jc. */
        public InnerClassDetector(final JavaClass javaClass) {
            this.jc = javaClass;
            this.cp = jc.getConstantPool();
            (new DescendingVisitor(jc, this)).visit();
        }

        /**
         * Returns if the JavaClass this InnerClassDetector is working on
         * has an Inner Class reference in its constant pool.
         *
         * @return Whether this InnerClassDetector is working on has an Inner Class reference in its constant pool.
         */
        public boolean innerClassReferenced() {
            return hasInnerClass;
        }

        /** This method casually visits ConstantClass references. */
        @Override
        public void visitConstantClass(final ConstantClass obj) {
            final Constant c = cp.getConstant(obj.getNameIndex());
            if (c instanceof ConstantUtf8) { //Ignore the case where it's not a ConstantUtf8 here, we'll find out later.
                final String classname = ((ConstantUtf8) c).getBytes();
                if (classname.startsWith(jc.getClassName().replace('.','/')+"$")) {
                    hasInnerClass = true;
                }
            }
        }
    }

    /**
     * This method is here to save typing work and improve code readability.
     */
    private static String tostring(final Node n) {
        return new StringRepresentation(n).toString();
    }
}
