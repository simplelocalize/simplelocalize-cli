package io.simplelocalize.cli.extraction.files;


import io.simplelocalize.cli.TestResourcesUtility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavaScriptAndTypeScriptFilesFinderTest {

  private final JavaScriptAndTypeScriptFilesFinder javaScriptAndTypeScriptFilesFinder = new JavaScriptAndTypeScriptFilesFinder();

  @Test
  public void shouldFindFiles() throws Exception {
    //given
    Path path = TestResourcesUtility.read("react-intl");

    //when
    List<Path> files = javaScriptAndTypeScriptFilesFinder.findFilesToProcess(path);

    //then
    Assertions.assertThat(files).hasSize(5);
  }

  @Test
  public void shouldThrowWhenFileNotFound() throws Exception {
    //given
    Path path = Paths.get("some invalid path");

    //when
    Assertions.assertThatThrownBy(() -> javaScriptAndTypeScriptFilesFinder.findFilesToProcess(path));

    //then
  }
}
