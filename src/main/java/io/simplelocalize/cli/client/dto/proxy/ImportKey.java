package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;
import picocli.CommandLine;

@CommandLine.Command(name = "config-ik", mixinStandardHelpOptions = true)
@NativeProxy
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
