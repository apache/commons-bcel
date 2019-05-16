/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.bcel.verifier;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

/**
 * This class has a main method implementing a demonstration program
 * of how to use the VerifierFactoryObserver. It transitively verifies
 * all class files encountered; this may take up a lot of time and,
 * more notably, memory.
 *
 */
public class TransitiveHull implements VerifierFactoryObserver {

    /** Used for indentation. */
    private int indent = 0;


    /** Not publicly instantiable. */
    private TransitiveHull() {
    }


    /* Implementing VerifierFactoryObserver. */
    @Override
    public void update( final String classname ) {
        System.gc(); // avoid swapping if possible.
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
        System.out.println(classname);
        indent += 1;
        final Verifier v = VerifierFactory.getVerifier(classname);
        VerificationResult vr;
        vr = v.doPass1();
        if (vr != VerificationResult.VR_OK) {
            System.out.println("Pass 1:\n" + vr);
        }
        vr = v.doPass2();
        if (vr != VerificationResult.VR_OK) {
            System.out.println("Pass 2:\n" + vr);
        }
        if (vr == VerificationResult.VR_OK) {
            try {
                final JavaClass jc = Repository.lookupClass(v.getClassName());
                for (int i = 0; i < jc.getMethods().length; i++) {
                    vr = v.doPass3a(i);
                    if (vr != VerificationResult.VR_OK) {
                        System.out.println(v.getClassName() + ", Pass 3a, method " + i + " ['"
                                + jc.getMethods()[i] + "']:\n" + vr);
                    }
                    vr = v.doPass3b(i);
                    if (vr != VerificationResult.VR_OK) {
                        System.out.println(v.getClassName() + ", Pass 3b, method " + i + " ['"
                                + jc.getMethods()[i] + "']:\n" + vr);
                    }
                }
            } catch (final ClassNotFoundException e) {
                System.err.println("Could not find class " + v.getClassName() + " in Repository");
            }
        }
        indent -= 1;
    }


    /**
     * This method implements a demonstration program
     * of how to use the VerifierFactoryObserver. It transitively verifies
     * all class files encountered; this may take up a lot of time and,
     * more notably, memory.
     */
    public static void main( final String[] args ) {
        if (args.length != 1) {
            System.out.println("Need exactly one argument: The root class to verify.");
            System.exit(1);
        }
        final int dotclasspos = args[0].lastIndexOf(".class");
        if (dotclasspos != -1) {
            args[0] = args[0].substring(0, dotclasspos);
        }
        args[0] = args[0].replace('/', '.');
        final TransitiveHull th = new TransitiveHull();
        VerifierFactory.attach(th);
        VerifierFactory.getVerifier(args[0]); // the observer is called back and does the actual trick.
        VerifierFactory.detach(th);
    }
}
