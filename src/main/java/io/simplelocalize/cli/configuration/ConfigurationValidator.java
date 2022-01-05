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
    Set<String> options = configuration.getUploadOptions();
    validateCommonOptions(options, "uploadOptions");
    validateApiKey(configuration);
  }

  public void validateDownloadConfiguration(Configuration configuration)
  {
    Set<String> options = configuration.getDownloadOptions();
    validateCommonOptions(options, "downloadOptions");
    validateApiKey(configuration);
  }

  private void validateApiKey(Configuration configuration)
  {

    String apiKey = configuration.getApiKey();
    if (StringUtils.isEmpty(apiKey))
    {
      log.error("Missing 'apiKey' value");
      throw new IllegalArgumentException();
    }
  }

  private void validateCommonOptions(Set<String> optionsArgumentValue, String argumentName)
  {
    for (String value : optionsArgumentValue)
    {
      try
      {
        Options.valueOf(value);
      } catch (Exception e)
      {
        log.error("Incorrect value '{}' in '{}' field", optionsArgumentValue, argumentName);
        throw new IllegalArgumentException();
      }
    }
  }

}
