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
import org.apache.bcel.classfile.ConstantDynamic;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantInvokeDynamic;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodHandle;
import org.apache.bcel.classfile.ConstantMethodType;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantModule;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPackage;
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
import org.apache.bcel.classfile.MethodParameter;
import org.apache.bcel.classfile.MethodParameters;
import org.apache.bcel.classfile.Module;
import org.apache.bcel.classfile.ModuleExports;
import org.apache.bcel.classfile.ModuleMainClass;
import org.apache.bcel.classfile.ModuleOpens;
import org.apache.bcel.classfile.ModulePackages;
import org.apache.bcel.classfile.ModuleProvides;
import org.apache.bcel.classfile.ModuleRequires;
import org.apache.bcel.classfile.NestHost;
import org.apache.bcel.classfile.NestMembers;
import org.apache.bcel.classfile.ParameterAnnotationEntry;
import org.apache.bcel.classfile.ParameterAnnotations;
import org.apache.bcel.classfile.Record;
import org.apache.bcel.classfile.RecordComponentInfo;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;

public class CountingVisitor implements Visitor {
    // CHECKSTYLE:OFF (public mutable fields in test code)
    public int unknownCount;

    public int syntheticCount;

    public int stackMapEntryCount;

    public int stackMapTypeCount;

    public int stackMapCount;

    public int sourceFileCount;

    public int signatureAnnotationCount;

    public int parameterAnnotationCount;

    public int methodCount;

    public int localVariableTypeTableCount;

    public int localVariableTableCount;

    public int localVariableCount;

    public int lineNumberTableCount;

    public int lineNumberCount;

    public int javaClassCount;

    public int innerClassesCount;

    public int innerClassCount;

    public int fieldCount;

    public int exceptionTableCount;

    public int enclosingMethodCount;

    public int deprecatedCount;

    public int constantValueCount;

    public int constantUtf8Count;

    public int constantStringCount;

    public int constantNameAndTypeCount;

    public int constantPoolCount;

    public int constantMethodrefCount;

    public int constantLongCount;

    public int constantIntegerCount;

    public int constantInterfaceMethodrefCount;

    public int constantFloatCount;

    public int constantFieldrefCount;

    public int constantClassCount;

    public int constantDoubleCount;

    public int codeExceptionCount;

    public int codeCount;

    public int annotationEntryCount;

    public int annotationDefaultCount;

    public int annotationCount;

    /** @since 6.0 */
    public int bootstrapMethodsCount;

    /** @since 6.0 */
    public int methodParameterCount;

    /** @since 6.0 */
    public int methodParametersCount;

    /** @since 6.0 */
    public int constantInvokeDynamic;

    /** @since 6.1 */
    public int constantModuleCount;

    /** @since 6.1 */
    public int constantPackageCount;

    /** @since 6.3 */
    public int constantDynamicCount;

    /** @since 6.4.0 */
    public int moduleCount;

    /** @since 6.4.0 */
    public int moduleExportsCount;

    /** @since 6.4.0 */
    public int moduleOpensCount;

    /** @since 6.4.0 */
    public int moduleProvidesCount;

    /** @since 6.4.0 */
    public int moduleRequiresCount;

    /** @since 6.4.0 */
    public int moduleMainClassCount;

    /** @since 6.4.0 */
    public int modulePackagesCount;

    /** @since 6.4.0 */
    public int nestHostCount;

    /** @since 6.4.0 */
    public int nestMembersCount;

    /** @since 6.9.0 */
    public int recordCount;

    /** @since 6.9.0 */
    public int recordComponentCount;

    // CHECKSTYLE:ON

    @Override
    public void visitAnnotation(final Annotations obj) {
        annotationCount++;
    }

    @Override
    public void visitAnnotationDefault(final AnnotationDefault obj) {
        annotationDefaultCount++;
    }

