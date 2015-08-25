package org.apache.commons.bcel6.util;

import static org.junit.Assert.*;

import org.apache.commons.bcel6.classfile.JavaClass;
import org.junit.Test;
import static org.junit.Assume.assumeTrue;

public class BCELifierTestCase {

    // A bit of a hack - we use the same property as for the perf test for now
    private static final boolean REPORT = Boolean.parseBoolean(System.getProperty("PerformanceTest.report", "true"));;

    @Test
    public void test() throws Exception {
        assumeTrue(REPORT); // set to false by pom so this will only run on demand
        JavaClass java_class = BCELifier.getJavaClass("Java8Example");
        assertNotNull(java_class);
        BCELifier bcelifier = new BCELifier(java_class, System.out);
        bcelifier.start();
    }

}
