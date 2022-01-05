package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.configuration.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class DownloadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(DownloadCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;
  private final ConfigurationValidator configurationValidator;

  public DownloadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration);
    this.configurationValidator = new ConfigurationValidator();
  }

  public void invoke()
  {
    configurationValidator.validateDownloadConfiguration(configuration);

    String downloadPath = configuration.getDownloadPath();
    String downloadFormat = configuration.getDownloadFormat();
    String languageKey = configuration.getLanguageKey();
    Set<String> downloadOptions = configuration.getDownloadOptions();

    log.info(" 🌍 Downloading translation files");
    try
    {
      if (isMultiFileDownload(downloadOptions))
      {
        client.downloadMultiFile(downloadPath, downloadFormat, downloadOptions);
      } else
      {
        client.downloadFile(downloadPath, downloadFormat, languageKey, downloadOptions);
      }
    } catch (InterruptedException e)
    {
      log.error(" 😝 Translations could not be downloaded", e);
      Thread.currentThread().interrupt();
    } catch (IOException e)
    {
      log.error(" 😝 Translations could not be downloaded", e);
      System.exit(1);
    }
  }

  private boolean isMultiFileDownload(Set<String> downloadOptions)
  {
    return downloadOptions.contains(Options.MULTI_FILE.name());
  }
}
