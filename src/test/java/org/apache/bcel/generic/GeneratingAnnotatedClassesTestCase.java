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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.ParameterAnnotationEntry;
import org.apache.bcel.classfile.SimpleElementValue;
import org.apache.bcel.util.SyntheticRepository;
import org.junit.jupiter.api.Test;

/**
 * The program that some of the tests generate looks like this:
 *
 * <pre>
 * public class HelloWorld {
 *     public static void main(String[] argv) {
 *         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 *         String name = null;
 *
 *         try {
 *             name = &quot;Andy&quot;;
 *         } catch (IOException e) {
 *             return;
 *         }
 *         System.out.println(&quot;Hello, &quot; + name);
 *     }
 * }
 * </pre>
 */
public class GeneratingAnnotatedClassesTestCase extends AbstractTestCase {
    private void assertArrayElementValue(final int nExpectedArrayValues, final AnnotationEntry anno) {
        final ElementValuePair elementValuePair = anno.getElementValuePairs()[0];
        assertEquals("value", elementValuePair.getNameString());
        final ArrayElementValue ev = (ArrayElementValue) elementValuePair.getValue();
        final ElementValue[] eva = ev.getElementValuesArray();
        assertEquals(nExpectedArrayValues, eva.length);
    }

    private void assertMethodAnnotations(final Method method, final int expectedNumberAnnotations, final int nExpectedArrayValues) {
        final String methodName = method.getName();
        final AnnotationEntry[] annos = method.getAnnotationEntries();
        assertEquals(expectedNumberAnnotations, annos.length, () -> "For " + methodName);
        if (expectedNumberAnnotations != 0) {
            assertArrayElementValue(nExpectedArrayValues, annos[0]);
        }
    }

    private void assertParameterAnnotations(final Method method, final int... expectedNumberOfParmeterAnnotations) {
        final String methodName = "For " + method.getName();
        final ParameterAnnotationEntry[] parameterAnnotations = method.getParameterAnnotationEntries();
        assertEquals(expectedNumberOfParmeterAnnotations.length, parameterAnnotations.length, methodName);

        int i = 0;
        for (final ParameterAnnotationEntry parameterAnnotation : parameterAnnotations) {
            final AnnotationEntry[] annos = parameterAnnotation.getAnnotationEntries();
            final int expectedLength = expectedNumberOfParmeterAnnotations[i++];
            final int j = i;
            assertEquals(expectedLength, annos.length, () -> methodName + " parameter " + j);
            if (expectedLength != 0) {
                assertSimpleElementValue(annos[0]);
            }
        }
    }

    private void assertSimpleElementValue(final AnnotationEntry anno) {
        final ElementValuePair elementValuePair = anno.getElementValuePairs()[0];
        assertEquals("id", elementValuePair.getNameString());
        final SimpleElementValue ev = (SimpleElementValue) elementValuePair.getValue();
        assertEquals(42, ev.getValueInt());
    }

