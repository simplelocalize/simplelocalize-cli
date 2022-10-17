package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.exception.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class ConfigurationValidator
{
  private static final Logger log = LoggerFactory.getLogger(ConfigurationValidator.class);

  public void validateUploadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getUploadFormat(), "uploadFormat");
    validateIsNotEmptyOrNull(configuration.getUploadPath(), "uploadPath");
    warnIfForwardSlashOnWindows(configuration.getUploadPath(), "uploadPath");
  }

  public void validateDownloadConfiguration(Configuration configuration)
  {
    validateIsNotEmptyOrNull(configuration.getApiKey(), "apiKey");
    validateIsNotEmptyOrNull(configuration.getDownloadFormat(), "downloadFormat");
    validateIsNotEmptyOrNull(configuration.getDownloadPath(), "downloadPath");
    warnIfForwardSlashOnWindows(configuration.getDownloadFormat(), "downloadPath");
  }

  private void warnIfForwardSlashOnWindows(String propertyName, String value)
  {
    String property = System.getProperty("os.name");
    String operatingSystem = Optional.ofNullable(property).map(String::toLowerCase).orElse("");
    boolean isWindows = operatingSystem.contains("win");
    boolean isContainsForwardSlash = value.contains("/");
    if (isContainsForwardSlash && isWindows)
    {
      log.warn("Warning: '{}' uses forward slash ('/') instead backslash ('\\') that may not work properly on Windows machines. Current value: '{}'", propertyName, value);
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
