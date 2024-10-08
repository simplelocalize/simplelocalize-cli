package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.exception.ConfigurationException;
import io.simplelocalize.cli.util.StringUtils;

public final class ConfigurationValidatorUtil
{
  public static void validateIsNotEmptyOrNull(String value, String argumentName)
  {

    if (StringUtils.isBlank(value))
    {
      String message = "Missing '%s' value".formatted(argumentName);
      throw new ConfigurationException(message);
    }
  }
}
