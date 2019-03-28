package io.simplelocalize.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FileReaderUtil {

  private FileReaderUtil() {

  }

  private static Logger log = LoggerFactory.getLogger(FileReaderUtil.class);

  public static List<String> tryReadLines(Path componentPath) {
    List<String> componentLines = Collections.emptyList();
    try {
      componentLines = Files.readAllLines(componentPath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.warn("Cannot read file from path " + componentPath.toString(), e);
    }
    return componentLines;
  }

}
