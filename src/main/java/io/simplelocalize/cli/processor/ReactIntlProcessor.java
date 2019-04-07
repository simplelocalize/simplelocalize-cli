package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.processor.files.JavascriptFilesFinder;
import io.simplelocalize.cli.processor.keys.ReactIntlKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ReactIntlProcessor implements ProjectProcessor {

  @Override
  public List<Path> findFilesToProcess(Path path) {
    JavascriptFilesFinder javascriptFilesFinder = new JavascriptFilesFinder();
    return javascriptFilesFinder.findFilesToProcess(path);
  }

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {
    ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();
    return reactIntlKeyExtractor.extractKeysFromFile(filePath);
  }

  @Override
  public String getProjectTypeSupport() {
    return "react-intl";
  }
}
