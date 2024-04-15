package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.DefaultKeysExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class DataKeyAttributeProcessor implements ExtractionProcessor
{

  @Override
  public List<ExtractionResult> process(Path inputPath, List<String> ignorePaths)
  {
    BaseExtensionFilesFinder filesFinder = new BaseExtensionFilesFinder(ignorePaths);
    List<Path> foundFiles = filesFinder.findFiles(inputPath);
    List<ExtractionResult> output = new ArrayList<>();
    for (Path file : foundFiles)
    {
      Set<String> batchKeys = DefaultKeysExtractor.extractValuesByPattern(file, Pattern.compile("(?<=data-i18n-key=[\"|'])(.*?)(?=[\"|'])"));
      output.addAll(ExtractionResult.fromCollection(batchKeys, file));
    }
    return output;
  }

  @Override
  public String getExtractTypeSupport()
  {
    return "simplelocalize/data-i18n-key";
  }
}
