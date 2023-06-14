package io.simplelocalize.cli;

import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockitoExtension.class)
class SimplelocalizeCliCommandTest
{

  private final static String MOCK_SERVER_BASE_URL = "http://localhost:1080";
  private static ClientAndServer mockServer;
  @InjectMocks
  private SimplelocalizeCliCommand sut;

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
  void main()
  {
  }

  @Test
  void extract()
  {
    // given & when & then
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v1/keys")
                            .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK', data: { uniqueKeysProcessed: 1, processedWithWarnings: false } }")
                            .withDelay(TimeUnit.MILLISECONDS, 200));

    sut.extract("my-api-key", "yahoo/react-intl", "./", MOCK_SERVER_BASE_URL);
  }

  @Test
  void sync() throws IOException
  {

    // given & when & then
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v2/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "SPLIT_BY_NAMESPACES")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"namespace\": \"my-file\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    sut.sync(
            "my-api-key",
            "./junit/mock-server/test.json",
            "multi-language-json",
            List.of(),
            "./junit/mock-server/test.json",
            "java-properties",
            List.of("SPLIT_BY_NAMESPACES"),
            null,
            null,
            MOCK_SERVER_BASE_URL
    );
  }

  @Test
  void upload() throws IOException
  {
    // given & when & then
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v2/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("uploadOptions", "SPLIT_BY_NAMESPACES")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    sut.upload(
            "my-api-key",
            "./junit/mock-server/test.json",
            "multi-language-json",
            false,
            false,
            false,
            List.of("SPLIT_BY_NAMESPACES"),
            null,
            null,
            MOCK_SERVER_BASE_URL
    );
  }

  @Test
  void download()
  {
    // given & when & then
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "SPLIT_BY_NAMESPACES")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"namespace\": \"my-file\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    sut.download(
            "my-api-key",
            "./junit/mock-server/test.json",
            "java-properties",
            List.of("SPLIT_BY_NAMESPACES"),
            null,
            null,
            MOCK_SERVER_BASE_URL
    );
  }

  @Test
  void pull() throws IOException
  {
    // given & when & then
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v1/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );


    sut.pull("my-api-key", "./my-path", "latest", null, MOCK_SERVER_BASE_URL);

  }

  @Test
  void status() throws IOException
  {
    // given & when & then
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v1/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );


    sut.status("my-api-key", MOCK_SERVER_BASE_URL);

  }

  @Test
  void publishLatest() throws IOException
  {
    // given & when & then
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v1/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/api/v1/translations/publish")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{}")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );


    sut.publish("my-api-key", "latest", MOCK_SERVER_BASE_URL);
  }

  @Test
  void publishProduction() throws IOException
  {
    // given & when & then
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v1/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/api/v1/translations/deploy")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key")
                            .withQueryStringParameter("sourceEnvironment", "_latest")
                            .withQueryStringParameter("targetEnvironment", "_production")
                    ,
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{}")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );


    sut.publish("my-api-key", "production", MOCK_SERVER_BASE_URL);
  }

  @Test
  void startAutoTranslation()
  {
    // given & when & then
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/jobs")
                            .withQueryStringParameter("status", "RUNNING")
                            .withQueryStringParameter("type", "AUTO_TRANSLATION")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(2))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("""
                                    {
                                      "msg": "OK",
                                      "status": 200,
                                      "data": []
                                    }
                                    """)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/api/v2/jobs/auto-translate")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key")
                    ,
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("""
                                    {
                                      "languageKeys:" ["pl", "en"],
                                      "source": "CLI"
                                    }
                                    """)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    sut.startAutoTranslation("my-api-key", List.of("pl", "en"), MOCK_SERVER_BASE_URL);
  }

  @Test
  void init()
  {
    // given & when & then
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("sample.yml"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("""
                                    # SimpleLocalize configuration file
                                    # More info: https://simplelocalize.io/docs/cli/configuration
                                    """)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    sut.init();
  }

}
