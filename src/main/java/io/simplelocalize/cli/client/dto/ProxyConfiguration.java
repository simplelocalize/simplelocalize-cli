package io.simplelocalize.cli.client.dto;

import lombok.Builder;

@Builder
public record ProxyConfiguration(
        String host,
        Integer port,
        String username,
        String password
)
{

  @Override
  public String toString()
  {
    return "host='%s', port=%d, username='%s', password='%s'".formatted(host, port, username, password == null ? null : "***");
  }

}
