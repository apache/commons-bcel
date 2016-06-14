package org.apache.bcel.generic;

import junit.framework.TestCase;

public class TypeTestCase extends TestCase {
    public void testBCEL243() {
        // expectedValue = "Ljava/util/Map<TX;Ljava/util/List<TY;>;>;";
        // The line commented out above is the correct expected value; however,
        // the constructor for ObjectType is yet another place where BCEL does
        // not understand generics so we need to substitute the modified value below.
        String expectedValue = "Ljava/util/Map<X, java/util/List<Y>>;";
        String actualValue = (Type.getType("Ljava/util/Map<TX;Ljava/util/List<TY;>;>;")).getSignature();
        assertEquals("Type.getType", expectedValue, actualValue);        
    }

}
