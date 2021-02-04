package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.simplelocalize.cli.command.DownloadCommand;
import io.simplelocalize.cli.command.ExtractCommand;
import io.simplelocalize.cli.command.UploadCommand;
import io.simplelocalize.cli.command.ValidateCommand;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Path;

@Command(
        name = "simplelocalize-cli",
        description =
                {
                        "SimpleLocalize CLI can",
                        "-> 'extract' i18n keys from project files",
                        "-> 'upload' translations or translation keys",
                        "-> 'download' ready to use translation files",
                        "",
                        "Use 'simplelocalize-cli [command]' to use one of above commands",
                        "Check https://docs.simplelocalize.io to learn more."
                },
        mixinStandardHelpOptions = true,
        version = {
                "SimpleLocalize CLI: 1.0.1",
                "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"}
)
public class SimplelocalizeCliCommand implements Runnable
{
  private static final Logger log = LoggerFactory.getLogger(SimplelocalizeCliCommand.class);

  @Option(names = {"-c", "--config"}, description = "Configuration file path (default: ./simplelocalize.yml)")
  Path configurationFilePath;

  public static void main(String[] args)
  {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  @Command(name = "extract", description = "Extract translation keys from project files.")
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
    ExtractCommand extractCommand = new ExtractCommand();
    extractCommand.invoke(configuration);
  }

  @Command(name = "upload", description = "Upload translations or translation keys to SimpleLocalize editor. Use '--uploadFormat' to setup file format.")
  public void upload(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--uploadPath"}, description = "Path to file with translation or translation keys to upload. Use '{lang}' to define language key if you are uploading more than one file with translations.") Path uploadPath,
          @Option(names = {"--uploadFormat"}, description = "Translations or keys format") String uploadFormat,
          @Option(names = {"--uploadLanguageKey"}, description = "(Optional) If you upload one file you can specify language key for it") String uploadLanguageKey,
          @Option(names = {"--uploadOptions"}, description = "(Optional) Read more about 'uploadOptions' param at docs.simplelocalize.io") String uploadOptions
  ) throws IOException
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }
    if (uploadPath != null)
    {
      configuration.setUploadPath(uploadPath);
    }
    if (StringUtils.isNotEmpty(uploadFormat))
    {
      configuration.setUploadFormat(uploadFormat);
    }
    if (StringUtils.isNotEmpty(uploadLanguageKey))
    {
      configuration.setUploadLanguageKey(uploadLanguageKey);
    }
    if (StringUtils.isNotEmpty(uploadOptions))
    {
      configuration.setUploadOptions(uploadOptions);
    }

    UploadCommand uploadCommand = new UploadCommand();
    uploadCommand.invoke(configuration);
  }

  @Command(name = "download", description = "Download translations in ready to use format for your i18n library. Use '--downloadFormat' to setup file format.")
  public void download(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--downloadPath"}, description = "Directory where translations should be downloaded") Path downloadPath,
          @Option(names = {"--downloadFormat"}, description = "Download format for translation file") String downloadFormat
  )
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }
    if (downloadPath != null)
    {
      configuration.setDownloadPath(downloadPath);
    }
    if (StringUtils.isNotEmpty(downloadFormat))
    {
      configuration.setDownloadFormat(downloadFormat);
    }
    DownloadCommand downloadCommand = new DownloadCommand();
    downloadCommand.invoke(configuration);
  }

  @Command(name = "validate", hidden = true)
  public void validate(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey
  )
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    if (StringUtils.isNotEmpty(apiKey))
    {
      configuration.setApiKey(apiKey);
    }
    ValidateCommand validateCommand = new ValidateCommand();
    validateCommand.invoke(configuration);
  }

  public void run()
  {
    log.warn("You are running CLI without specifying a command. We will run 'extract' command as a default but please adjust your configuration to invoke some command explicitly. Learn more https://docs.simplelocalize.io");

    ConfigurationLoader configurationLoader = new ConfigurationLoader();
    Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);

    extract(configuration.getApiKey(), configuration.getProjectType(), configuration.getSearchDir());
  }


}
