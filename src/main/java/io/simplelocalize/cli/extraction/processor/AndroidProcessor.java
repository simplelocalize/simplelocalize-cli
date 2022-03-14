package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.GenericExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.AndroidRStringKeyExtractor;
import io.simplelocalize.cli.extraction.keys.AndroidXmlKeysExtractor;
import io.simplelocalize.cli.util.ListsUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AndroidProcessor implements ExtractionProcessor
{


  @Override
  public ExtractionResult process(Path searchDirectory)
  {

    Set<String> foundKeys = new LinkedHashSet<>();
    List<Path> foundFiles = new ArrayList<>();

    GenericExtensionFilesFinder filesFinder = new GenericExtensionFilesFinder();
    List<Path> javaFiles = filesFinder.findFilesToProcess(searchDirectory, ".java");
    List<Path> kotlinFiles = filesFinder.findFilesToProcess(searchDirectory, ".kt");
    List<Path> combinedLists = ListsUtil.combine(javaFiles, kotlinFiles);
    foundFiles.addAll(combinedLists);

    AndroidRStringKeyExtractor javaKeysExtractor = new AndroidRStringKeyExtractor();
    for (Path javaFile : combinedLists)
    {
      Set<String> chunk = javaKeysExtractor.extractKeysFromFile(javaFile);
      foundKeys.addAll(chunk);
    }

    List<Path> xmlFiles = filesFinder.findFilesToProcess(searchDirectory, ".xml");
    foundFiles.addAll(xmlFiles);
    AndroidXmlKeysExtractor xmlKeysExtractor = new AndroidXmlKeysExtractor();
    for (Path xmlFile : xmlFiles)
    {
      Set<String> chunk = xmlKeysExtractor.extractKeysFromFile(xmlFile);
      foundKeys.addAll(chunk);
    }

    return ExtractionResult.of(foundKeys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "google/android";
  }
}
