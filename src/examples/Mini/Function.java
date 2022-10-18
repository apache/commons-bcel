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
package Mini;

/**
 * Represents a function declaration and its arguments. Type information is contained in the ASTIdent fields.
 */
public class Function implements EnvEntry {
    private final ASTIdent name; // Reference to the original declaration
    private ASTIdent[] args; // Reference to argument identifiers
//  private ASTExpr    body;         // Reference to function expression body
    private final boolean reserved; // Is a key word?
    private final int line, column; // Short for name.getToken()
    private final String fun_name; // Short for name.getName()
    private int no_args;

    public Function(final ASTIdent name, final ASTIdent[] args) {
        this(name, args, false);
    }

    public Function(final ASTIdent name, final ASTIdent[] args, final boolean reserved) {
        this.name = name;
        this.args = args;
        this.reserved = reserved;

        fun_name = name.getName();
        line = name.getLine();
        column = name.getColumn();
        setArgs(args);
    }

    public ASTIdent getArg(final int i) {
        return args[i];
    }

    public ASTIdent[] getArgs() {
        return args;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public String getHashKey() {
        return fun_name;
    }

    @Override
    public int getLine() {
        return line;
    }

    public ASTIdent getName() {
        return name;
    }

    public int getNoArgs() {
        return no_args;
    }

    public void setArgs(final ASTIdent[] args) {
        this.args = args;
        no_args = args == null ? 0 : args.length;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();

        for (int i = 0; i < no_args; i++) {
            buf.append(args[i].getName());

            if (i < no_args - 1) {
                buf.append(", ");
            }
        }

        final String prefix = "Function " + fun_name + "(" + buf.toString() + ")";

        if (!reserved) {
            return prefix + " declared at line " + line + ", column " + column;
        }
        return prefix + " <predefined function>";
    }
}
