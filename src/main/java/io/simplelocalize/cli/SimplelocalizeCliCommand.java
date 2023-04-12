package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.command.*;
import io.simplelocalize.cli.configuration.AutoTranslationConfiguration;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationLoader;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.util.List;


@Command(
        name = "simplelocalize-cli",
        description =
                {
                        "SimpleLocalize CLI is an official command-line tool for SimpleLocalize platform.",
                        "Usage: 'simplelocalize [command]'",
                        "Visit https://simplelocalize.io/docs/ to learn more."
                },
        mixinStandardHelpOptions = true,
        version = {
                "Current version: " + Version.NUMBER + " (${os.name} ${os.version} ${os.arch})\nReleases: https://github.com/simplelocalize/simplelocalize-cli/releases"
        }
)
public class SimplelocalizeCliCommand implements Runnable
{

  private static final Logger log = LoggerFactory.getLogger(SimplelocalizeCliCommand.class);

  @Option(names = {"-c", "--config"}, description = "Configuration file (default: simplelocalize.yml)")
  Path configurationFilePath;

  @Option(names = {"--debug"}, description = "Debug mode", defaultValue = "false")
  boolean debug;

  public static void main(String[] args)
  {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  @Command(
          name = "extract",
          description = "Extract translation keys from project files. Use 'simplelocalize extract --help' to learn more about the parameters.")
  public void extract(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--projectType"}, description = "Project type tells CLI how to find i18n keys in your project files") String projectType,
          @Option(names = {"--searchDir"}, description = "(Optional) Search directory tells CLI where to look for project files which may contain translation keys. Default: ./") String searchDirectory,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }
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
      if (StringUtils.isNotEmpty(searchDirectory))
      {
        configuration.setSearchDir(searchDirectory);
      }
      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateExtractConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      ExtractCommand extractCommand = new ExtractCommand(client, configuration);
      extractCommand.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "sync",
          description = "Synchronize (Upload & Download) translations with SimpleLocalize editor. Use 'simplelocalize sync --help' to learn more about the parameters.")
  public void sync(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--uploadPath"}, description = "Path to file with translation or translation keys to upload. Use '{lang}' to define language key if you are uploading more than one file with translations.") String uploadPath,
          @Option(names = {"--uploadFormat"}, description = "Translations file format") String uploadFormat,
          @Option(names = {"--uploadOptions"}, split = ",", description = "(Optional) Upload options") List<String> uploadOptions,
          @Option(names = {"--downloadPath"}, description = "Directory where translations should be downloaded") String downloadPath,
          @Option(names = {"--downloadFormat"}, description = "Download format for translation file") String downloadFormat,
          @Option(names = {"--downloadOptions"}, split = ",", description = "(Optional) Download options") List<String> downloadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Specify language key for single file upload") String languageKey,
          @Option(names = {"--customerId"}, description = "(Optional) Download translations for given customerId") String customerId,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl

  )
  {
    upload(apiKey, uploadPath, uploadFormat, false, false, false, uploadOptions, languageKey, customerId, baseUrl);
    download(apiKey, downloadPath, downloadFormat, downloadOptions, languageKey, customerId, baseUrl);
  }

