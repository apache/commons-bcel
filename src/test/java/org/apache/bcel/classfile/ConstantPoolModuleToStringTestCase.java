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
package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@code module-info.class} files.
 */
public class ConstantPoolModuleToStringTestCase {

    static class ToStringVisitor implements Visitor {

        private final StringBuilder stringBuilder = new StringBuilder();
        private final ConstantPool pool;
        private int count;

        public ToStringVisitor(final ConstantPool pool) {
            this.pool = pool;
        }

        private void append(final Object obj) {
            if (!(stringBuilder.length() == 0)) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(obj);
        }

        @Override
        public String toString() {
            return "ToStringVisitor [count=" + count + ", stringBuilder=" + stringBuilder + ", pool=" + pool + "]";
        }

        @Override
        public void visitAnnotation(final Annotations obj) {
            append(obj);
        }

        @Override
        public void visitAnnotationDefault(final AnnotationDefault obj) {
            append(obj);
        }

        @Override
        public void visitAnnotationEntry(final AnnotationEntry obj) {
            append(obj);
        }

        @Override
        public void visitBootstrapMethods(final BootstrapMethods obj) {
            append(obj);
        }

        @Override
        public void visitCode(final Code obj) {
            append(obj.toString(true));
        }

        @Override
        public void visitCodeException(final CodeException obj) {
            append(obj.toString(pool, true));
        }

        @Override
        public void visitConstantClass(final ConstantClass obj) {
            append(obj);
        }

        @Override
        public void visitConstantDouble(final ConstantDouble obj) {
            append(obj);
        }

        @Override
        public void visitConstantDynamic(final ConstantDynamic constantDynamic) {
            append(constantDynamic);
        }

        @Override
        public void visitConstantFieldref(final ConstantFieldref obj) {
            append(obj);
        }

        @Override
        public void visitConstantFloat(final ConstantFloat obj) {
            append(obj);
        }

        @Override
        public void visitConstantInteger(final ConstantInteger obj) {
            append(obj);
        }

        @Override
        public void visitConstantInterfaceMethodref(final ConstantInterfaceMethodref obj) {
            append(obj);
        }

        @Override
        public void visitConstantInvokeDynamic(final ConstantInvokeDynamic obj) {
            append(obj);
        }

        @Override
        public void visitConstantLong(final ConstantLong obj) {
            append(obj);
        }

        @Override
        public void visitConstantMethodHandle(final ConstantMethodHandle obj) {
            append(obj);
        }

        @Override
        public void visitConstantMethodref(final ConstantMethodref obj) {
            append(obj);
        }

        @Override
        public void visitConstantMethodType(final ConstantMethodType obj) {
            append(obj);
        }

        @Override
        public void visitConstantModule(final ConstantModule constantModule) {
            append(constantModule);
        }

        @Override
        public void visitConstantNameAndType(final ConstantNameAndType obj) {
            append(obj);
        }

        @Override
        public void visitConstantPackage(final ConstantPackage constantPackage) {
            append(constantPackage);
        }

        @Override
        public void visitConstantPool(final ConstantPool obj) {
            append(obj);
        }

        @Override
        public void visitConstantString(final ConstantString obj) {
            append(obj);
        }

        @Override
        public void visitConstantUtf8(final ConstantUtf8 obj) {
            append(obj);
        }

        @Override
        public void visitConstantValue(final ConstantValue obj) {
            append(obj);
        }

        @Override
        public void visitDeprecated(final Deprecated obj) {
            append(obj);
        }

        @Override
        public void visitEnclosingMethod(final EnclosingMethod obj) {
            append(obj);
        }

        @Override
        public void visitExceptionTable(final ExceptionTable obj) {
            append(obj);
        }

        @Override
        public void visitField(final Field obj) {
            append(obj);
        }

        @Override
        public void visitInnerClass(final InnerClass obj) {
            append(obj.toString(pool));
        }

