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
 *
 */
package org.apache.commons.bcel6.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.bcel6.Constants;
import org.apache.commons.bcel6.classfile.Utility;
import org.apache.commons.bcel6.generic.AllocationInstruction;
import org.apache.commons.bcel6.generic.ArrayInstruction;
import org.apache.commons.bcel6.generic.ArrayType;
import org.apache.commons.bcel6.generic.BranchInstruction;
import org.apache.commons.bcel6.generic.CHECKCAST;
import org.apache.commons.bcel6.generic.CPInstruction;
import org.apache.commons.bcel6.generic.CodeExceptionGen;
import org.apache.commons.bcel6.generic.ConstantPoolGen;
import org.apache.commons.bcel6.generic.ConstantPushInstruction;
import org.apache.commons.bcel6.generic.EmptyVisitor;
import org.apache.commons.bcel6.generic.FieldInstruction;
import org.apache.commons.bcel6.generic.IINC;
import org.apache.commons.bcel6.generic.INSTANCEOF;
import org.apache.commons.bcel6.generic.Instruction;
import org.apache.commons.bcel6.generic.InstructionConstants;
import org.apache.commons.bcel6.generic.InstructionHandle;
import org.apache.commons.bcel6.generic.InvokeInstruction;
import org.apache.commons.bcel6.generic.LDC;
import org.apache.commons.bcel6.generic.LDC2_W;
import org.apache.commons.bcel6.generic.LocalVariableInstruction;
import org.apache.commons.bcel6.generic.MULTIANEWARRAY;
import org.apache.commons.bcel6.generic.MethodGen;
import org.apache.commons.bcel6.generic.NEWARRAY;
import org.apache.commons.bcel6.generic.ObjectType;
import org.apache.commons.bcel6.generic.RET;
import org.apache.commons.bcel6.generic.ReturnInstruction;
import org.apache.commons.bcel6.generic.Select;
import org.apache.commons.bcel6.generic.Type;

/**
 * Factory creates il.append() statements, and sets instruction targets.
 * A helper class for BCELifier.
 *
 * @see BCELifier
 * @version $Id: BCELFactory.java 1695790 2015-08-13 22:16:16Z ggregory $
 */
class BCELFactory extends EmptyVisitor {

    private final MethodGen _mg;
    private final PrintWriter _out;
    private final ConstantPoolGen _cp;


    BCELFactory(MethodGen mg, PrintWriter out) {
        _mg = mg;
        _cp = mg.getConstantPool();
        _out = out;
    }

    private final Map<BranchInstruction, InstructionHandle> branch_map = new HashMap<>();


    public void start() {
        if (!_mg.isAbstract() && !_mg.isNative()) {
            for (InstructionHandle ih = _mg.getInstructionList().getStart(); ih != null; ih = ih
                    .getNext()) {
                Instruction i = ih.getInstruction();
                if (i instanceof BranchInstruction) {
                    branch_map.put((BranchInstruction)i, ih); // memorize container
                }
                if (ih.hasTargeters()) {
                    if (i instanceof BranchInstruction) {
                        _out.println("    InstructionHandle ih_" + ih.getPosition() + ";");
                    } else {
                        _out.print("    InstructionHandle ih_" + ih.getPosition() + " = ");
                    }
                } else {
                    _out.print("    ");
                }
                if (!visitInstruction(i)) {
                    i.accept(this);
                }
            }
            updateBranchTargets();
            updateExceptionHandlers();
        }
    }


    private boolean visitInstruction( Instruction i ) {
        short opcode = i.getOpcode();
        if ((InstructionConstants.getInstruction(opcode) != null)
                && !(i instanceof ConstantPushInstruction) && !(i instanceof ReturnInstruction)) { // Handled below
            _out.println("il.append(InstructionConstants."
                    + i.getName().toUpperCase(Locale.ENGLISH) + ");");
            return true;
        }
        return false;
    }


