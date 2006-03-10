import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.MethodGen;

/**
 * Test BCEL if an input file is identical to the outfile generated
 * with BCEL. Of course there may some small differences, e.g., because
 * BCEL generates local variable tables by default.
 *
 * Try to:
 * <pre>
% java id <someclass>
% java listclass -code <someclass> &gt; foo
% java listclass -code <someclass>.clazz &gt; bar
% diff foo bar | more
 * <pre>
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class id {
  public static void main(String[] argv) throws Exception { 
    JavaClass clazz = null;

    if((clazz = Repository.lookupClass(argv[0])) == null)
      clazz = new ClassParser(argv[0]).parse(); // May throw IOException

    ClassGen cg = new ClassGen(clazz);

    Method[] methods = clazz.getMethods();

    for(int i=0; i < methods.length; i++) {
      MethodGen mg = new MethodGen(methods[i], cg.getClassName(), cg.getConstantPool());
      cg.replaceMethod(methods[i], mg.getMethod());
    }

    Field[] fields = clazz.getFields();

    for(int i=0; i < fields.length; i++) {
      FieldGen fg =  new FieldGen(fields[i], cg.getConstantPool());
      cg.replaceField(fields[i], fg.getField());
    }

    cg.getJavaClass().dump(clazz.getClassName() + ".clazz");
  }
}
