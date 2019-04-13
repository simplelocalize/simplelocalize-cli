package io.simplelocalize.cli.exception;

public class MissingPropertyException extends RuntimeException {
  public MissingPropertyException(String propertyKey) {

    super("Property " + propertyKey + " is required. Please add this property to your config file.");
  }
}
