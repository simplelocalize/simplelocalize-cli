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
    if (!dir.exists()) dir.mkdirs();
    FileInputStream fileInputStream;
    byte[] buffer = new byte[1024];
    try
    {
      fileInputStream = new FileInputStream(zipFilePath);
      ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
      ZipEntry entry = zipInputStream.getNextEntry();
      while (entry != null)
      {
        String fileName = entry.getName();
        File newFile = new File(destinationPath + File.separator + fileName);
        log.info("Unzipping to " + newFile.getAbsolutePath());
        new File(newFile.getParent()).mkdirs();
        FileOutputStream fos = new FileOutputStream(newFile);
        int lenght;
        while ((lenght = zipInputStream.read(buffer)) > 0)
        {
          fos.write(buffer, 0, lenght);
        }
        fos.close();
        zipInputStream.closeEntry();
        entry = zipInputStream.getNextEntry();
      }
      zipInputStream.closeEntry();
      zipInputStream.close();
      fileInputStream.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }


}
