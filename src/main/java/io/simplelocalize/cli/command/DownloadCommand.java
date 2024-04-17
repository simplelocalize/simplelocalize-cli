package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TemplateKeys;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import io.simplelocalize.cli.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    String namespace = configuration.getNamespace();
    String sort = configuration.getDownloadSort();
    List<String> downloadOptions = new ArrayList<>(configuration.getDownloadOptions());

    if (downloadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_NAMESPACES");
    }

    if (downloadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_LANGUAGES");
    }

    DownloadRequest downloadRequest = DownloadRequest.builder()
            .withFormat(downloadFormat)
            .withOptions(downloadOptions)
            .withNamespace(namespace)
            .withCustomerId(customerId)
            .withLanguageKey(languageKey)
            .withSort(sort)
            .build();
    log.info("File format: {}", downloadFormat);
    if (StringUtils.isNotEmpty(customerId))
    {
      log.info("Customer ID: {}", customerId);
    }

    boolean hasNamespace = StringUtils.isNotEmpty(namespace);
    if (hasNamespace)
    {
      log.info("Namespace: {}", namespace);
    }

    if (StringUtils.isNotEmpty(languageKey))
    {
      log.info("Language key: {}", languageKey);
    }
    if (StringUtils.isNotEmpty(sort))
    {
      log.info("Sort: {}", sort);
    }
    log.info("Download options: {}", downloadOptions);
    List<DownloadableFile> downloadableFiles = client.fetchDownloadableFiles(downloadRequest);
    int downloadedFilesCounter = 0;
    for (DownloadableFile downloadableFile : downloadableFiles)
    {
      boolean isNamespaceMatches = Objects.equals(downloadableFile.namespace(), namespace);
      if (hasNamespace && !isNamespaceMatches)
      {
        log.info("Skipping file download for namespace = {}", downloadableFile.namespace());
        continue;
      }
      client.downloadFile(downloadableFile, downloadPath);
      downloadedFilesCounter++;
    }
    log.info("Downloaded {} file(s) from SimpleLocalize", downloadedFilesCounter);
  }

}
