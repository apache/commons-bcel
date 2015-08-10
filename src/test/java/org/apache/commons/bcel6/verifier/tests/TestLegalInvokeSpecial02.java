package org.apache.commons.bcel6.verifier.tests;

abstract public class TestLegalInvokeSpecial02 implements Runnable{

    public static void test1(TestLegalInvokeSpecial02 t, int i){
        if(i > 0){
            t.run();
        }
    }
    
}
