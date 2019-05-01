package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Set;

public final class SimpleLocalizeClient {

  private static final String API_URL = "https://api.simplelocalize.io";
  private static final String TOKEN_HEADER_NAME = "X-SimpleLocalize-Token";
  private final HttpClient httpClient;
  private final String uploadToken;

  private Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  public SimpleLocalizeClient(String uploadToken) {
    Objects.requireNonNull(uploadToken);
    this.uploadToken = uploadToken;
    this.httpClient = HttpClient.newBuilder().build();
  }

  public void sendKeys(Set<String> keys) throws IOException, InterruptedException {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofKeysBody(keys))
            .uri(URI.create(API_URL + "/api/v1/keys"))
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(TOKEN_HEADER_NAME, uploadToken)
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    String json = httpResponse.body();

    int keysProcessed = 0;
    try {
      keysProcessed = JsonPath.read(json, "$.data.uniqueKeysProcessed");
    } catch (Exception e) {
      log.warn(json);
    }
    int statusCode = httpResponse.statusCode();
    if (statusCode == 201) {
      log.info("SimpleLocalize Cloud successfully processed {} keys", keysProcessed);
    } else if (statusCode == 200) {
      log.info("SimpleLocalize Cloud successfully processed your request but no new keys were processed");
    } else {
      log.warn("There was a problem with your request");
    }
  }

}
