package io.simplelocalize.cli.client.dto.proxy;

import io.simplelocalize.cli.NativeProxy;
import lombok.Builder;
import lombok.Data;

@Data
@NativeProxy
@Builder
public class SimpleLocalizeJsonMetadata
{
  private String defaultMessage;
  private String fileName;
  private String path;
}
