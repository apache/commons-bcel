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
 */
package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.apache.bcel.AbstractTestCase;
import org.junit.jupiter.api.Test;

public class RecordTestCase extends AbstractTestCase {
    /**
     * An record type, once compiled, should result in a class file that is marked such that we can determine from the
     * access flags (through BCEL) that it is in fact a record
     * @throws IOException 
     * @throws ClassFormatException 
     */
    @Test
    public void testRecordClassSaysItIs() throws ClassNotFoundException, ClassFormatException, IOException {
        JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        assertTrue(clazz.isRecord(), "Expected SimpleEnum class to say it was a record - but it didn't !");
        clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleClass");
        assertFalse(clazz.isRecord(), "Expected SimpleClass class to say it was not a record - but it didn't !");
    }
    
    /**
     * An simple record with two simple fields, an integer and a String field, should
     * show its content in its string representation.
     * @throws ClassNotFoundException 
     * @throws ClassFormatException 
     * @throws IOException 
     */
    @Test
    public void testRecordToString() throws ClassNotFoundException, ClassFormatException, IOException {
        JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        final Attribute[] attributes = clazz.getAttributes();
        assertEquals(4,attributes.length);
        assertEquals("SourceFile: SimpleRecord.java",attributes[0].toString());
        assertEquals("Record(2):\n"
                + "  RecordComponentInfo(aNumber,I,0):\n"
                + "  RecordComponentInfo(aString,Ljava/lang/String;,0):",attributes[1].toString());
        
    }
}
