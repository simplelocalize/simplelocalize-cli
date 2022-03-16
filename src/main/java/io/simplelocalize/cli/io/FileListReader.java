package io.simplelocalize.cli.io;

import io.micronaut.core.util.AntPathMatcher;
import io.simplelocalize.cli.client.dto.FileToUpload;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.simplelocalize.cli.TemplateKeys.LANGUAGE_TEMPLATE_KEY;
import static io.simplelocalize.cli.TemplateKeys.NAMESPACE_TEMPLATE_KEY;

public class FileListReader
{

  public List<FileToUpload> findFilesToUpload(String uploadPath) throws IOException
  {
    List<FileToUpload> output = new ArrayList<>();

    String beforeTemplatePart = getParentDirectory(uploadPath);
    Path parentDir = Path.of(beforeTemplatePart);

    boolean exists = Files.exists(parentDir);
    if (!exists)
    {
      String parentDirectory = StringUtils.substringBeforeLast(beforeTemplatePart, File.separator);
      parentDir = Path.of(parentDirectory);
    }

    try (Stream<Path> foundFilesStream = Files.walk(parentDir, 6))
    {
      AntPathMatcher antPathMatcher = new AntPathMatcher();
      String uploadPathPattern = uploadPath
              .replace(LANGUAGE_TEMPLATE_KEY, "**")
              .replace(NAMESPACE_TEMPLATE_KEY, "**");
      var foundFiles = foundFilesStream
              .filter(Files::isRegularFile)
              .filter(path -> antPathMatcher.matches(uploadPathPattern, path.toString()))
              .collect(Collectors.toList());
      for (Path foundFile : foundFiles)
      {
        String languageKey = extractTemplateValue(uploadPath, foundFile, LANGUAGE_TEMPLATE_KEY);
        String pathWithLanguage = uploadPath.replace(LANGUAGE_TEMPLATE_KEY, languageKey);
        String namespace = extractTemplateValue(pathWithLanguage, foundFile, NAMESPACE_TEMPLATE_KEY);
        if (StringUtils.isNotBlank(namespace))
        {
          String pathWithNamespace = uploadPath.replace(NAMESPACE_TEMPLATE_KEY, namespace);
          languageKey = extractTemplateValue(pathWithNamespace, foundFile, LANGUAGE_TEMPLATE_KEY);
        }
        FileToUpload fileToUpload = FileToUpload.FileToUploadBuilder.aFileToUpload()
                .withLanguage(StringUtils.trimToNull(languageKey))
                .withNamespace(StringUtils.trimToNull(namespace))
                .withPath(foundFile).build();
        output.add(fileToUpload);
      }
      return output;
    }
  }

  private String getParentDirectory(String uploadPath)
  {
    int languageTemplateKeyPosition = uploadPath.indexOf(LANGUAGE_TEMPLATE_KEY);
    int namespaceTemplateKeyPosition = uploadPath.indexOf(NAMESPACE_TEMPLATE_KEY);
    String[] splitUploadPath = StringUtils.splitByWholeSeparator(uploadPath, LANGUAGE_TEMPLATE_KEY);
    if (namespaceTemplateKeyPosition > 0 && languageTemplateKeyPosition > 0)
    {
      if (languageTemplateKeyPosition < namespaceTemplateKeyPosition)
      {
        splitUploadPath = StringUtils.splitByWholeSeparator(uploadPath, LANGUAGE_TEMPLATE_KEY);
      } else
      {
        splitUploadPath = StringUtils.splitByWholeSeparator(uploadPath, NAMESPACE_TEMPLATE_KEY);
      }
    }

    if (splitUploadPath.length == 0)
    {
      throw new IllegalStateException("Unable to find parent directory for upload path");
    }

    return splitUploadPath[0];
  }

  private String extractTemplateValue(String uploadPath, Path file, String templateKey)
  {
    if (!uploadPath.contains(templateKey))
    {
      return "";
    }
    String[] splitUploadPath = StringUtils.splitByWholeSeparator(uploadPath, templateKey);
    String beforeTemplatePart = splitUploadPath[0];
    String afterTemplatePart = splitUploadPath[1];
    String fileName = file.getFileName().toString();
    String output = StringUtils.remove(file.toString(), beforeTemplatePart);
    output = StringUtils.remove(output, afterTemplatePart);
    output = StringUtils.remove(output, fileName);
    output = output.replace(File.separator, "");
    output = StringUtils.remove(output, File.separator);
    return output.trim();
  }
}
