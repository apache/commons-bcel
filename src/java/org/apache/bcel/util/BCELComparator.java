package org.apache.bcel.util;

/**
 * Used for BCEL comparison strategy
 * 
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 */
public interface BCELComparator {
	/**
	 * Compare two objects and return what THIS.equals(THAT) should return
	 * 
	 * @param THIS
	 * @param THAT
	 * @return
	 */
	public boolean equals(Object THIS, Object THAT);
	
	/**
	 * Return hashcode for THIS.hashCode()
	 * 
	 * @param THIS
	 * @param THAT
	 * @return
	 */
	public int hashCode(Object THIS);
}
