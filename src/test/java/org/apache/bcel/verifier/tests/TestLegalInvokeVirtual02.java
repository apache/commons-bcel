package org.apache.bcel.verifier.tests;

abstract public class TestLegalInvokeVirtual02 implements Runnable{

    public static void test1(TestLegalInvokeVirtual02 t, int i){
        if(i > 0){
            t.run();
        }
    }
    
}
