package io.simplelocalize.cli.processor.processor;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.ProjectProcessor;
import io.simplelocalize.cli.processor.files.GenericExtensionFilesFinder;
import io.simplelocalize.cli.processor.keys.EjsKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class EjsProcessor implements ProjectProcessor {

  @Override
  public ProcessResult process(Path searchDirectory) {
    GenericExtensionFilesFinder genericExtensionFilesFinder = new GenericExtensionFilesFinder();
    EjsKeyExtractor keyExtractor = new EjsKeyExtractor();

    List<Path> foundFiles = genericExtensionFilesFinder.findFilesToProcess(searchDirectory, ".ejs");

    Set<String> keys = Sets.newHashSet();
    for (Path file : foundFiles) {
      Set<String> batchKeys = keyExtractor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }
    return ProcessResult.of(keys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "mde/ejs";
  }
}
