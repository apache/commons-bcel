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
package org.apache.bcel.verifier.exc;


/**
 * Instances of this class are thrown by BCEL's class file verifier "JustIce"
 * whenever
 * verification proves that some constraint of a class file (as stated in the
 * Java Virtual Machine Specification, Edition 2) is violated.
 * This is roughly equivalent to the VerifyError the JVM-internal verifiers
 * throw.
 *
 */
public abstract class VerifierConstraintViolatedException extends RuntimeException{
    // /** The name of the offending class that did not pass the verifier. */
    // String name_of_offending_class;

    private static final long serialVersionUID = 2946136970490179465L;
    /** The specified error message. */
    private String detailMessage;
    /**
     * Constructs a new VerifierConstraintViolatedException with null as its error message string.
     */
    VerifierConstraintViolatedException() {
        super();
    }
    /**
     * Constructs a new VerifierConstraintViolatedException with the specified error message.
     */
    VerifierConstraintViolatedException(final String message) {
        super(message); // Not that important
        detailMessage = message;
    }
    /**
     * Constructs a new VerifierConstraintViolationException with the specified error message and cause
     */
    VerifierConstraintViolatedException(final String message, final Throwable initCause) {
        super(message, initCause);
        detailMessage = message;
    }


    /** Extends the error message with a string before ("pre") and after ("post") the
        'old' error message. All of these three strings are allowed to be null, and null
        is always replaced by the empty string (""). In particular, after invoking this
        method, the error message of this object can no longer be null.
    */
    public void extendMessage(String pre, String post) {
        if (pre  == null) {
            pre="";
        }
        if (detailMessage == null) {
            detailMessage="";
        }
        if (post == null) {
            post="";
        }
        detailMessage = pre+detailMessage+post;
    }
    /**
     * Returns the error message string of this VerifierConstraintViolatedException object.
     * @return the error message string of this VerifierConstraintViolatedException.
     */
    @Override
    public String getMessage() {
        return detailMessage;
    }
}
