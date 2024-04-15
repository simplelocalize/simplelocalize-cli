package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.extraction.keys.ReactIntlKeyExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class YahooReactIntlProcessor implements ExtractionProcessor
{

  @Override
  public List<ExtractionResult> process(Path searchDirectory, List<String> ignorePaths)
  {
    JavaScriptAndTypeScriptFilesFinder javaScriptAndTypeScriptFilesFinder = new JavaScriptAndTypeScriptFilesFinder(new BaseExtensionFilesFinder(ignorePaths));
    ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();
    List<Path> foundFiles = javaScriptAndTypeScriptFilesFinder.findFilesToProcess(searchDirectory);

    List<ExtractionResult> output = new ArrayList<>();
    for (Path file : foundFiles)
    {
      Set<String> batchKeys = reactIntlKeyExtractor.extractKeysFromFile(file);
      output.addAll(ExtractionResult.fromCollection(batchKeys, file));
    }
    return output;
  }

  @Override
  public String getExtractTypeSupport()
  {
    return "yahoo/react-intl";
  }
}
