package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.StringBody;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class SimpleLocalizeClientTest
{
  private final static String MOCK_SERVER_BASE_URL = "http://localhost:1080";

  private static ClientAndServer mockServer;

  @BeforeAll
  public static void startServer()
  {
    mockServer = startClientAndServer(1080);
  }

  @AfterAll
  public static void stopServer()
  {
    mockServer.stop();
  }

  @Test
  void shouldSendKeys() throws Exception
  {
    //given
    List<String> givenKeys = List.of("test");

    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v1/keys")
                            .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .withHeader("X-SimpleLocalize-Token", "237b305f6b2273e92ac857eb44d7f33b")
                            .withBody(StringBody.exact("{\"content\":[{\"key\":\"test\"}]}")),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK', data: { uniqueKeysProcessed: 1, processedWithWarnings: false } }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "237b305f6b2273e92ac857eb44d7f33b");

    //when
    client.sendKeys(givenKeys);

    //then
  }

  @Test
  void shouldUploadFileWithLanguageKey() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1");
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v1/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("importOptions", "MULTI_FILE")
                            .withQueryStringParameter("projectPath", "./my-path/my-file/test.json")
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    //when
    client.uploadFile(Path.of("./junit/mock-server/test.json"), null, "multi-language-json", List.of("MULTI_FILE"), "./my-path/my-file/test.json");

    //then
  }

  @Test
  void shouldUploadFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1");
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v1/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("languageKey", "en")
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    //when
    client.uploadFile(Path.of("./junit/mock-server/test.json"), "en", "multi-language-json", List.of(), null);

    //then
  }

  @Test
  void shouldDownloadFileToDirectory() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v1/download")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    //when
    client.downloadFile("./i18n", "java-properties", "", List.of());

    //then
  }

  @Test
  void shouldDownloadFileToFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v1/download")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withQueryStringParameter("languageKey", "en")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ msg: 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );
    //when
    client.downloadFile("./messages_test.properties", "java-properties", "en", List.of());

    //then
  }

  @Test
  void shouldDownloadMultiFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "MULTI_FILE")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"projectPath\": \"./junit/mock-server/my-file.properties\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );
    //when
    client.downloadMultiFile("./", "java-properties", List.of("MULTI_FILE"));

    //then
  }

  @Test
  void shouldDownloadWithManyOptions() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "MULTI_FILE,USE_NESTED_JSON")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"projectPath\": \"./junit/mock-server/my-file.properties\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    List<String> options = new ArrayList<>();
    options.add("MULTI_FILE");
    options.add("USE_NESTED_JSON");

    //when
    client.downloadMultiFile("./", "java-properties", options);

    //then
  }
}
