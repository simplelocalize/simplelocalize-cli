package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.DefaultKeysExtractor;
import io.simplelocalize.cli.util.ListsUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class AndroidProcessor implements ExtractionProcessor
{

  @Override
  public List<ExtractionResult> process(Path searchDirectory, List<String> ignorePaths)
  {
    BaseExtensionFilesFinder filesFinder = new BaseExtensionFilesFinder(ignorePaths);
    List<Path> javaFiles = filesFinder.findFilesWithExtension(searchDirectory, ".java");
    List<Path> kotlinFiles = filesFinder.findFilesWithExtension(searchDirectory, ".kt");
    List<Path> combinedLists = ListsUtil.combine(javaFiles, kotlinFiles);

    List<ExtractionResult> output = new ArrayList<>();
    for (Path javaFile : combinedLists)
    {
      Set<String> chunk = DefaultKeysExtractor.extractValuesByPattern(javaFile, Pattern.compile("(?<=R\\.string\\.)(.*?)(?=\\))"));
      output.addAll(ExtractionResult.fromCollection(chunk, javaFile));
    }

    List<Path> xmlFiles = filesFinder.findFilesWithExtension(searchDirectory, ".xml");
    for (Path xmlFile : xmlFiles)
    {
      Set<String> chunk = DefaultKeysExtractor.extractValuesByPattern(xmlFile, Pattern.compile("(?<=android:text=\"@string/)(.*?)(?=\")"));
      output.addAll(ExtractionResult.fromCollection(chunk, xmlFile));
    }

    return output;
  }

  @Override
  public String getExtractTypeSupport()
  {
    return "google/android";
  }
}
