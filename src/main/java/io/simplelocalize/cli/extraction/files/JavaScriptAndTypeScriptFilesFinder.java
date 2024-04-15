package io.simplelocalize.cli.extraction.files;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class JavaScriptAndTypeScriptFilesFinder implements FilesFinder
{

  private static final String TS_TSX_JS_JSX = ".*\\.(t|j)s.?$";

  private final BaseExtensionFilesFinder baseExtensionFilesFinder;

  public JavaScriptAndTypeScriptFilesFinder(BaseExtensionFilesFinder baseExtensionFilesFinder)
  {
    this.baseExtensionFilesFinder = baseExtensionFilesFinder;
  }

  @Override
  public List<Path> findFilesToProcess(Path path)
  {
    return baseExtensionFilesFinder.findFiles(path)
            .stream()
            .filter(isJavaScriptOrTypeScriptFile())
            .filter(isNotNodeModule())
            .toList();
  }


  private Predicate<Path> isNotNodeModule()
  {
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
