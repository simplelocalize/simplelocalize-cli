package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class ConfigurationValidator
{

  private static final Logger log = LoggerFactory.getLogger(ConfigurationValidator.class);

  public void validateUploadConfiguration(Configuration configuration)
  {
    String uploadOptions = configuration.getUploadOptions();
    validateCommonOptions(uploadOptions, "uploadOptions");
    validateApiKey(configuration);
  }

  public void validateDownloadConfiguration(Configuration configuration)
  {
    String uploadOptions = configuration.getDownloadOptions();
    validateCommonOptions(uploadOptions, "downloadOption");
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

  private void validateApiKeyAgainstRealApi(Configuration configuration)
  {
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration);
    try
    {
      HttpResponse<String> httpResponse = client.validateApiKey();
      if (httpResponse.statusCode() != 200)
      {
        log.error("Incorrect 'apiKey' value");
        throw new IllegalArgumentException();
      }
    } catch (IOException | InterruptedException e)
    {
      log.warn("Unable to validate apiKey");
      Thread.currentThread().interrupt();
    }
  }

  private void validateCommonOptions(String optionsArgumentValue, String argumentName)
  {
    boolean hasOptions = StringUtils.isNotEmpty(optionsArgumentValue);
    boolean isMultiFile = optionsArgumentValue.equalsIgnoreCase("MULTI_FILE");
    if (hasOptions && !isMultiFile)
    {
      log.error("Incorrect value '{}' in '{}' field", optionsArgumentValue, argumentName);
      throw new IllegalArgumentException();
    }
  }

}
