package io.simplelocalize.cli.extraction.keys;

import io.simplelocalize.cli.io.FileContentReader;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DefaultKeysExtractor
{
  private DefaultKeysExtractor()
  {

  }

  public static Set<String> extractValuesByPattern(Path filePath, Pattern pattern)
  {
    String fileContent = FileContentReader.tryReadContent(filePath);
    String fileContentInOneLine = FileContentReader.transformTextToOneLine(fileContent);
    return extractValuesByPattern(fileContentInOneLine, pattern);
  }

  public static Set<String> extractValuesByPattern(String fileContentInOneLine, Pattern pattern)
  {
    return pattern
            .matcher(fileContentInOneLine)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }

}
