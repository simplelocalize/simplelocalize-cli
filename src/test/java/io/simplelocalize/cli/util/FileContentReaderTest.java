package io.simplelocalize.cli.util;

import io.simplelocalize.cli.io.FileContentReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileContentReaderTest
{

  @Test
  public void shouldReturnEmptyStringWhenFileNotFound()
  {
    //given
    Path given = Paths.get("some path");

    //when
    String result = FileContentReader.tryReadContent(given);

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void shouldTryReadContent()
  {
    //given
    String path = FileListReaderTest.class.getClassLoader().getResource("example-test.txt").getPath();

    //when
    String result = FileContentReader.tryReadContent(Paths.get(path));

    //then
    Assertions.assertThat(result).isNotEmpty();
  }

}
