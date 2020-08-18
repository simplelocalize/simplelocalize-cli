package io.simplelocalize.cli.processor.processor;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.ProjectProcessor;
import io.simplelocalize.cli.processor.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.processor.keys.IEighteenNextKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class IEighteenNextProcessor implements ProjectProcessor {

  @Override
  public ProcessResult process(Path searchDirectory) {
    JavaScriptAndTypeScriptFilesFinder filesFinder = new JavaScriptAndTypeScriptFilesFinder();
    IEighteenNextKeyExtractor keyExtractor = new IEighteenNextKeyExtractor();

    List<Path> foundFiles = filesFinder.findFilesToProcess(searchDirectory);

    Set<String> keys = Sets.newHashSet();
    for (Path file : foundFiles) {
      Set<String> batchKeys = keyExtractor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }
    return ProcessResult.of(keys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "i18next/i18next";
  }
}
