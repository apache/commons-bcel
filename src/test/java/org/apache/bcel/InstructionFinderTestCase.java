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

import java.util.Iterator;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.util.InstructionFinder;

public class InstructionFinderTestCase extends AbstractTestCase
{
    public void testSearchAll() throws Exception
    {
        JavaClass clazz = getTestClass("org.apache.bcel.util.InstructionFinder");
        Method[] methods = clazz.getMethods();
        Method searchM = null;
        for (Method m : methods)
        {
            if (m.getName().equals("search") && (m.getArgumentTypes().length == 3))
            {
                searchM = m;
                break;
            }
        }

        if (searchM == null) {
            throw new Exception("search method not found");
        }

        byte[] bytes = searchM.getCode().getCode();
        InstructionList il = new InstructionList(bytes);
        InstructionFinder finder = new InstructionFinder(il);
        Iterator<?> it = finder.search(".*", il.getStart(), null);

        InstructionHandle[] ihs = (InstructionHandle[])it.next();
        int size = 0;
        for (InstructionHandle ih : ihs)
        {
            size += ih.getInstruction().getLength();
        }
        assertEquals(bytes.length, size);

    }
}
