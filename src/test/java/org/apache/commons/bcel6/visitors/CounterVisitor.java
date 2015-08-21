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
    public int stackMapTableCount = 0;

    /** @since 6.0 */
    public int stackMapTableEntryCount = 0;

    /** @since 6.0 */
    public int bootstrapMethodsCount = 0;

    /** @since 6.0 */
    public int methodParametersCount = 0;

    /** @since 6.0 */
    public int constantInvokeDynamic = 0;
    // CHECKSTYLE:ON


    @Override
    public void visitAnnotation(Annotations obj)
    {
        annotationCount++;
    }

    @Override
    public void visitAnnotationDefault(AnnotationDefault obj)
    {
        annotationDefaultCount++;
    }

    @Override
    public void visitAnnotationEntry(AnnotationEntry obj)
    {
        annotationEntryCount++;
    }

    @Override
    public void visitCode(Code obj)
    {
        codeCount++;
    }

    @Override
    public void visitCodeException(CodeException obj)
    {
        codeExceptionCount++;
    }

    @Override
    public void visitConstantClass(ConstantClass obj)
    {
        constantClassCount++;
    }

    @Override
    public void visitConstantDouble(ConstantDouble obj)
    {
        constantDoubleCount++;
    }

    @Override
    public void visitConstantFieldref(ConstantFieldref obj)
    {
        constantFieldrefCount++;
    }

    @Override
    public void visitConstantFloat(ConstantFloat obj)
    {
        constantFloatCount++;
    }

    @Override
    public void visitConstantInteger(ConstantInteger obj)
    {
        constantIntegerCount++;
    }

    @Override
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj)
    {
        constantInterfaceMethodrefCount++;
    }

    @Override
    public void visitConstantLong(ConstantLong obj)
    {
        constantLongCount++;
    }

    @Override
    public void visitConstantMethodref(ConstantMethodref obj)
    {
        constantMethodrefCount++;
    }

    @Override
    public void visitConstantNameAndType(ConstantNameAndType obj)
    {
        constantNameAndTypeCount++;
    }

    @Override
    public void visitConstantPool(ConstantPool obj)
    {
        constantPoolCount++;
    }

    @Override
    public void visitConstantString(ConstantString obj)
    {
        constantStringCount++;
    }

    @Override
    public void visitConstantUtf8(ConstantUtf8 obj)
    {
        constantUtf8Count++;
    }

    @Override
    public void visitConstantValue(ConstantValue obj)
    {
        constantValueCount++;
    }

    @Override
    public void visitDeprecated(Deprecated obj)
    {
        deprecatedCount++;
    }

    @Override
    public void visitEnclosingMethod(EnclosingMethod obj)
    {
        enclosingMethodCount++;
    }

    @Override
    public void visitExceptionTable(ExceptionTable obj)
    {
        exceptionTableCount++;
    }

    @Override
    public void visitField(Field obj)
    {
        fieldCount++;
    }

    @Override
    public void visitInnerClass(InnerClass obj)
    {
        innerClassCount++;
    }

    @Override
    public void visitInnerClasses(InnerClasses obj)
    {
        innerClassesCount++;
    }

    @Override
    public void visitJavaClass(JavaClass obj)
    {
        javaClassCount++;
    }

    @Override
    public void visitLineNumber(LineNumber obj)
    {
        lineNumberCount++;
    }

    @Override
    public void visitLineNumberTable(LineNumberTable obj)
    {
        lineNumberTableCount++;
    }

    @Override
    public void visitLocalVariable(LocalVariable obj)
    {
        localVariableCount++;
    }

    @Override
    public void visitLocalVariableTable(LocalVariableTable obj)
    {
        localVariableTableCount++;
    }

    @Override
    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj)
    {
        localVariableTypeTableCount++;
    }

    @Override
    public void visitMethod(Method obj)
    {
        methodCount++;
    }

    @Override
    public void visitParameterAnnotation(ParameterAnnotations obj)
    {
        parameterAnnotationCount++;
    }

    @Override
    public void visitSignature(Signature obj)
    {
        signatureAnnotationCount++;
    }

    @Override
    public void visitSourceFile(SourceFile obj)
    {
        sourceFileCount++;
    }

    @Override
    public void visitStackMap(StackMap obj)
    {
        stackMapCount++;
    }

    @Override
    public void visitStackMapEntry(StackMapEntry obj)
    {
        stackMapEntryCount++;
    }

    @Override
    public void visitSynthetic(Synthetic obj)
    {
        syntheticCount++;
    }

    @Override
    public void visitUnknown(Unknown obj)
    {
        unknownCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitBootstrapMethods(BootstrapMethods obj)
    {
        bootstrapMethodsCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitMethodParameters(MethodParameters obj)
    {
        methodParametersCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj)
    {
        constantInvokeDynamic++;
    }
}
