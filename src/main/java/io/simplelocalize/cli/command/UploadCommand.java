package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TemplateKeys;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidatorUtil;
import io.simplelocalize.cli.exception.ConfigurationException;
import io.simplelocalize.cli.io.FileListReader;
import io.simplelocalize.cli.util.StringUtils;
import io.simplelocalize.cli.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
  private final FileListReader fileListReader = new FileListReader();
  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public UploadCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public void invoke() throws IOException, InterruptedException
  {
    validateConfiguration(configuration);

    final boolean isDryRun = Boolean.TRUE.equals(configuration.getDryRun());
    if (isDryRun)
    {
      log.info("Dry run mode enabled, no files will be uploaded");
    }

    String uploadPath = configuration.getUploadPath();
    if (SystemUtils.isWindows())
    {
      uploadPath = SystemUtils.convertToWindowsPath(uploadPath);
    }

    final String uploadFormat = configuration.getUploadFormat();
    log.info("File format: {}", uploadFormat);

    final String uploadLanguageKey = configuration.getUploadLanguageKey();
    boolean hasDefinedLanguageKey = StringUtils.isNotEmpty(uploadLanguageKey);
    if (hasDefinedLanguageKey)
    {
      log.info("Language: {}", uploadLanguageKey);
    }

    final String uploadNamespace = configuration.getUploadNamespace();
    boolean hasDefinedNamespace = StringUtils.isNotBlank(uploadNamespace);
    if (hasDefinedNamespace)
    {
      log.info("Namespace: {}", uploadNamespace);
    }

    final String uploadCustomerId = configuration.getUploadCustomerId();
    if (StringUtils.isNotEmpty(uploadCustomerId))
    {
      log.info("Customer ID: {}", uploadCustomerId);
    }

    final List<String> uploadOptions = configuration.getUploadOptions();
    log.info("Options: {}", uploadOptions);

    final List<FileToUpload> filesToUpload = fileListReader.findFilesToUpload(uploadPath);
    if (filesToUpload.size() > 1)
    {
      log.info("Found {} files matching the upload path", filesToUpload.size());
    }

    if (filesToUpload.isEmpty())
    {
      log.warn("No files found to upload! Please check the upload path: {}", uploadPath);
      return;
    }

    for (FileToUpload fileToUpload : filesToUpload)
    {
      final Path path = fileToUpload.path();
      final long length = path.toFile().length();
      if (length == 0)
      {
        log.warn("Skipping empty file = {}", path);
        continue;
      }

      final String effectiveLanguageKey = hasDefinedLanguageKey ? uploadLanguageKey : fileToUpload.language();
      final String effectiveNamespace = hasDefinedNamespace ? uploadNamespace : fileToUpload.namespace();

      final UploadRequest uploadRequest = UploadRequest.builder()
              .withPath(path)
              .withFormat(uploadFormat)
              .withLanguageKey(effectiveLanguageKey)
              .withNamespace(effectiveNamespace)
              .withCustomerId(uploadCustomerId)
              .withOptions(uploadOptions)
              .build();

      if (isDryRun)
      {
        log.info("[Dry run] Upload candidate = {}", path);
      } else
      {
        log.info("Uploading file = {}", path);
      }

      if (StringUtils.isNotEmpty(fileToUpload.language()))
      {
        log.info("- Language = {}", effectiveLanguageKey);
      }
      if (StringUtils.isNotEmpty(fileToUpload.namespace()))
      {
        log.info("- Namespace = {}", effectiveNamespace);
      }

      if (!isDryRun)
      {
        client.uploadFile(uploadRequest);
      }
    }

    if (isDryRun)
    {
      log.info("Dry run mode completed. Run the command without --dryRun flag to upload files.");
    } else
    {
      log.info("Upload to SimpleLocalize completed");
    }
  }

  private void validateConfiguration(Configuration configuration)
  {
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(configuration.getUploadFormat(), "uploadFormat");
    final String uploadPath = configuration.getUploadPath();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(uploadPath, "uploadPath");

    final String uploadLanguageKey = configuration.getUploadLanguageKey();
    final boolean hasUploadLanguageKey = StringUtils.isNotBlank(uploadLanguageKey);

    final boolean hasLanguagePlaceholder = uploadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY);
    if (hasLanguagePlaceholder && hasUploadLanguageKey)
    {
      throw new ConfigurationException("You cannot use {lang} placeholder in uploadPath and language key parameter at the same time");
    }

    final boolean hasNamespacePlaceholder = uploadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY);
    if (hasNamespacePlaceholder && hasUploadLanguageKey)
    {
      throw new ConfigurationException("You cannot use {ns} placeholder in uploadPath and namespace parameter at the same time");
    }

    final boolean isMultiLanguage = isMultiLanguage(configuration);
    if (isMultiLanguage && hasUploadLanguageKey)
    {
      throw new ConfigurationException("You cannot use language key parameter with multi-language file formats");
    }
  }

  private boolean isMultiLanguage(Configuration configuration)
  {
    List<String> multiLanguageFileFormats = List.of("multi-language-json", "excel", "csv-translations");
    for (String uploadFormat : multiLanguageFileFormats)
    {
      if (uploadFormat.equalsIgnoreCase(configuration.getUploadFormat()))
      {
        return true;
      }
    }

    List<String> uploadOptions = configuration.getUploadOptions();
    for (String uploadOption : uploadOptions)
    {
      if (uploadOption.equalsIgnoreCase("MULTI_LANGUAGE"))
      {
        return true;
      }
    }
    return false;
  }
}
