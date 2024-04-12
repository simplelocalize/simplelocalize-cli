package io.simplelocalize.cli.client.dto.proxy;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;

import java.util.Collection;

@NativeProxy
public record StartAutoTranslationRequest(
        @JsonProperty("languageKeys")
        Collection<String> languageKeys,
        @JsonProperty("source")
        String source)
{
}
