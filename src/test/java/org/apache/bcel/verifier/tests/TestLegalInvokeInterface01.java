package org.apache.bcel.verifier.tests;

public class TestLegalInvokeInterface01{

    public static void test1(Interface01 t){
        t.run();
    }
}

interface Interface01 extends Runnable {
    
}