package io.simplelocalize.cli.command;

import com.google.common.collect.Lists;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.util.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);

  public void invoke(Configuration configuration)
  {
    Path configurationUploadPath = configuration.getUploadPath();
    String apiKey = configuration.getApiKey();
    String profile = configuration.getProfile();
    String uploadLanguageKey = Optional.ofNullable(configuration.getLanguageKey()).orElse("default");

    SimpleLocalizeClient client = new SimpleLocalizeClient(apiKey, profile);

    List<FileToUpload> filesToUpload = Lists.newArrayList();
    if (configurationUploadPath.toFile().isDirectory())
    {
      log.error(" 😝 Upload path cannot be a directory!");
      System.exit(1);
    }

    final String languageTemplateKey = "{lang}";
    boolean fileNameWithTemplate = configurationUploadPath.toString().contains(languageTemplateKey);
    if (fileNameWithTemplate)
    {
      try
      {
        List<FileToUpload> foundMatchingFiles = FileReaderUtil.getMatchingFilesToUpload(configurationUploadPath, languageTemplateKey);
        filesToUpload.addAll(foundMatchingFiles);
      } catch (IOException e)
      {
        log.error(" 😝 Matching files could not be found", e);
        System.exit(1);
      }
    } else
    {
      filesToUpload.add(FileToUpload.of(configurationUploadPath, uploadLanguageKey));
    }

    for (FileToUpload fileToUpload : filesToUpload)
    {
      try
      {
        String language = fileToUpload.getLanguage();
        String uploadFormat = configuration.getUploadFormat();
        String uploadOptions = configuration.getUploadOptions();
        client.uploadFile(fileToUpload.getPath(), language, uploadFormat, uploadOptions);
      } catch (InterruptedException | IOException e)
      {
        log.warn(" 😝 File {} could not be uploaded", fileToUpload.getPath(), e);
      }
    }
  }
}
