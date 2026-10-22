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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.verifier.statics.StringRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests the type annotation attributes, read from {@code TypeAnnotated.class} (see {@code TypeAnnotated.java}).
 */
class TypeAnnotationsTest {

    private static final String CLASS_FILE = "src/test/resources/typeannotations/TypeAnnotated.class";

    private static JavaClass parse() throws IOException {
        return new ClassParser(CLASS_FILE).parse();
    }

    private static TypeAnnotations typeAnnotationsOf(final FieldOrMethod member, final byte tag) {
        return (TypeAnnotations) Stream.of(member.getAttributes()).filter(a -> a.getTag() == tag).findFirst().orElse(null);
    }

    private static Method method(final JavaClass clazz, final String name) {
        return Stream.of(clazz.getMethods()).filter(m -> m.getName().equals(name)).findFirst().orElseThrow(AssertionError::new);
    }

    private static Field field(final JavaClass clazz, final String name) {
        return Stream.of(clazz.getFields()).filter(f -> f.getName().equals(name)).findFirst().orElseThrow(AssertionError::new);
    }

    @Test
    void readsTheTypeAnnotationOfAField() throws IOException {
        final TypeAnnotations annotations = typeAnnotationsOf(field(parse(), "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        assertNotNull(annotations);
        assertTrue(annotations instanceof RuntimeVisibleTypeAnnotations);
        assertTrue(annotations.isRuntimeVisible());
        assertEquals(1, annotations.getNumTypeAnnotations());
        final TypeAnnotationEntry entry = annotations.getTypeAnnotationEntries()[0];
        assertEquals(TypeAnnotationEntry.TARGET_FIELD, entry.getTargetType());
        assertEquals(0, entry.getTargetInfo().length);
        assertEquals(0, entry.getTypePathLength());
        assertEquals("Ltypeannotations/Visible;", entry.getAnnotationEntry().getAnnotationType());
    }

    @Test
    void readsTheTypePathOfATypeArgument() throws IOException {
        final TypeAnnotations annotations = typeAnnotationsOf(field(parse(), "items"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        final TypeAnnotationEntry entry = annotations.getTypeAnnotationEntries()[0];
        assertEquals(TypeAnnotationEntry.TARGET_FIELD, entry.getTargetType());
        assertEquals(1, entry.getTypePathLength());
        // a type argument of the parameterized type, the first one
        assertEquals(3, entry.getTypePathKind(0));
        assertEquals(0, entry.getTypePathArgumentIndex(0));
    }

    @Test
    void readsTheInvisibleAttribute() throws IOException {
        final Field hidden = field(parse(), "hidden");
        assertNull(typeAnnotationsOf(hidden, Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS));
        final TypeAnnotations annotations = typeAnnotationsOf(hidden, Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);
        assertTrue(annotations instanceof RuntimeInvisibleTypeAnnotations);
        assertFalse(annotations.isRuntimeVisible());
        assertEquals("Ltypeannotations/Invisible;", annotations.getTypeAnnotationEntries()[0].getAnnotationEntry().getAnnotationType());
    }

    @Test
    void readsTheTargetsOfAMethod() throws IOException {
        final TypeAnnotations annotations = typeAnnotationsOf(method(parse(), "find"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        final List<Integer> targets = new ArrayList<>();
        for (final TypeAnnotationEntry entry : annotations) {
            targets.add(entry.getTargetType());
            if (entry.getTargetType() == TypeAnnotationEntry.TARGET_METHOD_FORMAL_PARAMETER) {
                // the index of the parameter
                assertArrayEquals(new byte[] {0}, entry.getTargetInfo());
            }
        }
        assertTrue(targets.contains(TypeAnnotationEntry.TARGET_METHOD_RETURN));
        assertTrue(targets.contains(TypeAnnotationEntry.TARGET_METHOD_FORMAL_PARAMETER));
    }

    @Test
    void readsTheBoundOfATypeParameterOfTheClass() throws IOException {
        final JavaClass clazz = parse();
        final TypeAnnotations annotations = (TypeAnnotations) Stream.of(clazz.getAttributes())
                .filter(a -> a.getTag() == Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).findFirst().orElse(null);
        assertNotNull(annotations);
        final TypeAnnotationEntry entry = annotations.getTypeAnnotationEntries()[0];
        assertEquals(TypeAnnotationEntry.TARGET_CLASS_TYPE_PARAMETER_BOUND, entry.getTargetType());
        // the first type parameter, its first bound
        assertArrayEquals(new byte[] {0, 0}, entry.getTargetInfo());
    }

    @Test
    void dumpsWhatItReads() throws IOException {
        final JavaClass clazz = parse();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(out)) {
            clazz.dump(dos);
        }
        final JavaClass reread = new ClassParser(new ByteArrayInputStream(out.toByteArray()), CLASS_FILE).parse();
        for (final String name : new String[] {"field", "items", "hidden"}) {
            final Field original = field(clazz, name);
            final Field copy = field(reread, name);
            assertEquals(original.getAttributes().length, copy.getAttributes().length);
            for (int i = 0; i < original.getAttributes().length; i++) {
                assertEquals(original.getAttributes()[i].toString(), copy.getAttributes()[i].toString());
            }
        }
    }

    @Test
    void copiesTheAttribute() throws IOException {
        final JavaClass clazz = parse();
        final TypeAnnotations annotations = typeAnnotationsOf(field(clazz, "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        assertEquals(annotations.toString(), annotations.copy(clazz.getConstantPool()).toString());
    }

    @Test
    void isVisitedAndRepresented() throws IOException {
        final JavaClass clazz = parse();
        final int[] visited = new int[1];
        new DescendingVisitor(clazz, new EmptyVisitor() {
            @Override
            public void visitTypeAnnotations(final TypeAnnotations obj) {
                visited[0]++;
            }
        }).visit();
        // the class, three fields and one method have one
        assertEquals(5, visited[0]);
        final StringRepresentation representation = new StringRepresentation(typeAnnotationsOf(field(clazz, "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS));
        assertTrue(representation.toString().contains("RuntimeVisibleTypeAnnotations"), representation.toString());
    }

    @Test
    void readsTheTableOfALocalVariableTarget() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeByte(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE);
            dos.writeShort(2);
            for (int i = 0; i < 2; i++) {
                dos.writeShort(1 + i);
                dos.writeShort(5);
                dos.writeShort(i);
            }
            dos.writeByte(0);
            dos.writeShort(1);
            dos.writeShort(0);
        }
        final ConstantPool constantPool = parse().getConstantPool();
        final TypeAnnotationEntry entry = TypeAnnotationEntry.read(new DataInputStream(new ByteArrayInputStream(out.toByteArray())), constantPool, true);
        assertEquals(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE, entry.getTargetType());
        assertEquals(2 + 2 * 6, entry.getTargetInfo().length);
        final ByteArrayOutputStream dumped = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(dumped)) {
            entry.dump(dos);
        }
        assertArrayEquals(out.toByteArray(), dumped.toByteArray());
    }

    @Test
    void rejectsAnUndefinedTargetType() throws IOException {
        final ConstantPool constantPool = parse().getConstantPool();
        final DataInputStream input = new DataInputStream(new ByteArrayInputStream(new byte[] {0x7F, 0, 0, 0}));
        assertThrows(ClassFormatException.class, () -> TypeAnnotationEntry.read(input, constantPool, true));
    }
}
