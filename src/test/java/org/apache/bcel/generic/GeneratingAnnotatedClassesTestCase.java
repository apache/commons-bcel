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
package org.apache.bcel.generic;

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

/**
 * The program that some of the tests generate looks like this:
 *
 * <pre>
 * public class HelloWorld
 * {
 *  public static void main(String[] argv)
 *  {
 *      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 *      String name = null;
 *
 *      try
 *      {
 *          name = &quot;Andy&quot;;
 *      }
 *      catch (IOException e)
 *      {
 *          return;
 *      }
 *      System.out.println(&quot;Hello, &quot; + name);
 *  }
 * }
 * </pre>
 */
public class GeneratingAnnotatedClassesTestCase extends AbstractTestCase
{
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
    public void testGenerateClassLevelAnnotations()
            throws ClassNotFoundException
    {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        cg.setMajor(49);
        cg.setMinor(0);
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        cg.addAnnotationEntry(createSimpleVisibleAnnotation(cp));
        cg.addAnnotationEntry(createSimpleInvisibleAnnotation(cp));
        buildClassContents(cg, cp, il);
        //System.out.println(cg.getJavaClass().toString());
        dumpClass(cg, "HelloWorld.class");
        final JavaClass jc = getClassFrom(".", "HelloWorld");
        final AnnotationEntry[] as = jc.getAnnotationEntries();
        assertTrue("Should be two AnnotationEntries but found " + as.length,
                as.length == 2);
        // TODO L??;
        assertTrue(
                "Name of annotation 1 should be LSimpleAnnotation; but it is "
                        + as[0].getAnnotationType(), as[0].getAnnotationType()
                        .equals("LSimpleAnnotation;"));
        assertTrue(
                "Name of annotation 2 should be LSimpleAnnotation; but it is "
                        + as[1].getAnnotationType(), as[1].getAnnotationType()
                        .equals("LSimpleAnnotation;"));
        final ElementValuePair[] vals = as[0].getElementValuePairs();
        final ElementValuePair nvp = vals[0];
        assertTrue(
                "Name of element in SimpleAnnotation should be 'id' but it is "
                        + nvp.getNameString(), nvp.getNameString().equals("id"));
        final ElementValue ev = nvp.getValue();
        assertTrue("Type of element value should be int but it is "
                + ev.getElementValueType(),
                ev.getElementValueType() == ElementValue.PRIMITIVE_INT);
        assertTrue("Value of element should be 4 but it is "
                + ev.stringifyValue(), ev.stringifyValue().equals("4"));
        assertTrue(createTestdataFile("HelloWorld.class").delete());
    }

    /**
     * Just check that we can dump a class that has a method annotation on it
     * and it is still there when we read it back in
     */
    public void testGenerateMethodLevelAnnotations1()
            throws ClassNotFoundException
    {
        // Create HelloWorld
        final ClassGen cg = createClassGen("HelloWorld");
        final ConstantPoolGen cp = cg.getConstantPool();
        final InstructionList il = new InstructionList();
        buildClassContentsWithAnnotatedMethods(cg, cp, il);
        // Check annotation is OK
        int i = cg.getMethods()[0].getAnnotationEntries().length;
        assertTrue(
                "Prior to dumping, main method should have 1 annotation but has "
                        + i, i == 1);
        dumpClass(cg, "temp1" + File.separator + "HelloWorld.class");
        final JavaClass jc2 = getClassFrom("temp1", "HelloWorld");
        // Check annotation is OK
        i = jc2.getMethods()[0].getAnnotationEntries().length;
        assertTrue("JavaClass should say 1 annotation on main method but says "
                + i, i == 1);
        final ClassGen cg2 = new ClassGen(jc2);
        // Check it now it is a ClassGen
        final Method[] m = cg2.getMethods();
        i = m[0].getAnnotationEntries().length;
        assertTrue("The main 'Method' should have one annotation but has " + i,
                i == 1);
        final MethodGen mg = new MethodGen(m[0], cg2.getClassName(), cg2
                .getConstantPool());
        // Check it finally when the Method is changed to a MethodGen
        i = mg.getAnnotationEntries().length;
        assertTrue("The main 'MethodGen' should have one annotation but has "
                + i, i == 1);

        assertTrue(wipe("temp1", "HelloWorld.class"));
    }

