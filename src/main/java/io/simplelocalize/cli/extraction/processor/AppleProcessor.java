package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.DefaultKeysExtractor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class AppleProcessor implements ExtractionProcessor
{
  @Override
  public List<ExtractionResult> process(Path searchDirectory, List<String> ignorePaths)
  {
    BaseExtensionFilesFinder filesFinder = new BaseExtensionFilesFinder(ignorePaths);
    List<Path> swiftFiles = filesFinder.findFilesWithExtension(searchDirectory, ".swift");

    List<ExtractionResult> output = new ArrayList<>();
    for (Path path : swiftFiles)
    {
      Set<String> chunk = DefaultKeysExtractor.extractValuesByPattern(path, Pattern.compile("(?<=NSLocalizedString\\(\")(.*?)(?=\")"));
      output.addAll(ExtractionResult.fromCollection(chunk, path));
    }

    return output;
  }

  @Override
  public String getExtractTypeSupport()
  {
    return "apple/ios-macos";
  }
}
