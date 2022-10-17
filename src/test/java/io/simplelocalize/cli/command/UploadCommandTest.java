package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Collections;

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
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(12)).uploadFile(
            Mockito.refEq(UploadRequest.UploadFileRequestBuilder.anUploadFileRequest()
                            .withPath(Path.of("./junit/download-test/values-{lang}/strings.xml"))
                            .withFormat("android")
                            .withOptions(Collections.emptyList())
                            .build(),
                    "languageKey", "path"
            )
    );
  }

  @Test
  public void shouldUploadOneFileWithLangTemplate() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setLanguageKey("en");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1)).uploadFile(
            Mockito.refEq(UploadRequest.UploadFileRequestBuilder.anUploadFileRequest()
                            .withPath(Path.of("./junit/download-test/values-{lang}/strings.xml"))
                            .withFormat("android")
                            .withLanguageKey("en")
                            .withOptions(Collections.emptyList())
                            .build(),
                    "path"
            )
    );
  }

  @Test
  public void shouldUploadOneFileWithOnlyTranslationKeys() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1)).uploadFile(
            Mockito.refEq(UploadRequest.UploadFileRequestBuilder.anUploadFileRequest()
                    .withPath(Path.of("./junit/download-test/values-en/strings.xml"))
                    .withFormat("android")
                    .withLanguageKey("")
                    .withOptions(Collections.emptyList())
                    .build()
            )
    );
  }

  @Test
  public void shouldSkipEmptyFile() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
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
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setLanguageKey("en");
    configuration.setUploadFormat("android");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1)).uploadFile(
            Mockito.refEq(UploadRequest.UploadFileRequestBuilder.anUploadFileRequest()
                            .withPath(Path.of("./junit/download-test/values-{lang}/strings.xml"))
                            .withFormat("android")
                            .withLanguageKey("en")
                            .withOptions(Collections.emptyList())
                            .build(),
                    "path"
            )
    );
  }

  @Test
  public void shouldUploadZeroFiles() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
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
  public void shouldThrowMatchingFilesCouldNotBeFoundWhenFakeWindows() throws Exception
  {
    //given
    System.setProperty("os.name", "Windows 10");
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadPath("./junit/download-test/values-en/strings.xml");
    configuration.setUploadFormat("android");

    //when & then
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    Assertions
            .assertThatThrownBy(uploadCommand::invoke)
            .isInstanceOf(IllegalArgumentException.class);
  }
}
