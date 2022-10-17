package io.simplelocalize.cli.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.client.dto.ExportResponse;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.exception.ApiRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.simplelocalize.cli.TemplateKeys.LANGUAGE_TEMPLATE_KEY;
import static io.simplelocalize.cli.TemplateKeys.NAMESPACE_TEMPLATE_KEY;

public class SimpleLocalizeClient
{

  private static final String ERROR_MESSAGE_PATH = "$.msg";
  private final HttpClient httpClient;
  private final SimpleLocalizeHttpRequestFactory httpRequestFactory;
  private final SimpleLocalizeUriFactory uriFactory;

  private final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);
  private final ObjectMapper objectMapper;

  public SimpleLocalizeClient(String baseUrl, String apiKey)
  {

    Objects.requireNonNull(baseUrl);
    Objects.requireNonNull(apiKey);
    this.uriFactory = new SimpleLocalizeUriFactory(baseUrl);
    this.httpRequestFactory = new SimpleLocalizeHttpRequestFactory(apiKey);
    this.objectMapper = new ObjectMapper();
    this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(5))
            .build();
  }

  public static SimpleLocalizeClient create(String baseUrl, String apiKey)
  {
    return new SimpleLocalizeClient(baseUrl, apiKey);
  }

  public void uploadKeys(Collection<String> keys) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildSendKeysURI();
    HttpRequest httpRequest = httpRequestFactory.createSendKeysRequest(uri, keys);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    int keysProcessed = JsonPath.read(httpResponse.body(), "$.data.uniqueKeysProcessed");
    log.info("Successfully uploaded {} keys", keysProcessed);
  }

  public void uploadFile(UploadRequest uploadRequest) throws IOException, InterruptedException
  {
    Path uploadPath = uploadRequest.getPath();
    log.info("Uploading {}", uploadPath);
    URI uri = uriFactory.buildUploadUri(uploadRequest);
    HttpRequest httpRequest = httpRequestFactory.createUploadFileRequest(uri, uploadRequest);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  public List<DownloadableFile> fetchDownloadableFiles(DownloadRequest downloadRequest) throws IOException, InterruptedException
  {
    log.info("Preparing files to download");
    URI downloadUri = uriFactory.buildDownloadUri(downloadRequest);
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(downloadUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    String body = httpResponse.body();
    ExportResponse exportResponse = objectMapper.readValue(body, ExportResponse.class);
    return exportResponse.getFiles();
  }

  public void downloadFile(DownloadableFile downloadableFile, String downloadPathTemplate)
  {
    Optional<DownloadableFile> optionalDownloadableFile = Optional.of(downloadableFile);
    String downloadPath = downloadPathTemplate
            .replace(NAMESPACE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::getNamespace).orElse(""))
            .replace(LANGUAGE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::getLanguage).orElse(""));
    String url = downloadableFile.getUrl();
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(URI.create(url)).build();
    Path savePath = Path.of(downloadPath);
    try
    {
      Path parentDirectory = savePath.getParent();
      if (parentDirectory != null)
      {
        Files.createDirectories(parentDirectory);
      }
      log.info("Downloading {}", savePath);
      httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(savePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
    } catch (IOException e)
    {
      log.error("Download failed: {}", savePath, e);
    } catch (InterruptedException e)
    {
      log.error("Download interrupted: {}", savePath, e);
      Thread.currentThread().interrupt();
    }
  }

  public int validateGate() throws IOException, InterruptedException
  {
    URI validateUri = uriFactory.buildValidateGateUri();
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(validateUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    String json = httpResponse.body();
    Boolean passed = JsonPath.read(json, "$.data.passed");
    String message = JsonPath.read(json, "$.data.message");
    int status = JsonPath.read(json, "$.data.status");
    log.info("Gate result: {} (status: {}, message: {})", passed, status, message);
    return status;
  }

  private void throwOnError(HttpResponse<?> httpResponse)
  {
    if (httpResponse.statusCode() != 200)
    {
      com.jayway.jsonpath.Configuration parseContext = com.jayway.jsonpath.Configuration
              .defaultConfiguration()
              .addOptions(Option.SUPPRESS_EXCEPTIONS);

      Object responseBody = httpResponse.body();
      String stringBody = safeCastHttpBodyToString(responseBody);
      String message = JsonPath.using(parseContext).parse(stringBody).read(ERROR_MESSAGE_PATH);
      if (message == null)
      {
        message = "Unknown error, HTTP Status: " + httpResponse.statusCode();
      }
      log.error("Request failed: {}", message);
      throw new ApiRequestException(message, httpResponse);
    }
  }

  private String safeCastHttpBodyToString(Object responseBody)
  {
    if (responseBody instanceof byte[])
    {
      return new String((byte[]) responseBody);
    } else if (responseBody instanceof String)
    {
      return (String) responseBody;
    }
    return "";
  }


}
