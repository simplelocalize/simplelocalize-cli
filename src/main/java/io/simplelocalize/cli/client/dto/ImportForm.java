package io.simplelocalize.cli.client.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ImportForm
{
    private Set<ImportKey> content;
}
