/*
 * Copyright 2022 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Tim Boudreau
 */
public class ConstantPoolModuleToStringNPETest {

    private static byte[] MODULE_INFO_BYTES;

    @Test
    public void test() throws Exception {
        ClassParser cp = new ClassParser(new ByteArrayInputStream(MODULE_INFO_BYTES), "module-info.class");
        JavaClass jc = cp.parse();
        System.out.println("Visit Created a JC");
        ConstantPool pool = jc.getConstantPool();
        V v = new V(pool);
        DescendingVisitor dv = new DescendingVisitor(jc, v);
        try {
            jc.accept(dv);
        } catch (Exception | Error err) {
            v.rethrow(err);
        }
    }

    static class V implements Visitor {

        private final StringBuilder sb = new StringBuilder();
        private final ConstantPool pool;
        private int count;

        public V(ConstantPool pool) {
            this.pool = pool;
        }

        void rethrow(Throwable thrown) {
            throw new AssertionError(thrown + " after " + count
                    + " objects visited: " + sb, thrown);
        }

        private void out(Object o) {
            if (sb.length() == 0) {
                sb.append('\n');
            }
            sb.append(o);
        }

        @Override
        public void visitCode(Code obj) {
            out(obj.toString(true));
        }

        @Override
        public void visitCodeException(CodeException obj) {
            out(obj.toString(pool, true));
        }

        @Override
        public void visitConstantClass(ConstantClass obj) {
            out(obj);
        }

        @Override
        public void visitConstantDouble(ConstantDouble obj) {
            out(obj);
        }

        @Override
        public void visitConstantFieldref(ConstantFieldref obj) {
            out(obj);
        }

        @Override
        public void visitConstantFloat(ConstantFloat obj) {
            out(obj);
        }

        @Override
        public void visitConstantInteger(ConstantInteger obj) {
            out(obj);
        }

        @Override
        public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
            out(obj);
        }

        @Override
        public void visitConstantInvokeDynamic(ConstantInvokeDynamic obj) {
            out(obj);
        }

        @Override
        public void visitConstantLong(ConstantLong obj) {
            out(obj);
        }

        @Override
        public void visitConstantMethodref(ConstantMethodref obj) {
            out(obj);
        }

        @Override
        public void visitConstantNameAndType(ConstantNameAndType obj) {
            out(obj);
        }

        @Override
        public void visitConstantPool(ConstantPool obj) {
            out(obj);
        }

        @Override
        public void visitConstantString(ConstantString obj) {
            out(obj);
        }

        @Override
        public void visitConstantUtf8(ConstantUtf8 obj) {
            out(obj);
        }

        @Override
        public void visitConstantValue(ConstantValue obj) {
            out(obj);
        }

        @Override
        public void visitDeprecated(Deprecated obj) {
            out(obj);
        }

        @Override
        public void visitExceptionTable(ExceptionTable obj) {
            out(obj);
        }

        @Override
        public void visitField(Field obj) {
            out(obj);
        }

        @Override
        public void visitInnerClass(InnerClass obj) {
            out(obj.toString(pool));
        }

        @Override
        public void visitInnerClasses(InnerClasses obj) {
            out(obj);
        }

        @Override
        public void visitJavaClass(JavaClass obj) {
            out(obj);
        }

        @Override
        public void visitLineNumber(LineNumber obj) {
            out(obj);
        }

        @Override
        public void visitLineNumberTable(LineNumberTable obj) {
            out(obj);
        }

        @Override
        public void visitLocalVariable(LocalVariable obj) {
            out(obj);
        }

        @Override
        public void visitLocalVariableTable(LocalVariableTable obj) {
            out(obj);
        }

        @Override
        public void visitMethod(Method obj) {
            out(obj);
        }

        @Override
        public void visitSignature(Signature obj) {
            out(obj);
        }

        @Override
        public void visitSourceFile(SourceFile obj) {
            out(obj);
        }

        @Override
        public void visitSynthetic(Synthetic obj) {
            out(obj);
        }

        @Override
        public void visitUnknown(Unknown obj) {
            out(obj);
        }

        @Override
        public void visitStackMap(StackMap obj) {
            out(obj);
        }

        @Override
        public void visitStackMapEntry(StackMapEntry obj) {
            out(obj);
        }

        @Override
        public void visitAnnotation(Annotations obj) {
            out(obj);
        }

        @Override
        public void visitParameterAnnotation(ParameterAnnotations obj) {
            out(obj);
        }

