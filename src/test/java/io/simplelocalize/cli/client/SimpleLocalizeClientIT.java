package io.simplelocalize.cli.client;

import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;


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

    //when
    client.uploadFile(Path.of("./test.json"), null, "multi-language-json", Collections.emptyList(), "");

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

    //when
    client.downloadFile("./i18n", "java-properties", "", Collections.emptyList());

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

    //when
    client.downloadFile("./messages_test.properties", "java-properties", "en", Collections.emptyList());

    //then
  }
}
