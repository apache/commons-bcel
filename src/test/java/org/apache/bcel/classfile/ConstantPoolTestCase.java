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

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.junit.Test;

public class ConstantPoolTestCase extends AbstractTestCase {
    @Test
    public void testConstantToString() throws ClassNotFoundException {
        JavaClass clazz = getTestClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().equals("<init>")) {
                for (InstructionHandle instructionHandle : getInstructionHandles(clazz, cp, method)) {
                    System.out.println(instructionHandle.getInstruction().toString(cp.getConstantPool()));
                }
            }
        }
    }

    private InstructionHandle[] getInstructionHandles(JavaClass clazz, ConstantPoolGen cp, Method method) {
        MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);
        InstructionList instructionList = methodGen.getInstructionList();
        return instructionList.getInstructionHandles();
    }
}
