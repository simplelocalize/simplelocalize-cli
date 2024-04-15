package io.simplelocalize.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simplelocalize.cli.client.ObjectMapperSingleton;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.extraction.*;
import io.simplelocalize.cli.extraction.processor.ExtractionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(ExtractCommand.class);

  private final Configuration configuration;
  private final ExtractionProcessorFactory extractionProcessorFactory = new ExtractionProcessorFactory();
  private final ExtractionResultMapper extractionResultMapper = new ExtractionResultMapper();

  public ExtractCommand(Configuration configuration)
  {
    this.configuration = configuration;
  }

  public void invoke() throws IOException, InterruptedException
  {
    final String projectType = configuration.getProjectType();
    log.info("Project type: {}", projectType);

    final String searchDir = configuration.getSearchDir();
    log.info("Search directory: {}", searchDir);

    final String outputPath = configuration.getOutputPath();
    log.info("Output path: {}", outputPath);

    final List<String> ignorePaths = configuration.getIgnorePaths();
    log.info("Ignoring paths: {}", ignorePaths);

    final List<String> ignoreKeys = configuration.getIgnoreKeys();
    Set<String> ignoredKeys = new HashSet<>(ignoreKeys);
    log.info("Ignoring keys: {}", ignoredKeys);

    ExtractionProcessor extractionProcessor = extractionProcessorFactory.createForType(projectType);
    Set<ExtractionResult> results = new HashSet<>(extractionProcessor.process(Paths.get(searchDir), ignorePaths));

    Set<String> uniqueKeys = results.stream()
            .map(ExtractionResult::getKey)
            .collect(Collectors.toSet());

    Set<Path> uniqueFiles = results.stream()
            .map(ExtractionResult::getFilePath)
            .collect(Collectors.toSet());

    log.info("Extracted {} unique keys from {} files", uniqueKeys.size(), uniqueFiles.size());

    Set<ExtractionResult> filteredKeys = ExtractionUtils.filterOutIgnoredKeys(results, ignoredKeys);
    Map<String, SimpleLocalizeJsonMetadata> keysWithMetadata = extractionResultMapper.map(filteredKeys);

    log.info("Saving results to: {}", outputPath);
    saveToFile(keysWithMetadata, outputPath);
    log.info("Done!");
  }


  private void saveToFile(Map<String, SimpleLocalizeJsonMetadata> data, String outputPath) throws IOException
  {
    ObjectMapper objectMapperSingleton = ObjectMapperSingleton.getInstance();
    objectMapperSingleton
            .writerWithDefaultPrettyPrinter()
            .writeValue(Paths.get(outputPath).toFile(), data);
  }
}
