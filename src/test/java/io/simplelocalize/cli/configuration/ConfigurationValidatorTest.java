package io.simplelocalize.cli.configuration;

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
    configuration.setUploadOptions(List.of("MULTI_FILE", "USE_NESTED_JSON"));

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
    configuration.setDownloadOptions(List.of("MULTI_FILE", "USE_NESTED_JSON"));

    //when
    configurationValidator.validateDownloadConfiguration(configuration);
    //then

  }

  @Test
  void shouldThrowIfOptionIsLowercase()
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadFormat("multi-language-json");
    configuration.setDownloadPath("./path");
    configuration.setDownloadOptions(List.of("multi_file", "USE_NESTED_JSON"));

    //when
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      configurationValidator.validateDownloadConfiguration(configuration);
    });
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
    configuration.setDownloadOptions(List.of("USE_NESTED_JSON"));

    //when
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      configurationValidator.validateDownloadConfiguration(configuration);
    });
    //then
  }

  @Test
  void shouldThrowIfOptionIsEmpty()
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadFormat("multi-language-json");
    configuration.setDownloadPath("./path");
    configuration.setDownloadOptions(List.of("MULTI_FILE", "USE_NESTED_JSON", ""));

    //when
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      configurationValidator.validateDownloadConfiguration(configuration);
    });
    //then
  }
}
