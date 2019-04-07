package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.processor.files.FilesFinder;
import io.simplelocalize.cli.processor.keys.KeyExtractor;

public interface ProjectProcessor extends KeyExtractor, FilesFinder {

  String getProjectTypeSupport();
}
