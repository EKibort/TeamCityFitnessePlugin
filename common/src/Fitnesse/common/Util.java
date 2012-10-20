package Fitnesse.common;

import java.util.Collection;
import java.util.Iterator;

/**
 * Example server and agent shared class
 */
public class Util {
  public final static String NAME = "Fitnesse";
  public final static String RUNNER_TYPE = "Fitnesse";


  public final static String PROPERTY_FITNESSE_PORT = "fitnessePort";
  public final static String PROPERTY_FITNESSE_TEST = "fitnesseTest";


  public static String join(Collection<?> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }


}
