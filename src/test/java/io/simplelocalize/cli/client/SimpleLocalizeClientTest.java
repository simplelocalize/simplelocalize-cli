package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;


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
    mockServer.when(request()
                    .withMethod("POST")
                    .withPath("/cli/v1/keys")
                    .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .withHeader("X-SimpleLocalize-Token", "237b305f6b2273e92ac857eb44d7f33b")
                    .withBody(exact("{ content: [ { 'key': 'test' } ] }")),
            Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK', data: { uniqueKeysProcessed: 1, processedWithWarnings: false } }")
                            .withDelay(TimeUnit.SECONDS, 10)
            );

    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "237b305f6b2273e92ac857eb44d7f33b", "default");

    //when
    client.sendKeys(List.of("test"));

    //then
  }

  @Test
  @Disabled
  public void shouldUploadFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1", "default");

    //when
    client.uploadFile(Path.of("./test.json"), null, "multi-language-json", "");

    //then
  }

  @Test
  @Disabled
  public void shouldDownloadFileToDirectory() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4", "default");

    //when
    client.downloadFile(Path.of("./i18n"), "java-properties", "");

    //then
  }

  @Test
  @Disabled
  public void shouldDownloadFileToFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4", "default");

    //when
    client.downloadFile(Path.of("./messages_test.properties"), "java-properties", "en");

    //then
  }
}