        @Override
        public void visitInnerClasses(final InnerClasses obj) {
            append(obj);
        }

        @Override
        public void visitJavaClass(final JavaClass obj) {
            append(obj);
        }

        @Override
        public void visitLineNumber(final LineNumber obj) {
            append(obj);
        }

        @Override
        public void visitLineNumberTable(final LineNumberTable obj) {
            append(obj);
        }

        @Override
        public void visitLocalVariable(final LocalVariable obj) {
            append(obj);
        }

        @Override
        public void visitLocalVariableTable(final LocalVariableTable obj) {
            append(obj);
        }

        @Override
        public void visitLocalVariableTypeTable(final LocalVariableTypeTable obj) {
            append(obj);
        }

        @Override
        public void visitMethod(final Method obj) {
            append(obj);
        }

        @Override
        public void visitMethodParameter(final MethodParameter obj) {
            append(obj);
        }

        @Override
        public void visitMethodParameters(final MethodParameters obj) {
            append(obj);
        }

        @Override
        public void visitModule(final Module constantModule) {
            append(constantModule);
        }

        @Override
        public void visitModuleExports(final ModuleExports constantModule) {
            append(constantModule);
            append(constantModule.toString(pool));
        }

        @Override
        public void visitModuleMainClass(final ModuleMainClass obj) {
            append(obj);
        }

        @Override
        public void visitModuleOpens(final ModuleOpens constantModule) {
            append(constantModule);
            append(constantModule.toString(pool));
        }

        @Override
        public void visitModulePackages(final ModulePackages constantModule) {
            append(constantModule);
        }

        @Override
        public void visitModuleProvides(final ModuleProvides constantModule) {
            append(constantModule);
            append(constantModule.toString(pool));
        }

        @Override
        public void visitModuleRequires(final ModuleRequires constantModule) {
            append(constantModule);
            append(constantModule.toString(pool));
            final String s = constantModule.toString(pool).trim();
            assertTrue(s.startsWith("java.base") || s.startsWith("Othermodularthing"), s);
        }

        @Override
        public void visitNestHost(final NestHost obj) {
            append(obj);
        }

        @Override
        public void visitNestMembers(final NestMembers obj) {
            append(obj);
        }

        @Override
        public void visitParameterAnnotation(final ParameterAnnotations obj) {
            append(obj);
        }

        @Override
        public void visitParameterAnnotationEntry(final ParameterAnnotationEntry obj) {
            append(obj);
        }

        @Override
        public void visitSignature(final Signature obj) {
            append(obj);
        }

        @Override
        public void visitSourceFile(final SourceFile obj) {
            append(obj);
        }

        @Override
        public void visitStackMap(final StackMap obj) {
            append(obj);
        }

        @Override
        public void visitStackMapEntry(final StackMapEntry obj) {
            append(obj);
        }

        @Override
        public void visitSynthetic(final Synthetic obj) {
            append(obj);
        }

        @Override
        public void visitUnknown(final Unknown obj) {
            append(obj);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "src/test/resources/jpms/java11/commons-io/module-info.class",
        "src/test/resources/jpms/java17/commons-io/module-info.class",
        "src/test/resources/jpms/java18/commons-io/module-info.class",
        "src/test/resources/jpms/java19-ea/commons-io/module-info.class"})
    // @formatter:on
    public void test(final String first) throws Exception {
        try (final InputStream inputStream = Files.newInputStream(Paths.get(first))) {
            final ClassParser classParser = new ClassParser(inputStream, "module-info.class");
            final JavaClass javaClass = classParser.parse();
            final ConstantPool constantPool = javaClass.getConstantPool();
            final ToStringVisitor visitor = new ToStringVisitor(constantPool);
            final DescendingVisitor descendingVisitor = new DescendingVisitor(javaClass, visitor);
            try {
                javaClass.accept(descendingVisitor);
            } catch (Exception | Error e) {
                fail(visitor.toString(), e);
            }
        }
    }

}