    @Override
    public void visitLocalVariableInstruction( LocalVariableInstruction i ) {
        short opcode = i.getOpcode();
        Type type = i.getType(_cp);
        if (opcode == Constants.IINC) {
            _out.println("il.append(new IINC(" + i.getIndex() + ", " + ((IINC) i).getIncrement()
                    + "));");
        } else {
            String kind = (opcode < Constants.ISTORE) ? "Load" : "Store";
            _out.println("il.append(_factory.create" + kind + "(" + BCELifier.printType(type)
                    + ", " + i.getIndex() + "));");
        }
    }


    @Override
    public void visitArrayInstruction( ArrayInstruction i ) {
        short opcode = i.getOpcode();
        Type type = i.getType(_cp);
        String kind = (opcode < Constants.IASTORE) ? "Load" : "Store";
        _out.println("il.append(_factory.createArray" + kind + "(" + BCELifier.printType(type)
                + "));");
    }


    @Override
    public void visitFieldInstruction( FieldInstruction i ) {
        short opcode = i.getOpcode();
        String class_name = i.getClassName(_cp);
        String field_name = i.getFieldName(_cp);
        Type type = i.getFieldType(_cp);
        _out.println("il.append(_factory.createFieldAccess(\"" + class_name + "\", \"" + field_name
                + "\", " + BCELifier.printType(type) + ", " + "Constants."
                + Constants.OPCODE_NAMES[opcode].toUpperCase(Locale.ENGLISH) + "));");
    }


    @Override
    public void visitInvokeInstruction( InvokeInstruction i ) {
        short opcode = i.getOpcode();
        String class_name = i.getClassName(_cp);
        String method_name = i.getMethodName(_cp);
        Type type = i.getReturnType(_cp);
        Type[] arg_types = i.getArgumentTypes(_cp);
        _out.println("il.append(_factory.createInvoke(\"" + class_name + "\", \"" + method_name
                + "\", " + BCELifier.printType(type) + ", "
                + BCELifier.printArgumentTypes(arg_types) + ", " + "Constants."
                + Constants.OPCODE_NAMES[opcode].toUpperCase(Locale.ENGLISH) + "));");
    }


    @Override
    public void visitAllocationInstruction( AllocationInstruction i ) {
        Type type;
        if (i instanceof CPInstruction) {
            type = ((CPInstruction) i).getType(_cp);
        } else {
            type = ((NEWARRAY) i).getType();
        }
        short opcode = ((Instruction) i).getOpcode();
        int dim = 1;
        switch (opcode) {
            case Constants.NEW:
                _out.println("il.append(_factory.createNew(\"" + ((ObjectType) type).getClassName()
                        + "\"));");
                break;
            case Constants.MULTIANEWARRAY:
                dim = ((MULTIANEWARRAY) i).getDimensions();
                //$FALL-THROUGH$
            case Constants.ANEWARRAY:
            case Constants.NEWARRAY:
                if (type instanceof ArrayType) {
                    type = ((ArrayType) type).getBasicType();
                }
                _out.println("il.append(_factory.createNewArray(" + BCELifier.printType(type)
                        + ", (short) " + dim + "));");
                break;
            default:
                throw new RuntimeException("Oops: " + opcode);
        }
    }


    private void createConstant( Object value ) {
        String embed = value.toString();
        if (value instanceof String) {
            embed = '"' + Utility.convertString(embed) + '"';
        } else if (value instanceof Character) {
            embed = "(char)0x" + Integer.toHexString(((Character) value).charValue());
        } else if (value instanceof Float) {
            embed += "f";
        } else if (value instanceof Long) {
            embed += "L";
        } else if (value instanceof ObjectType){
            ObjectType ot = (ObjectType) value;
            embed = "new ObjectType(\""+ot.getClassName()+"\")";
        }

        _out.println("il.append(new PUSH(_cp, " + embed + "));");
    }


    @Override
    public void visitLDC( LDC i ) {
        createConstant(i.getValue(_cp));
    }


