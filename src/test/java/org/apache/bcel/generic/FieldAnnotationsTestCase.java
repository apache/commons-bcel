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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.Test;

public class FieldAnnotationsTestCase extends AbstractTestCase {
    // helper methods
    public void checkAnnotatedField(final JavaClass clazz, final String fieldname, final String AnnotationEntryName, final String AnnotationEntryElementName,
        final String AnnotationEntryElementValue) {
        final Field[] fields = clazz.getFields();
        for (final Field f : fields) {
            final AnnotationEntry[] fieldAnnotationEntrys = f.getAnnotationEntries();
            if (f.getName().equals(fieldname)) {
                checkAnnotationEntry(fieldAnnotationEntrys[0], AnnotationEntryName, AnnotationEntryElementName, AnnotationEntryElementValue);
            }
        }
    }

    private void checkAnnotationEntry(final AnnotationEntry a, final String name, final String elementname, final String elementvalue) {
        assertEquals(name, a.getAnnotationType(), "Wrong AnnotationEntry name");
        assertEquals(1, a.getElementValuePairs().length, "Wrong number of AnnotationEntry elements");
        final ElementValuePair envp = a.getElementValuePairs()[0];
        assertEquals(envp.getNameString(), elementname, "Wrong element name");
        assertEquals(envp.getValue().stringifyValue(), elementvalue, "Wrong element value");
    }

    /**
     * Check field AnnotationEntrys are retrievable.
     */
    @Test
    public void testFieldAnnotationEntrys() throws ClassNotFoundException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedFields");
        // TODO L...;?
        checkAnnotatedField(clazz, "i", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "2");
    }

    /**
     * Check field AnnotationEntrys (de)serialize ok.
     */
    @Test
    public void testFieldAnnotationEntrysReadWrite() throws ClassNotFoundException, IOException {
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedFields");
        checkAnnotatedField(clazz, "i", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "2");
        // Write it out
        final File tfile = createTestdataFile("AnnotatedFields.class");
        clazz.dump(tfile);
        final SyntheticRepository repos2 = createRepos(".");
        repos2.loadClass("AnnotatedFields");
        checkAnnotatedField(clazz, "i", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "1");
        checkAnnotatedField(clazz, "s", "L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", "id", "2");
        assertTrue(tfile.delete());
    }

    /**
     * Check we can load in a class, modify its field AnnotationEntrys, save it, reload it and everything is correct.
     */
    @Test
    public void testFieldAnnotationModification() throws ClassNotFoundException {
        final boolean dbg = false;
        final JavaClass clazz = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedFields");
        final ClassGen clg = new ClassGen(clazz);
        Field f = clg.getFields()[0];
        if (dbg) {
            System.err.println("Field in freshly constructed class is: " + f);
        }
        if (dbg) {
            System.err.println("AnnotationEntrys on field are: " + dumpAnnotationEntries(f.getAnnotationEntries()));
        }
        final AnnotationEntryGen fruitBasedAnnotationEntry = createFruitAnnotationEntry(clg.getConstantPool(), "Tomato", false);
        final FieldGen fg = new FieldGen(f, clg.getConstantPool());
        if (dbg) {
            System.err.println("Adding AnnotationEntry to the field");
        }
        fg.addAnnotationEntry(fruitBasedAnnotationEntry);
        if (dbg) {
            System.err.println("FieldGen (mutable field) is " + fg);
        }
        if (dbg) {
            System.err.println("with AnnotationEntrys: " + dumpAnnotationEntries(fg.getAnnotationEntries()));
        }
        if (dbg) {
            System.err.println("Replacing original field with new field that has extra AnnotationEntry");
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
            System.err.println("With AnnotationEntrys: " + dumpAnnotationEntries(f.getAnnotationEntries()));
        }
        assertEquals(2, f.getAnnotationEntries().length, "Wrong number of AnnotationEntries");
    }
}
