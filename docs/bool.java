  InstructionList il = new InstructionList();
  ...
  CodeConstraint constraint = new CodeConstraint() {
    public boolean checkCode(InstructionHandle[] match) {
      IfInstruction if1 = (IfInstruction)match[0].getInstruction();
      GOTO          g   = (GOTO)match[2].getInstruction();
      return (if1.getTarget() == match[3]) &&
             (g.getTarget() == match[4]);
    }  
  };
  FindPattern f   = new FindPattern(il);
  String      pat = "`IfInstruction'`ICONST_0'`GOTO'`ICONST_1'" + 
                    "`NOP'(`IFEQ'|`IFNE')";
  InstructionHandle[] match;
  for(InstructionHandle ih = f.search(pat, constraint);
      ih != null; ih = f.search(pat, match[0], constraint)) {
    match = f.getMatch(); // Constraint already checked
    ...
    match[0].setTarget(match[5].getTarget()); // Update target
    ...
    try {
      il.delete(match[1], match[5]);
    } catch(TargetLostException e) { ... }
  }
