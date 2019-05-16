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
 * when a class file to verify does not pass the verification pass 2 as described
 * in the Java Virtual Machine specification, 2nd edition.
 *
 */
public class ClassConstraintException extends VerificationException{
    private static final long serialVersionUID = -4745598983569128296L;

    /**
     * Constructs a new ClassConstraintException with null as its error message string.
     */
    public ClassConstraintException() {
        super();
    }

    /**
     * Constructs a new ClassConstraintException with the specified error message.
     */
    public ClassConstraintException(final String message) {
        super (message);
    }

    /**
     * Constructs a new ClassConstraintException with the specified error message and cause
     * @since 6.0
     */
    public ClassConstraintException(final String message, final Throwable initCause) {
        super(message, initCause);
    }
}
