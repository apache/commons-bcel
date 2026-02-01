/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.bcel.AbstractTest;
import org.apache.bcel.Const;
import org.apache.bcel.util.SyntheticRepository;
import org.apache.bcel.visitors.CountingVisitor;
import org.junit.jupiter.api.Test;

class RecordTest extends AbstractTest {

    /**
     * Check that we can copy a attribute correctly.
     */
    @Test
    void recordsCanBeCopied() throws ClassNotFoundException, IOException {
        final JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        final JavaClass copyClazz = clazz.copy();
        assertEquals(clazz.toString(), copyClazz.toString(), "both records should have the same value");
    }

    /**
     * Check that a record can be visited by our visitors
     */
    @Test
    void recordsCanBeVisited() throws ClassNotFoundException, IOException {
        final JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        final CountingVisitor countVisitor = new CountingVisitor();
        final DescendingVisitor desendingVisitor = new DescendingVisitor(clazz, countVisitor);
        desendingVisitor.visit();
        assertEquals(1, countVisitor.recordCount, "should count one record");
        assertEquals(2, countVisitor.recordComponentCount, "should count two record components");
    }

    /**
     * Check that we can save and load the attribute correctly.
     */
    @Test
    void testAttributeSerializtion() throws ClassNotFoundException, IOException {
        final JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        final File tfile = createTestdataFile("SimpleRecord.class");
        final Record recordAttribute = (Record) findAttribute("Record", clazz)[0];
        clazz.dump(tfile);
        // Read in the new version and check it is OK
        final SyntheticRepository repos2 = createRepos(".");
        final JavaClass clazzFromRepo = repos2.loadClass("SimpleRecord");
        assertNotNull(clazzFromRepo); // Use the variable to avoid a warning
        final Record recordAttributeFromRepo = (Record) findAttribute("Record", clazzFromRepo)[0];
        assertEquals(recordAttribute.toString(), recordAttributeFromRepo.toString(), "Both attributes needs to be equal");
        tfile.deleteOnExit();
    }

    /**
     * A record type, once compiled, should result in a class file that is
     * marked such that we can determine from the access flags
     * (through BCEL) that it is in fact a record.
     *
     * @throws IOException if an I/O error occurs.
     * @throws ClassFormatException
     */
    @Test
    void testRecordClassSaysItIs() throws ClassNotFoundException, ClassFormatException, IOException {
        final JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        assertTrue(clazz.isRecord(), "Expected SimpleRecord class to say it was a record - but it didn't !");
        final JavaClass simpleClazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleClass");
        assertFalse(simpleClazz.isRecord(), "Expected SimpleClass class to say it was not a record - but it didn't !");
    }

    /**
     * A simple record with two simple fields, an integer and a String field, should
     * show its content in its string representation.
     *
     * @throws ClassNotFoundException
     * @throws ClassFormatException
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testRecordToString() throws ClassNotFoundException, ClassFormatException, IOException {
        final JavaClass clazz = new ClassParser("src/test/resources/record/SimpleRecord.class").parse();
        final Attribute[] attributes = clazz.getAttributes();
        final Record recordAttribute = (Record) findAttribute("Record", clazz)[0];
        assertEquals(4, attributes.length);
        assertEquals("SourceFile: SimpleRecord.java", attributes[0].toString());
        assertEquals("Record(2):\n"
                + "  RecordComponentInfo(aNumber,I,0):\n"
                + "  RecordComponentInfo(aString,Ljava/lang/String;,1):\n"
                + "  RuntimeVisibleAnnotations:\n"
                + "  @Ljavax/annotation/Nonnull;", recordAttribute.toString());
        final RecordComponentInfo firstComponent = recordAttribute.getComponents()[0];
        final RecordComponentInfo secondComponent = recordAttribute.getComponents()[1];
        assertEquals(5, firstComponent.getIndex());
        assertEquals(6, firstComponent.getDescriptorIndex());
        assertEquals(0, firstComponent.getAttributes().length);
        assertEquals(recordAttribute.getConstantPool(), firstComponent.getConstantPool());
        assertEquals("RecordComponentInfo(aNumber,I,0):", firstComponent.toString());
        final RuntimeVisibleAnnotations ann = secondComponent.getAttribute(Const.ATTR_RUNTIME_VISIBLE_ANNOTATIONS);
        assertEquals("RuntimeVisibleAnnotations:\n"
                + "  @Ljavax/annotation/Nonnull;", ann.toString());
        assertNull(secondComponent.getAttribute(Const.ATTR_RUNTIME_INVISIBLE_ANNOTATIONS));
    }

}
