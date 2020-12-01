package io.simplelocalize.cli.processor.keys;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

public class ReactIntlKeyExtractorTest {

  private final ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Test
  public void shouldExtractKeysFromLines() throws Exception {
    //given
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL resource = classLoader.getResource("react-intl/UserPage.js");
    String path = resource.getPath();


    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(keys).hasSize(10);
    Assertions.assertThat(keys).containsExactlyInAnyOrder(
            "VISITS",
            "LIKES",
            "CREATED_COUNT",
            "COMMENTS",
            "COVER_IMAGE.PLACEHOLDER",
            "COVER_IMAGE.TITLE",
            "COVER_IMAGE.PLACEHOLDER_DOUBLE",
            "COVER_IMAGE.TITLE_DOUBLE",
            "ACHIEVEMENTS",
            "AVAILABLE_SOON"
    );
  }

  @Test
  public void shouldExtractKeysFromProblematicCase2() throws Exception {
    //given
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL resource = classLoader.getResource("react-intl/ProblematicCase-issue-2.js");
    String path = resource.getPath();

    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(keys).hasSize(2);
    Assertions.assertThat(keys).contains("SAVE", "CREATE");
  }

  @Test
  public void shouldExtractKeysFromProblematicCase3() throws Exception {
    //given
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL resource = classLoader.getResource("react-intl/ProblematicCase-issue-3.js");
    String path = resource.getPath();

    //when
    Set<String> keys = reactIntlKeyExtractor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(keys).hasSize(3);
    Assertions.assertThat(keys).contains("header-text", "header-subtitle", "header-button");
  }
}
