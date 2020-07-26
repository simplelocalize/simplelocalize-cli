package io.simplelocalize.cli;

import com.google.common.base.Strings;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationLoader;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.ProjectProcessor;
import io.simplelocalize.cli.processor.ProjectProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Command(name = "simplelocalize-cli", description = "...", mixinStandardHelpOptions = true)
public class SimplelocalizeCliCommand implements Runnable {

  private static final String DEFAULT_CONFIG_FILE_NAME = "./simplelocalize.yml";
  @CommandLine.Option(names = {"-c", "--config"}, description = "Configuration file")
  String configurationFilePath;
  private Logger log = LoggerFactory.getLogger(SimplelocalizeCliCommand.class);
  private ConfigurationLoader configurationLoader;
  private ConfigurationValidator configurationValidator;

  public static void main(String[] args) throws Exception {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  public void run() {
    this.configurationLoader = new ConfigurationLoader();
    this.configurationValidator = new ConfigurationValidator();

    if (Strings.isNullOrEmpty(configurationFilePath)) {
      configurationFilePath = DEFAULT_CONFIG_FILE_NAME;
    }

    Configuration configuration = configurationLoader.load(configurationFilePath);
    configurationValidator.validate(configuration);

    String projectType = configuration.getProjectType();
    String searchDir = configuration.getSearchDir();

    ProjectProcessor projectProcessor = ProjectProcessorFactory.createForType(projectType);
    ProcessResult result = projectProcessor.process(Paths.get(searchDir));

    Set<String> keys = result.getKeys();
    List<Path> processedFiles = result.getProcessedFiles();
    log.info("Found {} unique keys in {} components", keys.size(), processedFiles.size());

    Set<String> ignoredKeys = configuration.getIgnoredKeys();
    keys.removeAll(ignoredKeys);

    String uploadToken = configuration.getUploadToken();
    SimpleLocalizeClient client = new SimpleLocalizeClient(uploadToken);
    try {
      client.sendKeys(keys);
    } catch (Exception e) {
      log.error("Could not send keys", e);
    }
  }

}
