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
import java.util.List;

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
  public void shouldUploadTwelveFilesWithRelativePathInformation() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setUploadOptions(List.of("MULTI_FILE"));
    configuration.setUploadPath("./junit/download-test/");
    configuration.setUploadFormat("multi-language-json");

    //when
    UploadCommand uploadCommand = new UploadCommand(client, configuration);
    uploadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(7)).uploadFile(
            Mockito.refEq(UploadRequest.UploadFileRequestBuilder.anUploadFileRequest()
                            .withFormat("multi-language-json")
                            .withOptions(List.of("MULTI_FILE"))
                            .build(),
                    "languageKey", "path", "relativePath"
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
