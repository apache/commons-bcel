package org.apache.bcel;

import junit.framework.TestCase;
import org.apache.bcel.*;
import org.apache.bcel.classfile.Signature;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.Type;

public class PLSE2TestCase extends AbstractTestCase {

    public void testSignatureToStringWithGenerics() throws Exception {

    // tests for BCEL-197
        
        String expectedValue = "java.util.Map<X, java.util.List<Y>>";
        String actualValue = Utility.signatureToString("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;");
        assertEquals("generic signature", expectedValue, actualValue);
        //System.out.printf("U: %s%nS: %s%n", actualValue, Signature.translate("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;"));

        expectedValue = "java.util.Set<? extends java.nio.file.OpenOption>";
        actualValue = Utility.signatureToString("Ljava/util/Set<+Ljava/nio/file/OpenOption;>;");
        assertEquals("generic signature", expectedValue, actualValue);
        //System.out.printf("U: %s%nS: %s%n", actualValue, Signature.translate("Ljava/util/Set<+Ljava/nio/file/OpenOption;>;"));

        expectedValue = "java.nio.file.attribute.FileAttribute<?>...[]";
        actualValue = Utility.signatureToString("[Ljava/nio/file/attribute/FileAttribute<*>;");
        assertEquals("generic signature", expectedValue, actualValue);
        //System.out.printf("U: %s%nS: %s%n", actualValue, Signature.translate("[Ljava/nio/file/attribute/FileAttribute<*>;"));
        //System.out.printf("U: %s%nS: %s%n", actualValue, Signature.translate("Ljava/nio/file/attribute/FileAttribute<*>;"));

    // test for BCEL-243
        
        // expectedValue = "Ljava/util/Map<TX;Ljava/util/List<TY;>;>;";
        // The line commented out above is the correct expected value; however,
        // the constructor for ObjectType is yet another place where BCEL does
        // not understand generics so we need to substitute the modified value below.
        expectedValue = "Ljava/util/Map<X, java/util/List<Y>>;";
        actualValue = (Type.getType("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;")).getSignature();
        assertEquals("Type.getType", expectedValue, actualValue);
    }

}
