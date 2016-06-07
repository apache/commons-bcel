/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.commons.bcel6.visitors;

import org.apache.commons.bcel6.classfile.AnnotationDefault;
import org.apache.commons.bcel6.classfile.AnnotationEntry;
import org.apache.commons.bcel6.classfile.Annotations;
import org.apache.commons.bcel6.classfile.BootstrapMethods;
import org.apache.commons.bcel6.classfile.Code;
import org.apache.commons.bcel6.classfile.CodeException;
import org.apache.commons.bcel6.classfile.ConstantClass;
import org.apache.commons.bcel6.classfile.ConstantDouble;
import org.apache.commons.bcel6.classfile.ConstantFieldref;
import org.apache.commons.bcel6.classfile.ConstantFloat;
import org.apache.commons.bcel6.classfile.ConstantInteger;
import org.apache.commons.bcel6.classfile.ConstantInterfaceMethodref;
import org.apache.commons.bcel6.classfile.ConstantInvokeDynamic;
import org.apache.commons.bcel6.classfile.ConstantLong;
import org.apache.commons.bcel6.classfile.ConstantMethodHandle;
import org.apache.commons.bcel6.classfile.ConstantMethodType;
import org.apache.commons.bcel6.classfile.ConstantMethodref;
import org.apache.commons.bcel6.classfile.ConstantNameAndType;
import org.apache.commons.bcel6.classfile.ConstantPool;
import org.apache.commons.bcel6.classfile.ConstantString;
import org.apache.commons.bcel6.classfile.ConstantUtf8;
import org.apache.commons.bcel6.classfile.ConstantValue;
import org.apache.commons.bcel6.classfile.Deprecated;
import org.apache.commons.bcel6.classfile.EnclosingMethod;
import org.apache.commons.bcel6.classfile.ExceptionTable;
import org.apache.commons.bcel6.classfile.Field;
import org.apache.commons.bcel6.classfile.InnerClass;
import org.apache.commons.bcel6.classfile.InnerClasses;
import org.apache.commons.bcel6.classfile.JavaClass;
import org.apache.commons.bcel6.classfile.LineNumber;
import org.apache.commons.bcel6.classfile.LineNumberTable;
import org.apache.commons.bcel6.classfile.LocalVariable;
import org.apache.commons.bcel6.classfile.LocalVariableTable;
import org.apache.commons.bcel6.classfile.LocalVariableTypeTable;
import org.apache.commons.bcel6.classfile.Method;
import org.apache.commons.bcel6.classfile.MethodParameters;
import org.apache.commons.bcel6.classfile.ParameterAnnotationEntry;
import org.apache.commons.bcel6.classfile.ParameterAnnotations;
import org.apache.commons.bcel6.classfile.Signature;
import org.apache.commons.bcel6.classfile.SourceFile;
import org.apache.commons.bcel6.classfile.StackMap;
import org.apache.commons.bcel6.classfile.StackMapEntry;
import org.apache.commons.bcel6.classfile.Synthetic;
import org.apache.commons.bcel6.classfile.Unknown;
import org.apache.commons.bcel6.classfile.Visitor;

public class CounterVisitor implements Visitor
{
    // CHECKSTYLE:OFF (public mutable fields in test code)
    public int unknownCount = 0;

    public int syntheticCount = 0;

    public int stackMapEntryCount = 0;

    public int stackMapCount = 0;

    public int sourceFileCount = 0;

    public int signatureAnnotationCount = 0;

    public int parameterAnnotationCount = 0;

    public int methodCount = 0;

    public int localVariableTypeTableCount = 0;

    public int localVariableTableCount = 0;

    public int localVariableCount = 0;

    public int lineNumberTableCount = 0;

    public int lineNumberCount = 0;

    public int javaClassCount = 0;

    public int innerClassesCount = 0;

    public int innerClassCount = 0;

    public int fieldCount = 0;

    public int exceptionTableCount = 0;

    public int enclosingMethodCount = 0;

    public int deprecatedCount = 0;

    public int constantValueCount = 0;

    public int constantUtf8Count = 0;

    public int constantStringCount = 0;

    public int constantNameAndTypeCount = 0;

    public int constantPoolCount = 0;

    public int constantMethodrefCount = 0;

    public int constantLongCount = 0;

    public int constantIntegerCount = 0;

    public int constantInterfaceMethodrefCount = 0;

    public int constantFloatCount = 0;

    public int constantFieldrefCount = 0;

    public int constantClassCount = 0;

    public int constantDoubleCount = 0;

    public int codeExceptionCount = 0;

    public int codeCount = 0;

    public int annotationEntryCount = 0;

    public int annotationDefaultCount = 0;

    public int annotationCount = 0;

    /** @since 6.0 */
    public int bootstrapMethodsCount = 0;

    /** @since 6.0 */
    public int methodParametersCount = 0;

    /** @since 6.0 */
    public int constantInvokeDynamic = 0;
    // CHECKSTYLE:ON


