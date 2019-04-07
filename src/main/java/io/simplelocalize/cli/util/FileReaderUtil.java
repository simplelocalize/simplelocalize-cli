package io.simplelocalize.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Feel free to use or extend this utility
 */
public class FileReaderUtil {

  private static Logger log = LoggerFactory.getLogger(FileReaderUtil.class);

  private FileReaderUtil() {

  }

  public static List<String> tryReadLines(Path filePath) {
    List<String> fileLines = Collections.emptyList();
    try {
      fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.warn("Cannot read file from path " + filePath.toString(), e);
    }
    return fileLines;
  }

}
