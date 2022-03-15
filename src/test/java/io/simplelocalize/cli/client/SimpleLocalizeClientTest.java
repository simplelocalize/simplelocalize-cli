package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.exception.ApiRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.MediaType;
import org.mockserver.model.StringBody;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.aDownloadRequest;
import static io.simplelocalize.cli.client.dto.UploadRequest.UploadFileRequestBuilder.anUploadFileRequest;
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
                            .withBody("{ 'msg': 'OK', data: { uniqueKeysProcessed: 1, processedWithWarnings: false } }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "237b305f6b2273e92ac857eb44d7f33b");

    //when
    client.uploadKeys(givenKeys);

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
                            .withHeader("X-SimpleLocalize-Token", "81707741b64e68427e1a2c20e75095b1"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = anUploadFileRequest()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey(null)
            .withFormat("multi-language-json")
            .withOptions(List.of("MULTI_FILE"))
            .build();

    //when
    client.uploadFile(uploadRequest);

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
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = anUploadFileRequest()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey("en")
            .withFormat("multi-language-json")
            .withOptions(List.of())
            .build();

    //when
    client.uploadFile(uploadRequest);

    //then
  }

  @Test
  void shouldLogApiErrorMessageWhenUploadFileFailed() throws Exception
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
                            .withStatusCode(500)
                            .withBody("{ 'msg': 'failure message' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    UploadRequest uploadRequest = anUploadFileRequest()
            .withPath(Path.of("./junit/mock-server/test.json"))
            .withLanguageKey("en")
            .withFormat("multi-language-json")
            .withOptions(List.of())
            .build();

    //when & then
    Assertions
            .assertThatThrownBy(() -> client.uploadFile(uploadRequest))
            .isInstanceOf(ApiRequestException.class)
            .hasMessage("failure message");
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
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./i18n")
            .withFormat("java-properties")
            .withLanguageKey("")
            .withOptions(List.of())
            .build();

    //when
    client.downloadFile(downloadRequest);

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
                            .withBody("{ 'msg': 'OK' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./messages_test.properties")
            .withFormat("java-properties")
            .withLanguageKey("en")
            .withOptions(List.of())
            .build();

    //when
    client.downloadFile(downloadRequest);

    //then
  }


  @Test
  void shouldThrowExceptionOnHttpStatusError() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v1/download")
                            .withQueryStringParameter("downloadFormat", "unknown-format")
                            .withQueryStringParameter("languageKey", "en")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(400)
                            .withBody("{ 'msg': 'not ok' }")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./messages_test.properties")
            .withFormat("unknown-format")
            .withLanguageKey("en")
            .withOptions(List.of())
            .build();

    //when & then
    Assertions
            .assertThatThrownBy(() -> client.downloadFile(downloadRequest))
            .isInstanceOf(ApiRequestException.class)
            .hasMessage("not ok");
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

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./")
            .withFormat("java-properties")
            .withOptions(List.of("MULTI_FILE"))
            .build();

    //when
    client.fetchDownloadableFiles(downloadRequest);

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

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath("./")
            .withFormat("java-properties")
            .withOptions(List.of("MULTI_FILE", "USE_NESTED_JSON"))
            .build();


    //when
    client.fetchDownloadableFiles(downloadRequest);

    //then
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

    DownloadableFile downloadableFile = new DownloadableFile();
    downloadableFile.setUrl(MOCK_SERVER_BASE_URL + "/s3/file");
    downloadableFile.setProjectPath("./");
    String downloadPath = "file.json";

    //when
    client.downloadFile(downloadableFile, downloadPath);

    //then
  }

  @Test
  void shouldValidateQuality() throws Exception
  {
    //given
    SimpleLocalizeClient client = new SimpleLocalizeClient(MOCK_SERVER_BASE_URL, "96a7b6ca75c79d4af4dfd5db2946fdd4");
    mockServer.when(request()
                            .withMethod("GET")
                            .withPath("/cli/v1/validate/gate")
                            .withHeader("X-SimpleLocalize-Token", "96a7b6ca75c79d4af4dfd5db2946fdd4"),
                    Times.exactly(1))
            .respond(
                    response()
                            .withStatusCode(200)
                            .withBody("{'data':{'passed': true, 'message': 'ok', 'status': 200}}")
                            .withDelay(TimeUnit.MILLISECONDS, 200)
            );

    //when
    int validateGate = client.validateGate();

    //then
    Assertions.assertThat(validateGate).isEqualTo(200);
  }
}
