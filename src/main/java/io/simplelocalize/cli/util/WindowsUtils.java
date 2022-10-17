package io.simplelocalize.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class WindowsUtils
{

  private static final Logger log = LoggerFactory.getLogger(WindowsUtils.class);


  public static boolean isWindows()
  {
    String property = System.getProperty("os.name");
    String operatingSystem = Optional.ofNullable(property).map(String::toLowerCase).orElse("");
    return operatingSystem.contains("win");
  }

  public static String convertToWindowsPath(String path)
  {
    boolean hasForwardSlash = path.contains("/");
    if (hasForwardSlash)
    {
      log.info("Detected Windows machine and '/' in path. Converting to Windows path");
      return path.replace("/", "\\");
    }
    return path;
  }

}
