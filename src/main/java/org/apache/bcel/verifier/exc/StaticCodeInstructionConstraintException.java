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
 * Instances of this class are thrown by BCEL's class file verifier "JustIce" when
 * a class file to verify does not pass the verification pass 3 because of a violation
 * of a static constraint as described in the Java Virtual Machine Specification,
 * Second edition, 4.8.1, pages 133-137. The static constraints checking part of pass 3
 * is called pass 3a in JustIce.
 * Static constraints on the instructions in the code array are checked early in
 * pass 3a and are described on page 134 in the Java Virtual Machine Specification,
 * Second Edition.
 *
 */
public class StaticCodeInstructionConstraintException extends StaticCodeConstraintException{
    private static final long serialVersionUID = 4987255974346614794L;

    public StaticCodeInstructionConstraintException(final String message) {
        super(message);
    }
}
