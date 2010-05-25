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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.AnnotationEntryGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ElementValueGen;
import org.apache.bcel.generic.ElementValuePairGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.SimpleElementValueGen;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;

public abstract class AbstractTestCase extends TestCase
{
	private boolean verbose = false;

	protected File createTestdataFile(String name)
	{
		return new File("target" + File.separator + "testdata" + File.separator
				+ name);
	}

	protected JavaClass getTestClass(String name) throws ClassNotFoundException
	{
		return SyntheticRepository.getInstance().loadClass(name);
	}

	protected Method getMethod(JavaClass cl, String methodname)
	{
		Method[] methods = cl.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method m = methods[i];
			if (m.getName().equals(methodname))
			{
				return m;
			}
		}
		return null;
	}

	protected boolean wipe(String name)
	{
		return new File("target" + File.separator + "testdata" + File.separator
				+ name).delete();
	}

	protected boolean wipe(String dir, String name)
	{
		boolean b = wipe(dir + File.separator + name);
		String[] files = new File(dir).list();
		if (files == null || files.length == 0)
		{
			new File(dir).delete(); // Why does this not succeed? stupid thing
		}
		return b;
	}

	public SyntheticRepository createRepos(String cpentry)
	{
		ClassPath cp = new ClassPath("target" + File.separator + "testdata"
				+ File.separator + cpentry + File.separator);
		return SyntheticRepository.getInstance(cp);
	}

	protected Attribute[] findAttribute(String name, JavaClass clazz)
	{
		Attribute[] all = clazz.getAttributes();
		List<Attribute> chosenAttrsList = new ArrayList<Attribute>();
		for (int i = 0; i < all.length; i++)
		{
			if (verbose)
				System.err.println("Attribute: " + all[i].getName());
			if (all[i].getName().equals(name))
				chosenAttrsList.add(all[i]);
		}
		return chosenAttrsList.toArray(new Attribute[] {});
	}

	protected Attribute findAttribute(String name, Attribute[] all)
	{
		List<Attribute> chosenAttrsList = new ArrayList<Attribute>();
		for (int i = 0; i < all.length; i++)
		{
			if (verbose)
				System.err.println("Attribute: " + all[i].getName());
			if (all[i].getName().equals(name))
				chosenAttrsList.add(all[i]);
		}
		assertTrue("Should be one match: " + chosenAttrsList.size(),
				chosenAttrsList.size() == 1);
		return chosenAttrsList.get(0);
	}

	protected String dumpAttributes(Attribute[] as)
	{
		StringBuffer result = new StringBuffer();
		result.append("AttributeArray:[");
		for (int i = 0; i < as.length; i++)
		{
			Attribute attr = as[i];
			result.append(attr.toString());
			if (i + 1 < as.length)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	protected String dumpAnnotationEntries(AnnotationEntry[] as)
	{
		StringBuffer result = new StringBuffer();
		result.append("[");
		for (int i = 0; i < as.length; i++)
		{
			AnnotationEntry annotation = as[i];
			result.append(annotation.toShortString());
			if (i + 1 < as.length)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	protected String dumpAnnotationEntries(AnnotationEntryGen[] as)
	{
		StringBuffer result = new StringBuffer();
		result.append("[");
		for (int i = 0; i < as.length; i++)
		{
			AnnotationEntryGen annotation = as[i];
			result.append(annotation.toShortString());
			if (i + 1 < as.length)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}

	public AnnotationEntryGen createFruitAnnotationEntry(ConstantPoolGen cp,
			String aFruit, boolean visibility)
	{
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.STRING, cp, aFruit);
		ElementValuePairGen nvGen = new ElementValuePairGen("fruit", evg, cp);
		ObjectType t = new ObjectType("SimpleStringAnnotation");
		List<ElementValuePairGen> elements = new ArrayList<ElementValuePairGen>();
		elements.add(nvGen);
		return new AnnotationEntryGen(t, elements, visibility, cp);
	}
}
