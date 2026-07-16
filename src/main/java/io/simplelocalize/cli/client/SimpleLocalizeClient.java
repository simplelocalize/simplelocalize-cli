package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.simplelocalize.cli.client.dto.ExportRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import io.simplelocalize.cli.client.dto.proxy.ExportResponse;
import io.simplelocalize.cli.exception.ApiRequestException;
import io.simplelocalize.cli.exception.ConfigurationException;
import io.simplelocalize.cli.util.StringUtils;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.simplelocalize.cli.TemplateKeys.*;

public class SimpleLocalizeClient
{

  private static final String ERROR_MESSAGE_PATH = "$.msg";
  private static final int MAX_DOWNLOAD_ATTEMPTS = 3;
  private static final long RETRY_DELAY_MILLIS = 1000;
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
    this.objectMapper = ObjectMapperSingleton.getInstance();
    this.httpClient = HttpClientFactory.createHttpClient();
  }

  public static SimpleLocalizeClient create(String baseUrl, String apiKey)
  {
    final String effectiveApiKey = StringUtils.isNotEmpty(apiKey) ? apiKey : loadApiKeyFromEnvironmentVariable();
    if (StringUtils.isBlank(effectiveApiKey))
    {
      throw new ConfigurationException("API key is required");
    }

    return new SimpleLocalizeClient(baseUrl, effectiveApiKey);
  }

  public static String loadApiKeyFromEnvironmentVariable()
  {
    return System.getenv("SIMPLELOCALIZE_API_KEY");
  }

  public void uploadFile(UploadRequest uploadRequest) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildUploadUri(uploadRequest, false);
    HttpRequest httpRequest = httpRequestFactory.createUploadFileRequest(uri, uploadRequest);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  public String previewFile(UploadRequest uploadRequest) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildUploadUri(uploadRequest, true);
    HttpRequest httpRequest = httpRequestFactory.createUploadFileRequest(uri, uploadRequest);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    DocumentContext json = JsonPath.parse(httpResponse.body());
    return prettyPrintJson(json);
  }

  private String prettyPrintJson(DocumentContext json) throws JsonProcessingException
  {
    Object data = json.read("$.data");
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(data);
  }

  public List<DownloadableFile> exportFiles(ExportRequest exportRequest) throws IOException, InterruptedException
  {
    URI downloadUri = uriFactory.buildDownloadUri(exportRequest);
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(downloadUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    String body = httpResponse.body();
    ExportResponse exportResponse = objectMapper.readValue(body, ExportResponse.class);
    return exportResponse.files();
  }

  public void downloadFile(DownloadableFile downloadableFile, String downloadPathTemplate) throws IOException, InterruptedException
  {
    Optional<DownloadableFile> optionalDownloadableFile = Optional.of(downloadableFile);
    String downloadPath = downloadPathTemplate
            .replace(NAMESPACE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::namespace).orElse(""))
            .replace(LANGUAGE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::language).orElse(""))
            .replace(CUSTOMER_KEY_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::customer).orElse(""))
            .replace(TRANSLATION_KEY_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::translationKey).orElse(""))
            .replace(REMOTE_PATH, optionalDownloadableFile.map(DownloadableFile::remotePath).orElse(""));
    String url = downloadableFile.url();
    Path savePath = Path.of(downloadPath);
    downloadFile(url, savePath);
  }

  public void downloadFile(String url, Path savePath) throws IOException, InterruptedException
  {
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(URI.create(url)).build();
    Path parentDirectory = savePath.getParent();
    if (parentDirectory != null)
    {
      Files.createDirectories(parentDirectory);
    }
    log.info("Downloading {}", savePath);
    for (int attempt = 1; ; attempt++)
    {
      HttpResponse<Path> response;
      try
      {
        response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(savePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
      } catch (IOException e)
      {
        if (attempt >= MAX_DOWNLOAD_ATTEMPTS)
        {
          throw e;
        }
        log.warn("Downloading {} failed: {}, retrying ({}/{})", savePath, e.getMessage(), attempt + 1, MAX_DOWNLOAD_ATTEMPTS);
        Thread.sleep(RETRY_DELAY_MILLIS * attempt);
        continue;
      }
      if (response.statusCode() == 200)
      {
        return;
      }
      // the body handler wrote the error response to savePath; don't leave it there as a translation file
      try
      {
        if (isRetryableStatusCode(response.statusCode()) && attempt < MAX_DOWNLOAD_ATTEMPTS)
        {
          log.warn("Downloading {} failed with HTTP {}, retrying ({}/{})", savePath, response.statusCode(), attempt + 1, MAX_DOWNLOAD_ATTEMPTS);
        } else
        {
          throwOnError(response);
        }
      } finally
      {
        deleteQuietly(savePath);
      }
      Thread.sleep(RETRY_DELAY_MILLIS * attempt);
    }
  }

  private boolean isRetryableStatusCode(int statusCode)
  {
    return statusCode == 429 || statusCode >= 500;
  }

  private void deleteQuietly(Path path)
  {
    try
    {
      Files.deleteIfExists(path);
    } catch (IOException e)
    {
      log.warn("Unable to remove incomplete file: {}", path);
    }
  }

  public String fetchProject() throws IOException, InterruptedException
  {
    URI getProjectUri = uriFactory.buildGetProjectUri();
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(getProjectUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    return httpResponse.body();
  }

  public String getAutoTranslationJobs() throws IOException, InterruptedException
  {
    URI getProjectUri = uriFactory.buildGetRunningAutoTranslationJobsUri();
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(getProjectUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    return httpResponse.body();
  }

  public void startAutoTranslation(List<String> languageKeys, List<String> options) throws IOException, InterruptedException
  {
    URI startAutoTranslationUri = uriFactory.buildStartAutoTranslationUri();
    HttpRequest httpRequest = httpRequestFactory.createBaseRequest(startAutoTranslationUri)
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(ClientBodyBuilders.ofStartAutoTranslation(languageKeys, options)).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  public void publish(String environment) throws IOException, InterruptedException
  {
    URI publishUri = uriFactory.buildPublishUri(environment);
    HttpRequest httpRequest = httpRequestFactory.createBaseRequest(publishUri).POST(HttpRequest.BodyPublishers.noBody()).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  public void purgeTranslations() throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildPurgeTranslations();
    HttpRequest httpRequest = httpRequestFactory.createDeleteRequest(uri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }


  public void sendException(Configuration configuration, Exception exception) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildStacktraceUri();
    HttpRequest httpRequest = httpRequestFactory.createBaseRequest(uri)
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(ClientBodyBuilders.ofException(configuration, exception)).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  private void throwOnError(HttpResponse<?> httpResponse)
  {
    if (httpResponse.statusCode() != 200)
    {
      String message = tryReadErrorMessage(httpResponse);
      if (StringUtils.isBlank(message))
      {
        message = "Unknown error, HTTP Status: " + httpResponse.statusCode();
      }
      log.error("Request failed: {}", message);
      throw new ApiRequestException(message);
    }
  }

  private String tryReadErrorMessage(HttpResponse<?> httpResponse)
  {
    String stringBody = safeCastHttpBodyToString(httpResponse.body());
    if (StringUtils.isBlank(stringBody))
    {
      return null;
    }
    try
    {
      com.jayway.jsonpath.Configuration parseContext = com.jayway.jsonpath.Configuration
              .defaultConfiguration()
              .addOptions(Option.SUPPRESS_EXCEPTIONS);
      Object message = JsonPath.using(parseContext).parse(stringBody).read(ERROR_MESSAGE_PATH);
      return message != null ? String.valueOf(message) : null;
    } catch (Exception e)
    {
      log.debug("Unable to extract error message from response body", e);
      return null;
    }
  }

  private String safeCastHttpBodyToString(Object responseBody)
  {
    if (responseBody instanceof byte[] responseBodyBytes)
    {
      return new String(responseBodyBytes);
    } else if (responseBody instanceof String responseBodyString)
    {
      return responseBodyString;
    } else if (responseBody instanceof Path responseBodyPath)
    {
      try
      {
        return Files.readString(responseBodyPath);
      } catch (IOException e)
      {
        return "";
      }
    }
    return "";
  }
}
