package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.ProxyConfiguration;

public class SystemProxySelector
{
  private SystemProxySelector()
  {
  }

  public static ProxyConfiguration getHttpProxyValueOrNull(String httpProxyValue)
  {
    if (httpProxyValue == null || httpProxyValue.isEmpty())
    {
      return null;
    }

    boolean isHttpOrHttps = httpProxyValue.startsWith("http://") || httpProxyValue.startsWith("https://");
    if (isHttpOrHttps)
    {
      httpProxyValue = httpProxyValue
              .replace("http://", "")
              .replace("https://", "");
    } else
    {
      throw new IllegalArgumentException("Proxy url must start with 'http://' or 'https://', other protocols are not supported");
    }

    String addressUrl;
    String authenticationString = "";
    boolean hasAuthentication = httpProxyValue.contains("@");
    if (hasAuthentication)
    {
      String[] authenticationAndAddress = httpProxyValue.split("@");
      authenticationString = authenticationAndAddress[0];
      addressUrl = authenticationAndAddress[1];
    } else
    {
      addressUrl = httpProxyValue;
    }

    String host;
    int port = 80;
    boolean hasHostnameAndPort = addressUrl.contains(":");
    if (hasHostnameAndPort)
    {
      String[] hostAndPortParts = addressUrl.split(":");
      host = hostAndPortParts[0];
      port = Integer.parseInt(hostAndPortParts[1]);
    } else
    {
      host = addressUrl;
    }

    String username = null;
    String password = null;
    boolean hasUsernameAndPassword = authenticationString.contains(":");
    if (hasUsernameAndPassword)
    {
      String[] usernameAndPassword = authenticationString.split(":");
      username = usernameAndPassword[0];
      password = usernameAndPassword[1];
    }
    return new ProxyConfiguration()
            .setHost(host)
            .setPort(port)
            .setUsername(username)
            .setPassword(password);
  }
}
