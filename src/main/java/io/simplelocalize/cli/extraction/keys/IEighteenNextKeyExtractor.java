package io.simplelocalize.cli.extraction.keys;

import io.simplelocalize.cli.io.FileContentReader;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IEighteenNextKeyExtractor implements KeyExtractor {

  @Override
  public Set<String> extractKeysFromFile(Path filePath)
  {
    String fileContent = FileContentReader.tryReadContent(filePath);
    fileContent = FileContentReader.transformTextToOneLine(fileContent);
    Set<String> output = new HashSet<>();
    Set<String> ts = matchT(fileContent);
    Set<String> i18nKeys = matchI18nKey(fileContent);
    output.addAll(ts);
    output.addAll(i18nKeys);
    return output;
  }

  private Set<String> matchI18nKey(String fileContent) {
    return Pattern.compile("(?<=i18nKey=[\"|'])(.*?)(?=[\"|'])")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }

  private Set<String> matchT(String fileContent) {
    return Pattern.compile("(?<=[^a-zA-Z]t\\([\"|'])(.*?)(?=[\"|'])")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }
}
