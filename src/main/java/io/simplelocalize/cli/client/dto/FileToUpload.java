package io.simplelocalize.cli.client.dto;


import lombok.Builder;

import java.nio.file.Path;

@Builder(setterPrefix = "with")
public record FileToUpload(Path path, String language, String namespace)
{
}
