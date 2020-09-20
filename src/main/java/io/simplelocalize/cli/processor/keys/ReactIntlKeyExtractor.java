package io.simplelocalize.cli.processor.keys;

import io.simplelocalize.cli.util.FileContentUtil;
import io.simplelocalize.cli.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReactIntlKeyExtractor implements KeyExtractor {

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {
    String fileContent = FileReaderUtil.tryReadContent(filePath);
    fileContent = FileContentUtil.transformTextToOneLine(fileContent);

    Set<String> formattedMessageIds = matchFormattedMessageIds(fileContent);
    Set<String> formattedHTMLMessageIds = matchFormattedHTMLMessageIds(fileContent);
    Set<String> defineMessageIds = matchDefineMessageIds(fileContent);
    Set<String> intlFormatMessageIds = matchIntlFormatMessageIds(fileContent);
    return Stream.of(formattedMessageIds, formattedHTMLMessageIds, defineMessageIds, intlFormatMessageIds)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

  }

  private Set<String> matchDefineMessageIds(String fileContent) {
    fileContent = fileContent.replaceAll("\\s+", "");
    return Pattern.compile("(?<=defineMessages\\(\\{id:[\"|\'])(.*?)(?=[\"|\'])")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }

  private Set<String> matchFormattedHTMLMessageIds(String fileContent) {
    return Pattern.compile("(?<=<FormattedHTMLMessage id=\")(.*?)(?=\")")
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

  private Set<String> matchIntlFormatMessageIds(String fileContent) {
    fileContent = fileContent.replaceAll("\\s+", "");
    return Pattern.compile("(?<=intl\\.formatMessage\\(\\{id:[\"|'])(.*?)(?=[\"|'])")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }


}
