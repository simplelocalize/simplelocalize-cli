package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.builder;

@ExtendWith(MockitoExtension.class)
public class DownloadCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");

  @Test
  public void shouldDownloadMultipleFiles() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setDownloadPath("./my-project-path");
    configuration.setLanguageKey("en");
    configuration.setDownloadOptions(List.of("SPLIT_BY_NAMESPACES"));
    configuration.setDownloadFormat("android");

    //when
    Mockito.when(client.fetchDownloadableFiles(builder()
                    .withFormat("android")
                    .withLanguageKey("en")
                    .withOptions(List.of("SPLIT_BY_NAMESPACES"))
                    .build()))
            .thenReturn(List.of(
                    DownloadableFile.DownloadableFileBuilder.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    DownloadableFile.DownloadableFileBuilder.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file2.xml").build()
            ));

    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.DownloadableFileBuilder.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file1.xml").build(),
                    "./my-project-path");
    Mockito.verify(client, Mockito.times(1))
            .downloadFile(
                    DownloadableFile.DownloadableFileBuilder.builder().withNamespace("common").withUrl("https://s3.simplelocalize.io/file2.xml").build(),
                    "./my-project-path");
  }
}