    private void buildClassContents(final ClassGen cg, final ConstantPoolGen cp, final InstructionList il) {
        // Create method 'public static void main(String[]argv)'
        final MethodGen mg = createMethodGen("main", il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);
        // We now define some often used types:
        final ObjectType iStream = new ObjectType("java.io.InputStream");
        final ObjectType pStream = new ObjectType("java.io.PrintStream");
        // Create variables in and name : We call the constructors, i.e.,
        // execute BufferedReader(InputStreamReader(System.in)) . The reference
        // to the BufferedReader object stays on top of the stack and is stored
        // in the newly allocated in variable.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", iStream, Const.GETSTATIC));
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>", Type.VOID, new Type[] {iStream}, Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID, new Type[] {new ObjectType("java.io.Reader")}, Const.INVOKESPECIAL));
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType("java.io.BufferedReader"), null, null);
        final int in = lg.getIndex();
        lg.setStart(il.append(new ASTORE(in))); // "in" valid from here
        // Create local variable name and initialize it to null
        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        final int name = lg.getIndex();
        il.append(InstructionConst.ACONST_NULL);
        lg.setStart(il.append(new ASTORE(name))); // "name" valid from here
        // Create try-catch block: We remember the start of the block, read a
        // line from the standard input and store it into the variable name .
        // InstructionHandle try_start = il.append(factory.createFieldAccess(
        // "java.lang.System", "out", p_stream, Constants.GETSTATIC));
        // il.append(new PUSH(cp, "Please enter your name> "));
        // il.append(factory.createInvoke("java.io.PrintStream", "print",
        // Type.VOID, new Type[] { Type.STRING },
        // Constants.INVOKEVIRTUAL));
        // il.append(new ALOAD(in));
        // il.append(factory.createInvoke("java.io.BufferedReader", "readLine",
        // Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        final InstructionHandle tryStart = il.append(new PUSH(cp, "Andy"));
        il.append(new ASTORE(name));
        // Upon normal execution we jump behind exception handler, the target
        // address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle tryEnd = il.append(g);
        // We add the exception handler which simply returns from the method.
        final LocalVariableGen varEx = mg.addLocalVariable("ex", Type.getType("Ljava.io.IOException;"), null, null);
        final int varExSlot = varEx.getIndex();
        final InstructionHandle handler = il.append(new ASTORE(varExSlot));
        varEx.setStart(handler);
        varEx.setEnd(il.append(InstructionConst.RETURN));
        mg.addExceptionHandler(tryStart, tryEnd, handler, new ObjectType("java.io.IOException"));
        // "Normal" code continues, now we can set the branch target of the GOTO
        // .
        final InstructionHandle ih = il.append(factory.createFieldAccess("java.lang.System", "out", pStream, Const.GETSTATIC));
        g.setTarget(ih);
        // Printing "Hello": String concatenation compiles to StringBuffer
        // operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il.append(factory.createInvoke("java.lang.StringBuffer", "<init>", Type.VOID, new Type[] {Type.STRING}, Const.INVOKESPECIAL));
        il.append(new ALOAD(name));
        il.append(factory.createInvoke("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        // Finalization: Finally, we have to set the stack size, which normally
        // would have to be computed on the fly and add a default constructor
        // method to the class, which is empty in this case.
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());
        il.dispose(); // Allow instruction handles to be reused
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
    }

