import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.util.*;
import java.io.*;
import java.util.*;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;

/**
 * Find all classes referenced by given start class and all classes
 * referenced by tjose and so on. In other words: Compute the tranitive
 * hull of classes used by a given class. This is done by checking all
 * ConstantClass entries and all method and field signatures.<br> This
 * may be useful in order to put all class files of an application
 * into a single JAR file.
 * <p>
 * It fails however in the presence of reflection code.
 *
 * @version $Id$
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class TransitiveHull extends org.apache.bcel.classfile.EmptyVisitor {
  private JavaClass    _class;
  private ClassQueue   _queue;
  private ClassSet     _set;
  private ConstantPool _cp;

  private String[] _ignored = {
    "java[.].*",
    "javax[.].*",
    "com[.]sun[.].*"
  };


  public TransitiveHull(JavaClass clazz) {
    _queue = new ClassQueue();
    _queue.enqueue(clazz);
    _set = new ClassSet();
    _set.add(clazz);
  }

  public JavaClass[] getClasses() {
    return _set.toArray();
  }

  public String[] getClassNames() {
    return _set.getClassNames();
  }

  /**
   * Start traversal using DescendingVisitor pattern.
   */
  public void start() {
    while(!_queue.empty()) {
      JavaClass clazz = _queue.dequeue();
      _class = clazz;
      _cp = clazz.getConstantPool();

      new org.apache.bcel.classfile.DescendingVisitor(clazz, this).visit();
    }
  }

  private void add(String class_name) {
    class_name = class_name.replace('/', '.');

    for(int i = 0; i < _ignored.length; i++) {
      if(class_name.matches(_ignored[i])) {
	return; // Ihh
      }
    }

    JavaClass clazz = Repository.lookupClass(class_name);

    if(clazz != null && _set.add(clazz)) {
      _queue.enqueue(clazz);
    }
  }

  public void visitConstantClass(ConstantClass cc) {
    String class_name = (String)cc.getConstantValue(_cp);
    add(class_name);
  }

  private void visitRef(ConstantCP ccp, boolean method) {
    String class_name = ccp.getClass(_cp);
    add(class_name);

    ConstantNameAndType cnat = (ConstantNameAndType)_cp.
      getConstant(ccp.getNameAndTypeIndex(), Constants.CONSTANT_NameAndType);

    String signature = cnat.getSignature(_cp);

    if(method) {
      Type type = Type.getReturnType(signature);

      if(type instanceof ObjectType) {
	add(((ObjectType)type).getClassName());
      }

      Type[] types = Type.getArgumentTypes(signature);

      for(int i = 0; i < types.length; i++) {
	type = types[i];
	if(type instanceof ObjectType) {
	  add(((ObjectType)type).getClassName());
	}
      }
    } else {
      Type type = Type.getType(signature);
      if(type instanceof ObjectType) {
	add(((ObjectType)type).getClassName());
      }
    }
  }

  public void visitConstantMethodref(ConstantMethodref cmr) {
    visitRef(cmr, true);
  }

  public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref cimr) {
    visitRef(cimr, true);
  }

  public void visitConstantFieldref(ConstantFieldref cfr) {
    visitRef(cfr, false);
  }

  public static void main(String[] argv) { 
    ClassParser parser=null;
    JavaClass   java_class;

    try {
      if(argv.length == 0) {
	System.err.println("transitive: No input files specified");
      }
      else {
	if((java_class = Repository.lookupClass(argv[0])) == null) {
	  java_class = new ClassParser(argv[0]).parse();
	}

	TransitiveHull hull = new TransitiveHull(java_class);

	hull.start();
	System.out.println(Arrays.asList(hull.getClassNames()));
      }	  
    } catch(Exception e) {
      e.printStackTrace();
    }
  }        
}
