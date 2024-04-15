package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.EjsKeyExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EjsProcessor implements ExtractionProcessor
{

  @Override
  public List<ExtractionResult> process(Path searchDirectory, List<String> ignorePaths)
  {
    BaseExtensionFilesFinder baseExtensionFilesFinder = new BaseExtensionFilesFinder(ignorePaths);
    EjsKeyExtractor keyExtractor = new EjsKeyExtractor();

    List<ExtractionResult> output = new ArrayList<>();
    List<Path> foundFiles = baseExtensionFilesFinder.findFilesWithExtension(searchDirectory, ".ejs");
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
    return "mde/ejs";
  }
}