    private void buildClassContentsWithAnnotatedMethods(final ClassGen cg, final ConstantPoolGen cp, final InstructionList il) {
        // Create method 'public static void main(String[]argv)'
        final MethodGen mg = createMethodGen("main", il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);
        mg.addAnnotationEntry(createSimpleVisibleAnnotation(mg.getConstantPool()));
        // We now define some often used types:
        final ObjectType iStream = new ObjectType("java.io.InputStream");
        final ObjectType pStream = new ObjectType("java.io.PrintStream");
        // Create variables in and name : We call the constructors, i.e.,
        // execute BufferedReader(InputStreamReader(System.in)) . The reference
        // to the BufferedReader object stays on top of the stack and is stored
        // in the newly allocated in variable.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", iStream, Const.GETSTATIC));
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>", Type.VOID, new Type[] {iStream}, Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID, new Type[] {new ObjectType("java.io.Reader")}, Const.INVOKESPECIAL));
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType("java.io.BufferedReader"), null, null);
        final int in = lg.getIndex();
        lg.setStart(il.append(new ASTORE(in))); // "in" valid from here
        // Create local variable name and initialize it to null
        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        final int name = lg.getIndex();
        il.append(InstructionConst.ACONST_NULL);
        lg.setStart(il.append(new ASTORE(name))); // "name" valid from here
        // Create try-catch block: We remember the start of the block, read a
        // line from the standard input and store it into the variable name .
        // InstructionHandle try_start = il.append(factory.createFieldAccess(
        // "java.lang.System", "out", p_stream, Constants.GETSTATIC));
        // il.append(new PUSH(cp, "Please enter your name> "));
        // il.append(factory.createInvoke("java.io.PrintStream", "print",
        // Type.VOID, new Type[] { Type.STRING },
        // Constants.INVOKEVIRTUAL));
        // il.append(new ALOAD(in));
        // il.append(factory.createInvoke("java.io.BufferedReader", "readLine",
        // Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        final InstructionHandle tryStart = il.append(new PUSH(cp, "Andy"));
        il.append(new ASTORE(name));
        // Upon normal execution we jump behind exception handler, the target
        // address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle tryEnd = il.append(g);
        // We add the exception handler which simply returns from the method.
        final LocalVariableGen varEx = mg.addLocalVariable("ex", Type.getType("Ljava.io.IOException;"), null, null);
        final int varExSlot = varEx.getIndex();
        final InstructionHandle handler = il.append(new ASTORE(varExSlot));
        varEx.setStart(handler);
        varEx.setEnd(il.append(InstructionConst.RETURN));
        mg.addExceptionHandler(tryStart, tryEnd, handler, new ObjectType("java.io.IOException"));
        // "Normal" code continues, now we can set the branch target of the GOTO
        // .
        final InstructionHandle ih = il.append(factory.createFieldAccess("java.lang.System", "out", pStream, Const.GETSTATIC));
        g.setTarget(ih);
        // Printing "Hello": String concatenation compiles to StringBuffer
        // operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il.append(factory.createInvoke("java.lang.StringBuffer", "<init>", Type.VOID, new Type[] {Type.STRING}, Const.INVOKESPECIAL));
        il.append(new ALOAD(name));
        il.append(factory.createInvoke("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] {Type.STRING}, Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        // Finalization: Finally, we have to set the stack size, which normally
        // would have to be computed on the fly and add a default constructor
        // method to the class, which is empty in this case.
        mg.setMaxStack();
        mg.setMaxLocals();
        cg.addMethod(mg.getMethod());
        il.dispose(); // Allow instruction handles to be reused
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
    }

    // helper methods
    private ClassGen createClassGen(final String className) {
        return new ClassGen(className, "java.lang.Object", "<generated>", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
    }

    public AnnotationEntryGen createCombinedAnnotation(final ConstantPoolGen cp) {
        // Create an annotation instance
        final AnnotationEntryGen a = createSimpleVisibleAnnotation(cp);
        final ArrayElementValueGen array = new ArrayElementValueGen(cp);
        array.addElement(new AnnotationElementValueGen(a, cp));
        final ElementValuePairGen nvp = new ElementValuePairGen("value", array, cp);
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvp);
        return new AnnotationEntryGen(new ObjectType("CombinedAnnotation"), elements, true, cp);
    }

    public AnnotationEntryGen createFruitAnnotation(final ConstantPoolGen cp, final String aFruit) {
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.STRING, cp, aFruit);
        final ElementValuePairGen nvGen = new ElementValuePairGen("fruit", evg, cp);
        final ObjectType t = new ObjectType("SimpleStringAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        return new AnnotationEntryGen(t, elements, true, cp);
    }

    private MethodGen createMethodGen(final String methodname, final InstructionList il, final ConstantPoolGen cp) {
        return new MethodGen(Const.ACC_STATIC | Const.ACC_PUBLIC, // access
            // flags
            Type.VOID, // return type
            new Type[] {new ArrayType(Type.STRING, 1)}, // argument
            // types
            new String[] {"argv"}, // arg names
            methodname, "HelloWorld", // method, class
            il, cp);
    }

    public AnnotationEntryGen createSimpleInvisibleAnnotation(final ConstantPoolGen cp) {
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_INT, cp, 4);
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, false, cp);
        return a;
    }

    public AnnotationEntryGen createSimpleVisibleAnnotation(final ConstantPoolGen cp) {
        final SimpleElementValueGen evg = new SimpleElementValueGen(ElementValueGen.PRIMITIVE_INT, cp, 4);
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
        return a;
    }

    private void dumpClass(final ClassGen cg, final String fname) {
        try {
            final File f = createTestdataFile(fname);
            cg.getJavaClass().dump(f);
        } catch (final java.io.IOException e) {
            System.err.println(e);
        }
    }

    private void dumpClass(final ClassGen cg, final String dir, final String fname) {
        dumpClass(cg, dir + File.separator + fname);
    }

    private JavaClass getClassFrom(final String where, final String clazzname) throws ClassNotFoundException {
        // System.out.println(where);
        final SyntheticRepository repos = createRepos(where);
        return repos.loadClass(clazzname);
    }

    /**
     * Steps in the test:
     * <ol>
     * <li>Programmatically construct the HelloWorld program</li>
     * <li>Add two simple annotations at the class level</li>
     * <li>Save the class to disk</li>
     * <li>Reload the class using the 'static' variant of the BCEL classes</li>
     * <li>Check the attributes are OK</li>
     * </ol>
     */
    @Test
    public void testGenerateClassLevelAnnotations() throws ClassNotFoundException {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        cg.setMajor(49);
        cg.setMinor(0);
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        cg.addAnnotationEntry(createSimpleVisibleAnnotation(cp));
        cg.addAnnotationEntry(createSimpleInvisibleAnnotation(cp));
        buildClassContents(cg, cp, il);
        // System.out.println(cg.getJavaClass().toString());
        dumpClass(cg, "HelloWorld.class");
        final JavaClass jc = getClassFrom(".", "HelloWorld");
        final AnnotationEntry[] as = jc.getAnnotationEntries();
        assertEquals(2, as.length, "Wrong number of AnnotationEntries");
        // TODO L??;
        assertEquals("LSimpleAnnotation;", as[0].getAnnotationType(), "Wrong name of annotation 1");
        assertEquals("LSimpleAnnotation;", as[1].getAnnotationType(), "Wrong name of annotation 2");
        final ElementValuePair[] vals = as[0].getElementValuePairs();
        final ElementValuePair nvp = vals[0];
        assertEquals("id", nvp.getNameString(), "Wrong name of element in SimpleAnnotation");
        final ElementValue ev = nvp.getValue();
        assertEquals(ElementValue.PRIMITIVE_INT, ev.getElementValueType(), "Wrong type of element value");
        assertEquals("4", ev.stringifyValue(), "Wrong value of element");
        assertTrue(createTestdataFile("HelloWorld.class").delete());
    }

    /**
     * Just check that we can dump a class that has a method annotation on it and it is still there when we read it back in
     */
    @Test
    public void testGenerateMethodLevelAnnotations1() throws ClassNotFoundException {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        buildClassContentsWithAnnotatedMethods(cg, cp, il);
        // Check annotation is OK
        int i = cg.getMethods()[0].getAnnotationEntries().length;
        assertEquals(1, i, "Wrong number of annotations of main method prior to dumping");
        dumpClass(cg, "temp1" + File.separator + "HelloWorld.class");
        final JavaClass jc2 = getClassFrom("temp1", "HelloWorld");
        // Check annotation is OK
        i = jc2.getMethods()[0].getAnnotationEntries().length;
        assertEquals(1, i, "Wrong number of annotation on JavaClass");
        final ClassGen cg2 = new ClassGen(jc2);
        // Check it now it is a ClassGen
        final Method[] m = cg2.getMethods();
        i = m[0].getAnnotationEntries().length;
        assertEquals(1, i, "Wrong number of annotations on the main 'Method'");
        final FieldGenOrMethodGen mg = new MethodGen(m[0], cg2.getClassName(), cg2.getConstantPool());
        // Check it finally when the Method is changed to a MethodGen
        i = mg.getAnnotationEntries().length;
        assertEquals(1, i, "Wrong number of annotations on the main 'MethodGen'");

        assertTrue(delete("temp1", "HelloWorld.class"));
    }

    /**
     * Going further than the last test - when we reload the method back in, let's change it (adding a new annotation) and
     * then store that, read it back in and verify both annotations are there ! Also check that we can remove method
     * annotations.
     */
    @Test
    public void testGenerateMethodLevelAnnotations2() throws ClassNotFoundException {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        buildClassContentsWithAnnotatedMethods(cg, cp, il);
        dumpClass(cg, "temp2", "HelloWorld.class");
        final JavaClass jc2 = getClassFrom("temp2", "HelloWorld");
        final ClassGen cg2 = new ClassGen(jc2);
        // Main method after reading the class back in
        final Method mainMethod1 = jc2.getMethods()[0];
        assertEquals(1, mainMethod1.getAnnotationEntries().length, "Wrong number of annotations of the 'Method'");
        final MethodGen mainMethod2 = new MethodGen(mainMethod1, cg2.getClassName(), cg2.getConstantPool());
        assertEquals(1, mainMethod2.getAnnotationEntries().length, "Wrong number of annotations of the 'MethodGen'");
        final AnnotationEntryGen fruit = createFruitAnnotation(cg2.getConstantPool(), "Pear");
        mainMethod2.addAnnotationEntry(fruit);
        cg2.removeMethod(mainMethod1);
        cg2.addMethod(mainMethod2.getMethod());
        dumpClass(cg2, "temp3", "HelloWorld.class");
        final JavaClass jc3 = getClassFrom("temp3", "HelloWorld");
        final ClassGen cg3 = new ClassGen(jc3);
        final Method mainMethod3 = cg3.getMethods()[1];
        final int i = mainMethod3.getAnnotationEntries().length;
        assertEquals(2, i, "Wrong number of annotations on the 'Method'");
        mainMethod2.removeAnnotationEntry(fruit);
        assertEquals(1, mainMethod2.getAnnotationEntries().length, "Wrong number of annotations on the 'MethodGen'");
        mainMethod2.removeAnnotationEntries();
        assertEquals(0, mainMethod2.getAnnotationEntries().length, 0, "Wrong number of annotations on the 'MethodGen'");
        assertTrue(delete("temp2", "HelloWorld.class"));
        assertTrue(delete("temp3", "HelloWorld.class"));
    }

    /**
     * Load a class in and modify it with a new attribute - A SimpleAnnotation annotation
     */
    @Test
    public void testModifyingClasses1() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        final ConstantPoolGen cp = cgen.getConstantPool();
        cgen.addAnnotationEntry(createFruitAnnotation(cp, "Pineapple"));
        assertEquals(2, cgen.getAnnotationEntries().length, "Wrong number of annotations");
        dumpClass(cgen, "SimpleAnnotatedClass.class");
        assertTrue(delete("SimpleAnnotatedClass.class"));
    }

    /**
     * Load a class in and modify it with a new attribute - A ComplexAnnotation annotation
     */
    @Test
    public void testModifyingClasses2() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        final ConstantPoolGen cp = cgen.getConstantPool();
        cgen.addAnnotationEntry(createCombinedAnnotation(cp));
        assertEquals(2, cgen.getAnnotationEntries().length, "Wrong number of annotations");
        dumpClass(cgen, "SimpleAnnotatedClass.class");
        final JavaClass jc2 = getClassFrom(".", "SimpleAnnotatedClass");
        jc2.getAnnotationEntries();
        assertTrue(delete("SimpleAnnotatedClass.class"));
        // System.err.println(jc2.toString());
    }

    /**
     * Transform simple class from an immutable to a mutable object. The class is annotated with an annotation that uses an
     * array of SimpleAnnotations.
     */
    @Test
    public void testTransformClassToClassGen_ArrayAndAnnotationTypes() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedWithCombinedAnnotation");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertEquals(1, annotations.length, "Wrong number of annotations");
        final AnnotationEntryGen a = annotations[0];
        assertEquals(1, a.getValues().size(), "Wrong number of values for the annotation");
        final ElementValuePairGen nvp = a.getValues().get(0);
        final ElementValueGen value = nvp.getValue();
        assertTrue(value instanceof ArrayElementValueGen, "Value should be ArrayElementValueGen but is " + value);
        final ArrayElementValueGen arrayValue = (ArrayElementValueGen) value;
        assertEquals(1, arrayValue.getElementValuesSize(), "Wrong size of the array");
        final ElementValueGen innerValue = arrayValue.getElementValues().get(0);
        assertTrue(innerValue instanceof AnnotationElementValueGen, "Value in the array should be AnnotationElementValueGen but is " + innerValue);
        final AnnotationElementValueGen innerAnnotationValue = (AnnotationElementValueGen) innerValue;
        assertEquals("L" + PACKAGE_BASE_SIG + "/data/SimpleAnnotation;", innerAnnotationValue.getAnnotation().getTypeSignature(), "Wrong type signature");

        // check the three methods
        final Method[] methods = cgen.getMethods();
        assertEquals(3, methods.length);
        for (final Method method : methods) {
            final String methodName = method.getName();
            if (methodName.equals("<init>")) {
                assertMethodAnnotations(method, 0, 1);
                assertParameterAnnotations(method, 0, 1);
            } else if (methodName.equals("methodWithArrayOfZeroAnnotations")) {
                assertMethodAnnotations(method, 1, 0);
            } else if (methodName.equals("methodWithArrayOfTwoAnnotations")) {
                assertMethodAnnotations(method, 1, 2);
            } else {
                fail(() -> "unexpected method " + method.getName());
            }
        }
    }

    /**
     * Transform simple class from an immutable to a mutable object. The class is annotated with an annotation that uses an
     * enum.
     */
    @Test
    public void testTransformClassToClassGen_EnumType() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.AnnotatedWithEnumClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertEquals(1, annotations.length, "Wrong number of annotations");
    }

    // J5TODO: Need to add deleteFile calls to many of these tests
    /**
     * Transform simple class from an immutable to a mutable object.
     */
    @Test
    public void testTransformClassToClassGen_SimpleTypes() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertEquals(1, annotations.length, "Wrong number of annotations");
    }

    /**
     * Transform complex class from an immutable to a mutable object.
     */
    @Test
    public void testTransformComplexClassToClassGen() throws ClassNotFoundException {
        final JavaClass jc = getTestJavaClass(PACKAGE_BASE_NAME + ".data.ComplexAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertEquals(1, annotations.length, "Wrong number of annotations");
        final List<?> l = annotations[0].getValues();
        boolean found = false;
        for (final Object name : l) {
            final ElementValuePairGen element = (ElementValuePairGen) name;
            if (element.getNameString().equals("dval") && element.getValue().stringifyValue().equals("33.4")) {
                found = true;
            }
        }
        assertTrue(found, "Did not find double annotation value with value 33.4");
    }
}
