import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

/**
 * Read class file(s) and patch all of its methods, so that they print
 * "hello" and their name and signature before doing anything else.
 *
 * @version $Id$
 * @author  <A HREF="http://www.berlin.de/~markus.dahm/">M. Dahm</A>
 */
public final class helloify implements Constants {
  private static String          class_name;
  private static ConstantPoolGen cp;
  private static int             out;     // reference to System.out
  private static int             println; // reference to PrintStream.println

  public static void main(String[] argv) { 
    try {
      for(int i=0; i < argv.length; i++) {
	if(argv[i].endsWith(".class")) {
          JavaClass       java_class = new ClassParser(argv[i]).parse();
	  ConstantPool    constants  = java_class.getConstantPool();
          String          file_name  = argv[i].substring(0, argv[i].length() - 6) +
	    "_hello.class";
	  cp = new ConstantPoolGen(constants);

	  helloifyClassName(java_class);

	  out     = cp.addFieldref("java.lang.System", "out",
				   "Ljava/io/PrintStream;");
	  println = cp.addMethodref("java.io.PrintStream", "println",
				    "(Ljava/lang/String;)V");
	  /* Patch all methods.
	   */
          Method[] methods = java_class.getMethods();

	  for(int j=0; j < methods.length; j++) // Directly use array
	    methods[j] = helloifyMethod(methods[j]);

	  /* Finally dump it back to a file.
	   */
	  java_class.setConstantPool(cp.getFinalConstantPool());
	  java_class.dump(file_name);
	}
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  /** Change class name to <old_name>_hello
   */
  private static void helloifyClassName(JavaClass java_class ) {
    class_name = java_class.getClassName() + "_hello";
    int index = java_class.getClassNameIndex();
    
    index = ((ConstantClass)cp.getConstant(index)).getNameIndex();
    cp.setConstant(index, new ConstantUtf8(class_name.replace('.', '/')));
  }

  /**
   * Patch a method.
   */
  private static Method helloifyMethod(Method m) {
    Code   code  = m.getCode();
    int    flags = m.getAccessFlags();
    String name  = m.getName();
    
    // Sanity check
    if(m.isNative() || m.isAbstract() || (code == null))
      return m;
    
    /* Create instruction list to be inserted at method start.
     */
    String mesg = "Hello from " + Utility.methodSignatureToString(m.getSignature(),
								  name, 
								  Utility.accessToString(flags));
    InstructionList patch  = new InstructionList();
    patch.append(new GETSTATIC(out));
    patch.append(new PUSH(cp, mesg));
    patch.append(new INVOKEVIRTUAL(println));
    
    MethodGen           mg  = new MethodGen(m, class_name, cp);
    InstructionList     il  = mg.getInstructionList();
    InstructionHandle[] ihs = il.getInstructionHandles();

    if(name.equals("<init>")) { // First let the super or other constructor be called
      for(int j=1; j < ihs.length; j++) {
	if(ihs[j].getInstruction() instanceof INVOKESPECIAL) {
	  il.append(ihs[j], patch); // Should check: method name == "<init>"
	  break;
	}
      }
    } else
      il.insert(ihs[0], patch);

    /* Stack size must be at least 2, since the println method takes 2 argument.
     */
    if(code.getMaxStack() < 2)
      mg.setMaxStack(2);

    m = mg.getMethod();

    il.dispose(); // Reuse instruction handles
    
    return m;
  }
}
