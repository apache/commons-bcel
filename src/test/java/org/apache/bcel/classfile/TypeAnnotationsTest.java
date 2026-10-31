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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.verifier.statics.StringRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests the type annotation attributes, read from {@code TypeAnnotated.class} (see {@code TypeAnnotated.java}).
 */
class TypeAnnotationsTest {

    static class AnnotatedTypes {
        @TypeUse Outer.Inner outer;
        Outer.@TypeUse Inner inner;
        Outer.@TypeUse Nested nested;
        List<? extends @TypeUse Number[]> wildcard;

        String cast(final Object value) {
            @TypeUse final String local = (@TypeUse String) value;
            return local;
        }
    }

    static class Outer {
        class Inner {
        }

        static class Nested {
        }
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TypeUse {
    }

    private static final String CLASS_FILE = "src/test/resources/typeannotations/TypeAnnotated.class";

    private static JavaClass annotatedTypes() throws IOException {
        final String resource = "TypeAnnotationsTest$AnnotatedTypes.class";
        try (InputStream input = TypeAnnotationsTest.class.getResourceAsStream(resource)) {
            assertNotNull(input);
            return new ClassParser(input, resource).parse();
        }
    }

    private static Field field(final JavaClass clazz, final String name) {
        return Stream.of(clazz.getFields()).filter(f -> f.getName().equals(name)).findFirst().orElseThrow(AssertionError::new);
    }

    private static Method method(final JavaClass clazz, final String name) {
        return Stream.of(clazz.getMethods()).filter(m -> m.getName().equals(name)).findFirst().orElseThrow(AssertionError::new);
    }

    private static JavaClass parse() throws IOException {
        return new ClassParser(CLASS_FILE).parse();
    }

    private static TypeAnnotations typeAnnotationsOf(final FieldOrMethod member, final byte tag) {
        return (TypeAnnotations) Stream.of(member.getAttributes()).filter(a -> a.getTag() == tag).findFirst().orElse(null);
    }

