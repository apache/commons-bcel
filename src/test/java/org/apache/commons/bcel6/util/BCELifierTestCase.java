package org.apache.commons.bcel6.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.bcel6.classfile.JavaClass;
import org.junit.Test;

public class BCELifierTestCase {

    @Test
    public void test() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        JavaClass java_class = BCELifier.getJavaClass("Java8Example");
        assertNotNull(java_class);
        BCELifier bcelifier = new BCELifier(java_class, os);
        bcelifier.start();
    }

}
