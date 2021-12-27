package io.simplelocalize.cli.command;

import com.google.common.collect.Lists;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.FileListReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static io.simplelocalize.cli.io.FileListReader.LANGUAGE_TEMPLATE_KEY;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);

  public void invoke(Configuration configuration)
  {
    Path configurationUploadPath = configuration.getUploadPath();
    String apiKey = configuration.getApiKey();
    String profile = configuration.getProfile();
    String uploadLanguageKey = configuration.getLanguageKey();

    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(apiKey, profile);
    FileListReader fileListReader = new FileListReader();

    boolean hasLanguageKeyInPath = configurationUploadPath.toString().contains(LANGUAGE_TEMPLATE_KEY);
    List<FileToUpload> filesToUpload = Lists.newArrayList();
    if (configurationUploadPath.toFile().isDirectory())
    {
      try
      {
        filesToUpload = fileListReader.getFilesToUploadByUploadFormat(configurationUploadPath, configuration.getUploadFormat());
      } catch (IOException e)
      {
        log.error(" 😝 Matching files could not be found", e);
        System.exit(1);
      }
    } else if (hasLanguageKeyInPath && StringUtils.isNotBlank(uploadLanguageKey))
    {
      log.error(" 😝 You cannot use '{lang}' param in upload path and '--languageKey' option together.");
      System.exit(1);
    } else if (hasLanguageKeyInPath)
    {
      try
      {
        filesToUpload = fileListReader.getMatchingFilesToUpload(configurationUploadPath, LANGUAGE_TEMPLATE_KEY);
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
