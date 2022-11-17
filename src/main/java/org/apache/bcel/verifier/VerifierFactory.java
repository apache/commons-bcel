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
 */
package org.apache.bcel.verifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class produces instances of the Verifier class. Its purpose is to make sure that they are singleton instances
 * with respect to the class name they operate on. That means, for every class (represented by a unique fully qualified
 * class name) there is exactly one Verifier.
 *
 * @see Verifier
 */
public class VerifierFactory {

    /**
     * The HashMap that holds the data about the already-constructed Verifier instances.
     */
    private static final Map<String, Verifier> MAP = new HashMap<>();

    /**
     * The VerifierFactoryObserver instances that observe the VerifierFactory.
     */
    private static final List<VerifierFactoryObserver> OBSVERVERS = new Vector<>();

    /**
     * Adds the VerifierFactoryObserver o to the list of observers.
     */
    public static void attach(final VerifierFactoryObserver o) {
        OBSVERVERS.add(o);
    }

    /**
     * Clears the factory.
     *
     * @since 6.6.2
     */
    public static void clear() {
        MAP.clear();
        OBSVERVERS.clear();
    }

    /**
     * Removes the VerifierFactoryObserver o from the list of observers.
     */
    public static void detach(final VerifierFactoryObserver o) {
        OBSVERVERS.remove(o);
    }

    /**
     * Returns the (only) verifier responsible for the class with the given name. Possibly a new Verifier object is
     * transparently created.
     *
     * @return the (only) verifier responsible for the class with the given name.
     */
    public static Verifier getVerifier(final String fullyQualifiedClassName) {
        return MAP.computeIfAbsent(fullyQualifiedClassName, k -> {
            final Verifier v = new Verifier(k);
            notify(k);
            return v;
        });
    }

    /**
     * Returns all Verifier instances created so far. This is useful when a Verifier recursively lets the VerifierFactory
     * create other Verifier instances and if you want to verify the transitive hull of referenced class files.
     */
    public static Verifier[] getVerifiers() {
        return MAP.values().toArray(Verifier.EMPTY_ARRAY);
    }

    /**
     * Notifies the observers of a newly generated Verifier.
     */
    private static void notify(final String fullyQualifiedClassName) {
        // notify the observers
        OBSVERVERS.forEach(vfo -> vfo.update(fullyQualifiedClassName));
    }

    /**
     * The VerifierFactory is not instantiable.
     */
    private VerifierFactory() {
    }
}
