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

package org.apache.commons.bcel6.generic;

import org.apache.commons.bcel6.Repository;
import org.apache.commons.bcel6.classfile.JavaClass;
import org.apache.commons.bcel6.classfile.Method;

import junit.framework.TestCase;

public class MethodGenTestCase extends TestCase {

    public static class Foo {
        public void bar() {
            @SuppressWarnings("unused")
            int a = 1;
        }
    }

    private MethodGen getMethod(Class<?> cls, String name) throws ClassNotFoundException {
        JavaClass jc = Repository.lookupClass(cls);
        ConstantPoolGen cp = new ConstantPoolGen(jc.getConstantPool());
        for (Method method : jc.getMethods()) {
            if (method.getName().equals(name)) {
                return new MethodGen(method, jc.getClassName(), cp);
            }
        }

        fail("Method " + name + " not found in class " + cls);
        return null;
    }

    public void testRemoveLocalVariable() throws Exception {
        MethodGen mg = getMethod(Foo.class, "bar");

        LocalVariableGen lv = mg.getLocalVariables()[1];
        assertEquals("variable name", "a", lv.getName());
        InstructionHandle start = lv.getStart();
        InstructionHandle end = lv.getEnd();
        assertNotNull("scope start", start);
        assertNotNull("scope end", end);
        assertTrue("scope start not targeted by the local variable", start.getTargeters().contains(lv));
        assertTrue("scope end not targeted by the local variable", end.getTargeters().contains(lv));

        // now let's remove the local variable
        mg.removeLocalVariable(lv);

        assertFalse("scope start still targeted by the removed variable", start.getTargeters().contains(lv));
        assertFalse("scope end still targeted by the removed variable", end.getTargeters().contains(lv));
        assertNull("scope start", lv.getStart());
        assertNull("scope end", lv.getEnd());
    }

    public void testRemoveLocalVariables() throws Exception {
        MethodGen mg = getMethod(Foo.class, "bar");

        LocalVariableGen lv = mg.getLocalVariables()[1];
        assertEquals("variable name", "a", lv.getName());
        InstructionHandle start = lv.getStart();
        InstructionHandle end = lv.getEnd();
        assertNotNull("scope start", start);
        assertNotNull("scope end", end);
        assertTrue("scope start not targeted by the local variable", start.getTargeters().contains(lv));
        assertTrue("scope end not targeted by the local variable", end.getTargeters().contains(lv));

        // now let's remove the local variables
        mg.removeLocalVariables();

        assertFalse("scope start still targeted by the removed variable", start.getTargeters().contains(lv));
        assertFalse("scope end still targeted by the removed variable", end.getTargeters().contains(lv));
        assertNull("scope start", lv.getStart());
        assertNull("scope end", lv.getEnd());
    }
}
