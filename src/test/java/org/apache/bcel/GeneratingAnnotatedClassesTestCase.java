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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.AnnotationElementValueGen;
import org.apache.bcel.generic.AnnotationEntryGen;
import org.apache.bcel.generic.ArrayElementValueGen;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ElementValueGen;
import org.apache.bcel.generic.ElementValuePairGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.SimpleElementValueGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.SyntheticRepository;

/**
 * The program that some of the tests generate looks like this:
 * 
 * <pre>
 * public class HelloWorld
 * {
 * 	public static void main(String[] argv)
 * 	{
 * 		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 * 		String name = null;
 * 
 * 		try
 * 		{
 * 			name = &quot;Andy&quot;;
 * 		}
 * 		catch (IOException e)
 * 		{
 * 			return;
 * 		}
 * 		System.out.println(&quot;Hello, &quot; + name);
 * 	}
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
		ClassGen cg = createClassGen("HelloWorld");
		cg.setMajor(49);
		cg.setMinor(0);
		ConstantPoolGen cp = cg.getConstantPool();
		InstructionList il = new InstructionList();
		cg.addAnnotationEntry(createSimpleVisibleAnnotation(cp));
		cg.addAnnotationEntry(createSimpleInvisibleAnnotation(cp));
		buildClassContents(cg, cp, il);
		//System.out.println(cg.getJavaClass().toString());
		dumpClass(cg, "HelloWorld.class");
		JavaClass jc = getClassFrom(".", "HelloWorld");
		AnnotationEntry[] as = jc.getAnnotationEntries();
		assertTrue("Should be two AnnotationEntries but found " + as.length,
				as.length == 2);
		AnnotationEntry one = as[0];
		AnnotationEntry two = as[1];
		// TODO L??;
		assertTrue(
				"Name of annotation 1 should be LSimpleAnnotation; but it is "
						+ as[0].getAnnotationType(), as[0].getAnnotationType()
						.equals("LSimpleAnnotation;"));
		assertTrue(
				"Name of annotation 2 should be LSimpleAnnotation; but it is "
						+ as[1].getAnnotationType(), as[1].getAnnotationType()
						.equals("LSimpleAnnotation;"));
		ElementValuePair[] vals = as[0].getElementValuePairs();
		ElementValuePair nvp = vals[0];
		assertTrue(
				"Name of element in SimpleAnnotation should be 'id' but it is "
						+ nvp.getNameString(), nvp.getNameString().equals("id"));
		ElementValue ev = nvp.getValue();
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
		ClassGen cg = createClassGen("HelloWorld");
		ConstantPoolGen cp = cg.getConstantPool();
		InstructionList il = new InstructionList();
		buildClassContentsWithAnnotatedMethods(cg, cp, il);
		// Check annotation is OK
		int i = cg.getMethods()[0].getAnnotationEntries().length;
		assertTrue(
				"Prior to dumping, main method should have 1 annotation but has "
						+ i, i == 1);
		dumpClass(cg, "temp1" + File.separator + "HelloWorld.class");
		JavaClass jc2 = getClassFrom("temp1", "HelloWorld");
		// Check annotation is OK
		i = jc2.getMethods()[0].getAnnotationEntries().length;
		assertTrue("JavaClass should say 1 annotation on main method but says "
				+ i, i == 1);
		ClassGen cg2 = new ClassGen(jc2);
		// Check it now it is a ClassGen
		Method[] m = cg2.getMethods();
		i = m[0].getAnnotationEntries().length;
		assertTrue("The main 'Method' should have one annotation but has " + i,
				i == 1);
		MethodGen mg = new MethodGen(m[0], cg2.getClassName(), cg2
				.getConstantPool());
		// Check it finally when the Method is changed to a MethodGen
		i = mg.getAnnotationEntries().length;
		assertTrue("The main 'MethodGen' should have one annotation but has "
				+ i, i == 1);
		assertTrue(wipe("temp1" + File.separator + "HelloWorld.class"));
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
		ClassGen cg = createClassGen("HelloWorld");
		ConstantPoolGen cp = cg.getConstantPool();
		InstructionList il = new InstructionList();
		buildClassContentsWithAnnotatedMethods(cg, cp, il);
		dumpClass(cg, "temp2", "HelloWorld.class");
		JavaClass jc2 = getClassFrom("temp2", "HelloWorld");
		ClassGen cg2 = new ClassGen(jc2);
		// Main method after reading the class back in
		Method mainMethod1 = jc2.getMethods()[0];
		assertTrue("The 'Method' should have one annotations but has "
				+ mainMethod1.getAnnotationEntries().length, mainMethod1
				.getAnnotationEntries().length == 1);
		MethodGen mainMethod2 = new MethodGen(mainMethod1, cg2.getClassName(),
				cg2.getConstantPool());
		assertTrue("The 'MethodGen' should have one annotations but has "
				+ mainMethod2.getAnnotationEntries().length, mainMethod2
				.getAnnotationEntries().length == 1);
		mainMethod2.addAnnotationEntry(createFruitAnnotation(cg2
				.getConstantPool(), "Pear"));
		cg2.removeMethod(mainMethod1);
		cg2.addMethod(mainMethod2.getMethod());
		dumpClass(cg2, "temp3", "HelloWorld.class");
		JavaClass jc3 = getClassFrom("temp3", "HelloWorld");
		ClassGen cg3 = new ClassGen(jc3);
		Method mainMethod3 = cg3.getMethods()[1];
		int i = mainMethod3.getAnnotationEntries().length;
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
		JavaClass jc = getTestClass("org.apache.bcel.data.SimpleAnnotatedClass");
		ClassGen cgen = new ClassGen(jc);
		// Check annotations are correctly preserved
		AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
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
		JavaClass jc = getTestClass("org.apache.bcel.data.AnnotatedWithEnumClass");
		ClassGen cgen = new ClassGen(jc);
		// Check annotations are correctly preserved
		AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
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
		JavaClass jc = getTestClass("org.apache.bcel.data.AnnotatedWithCombinedAnnotation");
		ClassGen cgen = new ClassGen(jc);
		// Check annotations are correctly preserved
		AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
		assertTrue("Expected one annotation but found " + annotations.length,
				annotations.length == 1);
		AnnotationEntryGen a = annotations[0];
		assertTrue("That annotation should only have one value but has "
				+ a.getValues().size(), a.getValues().size() == 1);
		ElementValuePairGen nvp = a.getValues().get(0);
		ElementValueGen value = nvp.getValue();
		assertTrue("Value should be ArrayElementValueGen but is " + value,
				value instanceof ArrayElementValueGen);
		ArrayElementValueGen arrayValue = (ArrayElementValueGen) value;
		assertTrue("Array value should be size one but is "
				+ arrayValue.getElementValuesSize(), arrayValue
				.getElementValuesSize() == 1);
		ElementValueGen innerValue = arrayValue.getElementValues().get(0);
		assertTrue(
				"Value in the array should be AnnotationElementValueGen but is "
						+ innerValue,
				innerValue instanceof AnnotationElementValueGen);
		AnnotationElementValueGen innerAnnotationValue = (AnnotationElementValueGen) innerValue;
		assertTrue("Should be called Lorg/apache/bcel/data/SimpleAnnotation; but is called: "
				+ innerAnnotationValue.getAnnotation().getTypeName(),
				innerAnnotationValue.getAnnotation().getTypeSignature().equals(
						"Lorg/apache/bcel/data/SimpleAnnotation;"));
	}

	/**
	 * Transform complex class from an immutable to a mutable object.
	 */
	public void testTransformComplexClassToClassGen()
			throws ClassNotFoundException
	{
		JavaClass jc = getTestClass("org.apache.bcel.data.ComplexAnnotatedClass");
		ClassGen cgen = new ClassGen(jc);
		// Check annotations are correctly preserved
		AnnotationEntryGen[] annotations = cgen.getAnnotationEntries();
		assertTrue("Expected one annotation but found " + annotations.length,
				annotations.length == 1);
		List<?> l = annotations[0].getValues();
		boolean found = false;
		for (Iterator<?> iter = l.iterator(); iter.hasNext();)
		{
			ElementValuePairGen element = (ElementValuePairGen) iter.next();
			if (element.getNameString().equals("dval"))
			{
				if (((SimpleElementValueGen) element.getValue())
						.stringifyValue().equals("33.4"))
					found = true;
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
		JavaClass jc = getTestClass("org.apache.bcel.data.SimpleAnnotatedClass");
		ClassGen cgen = new ClassGen(jc);
		ConstantPoolGen cp = cgen.getConstantPool();
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
		JavaClass jc = getTestClass("org.apache.bcel.data.SimpleAnnotatedClass");
		ClassGen cgen = new ClassGen(jc);
		ConstantPoolGen cp = cgen.getConstantPool();
		cgen.addAnnotationEntry(createCombinedAnnotation(cp));
		assertTrue("Should now have two annotations but has "
				+ cgen.getAnnotationEntries().length, cgen
				.getAnnotationEntries().length == 2);
		dumpClass(cgen, "SimpleAnnotatedClass.class");
		JavaClass jc2 = getClassFrom(".", "SimpleAnnotatedClass");
		jc2.getAnnotationEntries();
		assertTrue(wipe("SimpleAnnotatedClass.class"));
		// System.err.println(jc2.toString());
	}

	private void dumpClass(ClassGen cg, String fname)
	{
		try
		{
			File f = createTestdataFile(fname);
			cg.getJavaClass().dump(f);
		}
		catch (java.io.IOException e)
		{
			System.err.println(e);
		}
	}

	private void dumpClass(ClassGen cg, String dir, String fname)
	{
		dumpClass(cg, dir + File.separator + fname);
	}

	private void buildClassContentsWithAnnotatedMethods(ClassGen cg,
			ConstantPoolGen cp, InstructionList il)
	{
		// Create method 'public static void main(String[]argv)'
		MethodGen mg = createMethodGen("main", il, cp);
		InstructionFactory factory = new InstructionFactory(cg);
		mg.addAnnotationEntry(createSimpleVisibleAnnotation(mg
				.getConstantPool()));
		// We now define some often used types:
		ObjectType i_stream = new ObjectType("java.io.InputStream");
		ObjectType p_stream = new ObjectType("java.io.PrintStream");
		// Create variables in and name : We call the constructors, i.e.,
		// execute BufferedReader(InputStreamReader(System.in)) . The reference
		// to the BufferedReader object stays on top of the stack and is stored
		// in the newly allocated in variable.
		il.append(factory.createNew("java.io.BufferedReader"));
		il.append(InstructionConstants.DUP); // Use predefined constant
		il.append(factory.createNew("java.io.InputStreamReader"));
		il.append(InstructionConstants.DUP);
		il.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
				Constants.GETSTATIC));
		il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
				Type.VOID, new Type[] { i_stream }, Constants.INVOKESPECIAL));
		il.append(factory.createInvoke("java.io.BufferedReader", "<init>",
				Type.VOID, new Type[] { new ObjectType("java.io.Reader") },
				Constants.INVOKESPECIAL));
		LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType(
				"java.io.BufferedReader"), null, null);
		int in = lg.getIndex();
		lg.setStart(il.append(new ASTORE(in))); // "in" valid from here
		// Create local variable name and initialize it to null
		lg = mg.addLocalVariable("name", Type.STRING, null, null);
		int name = lg.getIndex();
		il.append(InstructionConstants.ACONST_NULL);
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
		InstructionHandle try_start = il.append(new PUSH(cp, "Andy"));
		il.append(new ASTORE(name));
		// Upon normal execution we jump behind exception handler, the target
		// address is not known yet.
		GOTO g = new GOTO(null);
		InstructionHandle try_end = il.append(g);
		// We add the exception handler which simply returns from the method.
		LocalVariableGen var_ex = mg.addLocalVariable("ex", Type
				.getType("Ljava.io.IOException;"), null, null);
		int var_ex_slot = var_ex.getIndex();
		InstructionHandle handler = il.append(new ASTORE(var_ex_slot));
		var_ex.setStart(handler);
		var_ex.setEnd(il.append(InstructionConstants.RETURN));
		mg.addExceptionHandler(try_start, try_end, handler, new ObjectType(
				"java.io.IOException"));
		// "Normal" code continues, now we can set the branch target of the GOTO
		// .
		InstructionHandle ih = il.append(factory.createFieldAccess(
				"java.lang.System", "out", p_stream, Constants.GETSTATIC));
		g.setTarget(ih);
		// Printing "Hello": String concatenation compiles to StringBuffer
		// operations.
		il.append(factory.createNew(Type.STRINGBUFFER));
		il.append(InstructionConstants.DUP);
		il.append(new PUSH(cp, "Hello, "));
		il
				.append(factory.createInvoke("java.lang.StringBuffer",
						"<init>", Type.VOID, new Type[] { Type.STRING },
						Constants.INVOKESPECIAL));
		il.append(new ALOAD(name));
		il.append(factory.createInvoke("java.lang.StringBuffer", "append",
				Type.STRINGBUFFER, new Type[] { Type.STRING },
				Constants.INVOKEVIRTUAL));
		il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
				Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		il
				.append(factory.createInvoke("java.io.PrintStream", "println",
						Type.VOID, new Type[] { Type.STRING },
						Constants.INVOKEVIRTUAL));
		il.append(InstructionConstants.RETURN);
		// Finalization: Finally, we have to set the stack size, which normally
		// would have to be computed on the fly and add a default constructor
		// method to the class, which is empty in this case.
		mg.setMaxStack();
		mg.setMaxLocals();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		cg.addEmptyConstructor(Constants.ACC_PUBLIC);
	}

	private void buildClassContents(ClassGen cg, ConstantPoolGen cp,
			InstructionList il)
	{
		// Create method 'public static void main(String[]argv)'
		MethodGen mg = createMethodGen("main", il, cp);
		InstructionFactory factory = new InstructionFactory(cg);
		// We now define some often used types:
		ObjectType i_stream = new ObjectType("java.io.InputStream");
		ObjectType p_stream = new ObjectType("java.io.PrintStream");
		// Create variables in and name : We call the constructors, i.e.,
		// execute BufferedReader(InputStreamReader(System.in)) . The reference
		// to the BufferedReader object stays on top of the stack and is stored
		// in the newly allocated in variable.
		il.append(factory.createNew("java.io.BufferedReader"));
		il.append(InstructionConstants.DUP); // Use predefined constant
		il.append(factory.createNew("java.io.InputStreamReader"));
		il.append(InstructionConstants.DUP);
		il.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
				Constants.GETSTATIC));
		il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
				Type.VOID, new Type[] { i_stream }, Constants.INVOKESPECIAL));
		il.append(factory.createInvoke("java.io.BufferedReader", "<init>",
				Type.VOID, new Type[] { new ObjectType("java.io.Reader") },
				Constants.INVOKESPECIAL));
		LocalVariableGen lg = mg.addLocalVariable("in", new ObjectType(
				"java.io.BufferedReader"), null, null);
		int in = lg.getIndex();
		lg.setStart(il.append(new ASTORE(in))); // "in" valid from here
		// Create local variable name and initialize it to null
		lg = mg.addLocalVariable("name", Type.STRING, null, null);
		int name = lg.getIndex();
		il.append(InstructionConstants.ACONST_NULL);
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
		InstructionHandle try_start = il.append(new PUSH(cp, "Andy"));
		il.append(new ASTORE(name));
		// Upon normal execution we jump behind exception handler, the target
		// address is not known yet.
		GOTO g = new GOTO(null);
		InstructionHandle try_end = il.append(g);
		// We add the exception handler which simply returns from the method.
		LocalVariableGen var_ex = mg.addLocalVariable("ex", Type
				.getType("Ljava.io.IOException;"), null, null);
		int var_ex_slot = var_ex.getIndex();
		InstructionHandle handler = il.append(new ASTORE(var_ex_slot));
		var_ex.setStart(handler);
		var_ex.setEnd(il.append(InstructionConstants.RETURN));
		mg.addExceptionHandler(try_start, try_end, handler, new ObjectType(
				"java.io.IOException"));
		// "Normal" code continues, now we can set the branch target of the GOTO
		// .
		InstructionHandle ih = il.append(factory.createFieldAccess(
				"java.lang.System", "out", p_stream, Constants.GETSTATIC));
		g.setTarget(ih);
		// Printing "Hello": String concatenation compiles to StringBuffer
		// operations.
		il.append(factory.createNew(Type.STRINGBUFFER));
		il.append(InstructionConstants.DUP);
		il.append(new PUSH(cp, "Hello, "));
		il
				.append(factory.createInvoke("java.lang.StringBuffer",
						"<init>", Type.VOID, new Type[] { Type.STRING },
						Constants.INVOKESPECIAL));
		il.append(new ALOAD(name));
		il.append(factory.createInvoke("java.lang.StringBuffer", "append",
				Type.STRINGBUFFER, new Type[] { Type.STRING },
				Constants.INVOKEVIRTUAL));
		il.append(factory.createInvoke("java.lang.StringBuffer", "toString",
				Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		il
				.append(factory.createInvoke("java.io.PrintStream", "println",
						Type.VOID, new Type[] { Type.STRING },
						Constants.INVOKEVIRTUAL));
		il.append(InstructionConstants.RETURN);
		// Finalization: Finally, we have to set the stack size, which normally
		// would have to be computed on the fly and add a default constructor
		// method to the class, which is empty in this case.
		mg.setMaxStack();
		mg.setMaxLocals();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		cg.addEmptyConstructor(Constants.ACC_PUBLIC);
	}

	private JavaClass getClassFrom(String where, String clazzname)
			throws ClassNotFoundException
	{
		// System.out.println(where);
		SyntheticRepository repos = createRepos(where);
		return repos.loadClass(clazzname);
	}

	// helper methods
	private ClassGen createClassGen(String classname)
	{
		return new ClassGen(classname, "java.lang.Object", "<generated>",
				Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
	}

	private MethodGen createMethodGen(String methodname, InstructionList il,
			ConstantPoolGen cp)
	{
		return new MethodGen(Constants.ACC_STATIC | Constants.ACC_PUBLIC, // access
				// flags
				Type.VOID, // return type
				new Type[] { new ArrayType(Type.STRING, 1) }, // argument
				// types
				new String[] { "argv" }, // arg names
				methodname, "HelloWorld", // method, class
				il, cp);
	}

	public AnnotationEntryGen createSimpleVisibleAnnotation(ConstantPoolGen cp)
	{
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.PRIMITIVE_INT, cp, 4);
		ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
		ObjectType t = new ObjectType("SimpleAnnotation");
		List<ElementValuePairGen> elements = new ArrayList<ElementValuePairGen>();
		elements.add(nvGen);
		AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
		return a;
	}

	public AnnotationEntryGen createFruitAnnotation(ConstantPoolGen cp,
			String aFruit)
	{
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.STRING, cp, aFruit);
		ElementValuePairGen nvGen = new ElementValuePairGen("fruit", evg, cp);
		ObjectType t = new ObjectType("SimpleStringAnnotation");
		List<ElementValuePairGen> elements = new ArrayList<ElementValuePairGen>();
		elements.add(nvGen);
		return new AnnotationEntryGen(t, elements, true, cp);
	}

	public AnnotationEntryGen createCombinedAnnotation(ConstantPoolGen cp)
	{
		// Create an annotation instance
		AnnotationEntryGen a = createSimpleVisibleAnnotation(cp);
		ArrayElementValueGen array = new ArrayElementValueGen(cp);
		array.addElement(new AnnotationElementValueGen(a, cp));
		ElementValuePairGen nvp = new ElementValuePairGen("value", array, cp);
		List<ElementValuePairGen> elements = new ArrayList<ElementValuePairGen>();
		elements.add(nvp);
		return new AnnotationEntryGen(new ObjectType("CombinedAnnotation"),
				elements, true, cp);
	}

	public AnnotationEntryGen createSimpleInvisibleAnnotation(ConstantPoolGen cp)
	{
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.PRIMITIVE_INT, cp, 4);
		ElementValuePairGen nvGen = new ElementValuePairGen("id", evg, cp);
		ObjectType t = new ObjectType("SimpleAnnotation");
		List<ElementValuePairGen> elements = new ArrayList<ElementValuePairGen>();
		elements.add(nvGen);
		AnnotationEntryGen a = new AnnotationEntryGen(t, elements, false, cp);
		return a;
	}
}