  @Command(
          name = "upload",
          description = "Upload translations or translation keys to SimpleLocalize editor. Use 'simplelocalize upload --help' to learn more about the parameters.")
  public void upload(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--uploadPath"}, description = "Path to file with translation or translation keys to upload. Use '{lang}' to define language key if you are uploading more than one file with translations.") String uploadPath,
          @Option(names = {"--uploadFormat"}, description = "Translations file format") String uploadFormat,
          @Option(names = {"--overwrite"}, description = "(Optional) Overwrite existing translations", defaultValue = "false") Boolean overwrite,
          @Option(names = {"--delete"}, description = "(Optional) Delete translations which are not present in uploaded file", defaultValue = "false") Boolean delete,
          @Option(names = {"--dryRun"}, description = "(Optional) Dry run mode. Do not upload anything to SimpleLocalize", defaultValue = "false") Boolean dryRun,
          @Option(names = {"--uploadOptions"}, split = ",", description = "(Optional) Upload options") List<String> uploadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Specify language key for single file upload") String languageKey,
          @Option(names = {"--customerId"}, description = "(Optional) Upload translations for given customerId") String customerId,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

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

      if (StringUtils.isNotEmpty(customerId))
      {
        configuration.setCustomerId(customerId);
      }

      if (Boolean.TRUE.equals(overwrite))
      {
        uploadOptions.add("REPLACE_TRANSLATION_IF_FOUND");
      }

      if (Boolean.TRUE.equals(delete))
      {
        uploadOptions.add("DELETE_NOT_PRESENT_KEYS");
      }

      if (uploadOptions != null)
      {
        configuration.setUploadOptions(uploadOptions);
      }

      if (Boolean.TRUE.equals(dryRun))
      {
        configuration.setDryRun(true);
      }

      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateUploadConfiguration(configuration);

      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      UploadCommand uploadCommand = new UploadCommand(client, configuration);
      uploadCommand.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "download",
          description = "Download translations in ready to use format for your i18n library. Use 'simplelocalize download --help' to learn more about the parameters.")
  public void download(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--downloadPath"}, description = "Directory where translations should be downloaded") String downloadPath,
          @Option(names = {"--downloadFormat"}, description = "Translations file format") String downloadFormat,
          @Option(names = {"--downloadOptions"}, split = ",", description = "(Optional) Download options") List<String> downloadOptions,
          @Option(names = {"--languageKey"}, description = "(Optional) Setup languageKey parameter to download file with only one language translations") String languageKey,
          @Option(names = {"--customerId"}, description = "(Optional) Download translations for given customerId") String customerId,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

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
      if (StringUtils.isNotEmpty(customerId))
      {
        configuration.setCustomerId(customerId);
      }
      if (downloadOptions != null)
      {
        configuration.setDownloadOptions(downloadOptions);
      }
      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateDownloadConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      DownloadCommand downloadCommand = new DownloadCommand(client, configuration);
      downloadCommand.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "pull",
          description = "Pull translations from Translation Hosting Use 'simplelocalize pull --help' to learn more about the parameters.")
  public void pull(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--pullPath"}, description = "Directory where translations should be saved") String pullPath,
          @Option(names = {"--environment"}, description = "Translation Hosting environment ('latest' or 'production')") String environment,
          @Option(names = {"--filterRegex"}, description = "(Optional) Filter which resources should be downloaded") String filterRegex,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

      if (StringUtils.isNotEmpty(apiKey))
      {
        configuration.setApiKey(apiKey);
      }

      if (StringUtils.isNotEmpty(environment))
      {
        configuration.setEnvironment(environment);
      }

      if (StringUtils.isNotEmpty(pullPath))
      {
        configuration.setPullPath(pullPath);
      }

      if(StringUtils.isNotEmpty(filterRegex))
      {
        configuration.setFilterRegex(filterRegex);
      }

      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateHostingPullConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      PullHostingCommand command = new PullHostingCommand(client, configuration);
      command.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "auto-translate",
          description = "Start auto-translation. 'simplelocalize auto-translate --help' to learn more about the parameters.")
  public void startAutoTranslation(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--languageKeys"}, description = "(Optional) Project language keys to auto-translate", split = ",") List<String> languageKeys,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

      if (StringUtils.isNotEmpty(apiKey))
      {
        configuration.setApiKey(apiKey);
      }

      if (languageKeys != null)
      {
        AutoTranslationConfiguration autoTranslationConfiguration = new AutoTranslationConfiguration();
        autoTranslationConfiguration.setLanguageKeys(languageKeys);
        configuration.setAutoTranslation(autoTranslationConfiguration);
      }

      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateHostingPullConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      AutoTranslationCommand command = new AutoTranslationCommand(client, configuration);
      command.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "status",
          description = "Get project status. Use 'simplelocalize status --help' to learn more about the parameters.")
  public void status(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

      if (StringUtils.isNotEmpty(apiKey))
      {
        configuration.setApiKey(apiKey);
      }

      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateGetStatusConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      StatusCommand command = new StatusCommand(client);
      command.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  @Command(
          name = "publish",
          description = "Publish translations to Translation Hosting. Use 'simplelocalize publish --help' to learn more about the parameters.")
  public void publish(
          @Option(names = {"--apiKey"}, description = "Project API Key") String apiKey,
          @Option(names = {"--environment"}, description = "Translation Hosting environment to which you want to publish changes. Publishing changes to 'production' environment is always preceded by publishing to 'latest' environment.") String environment,
          @Option(names = {"--baseUrl"}, description = "(Optional) Set custom server URL") String baseUrl
  )
  {
    try
    {
      ConfigurationLoader configurationLoader = new ConfigurationLoader();
      Configuration configuration = configurationLoader.loadOrGetDefault(configurationFilePath);
      if (StringUtils.isNotEmpty(baseUrl))
      {
        configuration.setBaseUrl(baseUrl);
      }

      if (StringUtils.isNotEmpty(apiKey))
      {
        configuration.setApiKey(apiKey);
      }
      if (StringUtils.isNotEmpty(environment))
      {
        configuration.setEnvironment(environment);
      }

      ConfigurationValidator configurationValidator = new ConfigurationValidator();
      configurationValidator.validateHostingPublishConfiguration(configuration);
      SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
      PublishHostingCommand command = new PublishHostingCommand(client, configuration);
      command.invoke();
    } catch (Exception e)
    {
      printDebug(e);
      System.exit(CommandLine.ExitCode.USAGE);
    }
  }

  private void printDebug(Exception e)
  {
    if (debug)
    {
      log.error("Command failed.", e);
    } else
    {
      log.error("Command failed. Use '--debug' parameter before the command name to see stacktrace.");
    }
  }


  public void run()
  {
    log.warn("Please specify a command. Visit https://simplelocalize.io/docs/cli/get-started/ to learn more.");
    System.exit(CommandLine.ExitCode.USAGE);
  }
}
