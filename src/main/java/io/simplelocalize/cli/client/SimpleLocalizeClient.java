package io.simplelocalize.cli.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.simplelocalize.cli.client.dto.*;
import io.simplelocalize.cli.exception.ApiRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
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
    HttpClient.Builder builder = HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofMinutes(5));

    String httpProxyValue = System.getenv("http_proxy");
    ProxyConfiguration proxyConfigOptional = SystemProxySelector.getHttpProxyValueOrNull(httpProxyValue);
    if (proxyConfigOptional != null)
    {
      log.info("Using proxy: {}", proxyConfigOptional);
      String host = proxyConfigOptional.getHost();
      Integer port = proxyConfigOptional.getPort();
      InetSocketAddress proxyAddress = new InetSocketAddress(host, port);
      builder.proxy(ProxySelector.of(proxyAddress));

      String proxyUsername = proxyConfigOptional.getUsername();
      String proxyPassword = proxyConfigOptional.getPassword();
      boolean hasAuthentication = proxyUsername != null && proxyPassword != null;
      if (hasAuthentication)
      {
        Authenticator authenticator = new Authenticator()
        {
          @Override
          protected PasswordAuthentication getPasswordAuthentication()
          {
            return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
          }
        };
        builder.authenticator(authenticator);
      }
    }

    this.httpClient = builder.build();
  }

  public static SimpleLocalizeClient create(String baseUrl, String apiKey)
  {
    return new SimpleLocalizeClient(baseUrl, apiKey);
  }

  public Integer uploadKeys(Collection<String> keys) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildSendKeysURI();
    HttpRequest httpRequest = httpRequestFactory.createSendKeysRequest(uri, keys);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    return JsonPath.read(httpResponse.body(), "$.data.uniqueKeysProcessed");
  }

  public void uploadFile(UploadRequest uploadRequest) throws IOException, InterruptedException
  {
    URI uri = uriFactory.buildUploadUri(uploadRequest);
    HttpRequest httpRequest = httpRequestFactory.createUploadFileRequest(uri, uploadRequest);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
  }

  public List<DownloadableFile> fetchDownloadableFiles(DownloadRequest downloadRequest) throws IOException, InterruptedException
  {
    URI downloadUri = uriFactory.buildDownloadUri(downloadRequest);
    HttpRequest httpRequest = httpRequestFactory.createGetRequest(downloadUri).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    throwOnError(httpResponse);
    String body = httpResponse.body();
    ExportResponse exportResponse = objectMapper.readValue(body, ExportResponse.class);
    return exportResponse.getFiles();
  }

  public void downloadFile(DownloadableFile downloadableFile, String downloadPathTemplate) throws IOException, InterruptedException
  {
    Optional<DownloadableFile> optionalDownloadableFile = Optional.of(downloadableFile);
    String downloadPath = downloadPathTemplate
            .replace(NAMESPACE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::getNamespace).orElse(""))
            .replace(LANGUAGE_TEMPLATE_KEY, optionalDownloadableFile.map(DownloadableFile::getLanguage).orElse(""));
    String url = downloadableFile.getUrl();
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
    HttpResponse<Path> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(savePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
    throwOnError(response);
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

  public void startAutoTranslation(List<String> languageKeys) throws IOException, InterruptedException
  {
    URI startAutoTranslationUri = uriFactory.buildStartAutoTranslationUri();
    HttpRequest httpRequest = httpRequestFactory.createBaseRequest(startAutoTranslationUri)
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(ClientBodyBuilders.ofStartAutoTranslation(languageKeys)).build();
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
