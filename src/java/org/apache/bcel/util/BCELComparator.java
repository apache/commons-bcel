package org.apache.bcel.util;

/**
 * Used for BCEL comparison strategy
 * 
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @version $Id$
 * @since 5.2
 */
public interface BCELComparator {

    /**
     * Compare two objects and return what THIS.equals(THAT) should return
     * 
     * @param THIS
     * @param THAT
     * @return true if and only if THIS equals THAT
     */
    public boolean equals( Object THIS, Object THAT );


    /**
     * Return hashcode for THIS.hashCode()
     * 
     * @param THIS
     * @return hashcode for THIS.hashCode()
     */
    public int hashCode( Object THIS );
}
