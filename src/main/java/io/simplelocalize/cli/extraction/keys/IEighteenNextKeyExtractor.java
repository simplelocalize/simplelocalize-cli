package io.simplelocalize.cli.extraction.keys;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.util.FileContentUtil;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IEighteenNextKeyExtractor implements KeyExtractor {

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {
    String fileContent = FileContentUtil.tryReadContent(filePath);
    fileContent = FileContentUtil.transformTextToOneLine(fileContent);
    Set<String> ts = matchT(fileContent);
    Set<String> i18nKeys = matchI18nKey(fileContent);
    return Sets.union(ts,i18nKeys);
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