        @Override
        public void visitAnnotationEntry(AnnotationEntry obj) {
            out(obj);
        }

        @Override
        public void visitAnnotationDefault(AnnotationDefault obj) {
            out(obj);
        }

        @Override
        public void visitLocalVariableTypeTable(LocalVariableTypeTable obj) {
            out(obj);
        }

        @Override
        public void visitEnclosingMethod(EnclosingMethod obj) {
            out(obj);
        }

        @Override
        public void visitBootstrapMethods(BootstrapMethods obj) {
            out(obj);
        }

        @Override
        public void visitMethodParameters(MethodParameters obj) {
            out(obj);
        }

        @Override
        public void visitConstantMethodType(ConstantMethodType obj) {
            out(obj);
        }

        @Override
        public void visitConstantMethodHandle(ConstantMethodHandle obj) {
            out(obj);
        }

        @Override
        public void visitParameterAnnotationEntry(ParameterAnnotationEntry obj) {
            out(obj);
        }

        @Override
        public void visitConstantPackage(ConstantPackage constantPackage) {
            out(constantPackage);
        }

        @Override
        public void visitConstantModule(ConstantModule constantModule) {
            out(constantModule);
        }

        @Override
        public void visitMethodParameter(MethodParameter obj) {
            out(obj);
        }

        @Override
        public void visitConstantDynamic(ConstantDynamic constantDynamic) {
            out(constantDynamic);
        }

        @Override
        public void visitModule(Module constantModule) {
            out(constantModule);
        }

        @Override
        public void visitModuleRequires(ModuleRequires constantModule) {
            out(constantModule);
            out(constantModule.toString(pool));
            String s = constantModule.toString(pool).trim();
            System.out.println("S: " + s);
            assertTrue(s.startsWith("java.base") || s.startsWith("Othermodularthing"), s);
        }

        @Override
        public void visitModuleExports(ModuleExports constantModule) {
            out(constantModule);
            out(constantModule.toString(pool));
        }

        @Override
        public void visitModuleOpens(ModuleOpens constantModule) {
            out(constantModule);
            out(constantModule.toString(pool));
        }

        @Override
        public void visitModuleProvides(ModuleProvides constantModule) {
            out(constantModule);
            out(constantModule.toString(pool));
        }

        @Override
        public void visitModulePackages(ModulePackages constantModule) {
            out(constantModule);
        }

        @Override
        public void visitModuleMainClass(ModuleMainClass obj) {
            out(obj);
        }

        @Override
        public void visitNestHost(NestHost obj) {
            out(obj);
        }

        @Override
        public void visitNestMembers(NestMembers obj) {
            out(obj);
        }
    }

    @BeforeAll
    public static void setup() {
        MODULE_INFO_BYTES = Base64.getDecoder().decode(MODULE_INFO_BASE_64);
    }

    // A simple module-info.class file as base 64
    private static final String MODULE_INFO_BASE_64 = "yv66vgAAAD0AGwcAAgEAC21v"
            + "ZHVsZS1pbmZvAQAKU291cmNlRmlsZQEAEG1vZHVsZS1pbmZvLmphdmEBAAZNb2R1"
            + "bGUTAAcBAAxNb2R1bGFydGhpbmcTAAkBAAlqYXZhLmJhc2UBAAYxNy4wLjITAAwB"
            + "ABFPdGhlcm1vZHVsYXJ0aGluZxQADgEAGWNvbS9tYXN0ZnJvZy9tb2R1bGFydGhp"
            + "bmcTABABAA9jb20ud3VyZ2xlc25hcmYTABIBAAt3dWcuYnVnZ2xlcxQAFAEAHWNv"
            + "bS9tYXN0ZnJvZy9tb2R1bGFydGhpbmcvYnVnBwAWAQAMamF2YS9pby9GaWxlBwAY"
            + "AQApY29tL21hc3Rmcm9nL290aGVybW9kdWxhcnRoaW5nL090aGVyVGhpbmcHABoB"
            + "ACVjb20vbWFzdGZyb2cvbW9kdWxhcnRoaW5nL0dvcmdsZVRoaW5ngAAAAQAAAAAA"
            + "AAAAAAIAAwAAAAIABAAFAAAANAAGACAAAAACAAiAAAAKAAsAAAAAAAIADQAAAAIAD"
            + "wARABMAAAAAAAAAAQAVAAEAFwABABk=";
}