    /**
     * Going further than the last test - when we reload the method back in,
     * let's change it (adding a new annotation) and then store that, read it
     * back in and verify both annotations are there !
     */
    public void testGenerateMethodLevelAnnotations2()
            throws ClassNotFoundException
    {
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
        assertTrue("The 'Method' should have one annotations but has "
                + mainMethod1.getAnnotationEntries().length, mainMethod1
                .getAnnotationEntries().length == 1);
        final MethodGen mainMethod2 = new MethodGen(mainMethod1, cg2.getClassName(),
                cg2.getConstantPool());
        assertTrue("The 'MethodGen' should have one annotations but has "
                + mainMethod2.getAnnotationEntries().length, mainMethod2
                .getAnnotationEntries().length == 1);
        mainMethod2.addAnnotationEntry(createFruitAnnotation(cg2
                .getConstantPool(), "Pear"));
        cg2.removeMethod(mainMethod1);
        cg2.addMethod(mainMethod2.getMethod());
        dumpClass(cg2, "temp3", "HelloWorld.class");
        final JavaClass jc3 = getClassFrom("temp3", "HelloWorld");
        final ClassGen cg3 = new ClassGen(jc3);
        final Method mainMethod3 = cg3.getMethods()[1];
        final int i = mainMethod3.getAnnotationEntries().length;
        assertTrue("The 'Method' should now have two annotations but has " + i,
                i == 2);
        assertTrue(wipe("temp2", "HelloWorld.class"));
        assertTrue(wipe("temp3", "HelloWorld.class"));
    }

