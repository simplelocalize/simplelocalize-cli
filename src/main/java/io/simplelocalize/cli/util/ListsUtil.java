package io.simplelocalize.cli.util;

import java.util.ArrayList;
import java.util.List;

public class ListsUtil {

  private ListsUtil() {
  }

  public static <T> List<T> combine(List<T> lhs, List<T> rhs) {
    ArrayList<T> copyLhs = new ArrayList<>(lhs);
    ArrayList<T> copyRhs = new ArrayList<>(rhs);
    ArrayList<T> output = new ArrayList();
    output.addAll(copyLhs);
    output.addAll(copyRhs);
    return output;
  }

}
