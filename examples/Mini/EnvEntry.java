package Mini;

/**
 * Entry in environment.
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public interface EnvEntry {
  public String getHashKey();
  public int    getLine();
  public int    getColumn();
}
