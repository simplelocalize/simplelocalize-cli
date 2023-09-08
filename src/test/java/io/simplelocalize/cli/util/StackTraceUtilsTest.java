package io.simplelocalize.cli.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StackTraceUtilsTest
{

  @Test
  void getStackTrace()
  {
    //given
    String value = null;

    //when
    try
    {
      value.length();
    }
    catch (Exception e)
    {
      String stackTrace = StackTraceUtils.getStackTrace(e);

      //then
      assertNotNull(stackTrace);
      assertTrue(stackTrace.contains("java.lang.NullPointerException"));
    }

  }
}
