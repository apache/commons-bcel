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

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class EmptyVisitorTestCase {

    /*
     * https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.2
     */
    private static final String RESERVED_OPCODE = "Reserved opcode";

    @ParameterizedTest
    @ValueSource(strings = {
    // @formatter:off
        "java.math.BigInteger",                          // contains instructions [AALOAD, AASTORE, ACONST_NULL, ALOAD, ANEWARRAY, ARETURN, ARRAYLENGTH, ASTORE, ATHROW, BALOAD, BASTORE, BIPUSH, CALOAD, CHECKCAST, D2I, DADD, DALOAD, DASTORE, DCONST, DDIV, DMUL, DRETURN, DSUB, DUP, DUP2, DUP_X2, FCONST, FRETURN, GETFIELD, GETSTATIC, GOTO, I2B, I2D, I2L, IADD, IALOAD, IAND, IASTORE, ICONST, IDIV, IFEQ, IFGE, IFGT, IFLE, IFLT, IFNE, IFNONNULL, IFNULL, IF_ACMPNE, IF_ICMPEQ, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ICMPLT, IF_ICMPNE, IINC, ILOAD, IMUL, INEG, INSTANCEOF, INVOKESPECIAL, INVOKESTATIC, INVOKEVIRTUAL, IOR, IREM, IRETURN, ISHL, ISHR, ISTORE, ISUB, IUSHR, IXOR, L2D, L2F, L2I, LADD, LALOAD, LAND, LASTORE, LCMP, LCONST, LDC, LDC2_W, LDC_W, LDIV, LLOAD, LMUL, LNEG, LOOKUPSWITCH, LOR, LREM, LRETURN, LSHL, LSHR, LSTORE, LSUB, LUSHR, NEW, NEWARRAY, POP, PUTFIELD, PUTSTATIC, RETURN, SIPUSH]
        "java.math.BigDecimal",                          // contains instructions [CASTORE, D2L, DLOAD, FALOAD, FASTORE, FDIV, FMUL, I2S, IF_ACMPEQ, LXOR, MONITORENTER, MONITOREXIT, TABLESWITCH]
        "java.awt.Color",                                // contains instructions [D2F, DCMPG, DCMPL, F2D, F2I, FADD, FCMPG, FCMPL, FLOAD, FSTORE, FSUB, I2F, INVOKEDYNAMIC]
        "java.util.Map",                                 // contains instruction INVOKEINTERFACE
        "java.io.Bits",                                  // contains instruction I2C
        "java.io.BufferedInputStream",                   // contains instruction DUP_X1
        "java.io.StreamTokenizer",                       // contains instruction DNEG, DSTORE
        "java.lang.Float",                               // contains instruction F2L
        "java.lang.invoke.LambdaForm",                   // contains instruction MULTIANEWARRAY,
        "java.nio.Bits",                                 // contains instruction POP2,
        "java.nio.HeapShortBuffer",                      // contains instruction SALOAD, SASTORE
        "Java8Example2",                                 // contains instruction FREM
        "java.awt.GradientPaintContext",                 // contains instruction DREM
        "java.util.concurrent.atomic.DoubleAccumulator", // contains instruction DUP2_X1
        "java.util.Hashtable",                           // contains instruction FNEG
        "javax.swing.text.html.CSS",                     // contains instruction DUP2_X2
        "org.apache.bcel.generic.LargeJump",             // contains instruction GOTO_W
        "org.apache.commons.lang.SerializationUtils"     // contains instruction JSR
    // @formatter:on
    })
    public void test(final String className) throws ClassNotFoundException {
        // "java.io.Bits" is not in Java 21.
        assumeFalse(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_21) && className.equals("java.io.Bits"));
        final JavaClass javaClass = SyntheticRepository.getInstance().loadClass(className);
        for (final Method method : javaClass.getMethods()) {
            final Code code = method.getCode();
            if (code != null) {
                final InstructionList instructionList = new InstructionList(code.getCode());
                for (final InstructionHandle instructionHandle : instructionList) {
                    instructionHandle.accept(new EmptyVisitor() {
                        @Override
                        public void visitBREAKPOINT(final BREAKPOINT obj) {
                            fail(RESERVED_OPCODE);
                        }

                        @Override
                        public void visitIMPDEP1(final IMPDEP1 obj) {
                            fail(RESERVED_OPCODE);
                        }

                        @Override
                        public void visitIMPDEP2(final IMPDEP2 obj) {
                            fail(RESERVED_OPCODE);
                        }
                    });
                }
            }
        }
    }
}
