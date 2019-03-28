package io.simplelocalize.cli.exception;

public class MissingPropertyException extends RuntimeException {
  public MissingPropertyException(String property) {

    super("Property " + property + " is required. Please add this property to your config file.");
  }
}
