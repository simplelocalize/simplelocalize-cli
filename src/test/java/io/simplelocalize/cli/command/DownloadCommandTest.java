package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.ExportRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import io.simplelocalize.cli.client.dto.proxy.LanguageTransform;
import io.simplelocalize.cli.client.dto.proxy.Mappings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DownloadCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");

  @Test
  void shouldDownloadMultipleFiles() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadPath("./my-project-path");
    configuration.setDownloadLanguageKeys(Collections.singletonList("en"));
    configuration.setDownloadOptions(List.of("SPLIT_BY_NAMESPACES"));
    configuration.setDownloadFormat("android");

    //when
    Mockito.when(client.exportFiles(ExportRequest.builder()
                    .withFormat("android")
                    .withLanguageKeys(List.of("en"))
                    .withOptions(List.of("SPLIT_BY_NAMESPACES"))
                    .withTags(List.of())
                    .build()))
            .thenReturn(List.of(
                    DownloadableFile.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    DownloadableFile.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file2.xml").build()
            ));

    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    "./my-project-path");
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file2.xml").build(),
                    "./my-project-path");
  }

  @Test
  void shouldMapSimpleLocalizeLanguageKeyToFileSystemLanguageOnDownload() throws Exception
  {
    //given
    List<LanguageTransform> languageTransforms = List.of(
            new LanguageTransform("en", "english"),
            new LanguageTransform("pl", "polish"));

    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadPath("./messages/{lang}/strings.json");
    configuration.setDownloadFormat("single-language-json");
    configuration.setMappings(Mappings.builder().lang(languageTransforms).build());

    //when
    Mockito.when(client.exportFiles(Mockito.any(ExportRequest.class)))
            .thenReturn(List.of(
                    DownloadableFile.builder().withLanguage("en").withUrl("https://s3.simplelocalize.io/en.json").build(),
                    DownloadableFile.builder().withLanguage("pl").withUrl("https://s3.simplelocalize.io/pl.json").build(),
                    DownloadableFile.builder().withLanguage("de").withUrl("https://s3.simplelocalize.io/de.json").build()
            ));

    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.builder().withLanguage("english").withUrl("https://s3.simplelocalize.io/en.json").build(),
                    "./messages/{lang}/strings.json");
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.builder().withLanguage("polish").withUrl("https://s3.simplelocalize.io/pl.json").build(),
                    "./messages/{lang}/strings.json");
    // 'de' has no mapping, so it is passed through unchanged
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.builder().withLanguage("de").withUrl("https://s3.simplelocalize.io/de.json").build(),
                    "./messages/{lang}/strings.json");
  }

  @ParameterizedTest
  @ValueSource(strings = {"Linux", "Mac OS X", "Windows 10"})
  void shouldMapLanguageKeyOnDownloadRegardlessOfOperatingSystem(String osName) throws Exception
  {
    //given
    String originalOsName = System.getProperty("os.name");
    System.setProperty("os.name", osName);
    try
    {
      List<LanguageTransform> languageTransforms = List.of(new LanguageTransform("en", "english"));

      Configuration configuration = Configuration.defaultConfiguration();
      configuration.setApiKey("my-api-key");
      configuration.setDownloadPath("./messages/values-{lang}/strings.json");
      configuration.setDownloadFormat("single-language-json");
      configuration.setMappings(Mappings.builder().lang(languageTransforms).build());

      Mockito.when(client.exportFiles(Mockito.any(ExportRequest.class)))
              .thenReturn(List.of(
                      DownloadableFile.builder().withLanguage("en").withUrl("https://s3.simplelocalize.io/en.json").build()
              ));

      //when
      DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
      downloadCommand.invoke();

      //then
      Mockito.verify(client, Mockito.times(1))
              .downloadFile(
                      DownloadableFile.builder().withLanguage("english").withUrl("https://s3.simplelocalize.io/en.json").build(),
                      "./messages/values-{lang}/strings.json");
    } finally
    {
      System.setProperty("os.name", originalOsName);
    }
  }
}
