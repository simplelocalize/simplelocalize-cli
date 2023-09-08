package io.simplelocalize.cli.client.dto.proxy;

import io.simplelocalize.cli.NativeProxy;

import java.util.Set;

@NativeProxy
public record ImportForm(Set<ImportKey> content)
{
}
