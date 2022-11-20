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
package org.apache.bcel.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.AllocationInstruction;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.Type;

/**
 * Factory creates il.append() statements, and sets instruction targets. A helper class for BCELifier.
 *
 * @see BCELifier
 */
class BCELFactory extends EmptyVisitor {

    private static final String CONSTANT_PREFIX = Const.class.getSimpleName() + ".";
    private final MethodGen methodGen;
    private final PrintWriter printWriter;
    private final ConstantPoolGen constantPoolGen;

    private final Map<Instruction, InstructionHandle> branchMap = new HashMap<>();

    // Memorize BranchInstructions that need an update
    private final List<BranchInstruction> branches = new ArrayList<>();

    BCELFactory(final MethodGen mg, final PrintWriter out) {
        methodGen = mg;
        constantPoolGen = mg.getConstantPool();
        printWriter = out;
    }

    private void createConstant(final Object value) {
        String embed = value.toString();
        if (value instanceof String) {
            embed = '"' + Utility.convertString(embed) + '"';
        } else if (value instanceof Character) {
            embed = "(char)0x" + Integer.toHexString(((Character) value).charValue());
        } else if (value instanceof Float) {
            final Float f = (Float) value;
            if (Float.isNaN(f)) {
                embed = "Float.NaN";
            } else if (f == Float.POSITIVE_INFINITY) {
                embed = "Float.POSITIVE_INFINITY";
            } else if (f == Float.NEGATIVE_INFINITY) {
                embed = "Float.NEGATIVE_INFINITY";
            } else {
                embed += "f";
            }
        }  else if (value instanceof Double) {
            final Double d = (Double) value;
            if (Double.isNaN(d)) {
                embed = "Double.NaN";
            } else if (d == Double.POSITIVE_INFINITY) {
                embed = "Double.POSITIVE_INFINITY";
            } else if (d == Double.NEGATIVE_INFINITY) {
                embed = "Double.NEGATIVE_INFINITY";
            } else {
                embed += "d";
            }
        } else if (value instanceof Long) {
            embed += "L";
        } else if (value instanceof ObjectType) {
            final ObjectType ot = (ObjectType) value;
            embed = "new ObjectType(\"" + ot.getClassName() + "\")";
        } else if (value instanceof ArrayType) {
            final ArrayType at = (ArrayType) value;
            embed = "new ArrayType(" + BCELifier.printType(at.getBasicType()) + ", " + at.getDimensions() + ")";
        }

        printWriter.println("il.append(new PUSH(_cp, " + embed + "));");
    }

    public void start() {
        if (!methodGen.isAbstract() && !methodGen.isNative()) {
            for (InstructionHandle ih = methodGen.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
                final Instruction i = ih.getInstruction();
                if (i instanceof BranchInstruction) {
                    branchMap.put(i, ih); // memorize container
                }
                if (ih.hasTargeters()) {
                    if (i instanceof BranchInstruction) {
                        printWriter.println("    InstructionHandle ih_" + ih.getPosition() + ";");
                    } else {
                        printWriter.print("    InstructionHandle ih_" + ih.getPosition() + " = ");
                    }
                } else {
                    printWriter.print("    ");
                }
                if (!visitInstruction(i)) {
                    i.accept(this);
                }
            }
            updateBranchTargets();
            updateExceptionHandlers();
        }
    }

    private void updateBranchTargets() {
        branches.forEach(bi -> {
            final BranchHandle bh = (BranchHandle) branchMap.get(bi);
            final int pos = bh.getPosition();
            final String name = bi.getName() + "_" + pos;
            int targetPos = bh.getTarget().getPosition();
            printWriter.println("    " + name + ".setTarget(ih_" + targetPos + ");");
            if (bi instanceof Select) {
                final InstructionHandle[] ihs = ((Select) bi).getTargets();
                for (int j = 0; j < ihs.length; j++) {
                    targetPos = ihs[j].getPosition();
                    printWriter.println("    " + name + ".setTarget(" + j + ", ih_" + targetPos + ");");
                }
            }
        });
    }

    private void updateExceptionHandlers() {
        final CodeExceptionGen[] handlers = methodGen.getExceptionHandlers();
        for (final CodeExceptionGen h : handlers) {
            final String type = h.getCatchType() == null ? "null" : BCELifier.printType(h.getCatchType());
            printWriter.println("    method.addExceptionHandler(" + "ih_" + h.getStartPC().getPosition() + ", " + "ih_" + h.getEndPC().getPosition() + ", "
                + "ih_" + h.getHandlerPC().getPosition() + ", " + type + ");");
        }
    }

    @Override
    public void visitAllocationInstruction(final AllocationInstruction i) {
        Type type;
        if (i instanceof CPInstruction) {
            type = ((CPInstruction) i).getType(constantPoolGen);
        } else {
            type = ((NEWARRAY) i).getType();
        }
        final short opcode = ((Instruction) i).getOpcode();
        int dim = 1;
        switch (opcode) {
        case Const.NEW:
            printWriter.println("il.append(_factory.createNew(\"" + ((ObjectType) type).getClassName() + "\"));");
            break;
        case Const.MULTIANEWARRAY:
            dim = ((MULTIANEWARRAY) i).getDimensions();
            //$FALL-THROUGH$
        case Const.NEWARRAY:
            if (type instanceof ArrayType) {
                type = ((ArrayType) type).getBasicType();
            }
            //$FALL-THROUGH$
        case Const.ANEWARRAY:
            printWriter.println("il.append(_factory.createNewArray(" + BCELifier.printType(type) + ", (short) " + dim + "));");
            break;
        default:
            throw new IllegalArgumentException("Unhandled opcode: " + opcode);
        }
    }

