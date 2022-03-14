package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.exception.ConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class ConfigurationLoaderTest
{

  private final ConfigurationLoader loader = new ConfigurationLoader();


  @Test
  void shouldReturnDefaultConfigurationNotFound() throws Exception
  {
    //given
    Path configurationFilePath = Path.of("./non-existing-configuration.yml");

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
  }

  @Test
  void shouldReturnDefaultConfigurationWhenNoPathProvided() throws Exception
  {
    //given
    Path configurationFilePath = null;

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
  }

  @Test
  void shouldLoadConfiguration() throws Exception
  {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize.yml").toURI().toString().replace("file:", "");
    Path configurationFilePath = Path.of(pathToConfig);

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(configuration.getApiKey()).isEqualTo("my-api-key");
    Assertions.assertThat(configuration.getProjectType()).isEqualTo("yahoo/react-intl");
    Assertions.assertThat(configuration.getSearchDir()).isEqualTo("./target/test-classes/react-intl");
    Assertions.assertThat(configuration.getIgnoreKeys()).containsExactlyInAnyOrder("key one", "key_two");

    Assertions.assertThat(configuration.getUploadPath()).isEqualTo("./my-upload-path");
    Assertions.assertThat(configuration.getUploadFormat()).isEqualTo("my-upload-format");
    Assertions.assertThat(configuration.getUploadOptions()).containsExactlyInAnyOrder("MULTI_FILE");

    Assertions.assertThat(configuration.getDownloadPath()).isEqualTo("./my-download-path");
    Assertions.assertThat(configuration.getDownloadFormat()).isEqualTo("my-download-format");
    Assertions.assertThat(configuration.getDownloadOptions()).containsExactlyInAnyOrder("MULTI_FILE", "WRITE_NESTED");

    Assertions.assertThat(configuration.getLanguageKey()).isEqualTo("en");

    Assertions.assertThat(configuration.getIgnorePaths())
            .containsExactlyInAnyOrder(
                    "./ignore/path/1",
                    "./**/ignore/",
                    "./file.json",
                    "my-file.json"
            );
  }

  @Test
  void shouldThrowErrorWhenInvalidFile() throws Exception
  {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize-invalid.yml").toURI().toString().replace("file:", "");

    //when & then
    Assertions
            .assertThatThrownBy(() -> loader.loadOrGetDefault(Path.of(pathToConfig)))
            .isInstanceOf(ConfigurationException.class);
  }
}
