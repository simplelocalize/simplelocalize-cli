package io.simplelocalize.cli.client.dto.proxy;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NativeProxy
public class AutoTranslationConfiguration
{
  @JsonProperty("languageKeys")
  private List<String> languageKeys;

  public AutoTranslationConfiguration()
  {
    this.languageKeys = new ArrayList<>();
  }

  public List<String> getLanguageKeys()
  {
    return Objects.requireNonNullElseGet(languageKeys, List::of);
  }

  public void setLanguageKeys(List<String> languageKeys)
  {
    this.languageKeys = languageKeys;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (AutoTranslationConfiguration) obj;
    return Objects.equals(this.languageKeys, that.languageKeys);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(languageKeys);
  }

  @Override
  public String toString()
  {
    return "AutoTranslationConfiguration[" +
           "languageKeys=" + languageKeys + ']';
  }

}
