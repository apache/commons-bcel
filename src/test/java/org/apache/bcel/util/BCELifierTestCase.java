package org.apache.bcel.util;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.bcel.classfile.JavaClass;
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

    /*
     * Dump a class using "javap" and compare with the same class recreated
     * using BCELifier, "javac", "java" and dumped with "javap"
     * TODO: detect if JDK present and skip test if not 
     */
    @Test
    public void testJavapCompare() throws Exception {
        testClassOnPath("target/test-classes/Java8Example.class");
    }

    private void testClassOnPath(final String javaClass) throws Exception {
        // Get javap of the input class
        final String initial = exec(null, "javap", "-p", "-c", javaClass);

        final File workDir = new File("target");
        File infile = new File(javaClass);
        JavaClass java_class = BCELifier.getJavaClass(infile.getName().replace(".class", ""));
        assertNotNull(java_class);
        File outfile = new File(workDir, infile.getName().replace(".class", "Creator.java"));
        FileOutputStream fos = new FileOutputStream(outfile);
        BCELifier bcelifier = new BCELifier(java_class, fos);
        bcelifier.start();
        fos.close();
        exec(workDir, "javac", "-cp", "classes", outfile.getName());
        exec(workDir, "java", "-cp", "." + File.pathSeparator + "classes", outfile.getName().replace(".java", ""));
        final String output = exec(workDir, "javap", "-p", "-c", infile.getName());
        assertEquals(canonHashRef(initial), canonHashRef(output));
    }

    // Canonicalise the javap output so it compares better
    private String canonHashRef(String input) {
        input = input.replaceAll("#\\d+", "#n"); // numbers may vary in length
        input = input.replaceAll(" +", " "); // collapse spaces
        input = input.replaceAll("//.+",""); // comments may vary
        return input;
    }

    private String exec(final File workDir, final String... args) throws Exception {
        // System.err.println(java.util.Arrays.toString(args));
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(workDir);
        Process proc = pb.start();
        BufferedInputStream is = new BufferedInputStream(proc.getInputStream());
        InputStream es = proc.getErrorStream();
        proc.waitFor();
        byte[] buff = new byte[2048];
        int len;
        while ((len = es.read(buff)) != -1) {
            System.err.print(new String(buff, 0, len));
        }

        StringBuilder sb = new StringBuilder();
        while ((len = is.read(buff)) != -1) {
            sb.append(new String(buff, 0, len));
        }
        is.close();
        return sb.toString();
    }

}
