package io.simplelocalize.cli.exception;

public class ApiRequestException extends RuntimeException
{
  public ApiRequestException(String apiMessage)
  {
    super(apiMessage);
  }
}
