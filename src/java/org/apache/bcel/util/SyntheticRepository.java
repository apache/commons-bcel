package org.apache.bcel.util;

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

import java.io.*;

import java.util.Map;
import java.util.HashMap;

import org.apache.bcel.classfile.*;

/**
 * This repository is used in situations where a 
 * Class is created outside the realm of a ClassLoader.
 *
 * It is designed to be used as a singleton, however it
 * can also be used with custom classpaths.
 */

public class SyntheticRepository
    implements Repository
{
    private static Map instances = 
	new HashMap(); // CLASSPATH X REPOSITORY

    private ClassPath path = null;
    private static String defaultPath = ClassPath.getClassPath();

    private Map loadedClasses =
	new HashMap(); // CLASSNAME X JAVACLASS

    public SyntheticRepository( String classPath ) {
	path = new ClassPath( classPath );
	instances.put( classPath, this );
    }

    public SyntheticRepository( ) {
	path = new ClassPath( );
	instances.put( ClassPath.getClassPath(), this );
    }

    public static SyntheticRepository getInstance() {
      SyntheticRepository rep =(SyntheticRepository)
	instances.get( defaultPath );

      if(rep == null) {
	rep = new SyntheticRepository(); // adds itself to instances
      }
      
      return rep;
    }

    public static SyntheticRepository getInstance(String classPath) {
	return (SyntheticRepository) instances.get( classPath );
    }

    /**
     * Store a new JavaClass into this Repository.
     */
    public void storeClass( JavaClass clazz ) {
	loadedClasses.put( clazz.getClassName(),
			   clazz );
    }

    /**
     * Find an already defined JavaClass.
     */
    public JavaClass findClass( String className ) {
	if ( loadedClasses.containsKey( className )) {
	    return (JavaClass) loadedClasses.get( className );
	} else {
	    return null;
	}
    }

    /**
     * Lookup a JavaClass object from the Class Name provided.
     */
    public JavaClass loadClass( String className ) 
	throws ClassNotFoundException
    {
	JavaClass RC = findClass( className );
	if (RC != null) { 
	    return RC; 
	} 

	try {
	    
	    InputStream is = path.getInputStream( className, ".class" );
	    if (is != null) {
		ClassParser parser = new ClassParser( is, className );
		RC = parser.parse();
		
		storeClass( RC );
		
		return RC;
	    }
	} catch (IOException e) {
	    throw new ClassNotFoundException("Exception looking for class " + 
					     className + ": " + e.toString());
	    
	}
	throw new ClassNotFoundException("SyntheticRepository does not load new classes.");
    }
}
