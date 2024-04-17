package io.simplelocalize.cli.client.dto;

import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
public record DownloadRequest(String format, String languageKey, String customerId, String namespace,
                              List<String> options, String sort)
{
}
