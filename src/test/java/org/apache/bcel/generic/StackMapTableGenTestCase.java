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

import junit.framework.TestCase;
import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;

import java.util.List;

public class StackMapTableGenTestCase extends TestCase {

    public static class StackMapTableTest {
        public void method1(){}

        Integer method2(Boolean b){
            if(b){
                return 1;
            }else{
                return 2;
            }
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

    public void testFrameSplittingTrivial() throws Exception{
        MethodGen mg = getMethod(StackMapTableTest.class, "method1");
        StackMapTableGen sg = new StackMapTableGen(mg,mg.getConstantPool());
        List blocks = sg.splitIntoBlocks(mg.getInstructionList(), mg.getConstantPool());
        assertTrue(blocks.size() == 1); //There is only one frame, because the method1 is empty
    }

}
