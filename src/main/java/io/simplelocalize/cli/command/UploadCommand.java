package io.simplelocalize.cli.command;

import com.google.common.collect.Lists;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.util.FileListReaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static io.simplelocalize.cli.util.FileListReaderUtil.LANGUAGE_TEMPLATE_KEY;

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

    boolean fileNameWithTemplate = configurationUploadPath.toString().contains(LANGUAGE_TEMPLATE_KEY);

    if (fileNameWithTemplate && StringUtils.isNotBlank(uploadLanguageKey))
    {
      log.error(" 😝 Please use 'languageKey' param OR '{lang}' variable in 'uploadPath' param!");
      System.exit(1);
    }

    if (fileNameWithTemplate)
    {
      try
      {
        List<FileToUpload> foundMatchingFiles = FileListReaderUtil.getMatchingFilesToUpload(configurationUploadPath, LANGUAGE_TEMPLATE_KEY);
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
