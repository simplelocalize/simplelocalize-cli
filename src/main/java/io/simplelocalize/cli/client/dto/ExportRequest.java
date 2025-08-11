package io.simplelocalize.cli.client.dto;

import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
public record ExportRequest(
        String format,
        List<String> languageKeys,
        String customerId,
        String namespace,
        List<String> options,
        String sort,
        List<String> tags
)
{
}
