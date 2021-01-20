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

    log.info("Download translation files");

    boolean isDirectory = downloadPath.toFile().isDirectory();
    if (!isDirectory)
    {
      log.error("Download path must be a directory!");
      System.exit(1);
    }

    try
    {
      SimpleLocalizeClient client = new SimpleLocalizeClient(apiKey, profile);
      client.downloadFile(downloadPath, downloadFormat);
    } catch (InterruptedException | IOException e)
    {
      log.error("Translations could not be downloaded");
      System.exit(1);
    }

  }
}
