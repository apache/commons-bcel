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
 * @author  <A HREF="http://www.berlin.de/~markus.dahm/">M. Dahm</A>
 */
public class Environment implements Cloneable {
  private static final int SIZE  = 127; // Prime number large enough for most cases
  private static final int SLOTS = 3;   // Number of slots of each field
  
  private int       size;               // The table is an array of
  private Vector[]  table;              // Vectors
  private int       elements=0;

  public Environment(int size) {
    this.size = size;
    table     = new Vector[size];
  }

  private Environment(Vector[] table) {
    size       = table.length;
    this.table = table;
  }

  public Environment() {
    this(SIZE);
  }

  private final int hashCode(String key) {
    return Math.abs(key.hashCode()) % size;
  }

  /**
   * Inserts macro into table or overwrite old contents if it
   * was already stored.
   */
  public void put(EnvEntry obj) {
    int    hash;
    Vector v;
    String key = obj.getHashKey();

    hash = hashCode(key);
    v    = table[hash];

    elements++; // Count

    if(v == null) // Not yet initialized
      table[hash] = v = new Vector(SLOTS);
    else {
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
    Vector    v;
    EnvEntry entry = null;

    hash = hashCode(key);
    v    = table[hash];

    if(v == null)
      return null;

    try {
      int index = lookup(v, key);

      if(index >= 0)
	entry = (EnvEntry)v.elementAt(index);
    } catch(ArrayIndexOutOfBoundsException e) {}

    return entry;
  }

  /**
   * Delete an object if it does exist.
   */
  public void delete(String key) {
    int       hash;
    Vector    v;

    hash = hashCode(key);
    v    = table[hash];

    if(v == null)
      return;

    try {
      int index = lookup(v, key);

      if(index >= 0) {
	elements--; // Count
	v.removeElementAt(index);
      }
    } catch(ArrayIndexOutOfBoundsException e) {}
  }

  private static final int lookup(Vector v, String key) 
       throws ArrayIndexOutOfBoundsException
  {
    int len = v.size();

    for(int i=0; i < len; i++) {
      EnvEntry entry = (EnvEntry)v.elementAt(i);

      if(entry.getHashKey().equals(key)) // Found index
	return i;
    }

    return -1;
  }

  public Object clone() { 
    Vector[] copy = new Vector[size];

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

  public String toString() {
    StringBuffer buf = new StringBuffer();

    for(int i=0; i < size; i++)
      if(table[i] != null)
	buf.append(table[i] + "\n");

    return buf.toString();
  }

  public EnvEntry[] getEntries() {
    EnvEntry[] entries = new EnvEntry[elements];
    int        k       = 0;
    Vector     v;

    for(int i=0; i < size; i++) {
      if((v = table[i]) != null) {
	int len = v.size();
	try {
	  for(int j=0; j < len; j++)
	    entries[k++] = (EnvEntry)v.elementAt(j);
	} catch(ArrayIndexOutOfBoundsException e) {}  
      }
    }

    return entries;
  }
}
