package io.simplelocalize.cli.extraction.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BaseExtensionFilesFinder
{
  private final List<String> ignorePaths;


  public BaseExtensionFilesFinder()
  {
    this.ignorePaths = List.of();
  }

  public BaseExtensionFilesFinder(List<String> ignorePaths)
  {
    this.ignorePaths = ignorePaths == null ? List.of() : ignorePaths;
  }

  public List<Path> findFiles(Path path)
  {
    return findFilesWithExtension(path, "");
  }

  public List<Path> findFilesWithExtension(Path path, String extension)
  {
    try (Stream<Path> walk = Files.walk(path))
    {
      return walk
              .filter(Files::isRegularFile)
              .filter(isNotHidden())
              .filter(isNotIgnored(ignorePaths))
              .filter(extension.isEmpty() ? filterPath -> true : hasExtension(extension))
              .toList();
    } catch (IOException e)
    {
      throw new IllegalStateException("Could not process files at path: " + path, e);
    }
  }

  private Predicate<Path> hasExtension(String extension)
  {
    return filePath -> {
      String filename = filePath.toString();
      return filename.endsWith(extension);
    };
  }

  private Predicate<Path> isNotHidden()
  {
    return filePath -> {
      try
      {
        return !Files.isHidden(filePath);
      } catch (IOException e)
      {
        return true;
      }
    };
  }

  private Predicate<Path> isNotIgnored(List<String> ignoredList)
  {
    return filePath -> {
      for (String ignored : ignoredList)
      {
        Pattern compile = Pattern.compile(ignored);
        if (compile.matcher(filePath.toString()).find())
        {
          return false;
        }
      }
      return true;
    };
  }

}
