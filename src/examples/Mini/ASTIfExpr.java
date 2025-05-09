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
/* Generated By:JJTree: Do not edit this line. ASTIfExpr.java */
/* JJT: 0.3pre1 */

package Mini;

import org.apache.bcel.Const;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

/**
 */
public class ASTIfExpr extends ASTExpr {
    public static Node jjtCreate(final MiniParser p, final int id) {
        return new ASTIfExpr(p, id);
    }

    private ASTExpr if_expr, then_expr, else_expr;

    // Generated methods
    ASTIfExpr(final int id) {
        super(id);
    }

    ASTIfExpr(final MiniParser p, final int id) {
        super(p, id);
    }

    /**
     * Fifth pass, produce Java byte code.
     */
    @Override
    public void byte_code(final InstructionList il, final MethodGen method, final ConstantPoolGen cp) {
        if_expr.byte_code(il, method, cp);

        final InstructionList then_code = new InstructionList();
        final InstructionList else_code = new InstructionList();

        then_expr.byte_code(then_code, method, cp);
        else_expr.byte_code(else_code, method, cp);

        BranchHandle i, g;

        i = il.append(new IFEQ(null)); // If POP() == FALSE(i.e. 0) then branch to ELSE
        ASTFunDecl.pop();
        il.append(then_code);
        g = il.append(new GOTO(null));
        i.setTarget(il.append(else_code));
        g.setTarget(il.append(InstructionConst.NOP)); // May be optimized away later
    }

    /**
     * Overrides ASTExpr.closeNode() Cast children nodes Node[] to appropriate type ASTExpr[]
     */
    @Override
    public void closeNode() {
        if_expr = (ASTExpr) children[0];
        then_expr = (ASTExpr) children[1];

        if (children.length == 3) {
            else_expr = (ASTExpr) children[2];
        } else {
            MiniC.addError(if_expr.getLine(), if_expr.getColumn(), "IF expression has no ELSE branch");
        }

        children = null; // Throw away
    }

    /**
     * Fourth pass, produce Java code.
     */
    @Override
    public void code(final StringBuffer buf) {
        if_expr.code(buf);

        buf.append("    if(" + ASTFunDecl.pop() + " == 1) {\n");
        final int size = ASTFunDecl.size;
        then_expr.code(buf);
        ASTFunDecl.size = size; // reset stack
        buf.append("    } else {\n");
        else_expr.code(buf);
        buf.append("    }\n");
    }

    @Override
    public void dump(final String prefix) {
        System.out.println(toString(prefix));

        if_expr.dump(prefix + " ");
        then_expr.dump(prefix + " ");
        if (else_expr != null) {
            else_expr.dump(prefix + " ");
        }
    }

    /**
     * Second pass Overrides AstExpr.eval()
     *
     * @return type of expression
     * @param expected type
     */
    @Override
    public int eval(final int expected) {
        int thenType, elseType, ifType;

        if ((ifType = if_expr.eval(Const.T_BOOLEAN)) != Const.T_BOOLEAN) {
            MiniC.addError(if_expr.getLine(), if_expr.getColumn(), "IF expression is not of type boolean, but " + Const.getTypeName(ifType) + ".");
        }

        thenType = then_expr.eval(expected);

        if (expected != Const.T_UNKNOWN && thenType != expected) {
            MiniC.addError(then_expr.getLine(), then_expr.getColumn(),
                    "THEN expression is not of expected type " + Const.getTypeName(expected) + " but " + Const.getTypeName(thenType) + ".");
        }

        if (else_expr != null) {
            elseType = else_expr.eval(expected);

            if (expected != Const.T_UNKNOWN && elseType != expected) {
                MiniC.addError(else_expr.getLine(), else_expr.getColumn(),
                        "ELSE expression is not of expected type " + Const.getTypeName(expected) + " but " + Const.getTypeName(elseType) + ".");
            } else if (thenType == Const.T_UNKNOWN) {
                thenType = elseType;
                then_expr.setType(elseType);
            }
        } else {
            elseType = thenType;
            else_expr = then_expr;
        }

        if (thenType != elseType) {
            MiniC.addError(line, column, "Type mismatch in THEN-ELSE: " + Const.getTypeName(thenType) + " vs. " + Const.getTypeName(elseType) + ".");
        }

        type = thenType;

        is_simple = if_expr.isSimple() && then_expr.isSimple() && else_expr.isSimple();

        return type;
    }

    /**
     * Overrides ASTExpr.traverse()
     */
    @Override
    public ASTExpr traverse(final Environment env) {
        this.env = env;

        if_expr = if_expr.traverse(env);
        then_expr = then_expr.traverse(env);

        if (else_expr != null) {
            else_expr = else_expr.traverse(env);
        }

        return this;
    }
}
