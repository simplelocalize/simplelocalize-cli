package io.simplelocalize.cli.extraction;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractionUtils
{

  private ExtractionUtils()
  {
  }


  public static Set<ExtractionResult> filterOutIgnoredKeys(Collection<ExtractionResult> keys, Collection<String> ignoredKeys)
  {
    return keys
            .stream()
            .filter(key -> !ignoredKeys.contains(key.getKey()))
            .collect(Collectors.toSet());
  }
}
