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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ElementValuePair;


public class AnnotationEntryGen
{
	private int typeIndex;

	private List<ElementValuePairGen> evs;

	private ConstantPoolGen cpool;

	private boolean isRuntimeVisible = false;

	/**
	 * Here we are taking a fixed annotation of type Annotation and building a
	 * modifiable AnnotationGen object. If the pool passed in is for a different
	 * class file, then copyPoolEntries should have been passed as true as that
	 * will force us to do a deep copy of the annotation and move the cpool
	 * entries across. We need to copy the type and the element name value pairs
	 * and the visibility.
	 */
	public AnnotationEntryGen(AnnotationEntry a, ConstantPoolGen cpool,
			boolean copyPoolEntries)
	{
		this.cpool = cpool;
		if (copyPoolEntries)
		{
			typeIndex = cpool.addUtf8(a.getAnnotationType());
		}
		else
		{
			typeIndex = a.getAnnotationTypeIndex();
		}
		isRuntimeVisible = a.isRuntimeVisible();
		evs = copyValues(a.getElementValuePairs(), cpool, copyPoolEntries);
	}

	private List<ElementValuePairGen> copyValues(ElementValuePair[] in, ConstantPoolGen cpool,
			boolean copyPoolEntries)
	{
		List<ElementValuePairGen> out = new ArrayList<ElementValuePairGen>();
		int l = in.length;
		for (int i = 0; i < l; i++)
		{
			ElementValuePair nvp = in[i];
			out.add(new ElementValuePairGen(nvp, cpool, copyPoolEntries));
		}
		return out;
	}

	private AnnotationEntryGen(ConstantPoolGen cpool)
	{
		this.cpool = cpool;
	}

	/**
	 * Retrieve an immutable version of this AnnotationGen
	 */
	public AnnotationEntry getAnnotation()
	{
		AnnotationEntry a = new AnnotationEntry(typeIndex, cpool.getConstantPool(),
				isRuntimeVisible);
		for (ElementValuePairGen element : evs) {
			a.addElementNameValuePair(element.getElementNameValuePair());
		}
		return a;
	}

	public AnnotationEntryGen(ObjectType type,
			List<ElementValuePairGen> elements, boolean vis,
			ConstantPoolGen cpool)
	{
		this.cpool = cpool;
		this.typeIndex = cpool.addUtf8(type.getSignature());
		evs = elements;
		isRuntimeVisible = vis;
	}

	public static AnnotationEntryGen read(DataInputStream dis,
			ConstantPoolGen cpool, boolean b) throws IOException
	{
		AnnotationEntryGen a = new AnnotationEntryGen(cpool);
		a.typeIndex = dis.readUnsignedShort();
		int elemValuePairCount = dis.readUnsignedShort();
		for (int i = 0; i < elemValuePairCount; i++)
		{
			int nidx = dis.readUnsignedShort();
			a.addElementNameValuePair(new ElementValuePairGen(nidx,
					ElementValueGen.readElementValue(dis, cpool), cpool));
		}
		a.isRuntimeVisible(b);
		return a;
	}

	public void dump(DataOutputStream dos) throws IOException
	{
		dos.writeShort(typeIndex); // u2 index of type name in cpool
		dos.writeShort(evs.size()); // u2 element_value pair count
		for (int i = 0; i < evs.size(); i++)
		{
			ElementValuePairGen envp = evs.get(i);
			envp.dump(dos);
		}
	}

	public void addElementNameValuePair(ElementValuePairGen evp)
	{
		if (evs == null)
			evs = new ArrayList<ElementValuePairGen>();
		evs.add(evp);
	}

	public int getTypeIndex()
	{
		return typeIndex;
	}

	public final String getTypeSignature()
	{
		// ConstantClass c = (ConstantClass)cpool.getConstant(typeIndex);
		ConstantUtf8 utf8 = (ConstantUtf8) cpool
				.getConstant(typeIndex/* c.getNameIndex() */);
		return utf8.getBytes();
	}

	public final String getTypeName()
	{
		return getTypeSignature();// BCELBUG: Should I use this instead?
									// Utility.signatureToString(getTypeSignature());
	}

	/**
	 * Returns list of ElementNameValuePair objects
	 */
	public List<ElementValuePairGen> getValues()
	{
		return evs;
	}

	@Override
    public String toString()
	{
	    StringBuilder s = new StringBuilder(32);
		s.append("AnnotationGen:[" + getTypeName() + " #" + evs.size() + " {");
		for (int i = 0; i < evs.size(); i++)
		{
			s.append(evs.get(i));
			if (i + 1 < evs.size())
				s.append(",");
		}
		s.append("}]");
		return s.toString();
	}

	public String toShortString()
	{
	    StringBuilder s = new StringBuilder();
		s.append("@" + getTypeName() + "(");
		for (int i = 0; i < evs.size(); i++)
		{
			s.append(evs.get(i));
			if (i + 1 < evs.size())
				s.append(",");
		}
		s.append(")");
		return s.toString();
	}

	private void isRuntimeVisible(boolean b)
	{
		isRuntimeVisible = b;
	}

	public boolean isRuntimeVisible()
	{
		return isRuntimeVisible;
	}
}
