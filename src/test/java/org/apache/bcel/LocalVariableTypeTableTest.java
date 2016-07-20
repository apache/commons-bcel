package org.apache.bcel;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sam
 */
public class LocalVariableTypeTableTest extends AbstractTestCase
{
    public class TestClassLoader extends ClassLoader
    {
        public TestClassLoader(ClassLoader parent)
        {
            super(parent);
        }

        public Class<?> findClass(String name, byte[] bytes)
        {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    @Test
    public void testWithGenericArguement() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        String targetClass = PACKAGE_BASE_NAME + ".data.SimpleClassHasMethodIncludeGenericArgument";
        TestClassLoader loader = new TestClassLoader(getClass().getClassLoader());
        Class cls = loader.findClass(targetClass, getBytesFromClass(targetClass));
        java.lang.reflect.Method method = cls.getDeclaredMethod("a", String.class, List.class);

        method.invoke(null, "a", new LinkedList<String>());
    }

    private byte[] getBytesFromClass(String className) throws ClassNotFoundException, IOException
    {
        JavaClass clazz = getTestClass(className);
        ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            if (!method.isNative() && !method.isAbstract())
                methods[i] = injection(clazz, method, cp, findFirstStringLocalVariableOffset(method));
        }

        clazz.setConstantPool(cp.getFinalConstantPool());

        return clazz.getBytes();
    }

    public Method injection(JavaClass clazz, Method method, ConstantPoolGen cp, int firstStringOffset)
    {
        MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);

        InstructionList instructionList = methodGen.getInstructionList();
        instructionList.insert(instructionList.getStart(), makeWillBeAddedInstructionList(methodGen, firstStringOffset));

        methodGen.setMaxStack();
        methodGen.setMaxLocals();

        method = methodGen.getMethod();
        instructionList.dispose();

        return method;
    }

    public InstructionList makeWillBeAddedInstructionList(MethodGen methodGen, int firstStringOffset)
    {
        if (firstStringOffset == -1)
            return new InstructionList();

        LocalVariableGen localVariableGen = methodGen.getLocalVariables()[firstStringOffset];
        Instruction instruction;

        if (localVariableGen != null)
            instruction = new ALOAD(localVariableGen.getIndex());
        else
            instruction = new ACONST_NULL();

        return createPrintln(methodGen.getConstantPool(), instruction);
    }

    public InstructionList createPrintln(ConstantPoolGen cp, Instruction instruction)
    {
        final InstructionList il = new InstructionList();
        final int out = cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
        final int println = cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
        il.append(new GETSTATIC(out));
        il.append(instruction);
        il.append(new INVOKEVIRTUAL(println));
        return il;
    }

    public int findFirstStringLocalVariableOffset(Method method)
    {
        Type[] argumentTypes = method.getArgumentTypes();
        int offset = -1;

        for (int i = 0, count = argumentTypes.length; i < count; i++)
        {
            if (Type.STRING.getSignature().equals(argumentTypes[i].getSignature()))
            {
                if (method.isStatic())
                    offset = i;
                else
                    offset = i + 1;

                break;
            }
        }

        return offset;
    }
}
