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
package Mini;
import org.apache.bcel.generic.LocalVariableGen;

/**
 * Represents a variable declared in a LET expression or a FUN declaration.
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class Variable implements EnvEntry {
  private ASTIdent name;         // Reference to the original declaration
  private boolean  reserved;     // Is a key word?

  private int      line, column; // Extracted from name.getToken()
  private String   var_name;     // Short for name.getName()
  private LocalVariableGen local_var; // local var associated with this variable

  public Variable(ASTIdent name) {
    this(name, false);
  }

  public Variable(ASTIdent name, boolean reserved) {
    this.name     = name;
    this.reserved = reserved;

    var_name = name.getName();
    line     = name.getLine();
    column   = name.getColumn();
  }

  @Override
  public String toString() {
    if(!reserved) {
        return var_name + " declared at line " + line + ", column " + column;
    } else {
        return var_name + " <reserved key word>";
    }
  }

  public ASTIdent getName()    { return name; }
  public String   getHashKey() { return var_name; }
  public int      getLine()    { return line; }
  public int      getColumn()  { return column; }
  public int      getType()    { return name.getType(); }

  void setLocalVariable(LocalVariableGen local_var) {
    this.local_var = local_var;
  }
  LocalVariableGen getLocalVariable() { return local_var; }
}

