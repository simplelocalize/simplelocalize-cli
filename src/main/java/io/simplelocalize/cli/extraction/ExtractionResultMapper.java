package io.simplelocalize.cli.extraction;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class ExtractionResultMapper
{

  public Map<String, SimpleLocalizeJsonMetadata> map(Collection<ExtractionResult> extractionResults)
  {
    Map<String, SimpleLocalizeJsonMetadata> output = new TreeMap<>();
    for (ExtractionResult extractionResult : extractionResults)
    {
      output.put(
              extractionResult.getKey(),
              SimpleLocalizeJsonMetadata.builder()
                      .defaultMessage(extractionResult.getTranslation())
                      .fileName(extractionResult.getFilePath().getFileName().toString())
                      .path(extractionResult.getFilePath().toString())
                      .build()
      );
    }
    return output;
  }
}
