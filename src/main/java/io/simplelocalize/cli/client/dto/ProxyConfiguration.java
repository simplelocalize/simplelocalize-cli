package io.simplelocalize.cli.client.dto;

public class ProxyConfiguration
{
  private String host;
  private Integer port;
  private String username;
  private String password;

  public String getHost()
  {
    return host;
  }

  public ProxyConfiguration setHost(String host)
  {
    this.host = host;
    return this;
  }

  public Integer getPort()
  {
    return port;
  }

  public ProxyConfiguration setPort(Integer port)
  {
    this.port = port;
    return this;
  }

  public String getUsername()
  {
    return username;
  }

  public ProxyConfiguration setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getPassword()
  {
    return password;
  }

  public ProxyConfiguration setPassword(String password)
  {
    this.password = password;
    return this;
  }

  @Override
  public String toString()
  {
    if (username != null && password != null)
    {
      return "http://" + username + ":" + "<REDACTED>" + "@" + host + ":" + port;
    }
    return "http://" + host + ":" + port;

  }
}
