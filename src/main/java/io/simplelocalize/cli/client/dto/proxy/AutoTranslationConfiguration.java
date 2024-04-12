package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;
import lombok.*;

import java.util.List;

@NativeProxy
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class AutoTranslationConfiguration
{
  private List<String> languageKeys;

  public static AutoTranslationConfiguration defaultConfiguration()
  {
    return AutoTranslationConfiguration.builder().languageKeys(List.of()).build();
  }
}
