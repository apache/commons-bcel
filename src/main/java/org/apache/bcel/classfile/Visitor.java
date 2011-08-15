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
package org.apache.bcel.classfile;

/**
 * Interface to make use of the Visitor pattern programming style. I.e. a class
 * that implements this interface can traverse the contents of a Java class just
 * by calling the `accept' method which all classes have.
 * 
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public interface Visitor
{
	void visitCode(Code obj);

	void visitCodeException(CodeException obj);

	void visitConstantClass(ConstantClass obj);

	void visitConstantDouble(ConstantDouble obj);

	void visitConstantFieldref(ConstantFieldref obj);

	void visitConstantFloat(ConstantFloat obj);

	void visitConstantInteger(ConstantInteger obj);

	void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj);

	void visitConstantLong(ConstantLong obj);

	void visitConstantMethodref(ConstantMethodref obj);

	void visitConstantNameAndType(ConstantNameAndType obj);

	void visitConstantPool(ConstantPool obj);

	void visitConstantString(ConstantString obj);

	void visitConstantUtf8(ConstantUtf8 obj);

	void visitConstantValue(ConstantValue obj);

	void visitDeprecated(Deprecated obj);

	void visitExceptionTable(ExceptionTable obj);

	void visitField(Field obj);

	void visitInnerClass(InnerClass obj);

	void visitInnerClasses(InnerClasses obj);

	void visitJavaClass(JavaClass obj);

	void visitLineNumber(LineNumber obj);

	void visitLineNumberTable(LineNumberTable obj);

	void visitLocalVariable(LocalVariable obj);

	void visitLocalVariableTable(LocalVariableTable obj);

	void visitMethod(Method obj);

	void visitSignature(Signature obj);

	void visitSourceFile(SourceFile obj);

	void visitSynthetic(Synthetic obj);

	void visitUnknown(Unknown obj);

	void visitStackMap(StackMap obj);

	void visitStackMapEntry(StackMapEntry obj);

	void visitStackMapTable(StackMapTable obj);

	void visitStackMapTableEntry(StackMapTableEntry obj);

	void visitAnnotation(Annotations obj);

	void visitParameterAnnotation(ParameterAnnotations obj);

	void visitAnnotationEntry(AnnotationEntry obj);

	void visitAnnotationDefault(AnnotationDefault obj);

	void visitLocalVariableTypeTable(LocalVariableTypeTable obj);

	void visitEnclosingMethod(EnclosingMethod obj);
}
