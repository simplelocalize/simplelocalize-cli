package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils
{
  private static final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  private ZipUtils()
  {
  }

  public static void unzip(String zipFilePath, String destinationPath, String templateKey)
  {
    try
    {
      unzipFiles(zipFilePath, destinationPath, templateKey);
    } catch (IOException e)
    {
      log.error(" 😝 Unable to unzip archive", e);
    }

  }

  private static void unzipFiles(String zipFilePath, String destinationPath, String templateKey) throws IOException
  {
    byte[] buffer = new byte[1024];
    try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath)))
    {
      ZipEntry entry = zipInputStream.getNextEntry();
      while (entry != null)
      {
        String fileName = entry.getName();
        String outputPath = StringUtils.replace(destinationPath, templateKey, fileName);
        File outputFile = new File(outputPath);
        log.info(" 📦 Unzipping to " + outputFile.getAbsolutePath());
        Files.createDirectories(Path.of(outputFile.getParent()));

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile))
        {
          int length;
          while ((length = zipInputStream.read(buffer)) > 0)
          {
            fileOutputStream.write(buffer, 0, length);
          }
        }
        zipInputStream.closeEntry();
        entry = zipInputStream.getNextEntry();
      }
      zipInputStream.closeEntry();
    }
  }


}
