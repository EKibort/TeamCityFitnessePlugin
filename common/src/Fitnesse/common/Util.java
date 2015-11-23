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
  public final static String PROPERTY_FITNESSE_TEST_RUN_PARALLEL = "fitnesseTestRunParallel";


  public static String join(Collection<?> s) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = s.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
            builder.append(" ");
        }
        return builder.toString();
    }


}
