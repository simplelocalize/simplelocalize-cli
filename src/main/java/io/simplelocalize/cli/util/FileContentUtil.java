package io.simplelocalize.cli.util;

public class FileContentUtil {

  private FileContentUtil() {
  }

  public static String transformTextToOneLine(String fileContent){
    fileContent = fileContent.replaceAll("\n", " ");
    fileContent = fileContent.replaceAll("\\s+", " ");
    return fileContent;
  }
}
