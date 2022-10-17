package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.ProjectProcessorFactory;
import io.simplelocalize.cli.extraction.processor.ExtractionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtractCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(ExtractCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public ExtractCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public void invoke()
  {
    String searchDir = configuration.getSearchDir();
    String projectType = configuration.getProjectType();

    log.info("Running keys extraction");
    ProjectProcessorFactory processorFactory = new ProjectProcessorFactory();
    ExtractionProcessor extractionProcessor = processorFactory.createForType(projectType);
    ExtractionResult result = extractionProcessor.process(Paths.get(searchDir));

    Set<String> keys = result.getKeys();
    List<Path> processedFiles = result.getProcessedFiles();
    log.info("Found {} unique keys in {} components", keys.size(), processedFiles.size());

    Set<String> ignoredKeys = new HashSet<>(configuration.getIgnoreKeys());
    keys.removeAll(ignoredKeys);

    try
    {
      client.uploadKeys(keys);
    } catch (Exception e)
    {
      log.error("Keys upload failed. Contact support: contact@simplelocalize.io", e);
      Thread.currentThread().interrupt();
    }
  }
}
