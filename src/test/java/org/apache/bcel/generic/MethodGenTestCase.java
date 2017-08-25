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
 */

package org.apache.bcel.generic;

import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import junit.framework.TestCase;

public class MethodGenTestCase extends TestCase {

    public static class Foo {
        public void bar() {
            @SuppressWarnings("unused")
            final
            int a = 1;
        }
    }

    @interface A {
    }

    @interface B {
    }

    public static class Bar {
        public class Inner {
            public Inner(@A final Object a, @B final Object b) {

            }
        }
    }

    private MethodGen getMethod(final Class<?> cls, final String name) throws ClassNotFoundException {
        final JavaClass jc = Repository.lookupClass(cls);
        final ConstantPoolGen cp = new ConstantPoolGen(jc.getConstantPool());
        for (final Method method : jc.getMethods()) {
            if (method.getName().equals(name)) {
                return new MethodGen(method, jc.getClassName(), cp);
            }
        }

        fail("Method " + name + " not found in class " + cls);
        return null;
    }

    public void testRemoveLocalVariable() throws Exception {
        final MethodGen mg = getMethod(Foo.class, "bar");

        final LocalVariableGen lv = mg.getLocalVariables()[1];
        assertEquals("variable name", "a", lv.getName());
        final InstructionHandle start = lv.getStart();
        final InstructionHandle end = lv.getEnd();
        assertNotNull("scope start", start);
        assertNotNull("scope end", end);
        assertTrue("scope start not targeted by the local variable", Arrays.asList(start.getTargeters()).contains(lv));
        assertTrue("scope end not targeted by the local variable", Arrays.asList(end.getTargeters()).contains(lv));

        // now let's remove the local variable
        mg.removeLocalVariable(lv);

        assertFalse("scope start still targeted by the removed variable", Arrays.asList(start.getTargeters()).contains(lv));
        assertFalse("scope end still targeted by the removed variable", Arrays.asList(end.getTargeters()).contains(lv));
        assertNull("scope start", lv.getStart());
        assertNull("scope end", lv.getEnd());
    }

    public void testRemoveLocalVariables() throws Exception {
        final MethodGen mg = getMethod(Foo.class, "bar");

        final LocalVariableGen lv = mg.getLocalVariables()[1];
        assertEquals("variable name", "a", lv.getName());
        final InstructionHandle start = lv.getStart();
        final InstructionHandle end = lv.getEnd();
        assertNotNull("scope start", start);
        assertNotNull("scope end", end);
        assertTrue("scope start not targeted by the local variable", Arrays.asList(start.getTargeters()).contains(lv));
        assertTrue("scope end not targeted by the local variable", Arrays.asList(end.getTargeters()).contains(lv));

        // now let's remove the local variables
        mg.removeLocalVariables();

        assertFalse("scope start still targeted by the removed variable", Arrays.asList(start.getTargeters()).contains(lv));
        assertFalse("scope end still targeted by the removed variable", Arrays.asList(end.getTargeters()).contains(lv));
        assertNull("scope start", lv.getStart());
        assertNull("scope end", lv.getEnd());
    }

    public void testAnnotationsAreUnpacked() throws Exception {
        final JavaClass jc = Repository.lookupClass(Bar.Inner.class);
        final ClassGen cg = new ClassGen(jc);
        final MethodGen mg = new MethodGen(cg.getMethodAt(0), cg.getClassName(), cg.getConstantPool());
        final List<AnnotationEntryGen> firstParamAnnotations = mg.getAnnotationsOnParameter(0);
        assertEquals("Wrong number of annotations in the first parameter", 1, firstParamAnnotations.size());
        final List<AnnotationEntryGen> secondParamAnnotations = mg.getAnnotationsOnParameter(1);
        assertEquals("Wrong number of annotations in the second parameter", 1, secondParamAnnotations.size());
    }
}
