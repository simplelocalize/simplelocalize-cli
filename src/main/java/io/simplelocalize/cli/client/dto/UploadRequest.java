package io.simplelocalize.cli.client.dto;

import lombok.Builder;

import java.nio.file.Path;
import java.util.List;

@Builder(setterPrefix = "with")
public record UploadRequest(
        Path path,
        String languageKey,
        String format,
        List<String> options,
        String namespace,
        String customerId,
        String translationKey,
        List<String> tags
)
{
}
