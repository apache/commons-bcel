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
package Mini;

import java.util.Vector;

/**
 * For efficiency and convenience reasons we want our own hash table. It does
 * not conform to java.util.Dictionary(yet).
 *
 * That environment contains all function definitions and identifiers.
 * Hash keys are Strings (identifiers), which are mapped to a table index.
 *
 * The table consists of `SIZE' fields which have `SLOTS' subfields. Thus 
 * the maximum number of storable items is `SLOTS' * `SIZE'.
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class Environment implements Cloneable {
  private static final int SIZE  = 127; // Prime number large enough for most cases
  private static final int SLOTS = 3;   // Number of slots of each field
  
  private int       size;               // The table is an array of
  private Vector<EnvEntry>[]  table;              // Vectors
  private int       elements=0;

  public Environment(int size) {
    this.size = size;
    table     = new Vector[size];
  }

  private Environment(Vector<EnvEntry>[] table) {
    size       = table.length;
    this.table = table;
  }

  public Environment() {
    this(SIZE);
  }

  private int hashCode(String key) {
    return Math.abs(key.hashCode()) % size;
  }

  /**
   * Inserts macro into table or overwrite old contents if it
   * was already stored.
   */
  public void put(EnvEntry obj) {
    int    hash;
    Vector<EnvEntry> v;
    String key = obj.getHashKey();

    hash = hashCode(key);
    v    = table[hash];

    elements++; // Count

    if(v == null) {
        table[hash] = v = new Vector<EnvEntry>(SLOTS);
    } else {
      try {
        int index = lookup(v, key);

        if(index >= 0) {
          v.setElementAt(obj, index); // Overwrite
          return;
        }
      } catch(ArrayIndexOutOfBoundsException e) {}
    }

    // Not found in Vector -> add it
    v.addElement(obj);
  }

  /** Get entry from hash table.
   */
  public EnvEntry get(String key) {
    int       hash;
    Vector<EnvEntry>    v;
    EnvEntry entry = null;

    hash = hashCode(key);
    v    = table[hash];

    if(v == null) {
        return null;
    }

    try {
      int index = lookup(v, key);

      if(index >= 0) {
        entry = v.elementAt(index);
    }
    } catch(ArrayIndexOutOfBoundsException e) {}

    return entry;
  }

  /**
   * Delete an object if it does exist.
   */
  public void delete(String key) {
    int       hash;
    Vector<EnvEntry>    v;

    hash = hashCode(key);
    v    = table[hash];

    if(v == null) {
        return;
    }

    try {
      int index = lookup(v, key);

      if(index >= 0) {
        elements--; // Count
        v.removeElementAt(index);
      }
    } catch(ArrayIndexOutOfBoundsException e) {}
  }

  private static int lookup(Vector<EnvEntry> v, String key) 
       throws ArrayIndexOutOfBoundsException
  {
    int len = v.size();

    for(int i=0; i < len; i++) {
      EnvEntry entry = v.elementAt(i);

      if(entry.getHashKey().equals(key)) {
        return i;
    }
    }

    return -1;
  }

  @Override
  public Object clone() { 
    Vector<EnvEntry>[] copy = new Vector[size];

    for(int i=0; i < size; i++) {
      if(table[i] != null) {
        copy[i] = (Vector)table[i].clone(); // Copies references

        /*
        int len = table[i].size();

        copy[i] = new Vector(len);
        try {
          for(int j=0; j < len; j++)
            copy[i].addElement(table[i].elementAt(j));
        } catch(ArrayIndexOutOfBoundsException e) {}*/
      }
    }

    return new Environment(copy);
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();

    for(int i=0; i < size; i++) {
        if(table[i] != null) {
            buf.append(table[i] + "\n");
        }
    }

    return buf.toString();
  }

  public EnvEntry[] getEntries() {
    EnvEntry[] entries = new EnvEntry[elements];
    int        k       = 0;
    Vector<EnvEntry>     v;

    for(int i=0; i < size; i++) {
      if((v = table[i]) != null) {
        int len = v.size();
        try {
          for(int j=0; j < len; j++) {
        entries[k++] = v.elementAt(j);
    }
        } catch(ArrayIndexOutOfBoundsException e) {}  
      }
    }

    return entries;
  }
}
