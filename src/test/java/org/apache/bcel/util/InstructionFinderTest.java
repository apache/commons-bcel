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

package org.apache.bcel.util;

import java.util.Iterator;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.generic.IADD;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

public class InstructionFinderTest extends AbstractTestCase {

	public void testSearch() {
		InstructionList il = new InstructionList();
		il.append(new ILOAD(1));
		il.append(new ILOAD(2));
		il.append(new IADD());
		il.append(new ISTORE(3));
		InstructionFinder finder = new InstructionFinder(il);
		
		Iterator<?> it = finder.search("ILOAD IADD", il.getInstructionHandles()[0], null );
		InstructionHandle[] ihs = (InstructionHandle[])it.next();
		assertEquals(2, ihs.length);
		assertEquals(ihs[0].getInstruction(), new ILOAD(2));
		assertEquals(ihs[1].getInstruction(), new IADD());
	}
}
