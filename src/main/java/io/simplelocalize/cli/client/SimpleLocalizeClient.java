package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Objects;

public final class SimpleLocalizeClient {

  private static final String API_URL = "https://api.simplelocalize.io";
  private static final String TOKEN_HEADER_NAME = "X-SimpleLocalize-Token";
  private final HttpClient httpClient;
  private final String apiKey;
  private final String profile;

  private final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  public SimpleLocalizeClient(String apiKey, String profile) {
    Objects.requireNonNull(apiKey);
    this.apiKey = apiKey;

    if (StringUtils.isEmpty(profile)) {
      log.warn("Profile not set, using: default");
      this.profile = "default";
    } else {
      this.profile = profile;
    }

    this.httpClient = HttpClient.newBuilder().build();
  }

  public void sendKeys(Collection<String> keys) throws IOException, InterruptedException {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofKeysBody(keys))
            .uri(URI.create(API_URL + "/cli/v1/keys?profile=" + profile))
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    String json = httpResponse.body();
    int statusCode = httpResponse.statusCode();
    boolean isSuccessful = statusCode == 200;

    if (isSuccessful) {
      int keysProcessed = JsonPath.read(json, "$.data.uniqueKeysProcessed");
      boolean processedWithWarning = JsonPath.read(json, "$.data.processedWithWarnings");
      if (processedWithWarning) {
        log.warn("Cloud processed your request with warnings, but it was successful.");
      }
      log.info("Successfully uploaded {} keys", keysProcessed);

    } else {
      String message = JsonPath.read(json, "$.msg");
      log.warn("There was a problem with your request: {}", message);
    }
  }

  public int runAnalysis() throws IOException, InterruptedException {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(API_URL + "/cli/v1/analysis?profile=" + profile))
            .header(TOKEN_HEADER_NAME, apiKey)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    String json = httpResponse.body();
    int statusCode = httpResponse.statusCode();
    boolean isSuccessful = statusCode == 200;

    if (isSuccessful) {
      String gateResult = JsonPath.read(json, "$.data.gateResult");
      String message = JsonPath.read(json, "$.data.message");
      int status = JsonPath.read(json, "$.data.status");
      log.info("Gate result: {} (status: {}, message: {})", gateResult, status, message);
      return status;
    } else {
      String message = JsonPath.read(json, "$.msg");
      log.warn("There was a problem with your request: {}", message);
    }
    return -1;
  }

}
