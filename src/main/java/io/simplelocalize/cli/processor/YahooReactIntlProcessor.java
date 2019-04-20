package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.processor.files.JavascriptFilesFinder;
import io.simplelocalize.cli.processor.keys.ReactIntlKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class YahooReactIntlProcessor implements ProjectProcessor {

  private JavascriptFilesFinder javascriptFilesFinder = new JavascriptFilesFinder();
  private ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Override
  public List<Path> findFilesToProcess(Path path) {
    return javascriptFilesFinder.findFilesToProcess(path);
  }

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {
    return reactIntlKeyExtractor.extractKeysFromFile(filePath);
  }

  @Override
  public String getProjectTypeSupport() {
    return "yahoo/react-intl";
  }
}
