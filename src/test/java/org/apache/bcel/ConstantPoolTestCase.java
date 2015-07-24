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

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.ConstantPoolGen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantPoolTestCase extends AbstractTestCase
{

    private final String STRING_VALUE = "Lorem ipsum dolor sit amet";
    private final int INT_VALUE = 42;

    public void testLoadConstantPoolFromByteArray() throws IOException
    {
        ConstantPoolGen constantPoolGen = new ConstantPoolGen();

        constantPoolGen.addString(STRING_VALUE);
        constantPoolGen.addInteger(INT_VALUE);

        ConstantPool generatedConstantPool = constantPoolGen.getFinalConstantPool();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        generatedConstantPool.dump(dataOutputStream);

        byte[] bytes = outputStream.toByteArray();

        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        ConstantPool loadedConstantPool = new ConstantPool(dataInputStream);

        assertEquals("Input stream not wholly read", 0, dataInputStream.available());

        assertEquals(
            generatedConstantPool.toString(),
            loadedConstantPool.toString()
        );

    }

}
