package org.apache.bcel.generic;

public interface VisitorSupportsInvokeDynamic extends Visitor{

	void visitNameSignatureInstruction(NameSignatureInstruction obj);
	void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);
}
