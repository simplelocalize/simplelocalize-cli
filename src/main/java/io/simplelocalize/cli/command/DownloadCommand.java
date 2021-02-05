package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class DownloadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(DownloadCommand.class);

  public void invoke(Configuration configuration)
  {
    Path downloadPath = configuration.getDownloadPath();
    String apiKey = configuration.getApiKey();
    String downloadFormat = configuration.getDownloadFormat();
    String profile = configuration.getProfile();
    String languageKey = configuration.getLanguageKey();

    log.info(" 🌍 Downloading translation files");
    try
    {
      SimpleLocalizeClient client = new SimpleLocalizeClient(apiKey, profile);
      client.downloadFile(downloadPath, downloadFormat, languageKey);
    } catch (InterruptedException | IOException e)
    {
      log.error(" 😝 Translations could not be downloaded", e);
      System.exit(1);
    }

  }
}
