package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.configuration.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.aDownloadRequest;

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
    List<String> downloadOptions = configuration.getDownloadOptions();

    log.info(" 🌍 Downloading translation files");

    DownloadRequest downloadRequest = aDownloadRequest()
            .withPath(downloadPath)
            .withFormat(downloadFormat)
            .withOptions(downloadOptions)
            .withLanguageKey(languageKey)
            .build();

    try
    {
      if (isMultiFileDownload(downloadOptions))
      {
        client.downloadMultiFile(downloadRequest);
      } else
      {
        client.downloadFile(downloadRequest);
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

  private boolean isMultiFileDownload(List<String> downloadOptions)
  {
    return downloadOptions.contains(Options.MULTI_FILE.name());
  }
}
