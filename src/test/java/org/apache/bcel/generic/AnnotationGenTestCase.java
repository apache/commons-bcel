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
 */

package org.apache.bcel.generic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.RuntimeInvisibleAnnotations;
import org.apache.bcel.classfile.RuntimeVisibleAnnotations;

public class AnnotationGenTestCase extends AbstractTestCase
{
    private ClassGen createClassGen(final String classname)
    {
        return new ClassGen(classname, "java.lang.Object", "<generated>",
                Const.ACC_PUBLIC | Const.ACC_SUPER, null);
    }

    /**
     * Programmatically construct an mutable annotation (AnnotationGen) object.
     */
    public void testConstructMutableAnnotation()
    {
        // Create the containing class
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        // Create the simple primitive value '4' of type 'int'
        final SimpleElementValueGen evg = new SimpleElementValueGen(
                ElementValueGen.PRIMITIVE_INT, cp, 4);
        // Give it a name, call it 'id'
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg,
                cp);
        // Check it looks right
        assertTrue(
                "Should include string 'id=4' but says: " + nvGen.toString(),
                nvGen.toString().contains("id=4"));
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        // Build an annotation of type 'SimpleAnnotation' with 'id=4' as the
        // only value :)
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
        // Check we can save and load it ok
        checkSerialize(a, cp);
    }

    public void testVisibleInvisibleAnnotationGen()
    {
        // Create the containing class
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        // Create the simple primitive value '4' of type 'int'
        final SimpleElementValueGen evg = new SimpleElementValueGen(
                ElementValueGen.PRIMITIVE_INT, cp, 4);
        // Give it a name, call it 'id'
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg,
                cp);
        // Check it looks right
        assertTrue(
                "Should include string 'id=4' but says: " + nvGen.toString(),
                nvGen.toString().contains("id=4"));
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        // Build a RV annotation of type 'SimpleAnnotation' with 'id=4' as the
        // only value :)
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
        final List<AnnotationEntryGen> v = new ArrayList<>();
        v.add(a);
        final Attribute[] attributes = AnnotationEntryGen.getAnnotationAttributes(cp, v.toArray(new AnnotationEntryGen[0]));
        boolean foundRV = false;
        for (final Attribute attribute : attributes) {
            if (attribute instanceof RuntimeVisibleAnnotations)
            {
                assertTrue(((Annotations) attribute).isRuntimeVisible());
                foundRV = true;
            }
        }
        assertTrue("Should have seen a RuntimeVisibleAnnotation", foundRV);
        // Build a RIV annotation of type 'SimpleAnnotation' with 'id=4' as the
        // only value :)
        final AnnotationEntryGen a2 = new AnnotationEntryGen(t, elements, false, cp);
        final List<AnnotationEntryGen> v2 = new ArrayList<>();
        v2.add(a2);
        final Attribute[] attributes2 = AnnotationEntryGen.getAnnotationAttributes(cp, v2.toArray(new AnnotationEntryGen[0]));
        boolean foundRIV = false;
        for (final Attribute attribute : attributes2) {
            if (attribute instanceof RuntimeInvisibleAnnotations)
            {
                assertFalse(((Annotations) attribute).isRuntimeVisible());
                foundRIV = true;
            }
        }
        assertTrue("Should have seen a RuntimeInvisibleAnnotation", foundRIV);
    }

    private void checkSerialize(final AnnotationEntryGen a, final ConstantPoolGen cpg)
    {
        try
        {
            final String beforeName = a.getTypeName();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (DataOutputStream dos = new DataOutputStream(baos)) {
                a.dump(dos);
                dos.flush();
            }
            final byte[] bs = baos.toByteArray();
            final ByteArrayInputStream bais = new ByteArrayInputStream(bs);
            AnnotationEntryGen annAfter;
            try (DataInputStream dis = new DataInputStream(bais)) {
                annAfter = AnnotationEntryGen.read(dis, cpg, a.isRuntimeVisible());
            }
            final String afterName = annAfter.getTypeName();
            if (!beforeName.equals(afterName))
            {
                fail("Deserialization failed: before type='" + beforeName
                        + "' after type='" + afterName + "'");
            }
            if (a.getValues().size() != annAfter.getValues().size())
            {
                fail("Different numbers of element name value pairs?? "
                        + a.getValues().size() + "!="
                        + annAfter.getValues().size());
            }
            for (int i = 0; i < a.getValues().size(); i++)
            {
                final ElementValuePairGen beforeElement = a.getValues().get(i);
                final ElementValuePairGen afterElement = annAfter.getValues().get(i);
                if (!beforeElement.getNameString().equals(
                        afterElement.getNameString()))
                {
                    fail("Different names?? " + beforeElement.getNameString()
                            + "!=" + afterElement.getNameString());
                }
            }
        }
        catch (final IOException ioe)
        {
            fail("Unexpected exception whilst checking serialization: " + ioe);
        }
    }
}
