package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public class DownloadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(DownloadCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public DownloadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration);
  }

  public void invoke()
  {
    Path downloadPath = configuration.getDownloadPath();
    String downloadFormat = configuration.getDownloadFormat();
    String languageKey = configuration.getLanguageKey();
    String downloadOptions = configuration.getDownloadOptions();

    log.info(" 🌍 Downloading translation files");
    try
    {
      if (isMultiFileDownload(downloadOptions))
      {
        client.downloadMultiFile(downloadPath, downloadFormat, languageKey);
      } else
      {
        client.downloadFile(downloadPath, downloadFormat, languageKey);
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

  private boolean isMultiFileDownload(String downloadOptions)
  {
    if (StringUtils.isEmpty(downloadOptions))
    {
      return false;
    }
    return downloadOptions.toUpperCase(Locale.ROOT).contains("MULTI_FILE");
  }
}
