package io.simplelocalize.cli.configuration;

import io.micronaut.core.annotation.Introspected;

import java.util.ArrayList;
import java.util.List;

@Introspected
public class AutoTranslationConfiguration
{
  private List<String> languageKeys = new ArrayList<>();

  public List<String> getLanguageKeys()
  {
    return languageKeys;
  }

  public void setLanguageKeys(List<String> languageKeys)
  {
    this.languageKeys = languageKeys;
  }
}
