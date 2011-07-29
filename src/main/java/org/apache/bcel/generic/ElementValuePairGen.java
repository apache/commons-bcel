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
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;

public class ElementValuePairGen
{
	private int nameIdx;

	private ElementValueGen value;

	private ConstantPoolGen cpool;

	public ElementValuePairGen(ElementValuePair nvp, ConstantPoolGen cpool,
			boolean copyPoolEntries)
	{
		this.cpool = cpool;
		// J5ASSERT:
		// Could assert nvp.getNameString() points to the same thing as
		// cpool.getConstant(nvp.getNameIndex())
		// if
		// (!nvp.getNameString().equals(((ConstantUtf8)cpool.getConstant(nvp.getNameIndex())).getBytes()))
		// {
		// throw new RuntimeException("envp buggered");
		// }
		if (copyPoolEntries)
		{
			nameIdx = cpool.addUtf8(nvp.getNameString());
		}
		else
		{
			nameIdx = nvp.getNameIndex();
		}
		value = ElementValueGen.copy(nvp.getValue(), cpool, copyPoolEntries);
	}

	/**
	 * Retrieve an immutable version of this ElementNameValuePairGen
	 */
	public ElementValuePair getElementNameValuePair()
	{
		ElementValue immutableValue = value.getElementValue();
		return new ElementValuePair(nameIdx, immutableValue, cpool
				.getConstantPool());
	}

	protected ElementValuePairGen(int idx, ElementValueGen value,
			ConstantPoolGen cpool)
	{
		this.nameIdx = idx;
		this.value = value;
		this.cpool = cpool;
	}

	public ElementValuePairGen(String name, ElementValueGen value,
			ConstantPoolGen cpool)
	{
		this.nameIdx = cpool.addUtf8(name);
		this.value = value;
		this.cpool = cpool;
	}

	protected void dump(DataOutputStream dos) throws IOException
	{
		dos.writeShort(nameIdx); // u2 name of the element
		value.dump(dos);
	}

	public int getNameIndex()
	{
		return nameIdx;
	}

	public final String getNameString()
	{
		// ConstantString cu8 = (ConstantString)cpool.getConstant(nameIdx);
		return ((ConstantUtf8) cpool.getConstant(nameIdx)).getBytes();
	}

	public final ElementValueGen getValue()
	{
		return value;
	}

	@Override
    public String toString()
	{
		return "ElementValuePair:[" + getNameString() + "="
				+ value.stringifyValue() + "]";
	}
}
