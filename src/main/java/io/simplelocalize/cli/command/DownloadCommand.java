package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TemplateKeys;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.ExportRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.DownloadableFile;
import io.simplelocalize.cli.configuration.ConfigurationValidatorUtil;
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
    final String downloadFormat = configuration.getDownloadFormat();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(downloadFormat, "downloadFormat");

    final String downloadPath = configuration.getDownloadPath();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(downloadPath, "downloadPath");

    log.info("Download path: {}", downloadPath);
    log.info("File format: {}", downloadFormat);

    final List<String> languageKeys = configuration.getDownloadLanguageKeys();
    log.info("Language key(s): {}", languageKeys);

    final String sort = configuration.getDownloadSort();
    if (StringUtils.isNotEmpty(sort))
    {
      log.info("Sort: {}", sort);
    }

    final String namespace = configuration.getDownloadNamespace();
    if (StringUtils.isNotEmpty(namespace))
    {
      log.info("Namespace: {}", namespace);
    }

    final String customerId = configuration.getDownloadCustomerId();
    if (StringUtils.isNotEmpty(customerId))
    {
      log.info("Customer ID: {}", customerId);
    }

    final List<String> downloadOptions = new ArrayList<>(configuration.getDownloadOptions());
    if (downloadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_NAMESPACES");
    }

    if (downloadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_LANGUAGES");
    }

    log.info("Options: {}", downloadOptions);

    final ExportRequest exportRequest = ExportRequest.builder()
            .withFormat(downloadFormat)
            .withLanguageKeys(languageKeys)
            .withNamespace(namespace)
            .withCustomerId(customerId)
            .withOptions(downloadOptions)
            .withSort(sort)
            .build();
    final List<DownloadableFile> downloadableFiles = client.exportFiles(exportRequest);
    int downloadedFilesCounter = 0;
    for (DownloadableFile downloadableFile : downloadableFiles)
    {
      client.downloadFile(downloadableFile, downloadPath);
      downloadedFilesCounter++;
    }
    log.info("Downloaded {} file(s) from SimpleLocalize", downloadedFilesCounter);
  }

}
