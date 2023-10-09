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
package org.apache.bcel.verifier.tests;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class TestThrow01Creator extends TestCreator {
    private final ClassGen cg;

    public TestThrow01Creator() {
        cg = new ClassGen(TEST_PACKAGE + ".TestThrow01", "java.lang.Object", "TestThrow01.java", Const.ACC_PUBLIC | Const.ACC_SUPER, new String[] {});
    }

    @Override
    public void create(final OutputStream out) throws IOException {
        final InstructionList il = new InstructionList();
        final MethodGen method = new MethodGen(Const.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[0], "b", cg.getClassName(), il, cg.getConstantPool());
        il.append(InstructionConst.getInstruction(Const.ATHROW));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
        cg.getJavaClass().dump(out);
    }
}
