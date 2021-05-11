package io.simplelocalize.cli.extraction.processor;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.GenericExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.EjsKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class EjsProcessor implements ExtractionProcessor
{

  @Override
  public ExtractionResult process(Path searchDirectory)
  {
    GenericExtensionFilesFinder genericExtensionFilesFinder = new GenericExtensionFilesFinder();
    EjsKeyExtractor keyExtractor = new EjsKeyExtractor();

    List<Path> foundFiles = genericExtensionFilesFinder.findFilesToProcess(searchDirectory, ".ejs");

    Set<String> keys = Sets.newHashSet();
    for (Path file : foundFiles)
    {
      Set<String> batchKeys = keyExtractor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }
    return ExtractionResult.of(keys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "mde/ejs";
  }
}
