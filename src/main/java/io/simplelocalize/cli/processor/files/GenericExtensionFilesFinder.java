package io.simplelocalize.cli.processor.files;

import io.simplelocalize.cli.exception.ProjectProcessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenericExtensionFilesFinder {

  public List<Path> findFilesToProcess(Path path, String extension) {
    try (Stream<Path> walk = Files.walk(path)) {
      return walk
              .filter(Files::isRegularFile)
              .filter(hasExtension(extension))
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new ProjectProcessException("Could not process files in path: " + path, e);
    }
  }

  private Predicate<Path> hasExtension(String extension) {
    return filePath -> {
      String filename = filePath.toString();
      return filename.endsWith(extension);
    };
  }

}
