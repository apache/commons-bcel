/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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


import org.apache.bcel.classfile.*;
import org.apache.bcel.classfile.Deprecated; // use _this_ one!
import org.apache.bcel.verifier.exc.*;

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
 * @version $Id$
 * @author <A HREF="http://www.inf.fu-berlin.de/~ehaase"/>Enver Haase</A>
 */
public class StringRepresentation extends org.apache.bcel.classfile.EmptyVisitor implements Visitor {
    /** The string representation, created by a visitXXX() method, output by toString(). */
    private String tostring;
    /** The node we ask for its string representation. Not really needed; only for debug output. */
    private Node n;

    /**
     * Creates a new StringRepresentation object which is the representation of n.
     *
     * @see #toString()
     */
    public StringRepresentation(Node n) {
        this.n = n;
        n.accept(this); // assign a string representation to field 'tostring' if we know n's class.
    }

    /**
     * Returns the String representation.
     */
    public String toString() {
// The run-time check below is needed because we don't want to omit inheritance
// of "EmptyVisitor" and provide a thousand empty methods.
// However, in terms of performance this would be a better idea.
// If some new "Node" is defined in BCEL (such as some concrete "Attribute"), we
// want to know that this class has also to be adapted.
        if (tostring == null) throw new AssertionViolatedException("Please adapt '" + getClass() + "' to deal with objects of class '" + n.getClass() + "'.");
        return tostring;
    }

    /**
     * Returns the String representation of the Node object obj;
     * this is obj.toString() if it does not throw any RuntimeException,
     * or else it is a string derived only from obj's class name.
     */
    private String toString(Node obj) {
        String ret;
        try {
            ret = obj.toString();
        }
        catch (RuntimeException e) { // including ClassFormatException, trying to convert the "signature" of a ReturnaddressType LocalVariable (shouldn't occur, but people do crazy things)
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
    public void visitCode(Code obj) {
        //tostring = toString(obj);
        tostring = "<CODE>"; // We don't need real code outputs.
    }

    public void visitCodeException(CodeException obj) {
        tostring = toString(obj);
    }

    public void visitConstantClass(ConstantClass obj) {
        tostring = toString(obj);
    }

    public void visitConstantDouble(ConstantDouble obj) {
        tostring = toString(obj);
    }

    public void visitConstantFieldref(ConstantFieldref obj) {
        tostring = toString(obj);
    }

    public void visitConstantFloat(ConstantFloat obj) {
        tostring = toString(obj);
    }

    public void visitConstantInteger(ConstantInteger obj) {
        tostring = toString(obj);
    }

    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
        tostring = toString(obj);
    }

    public void visitConstantLong(ConstantLong obj) {
        tostring = toString(obj);
    }

    public void visitConstantMethodref(ConstantMethodref obj) {
        tostring = toString(obj);
    }

    public void visitConstantNameAndType(ConstantNameAndType obj) {
        tostring = toString(obj);
    }

    public void visitConstantPool(ConstantPool obj) {
        tostring = toString(obj);
    }

    public void visitConstantString(ConstantString obj) {
        tostring = toString(obj);
    }

    public void visitConstantUtf8(ConstantUtf8 obj) {
        tostring = toString(obj);
    }

    public void visitConstantValue(ConstantValue obj) {
        tostring = toString(obj);
    }

    public void visitDeprecated(Deprecated obj) {
        tostring = toString(obj);
    }

    public void visitExceptionTable(ExceptionTable obj) {
        tostring = toString(obj);
    }

    public void visitField(Field obj) {
        tostring = toString(obj);
    }

    public void visitInnerClass(InnerClass obj) {
        tostring = toString(obj);
    }

    public void visitInnerClasses(InnerClasses obj) {
        tostring = toString(obj);
    }

    public void visitJavaClass(JavaClass obj) {
        tostring = toString(obj);
    }

    public void visitLineNumber(LineNumber obj) {
        tostring = toString(obj);
    }

    public void visitLineNumberTable(LineNumberTable obj) {
        tostring = "<LineNumberTable: " + toString(obj) + ">";
    }

    public void visitLocalVariable(LocalVariable obj) {
        tostring = toString(obj);
    }

    public void visitLocalVariableTable(LocalVariableTable obj) {
        tostring = "<LocalVariableTable: " + toString(obj) + ">";
    }

    public void visitMethod(Method obj) {
        tostring = toString(obj);
    }

    public void visitSignature(Signature obj) {
        tostring = toString(obj);
    }

    public void visitSourceFile(SourceFile obj) {
        tostring = toString(obj);
    }

    public void visitStackMap(StackMap obj) {
        tostring = toString(obj);
    }

    public void visitSynthetic(Synthetic obj) {
        tostring = toString(obj);
    }

    public void visitUnknown(Unknown obj) {
        tostring = toString(obj);
    }
}
