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
package org.apache.commons.bcel6.verifier.tests;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.bcel6.Constants;
import org.apache.commons.bcel6.generic.ClassGen;
import org.apache.commons.bcel6.generic.ConstantPoolGen;
import org.apache.commons.bcel6.generic.InstructionConstants;
import org.apache.commons.bcel6.generic.InstructionFactory;
import org.apache.commons.bcel6.generic.InstructionHandle;
import org.apache.commons.bcel6.generic.InstructionList;
import org.apache.commons.bcel6.generic.MethodGen;
import org.apache.commons.bcel6.generic.Type;
import org.junit.Assert;

public class TestReturn01Creator extends TestCreator {
  private InstructionFactory _factory;
  private ConstantPoolGen    _cp;
  private ClassGen           _cg;

  public TestReturn01Creator() {
    _cg = new ClassGen("org.apache.commons.bcel6.verifier.tests.TestReturn01", "java.lang.Object", "TestReturn01.java", Constants.ACC_PUBLIC | Constants.ACC_SUPER, new String[] {  });

    _cp = _cg.getConstantPool();
    _factory = new InstructionFactory(_cg, _cp);
  }

  @Override
public void create(OutputStream out) throws IOException {
    createMethod_0();
    createMethod_1();
    _cg.getJavaClass().dump(out);
  }

  private void createMethod_0() {
    InstructionList il = new InstructionList();
    MethodGen method = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {  }, "<init>", "org.apache.commons.bcel6.verifier.tests.TestReturn01", il, _cp);

    InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
    Assert.assertNotNull(ih_0); // TODO why is this not used
    il.append(_factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
    InstructionHandle ih_4 = il.append(InstructionFactory.createReturn(Type.VOID));
    Assert.assertNotNull(ih_4); // TODO why is this not used
    method.setMaxStack();
    method.setMaxLocals();
    _cg.addMethod(method.getMethod());
    il.dispose();
  }

  private void createMethod_1() {
    InstructionList il = new InstructionList();
    MethodGen method = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[] {  }, "foo", "org.apache.commons.bcel6.verifier.tests.TestReturn01", il, _cp);

    InstructionHandle ih_0 = il.append(_factory.createNew("java.lang.Object"));
    Assert.assertNotNull(ih_0); // TODO why is this not used
    il.append(InstructionConstants.DUP);
    il.append(_factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
    il.append(InstructionConstants.NOP);
    InstructionHandle ih_8 = il.append(InstructionFactory.createReturn(Type.OBJECT));
    Assert.assertNotNull(ih_8); // TODO why is this not used
    method.setMaxStack();
    method.setMaxLocals();
    _cg.addMethod(method.getMethod());
    il.dispose();
  }
}
