import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;

/**
 * Patch all Utf8 constants in the given class file <em>file</em>.class
 * and save the result in _<em>file</em>.class.
 *
 * Usage: patch <oldstring> <newstring> files
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class patchclass {
  public static void main(String[] argv) { 
    String[]    file_name = new String[argv.length];
    int         files     = 0;
    ClassParser parser    = null;
    JavaClass   java_class;

    if(argv.length < 3) {
      System.err.println("Usage: patch <oldstring> <newstring> file1.class ...");
      System.exit(-1);
    }

    try {
      for(int i=2; i < argv.length; i++) {
        file_name[files++] = argv[i];
    }
	
      for(int i=0; i < files; i++) {
	parser     = new ClassParser(file_name[i]);
	java_class = parser.parse();
	
	patchIt(argv[0], argv[1],
		java_class.getConstantPool().getConstantPool());

	// Dump the changed class to a new file
	java_class.dump("_" + file_name[i]);
	System.out.println("Results saved in: _" +  file_name[i]);
      }	  
    } catch(Exception e) {
      System.err.println(e);
    }
  }  
  /*
   * Replace all occurences of string "<em>old</em>" with 
   * "<em>replacement</em>" in all Utf8 constants
   */
  private static void patchIt(String old, String replacement,
			      Constant[] constant_pool)
  {
    ConstantUtf8 c;
    String       str;
    int          index, old_index;
    StringBuffer buf;

    /* Loop through constant pool
     */
    for(short i=0; i < constant_pool.length; i++) {
      if(constant_pool[i] instanceof ConstantUtf8) { // Utf8 string found
	try {
	  c   = (ConstantUtf8)constant_pool[i]; // Get the string
	  str = c.getBytes();
	  
	  if((index = str.indexOf(old)) != -1) { // `old' found in str
	    buf       = new StringBuffer();      // target buffer
	    old_index = 0;                       // String start offset
	    
	    // While we have something to replace
	    while((index = str.indexOf(old, old_index)) != -1) {
	      buf.append(str.substring(old_index, index)); // append prefix
	      buf.append(replacement);               // append `replacement'
	      
	      old_index = index + old.length(); // Skip `old'.length chars
	    }

	    buf.append(str.substring(old_index)); // append rest of string
	    str = buf.toString();

	    // Finally push the new string back to the constant pool
	    c = new ConstantUtf8(str);
	    constant_pool[i] = c;
	  }
	} catch(StringIndexOutOfBoundsException e) { // Should not occur
	  System.err.println(e);
	}
      }
    } 
  }  
}
