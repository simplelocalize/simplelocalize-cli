package io.simplelocalize.cli;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationLoader;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.ProjectProcessor;
import io.simplelocalize.cli.processor.ProjectProcessorFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Command(name = "simplelocalize-cli", description = "SimpleLocalize CLI finds all i18n keys in your source code and sends it to the SimpleLocalize Cloud where you can easily translate, and manage them.", mixinStandardHelpOptions = true)
public class SimplelocalizeCliCommand implements Runnable {

  private static final String DEFAULT_CONFIG_FILE_NAME = "./simplelocalize.yml";
  private static final int BATCH_SIZE = 1000;

  private final Logger log = LoggerFactory.getLogger(SimplelocalizeCliCommand.class);
  @Option(names = {"-c", "--config"}, description = "Configuration file path (default: ./simplelocalize.yml)")
  String configurationFilePath;

  @Option(names = {"--analysis"}, description = "Run project analysis")
  boolean analysisEnabled;

  @Option(names = {"--failOnGate"}, description = "Fail build if gate will fail")
  boolean failOnGate;

  @Option(names = {"--disableExtraction"}, description = "Turn off keys extraction")
  boolean extractionDisabled;

  @Option(names = {"--apiKey"}, description = "Project API Key")
  String apiKey;

  @Option(names = {"--projectType"}, description = "Project Type")
  String projectType;

  @Option(names = {"--searchDir"}, description = "Search directory")
  String searchDirectory;

  @Option(names = {"--profile"}, description = "Validation profile")
  String profile;

  public static void main(String[] args) {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  public void run() {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    ConfigurationValidator configurationValidator = new ConfigurationValidator();

    if (Strings.isNullOrEmpty(configurationFilePath)) {
      configurationFilePath = DEFAULT_CONFIG_FILE_NAME;
    }


    Configuration configuration = configurationLoader.load(configurationFilePath);
    if (StringUtils.isNotEmpty(apiKey)) {
      configuration.setApiKey(apiKey);
    }

    if (StringUtils.isNotEmpty(projectType)) {
      configuration.setProjectType(projectType);
    }

    if (StringUtils.isNotEmpty(searchDirectory)) {
      configuration.setSearchDir(searchDirectory);
    }
    configurationValidator.validate(configuration);

    String projectType = configuration.getProjectType();
    String searchDir = configuration.getSearchDir();


    String apiKey = configuration.getApiKey();
    SimpleLocalizeClient client = new SimpleLocalizeClient(apiKey, profile);

    boolean extractionEnabled = !extractionDisabled;
    if (extractionEnabled) {
      log.info("Running keys extraction");
      ProjectProcessor projectProcessor = ProjectProcessorFactory.createForType(projectType);
      ProcessResult result = projectProcessor.process(Paths.get(searchDir));

      Set<String> keys = result.getKeys();
      List<Path> processedFiles = result.getProcessedFiles();
      log.info("Found {} unique keys in {} components", keys.size(), processedFiles.size());

      Set<String> ignoredKeys = configuration.getIgnoredKeys();
      keys.removeAll(ignoredKeys);

      for (List<String> partition : Iterables.partition(keys, BATCH_SIZE)) {
        try {
          client.sendKeys(partition);
        } catch (Exception e) {
          log.error("Could not send keys chunk. Contact support: contact@simplelocalize.io", e);
        }
      }
    }

    if (analysisEnabled) {
      log.info("Running project analysis");
      try {
        int status = client.runAnalysis();
        if (failOnGate) {
          System.exit(status);
        }
      } catch (Exception e) {
        log.error("Project could not be analyzed. Contact support: contact@simplelocalize.io");
      }
    }

  }
}
