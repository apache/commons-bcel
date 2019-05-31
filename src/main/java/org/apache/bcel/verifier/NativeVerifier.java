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

/**
 * The NativeVerifier class implements a main(String[] args) method that's
 * roughly compatible to the one in the Verifier class, but that uses the
 * JVM's internal verifier for its class file verification.
 * This can be used for comparison runs between the JVM-internal verifier
 * and JustIce.
 *
 */
public abstract class NativeVerifier {

    /**
     * This class must not be instantiated.
     */
    private NativeVerifier() {
    }


    /**
     * Works only on the first argument.
     */
    public static void main( final String[] args ) {
        if (args.length != 1) {
            System.out.println("Verifier front-end: need exactly one argument.");
            System.exit(1);
        }
        final int dotclasspos = args[0].lastIndexOf(".class");
        if (dotclasspos != -1) {
            args[0] = args[0].substring(0, dotclasspos);
        }
        args[0] = args[0].replace('/', '.');
        //System.out.println(args[0]);
        try {
            Class.forName(args[0]);
        } catch (final ExceptionInInitializerError eiie) { //subclass of LinkageError!
            System.out.println("NativeVerifier: ExceptionInInitializerError encountered on '"
                    + args[0] + "'.");
            System.out.println(eiie);
            System.exit(1);
        } catch (final LinkageError le) {
            System.out.println("NativeVerifier: LinkageError encountered on '" + args[0] + "'.");
            System.out.println(le);
            System.exit(1);
        } catch (final ClassNotFoundException cnfe) {
            System.out.println("NativeVerifier: FILE NOT FOUND: '" + args[0] + "'.");
            System.exit(1);
        } catch (final Throwable t) { // OK to catch Throwable here as we call exit.
            System.out.println("NativeVerifier: Unspecified verification error on '" + args[0] + "'.");
            System.exit(1);
        }
        System.out.println("NativeVerifier: Class file '" + args[0] + "' seems to be okay.");
        System.exit(0);
    }
}
