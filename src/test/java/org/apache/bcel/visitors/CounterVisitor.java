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

package org.apache.bcel.visitors;

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
import org.apache.bcel.classfile.ConstantLong;
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
import org.apache.bcel.classfile.ParameterAnnotations;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapTable;
import org.apache.bcel.classfile.StackMapTableEntry;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;

public class CounterVisitor implements Visitor
{
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

    public int stackMapTableCount = 0;

    public int stackMapTableEntryCount = 0;

    public int bootstrapMethodsCount = 0;

    public int methodParametersCount = 0;


    public void visitAnnotation(Annotations obj)
    {
        annotationCount++;
    }

    public void visitAnnotationDefault(AnnotationDefault obj)
    {
        annotationDefaultCount++;
    }

    public void visitAnnotationEntry(AnnotationEntry obj)
    {
        annotationEntryCount++;
    }

    public void visitCode(Code obj)
    {
        codeCount++;
    }

    public void visitCodeException(CodeException obj)
    {
        codeExceptionCount++;
    }

    public void visitConstantClass(ConstantClass obj)
    {
        constantClassCount++;
    }

    public void visitConstantDouble(ConstantDouble obj)
    {
        constantDoubleCount++;
    }

    public void visitConstantFieldref(ConstantFieldref obj)
    {
        constantFieldrefCount++;
    }

    public void visitConstantFloat(ConstantFloat obj)
    {
        constantFloatCount++;
    }

    public void visitConstantInteger(ConstantInteger obj)
    {
        constantIntegerCount++;
    }

    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj)
    {
        constantInterfaceMethodrefCount++;
    }

    public void visitConstantLong(ConstantLong obj)
    {
        constantLongCount++;
    }

    public void visitConstantMethodref(ConstantMethodref obj)
    {
        constantMethodrefCount++;
    }

    public void visitConstantNameAndType(ConstantNameAndType obj)
    {
        constantNameAndTypeCount++;
    }

    public void visitConstantPool(ConstantPool obj)
    {
        constantPoolCount++;
    }

    public void visitConstantString(ConstantString obj)
    {
        constantStringCount++;
    }

    public void visitConstantUtf8(ConstantUtf8 obj)
    {
        constantUtf8Count++;
    }

    public void visitConstantValue(ConstantValue obj)
    {
        constantValueCount++;
    }

    public void visitDeprecated(Deprecated obj)
    {
        deprecatedCount++;
    }

    public void visitEnclosingMethod(EnclosingMethod obj)
    {
        enclosingMethodCount++;
    }

    public void visitExceptionTable(ExceptionTable obj)
    {
        exceptionTableCount++;
    }

    public void visitField(Field obj)
    {
        fieldCount++;
    }

    public void visitInnerClass(InnerClass obj)
    {
        innerClassCount++;
    }

    public void visitInnerClasses(InnerClasses obj)
    {
        innerClassesCount++;
    }

    public void visitJavaClass(JavaClass obj)
    {
        javaClassCount++;
    }

    public void visitLineNumber(LineNumber obj)
    {
        lineNumberCount++;
    }

    public void visitLineNumberTable(LineNumberTable obj)
    {
        lineNumberTableCount++;
    }

    public void visitLocalVariable(LocalVariable obj)
    {
        localVariableCount++;
    }

    public void visitLocalVariableTable(LocalVariableTable obj)
    {
        localVariableTableCount++;
    }

    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj)
    {
        localVariableTypeTableCount++;
    }

    public void visitMethod(Method obj)
    {
        methodCount++;
    }

    public void visitParameterAnnotation(ParameterAnnotations obj)
    {
        parameterAnnotationCount++;
    }

    public void visitSignature(Signature obj)
    {
        signatureAnnotationCount++;
    }

    public void visitSourceFile(SourceFile obj)
    {
        sourceFileCount++;
    }

    public void visitStackMap(StackMap obj)
    {
        stackMapCount++;
    }

    public void visitStackMapEntry(StackMapEntry obj)
    {
        stackMapEntryCount++;
    }

    public void visitSynthetic(Synthetic obj)
    {
        syntheticCount++;
    }

    public void visitUnknown(Unknown obj)
    {
        unknownCount++;
    }

    public void visitStackMapTable(StackMapTable obj)
    {
        stackMapTableCount++;
    }

    public void visitStackMapTableEntry(StackMapTableEntry obj)
    {
        stackMapTableEntryCount++;
    }

    public void visitBootstrapMethods(BootstrapMethods obj)
    {
        bootstrapMethodsCount++;
    }

    public void visitMethodParameters(MethodParameters obj)
    {
        methodParametersCount++;
    }
}
