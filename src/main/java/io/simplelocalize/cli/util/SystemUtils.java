package io.simplelocalize.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SystemUtils
{
  private static final Logger log = LoggerFactory.getLogger(SystemUtils.class);

  private SystemUtils()
  {
  }

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
      log.info("Detected Windows machine and '/' in file path");
      return path.replace("/", "\\");
    }
    return path;
  }

}
