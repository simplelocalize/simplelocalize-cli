package io.simplelocalize.cli.extraction.processor;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.extraction.keys.IEighteenNextKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class IEighteenNextProcessor implements ExtractionProcessor
{

  @Override
  public ExtractionResult process(Path searchDirectory)
  {
    JavaScriptAndTypeScriptFilesFinder filesFinder = new JavaScriptAndTypeScriptFilesFinder();
    IEighteenNextKeyExtractor keyExtractor = new IEighteenNextKeyExtractor();

    List<Path> foundFiles = filesFinder.findFilesToProcess(searchDirectory);

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
    return "i18next/i18next";
  }
}
