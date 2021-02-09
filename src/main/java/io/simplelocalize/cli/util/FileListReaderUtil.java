package io.simplelocalize.cli.util;

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

/**
 * Feel free to use or extend this utility
 */
public class FileListReaderUtil
{

  public static final String LANGUAGE_TEMPLATE_KEY = "{lang}";


  private FileListReaderUtil()
  {

  }

  public static List<FileToUpload> getMatchingFilesToUpload(Path uploadPathWithTemplateKey, String templateKey) throws IOException
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
        String removedPathSeparator = StringUtils.remove(removedSuffix, File.separator);
        output.add(FileToUpload.of(foundFile, removedPathSeparator));
      }
      return output;
    }
  }

}
