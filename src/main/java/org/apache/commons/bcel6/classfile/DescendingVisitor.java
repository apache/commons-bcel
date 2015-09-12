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
package org.apache.commons.bcel6.classfile;

import java.util.Stack;

/**
 * Traverses a JavaClass with another Visitor object 'piggy-backed' that is
 * applied to all components of a JavaClass object. I.e. this class supplies the
 * traversal strategy, other classes can make use of it.
 * 
 * @version $Id$
 */
public class DescendingVisitor implements Visitor
{
    private final JavaClass clazz;

    private final Visitor visitor;

    private final Stack<Object> stack = new Stack<>();

    /**
     * @return container of current entitity, i.e., predecessor during traversal
     */
    public Object predecessor()
    {
        return predecessor(0);
    }

    /**
     * @param level
     *            nesting level, i.e., 0 returns the direct predecessor
     * @return container of current entitity, i.e., predecessor during traversal
     */
    public Object predecessor(int level)
    {
        int size = stack.size();
        if ((size < 2) || (level < 0))
        {
            return null;
        }
        return stack.elementAt(size - (level + 2)); // size - 1 == current
    }

    /**
     * @return current object
     */
    public Object current()
    {
        return stack.peek();
    }

    /**
     * @param clazz
     *            Class to traverse
     * @param visitor
     *            visitor object to apply to all components
     */
    public DescendingVisitor(JavaClass clazz, Visitor visitor)
    {
        this.clazz = clazz;
        this.visitor = visitor;
    }

    /**
     * Start traversal.
     */
    public void visit()
    {
        clazz.accept(this);
    }

    @Override
    public void visitJavaClass(JavaClass _clazz)
    {
        stack.push(_clazz);
        _clazz.accept(visitor);
        Field[] fields = _clazz.getFields();
        for (Field field : fields) {
            field.accept(this);
        }
        Method[] methods = _clazz.getMethods();
        for (Method method : methods) {
            method.accept(this);
        }
        Attribute[] attributes = _clazz.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        _clazz.getConstantPool().accept(this);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotation(Annotations annotation)
    {
        stack.push(annotation);
        annotation.accept(visitor);
        AnnotationEntry[] entries = annotation.getAnnotationEntries();
        for (AnnotationEntry entrie : entries) {
            entrie.accept(this);
        }
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationEntry(AnnotationEntry annotationEntry)
    {
        stack.push(annotationEntry);
        annotationEntry.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitField(Field field)
    {
        stack.push(field);
        field.accept(visitor);
        Attribute[] attributes = field.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitConstantValue(ConstantValue cv)
    {
        stack.push(cv);
        cv.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitMethod(Method method)
    {
        stack.push(method);
        method.accept(visitor);
        Attribute[] attributes = method.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitExceptionTable(ExceptionTable table)
    {
        stack.push(table);
        table.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitCode(Code code)
    {
        stack.push(code);
        code.accept(visitor);
        CodeException[] table = code.getExceptionTable();
        for (CodeException element : table) {
            element.accept(this);
        }
        Attribute[] attributes = code.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitCodeException(CodeException ce)
    {
        stack.push(ce);
        ce.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitLineNumberTable(LineNumberTable table)
    {
        stack.push(table);
        table.accept(visitor);
        LineNumber[] numbers = table.getLineNumberTable();
        for (LineNumber number : numbers) {
            number.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitLineNumber(LineNumber number)
    {
        stack.push(number);
        number.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitLocalVariableTable(LocalVariableTable table)
    {
        stack.push(table);
        table.accept(visitor);
        LocalVariable[] vars = table.getLocalVariableTable();
        for (LocalVariable var : vars) {
            var.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitStackMap(StackMap table)
    {
        stack.push(table);
        table.accept(visitor);
        StackMapEntry[] vars = table.getStackMap();
        for (StackMapEntry var : vars) {
            var.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitStackMapEntry(StackMapEntry var)
    {
        stack.push(var);
        var.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
    @Override
    public void visitStackMapTable(StackMapTable table)
    {
        stack.push(table);
        table.accept(visitor);
        StackMapTableEntry[] vars = table.getStackMapTable();
        for (StackMapTableEntry var : vars) {
            var.accept(this);
        }
        stack.pop();
    }
     */

    /**
     * @since 6.0
    @Override
    public void visitStackMapTableEntry(StackMapTableEntry var)
    {
        stack.push(var);
        var.accept(visitor);
        stack.pop();
    }
     */

    @Override
    public void visitLocalVariable(LocalVariable var)
    {
        stack.push(var);
        var.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantPool(ConstantPool cp)
    {
        stack.push(cp);
        cp.accept(visitor);
        Constant[] constants = cp.getConstantPool();
        for (int i = 1; i < constants.length; i++)
        {
            if (constants[i] != null)
            {
                constants[i].accept(this);
            }
        }
        stack.pop();
    }

    @Override
    public void visitConstantClass(ConstantClass constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantDouble(ConstantDouble constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantFloat(ConstantFloat constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantInteger(ConstantInteger constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantInterfaceMethodref(
            ConstantInterfaceMethodref constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantInvokeDynamic(
            ConstantInvokeDynamic constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantLong(ConstantLong constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantNameAndType(ConstantNameAndType constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantString(ConstantString constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitConstantUtf8(ConstantUtf8 constant)
    {
        stack.push(constant);
        constant.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitInnerClasses(InnerClasses ic)
    {
        stack.push(ic);
        ic.accept(visitor);
        InnerClass[] ics = ic.getInnerClasses();
        for (InnerClass ic2 : ics) {
            ic2.accept(this);
        }
        stack.pop();
    }

    @Override
    public void visitInnerClass(InnerClass inner)
    {
        stack.push(inner);
        inner.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitBootstrapMethods(BootstrapMethods bm)
    {
        stack.push(bm);
        bm.accept(visitor);
        // BootstrapMethod[] bms = bm.getBootstrapMethods();
        // for (int i = 0; i < bms.length; i++)
        // {
        //     bms[i].accept(this);
        // }
        stack.pop();
    }

    @Override
    public void visitDeprecated(Deprecated attribute)
    {
        stack.push(attribute);
        attribute.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitSignature(Signature attribute)
    {
        stack.push(attribute);
        attribute.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitSourceFile(SourceFile attribute)
    {
        stack.push(attribute);
        attribute.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitSynthetic(Synthetic attribute)
    {
        stack.push(attribute);
        attribute.accept(visitor);
        stack.pop();
    }

    @Override
    public void visitUnknown(Unknown attribute)
    {
        stack.push(attribute);
        attribute.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationDefault(AnnotationDefault obj)
    {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitEnclosingMethod(EnclosingMethod obj)
    {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj)
    {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitParameterAnnotation(ParameterAnnotations obj)
    {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitMethodParameters(MethodParameters obj)
    {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodType(ConstantMethodType obj) {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodHandle(ConstantMethodHandle obj) {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

    /** @since 6.0 */
    @Override
    public void visitParameterAnnotationEntry(ParameterAnnotationEntry obj) {
        stack.push(obj);
        obj.accept(visitor);
        stack.pop();
    }

}
