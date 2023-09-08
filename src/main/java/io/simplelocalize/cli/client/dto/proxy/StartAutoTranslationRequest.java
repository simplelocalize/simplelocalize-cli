package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;

import java.util.Collection;

@NativeProxy
public record StartAutoTranslationRequest(Collection<String> languageKeys, String source)
{
}
