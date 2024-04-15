package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.exception.ConfigurationException;
import io.simplelocalize.cli.util.StringUtils;

public final class ConfigurationValidator
{
  public void validateExtractConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getProjectType(), "projectType");
  }

  public void validateUploadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getUploadFormat(), "uploadFormat");
    validateIsNotEmptyOrNull(configuration.getUploadPath(), "uploadPath");
  }

  public void validateDownloadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getDownloadFormat(), "downloadFormat");
    validateIsNotEmptyOrNull(configuration.getDownloadPath(), "downloadPath");
  }

  public void validateHostingPullConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getEnvironment(), "environment");
    validateIsNotEmptyOrNull(configuration.getPullPath(), "pullPath");
  }

  public void validateAutoTranslationConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
  }

  public void validateHostingPublishConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getEnvironment(), "environment");
  }

  public void validateGetStatusConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
  }

  public void validateGetPurgeConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
  }

  private void validateIsNotEmptyOrNull(String format, String argumentName)
  {

    if (StringUtils.isEmpty(format))
    {
      String message = "Missing '%s' value".formatted(argumentName);
      throw new ConfigurationException(message);
    }
  }
}
