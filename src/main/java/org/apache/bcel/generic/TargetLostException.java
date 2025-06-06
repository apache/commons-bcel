/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.bcel.generic;

/**
 * Thrown by {@link InstructionList} when one or multiple disposed instructions are still being referenced by an {@link InstructionTargeter} object. I.e. the
 * {@link InstructionTargeter} has to be notified that (one of) the {@link InstructionHandle} it is referencing is being removed from the
 * {@link InstructionList} and thus not valid anymore.
 *
 * <p>
 * Making this an exception instead of a return value forces the user to handle these case explicitly in a try { ... } catch. The following code illustrates how
 * this may be done:
 * </p>
 *
 * <pre>
 *     ...
 *     try {
 *         il.delete(start_ih, end_ih);
 *     } catch (TargetLostException e) {
 *         for (InstructionHandle target : e.getTargets()) {
 *             for (InstructionTargeter targeter : target.getTargeters()) {
 *                 targeter.updateTarget(target, new_target);
 *             }
 *         }
 *     }
 * </pre>
 *
 * @see InstructionHandle
 * @see InstructionList
 * @see InstructionTargeter
 */
public final class TargetLostException extends Exception {

    private static final long serialVersionUID = -6857272667645328384L;
    private final InstructionHandle[] targets;

    TargetLostException(final InstructionHandle[] targets, final String message) {
        super(message);
        this.targets = targets;
    }

    /**
     * Gets the list of instructions still being targeted.
     *
     * @return list of instructions still being targeted.
     */
    public InstructionHandle[] getTargets() {
        return targets;
    }
}
