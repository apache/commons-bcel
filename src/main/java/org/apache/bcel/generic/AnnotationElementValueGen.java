/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.classfile.AnnotationElementValue;
import org.apache.bcel.classfile.ElementValue;

public class AnnotationElementValueGen extends ElementValueGen
{
	// For annotation element values, this is the annotation
	private AnnotationEntryGen a;

	public AnnotationElementValueGen(AnnotationEntryGen a, ConstantPoolGen cpool)
	{
		super(ANNOTATION, cpool);
		this.a = a;
	}

	public AnnotationElementValueGen(int type, AnnotationEntryGen annotation,
			ConstantPoolGen cpool)
	{
		super(type, cpool);
		if (type != ANNOTATION)
			throw new RuntimeException(
					"Only element values of type annotation can be built with this ctor - type specified: " + type);
		this.a = annotation;
	}

	public AnnotationElementValueGen(AnnotationElementValue value,
			ConstantPoolGen cpool, boolean copyPoolEntries)
	{
		super(ANNOTATION, cpool);
		a = new AnnotationEntryGen(value.getAnnotationEntry(), cpool, copyPoolEntries);
	}

	@Override
    public void dump(DataOutputStream dos) throws IOException
	{
		dos.writeByte(type); // u1 type of value (ANNOTATION == '@')
		a.dump(dos);
	}

	@Override
    public String stringifyValue()
	{
		throw new RuntimeException("Not implemented yet");
	}

	/**
	 * Return immutable variant of this AnnotationElementValueGen
	 */
	@Override
    public ElementValue getElementValue()
	{
		return new AnnotationElementValue(this.type, a.getAnnotation(), cpGen
				.getConstantPool());
	}

	public AnnotationEntryGen getAnnotation()
	{
		return a;
	}
}
