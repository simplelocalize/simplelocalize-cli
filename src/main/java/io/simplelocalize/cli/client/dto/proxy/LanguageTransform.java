package io.simplelocalize.cli.client.dto.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single language mapping entry (configurable only via YAML configuration file, under mappings.lang).
 * Maps a SimpleLocalize language key ({@code languageKey}) to the language name used in the
 * file system / {lang} placeholder ({@code placeholder}). Unlike 'source'/'target', these names stay
 * unambiguous regardless of direction (download or upload).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageTransform
{
  private String languageKey;
  private String placeholder;
}