    @Override
    public void visitAnnotation(final Annotations obj)
    {
        annotationCount++;
    }

    @Override
    public void visitAnnotationDefault(final AnnotationDefault obj)
    {
        annotationDefaultCount++;
    }

    @Override
    public void visitAnnotationEntry(final AnnotationEntry obj)
    {
        annotationEntryCount++;
    }

    @Override
    public void visitCode(final Code obj)
    {
        codeCount++;
    }

    @Override
    public void visitCodeException(final CodeException obj)
    {
        codeExceptionCount++;
    }

    @Override
    public void visitConstantClass(final ConstantClass obj)
    {
        constantClassCount++;
    }

    @Override
    public void visitConstantDouble(final ConstantDouble obj)
    {
        constantDoubleCount++;
    }

    @Override
    public void visitConstantFieldref(final ConstantFieldref obj)
    {
        constantFieldrefCount++;
    }

    @Override
    public void visitConstantFloat(final ConstantFloat obj)
    {
        constantFloatCount++;
    }

    @Override
    public void visitConstantInteger(final ConstantInteger obj)
    {
        constantIntegerCount++;
    }

    @Override
    public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj)
    {
        constantInterfaceMethodrefCount++;
    }

    @Override
    public void visitConstantLong(final ConstantLong obj)
    {
        constantLongCount++;
    }

    @Override
    public void visitConstantMethodref(final ConstantMethodref obj)
    {
        constantMethodrefCount++;
    }

    @Override
    public void visitConstantNameAndType(final ConstantNameAndType obj)
    {
        constantNameAndTypeCount++;
    }

    @Override
    public void visitConstantPool(final ConstantPool obj)
    {
        constantPoolCount++;
    }

    @Override
    public void visitConstantString(final ConstantString obj)
    {
        constantStringCount++;
    }

    @Override
    public void visitConstantUtf8(final ConstantUtf8 obj)
    {
        constantUtf8Count++;
    }

    @Override
    public void visitConstantValue(final ConstantValue obj)
    {
        constantValueCount++;
    }

    @Override
    public void visitDeprecated(final Deprecated obj)
    {
        deprecatedCount++;
    }

    @Override
    public void visitEnclosingMethod(final EnclosingMethod obj)
    {
        enclosingMethodCount++;
    }

    @Override
    public void visitExceptionTable(final ExceptionTable obj)
    {
        exceptionTableCount++;
    }

    @Override
    public void visitField(final Field obj)
    {
        fieldCount++;
    }

    @Override
    public void visitInnerClass(final InnerClass obj)
    {
        innerClassCount++;
    }

    @Override
    public void visitInnerClasses(final InnerClasses obj)
    {
        innerClassesCount++;
    }

    @Override
    public void visitJavaClass(final JavaClass obj)
    {
        javaClassCount++;
    }

    @Override
    public void visitLineNumber(final LineNumber obj)
    {
        lineNumberCount++;
    }

    @Override
    public void visitLineNumberTable(final LineNumberTable obj)
    {
        lineNumberTableCount++;
    }

    @Override
    public void visitLocalVariable(final LocalVariable obj)
    {
        localVariableCount++;
    }

    @Override
    public void visitLocalVariableTable(final LocalVariableTable obj)
    {
        localVariableTableCount++;
    }

    @Override
    public void visitLocalVariableTypeTable(final LocalVariableTypeTable obj)
    {
        localVariableTypeTableCount++;
    }

    @Override
    public void visitMethod(final Method obj)
    {
        methodCount++;
    }

    @Override
    public void visitParameterAnnotation(final ParameterAnnotations obj)
    {
        parameterAnnotationCount++;
    }

    @Override
    public void visitSignature(final Signature obj)
    {
        signatureAnnotationCount++;
    }

    @Override
    public void visitSourceFile(final SourceFile obj)
    {
        sourceFileCount++;
    }

    @Override
    public void visitStackMap(final StackMap obj)
    {
        stackMapCount++;
    }

    @Override
    public void visitStackMapEntry(final StackMapEntry obj)
    {
        stackMapEntryCount++;
    }

    @Override
    public void visitSynthetic(final Synthetic obj)
    {
        syntheticCount++;
    }

    @Override
    public void visitUnknown(final Unknown obj)
    {
        unknownCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitBootstrapMethods(final BootstrapMethods obj)
    {
        bootstrapMethodsCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitMethodParameters(final MethodParameters obj)
    {
        methodParametersCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantInvokeDynamic(final ConstantInvokeDynamic obj)
    {
        constantInvokeDynamic++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodType(final ConstantMethodType obj) {
        // TODO Auto-generated method stub        
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodHandle(final ConstantMethodHandle constantMethodHandle) {
        // TODO Auto-generated method stub
    }

    /** @since 6.0 */
    @Override
    public void visitParameterAnnotationEntry(final ParameterAnnotationEntry parameterAnnotationEntry) {
        // TODO Auto-generated method stub        
    }
}
