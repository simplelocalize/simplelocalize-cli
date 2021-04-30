package io.simplelocalize.cli.extraction.keys;

import java.nio.file.Path;
import java.util.Set;

public interface KeyExtractor {

  Set<String> extractKeysFromFile(Path filePath);
}
