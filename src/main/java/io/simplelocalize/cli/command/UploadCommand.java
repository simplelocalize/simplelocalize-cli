package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidator;
import io.simplelocalize.cli.configuration.Options;
import io.simplelocalize.cli.io.FileListReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.simplelocalize.cli.client.dto.UploadRequest.UploadFileRequestBuilder.anUploadFileRequest;
import static io.simplelocalize.cli.io.FileListReader.LANGUAGE_TEMPLATE_KEY;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
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

  public UploadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
    this.fileListReader = new FileListReader();
    this.configurationValidator = new ConfigurationValidator();
  }

  public void invoke()
  {
    configurationValidator.validateUploadConfiguration(configuration);
    String uploadPath = configuration.getUploadPath();

    List<FileToUpload> filesToUpload = List.of();
    try
    {
      filesToUpload = findMatchingFiles(configuration);
    } catch (IOException e)
    {
      log.error(" 😝 Matching files could not be found", e);
      System.exit(1);
    }

    log.info(" 📄  Found {} files to upload", filesToUpload.size());
    String languageKey = configuration.getLanguageKey();
    for (FileToUpload fileToUpload : filesToUpload)
    {
      try
      {
        String fileLanguageKey = Optional.of(fileToUpload).map(FileToUpload::getLanguage).orElse("");
        boolean hasLanguageKey = StringUtils.isNotBlank(languageKey);
        boolean isLanguageMatching = fileLanguageKey.equals(languageKey);
        if (!isLanguageMatching && hasLanguageKey)
        {
          log.info(" 🤔 Skipping '{}' language file: {}", fileToUpload.getLanguage(), fileToUpload.getPath());
          continue;
        }

        long length = fileToUpload.getPath().toFile().length();
        if (length == 0)
        {
          log.warn(" 🤔 Skipping empty file: {}", fileToUpload.getPath());
          continue;
        }

        String uploadFormat = configuration.getUploadFormat();
        List<String> uploadOptions = configuration.getUploadOptions();
        boolean isMultiFileUpload = uploadOptions.contains(Options.MULTI_FILE.name());
        if (isMultiFileUpload)
        {
          fileLanguageKey = null;
        }

        UploadRequest uploadRequest = anUploadFileRequest()
                .withLanguageKey(fileLanguageKey)
                .withPath(fileToUpload.getPath())
                .withFormat(uploadFormat)
                .withOptions(uploadOptions)
                .build();

        client.uploadFile(uploadRequest);
      } catch (IOException e)
      {
        log.warn(" 😝 Upload failed: {}", fileToUpload.getPath(), e);
      } catch (InterruptedException e)
      {
        log.error(" 😝 Upload interrupted: {}", fileToUpload.getPath(), e);
        Thread.currentThread().interrupt();
      }
    }
  }

  private List<FileToUpload> findMatchingFiles(Configuration configuration) throws IOException
  {
    String uploadPath = configuration.getUploadPath();
    boolean hasLanguageKeyInPath = uploadPath.contains(LANGUAGE_TEMPLATE_KEY);
    String uploadLanguageKey = configuration.getLanguageKey();
    if (hasLanguageKeyInPath)
    {
      return fileListReader.findFilesWithTemplateKey(uploadPath);
    }
    FileToUpload fileToUpload = FileToUpload.of(Paths.get(uploadPath), uploadLanguageKey);
    return Collections.singletonList(fileToUpload);
  }
}
