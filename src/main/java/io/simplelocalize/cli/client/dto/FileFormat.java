package io.simplelocalize.cli.client.dto;

import io.simplelocalize.cli.command.DownloadCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FileFormat
{
  JAVA_PROPERTIES("java-properties"),
  ANDROID_XML("android-xml"),
  LOCALIZABLE_STRINGS("localizable-strings"),
  EXCEL("excel"),
  CSV("csv"),
  PO_POT("po-pot"),
  YAML("yaml"),
  MULTI_LANGUAGE_JSON("multi-language-json"),
  SIMPLELOCALIZE_JSON("simplelocalize-json"),
  MODULE_EXPORTS("module-exports"),
  SINGLE_LANGUAGE_JSON("single-language-json"),
  PHP_ARRAY("php-array");

  private static final Logger log = LoggerFactory.getLogger(DownloadCommand.class);

  private final String value;

  FileFormat(String value)
  {
    this.value = value;
  }

  public static void logWarningIfUnknownOrDeprecatedFileFormat(String value)
  {
    for (FileFormat fileFormat : FileFormat.values())
    {
      String fileFormatValue = fileFormat.getValue();
      if (fileFormatValue.equalsIgnoreCase(value))
      {
        return;
      }
    }

    log.warn(" 😒 You used unknown or deprecated '{}' file format value. Please use one of these: {}", value, FileFormat.values());
  }


  public String getValue()
  {
    return value;
  }
}
