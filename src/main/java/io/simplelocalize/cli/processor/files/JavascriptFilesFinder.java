package io.simplelocalize.cli.processor.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JavascriptFilesFinder implements FilesFinder {

  @Override
  public List<Path> findFiles(Path path) throws IOException {
    return Files.walk(path)
            .filter(Files::isRegularFile)
            .filter(isJavaScriptFile())
            .filter(isNotNodeModule())
            .collect(Collectors.toList());
  }


  private Predicate<? super Path> isNotNodeModule() {
    return filePath -> {
      String filename = filePath.toString();
      return !filename.contains("node_modules");
    };
  }

  private Predicate<Path> isJavaScriptFile() {
    return filePath -> {
      String filename = filePath.toString();
      String TS_TSX_JS_JSX = ".*\\.(t|j)s.?$";
      return filename.matches(TS_TSX_JS_JSX);
    };
  }

}
