package io.simplelocalize.cli.processor.keys;

import io.simplelocalize.cli.processor.files.TestResourcesUtility;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class ReactIntlKeyExtractorTest {

  private ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Test
  public void shouldExtractKeysFromLines() throws Exception {
    //given
    List<String> fileLines = TestResourcesUtility.readFile("react-intl/UserPage.js");

    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromLines(fileLines);

    //then
    Assertions.assertThat(keys).hasSize(8);
    Assertions.assertThat(keys).contains("VISITS", "LIKES", "CREATED_COUNT", "COMMENTS");
  }
}
