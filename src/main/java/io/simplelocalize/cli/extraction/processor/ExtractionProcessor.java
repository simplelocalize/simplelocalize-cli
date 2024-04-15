package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;

import java.nio.file.Path;
import java.util.List;

public interface ExtractionProcessor
{
  List<ExtractionResult> process(Path inputPath, List<String> ignorePaths);

  String getExtractTypeSupport();
}
