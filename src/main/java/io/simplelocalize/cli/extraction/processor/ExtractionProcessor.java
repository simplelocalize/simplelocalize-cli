package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;

import java.nio.file.Path;

public interface ExtractionProcessor
{

  ExtractionResult process(Path searchDirectory);

  String getProjectTypeSupport();
}
