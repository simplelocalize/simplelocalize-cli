package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.command.DownloadCommand;
import io.simplelocalize.cli.command.ExtractCommand;
import io.simplelocalize.cli.command.UploadCommand;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationLoader;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


@Command(
        name = "simplelocalize-cli",
        description =
                {
                        "SimpleLocalize CLI is an official Localization CLI Tool for SimpleLocalize.io Platform",
                        "Usage: 'simplelocalize-cli [command]'",
                        "Visit https://docs.simplelocalize.io to learn more."
                },
        mixinStandardHelpOptions = true,
        version = {
                "SimpleLocalize CLI: " + Version.NUMBER,
                "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"}
)
public class SimplelocalizeCliCommand implements Runnable
{

  private static final Logger log = LoggerFactory.getLogger(SimplelocalizeCliCommand.class);

  @Option(names = {"-c", "--config"}, description = "Configuration file (default: ./simplelocalize.yml)")
  Path configurationFilePath;

  public static void main(String[] args)
  {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  @Command(
          name = "extract",
          description = "Extract translation keys from project files. Use 'simplelocalize-cli extract --help' to learn more about the parameters.")
  public void extract(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--projectType"}, description = "Project type tells CLI how to find i18n keys in your project files") String projectType,
          @Option(names = {"--searchDir"}, description = "(Optional) Search directory tells CLI where to look for project files which may contain translation keys. Default: ./") String searchDirectory
  )
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }
    if (StringUtils.isNotEmpty(projectType))
    {
      configuration.setProjectType(projectType);
    }
    if (StringUtils.isNotEmpty(searchDirectory))
    {
      configuration.setSearchDir(searchDirectory);
    }
    ConfigurationValidator configurationValidator = new ConfigurationValidator();
    configurationValidator.validateDownloadConfiguration(configuration);
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
    ExtractCommand extractCommand = new ExtractCommand(client, configuration);
    extractCommand.invoke();
  }

  @Command(
          name = "sync",
          description = "Synchronize (Upload & Download) translations with SimpleLocalize editor. Use 'simplelocalize-cli sync --help' to learn more about the parameters.")
  public void sync(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--uploadPath"}, description = "Path to file with translation or translation keys to upload. Use '{lang}' to define language key if you are uploading more than one file with translations.") String uploadPath,
          @Option(names = {"--uploadFormat"}, description = "Translations or keys format") String uploadFormat,
          @Option(names = {"--uploadOptions"}, split = ",", description = "(Optional) Read more about 'uploadOptions' param at docs.simplelocalize.io") List<String> uploadOptions,
          @Option(names = {"--downloadPath"}, description = "Directory where translations should be downloaded") String downloadPath,
          @Option(names = {"--downloadFormat"}, description = "Download format for translation file") String downloadFormat,
          @Option(names = {"--downloadOptions"}, split = ",", description = "(Optional) Download options") List<String> downloadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Specify language key for single file upload") String languageKey
  ) throws IOException
  {
    upload(apiKey, uploadPath, uploadFormat, uploadOptions, languageKey);
    download(apiKey, downloadPath, downloadFormat, downloadOptions, languageKey);
  }

  @Command(
          name = "upload",
          description = "Upload translations or translation keys to SimpleLocalize editor. Use 'simplelocalize-cli upload --help' to learn more about the parameters.")
  public void upload(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--uploadPath"}, description = "Path to file with translation or translation keys to upload. Use '{lang}' to define language key if you are uploading more than one file with translations.") String uploadPath,
          @Option(names = {"--uploadFormat"}, description = "Translations or keys format") String uploadFormat,
          @Option(names = {"--uploadOptions"}, split = ",", description = "(Optional) Read more about 'uploadOptions' param at docs.simplelocalize.io") List<String> uploadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Specify language key for single file upload") String languageKey
  ) throws IOException
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }

    if (StringUtils.isNotEmpty(uploadPath))
    {
      configuration.setUploadPath(uploadPath);
    }

    if (StringUtils.isNotEmpty(uploadFormat))
    {
      configuration.setUploadFormat(uploadFormat);
    }

    if (StringUtils.isNotEmpty(languageKey))
    {
      configuration.setLanguageKey(languageKey);
    }

    if (uploadOptions != null)
    {
      configuration.setUploadOptions(uploadOptions);
    }

    ConfigurationValidator configurationValidator = new ConfigurationValidator();
    configurationValidator.validateDownloadConfiguration(configuration);
    UploadCommand uploadCommand = new UploadCommand(configuration);
    uploadCommand.invoke();
  }

  @Command(
          name = "download",
          description = "Download translations in ready to use format for your i18n library. Use 'simplelocalize-cli download --help' to learn more about the parameters.")
  public void download(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--downloadPath"}, description = "Directory where translations should be downloaded") String downloadPath,
          @Option(names = {"--downloadFormat"}, description = "Download format for translation file") String downloadFormat,
          @Option(names = {"--downloadOptions"}, split = ",", description = "(Optional) Download options") List<String> downloadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Setup languageKey parameter to download file with only one language translations") String languageKey
  )
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }
    if (StringUtils.isNotEmpty(downloadPath))
    {
      configuration.setDownloadPath(downloadPath);
    }
    if (StringUtils.isNotEmpty(downloadFormat))
    {
      configuration.setDownloadFormat(downloadFormat);
    }
    if (StringUtils.isNotEmpty(languageKey))
    {
      configuration.setLanguageKey(languageKey);
    }
    if (downloadOptions != null)
    {
      configuration.setDownloadOptions(downloadOptions);
    }
    ConfigurationValidator configurationValidator = new ConfigurationValidator();
    configurationValidator.validateDownloadConfiguration(configuration);
    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
    DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
    downloadCommand.invoke();
  }

  public void run()
  {
    log.warn(" 🤨 Please specify a command. Visit https://simplelocalize.io/docs/cli/get-started/ to learn more.");
  }
}
