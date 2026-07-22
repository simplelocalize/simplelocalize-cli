package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.LanguageTransform;
import io.simplelocalize.cli.client.dto.proxy.Mappings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UploadCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");

  private static String originalOsName;

  @BeforeEach
  void beforeAll()
  {
    originalOsName = System.getProperty("os.name");
  }

  @AfterEach
  void tearDown()
  {
    System.setProperty("os.name", originalOsName);
  }


  @Test
  public void shouldUploadTwelveFiles() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, times(12)).uploadFile(
            Mockito.refEq(UploadRequest.builder()
                            .withPath(Path.of("./junit/download-test/values-{lang}/strings.xml"))
                            .withFormat("android")
                            .withOptions(Collections.emptyList())
                            .withTags(List.of())
                            .build(),
                    "languageKey", "path"
            )
    );
  }

  @Test
  public void shouldUploadOneFileWithLangTemplate() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadLanguageKey("en");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, times(1)).uploadFile(
            Mockito.refEq(UploadRequest.builder()
                            .withPath(Path.of("./junit/download-test/values-en/strings.xml"))
                            .withFormat("android")
                            .withLanguageKey("en")
                            .withOptions(Collections.emptyList())
                            .withTags(List.of())
                            .build(),
                    "path"
            )
    );
  }

  @Test
  public void shouldUploadOneFileWithOnlyTranslationKeys() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadFormat("android");
    configuration.setUploadLanguageKey("en");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, times(1)).uploadFile(
            Mockito.refEq(UploadRequest.builder()
                    .withPath(Path.of("./junit/download-test/values-en/strings.xml"))
                    .withFormat("android")
                    .withLanguageKey("en")
                    .withOptions(Collections.emptyList())
                    .withTags(List.of())
                    .build()
            )
    );
  }

  @Test
  public void shouldSkipEmptyFile() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/empty-test/strings.xml");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verifyNoInteractions(client);
  }

  @Test
  public void shouldUploadOneFile() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadLanguageKey("en");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, times(1)).uploadFile(
            Mockito.refEq(UploadRequest.builder()
                            .withPath(Path.of("./junit/download-test/values-{lang}/strings.xml"))
                            .withFormat("android")
                            .withLanguageKey("en")
                            .withOptions(Collections.emptyList())
                            .withTags(List.of())
                            .build(),
                    "path"
            )
    );
  }

  @Test
  public void shouldUploadZeroFiles() throws Exception
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/zero-files");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verifyNoInteractions(client);
  }


  @Test
  public void shouldNotFindMatchingFilesWhenFakeWindows() throws Exception
  {
    //given
    System.setProperty("os.name", "Windows 10");
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, times(0)).uploadFile(any());
  }

  @Test
  public void shouldMapFileSystemLanguageToSimpleLocalizeLanguageKeyOnUpload() throws Exception
  {
    //given
    List<LanguageTransform> languageTransforms = List.of(
            new LanguageTransform("en_US", "en"), // SimpleLocalize key 'en_US' is stored on disk as 'en'
            new LanguageTransform("pl_PL", "pl")); // SimpleLocalize key 'pl_PL' is stored on disk as 'pl'

    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setUploadFormat("android");
    configuration.setMappings(Mappings.builder().lang(languageTransforms).build());

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    ArgumentCaptor<UploadRequest> uploadRequestCaptor = ArgumentCaptor.forClass(UploadRequest.class);
    Mockito.verify(client, times(12)).uploadFile(uploadRequestCaptor.capture());
    List<String> uploadedLanguageKeys = uploadRequestCaptor.getAllValues().stream()
            .map(UploadRequest::languageKey)
            .toList();

    Assertions.assertThat(uploadedLanguageKeys)
            .contains("en_US", "pl_PL") // mapped values
            .contains("de", "es", "fr", "it") // unmapped languages pass through unchanged
            .doesNotContain("en", "pl"); // file system names should have been mapped away
  }

  @Test
  public void shouldUseLanguageMappingReverseValueForSingleMappedFile() throws Exception
  {
    //given
    List<LanguageTransform> languageTransforms = List.of(new LanguageTransform("en_US", "en"));

    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setUploadFormat("android");
    configuration.setMappings(Mappings.builder().lang(languageTransforms).build());

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    ArgumentCaptor<UploadRequest> uploadRequestCaptor = ArgumentCaptor.forClass(UploadRequest.class);
    Mockito.verify(client, times(12)).uploadFile(uploadRequestCaptor.capture());
    UploadRequest enUsRequest = uploadRequestCaptor.getAllValues().stream()
            .filter(request -> "en_US".equals(request.languageKey()))
            .findFirst()
            .orElseThrow();
    Assertions.assertThat(enUsRequest.path().toString()).contains("values-en");
  }
}
