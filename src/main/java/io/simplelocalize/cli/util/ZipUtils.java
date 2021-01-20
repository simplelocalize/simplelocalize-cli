package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils
{
  private static final Logger log = LoggerFactory.getLogger(SimpleLocalizeClient.class);

  private ZipUtils()
  {
  }

  public static void unzip(String zipFilePath, String destinationPath)
  {
    File dir = new File(destinationPath);
    if (!dir.exists())
    {
      boolean isSuccessful = dir.mkdirs();
      if (!isSuccessful)
      {
        log.warn("Unable to create not existing directories");
      }
    }

    try
    {
      unzipFiles(zipFilePath, destinationPath);
    } catch (IOException e)
    {
      log.error("Unable to unzip archive", e);
    }

  }

  private static void unzipFiles(String zipFilePath, String destinationPath) throws IOException
  {
    byte[] buffer = new byte[1024];
    try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath)))
    {
      ZipEntry entry = zipInputStream.getNextEntry();
      while (entry != null)
      {
        String fileName = entry.getName();
        File newFile = new File(destinationPath + File.separator + fileName);
        log.info("Unzipping to " + newFile.getAbsolutePath());
        boolean isSuccessful = new File(newFile.getParent()).mkdirs();
        if (isSuccessful)
        {
          log.warn("Unable to create not existing directories for {}", newFile.getAbsolutePath());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile))
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
