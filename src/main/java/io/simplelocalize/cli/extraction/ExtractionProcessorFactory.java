package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.extraction.processor.*;

import java.util.List;
import java.util.Set;

public final class ExtractionProcessorFactory
{
  public ExtractionProcessor createForType(String extractType)
  {
    Set<ExtractionProcessor> processors = Set.of(
            new YahooReactIntlProcessor(),
            new AndroidProcessor(),
            new AppleProcessor(),
            new EjsProcessor(),
            new IEighteenNextProcessor(),
            new DataKeyAttributeProcessor()
    );
    List<String> supportedProjectTypesList = processors.stream()
            .map(ExtractionProcessor::getExtractTypeSupport)
            .toList();
    String supportedProjectTypes = String.join(",", supportedProjectTypesList);
    for (ExtractionProcessor processor : processors)
    {
      String projectTypeSupport = processor.getExtractTypeSupport();
      if (projectTypeSupport.equalsIgnoreCase(extractType))
      {
        return processor;
      }
    }
    throw new IllegalArgumentException("Could not find matching project processor for extractType: " + extractType + " please use one of these: " + supportedProjectTypes);
  }
}
