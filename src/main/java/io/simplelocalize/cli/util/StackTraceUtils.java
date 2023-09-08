package io.simplelocalize.cli.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtils
{
  private StackTraceUtils()
  {
  }

  public static String getStackTrace(Exception exception)
  {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw, true);
    exception.printStackTrace(pw);
    return sw.getBuffer().toString();
  }
}
