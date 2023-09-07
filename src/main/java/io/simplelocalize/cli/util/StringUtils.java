package io.simplelocalize.cli.util;

public class StringUtils
{

  public static boolean isEmpty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  public static boolean isNotEmpty(final CharSequence cs) {
    return !isEmpty(cs);
  }

  public static boolean isBlank(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(final CharSequence cs) {
    return !isBlank(cs);
  }

}
