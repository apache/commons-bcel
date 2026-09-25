/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

 package org.apache.bcel.data;

 import static org.junit.jupiter.api.Assertions.assertThrows;
 import java.io.DataInputStream;
 import java.io.ByteArrayInputStream;
 import java.security.InvalidParameterException;
 import org.apache.bcel.classfile.Attribute;
 import org.apache.bcel.classfile.Constant;
 import org.apache.bcel.classfile.ConstantPool;
 import org.junit.jupiter.api.Test;
 
 public class readAttributeTest {
     
     @Test
     public void testReadAttributeWithInvalidName() {
         DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0}));
         Constant[] constants = new Constant[1];
         ConstantPool constantPool = new ConstantPool(constants);
         
         assertThrows(InvalidParameterException.class, () -> {
             Attribute.readAttribute(dataInput, constantPool);
         });
     }
     
     @Test
     public void testReadAttributeWithNegativeLength() {
         DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(new byte[]{0, 0, 0, 0, -1}));
         Constant[] constants = new Constant[1];
         ConstantPool constantPool = new ConstantPool(constants);
         
         assertThrows(InvalidParameterException.class, () -> {
             Attribute.readAttribute(dataInput, constantPool);
         });
     }
 }