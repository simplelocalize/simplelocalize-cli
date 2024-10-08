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
    log.info("Path: {}", uploadPath);

    final String uploadFormat = configuration.getUploadFormat();
    log.info("File format: {}", uploadFormat);

    final String uploadLanguageKey = configuration.getUploadLanguageKey();
    final boolean hasDefinedLanguageKey = StringUtils.isNotEmpty(uploadLanguageKey);
    if (hasDefinedLanguageKey)
    {
      log.info("Language: {}", uploadLanguageKey);
    }

    final String uploadNamespace = configuration.getUploadNamespace();
    final boolean hasDefinedNamespace = StringUtils.isNotBlank(uploadNamespace);
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
    if (!uploadOptions.isEmpty())
    {
      log.info("Options: {}", uploadOptions);
    }

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

      final String fileLanguage = fileToUpload.language();
      final String effectiveLanguageKey = hasDefinedLanguageKey ? uploadLanguageKey : fileLanguage;

      final String fileNamespace = fileToUpload.namespace();
      final String effectiveNamespace = hasDefinedNamespace ? uploadNamespace : fileNamespace;

      final UploadRequest uploadRequest = UploadRequest.builder()
              .withPath(path)
              .withFormat(uploadFormat)
              .withLanguageKey(effectiveLanguageKey)
              .withNamespace(effectiveNamespace)
              .withCustomerId(uploadCustomerId)
              .withOptions(uploadOptions)
              .build();

      String logMessage = "";
      if (isDryRun)
      {
        logMessage += "Upload candidate {}";
      } else
      {
        logMessage += "Uploading {}";
      }

      if (StringUtils.isNotEmpty(fileLanguage))
      {
        logMessage += " (language: " + fileLanguage + ")";
      }

      if (StringUtils.isNotEmpty(fileNamespace))
      {
        logMessage += " (namespace: " + fileNamespace + ")";
      }

      log.info(logMessage, path);
      if (isDryRun)
      {
        continue;
      }
      client.uploadFile(uploadRequest);
    }

    if (isDryRun)
    {
      log.info("Dry run mode completed. Run the command without --dryRun flag to upload files.");
    } else
    {
      log.info("Successfully uploaded all files");
    }
  }

  private void validateConfiguration(Configuration configuration)
  {
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(configuration.getUploadFormat(), "uploadFormat");
    final String uploadPath = configuration.getUploadPath();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(uploadPath, "uploadPath");

    final boolean hasLanguagePlaceholder = uploadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY);
    final String uploadLanguageKey = configuration.getUploadLanguageKey();
    final boolean hasUploadLanguageKey = StringUtils.isNotBlank(uploadLanguageKey);
    if (hasLanguagePlaceholder && hasUploadLanguageKey)
    {
      throw new ConfigurationException("You cannot use {lang} placeholder in uploadPath and language key parameter at the same time");
    }

    final boolean hasNamespacePlaceholder = uploadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY);
    final String uploadNamespace = configuration.getUploadNamespace();
    final boolean hasUploadNamespace = StringUtils.isNotBlank(uploadNamespace);
    if (hasNamespacePlaceholder && hasUploadNamespace)
    {
      throw new ConfigurationException("You cannot use {ns} placeholder in uploadPath and namespace parameter at the same time");
    }

    final boolean isMultiLanguage = isMultiLanguage(configuration);
    if (isMultiLanguage && hasLanguagePlaceholder)
    {
      throw new ConfigurationException("You cannot use language key parameter with multi-language file formats");
    }
  }

  private boolean isMultiLanguage(Configuration configuration)
  {
    final List<String> multiLanguageFileFormats = List.of("multi-language-json", "excel", "csv-translations");
    for (String uploadFormat : multiLanguageFileFormats)
    {
      if (uploadFormat.equalsIgnoreCase(configuration.getUploadFormat()))
      {
        return true;
      }
    }

    final List<String> uploadOptions = configuration.getUploadOptions();
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
