package org.apache.bcel;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache BCEL" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache BCEL", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import org.apache.bcel.classfile.*;
import org.apache.bcel.util.*;
import java.util.HashMap;
import java.io.*;

/** 
 * Repository maintains informations about class interdependencies, e.g.
 * whether a class is a sub-class of another. JavaClass objects are put
 * into a cache which can be purged with clearCache().
 *
 * All JavaClass objects used as arguments must have been obtained via
 * the repository or been added with addClass() manually. This is
 * because we have to check for object identity (==).
 *
 * @version $Id$
 * @author <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A
 */
public abstract class Repository {
  private static ClassPath class_path = new ClassPath();
  private static HashMap classes;
  private static JavaClass OBJECT; // should be final ...

  static { clearCache(); }

  /** @return class object for given fully qualified class name, or null
   * if the class could not be found or parsed correctly
   */
  public static JavaClass lookupClass(String class_name) {
    if(class_name == null || class_name.equals(""))
      throw new RuntimeException("Invalid class name");

    class_name = class_name.replace('/', '.');

    JavaClass clazz = (JavaClass)classes.get(class_name);

    if(clazz == null) {
      try {
	InputStream is = class_path.getInputStream(class_name);
	clazz = new ClassParser(is, class_name).parse();
	class_name = clazz.getClassName();
      } catch(IOException e) {
	// Don't throw exception since there may be other ways to load
	return null;
      }

      classes.put(class_name, clazz);
    }

    return clazz;
  }

  /**
   * Try to find class source via getResourceAsStream()
   * @return JavaClass object for given runtime class
   */
  public static JavaClass lookupClass(Class clazz) {
    String class_name = clazz.getName();

    JavaClass j_class = (JavaClass)classes.get(class_name);

    if(j_class == null) {
      String name = class_name;
      int    i    = name.lastIndexOf('.');

      if(i > 0)
	name = name.substring(i + 1);

      try {
	InputStream is = clazz.getResourceAsStream(name + ".class");
	j_class = new ClassParser(is, class_name).parse();
      } catch(IOException e) {
	throw new RuntimeException(e.getMessage());
      }

      classes.put(class_name, j_class);
    }

    return j_class;
  }

  /** @return class file object for given Java class.
   */
  public static ClassPath.ClassFile lookupClassFile(String class_name) {
    try {
      return class_path.getClassFile(class_name);
    } catch(IOException e) { return null; }
  }

  /** Clear the repository.
   */
  public static void clearCache() {
    classes = new HashMap();
    OBJECT  = lookupClass("java.lang.Object");

    if(OBJECT == null)
      System.err.println("Warning: java.lang.Object not found on CLASSPATH!");
    else
      classes.put("java.lang.Object", OBJECT);
  }

  /**
   * Add clazz to repository if there isn't an equally named class already in there.
   *
   * @return old entry in repository
   */
  public static JavaClass addClass(JavaClass clazz) {
    String    name = clazz.getClassName();
    JavaClass cl   = (JavaClass)classes.get(name);

    if(cl == null)
      classes.put(name, cl = clazz);

    return cl;
  }

  /**
   * Remove class with given (fully qualifid) name from repository.
   */
  public static void removeClass(String clazz) {
    classes.remove(clazz);
  }

  /**
   * Remove given class from repository.
   */
  public static void removeClass(JavaClass clazz) {
    removeClass(clazz.getClassName());
  }


  private static final JavaClass getSuperClass(JavaClass clazz) {
    if(clazz == OBJECT)
      return null;

    return lookupClass(clazz.getSuperclassName());
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.,
   * Object is always the last element
   */
  public static JavaClass[] getSuperClasses(JavaClass clazz) {
    ClassVector vec = new ClassVector();

    for(clazz = getSuperClass(clazz); clazz != null; clazz = getSuperClass(clazz))
      vec.addElement(clazz);

    return vec.toArray();
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.,
   * Object is always the last element. "null", if clazz cannot be found.
   */
  public static JavaClass[] getSuperClasses(String class_name) {
    JavaClass jc = lookupClass(class_name);
    return (jc == null? null : getSuperClasses(jc));
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that those interfaces extend, and so on
   */
  public static JavaClass[] getInterfaces(JavaClass clazz) {
    ClassVector vec   = new ClassVector();
    ClassQueue  queue = new ClassQueue();

    queue.enqueue(clazz);

    while(!queue.empty()) {
      clazz = queue.dequeue();

      String   s          = clazz.getSuperclassName();
      String[] interfaces = clazz.getInterfaceNames();

      if(clazz.isInterface())
	vec.addElement(clazz);
      else if(!s.equals("java.lang.Object"))
	queue.enqueue(lookupClass(s));
      
      for(int i=0; i < interfaces.length; i++)
	queue.enqueue(lookupClass(interfaces[i]));
    }

    return vec.toArray();
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that extend those interfaces, and so on
   */
  public static JavaClass[] getInterfaces(String class_name) {
    return getInterfaces(lookupClass(class_name));
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(JavaClass clazz, JavaClass super_class) {
    if(clazz == super_class)
      return true;

    JavaClass[] super_classes = getSuperClasses(clazz);

    for(int i=0; i < super_classes.length; i++)
      if(super_classes[i] == super_class)
	return true;

    if(super_class.isInterface())
      return implementationOf(clazz, super_class);

    return false;
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(String clazz, String super_class) {
    return instanceOf(lookupClass(clazz), lookupClass(super_class));
  }
    
  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(JavaClass clazz, String super_class) {
    return instanceOf(clazz, lookupClass(super_class));
  }

  /**
   * @return true, if clazz is an instance of super_class
   */
  public static boolean instanceOf(String clazz, JavaClass super_class) {
    return instanceOf(lookupClass(clazz), super_class);
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(JavaClass clazz, JavaClass inter) {
    if(clazz == inter)
      return true;

    JavaClass[] super_interfaces = getInterfaces(clazz);

    for(int i=0; i < super_interfaces.length; i++)
      if(super_interfaces[i] == inter)
	return true;

    return false;
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(String clazz, String inter) {
    return implementationOf(lookupClass(clazz), lookupClass(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(JavaClass clazz, String inter) {
    return implementationOf(clazz, lookupClass(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   */
  public static boolean implementationOf(String clazz, JavaClass inter) {
    return implementationOf(lookupClass(clazz), inter);
  }
}
