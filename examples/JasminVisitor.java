import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import java.io.*;
import java.util.*;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;

/**
 * Disassemble Java class object into the <A HREF="http://www.cat.nyu.edu/meyer/jasmin">
 * JASMIN</A> format.
 *
 * @version $Id$
 * @author  <A HREF="http://www.berlin.de/~markus.dahm/">M. Dahm</A>
 */
public class JasminVisitor extends org.apache.bcel.classfile.EmptyVisitor {
  private JavaClass       clazz;
  private PrintWriter     out;
  private String          class_name;
  private ConstantPoolGen cp;

  public JasminVisitor(JavaClass clazz, OutputStream out) {
    this.clazz = clazz;
    this.out   = new PrintWriter(out);
    class_name = clazz.getClassName();
    cp = new ConstantPoolGen(clazz.getConstantPool());
  }

  /**
   * Start traversal using DefaultVisitor pattern.
   */
  public void disassemble() {
    new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
    out.close();
  }

  public void visitJavaClass(JavaClass clazz) {
    out.println(";; Produced by JasminVisitor (BCEL)");
    out.println(";; http://jakarta.apache.org/bcel/");
    out.println(";; " + new Date() + "\n");

    out.println(".source " + clazz.getSourceFileName());
    out.println("." + Utility.classOrInterface(clazz.getAccessFlags()) + " " +
		Utility.accessToString(clazz.getAccessFlags(), true) +
		" " + clazz.getClassName().replace('.', '/'));
    out.println(".super " + clazz.getSuperclassName().replace('.', '/'));

    String[] interfaces = clazz.getInterfaceNames();

    for(int i=0; i < interfaces.length; i++)
      out.println(".implements " + interfaces[i].replace('.', '/'));

    out.print("\n");
  }

  public void visitField(Field field) {
    out.print(".field " + Utility.accessToString(field.getAccessFlags()) +
		" " + field.getName() + " " + field.getSignature());
    if(field.getAttributes().length == 0)
      out.print("\n");
  }

  public void visitConstantValue(ConstantValue cv) {
    out.println(" = " + cv);
  }

  private Method _method;

  /**
   * Unfortunately Jasmin expects ".end method" after each method. Thus we've to check
   * for every of the method's attributes if it's the last one and print ".end method"
   * then.
   */
  private final void printEndMethod(Attribute attr) {
    Attribute[] attributes = _method.getAttributes();

    if(attr == attributes[attributes.length - 1])
      out.println(".end method");
  }

  public void visitDeprecated(Deprecated attribute) { printEndMethod(attribute); }

  public void visitSynthetic(Synthetic attribute) {
    if(_method != null)
      printEndMethod(attribute);
  }

  public void visitMethod(Method method) {
    this._method = method; // Remember for use in subsequent visitXXX calls

    out.println("\n.method " + Utility.accessToString(_method.getAccessFlags()) +
		" " + _method.getName() + _method.getSignature());

    Attribute[] attributes = _method.getAttributes();
    if((attributes == null) || (attributes.length == 0))
      out.println(".end method");
  }

  public void visitExceptionTable(ExceptionTable e) {
    String[] names = e.getExceptionNames();
    for(int i=0; i < names.length; i++)
      out.println(".throws " + names[i].replace('.', '/'));

    printEndMethod(e);
  }

  private Hashtable map;

