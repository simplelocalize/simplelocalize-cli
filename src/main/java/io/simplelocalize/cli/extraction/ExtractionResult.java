package io.simplelocalize.cli.extraction;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@EqualsAndHashCode(of = "key")
public class ExtractionResult
{
  private String key;
  private String translation;
  private Path filePath;

  public static Collection<ExtractionResult> fromCollection(Collection<String> data, Path file)
  {
    if (data == null)
    {
      return new ArrayList<>();
    }

    return data.stream()
            .map(key -> ExtractionResult.builder()
                    .key(key)
                    .translation("")
                    .filePath(file)
                    .build()
            )
            .toList();
  }
}
