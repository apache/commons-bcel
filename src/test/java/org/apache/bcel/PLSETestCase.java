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

package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class PLSETestCase extends AbstractTestCase
{
    /**
     * BCEL-208: A couple of methods in MethodGen.java need to test for
     * an empty instruction list.
     */
    public void testB208() throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = gen.getMethodAt(1);
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        mg.setInstructionList(null);
        mg.addLocalVariable("local2", Type.INT, null, null);
        // currently, this will cause null pointer exception
        mg.getLocalVariableTable(pool);
    }

    /**
     * BCEL-79:
     */
    public void testB79() throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.PLSETestClass");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        final Method m = gen.getMethodAt(2);
        final LocalVariableTable lvt = m.getLocalVariableTable();
        //System.out.println(lvt);
        //System.out.println(lvt.getTableLength());
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        final LocalVariableTable new_lvt = mg.getLocalVariableTable(mg.getConstantPool());
        //System.out.println(new_lvt);
        assertEquals("number of locals", lvt.getTableLength(), new_lvt.getTableLength());
    }

    /**
     * BCEL-262:
     */
    public void testB262() throws ClassNotFoundException
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.PLSETestEnum");
        final ClassGen gen = new ClassGen(clazz);
        final ConstantPoolGen pool = gen.getConstantPool();
        // get the values() method
        final Method m = gen.getMethodAt(0);
        final MethodGen mg = new MethodGen(m, gen.getClassName(), pool);
        final InstructionList il = mg.getInstructionList();
        // get the invokevirtual instruction
        final InstructionHandle ih = il.findHandle(3);
        final InvokeInstruction ii = (InvokeInstruction)(ih.getInstruction());
        // without fix, the getClassName() will throw:
        //   java.lang.IllegalArgumentException: Cannot be used on an array type
        final String cn = ii.getClassName(pool);
        assertEquals("[Lorg.apache.bcel.data.PLSETestEnum;", cn);
    }

    /**
     * BCEL-295:
     */
    public void testB295() throws Exception
    {
        final JavaClass clazz = getTestClass(PACKAGE_BASE_NAME+".data.PLSETestClass2");
        final ClassGen cg = new ClassGen(clazz);
        final ConstantPoolGen pool = cg.getConstantPool();
        final Method m = cg.getMethodAt(1);  // 'main'
        final LocalVariableTable lvt = m.getLocalVariableTable();
        final LocalVariable lv = lvt.getLocalVariable(2, 4);  // 'i'
        //System.out.println(lv);
        final MethodGen mg = new MethodGen(m, cg.getClassName(), pool);
        final LocalVariableTable new_lvt = mg.getLocalVariableTable(mg.getConstantPool());
        final LocalVariable new_lv = new_lvt.getLocalVariable(2, 4);  // 'i'
        //System.out.println(new_lv);
        assertEquals("live range length", lv.getLength(), new_lv.getLength());
    }
}
