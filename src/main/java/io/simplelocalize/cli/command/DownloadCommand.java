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
    final String downloadFormat = configuration.getDownloadFormat();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(downloadFormat, "downloadFormat");

    final String downloadPath = configuration.getDownloadPath();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(downloadPath, "downloadPath");

    log.info("Path: {}", downloadPath);
    log.info("File format: {}", downloadFormat);

    final List<String> languageKeys = configuration.getDownloadLanguageKeys();
    if (!languageKeys.isEmpty())
    {
      log.info("Languages: {}", languageKeys);
    }

    final List<String> tags = Objects.requireNonNullElse(configuration.getDownloadTags(), List.of());
    if (!tags.isEmpty())
    {
      log.info("Tags: {}", tags);
    }

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

    if (downloadPath.contains(TemplateKeys.CUSTOMER_KEY_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_CUSTOMERS");
    }

    if (!downloadOptions.isEmpty())
    {
      log.info("Options: {}", downloadOptions);
    }

    final ExportRequest exportRequest = ExportRequest.builder()
            .withFormat(downloadFormat)
            .withLanguageKeys(languageKeys)
            .withNamespace(namespace)
            .withCustomerId(customerId)
            .withOptions(downloadOptions)
            .withSort(sort)
            .withTags(tags)
            .build();
    final List<DownloadableFile> downloadableFiles = client.exportFiles(exportRequest);
    for (DownloadableFile downloadableFile : downloadableFiles)
    {
      client.downloadFile(downloadableFile, downloadPath);
    }
    log.info("Successfully downloaded all files");
  }

}
