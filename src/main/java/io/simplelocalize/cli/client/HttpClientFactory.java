package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.ProxyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientFactory
{
  private static final Logger log = LoggerFactory.getLogger(HttpClientFactory.class);

  private HttpClientFactory()
  {
  }

  public static HttpClient createHttpClient()
  {
    HttpClient.Builder builder = HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofMinutes(5));

    String httpProxyValue = getSystemProxyVariable();
    if (httpProxyValue == null || httpProxyValue.isEmpty())
    {
      return builder.build();
    }

    ProxyConfiguration proxyConfigOptional = SystemProxySelector.getHttpProxyValueOrNull(httpProxyValue);
    if (proxyConfigOptional != null)
    {
      log.info("Using proxy: {}", proxyConfigOptional);
      ProxySelector proxySelector = getProxySelector(proxyConfigOptional);
      builder.proxy(proxySelector);

      String proxyUsername = proxyConfigOptional.username();
      String proxyPassword = proxyConfigOptional.password();
      boolean hasProxyAuthentication = proxyUsername != null && proxyPassword != null;
      if (hasProxyAuthentication)
      {
        Authenticator authenticator = getAuthenticator(proxyUsername, proxyPassword);
        builder.authenticator(authenticator);
      }
    }

    return builder.build();
  }

  private static String getSystemProxyVariable()
  {
    String systemProxyVariable = System.getenv("http_proxy");
    if (systemProxyVariable == null)
    {
      systemProxyVariable = System.getenv("https_proxy");
    }
    return systemProxyVariable;
  }

  private static ProxySelector getProxySelector(ProxyConfiguration proxyConfigOptional)
  {
    String host = proxyConfigOptional.host();
    Integer port = proxyConfigOptional.port();
    InetSocketAddress proxyAddress = new InetSocketAddress(host, port);
    return ProxySelector.of(proxyAddress);
  }

  private static Authenticator getAuthenticator(String proxyUsername, String proxyPassword)
  {
    return new Authenticator()
    {
      @Override
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
      }
    };
  }
}
