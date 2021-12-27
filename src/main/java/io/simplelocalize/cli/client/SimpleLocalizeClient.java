package io.simplelocalize.cli.client;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.jayway.jsonpath.JsonPath;
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
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;


public final class SimpleLocalizeClient
{
  private static final String PRODUCTION_BASE_URL = "https://api.simplelocalize.io";
  private static final String TOKEN_HEADER_NAME = "X-SimpleLocalize-Token";
  private final HttpClient httpClient;
  private final String baseUrl;
  private final String apiKey;
  private final String profile;

  private final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);
  private final FileWriter fileWriter;
  private final SecureRandom random;

  public SimpleLocalizeClient(String baseUrl, String apiKey, String profile)
  {
    Objects.requireNonNull(apiKey);
    this.apiKey = apiKey;

    if (StringUtils.isEmpty(profile))
    {
      this.profile = "default";
    } else
    {
      this.profile = profile;
    }
    this.baseUrl = baseUrl;
    this.random = new SecureRandom();
    this.fileWriter = new FileWriter();
    this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(5))
            .build();
  }

  public static SimpleLocalizeClient withProductionServer(String apiKey, String profile)
  {
    return new SimpleLocalizeClient(PRODUCTION_BASE_URL, apiKey, profile);
  }

  public void sendKeys(Collection<String> keys) throws IOException, InterruptedException
  {
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofKeysBody(keys))
            .uri(URI.create(baseUrl + "/cli/v1/keys?profile=" + profile))
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
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
      System.exit(1);
    }
  }

  public void uploadFile(Path uploadPath, String languageKey, String uploadFormat, String uploadOptions) throws IOException, InterruptedException
  {
    int pseudoRandomNumber = (int) (random.nextDouble() * 1_000_000_000);
    String boundary = "simplelocalize" + pseudoRandomNumber;
    Map<Object, Object> formData = Maps.newHashMap();
    formData.put("file", uploadPath);
    log.info(" 🌍 Uploading {} with language key '{}'", uploadPath, languageKey);
    if (StringUtils.isNotEmpty(languageKey))
    {
      formData.put("languageKey", languageKey);
    }

    String endpointUrl = baseUrl + "/cli/v1/upload?uploadFormat=" + uploadFormat;
    if (StringUtils.isNotEmpty(uploadOptions))
    {
      endpointUrl += "&uploadOptions=" + uploadOptions;
    }
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofMimeMultipartData(formData, boundary))
            .uri(URI.create(endpointUrl))
            .header(TOKEN_HEADER_NAME, apiKey)
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    if (httpResponse.statusCode() == 200)
    {
      log.info(" 🎉 Upload success");
    } else
    {
      log.error(" 😝 Upload failed");
      log.error("{} - {}", httpResponse.statusCode(), httpResponse.body());
    }

  }

  public void downloadFile(Path downloadPath, String downloadFormat, String languageKey) throws IOException, InterruptedException
  {
    String endpointUrl = baseUrl + "/cli/v2/download?downloadFormat=" + downloadFormat;
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
      return;
    }
    byte[] body = httpResponse.body();

    boolean isFileFormatWithAllLanguages = downloadFormat.equalsIgnoreCase("multi-language-json");

    //here I should decide if I should write downloaded file as single file or multiple files with multiple languages
    if (isRequestedTranslationsForSpecificLanguage || isFileFormatWithAllLanguages)
    {
      Files.createDirectories(downloadPath.getParent());
      Files.write(downloadPath, body);
    } else
    {
      fileWriter.saveAsMultipleFiles(downloadPath, body);
    }

    log.info(" 🎉 Download success!");
  }

  public int fetchGateCheckStatus() throws IOException, InterruptedException
  {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(baseUrl + "/cli/v1/analysis?profile=" + profile))
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
