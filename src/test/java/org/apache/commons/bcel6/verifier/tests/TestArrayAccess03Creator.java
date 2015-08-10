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
import org.apache.commons.bcel6.generic.ObjectType;
import org.apache.commons.bcel6.generic.PUSH;
import org.apache.commons.bcel6.generic.Type;

public class TestArrayAccess03Creator extends TestCreator implements Constants {
  private InstructionFactory _factory;
  private ConstantPoolGen    _cp;
  private ClassGen           _cg;

  public TestArrayAccess03Creator() {
    _cg = new ClassGen("org.apache.commons.bcel6.verifier.tests.TestArrayAccess03", "java.lang.Object", "TestArrayAccess03.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

    _cp = _cg.getConstantPool();
    _factory = new InstructionFactory(_cg, _cp);
  }

  public void create(OutputStream out) throws IOException {
    createMethod_0();
    createMethod_1();
    _cg.getJavaClass().dump(out);
  }

  private void createMethod_0() {
    InstructionList il = new InstructionList();
    MethodGen method = new MethodGen(ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {  }, "<init>", "org.apache.commons.bcel6.verifier.tests.TestArrayAccess03", il, _cp);

    InstructionHandle ih_0 = il.append(_factory.createLoad(Type.OBJECT, 0));
    il.append(_factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
    InstructionHandle ih_4 = il.append(_factory.createReturn(Type.VOID));
    method.setMaxStack();
    method.setMaxLocals();
    _cg.addMethod(method.getMethod());
    il.dispose();
  }

  private void createMethod_1() {
    InstructionList il = new InstructionList();
    MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { Type.OBJECT }, new String[] { "arg0" }, "test", "org.apache.commons.bcel6.verifier.tests.TestArrayAccess03", il, _cp);

    InstructionHandle ih_0 = il.append(new PUSH(_cp, 1));
    il.append(_factory.createNewArray(new ObjectType("org.apache.commons.bcel6.verifier.tests.TestArrayAccess03"), (short) 1));
    il.append(_factory.createStore(Type.OBJECT, 1));
    InstructionHandle ih_5 = il.append(_factory.createLoad(Type.OBJECT, 0));
    il.append(new PUSH(_cp, 0));
    il.append(_factory.createNew("org.apache.commons.bcel6.verifier.tests.TestArrayAccess03"));
    il.append(InstructionConstants.DUP);
    il.append(_factory.createInvoke("org.apache.commons.bcel6.verifier.tests.TestArrayAccess03", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
    il.append(InstructionConstants.AASTORE);
    InstructionHandle ih_15 = il.append(_factory.createReturn(Type.VOID));
    method.setMaxStack();
    method.setMaxLocals();
    _cg.addMethod(method.getMethod());
    il.dispose();
  }
}