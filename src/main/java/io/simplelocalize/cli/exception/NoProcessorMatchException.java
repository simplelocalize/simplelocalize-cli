package io.simplelocalize.cli.exception;

public class NoProcessorMatchException extends RuntimeException {
  public NoProcessorMatchException(String message) {
    super(message);
  }
}
