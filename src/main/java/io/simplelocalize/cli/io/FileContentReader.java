package io.simplelocalize.cli.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileContentReader
{

  private static final Logger log = LoggerFactory.getLogger(FileListReader.class);


  private FileContentReader()
  {
  }

  public static String tryReadContent(Path filePath)
  {
    Path decodedFilePath = null;
    try
    {
      decodedFilePath = Paths.get(URLDecoder.decode(String.valueOf(Paths.get(String.valueOf(filePath))), StandardCharsets.UTF_8));
      return Files.readString(decodedFilePath, StandardCharsets.UTF_8);
    } catch (IOException e)
    {
      log.warn("Cannot read file from path " + decodedFilePath.toString(), e);
    }
    return "";
  }

  public static String transformTextToOneLine(String fileContent)
  {
    fileContent = fileContent.replaceAll("\n", " ");
    fileContent = fileContent.replaceAll("\\s+", " ");
    return fileContent;
  }

}
