package io.simplelocalize.cli.processor.keys;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

public class ReactIntlKeyExtractorTest {

  private ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Test
  public void shouldExtractKeysFromLines() throws Exception {
    //given
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL resource = classLoader.getResource("react-intl/UserPage.js");
    String path = resource.getPath();

    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(keys).hasSize(8);
    Assertions.assertThat(keys).contains("VISITS", "LIKES", "CREATED_COUNT", "COMMENTS");
  }
}
