package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.io.FileListReader;
import io.simplelocalize.cli.util.StringUtils;
import io.simplelocalize.cli.util.WindowsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
  private final FileListReader fileListReader = new FileListReader();
  private final ConfigurationValidator configurationValidator = new ConfigurationValidator();
  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public UploadCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public void invoke() throws IOException, InterruptedException
  {
    configurationValidator.validateUploadConfiguration(configuration);
    String uploadPath = configuration.getUploadPath();
    boolean isDryRun = Boolean.TRUE.equals(configuration.getDryRun());

    if (WindowsUtils.isWindows())
    {
      uploadPath = WindowsUtils.convertToWindowsPath(uploadPath);
    }

    List<FileToUpload> filesToUpload = fileListReader.findFilesToUpload(uploadPath);
    String uploadFormat = configuration.getUploadFormat();
    String customerId = configuration.getCustomerId();
    List<String> uploadOptions = configuration.getUploadOptions();
    log.info("File format: {}", uploadFormat);
    if (StringUtils.isNotEmpty(customerId))
    {
      log.info("Customer ID: {}", customerId);
    }

    String configurationNamespace = configuration.getNamespace();
    if (StringUtils.isNotBlank(configurationNamespace))
    {
      log.info("Namespace: {}", configurationNamespace);
    }

    log.info("Upload options: {}", uploadOptions);

    log.info("Found {} files matching upload path '{}'", filesToUpload.size(), uploadPath);

    if (isDryRun)
    {
      log.info("Dry run mode enabled, no files will be uploaded");
    }

    int uploadedFilesCounter = 0;
    for (FileToUpload fileToUpload : filesToUpload)
    {
      Path path = fileToUpload.path();
      long length = path.toFile().length();
      if (length == 0)
      {
        log.warn("Skipping empty file = {}", path);
        continue;
      }

      String fileLanguageKey = Optional.of(fileToUpload).map(FileToUpload::language).orElse("");
      boolean hasFileLanguageKey = StringUtils.isNotBlank(fileLanguageKey);

      String configurationLanguageKey = configuration.getLanguageKey();
      boolean hasConfigurationLanguageKey = StringUtils.isNotBlank(configurationLanguageKey);

      boolean isLanguageMatching = fileLanguageKey.equals(configurationLanguageKey);
      String language = fileToUpload.language();
      if (hasFileLanguageKey && hasConfigurationLanguageKey && !isLanguageMatching)
      {
        log.info("Skipping '{}' language = {}", language, path);
        continue;
      }

      String effectiveLanguageKey = fileLanguageKey;
      if (hasConfigurationLanguageKey && !hasFileLanguageKey)
      {
        effectiveLanguageKey = configurationLanguageKey;
      }

      boolean isMultiLanguage = isMultiLanguage(configuration);
      if (!hasFileLanguageKey && !hasConfigurationLanguageKey && !isMultiLanguage)
      {
        log.info("Language key not present in '--uploadPath' nor '--languageKey' parameter = {}", path);
      }

      String effectiveNamespace = fileToUpload.namespace();
      if (StringUtils.isBlank(effectiveNamespace))
      {
        effectiveNamespace = configuration.getNamespace();
      }
      UploadRequest uploadRequest = UploadRequest.builder()
              .withPath(path)
              .withLanguageKey(effectiveLanguageKey)
              .withNamespace(effectiveNamespace)
              .withFormat(uploadFormat)
              .withCustomerId(customerId)
              .withOptions(uploadOptions)
              .build();

      if (isDryRun)
      {
        log.info("[Dry run] Found file to upload, language=[{}], namespace=[{}] = {}", effectiveLanguageKey, effectiveNamespace, path);
      } else
      {
        log.info("Uploading file, language=[{}] namespace=[{}] = {}", effectiveLanguageKey, effectiveNamespace, path);
        client.uploadFile(uploadRequest);
        uploadedFilesCounter++;
      }
    }

    if (!isDryRun)
    {
      log.info("Uploaded {} file(s) to SimpleLocalize", uploadedFilesCounter);
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
