package io.simplelocalize.cli.processor;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.processor.files.JavaScriptAndTypeScriptFilesFinder;
import io.simplelocalize.cli.processor.keys.ReactIntlKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class YahooReactIntlProcessor implements ProjectProcessor {


  @Override
  public ProcessResult process(Path searchDirectory) {
    JavaScriptAndTypeScriptFilesFinder javaScriptAndTypeScriptFilesFinder = new JavaScriptAndTypeScriptFilesFinder();
    ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

    List<Path> foundFiles = javaScriptAndTypeScriptFilesFinder.findFilesToProcess(searchDirectory);

    Set<String> keys = Sets.newHashSet();
    for (Path file : foundFiles) {
      Set<String> batchKeys = reactIntlKeyExtractor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }
    return ProcessResult.of(keys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "yahoo/react-intl";
  }
}
