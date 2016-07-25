package org.apache.bcel.classfile;

import org.apache.bcel.AbstractTestCase;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.junit.Test;

/**
 * @author sam
 */
public class ConstantPoolTestCase extends AbstractTestCase
{
    @Test
    public void testConstantToString() throws ClassNotFoundException
    {
        JavaClass clazz = getTestClass(PACKAGE_BASE_NAME + ".data.SimpleClassWithDefaultConstructor");
        ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());

        Method[] methods = clazz.getMethods();

        for (Method method : methods)
        {
            if (method.getName().equals("<init>"))
            {
                for (InstructionHandle instructionHandle : getInstructionHandles(clazz, cp, method))
                {
                    System.out.println(instructionHandle.getInstruction().toString(cp.getConstantPool()));
                }
            }
        }
    }

    private InstructionHandle[] getInstructionHandles(JavaClass clazz, ConstantPoolGen cp, Method method)
    {
        MethodGen methodGen = new MethodGen(method, clazz.getClassName(), cp);
        InstructionList instructionList = methodGen.getInstructionList();
        return instructionList.getInstructionHandles();
    }
}
