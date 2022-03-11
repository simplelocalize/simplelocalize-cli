package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.aDownloadRequest;
import static io.simplelocalize.cli.client.dto.UploadRequest.UploadFileRequestBuilder.anUploadFileRequest;


/**
 * Tests are disabled due to fact they are using production server.
 * Feel free to remove @Disabled annotation to test CLI against your production project
 */
@Disabled
class SimpleLocalizeClientIT
{

  @Test
  void shouldSendKeys() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("237b305f6b2273e92ac857eb44d7f33b");
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration);

    //when
    client.sendKeys(List.of("test"));

    //then
  }

  @Test
  void shouldUploadFile() throws Exception
  {
    //given
    String projectApiKey = "81707741b64e68427e1a2c20e75095b1";
    Configuration configuration = new Configuration();
    configuration.setApiKey(projectApiKey);
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration);

    UploadRequest uploadRequest = anUploadFileRequest()
            .withPath(Path.of("./test.json"))
            .withLanguageKey(null)
            .withFormat("multi-language-json")
            .withOptions(Collections.emptyList())
            .withRelativePath("")
            .build();

    //when
    client.uploadFile(uploadRequest);

    //then
  }

  @Test
  void shouldDownloadFileToDirectory() throws Exception
  {
    //given
    String projectApiKey = "96a7b6ca75c79d4af4dfd5db2946fdd4";
    Configuration configuration = new Configuration();
    configuration.setApiKey(projectApiKey);
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration);

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./i18n")
            .withFormat("java-properties")
            .withLanguageKey("")
            .withOptions(Collections.emptyList())
            .build();

    //when
    client.downloadFile(downloadRequest);

    //then
  }

  @Test
  void shouldDownloadFileToFile() throws Exception
  {
    //given
    String projectApiKey = "96a7b6ca75c79d4af4dfd5db2946fdd4";
    Configuration configuration = new Configuration();
    configuration.setApiKey(projectApiKey);
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration);

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./messages_test.properties")
            .withFormat("java-properties")
            .withLanguageKey("en")
            .withOptions(Collections.emptyList())
            .build();

    //when
    client.downloadFile(downloadRequest);

    //then
  }
}
