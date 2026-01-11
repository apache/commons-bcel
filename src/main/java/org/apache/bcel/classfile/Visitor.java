/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.classfile;

/**
 * Interface to make use of the Visitor pattern programming style. I.e. a class that implements this interface can
 * traverse the contents of a Java class just by calling the 'accept' method which all classes have.
 */
public interface Visitor {

    /**
     * @since 6.0
     */
    void visitAnnotation(Annotations obj);

    /**
     * @since 6.0
     */
    void visitAnnotationDefault(AnnotationDefault obj);

    /**
     * @since 6.0
     */
    void visitAnnotationEntry(AnnotationEntry obj);

    /**
     * @since 6.0
     */
    void visitBootstrapMethods(BootstrapMethods obj);

    /**
     * Visits a Code attribute.
     *
     * @param obj the attribute.
     */
    void visitCode(Code obj);

    /**
     * Visits a CodeException.
     *
     * @param obj the exception.
     */
    void visitCodeException(CodeException obj);

    /**
     * Visits a ConstantClass.
     *
     * @param obj the constant.
     */
    void visitConstantClass(ConstantClass obj);

    /**
     * Visits a ConstantDouble.
     *
     * @param obj the constant.
     */
    void visitConstantDouble(ConstantDouble obj);

    /**
     * @since 6.3
     */
    default void visitConstantDynamic(final ConstantDynamic constantDynamic) {
        // empty
    }

    /**
     * Visits a ConstantFieldref.
     *
     * @param obj the constant.
     */
    void visitConstantFieldref(ConstantFieldref obj);

    /**
     * Visits a ConstantFloat.
     *
     * @param obj the constant.
     */
    void visitConstantFloat(ConstantFloat obj);

    /**
     * Visits a ConstantInteger.
     *
     * @param obj the constant.
     */
    void visitConstantInteger(ConstantInteger obj);

