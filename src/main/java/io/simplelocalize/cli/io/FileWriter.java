package io.simplelocalize.cli.io;

import io.simplelocalize.cli.util.ZipUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.simplelocalize.cli.io.FileListReader.LANGUAGE_TEMPLATE_KEY;

public class FileWriter
{

  public void writeFiles()
  {

  }

  public void saveAsMultipleFiles(Path downloadPath, byte[] body) throws IOException
  {
    String[] splitByLanguageTemplateKey = StringUtils.splitByWholeSeparator(downloadPath.toString(), LANGUAGE_TEMPLATE_KEY);
    String directoriesPartBeforeTemplateKeyWithPrefix = splitByLanguageTemplateKey[0];
    String directoriesPartBeforeTemplateKey = removePrefix(directoriesPartBeforeTemplateKeyWithPrefix);
    Files.createDirectories(Path.of(directoriesPartBeforeTemplateKey));
    Path fileSavePath = Path.of(directoriesPartBeforeTemplateKey + File.separator + "translations.zip");

    Files.write(fileSavePath, body);
    ZipUtils.unzip(fileSavePath.toString(), downloadPath.toString(), LANGUAGE_TEMPLATE_KEY);
    Files.delete(fileSavePath);
  }

  private String removePrefix(String directoriesPartBeforeTemplateKeyWithPrefix)
  {
    return StringUtils.substringBeforeLast(directoriesPartBeforeTemplateKeyWithPrefix, File.separator);
  }
}
