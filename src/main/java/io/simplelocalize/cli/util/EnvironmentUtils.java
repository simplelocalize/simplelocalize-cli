package io.simplelocalize.cli.util;

import org.slf4j.Logger;

public class EnvironmentUtils
{
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(EnvironmentUtils.class);

  private EnvironmentUtils()
  {
  }

  public static String convertDefaultEnvironmentKeyFromPreviousCliVersionsToV3IfNeeded(String environment)
  {
    if ("latest".equals(environment))
    {
      log.warn("[warning] You passed 'latest' value as environment key, please use '_latest' instead.");
      environment = "_" + environment;
    }

    if ("production".equals(environment))
    {
      log.warn("[warning] You passed 'production' value as environment key, please use '_production' instead.");
      environment = "_" + environment;
    }
    return environment;
  }
}
