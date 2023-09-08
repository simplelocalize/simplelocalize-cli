package io.simplelocalize.cli;

import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockitoExtension.class)
class SimplelocalizeCliCommandTest
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
  void extract()
  {
    // given
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

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"extract",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--projectType", "yahoo/react-intl",
            "--searchDir", "./",
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void sync()
  {

    // given
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


    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"sync",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--uploadPath", "./junit/mock-server/test.json",
            "--uploadFormat", "multi-language-json",
            "--downloadPath", "./junit/mock-server/test.json",
            "--downloadFormat", "java-properties",
            "--downloadOptions", "SPLIT_BY_NAMESPACES"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void upload()
  {
    // given
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

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"upload",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--uploadPath", "./junit/mock-server/test.json",
            "--uploadFormat", "multi-language-json",
            "--uploadOptions", "SPLIT_BY_NAMESPACES"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void download()
  {
    // given
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
    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"download",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--downloadPath", "./junit/mock-server/test.json",
            "--downloadFormat", "java-properties",
            "--downloadOptions", "SPLIT_BY_NAMESPACES"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void pull() throws IOException
  {
    // given
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"pull",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--pullPath", "./my-path",
            "--environment", "latest",
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void status() throws IOException
  {
    // given
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"status", "--apiKey", "my-api-key", "--baseUrl", MOCK_SERVER_BASE_URL};
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void purge() throws IOException
  {
    // given
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/project")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody(content)
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    mockServer.when(request()
                            .withMethod("DELETE")
                            .withPath("/api/v1/translations/purge")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
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
    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"purge",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--force"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void publishLatest() throws IOException
  {
    // given
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/project")
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
                            .withPath("/api/v2/environments/_latest/publish")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{}")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"publish",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--environment", "_latest"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void publishProduction() throws IOException
  {
    // given
    String path = SimplelocalizeCliCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project-empty-hosting-resources.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/api/v2/project")
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
                            .withPath("/api/v2/environments/_production/publish")
                            .withHeader("X-SimpleLocalize-Token", "my-api-key"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{}")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );
    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"publish",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--environment", "_production"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void startAutoTranslation()
  {
    // given
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
    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"auto-translate",
            "--apiKey", "my-api-key",
            "--baseUrl", MOCK_SERVER_BASE_URL,
            "--languageKeys", "pl,en"
    };
    commandLine.parseArgs(args);

    //when
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
  }

  @Test
  void init()
  {
    // given
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

    SimplelocalizeCliCommand app = new SimplelocalizeCliCommand();
    CommandLine commandLine = new CommandLine(app);
    String[] args = {"init"};
    commandLine.parseArgs(args);

    //then
    int execute = commandLine.execute(args);

    //then
    assertEquals(0, execute);
    assertTrue(Files.exists(Path.of("simplelocalize.yml")));
  }

}
