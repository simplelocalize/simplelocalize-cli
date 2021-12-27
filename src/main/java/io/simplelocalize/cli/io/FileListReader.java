package io.simplelocalize.cli.io;

import com.google.common.collect.Lists;
import io.simplelocalize.cli.client.dto.FileToUpload;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileListReader
{

  public static final String LANGUAGE_TEMPLATE_KEY = "{lang}";


  public List<FileToUpload> getMatchingFilesToUpload(Path uploadPathWithTemplateKey, String templateKey) throws IOException
  {
    List<FileToUpload> output = Lists.newArrayList();
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

  public List<FileToUpload> getFilesToUploadByUploadFormat(Path configurationUploadPath, String uploadFormat) throws IOException
  {
    String fileExtension = ".json";
    List<FileToUpload> output = Lists.newArrayList();

    try (Stream<Path> foundFilesStream = Files.walk(configurationUploadPath, 10))
    {
      var foundPaths = foundFilesStream.collect(Collectors.toList());
      var foundFiles = foundPaths.stream()
              .filter(Files::isRegularFile)
              .filter(path -> path.toString().endsWith(fileExtension))
              .collect(Collectors.toList());
      for (Path foundFile : foundFiles)
      {
        output.add(FileToUpload.of(foundFile, null));
      }
      return output;
    }
  }
}
