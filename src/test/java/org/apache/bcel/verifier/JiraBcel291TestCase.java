package org.apache.bcel.verifier;

import org.junit.Test;

public class JiraBcel291TestCase {

    @Test
    public void test() {
        Verifier.main(new String[] { "org/apache/bcel/verifier/tests/JiraBcel291TestFixture" });
    }
}
