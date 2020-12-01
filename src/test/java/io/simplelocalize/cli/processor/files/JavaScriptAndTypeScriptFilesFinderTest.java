package io.simplelocalize.cli.processor.files;


import io.simplelocalize.cli.exception.ProjectProcessException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

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

  @Test(expected = ProjectProcessException.class)
  public void shouldThrowWhenFileNotFound() throws Exception {
    //given
    Path path = Paths.get("some path");

    //when
    javaScriptAndTypeScriptFilesFinder.findFilesToProcess(path);

    //then
  }
}
