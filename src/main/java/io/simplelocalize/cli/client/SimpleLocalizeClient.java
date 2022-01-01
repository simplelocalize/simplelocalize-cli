package io.simplelocalize.cli.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.client.dto.ExportResponse;
import io.simplelocalize.cli.client.dto.FileFormat;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.FileWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;

public class SimpleLocalizeClient
{
  //  private static final String PRODUCTION_BASE_URL = "http://localhost:8080";
  private static final String PRODUCTION_BASE_URL = "https://api.simplelocalize.io";
  private static final String TOKEN_HEADER_NAME = "X-SimpleLocalize-Token";
  private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

  private final HttpClient httpClient;
  private final String baseUrl;
  private final String apiKey;

  private final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);
  private final FileWriter fileWriter;
  private final ObjectMapper objectMapper;
  private final SecureRandom random;

  public SimpleLocalizeClient(String baseUrl, String apiKey)
  {
    Objects.requireNonNull(baseUrl);
    Objects.requireNonNull(apiKey);
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.random = new SecureRandom();
    this.objectMapper = new ObjectMapper();
    this.fileWriter = new FileWriter();
    this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(5))
            .build();
  }

  public static SimpleLocalizeClient withProductionServer(Configuration configuration)
  {
    return new SimpleLocalizeClient(PRODUCTION_BASE_URL, configuration.getApiKey());
  }

  public void sendKeys(Collection<String> keys) throws IOException, InterruptedException
  {
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofKeysBody(keys))
            .uri(URI.create(baseUrl + "/cli/v1/keys"))
            .header(CONTENT_TYPE_HEADER_NAME, "application/json")
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    String json = httpResponse.body();

    if (httpResponse.statusCode() == 200)
    {
      int keysProcessed = JsonPath.read(json, "$.data.uniqueKeysProcessed");
      boolean processedWithWarning = JsonPath.read(json, "$.data.processedWithWarnings");
      if (processedWithWarning)
      {
        log.warn(" 🤨 SimpleLocalize processed your request with warnings, but it was successful.");
      }
      log.info(" 🎉 Successfully uploaded {} keys", keysProcessed);

    } else
    {
      String message = JsonPath.read(json, "$.msg");
      log.error(" 😝 There was a problem with your request: {}", message);
      throw new IllegalStateException();
    }
  }

  public void uploadFile(Path uploadPath, String languageKey, String uploadFormat, String uploadOptions, String relativePath) throws IOException, InterruptedException
  {
    int pseudoRandomNumber = (int) (random.nextDouble() * 1_000_000_000);
    String boundary = "simplelocalize" + pseudoRandomNumber;
    Map<Object, Object> formData = new HashMap<>();
    formData.put("file", uploadPath);
    String endpointUrl = baseUrl + "/cli/v1/upload?uploadFormat=" + uploadFormat;
    if (StringUtils.isNotEmpty(languageKey))
    {
      endpointUrl += "&languageKey=" + languageKey;
    }

    if (StringUtils.isNotEmpty(uploadOptions))
    {
      endpointUrl += "&uploadOptions=" + uploadOptions;
    }

    if (StringUtils.isNotEmpty(relativePath))
    {
      endpointUrl += "&projectPath=" + relativePath;
    }

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofMimeMultipartData(formData, boundary))
            .uri(URI.create(endpointUrl))
            .header(TOKEN_HEADER_NAME, apiKey)
            .header(CONTENT_TYPE_HEADER_NAME, "multipart/form-data; boundary=" + boundary)
            .build();

    log.info(" 🌍 Uploading {}", uploadPath);
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    if (httpResponse.statusCode() != 200)
    {
      log.error(" 😝 Upload failed");
      log.error("{} - {}", httpResponse.statusCode(), httpResponse.body());
      throw new IllegalStateException();
    }
  }

  public void downloadFile(String downloadPath, String downloadFormat, String languageKey) throws IOException, InterruptedException
  {
    String endpointUrl = baseUrl + "/cli/v1/download?downloadFormat=" + downloadFormat;
    boolean isRequestedTranslationsForSpecificLanguage = StringUtils.isNotEmpty(languageKey);
    if (isRequestedTranslationsForSpecificLanguage)
    {
      endpointUrl += "&languageKey=" + languageKey;
    }
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(endpointUrl))
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    log.info(" 🌍 Downloading to {}", downloadPath);
    HttpResponse<byte[]> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    if (httpResponse.statusCode() != 200)
    {
      log.error(" 😝 Download failed");
      log.error("{} - {}", httpResponse.statusCode(), httpResponse.body());
      throw new IllegalStateException();
    }
    byte[] body = httpResponse.body();

    boolean isFileFormatWithAllLanguages = downloadFormat.equalsIgnoreCase("multi-language-json");

    if (isRequestedTranslationsForSpecificLanguage || isFileFormatWithAllLanguages)
    {
      Files.createDirectories(Paths.get(downloadPath).getParent());
      Files.write(Paths.get(downloadPath), body);
    } else
    {
      fileWriter.saveAsMultipleFiles(downloadPath, body);
    }

    log.info(" 🎉 Download success!");
  }

  public void downloadMultiFile(String downloadPath, String downloadFormat) throws IOException, InterruptedException
  {
    FileFormat.logWarningIfUnknownOrDeprecatedFileFormat(downloadFormat);
    String endpointUrl = baseUrl + "/cli/v2/download?exportOptions=MULTI_FILE&downloadFormat=" + downloadFormat;

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(endpointUrl))
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    if (httpResponse.statusCode() != 200)
    {
      log.error(" 😝 Download failed");
      log.error("{} - {}", httpResponse.statusCode(), httpResponse.body());
      throw new IllegalStateException();
    }
    String body = httpResponse.body();
    ExportResponse exportResponse = objectMapper.readValue(body, ExportResponse.class);
    List<DownloadableFile> downloadableFiles = exportResponse.getFiles();
    downloadableFiles
            .parallelStream()
            .forEach(downloadableFile -> saveFile(downloadableFile, downloadPath));
    log.info(" 🎉 Download success!");
  }

  private void saveFile(DownloadableFile downloadableFile, String downloadPath)
  {
    String url = downloadableFile.getUrl();
    Path savePath = Paths.get(downloadPath, downloadableFile.getProjectPath());

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .build();
    try
    {
      log.info(" 🌍 Downloading to {}", savePath);
      Files.createDirectories(savePath.getParent());
      httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(savePath));
    } catch (IOException | InterruptedException e)
    {
      log.error(" 😝 Download failed: {}", savePath, e);
      throw new IllegalStateException();
    }
  }

  public int fetchGateCheckStatus() throws IOException, InterruptedException
  {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(baseUrl + "/cli/v1/analysis"))
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    String json = httpResponse.body();

    if (httpResponse.statusCode() == 200)
    {
      String gateResult = JsonPath.read(json, "$.data.gateResult");
      String message = JsonPath.read(json, "$.data.message");
      int status = JsonPath.read(json, "$.data.status");
      log.info(" 🌍 Gate result: {} (status: {}, message: {})", gateResult, status, message);
      return status;
    } else
    {
      String message = JsonPath.read(json, "$.msg");
      log.error(" 😝 There was a problem with your request: {}", message);
    }
    return -1;
  }

}
