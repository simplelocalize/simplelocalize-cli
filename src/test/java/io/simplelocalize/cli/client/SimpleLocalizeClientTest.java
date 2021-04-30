package io.simplelocalize.cli.client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.nio.file.Path;
import java.util.List;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;


@Disabled
public class SimpleLocalizeClientTest
{
  private final static String MOCK_SERVER_BASE_URL = "http://localhost:1080";

  private ClientAndServer mockServer;

  @BeforeAll
  public void startServer()
  {
    mockServer = startClientAndServer(1080);
  }

  @AfterAll
  public void stopServer()
  {
    mockServer.stop();
  }

  @Test
  public void shouldSendKeys() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "237b305f6b2273e92ac857eb44d7f33b", "default");

    //when
    client.sendKeys(List.of("test"));

    //then
  }

  @Test
  public void shouldUploadFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1", "default");

    //when
    client.uploadFile(Path.of("./test.json"), null, "multi-language-json", "");

    //then
  }

  @Test
  public void shouldDownloadFileToDirectory() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4", "default");

    //when
    client.downloadFile(Path.of("./i18n"), "java-properties", "");

    //then
  }

  @Test
  public void shouldDownloadFileToFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4", "default");

    //when
    client.downloadFile(Path.of("./messages_test.properties"), "java-properties", "en");

    //then
  }
}
