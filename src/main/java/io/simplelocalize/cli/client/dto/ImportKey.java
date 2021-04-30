package io.simplelocalize.cli.client.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public final class ImportKey
{
  private final String key;

  public ImportKey(String key)
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }

}
