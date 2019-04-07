package io.simplelocalize.cli.processor.keys;

import io.simplelocalize.cli.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ReactIntlKeyExtractor implements KeyExtractor {

  private static final String FORMATTED_MESSAGE_COMPONENT = "<FormattedMessage";
  private static final String DEFINE_MESSAGES_FUNCTION = "defineMessages(";

  @Override
  public Set<String> extractKeysFromFile(Path filePath) {

    List<String> fileLines = FileReaderUtil.tryReadLines(filePath);

    Set<String> outputKeys = new HashSet<>();
    for (int lineNumber = 0; lineNumber < fileLines.size(); lineNumber++) {
      String line = fileLines.get(lineNumber);
      if (!line.contains(FORMATTED_MESSAGE_COMPONENT) && !line.contains(DEFINE_MESSAGES_FUNCTION)) {
        continue;
      }

      for (int internalSearch = lineNumber; internalSearch < fileLines.size(); internalSearch++) {
        String localSearch = fileLines.get(internalSearch);
        Optional<String> foundIds = findKeyInLine(localSearch);
        foundIds.ifPresent(outputKeys::add);
        if (foundIds.isPresent()) {
          break;
        }
      }
    }
    return outputKeys;
  }

  private Optional<String> findKeyInLine(String line) {
    String extractIdWithKey = "id(=|: *)(\"|')(.*)(\"|')";
    Pattern pattern = Pattern.compile(extractIdWithKey);
    Matcher matcher = pattern.matcher(line);

    if (!matcher.find()) {
      return Optional.empty();
    }
    String foundKey = matcher.group();

    String idPrefix = "id(=|: *)";
    foundKey = foundKey.replaceAll(idPrefix, "");
    foundKey = foundKey.replaceAll("\"", "").replaceAll("'", "");
    foundKey = foundKey.trim();

    if (isNullOrEmpty(foundKey)) {
      return Optional.empty();
    }
    return Optional.of(foundKey);
  }
}
