/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;

public class AnnotationAccessFlagTestCase extends AbstractTestCase
{
	/**
	 * If you write an annotation and compile it, the class file generated
	 * should be marked as an annotation type - which is detectable through
	 * BCEL.
	 */
	public void testAnnotationClassSaysItIs() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.SimpleAnnotation");
		assertTrue(
				"Expected SimpleAnnotation class to say it was an annotation - but it didn't !",
				clazz.isAnnotation());
		clazz = getTestClass("org.apache.bcel.data.SimpleClass");
		assertTrue(
				"Expected SimpleClass class to say it was not an annotation - but it didn't !",
				!clazz.isAnnotation());
	}
}
