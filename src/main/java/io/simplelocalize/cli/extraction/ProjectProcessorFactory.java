package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.exception.NoProcessorMatchException;
import io.simplelocalize.cli.extraction.processor.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ProjectProcessorFactory
{

  public ProjectProcessorFactory()
  {
  }

  public ExtractionProcessor createForType(String projectType)
  {
    Objects.requireNonNull(projectType, "Could not create ProjectProcessor for null project type");

    Set<ExtractionProcessor> processors = Set.of(
            new YahooReactIntlProcessor(),
            new AndroidProcessor(),
            new iOSProcessor(),
            new EjsProcessor(),
            new IEighteenNextProcessor()
    );

    List<String> supportedProjectTypesList = processors.stream().map(ExtractionProcessor::getProjectTypeSupport).collect(Collectors.toList());
    String supportedProjectTypes = String.join(",", supportedProjectTypesList);

    for (ExtractionProcessor processor : processors)
    {
      String projectTypeSupport = processor.getProjectTypeSupport();
      boolean isSupportsProjectType = projectTypeSupport.equalsIgnoreCase(projectType);
      if (isSupportsProjectType)
      {
        return processor;
      }
    }

    throw new NoProcessorMatchException("Could not find matching project processor for type: " + projectType + " please use on of these: " + supportedProjectTypes);
  }


}
