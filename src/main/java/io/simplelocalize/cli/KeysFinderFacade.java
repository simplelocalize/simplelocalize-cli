package io.simplelocalize.cli;

import io.simplelocalize.cli.processor.files.FilesFinder;
import io.simplelocalize.cli.processor.files.JavascriptFilesFinder;
import io.simplelocalize.cli.processor.keys.KeyExtractor;
import io.simplelocalize.cli.processor.keys.ReactIntlKeyExtractor;
import io.simplelocalize.cli.util.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class KeysFinderFacade {
  private KeysFinderFacade() {
  }

  private static Logger log = LoggerFactory.getLogger(KeysFinderFacade.class);


  public static Set<String> invoke(String path) throws IOException {
    FilesFinder fileFinder = new JavascriptFilesFinder();
    List<Path> componentPaths = fileFinder.findFiles(Paths.get(path));
    KeyExtractor keyExtractor = new ReactIntlKeyExtractor();

    List<String> allComponentsLines = componentPaths.stream()
            .map(FileReaderUtil::tryReadLines)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

    Set<String> keys = keyExtractor.extractKeysFromLines(allComponentsLines);

    keys.stream().sorted().forEach(s -> log.debug(s));
    log.info("Found {} unique keys in {} components", keys.size(), componentPaths.size());
    return keys;
  }

}
