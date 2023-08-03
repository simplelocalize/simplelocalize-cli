package io.simplelocalize.cli.client.dto;

import java.util.Objects;

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
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProxyConfiguration that = (ProxyConfiguration) o;
    return Objects.equals(host, that.host) && Objects.equals(port, that.port) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(host, port, username, password);
  }

  @Override
  public String toString()
  {
    String redactedPassword = password == null ? null : "*****";
    return
            "host='" + host + '\'' +
            ", port=" + port +
            ", username='" + username + '\'' +
            ", password='" + redactedPassword + '\'';
  }
}
