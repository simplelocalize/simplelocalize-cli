package io.simplelocalize.cli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    Path decodedFilePath = null;
    try {
      decodedFilePath = Paths.get(URLDecoder.decode(String.valueOf(Paths.get(String.valueOf(filePath))), "utf-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    List<String> fileLines = Collections.emptyList();
    try {
      fileLines = Files.readAllLines(decodedFilePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.warn("Cannot read file from path " + decodedFilePath.toString(), e);
    }
    return fileLines;
  }


  public static String tryReadContent(Path filePath) {
    Path decodedFilePath = null;
    try {
      decodedFilePath = Paths.get(URLDecoder.decode(String.valueOf(Paths.get(String.valueOf(filePath))), "utf-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    try {
      return Files.readString(decodedFilePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.warn("Cannot read file from path " + decodedFilePath.toString(), e);
    }
    return "";
  }



}
