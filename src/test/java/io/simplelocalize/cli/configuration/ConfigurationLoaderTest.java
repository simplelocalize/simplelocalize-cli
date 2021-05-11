package io.simplelocalize.cli.configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class ConfigurationLoaderTest {

  private final ConfigurationLoader loader = new ConfigurationLoader();

  @Test
  public void shouldLoadConfiguration() throws Exception {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize.yml").toURI().toString().replace("file:", "");

    //when
    Configuration configuration = loader.loadOrGetDefault(Path.of(pathToConfig));

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(configuration.getApiKey()).isEqualTo("abc-apiKey");
    Assertions.assertThat(configuration.getProjectType()).isEqualTo("yahoo/react-intl");
    Assertions.assertThat(configuration.getSearchDir()).isEqualTo("./target/test-classes/react-intl");
  }

  @Test
  public void shouldLoadConfigurationWithCurrentDirectory() throws Exception {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize-without-search-dir.yml").toURI().toString().replace("file:", "");

    //when
    Configuration configuration = loader.loadOrGetDefault(Path.of(pathToConfig));

    //then
    Assertions.assertThat(configuration.getSearchDir()).isEqualTo(".");
  }


  @Test
  public void shouldLoadEmptyConfigurationNotFound() throws Exception
  {
    //given
    String pathToConfig = "somepath";

    //when
    Configuration configuration = loader.loadOrGetDefault(Path.of(pathToConfig));

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(configuration.getApiKey()).isNull();
  }

  @Test
  public void shouldLoadEmptyConfigurationFileWhenInvalidFile() throws Exception
  {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize-invalid.yml").toURI().toString().replace("file:", "");

    //when
    Configuration configuration = loader.loadOrGetDefault(Path.of(pathToConfig));

    //then
    Assertions.assertThat(configuration).isNotNull();
    Assertions.assertThat(configuration.getApiKey()).isNull();
  }

  @Test
  public void shouldLoadConfigurationWithDeprecatedUploadTokenAsApiKey() throws Exception {
    //given
    ClassLoader classLoader = getClass().getClassLoader();
    String pathToConfig = classLoader.getResource("simplelocalize-with-deprecated-upload-token.yml").toURI().toString().replace("file:", "");

    //when
    Configuration configuration = loader.loadOrGetDefault(Path.of(pathToConfig));

    //then
    Assertions.assertThat(configuration.getApiKey()).isEqualTo("abc");
  }
}
