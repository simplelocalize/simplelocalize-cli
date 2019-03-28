package io.simplelocalize.cli.processor.files;


import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

public class JavascriptFilesFinderTest {

  private JavascriptFilesFinder javascriptFilesFinder = new JavascriptFilesFinder();

  @Test
  public void shouldFindFiles() throws Exception {
    //given
    Path path = TestResourcesUtility.read("react-intl");

    //when
    List<Path> files = javascriptFilesFinder.findFiles(path);

    //then
    Assertions.assertThat(files).hasSize(3);
  }
}
