package io.simplelocalize.cli.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListsUtil {

  private ListsUtil() {
  }

  public static <T> List<T> combine(List<T> lhs, List<T> rhs) {
    ArrayList<T> copyLhs = Lists.newArrayList(lhs);
    ArrayList<T> copyRhs = Lists.newArrayList(rhs);
    ArrayList<T> output = Lists.newArrayList();
    output.addAll(copyLhs);
    output.addAll(copyRhs);
    return output;
  }

}
