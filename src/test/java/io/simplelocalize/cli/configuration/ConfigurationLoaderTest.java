package io.simplelocalize.cli.configuration;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.exception.ConfigurationException;
import io.simplelocalize.cli.util.TestLogEventFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

class ConfigurationLoaderTest
{

  private final ConfigurationLoader loader = new ConfigurationLoader();

  @Test
  void shouldLoadConfigurationFromFile() throws Exception
  {
    //given
    Path configurationFilePath = Path.of("./simplelocalize.yml");
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(loader.getClass());

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Loading configuration file from: ./simplelocalize.yml");
    Assertions.assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Configuration file loaded successfully");
  }


  @Test
  void shouldReturnDefaultConfigurationNotFound() throws Exception
  {
    //given
    Path configurationFilePath = Path.of("./non-existing-configuration.yml");
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(loader.getClass());

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Loading configuration file from: ./non-existing-configuration.yml");
    Assertions.assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Configuration file not found.");
  }

  @Test
  void shouldReturnDefaultConfigurationWhenNoPathProvided() throws Exception
  {
    //given
    Path configurationFilePath = null;
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(loader.getClass());

    //when
    Configuration configuration = loader.loadOrGetDefault(configurationFilePath);

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Loading configuration file from: simplelocalize.yml");
    Assertions.assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Configuration file loaded successfully");
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

    // common
    Assertions.assertThat(configuration.getApiKey()).isEqualTo("my-api-key");

    // upload
    Assertions.assertThat(configuration.getUploadPath()).isEqualTo("./my-upload-path");
    Assertions.assertThat(configuration.getUploadFormat()).isEqualTo("my-upload-format");
    Assertions.assertThat(configuration.getUploadLanguageKey()).isEqualTo("en");
    Assertions.assertThat(configuration.getUploadCustomerId()).isEqualTo("my-customer-id-1");
    Assertions.assertThat(configuration.getUploadNamespace()).isEqualTo("common");
    Assertions.assertThat(configuration.getUploadOptions()).containsExactlyInAnyOrder("SPLIT_BY_NAMESPACES");

    // download
    Assertions.assertThat(configuration.getDownloadPath()).isEqualTo("./my-download-path");
    Assertions.assertThat(configuration.getDownloadFormat()).isEqualTo("my-download-format");
    Assertions.assertThat(configuration.getDownloadLanguageKeys()).containsExactlyInAnyOrder("pl", "es");
    Assertions.assertThat(configuration.getDownloadCustomerId()).isEqualTo("my-customer-id-2");
    Assertions.assertThat(configuration.getDownloadNamespace()).isEqualTo("login");
    Assertions.assertThat(configuration.getDownloadOptions()).containsExactlyInAnyOrder("SPLIT_BY_NAMESPACES", "WRITE_NESTED");

    // auto-translate
    Assertions.assertThat(configuration.getAutoTranslateLanguageKeys()).isNotNull().containsExactlyInAnyOrder("en", "de");

    // extract
    Assertions.assertThat(configuration.getProjectType()).isEqualTo("yahoo/react-intl");
    Assertions.assertThat(configuration.getSearchDir()).isEqualTo("./target/test-classes/react-intl");
    Assertions.assertThat(configuration.getIgnoreKeys()).containsExactlyInAnyOrder("key one", "key_two");
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
