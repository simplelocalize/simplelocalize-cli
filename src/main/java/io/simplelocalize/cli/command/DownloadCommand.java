package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TemplateKeys;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.configuration.Configuration;
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

  public void invoke() throws IOException, InterruptedException
  {
    String downloadPath = configuration.getDownloadPath();
    String downloadFormat = configuration.getDownloadFormat();
    String languageKey = configuration.getLanguageKey();
    String customerId = configuration.getCustomerId();
    List<String> downloadOptions = configuration.getDownloadOptions();

    if (downloadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_NAMESPACES");
    }


    if (downloadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_LANGUAGES");
    }

    DownloadRequest downloadRequest = aDownloadRequest()
            .withFormat(downloadFormat)
            .withOptions(downloadOptions)
            .withCustomerId(customerId)
            .withLanguageKey(languageKey)
            .build();
    log.info("Preparing files to download");
    List<DownloadableFile> downloadableFiles = client.fetchDownloadableFiles(downloadRequest);
    for (DownloadableFile downloadableFile : downloadableFiles)
    {
      client.downloadFile(downloadableFile, downloadPath);
    }
    log.info("Downloaded {} files from SimpleLocalize", downloadableFiles.size());
  }

}
