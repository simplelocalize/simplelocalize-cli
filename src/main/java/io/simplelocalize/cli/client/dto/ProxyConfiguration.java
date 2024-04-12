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
}
