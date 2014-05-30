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
import java.io.IOException;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.AnnotationEntryGen;
import org.apache.bcel.util.SyntheticRepository;

public class FieldAnnotationsTestCase extends AbstractTestCase
{
    /**
     * Check field AnnotationEntrys are retrievable.
     */
    public void testFieldAnnotationEntrys() throws ClassNotFoundException
    {
        JavaClass clazz = getTestClass("org.apache.bcel.data.AnnotatedFields");
        // TODO L...;?
        checkAnnotatedField(clazz, "i", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "2");
    }

    /**
     * Check field AnnotationEntrys (de)serialize ok.
     */
    public void testFieldAnnotationEntrysReadWrite() throws ClassNotFoundException,
            IOException
    {
        JavaClass clazz = getTestClass("org.apache.bcel.data.AnnotatedFields");
        checkAnnotatedField(clazz, "i", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "2");
        // Write it out
        File tfile = createTestdataFile("AnnotatedFields.class");
        clazz.dump(tfile);
        SyntheticRepository repos2 = createRepos(".");
        repos2.loadClass("AnnotatedFields");
        checkAnnotatedField(clazz, "i", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "Lorg/apache/bcel/data/SimpleAnnotation;", "id", "2");
        assertTrue(tfile.delete());
    }

    /**
     * Check we can load in a class, modify its field AnnotationEntrys, save it,
     * reload it and everything is correct.
     */
    public void testFieldAnnotationModification()
            throws ClassNotFoundException
    {
        boolean dbg = false;
        JavaClass clazz = getTestClass("org.apache.bcel.data.AnnotatedFields");
        ClassGen clg = new ClassGen(clazz);
        Field f = clg.getFields()[0];
        if (dbg) {
            System.err.println("Field in freshly constructed class is: " + f);
        }
        if (dbg) {
            System.err.println("AnnotationEntrys on field are: "
                    + dumpAnnotationEntries(f.getAnnotationEntries()));
        }
        AnnotationEntryGen fruitBasedAnnotationEntry = createFruitAnnotationEntry(clg
                .getConstantPool(), "Tomato", false);
        FieldGen fg = new FieldGen(f, clg.getConstantPool());
        if (dbg) {
            System.err.println("Adding AnnotationEntry to the field");
        }
        fg.addAnnotationEntry(fruitBasedAnnotationEntry);
        if (dbg) {
            System.err.println("FieldGen (mutable field) is " + fg);
        }
        if (dbg) {
            System.err.println("with AnnotationEntrys: "
                    + dumpAnnotationEntries(fg.getAnnotationEntries()));
        }
        if (dbg) {
            System.err
                    .println("Replacing original field with new field that has extra AnnotationEntry");
        }
        clg.removeField(f);
        clg.addField(fg.getField());
        f = clg.getFields()[1]; // there are two fields in the class, removing
                                // and readding has changed the order
        // so this time index [1] is the 'int i' field
        if (dbg) {
            System.err.println("Field now looks like this: " + f);
        }
        if (dbg) {
            System.err.println("With AnnotationEntrys: "
                    + dumpAnnotationEntries(f.getAnnotationEntries()));
        }
        assertTrue("Should be 2 AnnotationEntrys on this field, but there are "
                + f.getAnnotationEntries().length, f.getAnnotationEntries().length == 2);
    }

    // helper methods
    public void checkAnnotatedField(JavaClass clazz, String fieldname,
            String AnnotationEntryName, String AnnotationEntryElementName,
            String AnnotationEntryElementValue)
    {
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            AnnotationEntry[] fieldAnnotationEntrys = f.getAnnotationEntries();
            if (f.getName().equals(fieldname))
            {
                checkAnnotationEntry(fieldAnnotationEntrys[0], AnnotationEntryName,
                        AnnotationEntryElementName, AnnotationEntryElementValue);
            }
        }
    }

    private void checkAnnotationEntry(AnnotationEntry a, String name, String elementname,
            String elementvalue)
    {
        assertTrue("Expected AnnotationEntry to have name " + name
                + " but it had name " + a.getAnnotationType(), a.getAnnotationType()
                .equals(name));
        assertTrue("Expected AnnotationEntry to have one element but it had "
                + a.getElementValuePairs().length, a.getElementValuePairs().length == 1);
        ElementValuePair envp = a.getElementValuePairs()[0];
        assertTrue("Expected element name " + elementname + " but was "
                + envp.getNameString(), elementname
                .equals(envp.getNameString()));
        assertTrue("Expected element value " + elementvalue + " but was "
                + envp.getValue().stringifyValue(), elementvalue.equals(envp
                .getValue().stringifyValue()));
    }

    // helper methods
    public void checkValue(AnnotationEntry a, String name, String tostring)
    {
        for (int i = 0; i < a.getElementValuePairs().length; i++)
        {
            ElementValuePair element = a.getElementValuePairs()[i];
            if (element.getNameString().equals(name))
            {
                if (!element.getValue().stringifyValue().equals(tostring))
                {
                    fail("Expected element " + name + " to have value "
                            + tostring + " but it had value "
                            + element.getValue().stringifyValue());
                }
                return;
            }
        }
        fail("Didnt find named element " + name);
    }
}
