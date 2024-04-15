package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.extraction.keys.IEighteenNextKeyExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IEighteenNextProcessor implements ExtractionProcessor
{

  @Override
  public List<ExtractionResult> process(Path searchDirectory, List<String> ignorePaths)
  {
    JavaScriptAndTypeScriptFilesFinder filesFinder = new JavaScriptAndTypeScriptFilesFinder(new BaseExtensionFilesFinder(ignorePaths));
    IEighteenNextKeyExtractor keyExtractor = new IEighteenNextKeyExtractor();

    List<Path> foundFiles = filesFinder.findFilesToProcess(searchDirectory);
    List<ExtractionResult> output = new ArrayList<>();
    for (Path file : foundFiles)
    {
      Set<String> batchKeys = keyExtractor.extractKeysFromFile(file);
      output.addAll(ExtractionResult.fromCollection(batchKeys, file));
    }
    return output;
  }

  @Override
  public String getExtractTypeSupport()
  {
    return "i18next/i18next";
  }
}
