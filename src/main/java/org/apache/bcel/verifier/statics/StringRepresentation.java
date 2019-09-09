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


import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.BootstrapMethods;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantInvokeDynamic;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodHandle;
import org.apache.bcel.classfile.ConstantMethodType;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.EnclosingMethod;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.LocalVariableTypeTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.MethodParameters;
import org.apache.bcel.classfile.NestMembers;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.ParameterAnnotationEntry;
import org.apache.bcel.classfile.ParameterAnnotations;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.verifier.exc.AssertionViolatedException;

/**
 * BCEL's Node classes (those from the classfile API that <B>accept()</B> Visitor
 * instances) have <B>toString()</B> methods that were not designed to be robust,
 * this gap is closed by this class.
 * When performing class file verification, it may be useful to output which
 * entity (e.g. a <B>Code</B> instance) is not satisfying the verifier's
 * constraints, but in this case it could be possible for the <B>toString()</B>
 * method to throw a RuntimeException.
 * A (new StringRepresentation(Node n)).toString() never throws any exception.
 * Note that this class also serves as a placeholder for more sophisticated message
 * handling in future versions of JustIce.
 *
 */
public class StringRepresentation extends org.apache.bcel.classfile.EmptyVisitor {
    /** The string representation, created by a visitXXX() method, output by toString(). */
    private String tostring;
    /** The node we ask for its string representation. Not really needed; only for debug output. */
    private final Node n;

    /**
     * Creates a new StringRepresentation object which is the representation of n.
     *
     * @param n The node to represent.
     * @see #toString()
     */
    public StringRepresentation(final Node n) {
        this.n = n;
        n.accept(this); // assign a string representation to field 'tostring' if we know n's class.
    }

    /**
     * Returns the String representation.
     */
    @Override
    public String toString() {
// The run-time check below is needed because we don't want to omit inheritance
// of "EmptyVisitor" and provide a thousand empty methods.
// However, in terms of performance this would be a better idea.
// If some new "Node" is defined in BCEL (such as some concrete "Attribute"), we
// want to know that this class has also to be adapted.
        if (tostring == null) {
            throw new AssertionViolatedException(
                "Please adapt '" + getClass() + "' to deal with objects of class '" + n.getClass() + "'.");
        }
        return tostring;
    }

    /**
     * Returns the String representation of the Node object obj;
     * this is obj.toString() if it does not throw any RuntimeException,
     * or else it is a string derived only from obj's class name.
     */
    private String toString(final Node obj) {
        String ret;
        try {
            ret = obj.toString();
        }

        catch (final RuntimeException e) {
            // including ClassFormatException, trying to convert the "signature" of a ReturnaddressType LocalVariable
            // (shouldn't occur, but people do crazy things)
            String s = obj.getClass().getName();
            s = s.substring(s.lastIndexOf(".") + 1);
            ret = "<<" + s + ">>";
        }
        return ret;
    }

    ////////////////////////////////
    // Visitor methods start here //
    ////////////////////////////////
    // We don't of course need to call some default implementation:
    // e.g. we could also simply output "Code" instead of a possibly
    // lengthy Code attribute's toString().
    @Override
    public void visitCode(final Code obj) {
        //tostring = toString(obj);
        tostring = "<CODE>"; // We don't need real code outputs.
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotation(final Annotations obj)
    {
        //this is invoked whenever an annotation is found
        //when verifier is passed over a class
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitLocalVariableTypeTable(final LocalVariableTypeTable obj)
    {
        //this is invoked whenever a local variable type is found
        //when verifier is passed over a class
        tostring = toString(obj);
    }

    @Override
    public void visitCodeException(final CodeException obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantClass(final ConstantClass obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantDouble(final ConstantDouble obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantFieldref(final ConstantFieldref obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantFloat(final ConstantFloat obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantInteger(final ConstantInteger obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantLong(final ConstantLong obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantMethodref(final ConstantMethodref obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantNameAndType(final ConstantNameAndType obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantPool(final ConstantPool obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantString(final ConstantString obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantUtf8(final ConstantUtf8 obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitConstantValue(final ConstantValue obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitDeprecated(final Deprecated obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitExceptionTable(final ExceptionTable obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitField(final Field obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitInnerClass(final InnerClass obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitInnerClasses(final InnerClasses obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitJavaClass(final JavaClass obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitLineNumber(final LineNumber obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitLineNumberTable(final LineNumberTable obj) {
        tostring = "<LineNumberTable: " + toString(obj) + ">";
    }

    @Override
    public void visitLocalVariable(final LocalVariable obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitLocalVariableTable(final LocalVariableTable obj) {
        tostring = "<LocalVariableTable: " + toString(obj) + ">";
    }

    @Override
    public void visitMethod(final Method obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitSignature(final Signature obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitSourceFile(final SourceFile obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitStackMap(final StackMap obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitSynthetic(final Synthetic obj) {
        tostring = toString(obj);
    }

    @Override
    public void visitUnknown(final Unknown obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitEnclosingMethod(final EnclosingMethod obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitBootstrapMethods(final BootstrapMethods obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitMethodParameters(final MethodParameters obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantInvokeDynamic(final ConstantInvokeDynamic obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitStackMapEntry(final StackMapEntry obj) {
        tostring = toString(obj);
    }
    /**
     * @since 6.0
     */

    @Override
    public void visitParameterAnnotation(final ParameterAnnotations obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationEntry(final AnnotationEntry obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationDefault(final AnnotationDefault obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantMethodType(final ConstantMethodType obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantMethodHandle(final ConstantMethodHandle obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitParameterAnnotationEntry(final ParameterAnnotationEntry obj) {
        tostring = toString(obj);
    }

    /**
     * @since 6.4.0
     */
    @Override
    public void visitNestMembers(final NestMembers obj) {
        tostring = toString(obj);
    }
}