    // J5TODO: Need to add deleteFile calls to many of these tests
    /**
     * Transform simple class from an immutable to a mutable object.
     */
    public void testTransformClassToClassGen_SimpleTypes()
            throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertTrue("Expected one annotation but found " + annotations.length,
                annotations.length == 1);
    }

    /**
     * Transform simple class from an immutable to a mutable object. The class
     * is annotated with an annotation that uses an enum.
     */
    public void testTransformClassToClassGen_EnumType()
            throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.AnnotatedWithEnumClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertTrue("Expected one annotation but found " + annotations.length,
                annotations.length == 1);
    }

    /**
     * Transform simple class from an immutable to a mutable object. The class
     * is annotated with an annotation that uses an array of SimpleAnnotations.
     */
    public void testTransformClassToClassGen_ArrayAndAnnotationTypes()
            throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.AnnotatedWithCombinedAnnotation");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertTrue("Expected one annotation but found " + annotations.length,
                annotations.length == 1);
        final AnnotationEntryGen a = annotations[0];
        assertTrue("That annotation should only have one value but has "
                + a.getValues().size(), a.getValues().size() == 1);
        final ElementValuePairGen nvp = a.getValues().get(0);
        final ElementValueGen value = nvp.getValue();
        assertTrue("Value should be ArrayElementValueGen but is " + value,
                value instanceof ArrayElementValueGen);
        final ArrayElementValueGen arrayValue = (ArrayElementValueGen) value;
        assertTrue("Array value should be size one but is "
                + arrayValue.getElementValuesSize(), arrayValue
                .getElementValuesSize() == 1);
        final ElementValueGen innerValue = arrayValue.getElementValues().get(0);
        assertTrue(
                "Value in the array should be AnnotationElementValueGen but is "
                        + innerValue,
                innerValue instanceof AnnotationElementValueGen);
        final AnnotationElementValueGen innerAnnotationValue = (AnnotationElementValueGen) innerValue;
        assertTrue("Should be called L"+PACKAGE_BASE_SIG+"/data/SimpleAnnotation; but is called: "
                + innerAnnotationValue.getAnnotation().getTypeName(),
                innerAnnotationValue.getAnnotation().getTypeSignature().equals(
                        "L"+PACKAGE_BASE_SIG+"/data/SimpleAnnotation;"));

        // check the three methods
        final Method[] methods = cgen.getMethods();
        assertEquals(3, methods.length);
        for (final Method method : methods)
        {
            final String methodName= method.getName();
            if(methodName.equals("<init>"))
            {
                assertMethodAnnotations(method, 0, 1);
                assertParameterAnnotations(method, 0, 1);
            }
            else if(methodName.equals("methodWithArrayOfZeroAnnotations"))
            {
                assertMethodAnnotations(method, 1, 0);
            }
            else if(methodName.equals("methodWithArrayOfTwoAnnotations"))
            {
                assertMethodAnnotations(method, 1, 2);
            }
            else
            {
                fail("unexpected method "+method.getName());
            }
        }
    }

    private void assertMethodAnnotations(final Method method, final int expectedNumberAnnotations, final int nExpectedArrayValues)
    {
        final String methodName= method.getName();
        final AnnotationEntry[] annos= method.getAnnotationEntries();
        assertEquals("For "+methodName, expectedNumberAnnotations, annos.length);
        if(expectedNumberAnnotations!=0)
        {
            assertArrayElementValue(nExpectedArrayValues, annos[0]);
        }
    }

    private void assertArrayElementValue(final int nExpectedArrayValues, final AnnotationEntry anno)
    {
        final ElementValuePair elementValuePair = anno.getElementValuePairs()[0];
        assertEquals("value", elementValuePair.getNameString());
        final ArrayElementValue ev = (ArrayElementValue) elementValuePair.getValue();
        final ElementValue[] eva = ev.getElementValuesArray();
        assertEquals(nExpectedArrayValues, eva.length);
    }

    private void assertParameterAnnotations(final Method method, final int... expectedNumberOfParmeterAnnotations)
    {
        final String methodName= "For "+method.getName();
        final ParameterAnnotationEntry[] parameterAnnotations= method.getParameterAnnotationEntries();
        assertEquals(methodName, expectedNumberOfParmeterAnnotations.length, parameterAnnotations.length);

        int i= 0;
        for (final ParameterAnnotationEntry parameterAnnotation : parameterAnnotations)
        {
            final AnnotationEntry[] annos= parameterAnnotation.getAnnotationEntries();
            final int expectedLength = expectedNumberOfParmeterAnnotations[i++];
            assertEquals(methodName+" parameter "+i, expectedLength, annos.length);
            if(expectedLength!=0)
            {
                assertSimpleElementValue(annos[0]);
            }
        }
    }

    private void assertSimpleElementValue(final AnnotationEntry anno)
    {
        final ElementValuePair elementValuePair = anno.getElementValuePairs()[0];
        assertEquals("id", elementValuePair.getNameString());
        final SimpleElementValue ev = (SimpleElementValue)elementValuePair.getValue();
        assertEquals(42, ev.getValueInt());
    }

    /**
     * Transform complex class from an immutable to a mutable object.
     */
    public void testTransformComplexClassToClassGen()
            throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.ComplexAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        // Check annotations are correctly preserved
        final AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
        assertTrue("Expected one annotation but found " + annotations.length,
                annotations.length == 1);
        final List<?> l = annotations[0].getValues();
        boolean found = false;
        for (final Object name : l) {
            final ElementValuePairGen element = (ElementValuePairGen) name;
            if (element.getNameString().equals("dval"))
            {
                if (((SimpleElementValueGen) element.getValue())
                        .stringifyValue().equals("33.4")) {
                    found = true;
                }
            }
        }
        assertTrue("Did not find double annotation value with value 33.4",
                found);
    }

    /**
     * Load a class in and modify it with a new attribute - A SimpleAnnotation
     * annotation
     */
    public void testModifyingClasses1() throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        final ConstantPoolGen cp = cgen.getConstantPool();
        cgen.addAnnotationEntry(createFruitAnnotation(cp, "Pineapple"));
        assertTrue("Should now have two annotations but has "
                + cgen.getAnnotationEntries().length, cgen
                .getAnnotationEntries().length == 2);
        dumpClass(cgen, "SimpleAnnotatedClass.class");
        assertTrue(wipe("SimpleAnnotatedClass.class"));
    }

    /**
     * Load a class in and modify it with a new attribute - A ComplexAnnotation
     * annotation
     */
    public void testModifyingClasses2() throws ClassNotFoundException
    {
        final JavaClass jc = getTestClass(PACKAGE_BASE_NAME+".data.SimpleAnnotatedClass");
        final ClassGen cgen = new ClassGen(jc);
        final ConstantPoolGen cp = cgen.getConstantPool();
        cgen.addAnnotationEntry(createCombinedAnnotation(cp));
        assertTrue("Should now have two annotations but has "
                + cgen.getAnnotationEntries().length, cgen
                .getAnnotationEntries().length == 2);
        dumpClass(cgen, "SimpleAnnotatedClass.class");
        final JavaClass jc2 = getClassFrom(".", "SimpleAnnotatedClass");
        jc2.getAnnotationEntries();
        assertTrue(wipe("SimpleAnnotatedClass.class"));
        // System.err.println(jc2.toString());
    }

    private void dumpClass(final ClassGen cg, final String fname)
    {
        try
        {
            final File f = createTestdataFile(fname);
            cg.getJavaClass().dump(f);
        }
        catch (final java.io.IOException e)
        {
            System.err.println(e);
        }
    }

    private void dumpClass(final ClassGen cg, final String dir, final String fname)
    {
        dumpClass(cg, dir + File.separator + fname);
    }

    private void buildClassContentsWithAnnotatedMethods(final ClassGen cg,
            final ConstantPoolGen cp, final InstructionList il)
    {
        // Create method 'public static void main(String[]argv)'
        final MethodGen mg = createMethodGen("main", il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);
        mg.addAnnotationEntry(createSimpleVisibleAnnotation(mg
                .getConstantPool()));
        // We now define some often used types:
        final ObjectType i_stream = new ObjectType("java.io.InputStream");
        final ObjectType p_stream = new ObjectType("java.io.PrintStream");
        // Create variables in and name : We call the constructors, i.e.,
        // execute BufferedReader(InputStreamReader(System.in)) . The reference
        // to the BufferedReader object stays on top of the stack and is stored
        // in the newly allocated in variable.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
                Const.GETSTATIC));
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
                Type.VOID, new Type[] { i_stream }, Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>",
                Type.VOID, new Type[] { new ObjectType("java.io.Reader") },
                Const.INVOKESPECIAL));
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType(
                "java.io.BufferedReader"), null, null);
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
        final InstructionHandle try_start = il.append(new PUSH(cp, "Andy"));
        il.append(new ASTORE(name));
        // Upon normal execution we jump behind exception handler, the target
        // address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle try_end = il.append(g);
        // We add the exception handler which simply returns from the method.
        final LocalVariableGen var_ex = mg.addLocalVariable("ex", Type
                .getType("Ljava.io.IOException;"), null, null);
        final int var_ex_slot = var_ex.getIndex();
        final InstructionHandle handler = il.append(new ASTORE(var_ex_slot));
        var_ex.setStart(handler);
        var_ex.setEnd(il.append(InstructionConst.RETURN));
        mg.addExceptionHandler(try_start, try_end, handler, new ObjectType(
                "java.io.IOException"));
        // "Normal" code continues, now we can set the branch target of the GOTO
        // .
        final InstructionHandle ih = il.append(factory.createFieldAccess(
                "java.lang.System", "out", p_stream, Const.GETSTATIC));
        g.setTarget(ih);
        // Printing "Hello": String concatenation compiles to StringBuffer
        // operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il
                .append(factory.createInvoke("java.lang.StringBuffer",
                        "<init>", Type.VOID, new Type[] { Type.STRING },
                        Const.INVOKESPECIAL));
        il.append(new ALOAD(name));
        il.append(factory.createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[] { Type.STRING },
                Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il
                .append(factory.createInvoke("java.io.PrintStream", "println",
                        Type.VOID, new Type[] { Type.STRING },
                        Const.INVOKEVIRTUAL));
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

    private void buildClassContents(final ClassGen cg, final ConstantPoolGen cp,
            final InstructionList il)
    {
        // Create method 'public static void main(String[]argv)'
        final MethodGen mg = createMethodGen("main", il, cp);
        final InstructionFactory factory = new InstructionFactory(cg);
        // We now define some often used types:
        final ObjectType i_stream = new ObjectType("java.io.InputStream");
        final ObjectType p_stream = new ObjectType("java.io.PrintStream");
        // Create variables in and name : We call the constructors, i.e.,
        // execute BufferedReader(InputStreamReader(System.in)) . The reference
        // to the BufferedReader object stays on top of the stack and is stored
        // in the newly allocated in variable.
        il.append(factory.createNew("java.io.BufferedReader"));
        il.append(InstructionConst.DUP); // Use predefined constant
        il.append(factory.createNew("java.io.InputStreamReader"));
        il.append(InstructionConst.DUP);
        il.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
                Const.GETSTATIC));
        il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
                Type.VOID, new Type[] { i_stream }, Const.INVOKESPECIAL));
        il.append(factory.createInvoke("java.io.BufferedReader", "<init>",
                Type.VOID, new Type[] { new ObjectType("java.io.Reader") },
                Const.INVOKESPECIAL));
        LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType(
                "java.io.BufferedReader"), null, null);
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
        final InstructionHandle try_start = il.append(new PUSH(cp, "Andy"));
        il.append(new ASTORE(name));
        // Upon normal execution we jump behind exception handler, the target
        // address is not known yet.
        final GOTO g = new GOTO(null);
        final InstructionHandle try_end = il.append(g);
        // We add the exception handler which simply returns from the method.
        final LocalVariableGen var_ex = mg.addLocalVariable("ex", Type
                .getType("Ljava.io.IOException;"), null, null);
        final int var_ex_slot = var_ex.getIndex();
        final InstructionHandle handler = il.append(new ASTORE(var_ex_slot));
        var_ex.setStart(handler);
        var_ex.setEnd(il.append(InstructionConst.RETURN));
        mg.addExceptionHandler(try_start, try_end, handler, new ObjectType(
                "java.io.IOException"));
        // "Normal" code continues, now we can set the branch target of the GOTO
        // .
        final InstructionHandle ih = il.append(factory.createFieldAccess(
                "java.lang.System", "out", p_stream, Const.GETSTATIC));
        g.setTarget(ih);
        // Printing "Hello": String concatenation compiles to StringBuffer
        // operations.
        il.append(factory.createNew(Type.STRINGBUFFER));
        il.append(InstructionConst.DUP);
        il.append(new PUSH(cp, "Hello, "));
        il
                .append(factory.createInvoke("java.lang.StringBuffer",
                        "<init>", Type.VOID, new Type[] { Type.STRING },
                        Const.INVOKESPECIAL));
        il.append(new ALOAD(name));
        il.append(factory.createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[] { Type.STRING },
                Const.INVOKEVIRTUAL));
        il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        il
                .append(factory.createInvoke("java.io.PrintStream", "println",
                        Type.VOID, new Type[] { Type.STRING },
                        Const.INVOKEVIRTUAL));
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

    private JavaClass getClassFrom(final String where, final String clazzname)
            throws ClassNotFoundException
    {
        // System.out.println(where);
        final SyntheticRepository repos = createRepos(where);
        return repos.loadClass(clazzname);
    }

    // helper methods
    private ClassGen createClassGen(final String classname)
    {
        return new ClassGen(classname, "java.lang.Object", "<generated>",
                Const.ACC_PUBLIC | Const.ACC_SUPER, null);
    }

    private MethodGen createMethodGen(final String methodname, final InstructionList il,
            final ConstantPoolGen cp)
    {
        return new MethodGen(Const.ACC_STATIC | Const.ACC_PUBLIC, // access
                // flags
                Type.VOID, // return type
                new Type[] { new ArrayType(Type.STRING, 1) }, // argument
                // types
                new String[] { "argv" }, // arg names
                methodname, "HelloWorld", // method, class
                il, cp);
    }

    public AnnotationEntryGen createSimpleVisibleAnnotation(final ConstantPoolGen cp)
    {
        final SimpleElementValueGen evg = new SimpleElementValueGen(
                ElementValueGen.PRIMITIVE_INT, cp, 4);
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
        return a;
    }

    public AnnotationEntryGen createFruitAnnotation(final ConstantPoolGen cp,
            final String aFruit)
    {
        final SimpleElementValueGen evg = new SimpleElementValueGen(
                ElementValueGen.STRING, cp, aFruit);
        final ElementValuePairGen nvGen = new ElementValuePairGen("fruit", evg, cp);
        final ObjectType t = new ObjectType("SimpleStringAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        return new AnnotationEntryGen(t, elements, true, cp);
    }

    public AnnotationEntryGen createCombinedAnnotation(final ConstantPoolGen cp)
    {
        // Create an annotation instance
        final AnnotationEntryGen a = createSimpleVisibleAnnotation(cp);
        final ArrayElementValueGen array = new ArrayElementValueGen(cp);
        array.addElement(new AnnotationElementValueGen(a, cp));
        final ElementValuePairGen nvp = new ElementValuePairGen("value", array, cp);
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvp);
        return new AnnotationEntryGen(new ObjectType("CombinedAnnotation"),
                elements, true, cp);
    }

    public AnnotationEntryGen createSimpleInvisibleAnnotation(final ConstantPoolGen cp)
    {
        final SimpleElementValueGen evg = new SimpleElementValueGen(
                ElementValueGen.PRIMITIVE_INT, cp, 4);
        final ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
        final ObjectType t = new ObjectType("SimpleAnnotation");
        final List<ElementValuePairGen> elements = new ArrayList<>();
        elements.add(nvGen);
        final AnnotationEntryGen a = new AnnotationEntryGen(t, elements, false, cp);
        return a;
    }
}