    @Override
    public void visitArrayInstruction(final ArrayInstruction i) {
        final short opcode = i.getOpcode();
        final Type type = i.getType(constantPoolGen);
        final String kind = opcode < Const.IASTORE ? "Load" : "Store";
        printWriter.println("il.append(_factory.createArray" + kind + "(" + BCELifier.printType(type) + "));");
    }

    @Override
    public void visitBranchInstruction(final BranchInstruction bi) {
        final BranchHandle bh = (BranchHandle) branchMap.get(bi);
        final int pos = bh.getPosition();
        final String name = bi.getName() + "_" + pos;
        if (bi instanceof Select) {
            final Select s = (Select) bi;
            branches.add(bi);
            final StringBuilder args = new StringBuilder("new int[] { ");
            final int[] matchs = s.getMatchs();
            for (int i = 0; i < matchs.length; i++) {
                args.append(matchs[i]);
                if (i < matchs.length - 1) {
                    args.append(", ");
                }
            }
            args.append(" }");
            printWriter.print("Select " + name + " = new " + bi.getName().toUpperCase(Locale.ENGLISH) + "(" + args + ", new InstructionHandle[] { ");
            for (int i = 0; i < matchs.length; i++) {
                printWriter.print("null");
                if (i < matchs.length - 1) {
                    printWriter.print(", ");
                }
            }
            printWriter.println(" }, null);");
        } else {
            final int tPos = bh.getTarget().getPosition();
            String target;
            if (pos > tPos) {
                target = "ih_" + tPos;
            } else {
                branches.add(bi);
                target = "null";
            }
            printWriter.println("    BranchInstruction " + name + " = _factory.createBranchInstruction(" + CONSTANT_PREFIX
                + bi.getName().toUpperCase(Locale.ENGLISH) + ", " + target + ");");
        }
        if (bh.hasTargeters()) {
            printWriter.println("    ih_" + pos + " = il.append(" + name + ");");
        } else {
            printWriter.println("    il.append(" + name + ");");
        }
    }

    @Override
    public void visitCHECKCAST(final CHECKCAST i) {
        final Type type = i.getType(constantPoolGen);
        printWriter.println("il.append(_factory.createCheckCast(" + BCELifier.printType(type) + "));");
    }

    @Override
    public void visitConstantPushInstruction(final ConstantPushInstruction i) {
        createConstant(i.getValue());
    }

    @Override
    public void visitFieldInstruction(final FieldInstruction i) {
        final short opcode = i.getOpcode();
        final String className = i.getClassName(constantPoolGen);
        final String fieldName = i.getFieldName(constantPoolGen);
        final Type type = i.getFieldType(constantPoolGen);
        printWriter.println("il.append(_factory.createFieldAccess(\"" + className + "\", \"" + fieldName + "\", " + BCELifier.printType(type) + ", "
            + CONSTANT_PREFIX + Const.getOpcodeName(opcode).toUpperCase(Locale.ENGLISH) + "));");
    }

    @Override
    public void visitINSTANCEOF(final INSTANCEOF i) {
        final Type type = i.getType(constantPoolGen);
        printWriter.println("il.append(_factory.createInstanceOf(" + BCELifier.printType(type) + "));");
    }

    private boolean visitInstruction(final Instruction i) {
        final short opcode = i.getOpcode();
        if (InstructionConst.getInstruction(opcode) != null && !(i instanceof ConstantPushInstruction) && !(i instanceof ReturnInstruction)) { // Handled below
            printWriter.println("il.append(InstructionConst." + i.getName().toUpperCase(Locale.ENGLISH) + ");");
            return true;
        }
        return false;
    }

    @Override
    public void visitInvokeInstruction(final InvokeInstruction i) {
        final short opcode = i.getOpcode();
        final String className = i.getClassName(constantPoolGen);
        final String methodName = i.getMethodName(constantPoolGen);
        final Type type = i.getReturnType(constantPoolGen);
        final Type[] argTypes = i.getArgumentTypes(constantPoolGen);
        printWriter.println("il.append(_factory.createInvoke(\"" + className + "\", \"" + methodName + "\", " + BCELifier.printType(type) + ", "
            + BCELifier.printArgumentTypes(argTypes) + ", " + CONSTANT_PREFIX + Const.getOpcodeName(opcode).toUpperCase(Locale.ENGLISH) + "));");
    }

    @Override
    public void visitLDC(final LDC i) {
        createConstant(i.getValue(constantPoolGen));
    }

    @Override
    public void visitLDC2_W(final LDC2_W i) {
        createConstant(i.getValue(constantPoolGen));
    }

    @Override
    public void visitLocalVariableInstruction(final LocalVariableInstruction i) {
        final short opcode = i.getOpcode();
        final Type type = i.getType(constantPoolGen);
        if (opcode == Const.IINC) {
            printWriter.println("il.append(new IINC(" + i.getIndex() + ", " + ((IINC) i).getIncrement() + "));");
        } else {
            final String kind = opcode < Const.ISTORE ? "Load" : "Store";
            printWriter.println("il.append(_factory.create" + kind + "(" + BCELifier.printType(type) + ", " + i.getIndex() + "));");
        }
    }

    @Override
    public void visitRET(final RET i) {
        printWriter.println("il.append(new RET(" + i.getIndex() + "));");
    }

    @Override
    public void visitReturnInstruction(final ReturnInstruction i) {
        final Type type = i.getType(constantPoolGen);
        printWriter.println("il.append(_factory.createReturn(" + BCELifier.printType(type) + "));");
    }
}
