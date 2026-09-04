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
package org.apache.bcel.verifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class produces instances of the Verifier class. Its purpose is to make sure that they are singleton instances with respect to the class name they
 * operate on. That means, for every class (represented by a unique fully qualified class name) there is at most one cached Verifier. The cache is bounded (see
 * {@link #MAX_CACHE_SIZE_PROPERTY}); after eviction, a new Verifier is transparently created on the next request for that class name.
 * <p>
 * The system property {@code org.apache.bcel.verifier.VerifierFactory.maxCacheSize} controls how many Verifier instances this factory caches;
 * least-recently-used entries are evicted first. Verifier names are taken from the constant pools of the (possibly untrusted) classes being verified, so an
 * unbounded cache would let a single hostile class file referencing many distinct bogus type names grow the heap without limit in a long-running process. Set
 * the property to {@code 0} or a negative value to opt out and restore the historical unbounded behavior.
 * </p>
 *
 * @see Verifier
 */
public class VerifierFactory {

    /**
     * Name of the system property controlling how many Verifier instances this factory caches; least-recently-used entries are evicted first. Verifier names
     * are taken from the constant pools of the (possibly untrusted) classes being verified, so an unbounded cache would let a single hostile class file
     * referencing many distinct bogus type names grow the heap without limit in a long-running process. Set the property to {@code 0} or a negative value to
     * opt out and restore the historical unbounded behavior.
     */
    static final String MAX_CACHE_SIZE_PROPERTY = "org.apache.bcel.verifier.VerifierFactory.maxCacheSize";

    /**
     * Default value used when {@link #MAX_CACHE_SIZE_PROPERTY} is not set.
     *
     * @since 6.13.0
     */
    public static final int DEFAULT_MAX_CACHE_SIZE = 10_000;

    /**
     * The map that holds the data about the already-constructed Verifier instances, in least-recently-used order,
     * bounded by {@link #MAX_CACHE_SIZE_PROPERTY}.
     */
    private static final Map<String, Verifier> MAP = new LinkedHashMap<String, Verifier>(16, 0.75f, true) {

        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(final Map.Entry<String, Verifier> eldest) {
            final int maxCacheSize = Integer.getInteger(MAX_CACHE_SIZE_PROPERTY, DEFAULT_MAX_CACHE_SIZE).intValue();
            return maxCacheSize > 0 && size() > maxCacheSize;
        }
    };

    /**
     * The VerifierFactoryObserver instances that observe the VerifierFactory.
     */
    private static final List<VerifierFactoryObserver> OBSVERVERS = new Vector<>();

    /**
     * Adds the VerifierFactoryObserver o to the list of observers.
     *
     * @param o The observer to add.
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
     *
     * @param o The observer to remove.
     */
    public static void detach(final VerifierFactoryObserver o) {
        OBSVERVERS.remove(o);
    }

    /**
     * Returns the verifier responsible for the class with the given name. Possibly a new Verifier object is
     * transparently created; if the cache bound ({@link #MAX_CACHE_SIZE_PROPERTY}) has been reached, the
     * least-recently-used cached Verifier is evicted first.
     *
     * @param fullyQualifiedClassName The fully qualified class name.
     * @return The verifier responsible for the class with the given name.
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
     *
     * @return array of all Verifier instances.
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
