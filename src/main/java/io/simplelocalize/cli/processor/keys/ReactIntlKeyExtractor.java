package io.simplelocalize.cli.processor.keys;

import com.google.common.collect.Sets;
import io.simplelocalize.cli.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReactIntlKeyExtractor implements KeyExtractor {

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {

    String fileContent = FileReaderUtil.tryReadContent(filePath);
    fileContent = fileContent.replaceAll("\n", " ");
    fileContent = fileContent.replaceAll("\\s+", " ");

    Set<String> formattedMessageIds = matchFormattedMessageIds(fileContent);
    Set<String> defineMessageIds = matchDefineMessageIds(fileContent);
    return Sets.union(formattedMessageIds, defineMessageIds);

  }

  private Set<String> matchDefineMessageIds(String fileContent) {
    fileContent = fileContent.replaceAll("\\s+", "");
    return Pattern.compile("(?<=defineMessages\\(\\{id:\")(.*?)(?=\")")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }

  private Set<String> matchFormattedMessageIds(String fileContent) {
    return Pattern.compile("(?<=<FormattedMessage id=\")(.*?)(?=\")")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }
}
