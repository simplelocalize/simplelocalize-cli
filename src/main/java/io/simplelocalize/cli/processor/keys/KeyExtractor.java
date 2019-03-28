package io.simplelocalize.cli.processor.keys;

import java.util.List;
import java.util.Set;

public interface KeyExtractor {

  Set<String> extractKeysFromLines(List<String> filePaths);
}
