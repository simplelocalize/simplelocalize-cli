package io.simplelocalize.cli.client.dto.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;

import java.util.List;
import java.util.Map;

@NativeProxy
public record ExportResponse(
        @JsonProperty("files") List<DownloadableFile> files,
        @JsonIgnore Map<String, Object> additionalProperties
)
{
}
