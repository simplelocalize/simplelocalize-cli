package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.configuration.Configuration;
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

  public DownloadCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public DownloadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
  }

  public void invoke()
  {
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
        List<DownloadableFile> downloadableFiles = client.fetchDownloadableFiles(downloadRequest);
        downloadableFiles
                .parallelStream()
                .forEach(downloadableFile -> client.downloadFile(downloadableFile, downloadPath));
        log.info(" 🎉 Download success!");
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
