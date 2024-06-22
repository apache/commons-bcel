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
package Mini;

import java.util.concurrent.ConcurrentHashMap;

/**
 * For efficiency and convenience reasons we want our own hash table. It does not conform to java.util.Dictionary(yet).
 *
 * That environment contains all function definitions and identifiers. Hash keys are Strings (identifiers), which are mapped to a table index.
 *
 * The table consists of 'SIZE' fields which have 'SLOTS' subfields. Thus the maximum number of storable items is 'SLOTS' * 'SIZE'.
 */
public class Environment implements Cloneable {

    private static final int SIZE = 127; // Prime number large enough for most cases

    private final ConcurrentHashMap<String, EnvEntry> table;

    public Environment() {
        this(SIZE);
    }

    private Environment(final ConcurrentHashMap<String, EnvEntry> table) {
        this.table = table;
    }

    public Environment(final int size) {
        table = new ConcurrentHashMap<>(size);
    }

    @Override
    public Object clone() {
        return new Environment(new ConcurrentHashMap<>(table));
    }

    /**
     * Delete an object if it does exist.
     */
    public void delete(final String key) {
        if (key != null) {
            table.remove(key);
        }
    }

    /**
     * Gets entry from hash table.
     */
    public EnvEntry get(final String key) {
        return key != null ? table.get(key) : null;
    }

    public EnvEntry[] getEntries() {
        return table.values().toArray(new EnvEntry[0]);
    }

    /**
     * Inserts macro into table or overwrite old contents if it was already stored.
     *
     * @param obj the entry to add.
     */
    public void put(final EnvEntry obj) {
        table.put(obj.getHashKey(), obj);
    }

    @Override
    public String toString() {
        return table.toString();
    }
}
