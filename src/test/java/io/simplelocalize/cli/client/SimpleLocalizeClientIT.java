package io.simplelocalize.cli.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;


/**
 * Tests are disabled due to fact they are using production server.
 * Feel free to remove @Disabled annotation to test CLI against your production project
 */
@Disabled
public class SimpleLocalizeClientIT
{

  @Test
  public void shouldSendKeys() throws Exception
  {
    //given
    String projectApiKey = "237b305f6b2273e92ac857eb44d7f33b";
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(projectApiKey, "default");

    //when
    client.sendKeys(List.of("test"));

    //then
  }

  @Test
  public void shouldUploadFile() throws Exception
  {
    //given
    String projectApiKey = "81707741b64e68427e1a2c20e75095b1";
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(projectApiKey, "default");

    //when
    client.uploadFile(Path.of("./test.json"), null, "multi-language-json", "");

    //then
  }

  @Test
  public void shouldDownloadFileToDirectory() throws Exception
  {
    //given
    String projectApiKey = "96a7b6ca75c79d4af4dfd5db2946fdd4";
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(projectApiKey, "default");

    //when
    client.downloadFile(Path.of("./i18n"), "java-properties", "");

    //then
  }

  @Test
  public void shouldDownloadFileToFile() throws Exception
  {
    //given
    String projectApiKey = "96a7b6ca75c79d4af4dfd5db2946fdd4";
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(projectApiKey, "default");

    //when
    client.downloadFile(Path.of("./messages_test.properties"), "java-properties", "en");

    //then
  }
}
