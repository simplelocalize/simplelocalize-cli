package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.exception.ConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ConfigurationValidatorTest
{

  private final ConfigurationValidator configurationValidator = new ConfigurationValidator();

  @Test
  void validateUploadConfiguration()
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadFormat("multi-language-json");
    configuration.setUploadPath("./path");
    configuration.setUploadOptions(List.of("SPLIT_BY_NAMESPACES", "WRITE_NESTED"));

    //when
    configurationValidator.validateUploadConfiguration(configuration);

    //then

  }

  @Test
  void validateDownloadConfiguration()
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadFormat("multi-language-json");
    configuration.setDownloadPath("./path");
    configuration.setDownloadOptions(List.of("SPLIT_BY_NAMESPACES", "WRITE_NESTED"));

    //when
    configurationValidator.validateDownloadConfiguration(configuration);
    //then

  }

  @Test
  void shouldThrowWhenApiKeyIsMissing()
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("");
    configuration.setDownloadFormat("multi-language-json");
    configuration.setDownloadPath("./path");
    configuration.setDownloadOptions(List.of("WRITE_NESTED"));

    //when
    Assertions.assertThrows(ConfigurationException.class, () -> {
      configurationValidator.validateDownloadConfiguration(configuration);
    });
    //then
  }
}
