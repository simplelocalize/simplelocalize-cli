package io.simplelocalize.cli.processor;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class ProjectProcessorFacade {

  private final String projectType;

  private Logger log = LoggerFactory.getLogger(ProjectProcessorFacade.class);

  public ProjectProcessorFacade(String projectType) {
    this.projectType = projectType;
  }

  public ProcessResult process(String searchDir) {
    ProjectProcessor projectProcessor = ProjectProcessorFactory.createForType(projectType);
    List<Path> foundFiles = projectProcessor.findFilesToProcess(Paths.get(searchDir));

    Set<String> keys = Sets.newHashSet();
    for (Path file : foundFiles) {
      Set<String> batchKeys = projectProcessor.extractKeysFromFile(file);
      keys.addAll(batchKeys);
    }

    keys.stream().sorted().forEach(s -> log.debug(s));

    ProcessResult processResult = new ProcessResult();
    processResult.setKeys(keys);
    processResult.setProcessedFiles(foundFiles);
    return processResult;
  }
}
