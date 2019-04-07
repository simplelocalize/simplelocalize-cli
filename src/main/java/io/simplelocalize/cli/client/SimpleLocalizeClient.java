package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.exception.AccessDeniedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import static io.simplelocalize.cli.client.ClientUtils.extractAccessToken;
import static io.simplelocalize.cli.client.ClientUtils.tryExtractMessage;

public final class SimpleLocalizeClient {

  private static final String API_URL = "https://api.simplelocalize.io";
  private final HttpClient httpClient;
  private final BasicHttpAuthenticator authenticator;

  private Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  public SimpleLocalizeClient(String clientId, String secret) {
    Objects.requireNonNull(clientId);
    Objects.requireNonNull(secret);
    this.authenticator = new BasicHttpAuthenticator(clientId, secret);
    this.httpClient = new DefaultHttpClient(); // NOSONAR, builder fails on second request
  }

  public void pushKeys(String projectHash, Set<String> keys) throws IOException {
    Objects.requireNonNull(projectHash);
    String accessToken = requestAccessToken();

    HttpPost pushRequest = new HttpPost(API_URL + "/projects/" + projectHash + "/keys");
    pushRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    pushRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    pushRequest.setEntity(ClientBodyBuilders.ofKeysBody(keys));

    HttpResponse httpResponse = httpClient.execute(pushRequest);
    String responseJson = ClientUtils.parseToJson(httpResponse);

    int keysProcessed = 0;
    try {
      keysProcessed = JsonPath.read(responseJson, "$.data.uniqueKeysProcessed");
    } catch (Exception e) {
      log.warn(responseJson);
    }
    int statusCode = httpResponse.getStatusLine().getStatusCode();
    if (statusCode == 201) {
      log.info("SimpleLocalize Cloud successfully processed {} keys", keysProcessed);
    } else if (statusCode == 200) {
      log.info("SimpleLocalize Cloud successfully processed your request but no new keys were processed");
    } else {
      log.warn("There was a problem with your request");
    }
  }

  private String requestAccessToken() throws IOException {

    HttpPost request = new HttpPost(API_URL + "/oauth/token");
    request.setEntity(ClientBodyBuilders.ofClientCredentialsGrantType());
    request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
    request.setHeader(HttpHeaders.AUTHORIZATION, authenticator.getClientToken());
    HttpResponse tokenResponse = httpClient.execute(request);

    if (tokenResponse.getStatusLine().getStatusCode() != 200) {
      log.error("There was a problem with authentication due error: {}", tryExtractMessage(tokenResponse));
      throw new AccessDeniedException();
    }
    return extractAccessToken(tokenResponse);
  }

}
