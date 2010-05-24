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

import org.apache.bcel.classfile.AnnotationDefault;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SimpleElementValue;

public class AnnotationDefaultAttributeTestCase extends AbstractTestCase
{
	/**
	 * For values in an annotation that have default values, we should be able
	 * to query the AnnotationDefault attribute against the method to discover
	 * the default value that was originally declared.
	 */
	public void testMethodAnnotations() throws ClassNotFoundException
	{
		JavaClass clazz = getTestClass("org.apache.bcel.data.SimpleAnnotation");
		Method m = getMethod(clazz, "fruit");
		AnnotationDefault a = (AnnotationDefault) findAttribute(
				"AnnotationDefault", m.getAttributes());
		SimpleElementValue val = (SimpleElementValue) a.getDefaultValue();
		assertTrue("Should be STRING but is " + val.getElementValueType(), val
				.getElementValueType() == ElementValue.STRING);
		assertTrue("Should have default of bananas but default is "
				+ val.getValueString(), val.getValueString().equals("bananas"));
	}
}
