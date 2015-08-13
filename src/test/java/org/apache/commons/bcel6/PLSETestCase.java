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

package org.apache.commons.bcel6;

import org.apache.commons.bcel6.classfile.JavaClass;
import org.apache.commons.bcel6.classfile.Method;
import org.apache.commons.bcel6.generic.ClassGen;
import org.apache.commons.bcel6.generic.ConstantPoolGen;
import org.apache.commons.bcel6.generic.MethodGen;
import org.apache.commons.bcel6.generic.Type;

public class PLSETestCase extends AbstractTestCase
{
    /**
     * BCEL-208: A couple of methods in MethodGen.java need to test for
     * an empty instruction list.
     */
    public void testB208() throws ClassNotFoundException
    {
        JavaClass clazz = getTestClass("org.apache.commons.bcel6.data.PLSETestClass");
        ClassGen gen = new ClassGen(clazz);
        ConstantPoolGen pool = gen.getConstantPool();
        Method m = gen.getMethodAt(1);
        MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        mg.setInstructionList(null);
        mg.addLocalVariable("local2", Type.INT, null, null);
        // currently, this will cause null pointer exception
        mg.getLocalVariableTable(pool);
    }
}
