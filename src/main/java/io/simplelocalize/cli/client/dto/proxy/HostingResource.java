package io.simplelocalize.cli.client.dto.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;

@NativeProxy
@JsonIgnoreProperties(ignoreUnknown = true)
public record HostingResource(
        @JsonProperty("path") String path,
        @JsonProperty("namespace") String namespace,
        @JsonProperty("language") String language,
        @JsonProperty("customerId") String customerId,
        @JsonProperty("key") String key
)
{
}
