package io.simplelocalize.cli.io;

import io.micronaut.core.util.AntPathMatcher;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileListReader
{

  private static final Logger log = LoggerFactory.getLogger(FileListReader.class);
  public static final String LANGUAGE_TEMPLATE_KEY = "{lang}";

  public List<FileToUpload> findFilesWithTemplateKey(String filePathWithTemplate) throws IOException
  {
    List<FileToUpload> output = new ArrayList<>();

    String[] splitUploadPath = StringUtils.splitByWholeSeparator(filePathWithTemplate, LANGUAGE_TEMPLATE_KEY);
    String beforeTemplatePart = splitUploadPath[0];

    Path parentDir = Path.of(beforeTemplatePart);

    boolean exists = Files.exists(parentDir);
    if (!exists)
    {
      String parentDirectory = StringUtils.substringBeforeLast(beforeTemplatePart, File.separator);
      parentDir = Path.of(parentDirectory);
    }

    try (Stream<Path> foundFilesStream = Files.walk(parentDir, 3))
    {
      AntPathMatcher antPathMatcher = new AntPathMatcher();
      String pattern = filePathWithTemplate.replace(LANGUAGE_TEMPLATE_KEY, "**");
      var foundPaths = foundFilesStream.collect(Collectors.toList());
      var foundFiles = foundPaths.stream()
              .filter(Files::isRegularFile)
              .filter(path -> antPathMatcher.matches(pattern, path.toString()))
              .collect(Collectors.toList());
      for (Path foundFile : foundFiles)
      {
        String removedSuffix = retainOnlyLanguageKey(filePathWithTemplate, foundFile);
        String language = StringUtils.remove(removedSuffix, File.separator);
        output.add(FileToUpload.of(foundFile, language));
      }
      return output;
    }
  }

  private String retainOnlyLanguageKey(String filePathWithTemplate, Path foundFilePath)
  {
    String[] splitUploadPath = StringUtils.splitByWholeSeparator(filePathWithTemplate, LANGUAGE_TEMPLATE_KEY);
    String beforeTemplatePart = splitUploadPath[0];
    String afterTemplatePart = splitUploadPath[1];
    String fileName = foundFilePath.getFileName().toString();
    String output = StringUtils.remove(foundFilePath.toString(), beforeTemplatePart);
    output = StringUtils.remove(output, afterTemplatePart);
    output = StringUtils.remove(output, fileName);
    return output.replace(File.separator, "").trim();
  }

  public List<FileToUpload> findFilesForMultiFileUpload(Configuration configuration) throws IOException
  {
    String configurationUploadPath = configuration.getUploadPath();
    String uploadFormat = configuration.getUploadFormat();
    List<String> ignorePaths = configuration.getIgnorePaths();

    if (!"multi-language-json".equals(uploadFormat))
    {
      log.error(" 😝 Currently, only 'multi-language-json' upload format is supported with 'MULTI_FILE' upload option");
      throw new UnsupportedOperationException();
    }
    String fileExtension = ".json";
    List<FileToUpload> output = new ArrayList<>();

    try (Stream<Path> foundFilesStream = Files.walk(Paths.get(configurationUploadPath), 10))
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

  private boolean isIgnoredPath(List<String> ignorePaths, Path path)
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
