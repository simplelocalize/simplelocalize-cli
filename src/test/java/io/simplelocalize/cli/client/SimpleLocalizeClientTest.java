package io.simplelocalize.cli.client;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.dto.ExportRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import io.simplelocalize.cli.exception.ApiRequestException;
import io.simplelocalize.cli.util.TestLogEventFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.MediaType;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
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
  void shouldUploadFileWithLanguageKey() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1");
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v2/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("uploadOptions", "SPLIT_BY_NAMESPACES")
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = UploadRequest.builder()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey(null)
            .withFormat("multi-language-json")
            .withOptions(List.of("SPLIT_BY_NAMESPACES"))
            .build();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(client.getClass());

    //when
    client.uploadFile(uploadRequest);

    //then
    logEventList.forEach(logEvent -> assertThat(logEvent.getFormattedMessage()).isEqualTo("File uploaded: test.json"));
  }

  @Test
  void shouldUploadFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1");
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v2/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("languageKey", "en")
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = UploadRequest.builder()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey("en")
            .withFormat("multi-language-json")
            .withOptions(List.of())
            .build();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(client.getClass());


    //when
    client.uploadFile(uploadRequest);

    //then
    logEventList.forEach(logEvent -> assertThat(logEvent.getFormattedMessage()).isEqualTo("File uploaded: test.json"));
  }

  @Test
  void shouldLogApiErrorMessageWhenUploadFileFailed() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "81707741b64e68427e1a2c20e75095b1");
    mockServer.when(request()
                            .withMethod("POST")
                            .withPath("/cli/v2/upload")
                            .withQueryStringParameter("uploadFormat", "multi-language-json")
                            .withQueryStringParameter("languageKey", "en")
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(500)
                            .withBody("{ 'msg': 'failure message' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = UploadRequest.builder()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey("en")
            .withFormat("multi-language-json")
            .withOptions(List.of())
            .build();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(client.getClass());

    //when & then
    Assertions
            .assertThatThrownBy(() -> client.uploadFile(uploadRequest))
            .isInstanceOf(ApiRequestException.class)
            .hasMessage("failure message");

    logEventList.forEach(logEvent -> assertThat(logEvent.getFormattedMessage()).isEqualTo("Request failed: failure message"));
  }

  @Test
  void shouldGetDownloadableFiles() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "SPLIT_BY_NAMESPACES")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"namespace\": \"my-file\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    ExportRequest exportRequest = ExportRequest.builder()
            .withFormat("java-properties")
            .withOptions(List.of("SPLIT_BY_NAMESPACES"))
            .build();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(client.getClass());

    //when
    client.exportFiles(exportRequest);

    //then
    logEventList.forEach(logEvent -> assertThat(logEvent.getFormattedMessage()).isEqualTo("Downloadable files fetched: 1"));
  }

  @Test
  void shouldDownloadWithManyOptions() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v2/download")
                            .withQueryStringParameter("downloadOptions", "SPLIT_BY_NAMESPACES,USE_NESTED_JSON")
                            .withQueryStringParameter("downloadFormat", "java-properties")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ \"files\": [{\"namespace\": \"my-file\", \"url\": \"https://simplelocalize.io\"}] }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    ExportRequest exportRequest = ExportRequest.builder()
            .withFormat("java-properties")
            .withOptions(List.of("SPLIT_BY_NAMESPACES", "USE_NESTED_JSON"))
            .build();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(client.getClass());

    //when
    client.exportFiles(exportRequest);

    //then
    logEventList.forEach(logEvent -> assertThat(logEvent.getFormattedMessage()).isEqualTo("Downloadable files fetched: 1"));
  }


  @Test
  void shouldDownloadS3File() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/s3/file"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withContentType(MediaType.APPLICATION_JSON_UTF_8)
                            .withBody("{}".getBytes(StandardCharsets.UTF_8))
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    DownloadableFile downloadableFile = new DownloadableFile(MOCK_SERVER_BASE_URL + "/s3/file", "common", null, null, null, null);
    String downloadPath = "./junit/download-test/file.json";

    //when
    client.downloadFile(downloadableFile, downloadPath);

    //then
    assertThat(Path.of(downloadPath)).isRegularFile();
  }

  @Test
  void shouldDownloadAndTruncateBeforeWriting() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/s3/file"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withContentType(MediaType.APPLICATION_JSON_UTF_8)
                            .withBody("sample".getBytes(StandardCharsets.UTF_8))
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    DownloadableFile downloadableFile = new DownloadableFile(MOCK_SERVER_BASE_URL + "/s3/file", "common", null, null, null, null);
    String downloadPath = "./junit/truncate/file.json";

    //when
    client.downloadFile(downloadableFile, downloadPath);

    //then
    assertThat(Path.of(downloadPath)).hasContent("sample").isRegularFile();
  }
}
