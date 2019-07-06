package org.apache.bcel.verifier.tests;

public class JiraBcel291TestFixture {

    public static Object[] bug(Object[] arg) {
        return arg.clone();
    }
}
