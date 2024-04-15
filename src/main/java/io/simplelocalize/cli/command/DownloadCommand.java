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
            .withCustomerId(customerId)
            .withLanguageKey(languageKey)
            .withSort(sort)
            .build();
    log.info("File format: {}", downloadFormat);
    if (StringUtils.isNotEmpty(customerId))
    {
      log.info("Customer ID: {}", customerId);
    }

    if (StringUtils.isNotEmpty(namespace))
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
    for (DownloadableFile downloadableFile : downloadableFiles)
    {
      client.downloadFile(downloadableFile, downloadPath);
    }
    log.info("Downloaded {} files from SimpleLocalize", downloadableFiles.size());
  }

}
