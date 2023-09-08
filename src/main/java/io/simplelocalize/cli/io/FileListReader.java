package io.simplelocalize.cli.io;

import io.simplelocalize.cli.client.dto.FileToUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.simplelocalize.cli.TemplateKeys.LANGUAGE_TEMPLATE_KEY;
import static io.simplelocalize.cli.TemplateKeys.NAMESPACE_TEMPLATE_KEY;

public class FileListReader
{

  public List<FileToUpload> findFilesToUpload(String uploadPath) throws IOException
  {
    Path parentDirectory = getParentDirectory(uploadPath);
    return findMatchingFiles(parentDirectory, uploadPath);
  }


  private List<FileToUpload> findMatchingFiles(Path parentDirectory, String pattern) throws IOException
  {
    try (Stream<Path> walk = Files.walk(parentDirectory))
    {
      return walk
              .map(file -> {
                String fileName = file.toFile().getPath();
                if (!Files.isRegularFile(Path.of(fileName)))
                {
                  return null;
                }
                Matcher matcher = getMatcher(fileName, pattern);
                if (matcher.matches())
                {
                  String lang = getGroupOrNull("lang", matcher);
                  String ns = getGroupOrNull("ns", matcher);
                  return FileToUpload.FileToUploadBuilder.builder()
                          .withLanguage(lang)
                          .withNamespace(ns)
                          .withPath(file).build();
                }
                return null;
              })
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
    }
  }

  private String getGroupOrNull(String group, Matcher matcher)
  {
    try
    {
      return matcher.group(group);
    } catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  private Matcher getMatcher(String input, String pattern)
  {
    String replace = pattern
            .replace("\\", "\\\\") // escape backslash
            .replace(NAMESPACE_TEMPLATE_KEY, "(?<ns>.*)")
            .replace(LANGUAGE_TEMPLATE_KEY, "(?<lang>.*)");
    Pattern regex = Pattern.compile(replace);
    return regex.matcher(input);
  }



  private Path getParentDirectory(String uploadPathPattern)
  {
    int length = uploadPathPattern.length();
    for (int i = 0; i < length; i++)
    {
      Path outputPath = Path.of(uploadPathPattern);
      if (Files.exists(outputPath))
      {
        return outputPath;
      }
      uploadPathPattern = uploadPathPattern.substring(0, uploadPathPattern.length() - 1);
    }
    throw new IllegalStateException("Unable to find parent directory for upload path");
  }

}
