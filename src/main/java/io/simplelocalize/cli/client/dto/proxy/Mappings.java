package io.simplelocalize.cli.client.dto.proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mappings
{
  @Builder.Default
  private List<LanguageTransform> lang = new ArrayList<>();
}