  public void visitCode(Code code) {
    int label_counter = 0;

    out.println(".limit stack " + code.getMaxStack());
    out.println(".limit locals " + code.getMaxLocals());

    MethodGen           mg  = new MethodGen(_method, class_name, cp);
    InstructionList     il  = mg.getInstructionList();
    InstructionHandle[] ihs = il.getInstructionHandles();

    /* Pass 1: Give all referenced instruction handles a symbolic name, i.e. a
     * label.
     */
    map = new Hashtable();

    for(int i=0; i < ihs.length; i++) {
      if(ihs[i] instanceof BranchHandle) {
	BranchInstruction bi = (BranchInstruction)ihs[i].getInstruction();
	
	if(bi instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
	  InstructionHandle[] targets = ((Select)bi).getTargets();
	  
	  for(int j=0; j < targets.length; j++)
	    put(targets[j], "Label" + label_counter++ + ":");
	}

	InstructionHandle ih = bi.getTarget();
	put(ih, "Label" + label_counter++ + ":");
      }
    }

    LocalVariableGen[] lvs = mg.getLocalVariables();
    for(int i=0; i < lvs.length; i++) {
      InstructionHandle ih = lvs[i].getStart();
      put(ih, "Label" + label_counter++ + ":");
      ih = lvs[i].getEnd();
      put(ih, "Label" + label_counter++ + ":");	
    }
    
    CodeExceptionGen[] ehs = mg.getExceptionHandlers();
    for(int i=0; i < ehs.length; i++) {
      CodeExceptionGen  c  = ehs[i];
      InstructionHandle ih = c.getStartPC();

      put(ih, "Label" + label_counter++ + ":");	
      ih = c.getEndPC();
      put(ih, "Label" + label_counter++ + ":");	
      ih = c.getHandlerPC();
      put(ih, "Label" + label_counter++ + ":");	
    }

    LineNumberGen[] lns = mg.getLineNumbers();
    for(int i=0; i < lns.length; i++) {
      InstructionHandle ih = lns[i].getInstruction();
      put(ih, ".line " + lns[i].getSourceLine());
    }
 
    /* Pass 2: Output code.
     */
    for(int i=0; i < lvs.length; i++) {
      LocalVariableGen l = lvs[i];
      out.println(".var " + l.getIndex() + " is " + l.getName() + " " +
		  l.getType().getSignature() + 
		  " from " + get(l.getStart()) + 
		  " to " + get(l.getEnd()));
    }

    out.print("\n");
    
    for(int i=0; i < ihs.length; i++) {
      InstructionHandle ih   = ihs[i];
      Instruction       inst = ih.getInstruction();
      String            str  = (String)map.get(ih);
      
      if(str != null)
	out.println(str);

      if(inst instanceof BranchInstruction) {
	if(inst instanceof Select) { // Special cases LOOKUPSWITCH and TABLESWITCH
	  Select              s       = (Select)inst;
	  int[]               matchs  = s.getMatchs();
	  InstructionHandle[] targets = s.getTargets();
	  
	  if(s instanceof TABLESWITCH) {
	    out.println("\ttableswitch " + matchs[0] + " " +
			matchs[matchs.length - 1]);
	    
	    for(int j=0; j < targets.length; j++)
	      out.println("\t\t" + get(targets[j]));

	  } else { // LOOKUPSWITCH
	    out.println("\tlookupswitch ");

	    for(int j=0; j < targets.length; j++)
	      out.println("\t\t" + matchs[j] + " : " + get(targets[j]));
	  }

	  out.println("\t\tdefault: " + get(s.getTarget())); // Applies for both
	} else {
	  BranchInstruction bi  = (BranchInstruction)inst;
	  ih  = bi.getTarget();
	  str = get(ih);
	  out.println("\t" + Constants.OPCODE_NAMES[bi.getOpcode()] + " " + str);
	}
      }
      else
	out.println("\t" + inst.toString(cp.getConstantPool()));
    }
    
    out.print("\n");

    for(int i=0; i < ehs.length; i++) {
      CodeExceptionGen c = ehs[i];
      ObjectType caught = c.getCatchType();
      String class_name = (caught == null)?  // catch any exception, used when compiling finally
	"all" : caught.getClassName().replace('.', '/');

      out.println(".catch " + class_name + " from " +
		  get(c.getStartPC()) + " to " + get(c.getEndPC()) +
		  " using " + get(c.getHandlerPC()));
    }

    printEndMethod(code);
  }
 
  private final String get(InstructionHandle ih) {
    String str = new StringTokenizer((String)map.get(ih), "\n").nextToken();
    return str.substring(0, str.length() - 1);
  }

  private final void put(InstructionHandle ih, String line) {
    String str = (String)map.get(ih);

    if(str == null)
      map.put(ih, line);
    else {
      if(line.startsWith("Label") || str.endsWith(line)) // Already have a label in the map
	return;

      map.put(ih, str + "\n" + line); // append
    }
  }	

  public static void main(String[] argv) { 
    ClassParser parser=null;
    JavaClass   java_class;

    try {
      if(argv.length == 0)
	System.err.println("disassemble: No input files specified");
      else {
	for(int i=0; i < argv.length; i++) {
	  if((java_class = Repository.lookupClass(argv[i])) == null)
	    java_class = new ClassParser(argv[i]).parse();

          String class_name = java_class.getClassName();
          int    index      = class_name.lastIndexOf('.');
	  String path       = class_name.substring(0, index + 1).replace('.', File.separatorChar);
	  class_name = class_name.substring(index + 1);

	  if(!path.equals("")) {
	    File f = new File(path);
	    f.mkdirs();
	  }

	  String name = path + class_name + ".j";
	  FileOutputStream out = new FileOutputStream(name);
	  new JasminVisitor(java_class, out).disassemble();
	  System.out.println("File dumped to: " + name);
	}
      }	  
    } catch(Exception e) {
      e.printStackTrace();
    }
  }        
}
