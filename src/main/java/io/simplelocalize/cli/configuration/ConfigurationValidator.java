package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import io.simplelocalize.cli.exception.MissingPropertyException;

public class ConfigurationValidator {


  public void validate(Configuration configuration) {
    if (Strings.isNullOrEmpty(configuration.getClientId())) {
      throw new MissingPropertyException("clientId");
    }

    if (Strings.isNullOrEmpty(configuration.getClientSecret())) {
      throw new MissingPropertyException("clientSecret");
    }

    if (Strings.isNullOrEmpty(configuration.getProjectToken())) {
      throw new MissingPropertyException("projectToken");
    }

    String projectType = configuration.getProjectType();
    if (Strings.isNullOrEmpty(projectType)) {
      throw new MissingPropertyException("projectType");
    }
  }

}
