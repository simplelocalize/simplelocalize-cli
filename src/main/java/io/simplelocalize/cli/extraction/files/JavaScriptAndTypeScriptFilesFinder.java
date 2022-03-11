package io.simplelocalize.cli.extraction.files;

import io.simplelocalize.cli.exception.ProjectProcessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaScriptAndTypeScriptFilesFinder implements FilesFinder
{

  private static final String TS_TSX_JS_JSX = ".*\\.(t|j)s.?$";

  @Override
  public List<Path> findFilesToProcess(Path path)
  {
    try (Stream<Path> walk = Files.walk(path))
    {
      return walk
              .filter(Files::isRegularFile)
              .filter(isNotNodeModule())
              .filter(isJavaScriptOrTypeScriptFile())
              .collect(Collectors.toList());
    } catch (IOException e)
    {
      throw new ProjectProcessException("Could not process files in path: " + path, e);
    }
  }



  private Predicate<? super Path> isNotNodeModule() {
    return filePath -> {
      String filename = filePath.toString();
      return !filename.contains("node_modules");
    };
  }

  private Predicate<Path> isJavaScriptOrTypeScriptFile()
  {
    return filePath -> {
      String filename = filePath.toString();

      return filename.matches(TS_TSX_JS_JSX);
    };
  }
}
