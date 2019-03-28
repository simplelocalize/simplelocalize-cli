package io.simplelocalize.cli.client;

import com.google.common.net.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.util.Base64;

public class BasicHttpAuthenticator implements HttpRequestInterceptor {

  private final String clientId;
  private final String secret;

  BasicHttpAuthenticator(String clientId, String secret) {
    this.clientId = clientId;
    this.secret = secret;
  }

  @Override
  public void process(HttpRequest request, HttpContext context) {
    request.setHeader(HttpHeaders.AUTHORIZATION, getClientToken());
  }

  public String getClientToken() {
    byte[] inputBytes = (clientId + ":" + secret).getBytes();
    String encode = Base64.getEncoder().encodeToString(inputBytes);
    return "Basic " + encode;
  }
}
