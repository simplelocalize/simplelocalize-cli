package io.simplelocalize.cli.extraction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleLocalizeJsonMetadata
{
  private final String defaultMessage;
  private final String fileName;
  private final String path;
}
