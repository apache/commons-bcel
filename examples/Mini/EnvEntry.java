package Mini;

/**
 * Entry in environment.
 *
 * @version $Id$
 * @author  <A HREF="http://www.berlin.de/~markus.dahm/">M. Dahm</A>
 */
public interface EnvEntry {
  public String getHashKey();
  public int    getLine();
  public int    getColumn();
}
