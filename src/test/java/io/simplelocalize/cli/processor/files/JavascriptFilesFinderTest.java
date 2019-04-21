package io.simplelocalize.cli.processor.files;


import io.simplelocalize.cli.exception.ProjectProcessException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavascriptFilesFinderTest {

  private JavascriptFilesFinder javascriptFilesFinder = new JavascriptFilesFinder();

  @Test
  public void shouldFindFiles() throws Exception {
    //given
    Path path = TestResourcesUtility.read("react-intl");

    //when
    List<Path> files = javascriptFilesFinder.findFilesToProcess(path);

    //then
    Assertions.assertThat(files).hasSize(4);
  }

  @Test(expected = ProjectProcessException.class)
  public void shouldThrowWhenFileNotFound() throws Exception {
    //given
    Path path = Paths.get("some path");

    //when
    javascriptFilesFinder.findFilesToProcess(path);

    //then
  }
}
