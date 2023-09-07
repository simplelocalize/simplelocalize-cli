package io.simplelocalize.cli.client.dto.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;

@NativeProxy
public record HostingResource(
        @JsonProperty("path") String path,
        @JsonProperty("namespace") String namespace,
        @JsonProperty("language") String language,
        @JsonProperty("customerId") String customerId,
        @JsonProperty("key") String key
)
{
}
