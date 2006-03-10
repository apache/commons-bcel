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

  public String toString() {
    if(!reserved) 
      return var_name + " declared at line " + line + ", column " + column;
    else
      return var_name + " <reserved key word>";
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

