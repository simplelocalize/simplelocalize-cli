package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.io.FileListReader;
import io.simplelocalize.cli.util.WindowsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.simplelocalize.cli.client.dto.UploadRequest.UploadFileRequestBuilder.anUploadFileRequest;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
  private static final List<String> MULTI_LANGUAGE_FORMATS = List.of("multi-language-json", "excel", "csv-translations");
  private final FileListReader fileListReader;
  private final SimpleLocalizeClient client;
  private final Configuration configuration;
  private final ConfigurationValidator configurationValidator;

  public UploadCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
    this.fileListReader = new FileListReader();
    this.configurationValidator = new ConfigurationValidator();
  }

  public void invoke() throws IOException, InterruptedException
  {
    configurationValidator.validateUploadConfiguration(configuration);
    String uploadPath = configuration.getUploadPath();
    if (WindowsUtils.isWindows())
    {
      uploadPath = WindowsUtils.convertToWindowsPath(uploadPath);
    }

    List<FileToUpload> filesToUpload;
    try
    {
      filesToUpload = fileListReader.findFilesToUpload(uploadPath);
    } catch (IOException e)
    {
      log.error("Matching files could not be found at {}", uploadPath, e);
      throw new IllegalArgumentException("Matching files could not be found", e);
    }

    log.info("Found {} files to upload", filesToUpload.size());
    for (FileToUpload fileToUpload : filesToUpload)
    {
      long length = fileToUpload.getPath().toFile().length();
      if (length == 0)
      {
        log.warn("Skipping empty file: {}", fileToUpload.getPath());
        continue;
      }

      String fileLanguageKey = Optional.of(fileToUpload).map(FileToUpload::getLanguage).orElse("");
      boolean hasFileLanguageKey = StringUtils.isNotBlank(fileLanguageKey);

      String configurationLanguageKey = configuration.getLanguageKey();
      boolean hasConfigurationLanguageKey = StringUtils.isNotBlank(configurationLanguageKey);

      boolean isLanguageMatching = fileLanguageKey.equals(configurationLanguageKey);
      if (hasFileLanguageKey && hasConfigurationLanguageKey && !isLanguageMatching)
      {
        log.info("Skipping '{}' language, file: {}", fileToUpload.getLanguage(), fileToUpload.getPath());
        continue;
      }

      String requestLanguageKey = fileLanguageKey;
      if (hasConfigurationLanguageKey && !hasFileLanguageKey)
      {
        requestLanguageKey = configurationLanguageKey;
      }

      boolean isMultiLanguageFormat = isMultiLanguageFormat(configuration.getUploadFormat());
      if (!hasFileLanguageKey && !hasConfigurationLanguageKey && !isMultiLanguageFormat)
      {
        log.info("Language key not present in '--uploadPath' nor '--languageKey' parameter, file: {}", fileToUpload.getPath());
      }

      String uploadFormat = configuration.getUploadFormat();
      String customerId = configuration.getCustomerId();
      List<String> uploadOptions = configuration.getUploadOptions();
      UploadRequest uploadRequest = anUploadFileRequest()
              .withPath(fileToUpload.getPath())
              .withLanguageKey(requestLanguageKey)
              .withNamespace(fileToUpload.getNamespace())
              .withFormat(uploadFormat)
              .withCustomerId(customerId)
              .withOptions(uploadOptions)
              .build();

      log.info("Uploading {}", uploadPath);
      client.uploadFile(uploadRequest);
    }
    log.info("Uploaded {} files to SimpleLocalize", filesToUpload.size());
  }

  private boolean isMultiLanguageFormat(String inputUploadFormat)
  {
    for (String uploadFormat : MULTI_LANGUAGE_FORMATS)
    {
      if (uploadFormat.equalsIgnoreCase(inputUploadFormat))
      {
        return true;
      }
    }
    return false;
  }
}
