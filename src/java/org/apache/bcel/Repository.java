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

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.*;
import java.io.*;

/**
 * The repository maintains informations about class interdependencies, e.g.,
 * whether a class is a sub-class of another. Delegates actual class loading
 * to SyntheticRepository with current class path by default.
 *
 * @see org.apache.bcel.util.Repository
 * @see org.apache.bcel.util.SyntheticRepository
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public abstract class Repository {
  private static org.apache.bcel.util.Repository _repository =
    SyntheticRepository.getInstance();

  /** @return currently used repository instance
   */
  public static org.apache.bcel.util.Repository getRepository() {
    return _repository;
  }

  /** Set repository instance to be used for class loading
   */
  public static void setRepository(org.apache.bcel.util.Repository rep) {
    _repository = rep;
  }

  /** Lookup class somewhere found on your CLASSPATH, or whereever the
   * repository instance looks for it.
   *
   * @return class object for given fully qualified class name
   * @throws ClassNotFoundException if the class could not be found or
   * parsed correctly
   */
  public static JavaClass lookupClass(String class_name)
    throws ClassNotFoundException {

    return _repository.loadClass(class_name);
  }

  /**
   * Try to find class source using the internal repository instance.
   * @see Class
   * @return JavaClass object for given runtime class
   * @throws ClassNotFoundException if the class could not be found or
   * parsed correctly
   */
  public static JavaClass lookupClass(Class clazz)
    throws ClassNotFoundException {
    return _repository.loadClass(clazz);
  }

  /**
   * @return class file object for given Java class by looking on the
   *  system class path; returns null if the class file can't be
   *  found
   */
	public static ClassPath.ClassFile lookupClassFile(String class_name) {
			try {
					ClassPath path = _repository.getClassPath();

					if(path != null) {
							return path.getClassFile(class_name);
					}	else {
						return null;
					}
							
			} catch(IOException e) { return null; }
	}

  /** Clear the repository.
   */
  public static void clearCache() {
    _repository.clear();
  }

  /**
   * Add clazz to repository if there isn't an equally named class already in there.
   *
   * @return old entry in repository
   */
  public static JavaClass addClass(JavaClass clazz) {
    JavaClass old = _repository.findClass(clazz.getClassName());
    _repository.storeClass(clazz);
    return old;
  }

  /**
   * Remove class with given (fully qualified) name from repository.
   */
  public static void removeClass(String clazz) {
    _repository.removeClass(_repository.findClass(clazz));
  }

  /**
   * Remove given class from repository.
   */
  public static void removeClass(JavaClass clazz) {
    _repository.removeClass(clazz);
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.,
   * Object is always the last element
   * @throws ClassNotFoundException if any of the superclasses can't be found
   */
  public static JavaClass[] getSuperClasses(JavaClass clazz)
    throws ClassNotFoundException {
    return clazz.getSuperClasses();
  }

  /**
   * @return list of super classes of clazz in ascending order, i.e.,
   * Object is always the last element.
   * @throws ClassNotFoundException if the named class or any of its
   *  superclasses can't be found
   */
  public static JavaClass[] getSuperClasses(String class_name)
    throws ClassNotFoundException {
    JavaClass jc = lookupClass(class_name);
    return getSuperClasses(jc);
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that those interfaces extend, and so on.
   * (Some people call this a transitive hull).
   * @throws ClassNotFoundException if any of the class's
   *  superclasses or superinterfaces can't be found
   */
  public static JavaClass[] getInterfaces(JavaClass clazz)
    throws ClassNotFoundException {
    return clazz.getAllInterfaces();
  }

  /**
   * @return all interfaces implemented by class and its super
   * classes and the interfaces that extend those interfaces, and so on
   * @throws ClassNotFoundException if the named class can't be found,
   *   or if any of its superclasses or superinterfaces can't be found
   */
  public static JavaClass[] getInterfaces(String class_name)
    throws ClassNotFoundException {
    return getInterfaces(lookupClass(class_name));
  }

  /**
   * Equivalent to runtime "instanceof" operator.
   * @return true, if clazz is an instance of super_class
   * @throws ClassNotFoundException if any superclasses or superinterfaces
   *   of clazz can't be found
   */
  public static boolean instanceOf(JavaClass clazz, JavaClass super_class)
    throws ClassNotFoundException {
    return clazz.instanceOf(super_class);
  }

  /**
   * @return true, if clazz is an instance of super_class
   * @throws ClassNotFoundException if either clazz or super_class
   *   can't be found
   */
  public static boolean instanceOf(String clazz, String super_class)
    throws ClassNotFoundException {
    return instanceOf(lookupClass(clazz), lookupClass(super_class));
  }

  /**
   * @return true, if clazz is an instance of super_class
   * @throws ClassNotFoundException if super_class can't be found
   */
  public static boolean instanceOf(JavaClass clazz, String super_class)
    throws ClassNotFoundException {
    return instanceOf(clazz, lookupClass(super_class));
  }

  /**
   * @return true, if clazz is an instance of super_class
   * @throws ClassNotFoundException if clazz can't be found
   */
  public static boolean instanceOf(String clazz, JavaClass super_class)
    throws ClassNotFoundException {
    return instanceOf(lookupClass(clazz), super_class);
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   * @throws ClassNotFoundException if any superclasses or superinterfaces
   *   of clazz can't be found
   */
  public static boolean implementationOf(JavaClass clazz, JavaClass inter)
    throws ClassNotFoundException {
    return clazz.implementationOf(inter);
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   * @throws ClassNotFoundException if clazz, inter, or any superclasses
   *   or superinterfaces of clazz can't be found
   */
  public static boolean implementationOf(String clazz, String inter)
    throws ClassNotFoundException {
    return implementationOf(lookupClass(clazz), lookupClass(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   * @throws ClassNotFoundException if inter or any superclasses
   *   or superinterfaces of clazz can't be found
   */
  public static boolean implementationOf(JavaClass clazz, String inter)
    throws ClassNotFoundException {
    return implementationOf(clazz, lookupClass(inter));
  }

  /**
   * @return true, if clazz is an implementation of interface inter
   * @throws ClassNotFoundException if clazz or any superclasses or
   *   superinterfaces of clazz can't be found
   */
  public static boolean implementationOf(String clazz, JavaClass inter)
    throws ClassNotFoundException {
    return implementationOf(lookupClass(clazz), inter);
  }
}

