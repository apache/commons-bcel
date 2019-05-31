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
 * A LocalVariableInfoInconsistentException instance is thrown by
 * the LocalVariableInfo class when it detects that the information
 * it holds is inconsistent; this is normally due to inconsistent
 * LocalVariableTable entries in the Code attribute of a certain
 * Method object.
 *
 */
public class LocalVariableInfoInconsistentException extends ClassConstraintException{
    private static final long serialVersionUID = -2833180480144304190L;

    /**
     * Constructs a new LocalVariableInfoInconsistentException with null as its error message string.
     */
    public LocalVariableInfoInconsistentException() {
        super();
    }

    /**
     * Constructs a new LocalVariableInfoInconsistentException with the specified error message.
     */
    public LocalVariableInfoInconsistentException(final String message) {
        super (message);
    }
}
