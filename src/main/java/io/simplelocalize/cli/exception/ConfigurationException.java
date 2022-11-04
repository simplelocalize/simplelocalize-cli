package io.simplelocalize.cli.exception;

public class ConfigurationException extends RuntimeException
{
  public ConfigurationException()
  {
  }

  public ConfigurationException(String message)
  {
    super(message);
  }
}
