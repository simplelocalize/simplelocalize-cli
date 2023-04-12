package io.simplelocalize.cli.client.dto;

import io.micronaut.core.annotation.Introspected;

import java.util.Collection;
import java.util.Objects;

@Introspected
public final class StartAutoTranslationRequest
{
  private final Collection<String> languageKeys;

  private final String source;

  public StartAutoTranslationRequest(Collection<String> languageKeys, String source)
  {
    this.languageKeys = languageKeys;
    this.source = source;
  }

  public Collection<String> getLanguageKeys()
  {
    return languageKeys;
  }

  public String getSource()
  {
    return source;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StartAutoTranslationRequest that = (StartAutoTranslationRequest) o;
    return Objects.equals(languageKeys, that.languageKeys) && Objects.equals(source, that.source);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(languageKeys, source);
  }
}