package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import io.simplelocalize.cli.exception.MissingPropertyException;

public class ConfigurationValidator {


  public void validate(Configuration configuration) {

    if (Strings.isNullOrEmpty(configuration.getToken())) {
      throw new MissingPropertyException("token");
    }

    String projectType = configuration.getProjectType();
    if (Strings.isNullOrEmpty(projectType)) {
      throw new MissingPropertyException("projectType");
    }
  }

}