    @Override
    public void visitAnnotationEntry(final AnnotationEntry obj) {
        annotationEntryCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitBootstrapMethods(final BootstrapMethods obj) {
        bootstrapMethodsCount++;
    }

    @Override
    public void visitCode(final Code obj) {
        codeCount++;
    }

    @Override
    public void visitCodeException(final CodeException obj) {
        codeExceptionCount++;
    }

    @Override
    public void visitConstantClass(final ConstantClass obj) {
        constantClassCount++;
    }

    @Override
    public void visitConstantDouble(final ConstantDouble obj) {
        constantDoubleCount++;
    }

    /** @since 6.3 */
    @Override
    public void visitConstantDynamic(final ConstantDynamic constantDynamic) {
        constantDynamicCount++;
    }

    @Override
    public void visitConstantFieldref(final ConstantFieldref obj) {
        constantFieldrefCount++;
    }

    @Override
    public void visitConstantFloat(final ConstantFloat obj) {
        constantFloatCount++;
    }

    @Override
    public void visitConstantInteger(final ConstantInteger obj) {
        constantIntegerCount++;
    }

    @Override
    public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj) {
        constantInterfaceMethodrefCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantInvokeDynamic(final ConstantInvokeDynamic obj) {
        constantInvokeDynamic++;
    }

    @Override
    public void visitConstantLong(final ConstantLong obj) {
        constantLongCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodHandle(final ConstantMethodHandle constantMethodHandle) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visitConstantMethodref(final ConstantMethodref obj) {
        constantMethodrefCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitConstantMethodType(final ConstantMethodType obj) {
        // TODO Auto-generated method stub
    }

    /** @since 6.1 */
    @Override
    public void visitConstantModule(final ConstantModule constantModule) {
        constantModuleCount++;
    }

    @Override
    public void visitConstantNameAndType(final ConstantNameAndType obj) {
        constantNameAndTypeCount++;
    }

    /** @since 6.1 */
    @Override
    public void visitConstantPackage(final ConstantPackage constantPackage) {
        constantPackageCount++;
    }

    @Override
    public void visitConstantPool(final ConstantPool obj) {
        constantPoolCount++;
    }

    @Override
    public void visitConstantString(final ConstantString obj) {
        constantStringCount++;
    }

    @Override
    public void visitConstantUtf8(final ConstantUtf8 obj) {
        constantUtf8Count++;
    }

    @Override
    public void visitConstantValue(final ConstantValue obj) {
        constantValueCount++;
    }

    @Override
    public void visitDeprecated(final Deprecated obj) {
        deprecatedCount++;
    }

    @Override
    public void visitEnclosingMethod(final EnclosingMethod obj) {
        enclosingMethodCount++;
    }

    @Override
    public void visitExceptionTable(final ExceptionTable obj) {
        exceptionTableCount++;
    }

    @Override
    public void visitField(final Field obj) {
        fieldCount++;
    }

    @Override
    public void visitInnerClass(final InnerClass obj) {
        innerClassCount++;
    }

    @Override
    public void visitInnerClasses(final InnerClasses obj) {
        innerClassesCount++;
    }

    @Override
    public void visitJavaClass(final JavaClass obj) {
        javaClassCount++;
    }

    @Override
    public void visitLineNumber(final LineNumber obj) {
        lineNumberCount++;
    }

    @Override
    public void visitLineNumberTable(final LineNumberTable obj) {
        lineNumberTableCount++;
    }

    @Override
    public void visitLocalVariable(final LocalVariable obj) {
        localVariableCount++;
    }

    @Override
    public void visitLocalVariableTable(final LocalVariableTable obj) {
        localVariableTableCount++;
    }

    @Override
    public void visitLocalVariableTypeTable(final LocalVariableTypeTable obj) {
        localVariableTypeTableCount++;
    }

    @Override
    public void visitMethod(final Method obj) {
        methodCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitMethodParameter(final MethodParameter obj) {
        methodParameterCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitMethodParameters(final MethodParameters obj) {
        methodParametersCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModule(final Module obj) {
        moduleCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModuleExports(final ModuleExports obj) {
        moduleExportsCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModuleMainClass(final ModuleMainClass obj) {
        moduleMainClassCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModuleOpens(final ModuleOpens obj) {
        moduleOpensCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModulePackages(final ModulePackages obj) {
        modulePackagesCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModuleProvides(final ModuleProvides obj) {
        moduleProvidesCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitModuleRequires(final ModuleRequires obj) {
        moduleRequiresCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitNestHost(final NestHost obj) {
        nestHostCount++;
    }

    /** @since 6.4.0 */
    @Override
    public void visitNestMembers(final NestMembers obj) {
        nestMembersCount++;
    }

    @Override
    public void visitParameterAnnotation(final ParameterAnnotations obj) {
        parameterAnnotationCount++;
    }

    /** @since 6.0 */
    @Override
    public void visitParameterAnnotationEntry(final ParameterAnnotationEntry parameterAnnotationEntry) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visitRecord(Record v) {
        recordCount++;
    }

    @Override
    public void visitRecordComponent(RecordComponentInfo v) {
        recordComponentCount++;
    }

    @Override
    public void visitSignature(final Signature obj) {
        signatureAnnotationCount++;
    }

    @Override
    public void visitSourceFile(final SourceFile obj) {
        sourceFileCount++;
    }

    @Override
    public void visitStackMap(final StackMap obj) {
        stackMapCount++;
    }

    @Override
    public void visitStackMapEntry(final StackMapEntry obj) {
        stackMapEntryCount++;
    }

    /**
     * @since 6.8.0
     */
    @Override
    public void visitStackMapType(final StackMapType obj) {
        stackMapTypeCount++;
    }

    @Override
    public void visitSynthetic(final Synthetic obj) {
        syntheticCount++;
    }

    @Override
    public void visitUnknown(final Unknown obj) {
        unknownCount++;
    }
}
