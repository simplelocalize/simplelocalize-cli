package io.simplelocalize.cli.io;

import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.command.UploadCommand;
import io.simplelocalize.cli.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileListReader
{

  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
  public static final String LANGUAGE_TEMPLATE_KEY = "{lang}";

  public List<FileToUpload> findFilesWithTemplateKey(Path uploadPathWithTemplateKey, String templateKey) throws IOException
  {
    List<FileToUpload> output = new ArrayList();
    String filePathWithTemplate = uploadPathWithTemplateKey.toString();

    String[] splitUploadPath = StringUtils.splitByWholeSeparator(filePathWithTemplate, templateKey);
    String firstPart = splitUploadPath[0];
    String fileName = StringUtils.substringAfterLast(firstPart, File.separator);
    String secondPart = splitUploadPath[1];

    Path parentDir = Path.of(firstPart);

    boolean exists = Files.exists(parentDir);
    if (!exists)
    {
      String parentDirectory = StringUtils.substringBeforeLast(firstPart, File.separator);
      parentDir = Path.of(parentDirectory);
    }

    try (Stream<Path> foundFilesStream = Files.walk(parentDir, 10))
    {
      var foundPaths = foundFilesStream.collect(Collectors.toList());
      var foundFiles = foundPaths.stream()
              .filter(Files::isRegularFile)
              .filter(path -> path.toString().endsWith(secondPart) && path.toString().contains(fileName))
              .collect(Collectors.toList());
      for (Path foundFile : foundFiles)
      {
        String foundFilePathString = foundFile.toString();
        String removedPrefix = StringUtils.remove(foundFilePathString, firstPart);
        String removedSuffix = StringUtils.remove(removedPrefix, secondPart);
        String language = StringUtils.remove(removedSuffix, File.separator);
        output.add(FileToUpload.of(foundFile, language));
      }
      return output;
    }
  }

  public List<FileToUpload> findFilesForMultiFileUpload(Configuration configuration) throws IOException
  {
    Path configurationUploadPath = configuration.getUploadPath();
    String uploadFormat = configuration.getUploadFormat();
    Set<String> ignorePaths = configuration.getIgnorePaths();

    if (!"multi-language-json".equals(uploadFormat))
    {
      log.error(" 😝 Currently, only 'multi-language-json' upload format is supported with 'MULTI_FILE' upload option");
      throw new UnsupportedOperationException();
    }
    String fileExtension = ".json";
    List<FileToUpload> output = new ArrayList();

    try (Stream<Path> foundFilesStream = Files.walk(configurationUploadPath, 10))
    {
      var foundPaths = foundFilesStream.collect(Collectors.toList());
      var foundFiles = foundPaths.stream()
              .filter(Files::isRegularFile)
              .filter(path -> !isIgnoredPath(ignorePaths, path))
              .filter(path -> path.toString().endsWith(fileExtension))
              .collect(Collectors.toList());
      for (Path foundFile : foundFiles)
      {
        output.add(FileToUpload.of(foundFile, null));
      }
      return output;
    }
  }

  private boolean isIgnoredPath(Set<String> ignorePaths, Path path)
  {
    String pathString = path.toString();
    for (String ignorePath : ignorePaths)
    {

      if (pathString.startsWith(ignorePath))
      {
        return true;
      }

      String globStyle = ignorePath.replaceAll(".\\*+", ".*");
      Pattern pattern = Pattern.compile(globStyle);
      Matcher matcher = pattern.matcher(pathString);
      boolean matches = matcher.matches();
      if (matches)
      {
        return true;
      }
    }
    return false;
  }
}
