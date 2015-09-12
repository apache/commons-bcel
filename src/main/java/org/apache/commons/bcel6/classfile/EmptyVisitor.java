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

/**
 * Visitor with empty method bodies, can be extended and used in conjunction
 * with the DescendingVisitor class, e.g. By courtesy of David Spencer.
 * 
 * @see DescendingVisitor
 * @version $Id$
 */
public class EmptyVisitor implements Visitor
{
    protected EmptyVisitor()
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotation(Annotations obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitParameterAnnotation(ParameterAnnotations obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationEntry(AnnotationEntry obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitAnnotationDefault(AnnotationDefault obj)
    {
    }

    @Override
    public void visitCode(Code obj)
    {
    }

    @Override
    public void visitCodeException(CodeException obj)
    {
    }

    @Override
    public void visitConstantClass(ConstantClass obj)
    {
    }

    @Override
    public void visitConstantDouble(ConstantDouble obj)
    {
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref obj)
    {
    }

    @Override
    public void visitConstantFloat(ConstantFloat obj)
    {
    }

    @Override
    public void visitConstantInteger(ConstantInteger obj)
    {
    }

    @Override
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj)
    {
    }

    @Override
    public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj)
    {
    }

    @Override
    public void visitConstantLong(ConstantLong obj)
    {
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref obj)
    {
    }

    @Override
    public void visitConstantNameAndType(ConstantNameAndType obj)
    {
    }

    @Override
    public void visitConstantPool(ConstantPool obj)
    {
    }

    @Override
    public void visitConstantString(ConstantString obj)
    {
    }

    @Override
    public void visitConstantUtf8(ConstantUtf8 obj)
    {
    }

    @Override
    public void visitConstantValue(ConstantValue obj)
    {
    }

    @Override
    public void visitDeprecated(Deprecated obj)
    {
    }

    @Override
    public void visitExceptionTable(ExceptionTable obj)
    {
    }

    @Override
    public void visitField(Field obj)
    {
    }

    @Override
    public void visitInnerClass(InnerClass obj)
    {
    }

    @Override
    public void visitInnerClasses(InnerClasses obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitBootstrapMethods(BootstrapMethods obj)
    {
    }

    @Override
    public void visitJavaClass(JavaClass obj)
    {
    }

    @Override
    public void visitLineNumber(LineNumber obj)
    {
    }

    @Override
    public void visitLineNumberTable(LineNumberTable obj)
    {
    }

    @Override
    public void visitLocalVariable(LocalVariable obj)
    {
    }

    @Override
    public void visitLocalVariableTable(LocalVariableTable obj)
    {
    }

    @Override
    public void visitMethod(Method obj)
    {
    }

    @Override
    public void visitSignature(Signature obj)
    {
    }

    @Override
    public void visitSourceFile(SourceFile obj)
    {
    }

    @Override
    public void visitSynthetic(Synthetic obj)
    {
    }

    @Override
    public void visitUnknown(Unknown obj)
    {
    }

    @Override
    public void visitStackMap(StackMap obj)
    {
    }

    @Override
    public void visitStackMapEntry(StackMapEntry obj)
    {
    }

    /**
     * @since 6.0
    @Override
    public void visitStackMapTable(StackMapTable obj)
    {
    }
     */

    /**
     * @since 6.0
    @Override
    public void visitStackMapTableEntry(StackMapTableEntry obj)
    {
    }
     */

    /**
     * @since 6.0
     */
    @Override
    public void visitEnclosingMethod(EnclosingMethod obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitMethodParameters(MethodParameters obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantMethodType(ConstantMethodType obj)
    {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitConstantMethodHandle(ConstantMethodHandle constantMethodHandle) {
    }

    /**
     * @since 6.0
     */
    @Override
    public void visitParameterAnnotationEntry(ParameterAnnotationEntry parameterAnnotationEntry) {
    }
}
