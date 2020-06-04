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
package org.apache.bcel.verifier.structurals;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

/**
 * This class allows easy access to ExceptionHandler objects.
 *
 */
public class ExceptionHandlers{
    /**
     * The ExceptionHandler instances.
     * Key: InstructionHandle objects, Values: HashSet<ExceptionHandler> instances.
     */
    private final Map<InstructionHandle, Set<ExceptionHandler>> exceptionHandlers;

    /**
     * Constructor. Creates a new ExceptionHandlers instance.
     */
    public ExceptionHandlers(final MethodGen mg) {
        exceptionHandlers = new HashMap<>();
        final CodeExceptionGen[] cegs = mg.getExceptionHandlers();
        for (final CodeExceptionGen ceg : cegs) {
            final ExceptionHandler eh = new ExceptionHandler(ceg.getCatchType(), ceg.getHandlerPC());
            for (InstructionHandle ih=ceg.getStartPC(); ih != ceg.getEndPC().getNext(); ih=ih.getNext()) {
                Set<ExceptionHandler> hs;
                hs = exceptionHandlers.get(ih);
                if (hs == null) {
                    hs = new HashSet<>();
                    exceptionHandlers.put(ih, hs);
                }
                hs.add(eh);
            }
        }
    }

    /**
     * Returns all the ExceptionHandler instances representing exception
     * handlers that protect the instruction ih.
     */
    public ExceptionHandler[] getExceptionHandlers(final InstructionHandle ih) {
        final Set<ExceptionHandler> hsSet = exceptionHandlers.get(ih);
        if (hsSet == null) {
            return new ExceptionHandler[0];
        }
        return hsSet.toArray(new ExceptionHandler[hsSet.size()]);
    }

}
