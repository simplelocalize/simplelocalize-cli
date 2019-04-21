package io.simplelocalize.cli.client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class BasicHttpAuthenticator extends Authenticator {

  private final String clientId;
  private final String secret;

  BasicHttpAuthenticator(String clientId, String secret) {
    this.clientId = clientId;
    this.secret = secret;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(clientId, secret.toCharArray());
  }
}
