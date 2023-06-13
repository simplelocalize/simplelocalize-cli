package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "config-at", mixinStandardHelpOptions = true)
@NativeProxy
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
