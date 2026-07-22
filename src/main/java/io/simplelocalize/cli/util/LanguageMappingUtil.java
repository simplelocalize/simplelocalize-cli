package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.proxy.LanguageTransform;

import java.util.List;

/**
 * Utility for translating language keys between SimpleLocalize and the file system representation ({lang}).
 * The mapping is configured only via the YAML configuration file, under mappings.lang, where 'languageKey' is
 * the SimpleLocalize language key and 'placeholder' is the name used in the file system.
 */
public final class LanguageMappingUtil
{

  private LanguageMappingUtil()
  {
  }

  /**
   * Translates a SimpleLocalize language key into the file system language name used in the {lang} placeholder.
   * Used on download. When there is no mapping for the given key, the original key is returned unchanged.
   */
  public static String toFileSystemLanguage(List<LanguageTransform> languageTransforms, String languageKey)
  {
    if (languageTransforms == null || languageTransforms.isEmpty() || languageKey == null)
    {
      return languageKey;
    }
    for (LanguageTransform languageTransform : languageTransforms)
    {
      if (languageKey.equals(languageTransform.getLanguageKey()))
      {
        return languageTransform.getPlaceholder();
      }
    }
    return languageKey;
  }

  /**
   * Translates a file system language name (found in the {lang} placeholder) back into the SimpleLocalize language key.
   * Used on upload. This is the reverse of {@link #toFileSystemLanguage(List, String)}.
   * When there is no mapping for the given file system name, the original value is returned unchanged.
   */
  public static String toSimpleLocalizeLanguage(List<LanguageTransform> languageTransforms, String fileSystemLanguage)
  {
    if (languageTransforms == null || languageTransforms.isEmpty() || fileSystemLanguage == null)
    {
      return fileSystemLanguage;
    }
    for (LanguageTransform languageTransform : languageTransforms)
    {
      if (fileSystemLanguage.equals(languageTransform.getPlaceholder()))
      {
        return languageTransform.getLanguageKey();
      }
    }
    return fileSystemLanguage;
  }
}
