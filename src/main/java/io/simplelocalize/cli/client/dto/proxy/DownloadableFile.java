package io.simplelocalize.cli.client.dto.proxy;

import io.simplelocalize.cli.NativeProxy;
import lombok.Builder;

@NativeProxy
@Builder(setterPrefix = "with")
public record DownloadableFile(String url, String namespace, String language)
{
}
