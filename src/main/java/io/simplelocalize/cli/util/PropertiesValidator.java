package io.simplelocalize.cli.util;

import io.simplelocalize.cli.ConfigProperty;
import io.simplelocalize.cli.exception.MissingPropertyException;

import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PropertiesValidator {

  private PropertiesValidator() {
  }

  public static void validate(Properties properties) {
    ConfigProperty[] configProperties = ConfigProperty.values();
    for (ConfigProperty configProperty : configProperties) {
      String property = properties.getProperty(configProperty.getKey());
      if (isNullOrEmpty(property) && configProperty.isRequired()) {
        throw new MissingPropertyException(property);
      }
    }
  }

}
