package org.apache.bcel.verifier.statics;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantInvokeDynamic;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodHandle;
import org.apache.bcel.classfile.ConstantMethodType;
import org.apache.bcel.classfile.ConstantModule;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPackage;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Pass3aVerifierTestCase {
    private Verifier verifier;
    private org.apache.bcel.util.Repository repository;
    private ConstantPool cp;
    private JavaClass javaClass;
    private Method method;
    private Code code;

    @BeforeEach
    void setup() throws ClassNotFoundException {
        String className = "pass3a";
        
        verifier = mock(Verifier.class);
        repository = mock(org.apache.bcel.util.Repository.class);
        cp = mock(ConstantPool.class);
        javaClass = mock(JavaClass.class);
        method = mock(Method.class);
        code = mock(Code.class);
        
        // Mock the verifier
        when(verifier.doPass2()).thenReturn(VerificationResult.VR_OK);
        when(verifier.getClassName()).thenReturn(className);
        
        // Mock the repository
        Repository.setRepository(repository);
        when(repository.loadClass(className)).thenReturn(javaClass);
        
        // Mock the constant pool
        when(cp.getConstantPool()).thenReturn(new Constant[] {new ConstantModule(0)});
        
        // Mock the java class
        when(javaClass.getMethods()).thenReturn(new Method[] {method});
        when(javaClass.getConstantPool()).thenReturn(cp);
        
        // Mock the method
        when(method.getCode()).thenReturn(code);
        
        // Mock the code
        when(code.getAttributes()).thenReturn(new Attribute[0]);
        when(code.getExceptionTable()).thenReturn(new CodeException[0]);
    }
    
    @AfterAll
    public static void restoreRepository() {
        // We have set our mock repository, revert the change 
        Repository.setRepository(SyntheticRepository.getInstance());
    }
    
    public static Stream<Constant> constantsNotSupportedByLdc() {
        return Stream.of(
               new ConstantFieldref(0, 0),
               new ConstantInterfaceMethodref(0, 0),
               new ConstantInvokeDynamic(0, 0),
               new ConstantMethodHandle(0, 0),
               new ConstantDouble(0D),
               new ConstantLong(0L),
               new ConstantMethodHandle(0, 0),
               new ConstantMethodType(0),
               new ConstantModule(0),
               new ConstantNameAndType(0, 0),
               new ConstantPackage(0),
               new ConstantUtf8("constant")
                );
    }
    
    @ParameterizedTest
    @MethodSource("constantsNotSupportedByLdc")
    public void rejectLdcConstantModule(Constant constant) {
        // LDC the constant 0 and then return
        byte[] methodCode = new byte[] {
                Const.LDC,
                0,
                0,
                (byte) Const.RETURN,
        };

        when(code.getCode()).thenReturn(methodCode);
        when(cp.getConstantPool()).thenReturn(new Constant[] {constant});
        
        Pass3aVerifier pass3aVerifier = new Pass3aVerifier(verifier, 0);
        VerificationResult verificationResult = pass3aVerifier.do_verify();
        
        assertThat(verificationResult.getStatus()).isEqualTo(VerificationResult.VERIFIED_REJECTED);
        assertThat(verificationResult.getMessage()).startsWith("Instruction ldc[18](2) 0 constraint violated: Operand of LDC");
    }
}

