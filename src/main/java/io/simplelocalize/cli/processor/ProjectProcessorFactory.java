package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.exception.NoProcessorMatchException;
import io.simplelocalize.cli.util.ReflectionLoader;

import java.util.Objects;
import java.util.Set;

public class ProjectProcessorFactory {
  private ProjectProcessorFactory() {
  }

  static ProjectProcessor createForType(String projectType) {
    Objects.requireNonNull(projectType, "Could not create ProjectProcessor for null project type");

    Set<ProjectProcessor> processors = ReflectionLoader.loadProcessors();

    for (ProjectProcessor processor : processors) {
      String projectTypeSupport = processor.getProjectTypeSupport();
      boolean isSupportsProjectType = projectTypeSupport.equalsIgnoreCase(projectType);
      if (isSupportsProjectType) {
        return processor;
      }
    }
    throw new NoProcessorMatchException("Could not find matching project processor for type: " + projectType);
  }


}
