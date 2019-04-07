package io.simplelocalize.cli.processor.keys;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ReactIntlKeyExtractorTest {

  private ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Test
  public void shouldExtractKeysFromLines() throws Exception {
    //given
    Path path = Paths.get("react-intl/UserPage.js");

    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromFile(path);

    //then
    Assertions.assertThat(keys).hasSize(8);
    Assertions.assertThat(keys).contains("VISITS", "LIKES", "CREATED_COUNT", "COMMENTS");
  }
}
