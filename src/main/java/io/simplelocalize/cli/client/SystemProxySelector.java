package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.ProxyConfiguration;

public class SystemProxySelector
{
  private SystemProxySelector()
  {
  }

  public static ProxyConfiguration getSystemEnvironmentProxy()
  {
    String environmentProxyValue = getEnvironmentProxyString();
    if (environmentProxyValue == null)
    {
      return null;
    }

    if (environmentProxyValue.startsWith("http://"))
    {
      environmentProxyValue = environmentProxyValue.replace("http://", "");
    } else
    {
      throw new IllegalArgumentException("Proxy url must start with http://");
    }

    String addressUrl;
    String authenticationString = "";
    if (environmentProxyValue.contains("@"))
    {
      String[] authenticationAndAddress = environmentProxyValue.split("@");
      authenticationString = authenticationAndAddress[0];
      addressUrl = authenticationAndAddress[1];
    } else
    {
      addressUrl = environmentProxyValue;
    }

    String[] hostAndPortParts = addressUrl.split(":");
    String host = hostAndPortParts[0];
    Integer port = Integer.valueOf(hostAndPortParts[1]);

    String username = null;
    String password = null;
    if (authenticationString.contains(":"))
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

  private static String getEnvironmentProxyString()
  {
    String host = System.getenv("http_proxy");
    if (host == null)
    {
      host = System.getenv("https_proxy");
    }
    return host;
  }
}