    @Override
    public void visitLDC2_W( LDC2_W i ) {
        createConstant(i.getValue(_cp));
    }


    @Override
    public void visitConstantPushInstruction( ConstantPushInstruction i ) {
        createConstant(i.getValue());
    }


    @Override
    public void visitINSTANCEOF( INSTANCEOF i ) {
        Type type = i.getType(_cp);
        _out.println("il.append(new INSTANCEOF(_cp.addClass(" + BCELifier.printType(type) + ")));");
    }


    @Override
    public void visitCHECKCAST( CHECKCAST i ) {
        Type type = i.getType(_cp);
        _out.println("il.append(_factory.createCheckCast(" + BCELifier.printType(type) + "));");
    }


    @Override
    public void visitReturnInstruction( ReturnInstruction i ) {
        Type type = i.getType(_cp);
        _out.println("il.append(_factory.createReturn(" + BCELifier.printType(type) + "));");
    }

    // Memorize BranchInstructions that need an update
    private final List<BranchInstruction> branches = new ArrayList<>();


    @Override
    public void visitBranchInstruction( BranchInstruction bi ) {
        InstructionHandle bh = branch_map.get(bi);
        int pos = bh.getPosition();
        String name = bi.getName() + "_" + pos;
        if (bi instanceof Select) {
            Select s = (Select) bi;
            branches.add(bi);
            StringBuilder args = new StringBuilder("new int[] { ");
            int[] matchs = s.getMatchs();
            for (int i = 0; i < matchs.length; i++) {
                args.append(matchs[i]);
                if (i < matchs.length - 1) {
                    args.append(", ");
                }
            }
            args.append(" }");
            _out.print("Select " + name + " = new " + bi.getName().toUpperCase(Locale.ENGLISH)
                    + "(" + args + ", new InstructionHandle[" + matchs.length + "], null);");
        } else {
            int t_pos = bi.getPosition();
            String target;
            if (pos > t_pos) {
                target = "ih_" + t_pos;
            } else {
                branches.add(bi);
                target = "null";
            }
            _out.println("    BranchInstruction " + name + " = _factory.createBranchInstruction("
                    + "Constants." + bi.getName().toUpperCase(Locale.ENGLISH) + ", " + target
                    + ");");
        }
        if (bh.hasTargeters()) {
            _out.println("    ih_" + pos + " = il.append(" + name + ");");
        } else {
            _out.println("    il.append(" + name + ");");
        }
    }


    @Override
    public void visitRET( RET i ) {
        _out.println("il.append(new RET(" + i.getIndex() + ")));");
    }


    private void updateBranchTargets() {
        for (BranchInstruction bi : branches) {
            InstructionHandle bh = branch_map.get(bi);
            int pos = bh.getPosition();
            String name = bi.getName() + "_" + pos;
            int t_pos = bh.getPosition();
            _out.println("    " + name + ".setTarget(ih_" + t_pos + ");");
            if (bi instanceof Select) {
                Select s = (Select) bi;
                for (int matchCount = s.getMatchCount(), m= 0; m<matchCount; ++m) {
                    InstructionHandle ih = s.getMatchTarget(m);
                    t_pos = ih.getPosition();
                    _out.println("    " + name + ".setTarget(" + m + ", ih_" + t_pos + ");");
                }
            }
        }
    }


    private void updateExceptionHandlers() {
        CodeExceptionGen[] handlers = _mg.getExceptionHandlers();
        for (CodeExceptionGen h : handlers) {
            String type = (h.getCatchType() == null) ? "null" : BCELifier.printType(h
                    .getCatchType());
            _out.println("    method.addExceptionHandler(" + "ih_" + h.getStartPC().getPosition()
                    + ", " + "ih_" + h.getEndPC().getPosition() + ", " + "ih_"
                    + h.getHandlerPC().getPosition() + ", " + type + ");");
        }
    }
}
