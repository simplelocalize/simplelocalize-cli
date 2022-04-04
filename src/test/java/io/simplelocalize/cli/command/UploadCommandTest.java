package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.configuration.Configuration;
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


}
