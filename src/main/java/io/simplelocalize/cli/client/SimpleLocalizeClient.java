package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.exception.AccessDeniedException;
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
  private final HttpClient httpClient;
  private final BasicHttpAuthenticator authenticator;

  private Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  public SimpleLocalizeClient(String clientId, String secret) {
    Objects.requireNonNull(clientId);
    Objects.requireNonNull(secret);
    this.authenticator = new BasicHttpAuthenticator(clientId, secret);
    this.httpClient = HttpClient.newBuilder()
            .authenticator(authenticator)
            .build();
  }

  public void pushKeys(String projectHash, Set<String> keys) throws IOException, InterruptedException {
    Objects.requireNonNull(projectHash);
    String accessToken = requestAccessToken();

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofKeysBody(keys))
            .uri(URI.create(API_URL + "/projects/" + projectHash + "/keys"))
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
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

  private String requestAccessToken() throws IOException, InterruptedException {

    HttpRequest httpRequest = HttpRequest.newBuilder()
            .POST(ClientBodyBuilders.ofClientCredentialsGrantType())
            .uri(URI.create(API_URL + "/oauth/token"))
            .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .build();

    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    String body = httpResponse.body();
    if (httpResponse.statusCode() != 200) {
      String message = JsonPath.read(body, "$.data.message");
      log.error("There was a problem with authentication due error: {}", message);
      throw new AccessDeniedException();
    }
    return JsonPath.read(body, "$.access_token");
  }

}
