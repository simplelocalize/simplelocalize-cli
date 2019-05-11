package io.simplelocalize.cli.processor.keys;

import io.simplelocalize.cli.processor.keys.KeyExtractor;
import io.simplelocalize.cli.util.FileContentUtil;
import io.simplelocalize.cli.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AndroidXmlKeysExtractor implements KeyExtractor {


  @Override
  public Set<String> extractKeysFromFile(Path filePath) {
    String fileContent = FileReaderUtil.tryReadContent(filePath);
    fileContent = FileContentUtil.transformTextToOneLine(fileContent);
    return findAllRString(fileContent);
  }

  private Set<String> findAllRString(String fileContent) {
    return Pattern.compile("(?<=android:text=\"@string/)(.*?)(?=\")")
            .matcher(fileContent)
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toSet());
  }
}
