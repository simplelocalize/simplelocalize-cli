package io.simplelocalize.cli.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public final class ConfigurationValidator
{
  private static final Logger log = LoggerFactory.getLogger(ConfigurationValidator.class);

  public void validateUploadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getUploadFormat(), "uploadFormat");
    validateIsNotEmptyOrNull(configuration.getUploadPath(), "uploadPath");
    validateCommonOptions(configuration.getUploadOptions(), "uploadOptions");
  }

  public void validateDownloadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getDownloadFormat(), "downloadFormat");
    validateIsNotEmptyOrNull(configuration.getDownloadPath(), "downloadPath");
    validateCommonOptions(configuration.getDownloadOptions(), "downloadOptions");
  }

  private void validateIsNotEmptyOrNull(String format, String argumentName)
  {

    if (StringUtils.isEmpty(format))
    {
      log.error("Missing '{}' value", argumentName);
      throw new IllegalArgumentException();
    }
  }

  private void validateCommonOptions(Set<String> options, String argumentName)
  {
    for (String value : options)
    {
      try
      {
        Options.valueOf(value);
      } catch (Exception e)
      {
        log.error("Incorrect value '{}' in '{}' field. Received: {}", options, argumentName, options);
        throw new IllegalArgumentException();
      }
    }
  }

}
