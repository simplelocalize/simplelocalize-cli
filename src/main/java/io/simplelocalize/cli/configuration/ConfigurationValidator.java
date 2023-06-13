package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.exception.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationValidator
{
  private static final Logger log = LoggerFactory.getLogger(ConfigurationValidator.class);

  public void validateExtractConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
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
    validateEnvironmentValue(configuration);
  }

  public void validateAutoTranslationConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
  }

  public void validateHostingPublishConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getEnvironment(), "environment");
    validateEnvironmentValue(configuration);
  }

  public void validateGetStatusConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
  }

  private void validateEnvironmentValue(Configuration configuration)
  {
    String environment = configuration.getEnvironment();
    if (!environment.equals("production") && !environment.equals("latest"))
    {
      log.error("Environment must be either 'production' or 'latest'");
      throw new ConfigurationException("Environment must be either 'production' or 'latest'");
    }
  }

  private void validateIsNotEmptyOrNull(String format, String argumentName)
  {

    if (StringUtils.isEmpty(format))
    {
      log.error("Missing '{}' value", argumentName);
      throw new ConfigurationException();
    }
  }
}
