package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import io.simplelocalize.cli.exception.MissingPropertyException;

public class ConfigurationValidator {


  public void validate(Configuration configuration) {

    String apiKey = configuration.getApiKey();
    if (Strings.isNullOrEmpty(apiKey)) {
      throw new MissingPropertyException("apiKey");
    }

    String projectType = configuration.getProjectType();
    if (Strings.isNullOrEmpty(projectType)) {
      throw new MissingPropertyException("projectType");
    }
  }

}
