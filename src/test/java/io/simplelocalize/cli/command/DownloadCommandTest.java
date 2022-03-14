package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.aDownloadRequest;
import static io.simplelocalize.cli.client.dto.DownloadableFile.DownloadableFileBuilder.aDownloadableFile;

@ExtendWith(MockitoExtension.class)
public class DownloadCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");


  @Test
  public void shouldDownloadSingleFile() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadPath("./junit/download-test/values-{lang}/strings.xml");
    configuration.setLanguageKey("en");
    configuration.setDownloadFormat("android");

    //when
    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1)).downloadFile(
            aDownloadRequest()
                    .withPath("./junit/download-test/values-{lang}/strings.xml")
                    .withFormat("android")
                    .withLanguageKey("en")
                    .withOptions(Collections.emptyList())
                    .build()

    );
  }

  @Test
  public void shouldDownloadMultipleFiles() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadPath("./my-project-path");
    configuration.setLanguageKey("en");
    configuration.setDownloadOptions(List.of("MULTI_FILE"));
    configuration.setDownloadFormat("android");

    //when
    Mockito.when(client.fetchDownloadableFiles(aDownloadRequest()
                    .withPath("./my-project-path")
                    .withFormat("android")
                    .withLanguageKey("en")
                    .withOptions(List.of("MULTI_FILE"))
                    .build()))
            .thenReturn(List.of(
                    aDownloadableFile().withProjectPath("./path/1/").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    aDownloadableFile().withProjectPath("./path/2/").withUrl("https://s3.simplelocalize.io/file2.xml").build()
            ));

    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    aDownloadableFile().withProjectPath("./path/1/").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    "./my-project-path");
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    aDownloadableFile().withProjectPath("./path/2/").withUrl("https://s3.simplelocalize.io/file2.xml").build(),
                    "./my-project-path");
  }
}
