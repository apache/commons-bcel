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

import java.util.Vector;

/**
 * For efficiency and convenience reasons we want our own hash table. It does not conform to java.util.Dictionary(yet).
 *
 * That environment contains all function definitions and identifiers. Hash keys are Strings (identifiers), which are
 * mapped to a table index.
 *
 * The table consists of 'SIZE' fields which have 'SLOTS' subfields. Thus the maximum number of storable items is
 * 'SLOTS' * 'SIZE'.
 */
public class Environment implements Cloneable {
    private static final int SIZE = 127; // Prime number large enough for most cases
    private static final int SLOTS = 3; // Number of slots of each field

    private static int lookup(final Vector<EnvEntry> v, final String key) throws ArrayIndexOutOfBoundsException {
        final int len = v.size();

        for (int i = 0; i < len; i++) {
            final EnvEntry entry = v.elementAt(i);

            if (entry.getHashKey().equals(key)) {
                return i;
            }
        }

        return -1;
    }

    private final int size; // The table is an array of
    private final Vector<EnvEntry>[] table; // Vectors

    private int elements = 0;

    public Environment() {
        this(SIZE);
    }

    public Environment(final int size) {
        this.size = size;
        table = new Vector[size];
    }

    private Environment(final Vector<EnvEntry>[] table) {
        size = table.length;
        this.table = table;
    }

    @Override
    public Object clone() {
        final Vector<EnvEntry>[] copy = new Vector[size];

        for (int i = 0; i < size; i++) {
            if (table[i] != null) {
                copy[i] = (Vector) table[i].clone(); // Copies references

                /*
                 * int len = table[i].size();
                 *
                 * copy[i] = new Vector(len); try { for(int j=0; j < len; j++) copy[i].addElement(table[i].elementAt(j)); }
                 * catch(ArrayIndexOutOfBoundsException e) {}
                 */
            }
        }

        return new Environment(copy);
    }

    /**
     * Delete an object if it does exist.
     */
    public void delete(final String key) {
        int hash;
        Vector<EnvEntry> v;

        hash = hashCode(key);
        v = table[hash];

        if (v == null) {
            return;
        }

        try {
            final int index = lookup(v, key);

            if (index >= 0) {
                elements--; // Count
                v.removeElementAt(index);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Get entry from hash table.
     */
    public EnvEntry get(final String key) {
        int hash;
        Vector<EnvEntry> v;
        EnvEntry entry = null;

        hash = hashCode(key);
        v = table[hash];

        if (v == null) {
            return null;
        }

        try {
            final int index = lookup(v, key);

            if (index >= 0) {
                entry = v.elementAt(index);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }

        return entry;
    }

    public EnvEntry[] getEntries() {
        final EnvEntry[] entries = new EnvEntry[elements];
        int k = 0;
        Vector<EnvEntry> v;

        for (int i = 0; i < size; i++) {
            if ((v = table[i]) != null) {
                final int len = v.size();
                try {
                    for (int j = 0; j < len; j++) {
                        entries[k++] = v.elementAt(j);
                    }
                } catch (final ArrayIndexOutOfBoundsException e) {
                }
            }
        }

        return entries;
    }

    private int hashCode(final String key) {
        return Math.abs(key.hashCode()) % size;
    }

    /**
     * Inserts macro into table or overwrite old contents if it was already stored.
     */
    public void put(final EnvEntry obj) {
        int hash;
        Vector<EnvEntry> v;
        final String key = obj.getHashKey();

        hash = hashCode(key);
        v = table[hash];

        elements++; // Count

        if (v == null) {
            table[hash] = v = new Vector<>(SLOTS);
        } else {
            try {
                final int index = lookup(v, key);

                if (index >= 0) {
                    v.setElementAt(obj, index); // Overwrite
                    return;
                }
            } catch (final ArrayIndexOutOfBoundsException e) {
            }
        }

        // Not found in Vector -> add it
        v.addElement(obj);
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();

        for (int i = 0; i < size; i++) {
            if (table[i] != null) {
                buf.append(table[i] + "\n");
            }
        }

        return buf.toString();
    }
}
