package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.extraction.keys.ReactIntlKeyExtractor;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YahooReactIntlProcessor implements ExtractionProcessor
{


  @Override
  public ExtractionResult process(Path searchDirectory)
  {
    JavaScriptAndTypeScriptFilesFinder javaScriptAndTypeScriptFilesFinder = new JavaScriptAndTypeScriptFilesFinder();
    ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

    List<Path> foundFiles = javaScriptAndTypeScriptFilesFinder.findFilesToProcess(searchDirectory);

    Set<String> keys = new HashSet<>();
    for (Path file : foundFiles)
    {
      Set<String> batchKeys = reactIntlKeyExtractor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }
    return ExtractionResult.of(keys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "yahoo/react-intl";
  }
}