    @Test
    void copiesTheAttributeWithItsOwnAnnotationsAndTheGivenConstantPool() throws IOException {
        final JavaClass clazz = parse();
        final ConstantPool otherPool = clazz.getConstantPool().copy();
        final TypeAnnotations original = typeAnnotationsOf(field(clazz, "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        final TypeAnnotations copy = (TypeAnnotations) original.copy(otherPool);
        assertEquals(original.toString(), copy.toString());
        assertTrue(copy instanceof RuntimeVisibleTypeAnnotations);
        assertTrue(copy.isRuntimeVisible());
        // the copy is bound to the given pool, the original to its own
        assertSame(otherPool, copy.getConstantPool());
        assertSame(otherPool, copy.getTypeAnnotationEntries()[0].getAnnotationEntry().getConstantPool());
        assertSame(clazz.getConstantPool(), original.getConstantPool());
        assertSame(clazz.getConstantPool(), original.getTypeAnnotationEntries()[0].getAnnotationEntry().getConstantPool());
        // nothing is shared: changing the copy leaves the original unchanged
        assertNotSame(original.getTypeAnnotationEntries(), copy.getTypeAnnotationEntries());
        assertNotSame(original.getTypeAnnotationEntries()[0], copy.getTypeAnnotationEntries()[0]);
        assertNotSame(original.getTypeAnnotationEntries()[0].getAnnotationEntry(), copy.getTypeAnnotationEntries()[0].getAnnotationEntry());
        copy.getTypeAnnotationEntries()[0].getAnnotationEntry().addElementNameValuePair(
                new ElementValuePair(1, new SimpleElementValue(ElementValue.PRIMITIVE_INT, 1, otherPool), otherPool));
        copy.setTypeAnnotationTable(null);
        assertEquals(0, original.getTypeAnnotationEntries()[0].getAnnotationEntry().getNumElementValuePairs());
        assertEquals(1, original.getNumTypeAnnotations());
    }

    @Test
    void copiesTheInvisibleAttribute() throws IOException {
        final JavaClass clazz = parse();
        final TypeAnnotations original = typeAnnotationsOf(field(clazz, "hidden"), Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);
        final TypeAnnotations copy = (TypeAnnotations) original.copy(clazz.getConstantPool().copy());
        assertTrue(copy instanceof RuntimeInvisibleTypeAnnotations);
        assertFalse(copy.isRuntimeVisible());
        assertFalse(copy.getTypeAnnotationEntries()[0].getAnnotationEntry().isRuntimeVisible());
        assertEquals(original.toString(), copy.toString());
    }

    @Test
    void distinguishesInnerClassesFromStaticNestedClasses() throws IOException {
        final JavaClass clazz = annotatedTypes();
        final TypeAnnotationEntry outer = typeAnnotationsOf(field(clazz, "outer"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).getTypeAnnotationEntries()[0];
        final TypeAnnotationEntry inner = typeAnnotationsOf(field(clazz, "inner"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).getTypeAnnotationEntries()[0];
        final TypeAnnotationEntry nested = typeAnnotationsOf(field(clazz, "nested"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).getTypeAnnotationEntries()[0];
        assertEquals(0, outer.getTypePathLength());
        assertEquals(1, inner.getTypePathLength());
        assertEquals(1, inner.getTypePathKind(0));
        assertEquals(0, inner.getTypePathArgumentIndex(0));
        assertEquals(0, nested.getTypePathLength());
    }

    @Test
    void dumpsWhatItReads() throws IOException {
        final JavaClass clazz = parse();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(out)) {
            clazz.dump(dos);
        }
        assertArrayEquals(Files.readAllBytes(Paths.get(CLASS_FILE)), out.toByteArray());
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
    void followsTypeArgumentsWildcardBoundsAndArrayElements() throws IOException {
        final TypeAnnotationEntry entry = typeAnnotationsOf(field(annotatedTypes(), "wildcard"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS)
                .getTypeAnnotationEntries()[0];
        assertEquals(3, entry.getTypePathLength());
        assertEquals(3, entry.getTypePathKind(0));
        assertEquals(2, entry.getTypePathKind(1));
        assertEquals(0, entry.getTypePathKind(2));
        for (int step = 0; step < 3; step++) {
            assertEquals(0, entry.getTypePathArgumentIndex(step));
        }
    }

    @Test
    void isRepresented() throws IOException {
        final JavaClass clazz = parse();
        final StringRepresentation representation = new StringRepresentation(typeAnnotationsOf(field(clazz, "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS));
        assertTrue(representation.toString().contains("RuntimeVisibleTypeAnnotations"), representation.toString());
    }

    @Test
    void isVisitedWithItsAnnotations() throws IOException {
        final JavaClass clazz = parse();
        final List<String> events = new ArrayList<>();
        final DescendingVisitor[] descending = new DescendingVisitor[1];
        descending[0] = new DescendingVisitor(clazz, new EmptyVisitor() {
            @Override
            public void visitAnnotationEntry(final AnnotationEntry obj) {
                // the annotation is visited within its attribute
                events.add("entry " + obj.getAnnotationType() + " in " + descending[0].predecessor().getClass().getSimpleName());
            }

            @Override
            public void visitTypeAnnotations(final TypeAnnotations obj) {
                events.add("attribute " + obj.getClass().getSimpleName());
            }
        });
        descending[0].visit();
        // the class, three fields and one method have an attribute, which holds six annotations in all
        assertEquals(5, events.stream().filter(e -> e.startsWith("attribute")).count());
        assertEquals(6, events.stream().filter(e -> e.startsWith("entry")).count());
        assertEquals(1, events.stream().filter(e -> e.equals("attribute RuntimeInvisibleTypeAnnotations")).count());
        assertEquals(1, events.stream().filter(e -> e.equals("entry Ltypeannotations/Invisible; in RuntimeInvisibleTypeAnnotations")).count());
        assertEquals(5, events.stream().filter(e -> e.startsWith("entry Ltypeannotations/Visible; in RuntimeVisibleTypeAnnotations")).count());
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
    void readsTheInvisibleAttribute() throws IOException {
        final Field hidden = field(parse(), "hidden");
        assertNull(typeAnnotationsOf(hidden, Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS));
        final TypeAnnotations annotations = typeAnnotationsOf(hidden, Const.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);
        assertTrue(annotations instanceof RuntimeInvisibleTypeAnnotations);
        assertFalse(annotations.isRuntimeVisible());
        assertEquals("Ltypeannotations/Invisible;", annotations.getTypeAnnotationEntries()[0].getAnnotationEntry().getAnnotationType());
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
    void rejectsAMalformedTypePath() throws IOException {
        final AnnotationEntry annotation = typeAnnotationsOf(field(parse(), "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).getTypeAnnotationEntries()[0]
                .getAnnotationEntry();
        // a path is made of pairs of bytes
        assertThrows(IllegalArgumentException.class, () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_FIELD, new byte[0], new byte[1], annotation));
        assertThrows(IllegalArgumentException.class, () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_FIELD, new byte[0], new byte[3], annotation));
        // and has at most 255 steps
        assertThrows(IllegalArgumentException.class, () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_FIELD, new byte[0], new byte[256 * 2], annotation));
        assertEquals(255, new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_FIELD, new byte[0], new byte[255 * 2], annotation).getTypePathLength());
    }

    @Test
    void rejectsAnUndefinedTargetType() throws IOException {
        final ConstantPool constantPool = parse().getConstantPool();
        final DataInputStream input = new DataInputStream(new ByteArrayInputStream(new byte[] {0x7F, 0, 0, 0}));
        assertThrows(ClassFormatException.class, () -> TypeAnnotationEntry.read(input, constantPool, true));
    }

    @Test
    void rejectsATargetInfoOfTheWrongSize() throws IOException {
        final AnnotationEntry annotation = typeAnnotationsOf(field(parse(), "field"), Const.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS).getTypeAnnotationEntries()[0]
                .getAnnotationEntry();
        assertThrows(IllegalArgumentException.class, () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_FIELD, new byte[1], new byte[0], annotation));
        assertThrows(IllegalArgumentException.class,
                () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_METHOD_FORMAL_PARAMETER, new byte[0], new byte[0], annotation));
        assertThrows(IllegalArgumentException.class,
                () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_CLASS_TYPE_PARAMETER_BOUND, new byte[3], new byte[0], annotation));
        assertThrows(ClassFormatException.class, () -> new TypeAnnotationEntry(0x7F, new byte[0], new byte[0], annotation));
        // a table of two ranges of 6 bytes, announced as one
        final byte[] twoRangesAnnouncedAsOne = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertThrows(IllegalArgumentException.class,
                () -> new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE, twoRangesAnnouncedAsOne, new byte[0], annotation));
        assertEquals(2, new TypeAnnotationEntry(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE, new byte[2], new byte[0], annotation).getTargetInfo().length);
    }

    @Test
    void visitsCompilerGeneratedCodeAnnotations() throws IOException {
        final JavaClass clazz = annotatedTypes();
        final Code code = method(clazz, "cast").getCode();
        final TypeAnnotations annotations = (TypeAnnotations) Stream.of(code.getAttributes()).filter(TypeAnnotations.class::isInstance).findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals(2, annotations.getNumTypeAnnotations());
        final List<Integer> targets = new ArrayList<>();
        annotations.forEach(entry -> targets.add(entry.getTargetType()));
        assertTrue(targets.contains(TypeAnnotationEntry.TARGET_LOCAL_VARIABLE));
        assertTrue(targets.contains(TypeAnnotationEntry.TARGET_CAST));
        for (final TypeAnnotationEntry entry : annotations) {
            try (DataInputStream info = new DataInputStream(new ByteArrayInputStream(entry.getTargetInfo()))) {
                if (entry.getTargetType() == TypeAnnotationEntry.TARGET_CAST) {
                    final int offset = info.readUnsignedShort();
                    assertTrue(offset < code.getCode().length);
                    // javac 8 points to the operand load here, while newer compilers can point to checkcast.
                    assertNotNull(new InstructionList(code.getCode()).findHandle(offset), "The cast annotation must point to an instruction boundary");
                    assertEquals(0, info.readUnsignedByte()); // first type in the cast
                } else {
                    final int ranges = info.readUnsignedShort();
                    assertTrue(ranges > 0);
                    for (int range = 0; range < ranges; range++) {
                        final int start = info.readUnsignedShort();
                        final int length = info.readUnsignedShort();
                        assertTrue(start < code.getCode().length);
                        assertTrue(start + length <= code.getCode().length);
                        assertTrue(info.readUnsignedShort() < code.getMaxLocals());
                    }
                }
                assertEquals(0, info.available());
            }
        }
        final int[] entries = {0};
        final DescendingVisitor[] descending = new DescendingVisitor[1];
        descending[0] = new DescendingVisitor(clazz, new EmptyVisitor() {
            @Override
            public void visitAnnotationEntry(final AnnotationEntry entry) {
                if (descending[0].predecessor() == annotations) {
                    assertSame(code, descending[0].predecessor(1));
                    entries[0]++;
                }
            }
        });
        descending[0].visit();
        assertEquals(2, entries[0]);
    }
}