    /**
     * Visits a ConstantInterfaceMethodref.
     *
     * @param obj the constant.
     */
    void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj);

    /**
     * Visits a ConstantInvokeDynamic.
     *
     * @param obj the constant.
     */
    void visitConstantInvokeDynamic(ConstantInvokeDynamic obj);

    /**
     * Visits a ConstantLong.
     *
     * @param obj the constant.
     */
    void visitConstantLong(ConstantLong obj);

    /**
     * @since 6.0
     */
    void visitConstantMethodHandle(ConstantMethodHandle obj);

    /**
     * Visits a ConstantMethodref.
     *
     * @param obj the constant.
     */
    void visitConstantMethodref(ConstantMethodref obj);

    /**
     * @since 6.0
     */
    void visitConstantMethodType(ConstantMethodType obj);

    /**
     * @since 6.1
     */
    void visitConstantModule(ConstantModule constantModule);

    /**
     * Visits a ConstantNameAndType.
     *
     * @param obj the constant.
     */
    void visitConstantNameAndType(ConstantNameAndType obj);

    /**
     * @since 6.1
     */
    void visitConstantPackage(ConstantPackage constantPackage);

    /**
     * Visits a ConstantPool.
     *
     * @param obj the constant pool.
     */
    void visitConstantPool(ConstantPool obj);

    /**
     * Visits a ConstantString.
     *
     * @param obj the constant.
     */
    void visitConstantString(ConstantString obj);

    /**
     * Visits a ConstantUtf8.
     *
     * @param obj the constant.
     */
    void visitConstantUtf8(ConstantUtf8 obj);

    /**
     * Visits a ConstantValue attribute.
     *
     * @param obj the attribute.
     */
    void visitConstantValue(ConstantValue obj);

    /**
     * Visits a Deprecated attribute.
     *
     * @param obj the attribute.
     */
    void visitDeprecated(Deprecated obj);

    /**
     * @since 6.0
     */
    void visitEnclosingMethod(EnclosingMethod obj);

    /**
     * Visits an ExceptionTable attribute.
     *
     * @param obj the attribute.
     */
    void visitExceptionTable(ExceptionTable obj);

    /**
     * Visits a Field.
     *
     * @param obj the field.
     */
    void visitField(Field obj);

    /**
     * Visits an InnerClass.
     *
     * @param obj the inner class.
     */
    void visitInnerClass(InnerClass obj);

    /**
     * Visits an InnerClasses attribute.
     *
     * @param obj the attribute.
     */
    void visitInnerClasses(InnerClasses obj);

    /**
     * Visits a JavaClass.
     *
     * @param obj the class.
     */
    void visitJavaClass(JavaClass obj);

    /**
     * Visits a LineNumber.
     *
     * @param obj the line number.
     */
    void visitLineNumber(LineNumber obj);

    /**
     * Visits a LineNumberTable attribute.
     *
     * @param obj the attribute.
     */
    void visitLineNumberTable(LineNumberTable obj);

    /**
     * Visits a LocalVariable.
     *
     * @param obj the local variable.
     */
    void visitLocalVariable(LocalVariable obj);

    /**
     * Visits a LocalVariableTable attribute.
     *
     * @param obj the attribute.
     */
    void visitLocalVariableTable(LocalVariableTable obj);

    /**
     * @since 6.0
     */
    void visitLocalVariableTypeTable(LocalVariableTypeTable obj);

    /**
     * Visits a Method.
     *
     * @param obj the method.
     */
    void visitMethod(Method obj);

    /**
     * @since 6.4.0
     */
    default void visitMethodParameter(final MethodParameter obj) {
        // empty
    }

    /**
     * @since 6.0
     */
    void visitMethodParameters(MethodParameters obj);

    /**
     * @since 6.4.0
     */
    default void visitModule(final Module constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModuleExports(final ModuleExports constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModuleMainClass(final ModuleMainClass obj) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModuleOpens(final ModuleOpens constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModulePackages(final ModulePackages constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModuleProvides(final ModuleProvides constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitModuleRequires(final ModuleRequires constantModule) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitNestHost(final NestHost obj) {
        // empty
    }

    /**
     * @since 6.4.0
     */
    default void visitNestMembers(final NestMembers obj) {
        // empty
    }

    /**
     * @since 6.0
     */
    void visitParameterAnnotation(ParameterAnnotations obj);

    /**
     * @since 6.0
     */
    void visitParameterAnnotationEntry(ParameterAnnotationEntry obj);

    /**
     * Visits a {@link Record} object.
     *
     * @param obj Record to visit
     * @since 6.9.0
     */
    default void visitRecord(final Record obj) {
        // empty
    }

    /**
     * Visits a {@link RecordComponentInfo} object.
     *
     * @param record component to visit
     * @since 6.9.0
     */
    default void visitRecordComponent(final RecordComponentInfo record) {
     // noop
    }

    /**
     * Visits a Signature attribute.
     *
     * @param obj the attribute.
     */
    void visitSignature(Signature obj);

    /**
     * Visits a SourceFile attribute.
     *
     * @param obj the attribute.
     */
    void visitSourceFile(SourceFile obj);

    /**
     * Visits a StackMap attribute.
     *
     * @param obj the attribute.
     */
    void visitStackMap(StackMap obj);

    /**
     * Visits a StackMapEntry.
     *
     * @param obj the entry.
     */
    void visitStackMapEntry(StackMapEntry obj);

    /**
     * Visits a {@link StackMapType} object.
     *
     * @param obj object to visit
     * @since 6.8.0
     */
    default void visitStackMapType(final StackMapType obj) {
      // empty
    }

    /**
     * Visits a Synthetic attribute.
     *
     * @param obj the attribute.
     */
    void visitSynthetic(Synthetic obj);

    /**
     * Visits an Unknown attribute.
     *
     * @param obj the attribute.
     */
    void visitUnknown(Unknown obj);

}
